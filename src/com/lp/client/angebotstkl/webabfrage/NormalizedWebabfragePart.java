package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;

import com.lp.server.angebotstkl.service.IWebpartnerDto;

public class NormalizedWebabfragePart {
	
	public enum AbfrageTyp {
		FINDCHIPS, OEMSECRETS
	}
	
	private AbfrageTyp type;
	private Object rawResponse;
	private Object rawPart;
	
	private Integer searchId;
	private IWebpartnerDto distributor;
	private String distributorItemNo;
	private String manufacturer;
	private String partName;
	private String description;
	private String url;
	private BigDecimal stock;
	private QuantityScale quantityScale;
	
	/**
	 * Bewertung, fuer die Reihung der Ergebnisse
	 */
	private float rate = 0;
	
	public NormalizedWebabfragePart(AbfrageTyp type, Object rawResponse) {
		this.type = type;
		this.rawResponse = rawResponse;
	}
	
	public Boolean isFindChipsPart() {
		return AbfrageTyp.FINDCHIPS.equals(type);
	}
	
	public Boolean isOemSecretsPart() {
		return AbfrageTyp.OEMSECRETS.equals(type);
	}
	
	public Object getRawResponseData() {
		return rawResponse;
	}

	public Object getRawPart() {
		return rawPart;
	}

	public void setRawPart(Object rawPart) {
		this.rawPart = rawPart;
	}

	public Integer getSearchId() {
		return searchId;
	}

	public void setSearchId(Integer searchId) {
		this.searchId = searchId;
	}

	public IWebpartnerDto getDistributor() {
		return distributor;
	}

	public void setDistributor(IWebpartnerDto distributor) {
		this.distributor = distributor;
	}

	public String getDistributorItemNo() {
		return distributorItemNo;
	}

	public void setDistributorItemNo(String distributorItemNo) {
		this.distributorItemNo = distributorItemNo;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public QuantityScale getQuantityScale() {
		return quantityScale;
	}

	public void setQuantityScale(QuantityScale quantityScale) {
		this.quantityScale = quantityScale;
	}
	
	public void setPriceByQuantity(BigDecimal quantity) {
		getQuantityScale().setPriceByQuantity(quantity);
	}

	public PartPrice getSelectedPrice() {
		return getQuantityScale().getSelectedPrice();
	}

	public PartPrice getPriceByQuantity(BigDecimal quantity) {
		return getQuantityScale().getPriceByQuantity(quantity);
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
