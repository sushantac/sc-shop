package com.scshop.orders.orderservice.validation;

public enum OrderValidationStatus {

	PRODUCT_OUT_OF_STOCK("PRODUCT_OUT_OF_STOCK",
			"Some of the items ordered are out of stock. Please check the order and try again."),
	PRODUCT_PRICE_CHANGED("PRODUCT_PRICE_CHANGED",
			"Price has changed for some of the items in the order. Please check the order and try again."),
	ORDER_IS_INVALID("ORDER_IS_INVALID", "Order details are not valid. Please check the order and try again."),
	ORDER_IS_VALID("ORDER_IS_VALID", "Order is valid.");

	private final String status;
	private final String details;

	OrderValidationStatus(String status, String details) {
		this.status = status;
		this.details = details;
	}

	public String getStatus() {
		return status;
	}

	public String getDetails() {
		return details;
	}

}
