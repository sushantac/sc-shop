package com.scshop.orders.orderservice.entity;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * @author sushant
 *
 */
@Embeddable
public class Payment {
	
	@NotNull
	private BigDecimal subTotal;
	
	@NotNull
	private BigDecimal shippingCharges;
	
	@NotNull
	private BigDecimal total;
	
	@NotNull
	private BigDecimal grandTotal;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getShippingCharges() {
		return shippingCharges;
	}
	public void setShippingCharges(BigDecimal shippingCharges) {
		this.shippingCharges = shippingCharges;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((grandTotal == null) ? 0 : grandTotal.hashCode());
		result = prime * result + ((shippingCharges == null) ? 0 : shippingCharges.hashCode());
		result = prime * result + ((subTotal == null) ? 0 : subTotal.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
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
		Payment other = (Payment) obj;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (grandTotal == null) {
			if (other.grandTotal != null)
				return false;
		} else if (!grandTotal.equals(other.grandTotal))
			return false;
		if (shippingCharges == null) {
			if (other.shippingCharges != null)
				return false;
		} else if (!shippingCharges.equals(other.shippingCharges))
			return false;
		if (subTotal == null) {
			if (other.subTotal != null)
				return false;
		} else if (!subTotal.equals(other.subTotal))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Payment [subTotal=" + subTotal + ", shippingCharges=" + shippingCharges + ", total=" + total
				+ ", grandTotal=" + grandTotal + ", currency=" + currency + "]";
	}
	
	
	
}
