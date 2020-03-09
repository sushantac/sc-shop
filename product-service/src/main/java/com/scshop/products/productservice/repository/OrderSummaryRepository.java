package com.scshop.products.productservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scshop.application.common.model.OrderSummary;;

@Repository
public interface OrderSummaryRepository extends JpaRepository<OrderSummary, UUID>{

}
