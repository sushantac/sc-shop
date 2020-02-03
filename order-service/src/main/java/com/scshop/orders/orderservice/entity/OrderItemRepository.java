package com.scshop.orders.orderservice.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

}
