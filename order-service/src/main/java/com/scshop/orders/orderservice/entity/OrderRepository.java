package com.scshop.orders.orderservice.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scshop.application.common.model.FinalOrder;

@Repository
public interface OrderRepository extends JpaRepository<FinalOrder, UUID> {

}
