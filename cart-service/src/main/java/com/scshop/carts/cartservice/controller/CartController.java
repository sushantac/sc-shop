package com.scshop.carts.cartservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scshop.carts.cartservice.entity.CartItem;
import com.scshop.carts.cartservice.entity.CartItemRepository;

@RestController
@RequestMapping(path = "/api/v1/carts")
public class CartController {

	@Autowired
	CartItemRepository cartItemRepository;

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	public List<CartItem> getCartItems(@PathVariable UUID userId) {

		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

		if (cartItems == null) {
			cartItems = new ArrayList<>();
		}
		return cartItems;
	}

	/**
	 * 
	 * @param userId
	 * @param cartItems
	 */
	@RequestMapping(path = "/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateCart(@PathVariable UUID userId, @RequestBody List<CartItem> cartItems) {

		if (cartItems == null || cartItems.isEmpty()) {
			return;
		}

		List<CartItem> cartItemsToSave = new ArrayList<>();

		cartItems.forEach(item -> {
			CartItem cartItemToSave = cartItemRepository.findByUserIdAndProductId(userId, item.getProductId());

			if (cartItemToSave != null) {
				cartItemToSave.setQuantity(cartItemToSave.getQuantity() + item.getQuantity());
				cartItemsToSave.add(cartItemToSave);
			} else {
				if (cartItemsToSave.contains(item)) {

					cartItemsToSave.stream().map(currentItem -> {
						if (currentItem.getProductId().equals(item.getProductId())) {
							currentItem.setQuantity(item.getQuantity() + currentItem.getQuantity());
						}
						return currentItem;
					}).collect(Collectors.toList());

				} else {
					cartItemToSave = item;
					cartItemToSave.setUserId(userId);
					cartItemsToSave.add(cartItemToSave);
				}
			}

		});

		cartItemRepository.saveAll(cartItemsToSave);
	}

	/**
	 * 
	 * @param userId
	 */
	@RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
	public void deleteCart(@PathVariable UUID userId) {

		cartItemRepository.deleteByUserId(userId);

	}

}
