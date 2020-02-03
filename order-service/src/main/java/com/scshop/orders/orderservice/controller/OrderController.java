package com.scshop.orders.orderservice.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scshop.orders.orderservice.entity.Order;
import com.scshop.orders.orderservice.entity.OrderRepository;

@RestController
@RequestMapping(path="/api/v1")
public class OrderController {
	
	
	@Autowired
	OrderRepository orderRepository;
	
	
	@RequestMapping(path = "/orders/{id}", method = RequestMethod.GET)
	public Order getOrder(@PathVariable UUID id) {
		
		Optional<Order> optional = orderRepository.findById(id);
		
		if(!optional.isPresent()) {
			throw new RuntimeException("Order not found");
		}
		
		return optional.get();
	}
	
	@RequestMapping(path = "/orders", method = RequestMethod.POST)
	public UUID generateOrder(@RequestBody Order order) {
		Order savedOrder = orderRepository.save(order);
		
		return savedOrder.getId();
	}
	
	
	@RequestMapping(path = "/orders/{id}", method = RequestMethod.PUT)
	public void cancelOrder(@PathVariable UUID id) {
		
		orderRepository.deleteById(id);
		
	}
	
}
