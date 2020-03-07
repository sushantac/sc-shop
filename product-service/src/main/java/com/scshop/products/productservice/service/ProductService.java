package com.scshop.products.productservice.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.scshop.application.common.model.OrderEvent;
import com.scshop.application.common.model.OrderItem;
import com.scshop.application.common.model.Product;
import com.scshop.products.productservice.entity.ProductRepository;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	ProductRepository productRepository;

	@KafkaListener(topics = "${app.order.kafka.topic.order-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void listen(@Payload OrderEvent orderEvent) {

		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- product-service -> update product inventory

		logger.info("Received new order event... " + orderEvent);
		
		Set<OrderItem> orderItems = orderEvent.getOrder().getItems();
		Set<Product> productsToSave = new HashSet<>();
		for (OrderItem orderItem : orderItems) {
			Optional<Product> optional = productRepository.findById(orderItem.getProductId());
			if (optional.isPresent()) {
				Product product = optional.get();

				if (product.getAvailableInventory().compareTo(orderItem.getQuantity()) >= 0) {

					product.setAvailableInventory(product.getAvailableInventory() - orderItem.getQuantity());
					productsToSave.add(product);
				} else {
					// TODO Send Event to Cancel the Order - as some items in the order are OutOfStock
					break;
				}
			}
		}
		
		productRepository.saveAll(productsToSave);
		
		logger.info(orderEvent.toString());

	}
}
