package com.scshop.products.productservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.google.common.collect.Lists;
import com.scshop.application.common.enums.InventoryStatus;
import com.scshop.application.common.enums.OrderEventType;
import com.scshop.application.common.model.OrderEvent;
import com.scshop.application.common.model.OrderItem;
import com.scshop.application.common.model.OrderSummary;
import com.scshop.application.common.model.Product;
import com.scshop.products.productservice.exception.ProductNotFoundException;
import com.scshop.products.productservice.exception.ProductOutOfStockException;
import com.scshop.products.productservice.repository.OrderSummaryRepository;
import com.scshop.products.productservice.repository.ProductRepository;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderSummaryRepository orderSummaryRepository;

	@Value("${app.order.kafka.topic.order-topic}")
	private String ORDER_TOPIC;

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	BiFunction<Integer, Integer, Integer> reduceInventoryFunction = (totalInvetory, inventoryToReduce) -> {
		if ((totalInvetory - inventoryToReduce) < 0) {
			throw new ProductOutOfStockException("Some products are out of stock");
		}
		return totalInvetory - inventoryToReduce;
	};

	BiFunction<Integer, Integer, Integer> restoreInventoryFunction = (totalInvetory, inventoryToRestore) -> {
		return totalInvetory + inventoryToRestore;
	};

	@KafkaListener(topics = "${app.order.kafka.topic.order-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void listen(@Payload OrderEvent orderEvent) {

		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- product-service -> update product inventory

		logger.info("\n\n ********** Received new order event... " + orderEvent + "********* \n\n");

		switch (orderEvent.getEventType()) {
		case ORDER_INITIATED:

			reduceProductInventory(orderEvent);
			break;

		case ORDER_ITEMS_OUTOFSTOCK:

			// Do nothing
			break;

		case ORDER_CANCELLED:

			restoreProductInventory(orderEvent);
			break;

		case ORDER_PAYMENT_FAILED:

			restoreProductInventory(orderEvent);
			break;

		case ORDER_CONFIRMED:

			// Do nothing
			break;

		default:
			break;
		}
	}

	@Transactional
	private void reduceProductInventory(OrderEvent orderEvent) {

		// Validation to make it idempotent - new or recovered Kafka consumer may consume since earliest
		Optional<OrderSummary> optional = orderSummaryRepository.findById(orderEvent.getOrderId());
		if (optional.isPresent()) {
			// Inventory for this order is already processed, do nothing
			return;
		}

		updateProductInventory(orderEvent, reduceInventoryFunction);

		//Update inventory processing status 
		OrderSummary orderSummary = new OrderSummary(orderEvent.getOrderId(), orderEvent.getUserId(),
				InventoryStatus.REDUCE_STOCK_UPDATE_PROCESSED);
		orderSummaryRepository.save(orderSummary);

	}

	@Transactional
	private void restoreProductInventory(OrderEvent orderEvent) {

		Optional<OrderSummary> optional = orderSummaryRepository.findById(orderEvent.getOrderId());
		if (!optional.isPresent()) {
			// Something is terribly wrong
			throw new RuntimeException("Order summary record is not present for orderId: " + orderEvent.getOrderId());
		}

		// Validation to make it idempotent
		OrderSummary orderSummary = optional.get();
		if (InventoryStatus.REDUCE_STOCK_UPDATE_INITIATED.equals(orderSummary.getInventoryStatus())
				|| InventoryStatus.RETURNED_STOCK_UPDATE_INITIATED.equals(orderSummary.getInventoryStatus())) {

			// Update inventory
			updateProductInventory(orderEvent, restoreInventoryFunction);

			// Update stock processing status
			if (InventoryStatus.REDUCE_STOCK_UPDATE_INITIATED.equals(orderSummary.getInventoryStatus())) {
				orderSummary.setInventoryStatus(InventoryStatus.REDUCE_STOCK_UPDATE_PROCESSED);
			} else {
				orderSummary.setInventoryStatus(InventoryStatus.RETURNED_STOCK_UPDATE_PROCESSED);
			}
			orderSummaryRepository.save(orderSummary);
		}

	}

	/**
	 * Update product inventory for new order, cancelled or payment failed products
	 * 
	 */
	private void updateProductInventory(OrderEvent orderEvent,
			BiFunction<Integer, Integer, Integer> inventoryFunction) {

		List<OrderItem> orderItems = orderEvent.getOrder().getItems();

		// Collect all product ids
		List<UUID> productIds = Lists.transform(orderItems, OrderItem::getProductId);

		// Retrieve all products
		List<Product> productListToUpdate = productRepository.findAllById(productIds);

		// Unusual case but still keep it
		if (productIds.size() != productListToUpdate.size()) {
			throw new ProductNotFoundException("Some product are not found for ids among : " + productIds);
		}

		// Create a map of productId to quantity for using it later
		Map<UUID, Integer> productIdToQuantityMap = orderItems.stream()
				.collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

		try {
			// Update inventory for all products - reduce/restore
			productListToUpdate.stream().forEach(productToUpdate -> {
				productToUpdate.setAvailableInventory(inventoryFunction.apply(productToUpdate.getAvailableInventory(),
						productIdToQuantityMap.get(productToUpdate.getId())));
			});

			// Save updated products
			productRepository.saveAll(productListToUpdate);

		} catch (ProductOutOfStockException ex) {

			logger.error("Some of the prducts are outofstock for order id: " + orderEvent.getOrderId());

			// Send OrderEventType.ORDER_ITEMS_OUTOFSTOCK event on kafka topic
			orderEvent.setEventType(OrderEventType.ORDER_ITEMS_OUTOFSTOCK);

			ListenableFuture<SendResult<String, OrderEvent>> future = kafkaTemplate.send(ORDER_TOPIC,
					orderEvent.getOrderId().toString(), orderEvent);

			logger.info("Sending ORDER_ITEMS_OUTOFSTOCKd event on topic ORDER_TOPIC...");
			future.addCallback(new ListenableFutureCallback<SendResult<String, OrderEvent>>() {

				@Override
				public void onSuccess(SendResult<String, OrderEvent> result) {
					logger.info(
							"ORDER_ITEMS_OUTOFSTOCK event SENT on topic ORDER_TOPIC for order id {0} and user id {1} ",
							new Object[] { orderEvent.getOrderId(), orderEvent.getUserId() });
				}

				@Override
				public void onFailure(Throwable ex) {
					logger.info(
							"ORDER_ITEMS_OUTOFSTOCK event send on ORDER_TOPIC FAILED for order id {0} and user id {1} ",
							new Object[] { orderEvent.getOrderId(), orderEvent.getUserId() });

					// TODO think what to do... kafka should retry or...let's see
					ex.printStackTrace();
				}

			});

		}

	}
}
