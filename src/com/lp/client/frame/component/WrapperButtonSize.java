package com.lp.client.frame.component;

import java.awt.Dimension;

import com.lp.client.frame.Defaults;

/**
 * WrapperButton implementierung, die bei defaults preffered size nicht setzt
 * und diese dynamisch von der UI, aber mit Defaults.getControlHeight() als
 * H&ouml;he berechnet. <br>
 * 
 * @author Alexander Daum
 *
 */
public class WrapperButtonSize extends WrapperButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setDefaults() {
		super.setDefaults();
		setPreferredSizeImpl(null);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension prefSize = super.getPreferredSize();
		prefSize.height = Defaults.getInstance().getControlHeight();
		return prefSize;
	}
}
