package com.lp.client.frame.maps;

import com.lp.util.Helper;


public class Script {
	
	private boolean async = false;
	private boolean defer = false;
	private String source = null;
	private String type = null;
	private String content = null;
	
	public Script() {
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public boolean isDefer() {
		return defer;
	}

	public void setDefer(boolean defer) {
		this.defer = defer;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String asString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<script")
			.append(isAsync() ? " async" : "")
			.append(isDefer() ? " defer" : "");
		
		if (!Helper.isStringEmpty(getType())) 
			builder.append(" type=\"").append(getType()).append("\"");
		
		if (!Helper.isStringEmpty(getSource())) 
			builder.append(" src=\"").append(getSource()).append("\"");
		
		builder.append(">")
			.append(!Helper.isStringEmpty(getContent()) ? getContent() : "")
			.append("</script>");
		
		return builder.toString();
	}
}
