package com.scshop.payments.paymentservice.entity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scshop.application.common.model.PaymentReceived;;

public interface PaymentRepository extends JpaRepository<PaymentReceived, UUID> {

	Optional<PaymentReceived> findByOrderId(UUID orderId);
}
