package com.lp.client.frame.component;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

public class FocusComponentAction extends AbstractAction {
	private static final long serialVersionUID = -1552653634869559665L;

	private JComponent component;
	
	public FocusComponentAction(JComponent component) {
		this.component = component;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (component != null)
			component.requestFocusInWindow();
	}
}
