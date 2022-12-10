package com.lp.editor.util;

import java.util.ArrayList;
import java.util.List;

import com.lp.util.Helper;

public class StyledTextConverter {

	private List<IStyledText> textParts;
	
	public StyledTextConverter() {
	}

	public List<IStyledText> getTextParts() {
		if (textParts == null) {
			textParts = new ArrayList<IStyledText>();
		}
		return textParts;
	}
	
	public String toHtml(String text) {
		textParts = null;
		parseStyledText(text);
		return asHtmlImpl();
	}

	private String asHtmlImpl() {
		StringBuilder builder = new StringBuilder();
		for (IStyledText styledText : getTextParts()) {
			builder.append(styledText.asHtml());
		}
		return builder.toString();
	}

	private void parseStyledText(String text) {
		int idx = 0;
		int styleStartIdx = 0;
		int txtStartIdx = 0;
		int txtEndIdx = 0;
		
		while (idx < text.length()) {
			styleStartIdx = text.indexOf("<style", idx);
			if (idx != styleStartIdx) {
				analyse(text.substring(idx, styleStartIdx < 0 ? text.length() : styleStartIdx), "");
			}
			
			if (styleStartIdx < 0) {
				break;
			}
			
			txtStartIdx = text.indexOf(">", styleStartIdx) + 1;
			txtEndIdx = text.indexOf("</style>", txtStartIdx);
			if (txtStartIdx < 0 || txtEndIdx < 0) {
				break;
			}
			
			analyse(text.substring(txtStartIdx, txtEndIdx), text.substring(styleStartIdx, txtStartIdx - 1));
			idx = text.indexOf(">", txtEndIdx) + 1;
		}
	}

	private void analyse(String text, String styleInfo) {
		if (text.contains("\n")) {
			List<IStyledText> decorated = splitAndDecorate(text, styleInfo, "\n", new ISpecialCharDecorate() {
				public IStyledText decorate(IStyledText toDecorate) {
					return new LineBreakDecorator(toDecorate);
				}
			});
			getTextParts().addAll(decorated);
		} else {
			getTextParts().add(decorate(new PlainText(text), styleInfo));
		}
	}
	
	private List<IStyledText> splitAndDecorate(String text, String styleInfo, String specialChars, ISpecialCharDecorate decorator) {
		List<IStyledText> decoratedList = new ArrayList<IStyledText>();
		int textStartIdx = 0;
		int i = 0;
		for ( ; i <= text.length() - specialChars.length(); i++) {
			if (specialChars.equals(text.substring(i, i + specialChars.length()))) {
				IStyledText decorated = decorate(new PlainText(text.substring(textStartIdx, i)), styleInfo);
				int k = i;
				for ( ; k <= text.length() - specialChars.length(); k++) {
					if (specialChars.equals(text.substring(k, k + specialChars.length()))) {
						decorated = decorator.decorate(decorated);
					} else {
						break;
					}
				}
				i = k - 1;
				textStartIdx = k;
				decoratedList.add(decorated);
			}
		}
		if (textStartIdx < i) {
			decoratedList.add(decorate(new PlainText(text.substring(textStartIdx, text.length())), styleInfo));
		}
		return decoratedList;
	}

	private interface ISpecialCharDecorate {
		IStyledText decorate(IStyledText toDecorate);
	}

	private IStyledText decorate(IStyledText toDecorate, String styleInfo) {
		if (Helper.isStringEmpty(styleInfo))
			return toDecorate;
		
		if (isAttributeTrue(styleInfo, "isBold")) {
			toDecorate = new BoldDecorator(toDecorate);
		}
		
		if (isAttributeTrue(styleInfo, "isItalic")) {
			toDecorate = new ItalicDecorator(toDecorate);
		}
		
		if (isAttributeTrue(styleInfo, "isUnderline")) {
			toDecorate = new UnderlineDecorator(toDecorate);
		}
		
		return toDecorate;
	}

	private boolean isAttributeTrue(String styleInfo, String attribute) {
		String value = getStyleValue(styleInfo, attribute);
		return isTrue(value);
	}
	
	private boolean isTrue(String value) {
		return value != null && value.toLowerCase().equals("true");
	}
	
	private String getStyleValue(String styleInfo, String attribute) {
		String preValueStr = attribute + "=\"";
		int idxStart = styleInfo.indexOf(preValueStr);
		if (idxStart == -1)
			return null;
		
		idxStart = idxStart + preValueStr.length();
		int idxEnd = styleInfo.indexOf("\"", idxStart);
		return styleInfo.substring(idxStart, idxEnd);
	}
}
