package com.scshop.carts.cartservice.entity;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;;

public interface CartItemRepository extends JpaRepository<CartItem, UUID>{
	
	public List<CartItem> findByUserId(UUID userId);
	
	public CartItem findByUserIdAndProductId(UUID userId, UUID productId);
	
	@Transactional
	public void deleteByUserId(UUID userId);
	
}
