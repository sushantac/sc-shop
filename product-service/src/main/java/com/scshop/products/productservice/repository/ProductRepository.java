package com.scshop.products.productservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scshop.application.common.model.Product;;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{

}
