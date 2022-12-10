package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class SearchResponseEntry implements Serializable {
	private static final long serialVersionUID = -8596368366186136269L;

	private Integer numberOfProducts;
	private ProductEntry[] products;
	
	public SearchResponseEntry() {
		setNumberOfProducts(0);
		setProducts(new ProductEntry[0]);
	}
	public Integer getNumberOfProducts() {
		return numberOfProducts;
	}
	public void setNumberOfProducts(Integer numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}
	public ProductEntry[] getProducts() {
		return products;
	}
	public void setProducts(ProductEntry[] products) {
		this.products = products;
	}
}
