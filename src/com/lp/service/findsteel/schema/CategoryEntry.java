package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class CategoryEntry implements Serializable {
	private static final long serialVersionUID = -6120840791754920310L;

	private String id;
	private String title;
	private String subtitle;
	private String image;
	private String[] information;
	private DocumentEntry[] documents;
	private FilterEntry[] filters;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String[] getInformation() {
		return information;
	}
	public void setInformation(String[] information) {
		this.information = information;
	}
	public DocumentEntry[] getDocuments() {
		return documents;
	}
	public void setDocuments(DocumentEntry[] documents) {
		this.documents = documents;
	}
	public FilterEntry[] getFilters() {
		return filters;
	}
	public void setFilters(FilterEntry[] filters) {
		this.filters = filters;
	}

}
