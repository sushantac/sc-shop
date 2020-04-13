package com.scshop.orders.orderservice.services;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.reactive.function.client.WebClient;

import com.scshop.application.common.enums.OrderEventType;
import com.scshop.application.common.enums.OrderStatus;
import com.scshop.application.common.model.FinalOrder;
import com.scshop.application.common.model.OrderEvent;
import com.scshop.application.common.model.OrderItem;
import com.scshop.application.common.model.Product;
import com.scshop.orders.orderservice.entity.OrderRepository;
import com.scshop.orders.orderservice.validation.OrderValidation;
import com.scshop.orders.orderservice.validation.OrderValidationStatus;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Value("${app.order.shipping.standard-charges}")
	private Integer STANDARD_SHIPPING_CHARGES;

	@Value("${app.order.kafka.topic.order-topic}")
	private String ORDER_TOPIC;

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	//@Autowired
	//WebClient.Builder loadBalancedWebClientBuilder;
	
	@Autowired
	WebClient loadBalancedWebClient;

	@Autowired
	OrderRepository orderRepository;

	public OrderValidation validate(FinalOrder order) {

		// TODO Add validation for currency check!

		OrderValidation orderValidation = new OrderValidation();

		if (order.getItems() == null || order.getItems().isEmpty() || order.getPayment() == null
				|| order.getPayment().getGrandTotal() == null || order.getPayment().getGrandTotal().equals(0)) {
			orderValidation.setStatus(OrderValidationStatus.ORDER_IS_INVALID);
		}

		List<OrderItem> orderItems = order.getItems();

		BigDecimal calculatedGrandTotalPrice = new BigDecimal(0);

		for (OrderItem orderItem : orderItems) {
			
			Product product = loadBalancedWebClient.get()
					//loadBalancedWebClientBuilder.build().get()
					.uri(uriBuilder -> uriBuilder.scheme("http").host("product-service").path("/api/v1/products/{id}")
							.build(orderItem.getProductId().toString()))
					.attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("order-service"))
					//.headers(headers -> headers.setBearerAuth(jwt.getTokenValue()))
					//.header("Authorization", "Bearer " + jwt.getTokenValue())
					.retrieve()
					.bodyToMono(Product.class).block();

			if (product != null) {
				if (orderItem.getPrice().floatValue() != product.getPrice().abs().floatValue()) {
					orderValidation.setStatus(OrderValidationStatus.PRODUCT_PRICE_CHANGED);
					orderValidation.getInvalidOrderItems().add(orderItem);
				} else if (orderItem.getQuantity() > product.getAvailableInventory()) {
					orderValidation.setStatus(OrderValidationStatus.PRODUCT_OUT_OF_STOCK);
					orderValidation.getInvalidOrderItems().add(orderItem);
				} else {
					calculatedGrandTotalPrice = calculatedGrandTotalPrice
							.add(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
				}
			} else {
				orderValidation.setStatus(OrderValidationStatus.ORDER_IS_INVALID);
				orderValidation.getInvalidOrderItems().add(orderItem);
			}

			logger.info("Fetched product details - " + product);

		}

		// Return now if any validation failed
		if (OrderValidationStatus.ORDER_IS_INVALID.equals(orderValidation.getStatus())
				|| OrderValidationStatus.PRODUCT_OUT_OF_STOCK.equals(orderValidation.getStatus())
				|| OrderValidationStatus.ORDER_IS_INVALID.equals(orderValidation.getStatus())) {

			return orderValidation;
		}

		calculatedGrandTotalPrice = calculatedGrandTotalPrice.add(new BigDecimal(STANDARD_SHIPPING_CHARGES));

		if (calculatedGrandTotalPrice.floatValue() == order.getPayment().getGrandTotal().floatValue()) {
			orderValidation.setStatus(OrderValidationStatus.ORDER_IS_VALID);
		} else {
			orderValidation.setStatus(OrderValidationStatus.ORDER_IS_INVALID);
		}

		// For now
		orderValidation.setStatus(OrderValidationStatus.ORDER_IS_VALID);
		// ---TODO REMOVE-LATER---

		return orderValidation;

	}

	/**
	 * Process the new order
	 * 
	 * @param order
	 */
	public void processOrder(FinalOrder order) {

		// -------------- First-cut from TODO --------------
		// ## order-service --> Send ORDER with ORDER_INITIATED status on ORDER_TOPIC
		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- payment-service -> update customer balance -- TODO later
		// --- product-service -> update product inventory
		// --- notification-service -> Send Order Initiated message/email to customer --
		// TODO later
		// -------------------------------------------------

		OrderEvent orderEvent = new OrderEvent(order.getUserId(), order.getId(), order, OrderEventType.ORDER_INITIATED);

		ListenableFuture<SendResult<String, OrderEvent>> future = kafkaTemplate.send(ORDER_TOPIC,
				order.getId().toString(), orderEvent);

		logger.info("Sending new order notification on topic ORDER_TOPIC...");
		future.addCallback(new ListenableFutureCallback<SendResult<String, OrderEvent>>() {

			@Override
			public void onSuccess(SendResult<String, OrderEvent> result) {
				logger.info("New order notification sent on topic ORDER_TOPIC!");

				order.setStatus(OrderStatus.INITIATED);
				orderRepository.save(order);
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.info("New order notification send failed... Cancelling the order for now!");
				ex.printStackTrace();

				// TODO Should we cancel the order or something else?
				order.setStatus(OrderStatus.CANCELLED);
				orderRepository.save(order);
			}

		});

		// -------------- TODO --------------
		// ## order-service --> Send ORDER_INITIATED event on ORDER_TOPIC
		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- payment-service -> update customer balance
		// --- product-service -> update product inventory
		// --- notification-service -> Send Order Initiated message/email to customer --
		// TODO later
		// ------
		//
		// ## payment-service --> Send ORDER_PAYMENT_CONFIRMED event on ORDER_TOPIC if
		// payment successful
		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- shipping-service -> Create shipping record -- TODO later
		// --- order-service -> Change order status to CONFIRMED
		// --- notification-service -> Send Order Confirmed message/email to customer --
		// TODO later
		// ------
		//
		// ## payment-service --> Send ORDER_PAYMENT_FAILED event on ORDER_TOPIC if
		// payment fails
		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- order-service -> Change order status to CANCELLED
		// --- notification-service -> Send Order Cancelled message/email to customer --
		// TODO later
		// ------

	}

}