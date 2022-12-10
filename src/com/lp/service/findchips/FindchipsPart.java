package com.lp.service.findchips;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FindchipsPart implements Serializable {
	private static final long serialVersionUID = 7428165225284325400L;
	
	private String manufacturer ;
	private String part ;
	private String description ;
	private String buyNowUrl ;
	private BigDecimal stock ;
	private String lastUpdated ;
	private String distributorItemNo ;
	private String pbFree ;
	
//	private FindchipsPartPricelist price ;
	private List<FindchipsPartPrice> price ;
	private FindchipsPartRohs rohs ;

	public FindchipsPart() {
		setRohs(new FindchipsPartRohs());
//		setPrice(new FindchipsPartPricelist());
		setPrice(new ArrayList<FindchipsPartPrice>()) ;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBuyNowUrl() {
		return buyNowUrl;
	}
	public void setBuyNowUrl(String buyNowUrl) {
		this.buyNowUrl = buyNowUrl;
	}
	public BigDecimal getStock() {
		return stock;
	}
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
//	public FindchipsPartPricelist getPrice() {
	public List<FindchipsPartPrice> getPrice() {
		return price;
	}
//	public void setPrice(FindchipsPartPricelist price) {
	public void setPrice(List<FindchipsPartPrice> prices) {
		this.price = prices;
	}
	
	public FindchipsPartRohs getRohs() {
		return rohs;
	}
	public void setRohs(FindchipsPartRohs rohs) {
		this.rohs = rohs;
	}

	public String getDistributorItemNo() {
		return distributorItemNo;
	}

	public void setDistributorItemNo(String distributorItemNo) {
		this.distributorItemNo = distributorItemNo;
	}

	public String getPbFree() {
		return pbFree;
	}

	public void setPbFree(String pbFree) {
		this.pbFree = pbFree;
	}
	
	public boolean isPbFree() {
		return "Yes".equals(pbFree) ;
	}
}
