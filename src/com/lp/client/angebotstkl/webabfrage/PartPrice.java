package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;

public class PartPrice {

	private BigDecimal quantityFrom;
	private BigDecimal quantityTo;
	private BigDecimal price;
	private String currency;

	public PartPrice() {
	}

	public BigDecimal getQuantityFrom() {
		return quantityFrom;
	}

	public void setQuantityFrom(BigDecimal quantityFrom) {
		this.quantityFrom = quantityFrom;
	}

	public BigDecimal getQuantityTo() {
		return quantityTo;
	}

	public void setQuantityTo(BigDecimal quantityTo) {
		this.quantityTo = quantityTo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Boolean isPriceForQuantity(BigDecimal quantity) {
		if (quantity == null || getQuantityFrom() == null) return false;
		
		return (getQuantityFrom().compareTo(quantity) < 0 || getQuantityFrom().compareTo(quantity) == 0)
				&& (getQuantityTo() == null || quantity.compareTo(getQuantityTo()) < 0);
	}

	@Override
	public String toString() {
		return "PartPrice [quantityFrom=" + quantityFrom + ", quantityTo="
				+ quantityTo + ", price=" + price + ", currency=" + currency
				+ "]";
	}
	
	
}
