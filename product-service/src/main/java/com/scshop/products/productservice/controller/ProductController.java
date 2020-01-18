package com.scshop.products.productservice.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.scshop.products.productservice.entity.Product;
import com.scshop.products.productservice.entity.ProductRepository;
import com.scshop.products.productservice.exception.ProductNotFoundException;

@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(path = "/products", method = RequestMethod.GET)
	public List<Product> getProducts() {

		List<Product> products = productRepository.findAll();

		return products;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/products/{id}", method = RequestMethod.GET)
	public Product getProduct(@PathVariable UUID id) {

		Optional<Product> optional = productRepository.findById(id);

		if (!optional.isPresent()) {
			throw new ProductNotFoundException("Product not found for id: " + id);
		}
		Product product = optional.get();

		return product;
	}

	/**
	 * Create product
	 * 
	 * @param {@link Product}
	 */
	@RequestMapping(path = "/products", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createProduct(@RequestBody Product product) {

		Product savedProduct = productRepository.save(product);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedProduct.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	/**
	 * 
	 * @param id
	 * @param product
	 */
	@RequestMapping(path = "/products/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateProduct(@PathVariable UUID id, @RequestBody Product product) {
		boolean is_present = productRepository.existsById(id);

		if (!is_present) {
			throw new ProductNotFoundException("Product not found for id: " + id);
		}

		product.setId(id);
		productRepository.save(product);
	}

	/**
	 * 
	 * @param id
	 */
	@RequestMapping(path = "/products/{id}", method = RequestMethod.DELETE)
	public void deleteProduct(@PathVariable UUID id) {

		boolean is_present = productRepository.existsById(id);

		if (!is_present) {
			throw new ProductNotFoundException("Product not found for id: " + id);
		}

		productRepository.deleteById(id);

	}

}
