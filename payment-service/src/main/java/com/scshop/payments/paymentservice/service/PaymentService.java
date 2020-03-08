package com.scshop.payments.paymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.scshop.application.common.enums.PaymentMode;
import com.scshop.application.common.enums.PaymentStatus;
import com.scshop.application.common.model.OrderEvent;
import com.scshop.application.common.model.Payment;
import com.scshop.application.common.model.PaymentReceived;
import com.scshop.application.common.model.User;
import com.scshop.payments.paymentservice.entity.PaymentRepository;
import com.scshop.payments.paymentservice.exception.UserCreditsNotAvailableException;

import reactor.core.publisher.Mono;

@Service
public class PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	WebClient.Builder loadBalancedWebClientBuilder;

	@KafkaListener(topics = "${app.order.kafka.topic.order-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void listen(@Payload OrderEvent orderEvent) {

		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- payment-service -> update customer balance

		logger.info("\n\n ********** Received new order event... " + orderEvent + "********* \n\n");

		Payment payment = orderEvent.getOrder().getPayment();

		User user = loadBalancedWebClientBuilder
				.build().get().uri(uriBuilder -> uriBuilder.scheme("http").host("user-service")
						.path("/api/v1/users/{id}").build(orderEvent.getUserId().toString()))
				.retrieve().bodyToMono(User.class).block();

		PaymentStatus status = null;

		if (user.getCredits().compareTo(payment.getGrandTotal()) == -1) {
			status = PaymentStatus.FAILED;
		} else {
			status = PaymentStatus.PROCESSED;
		}

		// For now - TODO revisit later

		if (PaymentStatus.PROCESSED.equals(status)) {
			user.setCredits(user.getCredits().subtract(payment.getGrandTotal()));
			WebClient.RequestHeadersSpec<?> requestSpec = loadBalancedWebClientBuilder.build().put()
					.uri(uriBuilder -> uriBuilder.scheme("http").host("user-service").path("/api/v1/users/{id}")
							.build(orderEvent.getUserId().toString()))
					.body(BodyInserters.fromPublisher(Mono.just(user), User.class));

			Mono<?> mono = requestSpec.exchange();
		}

		PaymentReceived paymentReceived = new PaymentReceived(null, orderEvent.getUserId(), orderEvent.getOrderId(),
				payment.getGrandTotal(), payment.getCurrency(), PaymentMode.CREDITS, status);

		paymentRepository.save(paymentReceived);

		if (!PaymentStatus.PROCESSED.equals(status)) {
			throw new UserCreditsNotAvailableException(
					"User doesn't have enough credits in his account. Please add credits and retry the order");

			// TODO Send Cancel order event on kafka topic -
		}

	}
}
