package com.scshop.orders.orderservice.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.scshop.orders.orderservice.entity.FinalOrder;
import com.scshop.orders.orderservice.entity.OrderRepository;
import com.scshop.orders.orderservice.entity.OrderStatus;
import com.scshop.orders.orderservice.exception.OrderDetailsInvalidException;
import com.scshop.orders.orderservice.services.OrderService;
import com.scshop.orders.orderservice.validation.OrderValidation;
import com.scshop.orders.orderservice.validation.OrderValidationStatus;

@RestController
@RequestMapping(path = "/api/v1/orders")
public class OrderController {

	static Logger logger = Logger.getLogger(OrderController.class.toString());

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<FinalOrder> getOrders() {

		return orderRepository.findAll();

	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public FinalOrder getOrder(@PathVariable UUID id) {

		Optional<FinalOrder> optional = orderRepository.findById(id);

		if (!optional.isPresent()) {
			throw new RuntimeException("Order not found");
		}

		return optional.get();
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<Object> generateOrder(@RequestBody FinalOrder order) {

		// # // Validate the incoming order data before committing it
		// - validate if the totalPrice is correct by re-calculating it by fetching product item prices from product-service
		// - validate if customer has enough balance in credits
		// - validate if product inventory is available
		
		OrderValidation orderValidation = orderService.validate(order);
		
		if(!OrderValidationStatus.ORDER_IS_VALID.equals(orderValidation.getStatus())) {
			throw new OrderDetailsInvalidException(orderValidation.getStatus().getDetails());
		}
		
		order.setStatus(OrderStatus.INITIATED);
		FinalOrder savedOrder = orderRepository.save(order);

		
		// -------------- TODO -------------- 
		// ## order-service --> Send ORDER_INITIATED event on ORDER_TOPIC
		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- payment-service -> update customer balance
		// --- product-service -> update product inventory 
		// --- notification-service -> Send Order Initiated message/email to customer -- TODO later
		// ------
		//
		// ## payment-service --> Send ORDER_PAYMENT_CONFIRMED event on ORDER_TOPIC if payment successful
		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- shipping-service -> Create shipping record -- TODO later
		// --- order-service -> Change order status to CONFIRMED
		// --- notification-service -> Send Order Confirmed message/email to customer -- TODO later
		// ------
		//
		// ## payment-service --> Send ORDER_PAYMENT_FAILED event on ORDER_TOPIC if payment fails
		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- order-service -> Change order status to CANCELLED
		// --- notification-service -> Send Order Cancelled message/email to customer -- TODO later
		// ------
		
		
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedOrder.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public void cancelOrder(@PathVariable UUID id) {
		orderRepository.deleteById(id);
	}

}
