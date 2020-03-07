package com.scshop.application.common.model;

import java.util.UUID;

import com.scshop.application.common.enums.OrderEventType;

public class OrderEvent {

	private UUID userId;
	private UUID orderId;
	private FinalOrder order;
	
	private OrderEventType eventType;

	public OrderEvent() {}
	
	public OrderEvent(UUID userId, UUID orderId, FinalOrder order, OrderEventType eventType) {
		super();
		this.userId = userId;
		this.orderId = orderId;
		this.order = order;
		this.eventType = eventType;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public FinalOrder getOrder() {
		return order;
	}

	public void setOrder(FinalOrder order) {
		this.order = order;
	}

	public OrderEventType getEventType() {
		return eventType;
	}

	public void setEventType(OrderEventType eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "OrderEvent [userId=" + userId + ", orderId=" + orderId + ", order=" + order + ", eventType=" + eventType
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderEvent other = (OrderEvent) obj;
		if (eventType != other.eventType)
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	
}
