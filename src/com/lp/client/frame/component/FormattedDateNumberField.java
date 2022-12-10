package com.lp.client.frame.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FormattedDateNumberField extends WrapperTextNumberField implements PropertyChangeListener {
	private static final long serialVersionUID = -8409558184629147154L;

	private WrapperDateField dateField;
	private DateFormat dateFormat;

	public FormattedDateNumberField(WrapperDateField dateField, String datePattern) {
		this.dateField = dateField;
		this.dateFormat = new SimpleDateFormat(datePattern);
		initListener();
	}
	
	private void initListener() {
		dateField.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (!event.getPropertyName().equals("date")) return;
		
		setText(dateField.getDate() == null ? null : 
			dateFormat.format(dateField.getDate()));
	}

}
