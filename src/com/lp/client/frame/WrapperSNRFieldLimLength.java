package com.lp.client.frame;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.lp.client.frame.component.WrapperSNRField;

public class WrapperSNRFieldLimLength extends WrapperSNRField {

	private static final long serialVersionUID = 1L;

	private int limit = 0;

	@Override
	protected Document createDefaultModel() {
		return new SNRDocument(limit);
	}

	public WrapperSNRFieldLimLength() throws Throwable {
		super();
	}
	
	public WrapperSNRFieldLimLength(int maxZeichen) throws Throwable {
		super();
		setLimit(maxZeichen);
	}

	public int getLimit() {
		return limit;
	}

	/**
	 * Setze maximale Anzahl an Zeichen. 0 beduetet kein Limit
	 * 
	 * @param limit
	 */
	public void setLimit(int limit) {
		limit = Math.max(0, limit);
		Document doc = getDocument();
		if (doc != null && doc instanceof SNRDocument) {
			((SNRDocument) doc).setLimit(limit);
		}
		this.limit = limit;
	}

	protected class SNRDocument extends WrapperSNRField.NumberDocument {

		private static final long serialVersionUID = 1L;

		private int limit;

		public SNRDocument(int limit) {
			setLimit(limit);
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null) {
				return;
			}
			// 0 == kein Limit
			if (limit == 0) {
				super.insertString(offset, str, attr);
			}
			int iLength = getLength() + str.length();
			if (iLength <= limit) {
				super.insertString(offset, str, attr);
			} else {
				int iAvailableLength = getLimit() - getLength();
				String s = str.substring(0, iAvailableLength);
				super.insertString(offset, s, attr);
			}
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = Math.max(limit, 0);
		}

	}
}
