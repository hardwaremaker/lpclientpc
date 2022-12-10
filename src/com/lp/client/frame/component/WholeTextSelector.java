package com.lp.client.frame.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

public class WholeTextSelector implements FocusListener {
	
	private JTextComponent textComponent;
	
	public WholeTextSelector(JTextComponent textComponent) {
		setTextComponent(textComponent);
	}
	
	public void setTextComponent(JTextComponent textComponent) {
		this.textComponent = textComponent;
	}
	public JTextComponent getTextComponent() {
		return textComponent;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (getTextComponent() == null) return;
		
		getTextComponent().selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

}
