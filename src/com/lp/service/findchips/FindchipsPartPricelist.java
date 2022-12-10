package com.lp.service.findchips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindchipsPartPricelist implements Serializable {
	private static final long serialVersionUID = 9133436649391365193L;
	private List<FindchipsPartPrice> price ;
	
	public FindchipsPartPricelist() {
		setPrice(new ArrayList<FindchipsPartPrice>()) ;
	}
	
	public FindchipsPartPricelist(List<FindchipsPartPrice> prices) {
		setPrice(prices) ;
	}
	
	public List<FindchipsPartPrice> getPrice() {
		return price;
	}

	public void setPrice(List<FindchipsPartPrice> price) {
		this.price = price;
	}
}
