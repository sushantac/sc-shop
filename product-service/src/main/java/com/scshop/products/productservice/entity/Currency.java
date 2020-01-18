package com.scshop.products.productservice.entity;

public enum Currency {

	AUD("AUD", "Australian Dollar"), 
	USD("USD", "US Dollar"), 
	INR("INR", "Indian Rupee"), 
	JPY("JPY", "Japanese Yen"), 
	EUR("EUR", "Euro"), 
	CAD("CAD", "Canadian Dollar");

	private final String name;
	private final String description;

	Currency(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
