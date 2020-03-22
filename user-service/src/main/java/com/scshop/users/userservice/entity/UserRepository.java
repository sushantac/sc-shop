package com.scshop.users.userservice.entity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scshop.application.common.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByUserId(UUID userId);
	
	boolean existsByUserId(UUID userId);
	
}
