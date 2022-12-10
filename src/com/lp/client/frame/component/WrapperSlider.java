
/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.frame.component;

import java.awt.Dimension;

import javax.swing.JSlider;

import com.lp.client.frame.Defaults;
import com.lp.client.pc.LPMain;

/**
 * <p>
 * Gewrappter RadioButton<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.8 $
 */
public class WrapperSlider extends JSlider implements IControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isActivatable = true;

	public WrapperSlider(int min, int max, int value) {
		super(HORIZONTAL, min, max, value);
	}

	public WrapperSlider() {
		super(HORIZONTAL, 0, 100, 100);
		setMajorTickSpacing(25);
		setMinorTickSpacing(25);
		setSnapToTicks(true);
		setPaintTicks(true);
		setPaintLabels(true);
		setToolTipText(LPMain.getTextRespectUISPr("pers.zeiterfassung.verrechenbar"));

		setValue(100);

	}

	/**
	 * isActivateable
	 * 
	 * @return boolean
	 */
	public boolean isActivatable() {
		return isActivatable;
	}

	/**
	 * isMandatoryField
	 * 
	 * @return boolean
	 */
	public boolean isMandatoryField() {
		return false;
	}

	/**
	 * setMandatoryField
	 * 
	 * @param isMandatoryField boolean
	 */
	public void setMandatoryField(boolean isMandatoryField) {
		// nix tun
	}

	/**
	 * setActivatable
	 * 
	 * @param isActivatable boolean
	 */
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		this.setFocusable(isActivatable);
	}

	public void removeContent() {
		this.setValue(0);
	}

	public void setDouble(Double d) {
		if (d != null) {
			super.setValue(d.intValue());
		}
	}

	public Double getDouble() {
		return new Double(getValue());
	}

	@Override
	public boolean hasContent() throws Throwable {
		return true;
	}

}
