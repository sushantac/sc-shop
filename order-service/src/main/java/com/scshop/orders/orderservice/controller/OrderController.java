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

@RestController
@RequestMapping(path="/api/v1")
public class OrderController {
	
	static Logger logger = Logger.getLogger(OrderController.class.toString());
	
	
	@Autowired
	OrderRepository orderRepository;
	
	
	@RequestMapping(path = "/orders", method = RequestMethod.GET)
	public List<FinalOrder> getOrders() {
		
		return orderRepository.findAll();

	}
	
	
	@RequestMapping(path = "/orders/{id}", method = RequestMethod.GET)
	public FinalOrder getOrder(@PathVariable UUID id) {
		
		Optional<FinalOrder> optional = orderRepository.findById(id);
		
		if(!optional.isPresent()) {
			throw new RuntimeException("Order not found");
		}
		
		return optional.get();
	}
	
	@RequestMapping(path = "/orders", method = RequestMethod.POST)
	public ResponseEntity<Object> generateOrder(@RequestBody FinalOrder order) {
		
		FinalOrder savedOrder = orderRepository.save(order);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedOrder.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	
	
	@RequestMapping(path = "/orders/{id}", method = RequestMethod.PUT)
	public void cancelOrder(@PathVariable UUID id) {
		
		orderRepository.deleteById(id);
		
	}
	
}
