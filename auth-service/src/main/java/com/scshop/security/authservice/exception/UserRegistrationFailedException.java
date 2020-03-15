package com.scshop.security.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserRegistrationFailedException extends RuntimeException{

	public UserRegistrationFailedException(String message) {
		super(message);
	}
}
