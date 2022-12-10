package com.lp.client.frame.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;

public class WrapperPasswordFieldPlain extends WrapperPasswordField {
	private static final long serialVersionUID = -2737028149260011822L;

	private WrapperCheckBox wcbShowPlainText;
	private Character defaultEchoChar;
	private boolean showPlainTextAllowed = false;
	
	public WrapperPasswordFieldPlain() {
		super();
		initCheckbox();
	}
	
	public WrapperPasswordFieldPlain(int columnsMax) {
		super(columnsMax);
		initCheckbox();
	}
	
	public WrapperPasswordFieldPlain(String text, int columnsMax) {
		super(text, columnsMax);
		initCheckbox();
	}

	private void initCheckbox() {
		defaultEchoChar = getEchoChar();
		wcbShowPlainText = new WrapperCheckBox(LPMain.getTextRespectUISPr("label.passwortanzeigen"));
		wcbShowPlainText.addActionListener(new ShowPlainTextListener());
		editModeDone();
		
		HelperClient.setComponentNames(this);
	}
	
	public JComponent getShowPlainTextComponent() {
		return wcbShowPlainText;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		wcbShowPlainText.setVisible(showPlainTextAllowed && visible);
	}
	
	@Override
	public void setActivatable(boolean isActivatable) {
		super.setActivatable(isActivatable);
		wcbShowPlainText.setActivatable(isActivatable);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		wcbShowPlainText.setEnabled(enabled);
	}
	
	public void setShowPlainTextAllowed(boolean showPlainTextAllowed) {
		this.showPlainTextAllowed = showPlainTextAllowed;
	}
	
	public void editMode() {
		if (showPlainTextAllowed) {
			wcbShowPlainText.setVisible(true);
		}
	}
	
	public void editModeDone() {
		wcbShowPlainText.setVisible(false);
	}
	
	private class ShowPlainTextListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setEchoChar(wcbShowPlainText.isSelected() ? '\u0000' : defaultEchoChar);
		}
	}
	
	public boolean validateLeadingTrailingSpaces() {
		String pwd = new String(getPassword());
		return pwd == null || pwd.equals(pwd.trim());
	}
}
