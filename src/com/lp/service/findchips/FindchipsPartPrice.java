package com.lp.service.findchips;

import java.io.Serializable;
import java.math.BigDecimal;

public class FindchipsPartPrice implements Serializable {
	private static final long serialVersionUID = 3434237987910661634L;

	private BigDecimal quantity ;
	private BigDecimal price ;
	private String currency ;
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
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
}
