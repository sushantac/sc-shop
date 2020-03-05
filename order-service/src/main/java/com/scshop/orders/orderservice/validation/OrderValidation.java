package com.scshop.orders.orderservice.validation;

import java.util.HashSet;
import java.util.Set;

import com.scshop.application.common.model.OrderItem;

public class OrderValidation {

	private OrderValidationStatus status;
	private Set<OrderItem> invalidOrderItems = new HashSet<>();

	public OrderValidationStatus getStatus() {
		return status;
	}

	public void setStatus(OrderValidationStatus status) {
		this.status = status;
	}

	public Set<OrderItem> getInvalidOrderItems() {
		return invalidOrderItems;
	}

	public void setInvalidOrderItems(Set<OrderItem> invalidOrderItems) {
		this.invalidOrderItems = invalidOrderItems;
	}
	
}
