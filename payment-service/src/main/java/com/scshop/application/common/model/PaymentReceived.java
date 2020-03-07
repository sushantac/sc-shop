package com.scshop.application.common.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.scshop.application.common.enums.Currency;
import com.scshop.application.common.enums.PaymentMode;
import com.scshop.application.common.enums.PaymentStatus;

@Entity
public class PaymentReceived {

	
	@Id
	@GeneratedValue
	private UUID id;
	
	@NotNull
	private UUID userId;
	
	@NotNull
	private UUID orderId;
	
	@NotNull
	private BigDecimal amount;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private PaymentMode mode;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	

	public PaymentReceived(UUID id, @NotNull UUID userId, @NotNull UUID orderId, @NotNull BigDecimal amount,
			@NotNull Currency currency, @NotNull PaymentMode mode, @NotNull PaymentStatus status) {
		super();
		this.id = id;
		this.userId = userId;
		this.orderId = orderId;
		this.amount = amount;
		this.currency = currency;
		this.mode = mode;
		this.status = status;
	}

	public PaymentReceived() {}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public PaymentMode getMode() {
		return mode;
	}
	public void setMode(PaymentMode mode) {
		this.mode = mode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		PaymentReceived other = (PaymentReceived) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (currency != other.currency)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mode != other.mode)
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (status != other.status)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PaymentReceived [id=" + id + ", userId=" + userId + ", orderId=" + orderId + ", amount=" + amount
				+ ", currency=" + currency + ", mode=" + mode + ", status=" + status + "]";
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}
	
	
}
