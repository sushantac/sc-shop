package com.scshop.payments.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserCreditsNotAvailableException extends RuntimeException{

	public UserCreditsNotAvailableException(String message) {
		super(message);
	}
}
