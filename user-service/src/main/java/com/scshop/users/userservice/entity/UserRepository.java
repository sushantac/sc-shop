package com.scshop.users.userservice.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scshop.application.common.model.User;;

public interface UserRepository extends JpaRepository<User, UUID>{

}
