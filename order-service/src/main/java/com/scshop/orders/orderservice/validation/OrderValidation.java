package com.scshop.orders.orderservice.validation;

import java.util.ArrayList;
import java.util.List;

import com.scshop.application.common.model.OrderItem;

public class OrderValidation {

	private OrderValidationStatus status;
	private List<OrderItem> invalidOrderItems = new ArrayList<>();

	public OrderValidationStatus getStatus() {
		return status;
	}

	public void setStatus(OrderValidationStatus status) {
		this.status = status;
	}

	public List<OrderItem> getInvalidOrderItems() {
		return invalidOrderItems;
	}

	public void setInvalidOrderItems(List<OrderItem> invalidOrderItems) {
		this.invalidOrderItems = invalidOrderItems;
	}
	
}
