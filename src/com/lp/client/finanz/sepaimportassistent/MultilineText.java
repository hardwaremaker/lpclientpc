package com.lp.client.finanz.sepaimportassistent;

import java.util.ArrayList;
import java.util.List;

public class MultilineText {
	
	private List<String> lines;

	public MultilineText() {
	}

	public List<String> getLines() {
		if (lines == null) {
			lines = new ArrayList<String>();
		}
		return lines;
	}
	
	public void addLine(String value) {
		getLines().add(value);
	}
	
	public String asString() {
		return asStringImpl("\n");
	}
	
	public String asString(int size) {
		String text = asStringImpl(" | ");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < text.length(); i += size) {
			builder.append(text.substring(i, Math.min(i + size, text.length())));
			if (i + size < text.length()) builder.append("\n");
		}
		
		return builder.toString();
	}
	
	private String asStringImpl(String separation) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getLines().size(); i++) {
			builder.append(getLines().get(i));
			if (i < getLines().size()-1) builder.append(separation);
		}
		return builder.toString();
	}
}
