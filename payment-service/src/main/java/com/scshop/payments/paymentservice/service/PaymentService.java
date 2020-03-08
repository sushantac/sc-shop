package com.scshop.payments.paymentservice.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.scshop.application.common.enums.OrderEventType;
import com.scshop.application.common.enums.PaymentMode;
import com.scshop.application.common.enums.PaymentStatus;
import com.scshop.application.common.model.OrderEvent;
import com.scshop.application.common.model.Payment;
import com.scshop.application.common.model.PaymentReceived;
import com.scshop.application.common.model.User;
import com.scshop.payments.paymentservice.entity.PaymentRepository;

import reactor.core.publisher.Mono;

@Service
public class PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	WebClient.Builder loadBalancedWebClientBuilder;

	@Value("${app.order.kafka.topic.order-topic}")
	private String ORDER_TOPIC;

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	@KafkaListener(topics = "${app.order.kafka.topic.order-topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void listen(@Payload OrderEvent orderEvent) {

		// $$ Kafka Consumers listening to ORDER_TOPIC
		// --- payment-service -> update customer balance

		logger.info("\n\n ********** Received new order event... " + orderEvent + "********* \n\n");

		switch (orderEvent.getEventType()) {
		case ORDER_INITIATED:

			processPaymentForNewOrder(orderEvent);
			break;

		case ORDER_ITEMS_OUTOFSTOCK:

			revertPayment(orderEvent);
			break;

		case ORDER_CANCELLED:

			revertPayment(orderEvent);
			break;

		case ORDER_PAYMENT_FAILED:

			revertPayment(orderEvent);
			break;

		case ORDER_CONFIRMED:
			// Do nothing
			break;

		default:
			break;
		}

	}

	/**
	 * Considering the User Credit update as a synchronous request for now.. the
	 * third party external payment will be most likely a synchronous api call so
	 * keeping it similar to it i.e. not sending kafka event
	 * 
	 * @param orderEvent
	 */
	private void processPaymentForNewOrder(OrderEvent orderEvent) {
		Payment payment = orderEvent.getOrder().getPayment();

		// Validate for Idempotency - Check if request already processed
		Optional<PaymentReceived> optional = paymentRepository.findByOrderId(orderEvent.getOrderId());
		if (optional.isPresent() && !PaymentStatus.PROCESSED.equals(optional.get().getStatus())) {

			// Retrieve the user to check credit balance
			User user = loadBalancedWebClientBuilder
					.build().get().uri(uriBuilder -> uriBuilder.scheme("http").host("user-service")
							.path("/api/v1/users/{id}").build(orderEvent.getUserId().toString()))
					.retrieve().bodyToMono(User.class).block();

			PaymentReceived paymentReceivedRecordToUpdate = null;
			boolean isEnoughUserCreditAvailable = false;
			if (user.getCredits().compareTo(payment.getGrandTotal()) >= 0) {

				isEnoughUserCreditAvailable = true;

				// Deduct customer credits
				user.setCredits(user.getCredits().subtract(payment.getGrandTotal()));
				ResponseEntity<Void> responseEntity = loadBalancedWebClientBuilder.build().put()
						.uri(uriBuilder -> uriBuilder.scheme("http").host("user-service").path("/api/v1/users/{id}")
								.build(orderEvent.getUserId().toString()))
						.body(BodyInserters.fromPublisher(Mono.just(user), User.class)).retrieve().toBodilessEntity()
						.block();

				logger.info("User credit update response : " + responseEntity);

				// Check the update credit response and update payment record
				if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
					paymentReceivedRecordToUpdate = new PaymentReceived(null, orderEvent.getUserId(),
							orderEvent.getOrderId(), payment.getGrandTotal(), payment.getCurrency(),
							PaymentMode.CREDITS, PaymentStatus.PROCESSED);
				} else {
					paymentReceivedRecordToUpdate = new PaymentReceived(null, orderEvent.getUserId(),
							orderEvent.getOrderId(), payment.getGrandTotal(), payment.getCurrency(),
							PaymentMode.CREDITS, PaymentStatus.PAYMENT_FAILED);

					// Sending ORDER_PAYMENT_FAILED event on kafka in the next block
				}

				paymentRepository.save(paymentReceivedRecordToUpdate);
			}

			// Send ORDER_PAYMENT_FAILED event on kafka topic
			if (!isEnoughUserCreditAvailable) {
				orderEvent.setEventType(OrderEventType.ORDER_PAYMENT_FAILED);

				ListenableFuture<SendResult<String, OrderEvent>> future = kafkaTemplate.send(ORDER_TOPIC,
						orderEvent.getOrderId().toString(), orderEvent);

				logger.info("Sending order payment failed event on topic ORDER_TOPIC...");
				future.addCallback(new ListenableFutureCallback<SendResult<String, OrderEvent>>() {

					@Override
					public void onSuccess(SendResult<String, OrderEvent> result) {
						logger.info(
								"ORDER_PAYMENT_FAILED event SENT on topic ORDER_TOPIC for order id {0} and user id {1} ",
								new Object[] { orderEvent.getOrderId(), orderEvent.getUserId() });
					}

					@Override
					public void onFailure(Throwable ex) {
						logger.info(
								"ORDER_PAYMENT_FAILED event send on ORDER_TOPIC FAILED for order id {0} and user id {1} ",
								new Object[] { orderEvent.getOrderId(), orderEvent.getUserId() });

						// TODO thinking what to do... kafka should retry..let's see

						ex.printStackTrace();
					}

				});
			}
		}

	}

	/**
	 * Process return of user credits for failed, outOfStock, cancelled orders
	 * 
	 * TODO PaymentStatus.RETURN_FAILED --> Need to add some kind of retry logic if
	 * user-service is unavailable (similar to a third party payment system going
	 * down) TODO It could also be an external accounting job which will scan the
	 * database table scheduled at the end of the day to check the RETURN_FAILED
	 * status and retry the return for all such records
	 * 
	 * @param orderEvent
	 */
	private void revertPayment(OrderEvent orderEvent) {

		// Validate for Idempotency - Check if request already processed
		Optional<PaymentReceived> optional = paymentRepository.findByOrderId(orderEvent.getOrderId());
		if (optional.isPresent() && !PaymentStatus.RETURNED.equals(optional.get().getStatus())) {

			Payment payment = orderEvent.getOrder().getPayment();

			// Retrieve the user to revert credit balance
			User user = loadBalancedWebClientBuilder
					.build().get().uri(uriBuilder -> uriBuilder.scheme("http").host("user-service")
							.path("/api/v1/users/{id}").build(orderEvent.getUserId().toString()))
					.retrieve().bodyToMono(User.class).block();

			PaymentReceived paymentReceivedRecordToUpdate = null;

			// Revert customer credits
			user.setCredits(user.getCredits().add(payment.getGrandTotal()));
			ResponseEntity<Void> responseEntity = loadBalancedWebClientBuilder.build().put()
					.uri(uriBuilder -> uriBuilder.scheme("http").host("user-service").path("/api/v1/users/{id}")
							.build(orderEvent.getUserId().toString()))
					.body(BodyInserters.fromPublisher(Mono.just(user), User.class)).retrieve().toBodilessEntity()
					.block();

			logger.info("User credit update response: " + responseEntity);

			// Check the update credit response and update payment record
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
				paymentReceivedRecordToUpdate = new PaymentReceived(null, orderEvent.getUserId(),
						orderEvent.getOrderId(), payment.getGrandTotal(), payment.getCurrency(), PaymentMode.CREDITS,
						PaymentStatus.RETURNED);
			} else {
				paymentReceivedRecordToUpdate = new PaymentReceived(null, orderEvent.getUserId(),
						orderEvent.getOrderId(), payment.getGrandTotal(), payment.getCurrency(), PaymentMode.CREDITS,
						PaymentStatus.RETURN_FAILED);
			}

			paymentRepository.save(paymentReceivedRecordToUpdate);
		}
	}
}
