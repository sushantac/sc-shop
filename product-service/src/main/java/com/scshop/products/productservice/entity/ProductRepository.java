package com.scshop.products.productservice.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scshop.application.common.model.Product;;

public interface ProductRepository extends JpaRepository<Product, UUID>{

}
