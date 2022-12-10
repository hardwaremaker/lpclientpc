package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class ProductEntry implements Serializable {
	private static final long serialVersionUID = 444478050604827481L;

	private String id;
	private String name;
	private CategoryEntry category;
	private PropertyEntry[] properties;
	private String[] filterValues;
	private String[] descriptions;
	private ImageEntry[] images;
	private PriceEntry priceTable;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getFilterValues() {
		return filterValues;
	}
	public void setFilterValues(String[] filterValues) {
		this.filterValues = filterValues;
	}	
	public String[] getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String[] descriptions) {
		this.descriptions = descriptions;
	}
	public CategoryEntry getCategory() {
		return category;
	}
	public void setCategory(CategoryEntry category) {
		this.category = category;
	}
	public PropertyEntry[] getProperties() {
		return properties;
	}
	public void setProperties(PropertyEntry[] properties) {
		this.properties = properties;
	}
	public ImageEntry[] getImages() {
		return images;
	}
	public void setImages(ImageEntry[] images) {
		this.images = images;
	}
	public PriceEntry getPriceTable() {
		return priceTable;
	}
	public void setPriceTable(PriceEntry priceTable) {
		this.priceTable = priceTable;
	}
}
