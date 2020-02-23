package com.scshop.orders.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderDetailsInvalidException extends RuntimeException{

	public OrderDetailsInvalidException(String message) {
		super(message);
	}
}
