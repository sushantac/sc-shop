package com.scshop.application.common.model;

import java.math.BigDecimal;
import java.net.URL;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.scshop.application.common.enums.Currency;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@NotNull
	private String name;
	
	private String description;
	
	@NotNull
	private String brand;
	
	@NotNull
	private String category;
	
	@NotNull
	private String subCategory;
	
	@NotNull
	private BigDecimal price;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	@NotNull
	private Integer availableInventory;
	
	private URL imgUrl;
	
	public Product() {
		
	}


	public Product(UUID id, @NotNull String name, String description, @NotNull String brand, @NotNull String category,
			@NotNull String subCategory, @NotNull BigDecimal price, @NotNull Currency currency,
			@NotNull Integer availableInventory, URL imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.category = category;
		this.subCategory = subCategory;
		this.price = price;
		this.currency = currency;
		this.availableInventory = availableInventory;
		this.imgUrl = imgUrl;
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public URL getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(URL imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availableInventory == null) ? 0 : availableInventory.hashCode());
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imgUrl == null) ? 0 : imgUrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((subCategory == null) ? 0 : subCategory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (availableInventory == null) {
			if (other.availableInventory != null)
				return false;
		} else if (!availableInventory.equals(other.availableInventory))
			return false;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (currency != other.currency)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imgUrl == null) {
			if (other.imgUrl != null)
				return false;
		} else if (!imgUrl.equals(other.imgUrl))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (subCategory == null) {
			if (other.subCategory != null)
				return false;
		} else if (!subCategory.equals(other.subCategory))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", brand=" + brand + ", category=" + category + ", subCategory="
				+ subCategory + ", price=" + price + ", currency=" + currency + ", availableInventory="
				+ availableInventory + ", imgUrl=" + imgUrl + "]";
	}

	public Integer getAvailableInventory() {
		return availableInventory;
	}

	public void setAvailableInventory(Integer availableInventory) {
		this.availableInventory = availableInventory;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}	
	
	
}
