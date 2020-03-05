package com.scshop.orders.orderservice.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scshop.application.common.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

}
