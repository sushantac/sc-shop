package com.scshop.products.productservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.scshop.orders.orderservice.entity.FinalOrder;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@KafkaListener(topics = "${app.order.kafka.topic.order-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void listen(@Payload FinalOrder order) {

		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- product-service -> update product inventory 
		
		logger.info(order.toString());

	}
}
