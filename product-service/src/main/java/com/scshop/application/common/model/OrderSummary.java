package com.scshop.application.common.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.scshop.application.common.enums.InventoryStatus;
import com.scshop.application.common.enums.OrderStatus;
import com.scshop.application.common.enums.PaymentStatus;

@Entity
public class OrderSummary {

	@Id
	private UUID orderId;

	@NotNull
	private UUID userId;

	@NotNull
	@Enumerated(EnumType.STRING)
	private InventoryStatus inventoryStatus;

	public OrderSummary() {
	}

	public OrderSummary(UUID orderId, UUID userId, InventoryStatus inventoryStatus) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.inventoryStatus = inventoryStatus;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public InventoryStatus getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(InventoryStatus inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	@Override
	public String toString() {
		return "OrderSummary [orderId=" + orderId + ", userId=" + userId + ", inventoryStatus=" + inventoryStatus + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inventoryStatus == null) ? 0 : inventoryStatus.hashCode());
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
		OrderSummary other = (OrderSummary) obj;
		if (inventoryStatus != other.inventoryStatus)
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
