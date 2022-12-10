package com.lp.client.frame.component;

public class AutoAuszugsnummerField extends FormattedDateNumberField {
	private static final long serialVersionUID = -1624975494645790552L;

	public AutoAuszugsnummerField(WrapperDateField dateField) {
		super(dateField, "yyyyMMdd");
		setActivatable(false);
	}
}
