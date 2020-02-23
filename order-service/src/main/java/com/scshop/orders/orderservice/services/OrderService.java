package com.scshop.orders.orderservice.services;

import java.math.BigDecimal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.scshop.orders.orderservice.entity.FinalOrder;
import com.scshop.orders.orderservice.entity.OrderItem;
import com.scshop.orders.orderservice.entity.Product;
import com.scshop.orders.orderservice.validation.OrderValidation;
import com.scshop.orders.orderservice.validation.OrderValidationStatus;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Value("${app.order.shipping.standard-charges}")
	private Integer STANDARD_SHIPPING_CHARGES;

	@Autowired
	WebClient.Builder loadBalancedWebClientBuilder;

	public OrderValidation validate(FinalOrder order) {

		//TODO Add validation for currency check!
		
		OrderValidation orderValidation = new OrderValidation();

		if (order.getItems() == null || order.getItems().isEmpty() || order.getPayment().getGrandTotal().equals(0)) {
			orderValidation.setStatus(OrderValidationStatus.ORDER_IS_INVALID);
		}

		Set<OrderItem> orderItems = order.getItems();

		BigDecimal calculatedGrandTotalPrice = new BigDecimal(0);

		for (OrderItem orderItem : orderItems) {
			Product product = loadBalancedWebClientBuilder
					.build().get().uri(uriBuilder -> uriBuilder.scheme("http").host("product-service")
							.path("/api/v1/products/{id}").build(orderItem.getProductId().toString()))
					.retrieve().bodyToMono(Product.class).block();

			if (product != null) {
				if (!orderItem.getPrice().equals(product.getPrice())) {
					orderValidation.setStatus(OrderValidationStatus.PRODUCT_PRICE_CHANGED);
					orderValidation.getInvalidOrderItems().add(orderItem);
				} else if (orderItem.getQuantity() > product.getAvailableStock()) {
					orderValidation.setStatus(OrderValidationStatus.PRODUCT_OUT_OF_STOCK);
					orderValidation.getInvalidOrderItems().add(orderItem);
				} else {
					calculatedGrandTotalPrice = calculatedGrandTotalPrice
							.add(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
				}
			} else {
				orderValidation.setStatus(OrderValidationStatus.ORDER_IS_INVALID);
				orderValidation.getInvalidOrderItems().add(orderItem);
			}

			logger.info("Fetched product details - " + product);

		}

		if (OrderValidationStatus.ORDER_IS_INVALID.equals(orderValidation.getStatus())
				|| OrderValidationStatus.PRODUCT_OUT_OF_STOCK.equals(orderValidation.getStatus())
				|| OrderValidationStatus.ORDER_IS_INVALID.equals(orderValidation.getStatus())) {

			return orderValidation;
		}

		calculatedGrandTotalPrice = calculatedGrandTotalPrice.add(new BigDecimal(STANDARD_SHIPPING_CHARGES));

		if (calculatedGrandTotalPrice.equals(order.getPayment().getGrandTotal())) {
			orderValidation.setStatus(OrderValidationStatus.ORDER_IS_VALID);
		} else {
			orderValidation.setStatus(OrderValidationStatus.ORDER_IS_INVALID);
		}

		return orderValidation;

	}

}