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
@RequestMapping(path = "/api/v1/orders")
public class OrderController {

	static Logger logger = Logger.getLogger(OrderController.class.toString());

	@Autowired
	OrderRepository orderRepository;

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
		// - validate if the totalPrice is correct by re-calculating it by fetching product item prices
		// - validate if customer has enough balance in credits
		// - validate if product inventory is available

		FinalOrder savedOrder = orderRepository.save(order);

		// # // Send events using kafka (an event listener will cancel this order if any of the target events send failure event)
		// - update products inventory 
		// - update customer balance
		// - create shipping record -- TODO
		// - email notification -- TODO
		
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedOrder.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public void cancelOrder(@PathVariable UUID id) {

		orderRepository.deleteById(id);

	}

}
