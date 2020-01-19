package com.scshop.users.userservice.entity;

public enum Gender {

	Male("Male"), 
	Female("Female"); 

	private final String name;

	Gender(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	

}
