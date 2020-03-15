package com.scshop.security.authservice.model;

import java.util.UUID;

public class AuthenticationResponse {

	private UUID userId;
	private String username;	
	private final String token;
	
	public AuthenticationResponse(UUID userId, String username, String token) {
		super();
		this.userId = userId;
		this.username = username;
		this.token = token;
	}

	
	public String getToken() {
		return token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	
	
}
