package com.lp.client.frame.component;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class BorderButton extends JButton {

	private static final long serialVersionUID = -6586856738172235802L;

	private Border defaultBorder = null;
	private boolean defaultBorderSet = false;

	public BorderButton() {
		super();
	}

	public BorderButton(Action a) {
		super(a);
	}

	public BorderButton(Icon icon) {
		super(icon);
	}

	public BorderButton(String text, Icon icon) {
		super(text, icon);
	}

	public BorderButton(String text) {
		super(text);
	}

	public void setDefaultBorder(Border border) {
		defaultBorder = border;
		defaultBorderSet = true;
	}
	
	public void removeDefaultBorder() {
		defaultBorderSet = false;
		defaultBorder = null;
	}

	public void activateDefaultBorder() {
		if(defaultBorderSet) {
			setBorder(defaultBorder);
		}
	}

	@Override
	public void updateUI() {
		super.updateUI();
		activateDefaultBorder();
	}

}
