package com.scshop.security.authservice.model;

public class AuthenticationResponse {

	private String username;
	private final String token;
	
	public AuthenticationResponse(String username, String token) {
		super();
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

	
	
}
