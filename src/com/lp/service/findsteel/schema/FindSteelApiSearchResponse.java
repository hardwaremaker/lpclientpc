package com.lp.service.findsteel.schema;

import java.io.Serializable;

import com.lp.service.findsteel.FindSteelApiBaseResponse;

public class FindSteelApiSearchResponse extends FindSteelApiBaseResponse implements Serializable {
	private static final long serialVersionUID = -1902747251658048333L;
	
	private SearchResponseEntry[] content;

	public FindSteelApiSearchResponse() {
		super();
		setContent(new SearchResponseEntry[1]);
	}
	
	public SearchResponseEntry[] getContent() {
		return content;
	}

	public void setContent(SearchResponseEntry[] content) {
		this.content = content;
	}
}
