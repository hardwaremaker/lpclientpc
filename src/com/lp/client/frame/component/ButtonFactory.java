package com.lp.client.frame.component;

import javax.swing.Icon;
import javax.swing.JButton;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;

public class ButtonFactory {

	public static JButton createJButton(Icon icon, String tooltip, String actionCommand) {

		BorderButton button = new BorderButton();

		button.setToolTipText(tooltip);
		button.setActionCommand(actionCommand);
		button.setIcon(icon);
		HelperClient.setButtonBackgroundBorderColor(button, 
				Defaults.getInstance().getButtonBackgroundColor(), 
				Defaults.getInstance().getButtonBorderColor());
		//Border wird in HelperClient Methode gesetzt
		button.setDefaultBorder(button.getBorder());
		return button;
	}
	
	public static JButton createJButton(Icon icon, String actionCommand) {
		return createJButton(icon, null, actionCommand);
	}
	
	public static JButton createJButton(Icon icon) {
		return createJButton(icon, null, null);
	}
	
	public static JButton createJButton() {
		return createJButton(null, null, null);
	}
	
	public static JButton createJButtonNotEnabled(Icon icon, String tooltip, String actionCommand) {
		JButton button = createJButton(icon, tooltip, actionCommand);
		button.setEnabled(false);
		return button;
	}

	public static JButton createJButtonNotEnabled(Icon icon) {
		JButton button = createJButton(icon);
		button.setEnabled(false);
		return button;
	}
	
	public static WrapperButton createWrapperButton(Icon icon, String tooltip, String actionCommand) {
		WrapperButton button = new WrapperButton();

		button.setToolTipText(tooltip);
		button.setActionCommand(actionCommand);
		button.setIcon(icon);

		return button;
	}
	
	public static WrapperButton createWrapperButtonNotEnabled(Icon icon, String tooltip, String actionCommand) {
		WrapperButton wrb = createWrapperButton(icon, tooltip, actionCommand);
		wrb.setEnabled(false);
		return wrb;
	}

}
