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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.UIManager;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.cib.CibIconWrapper;
import com.lp.client.frame.component.cib.ICibRectangleHolder;
import com.lp.util.Helper;

public class WrapperCheckBox extends JCheckBox implements IControl, IDirektHilfe, ICibRectangleHolder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isActivatable = true;
	private CornerInfoButton cib;
	private Rectangle iconRect = new Rectangle();

	public WrapperCheckBox() {
		setDefaults();
	}

	public WrapperCheckBox(String p0) {
		super(p0);
		setDefaults();
	}

	public WrapperCheckBox(String p0, boolean p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperCheckBox(Action p0) {
		super(p0);
		setDefaults();
	}

	public WrapperCheckBox(Icon p0) {
		super(p0);
		setDefaults();
	}

	public WrapperCheckBox(Icon p0, boolean p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperCheckBox(String p0, Icon p1) {
		super(p0, p1);
		setDefaults();
	}

	public WrapperCheckBox(String p0, Icon p1, boolean p2) {
		super(p0, p1, p2);
		setDefaults();
	}

	public void setShort(Short s) {
		this.setSelected(Helper.short2boolean(s));
	}

	public Short getShort() {
		return Helper.boolean2Short(isSelected());
	}

	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		this.setFocusable(isActivatable);
	}

	public boolean isActivatable() {
		return isActivatable;
	}

	public void setMandatoryField(boolean isMandatoryField) {
	}

	public boolean isMandatoryField() {
		return false;
	}

	public void removeContent() {
		this.setSelected(false);
	}

	public boolean saveReportInformation = true;

	public boolean isSaveReportInformation() {
		return saveReportInformation;
	}

	public void setSaveReportInformation(boolean saveReportInformation) {
		this.saveReportInformation = saveReportInformation;
	}

	private void setDefaults() {
		cib = new PaintingCornerInfoButton(this);
		HelperClient.setDefaultsToComponent(this);
		Color bgColor = Defaults.getInstance().getCheckboxBackgroundColor();
		if (bgColor != null) {
			setOpaque(true);
			setBackground(bgColor);
		}
		setMargin(new Insets(2, 0, 2, 2));
	}

	public void setMinimumSize(Dimension d) {
		super.setMinimumSize(new Dimension(d.width, Defaults.getInstance().getControlHeight()));
	}

	public void setMaximumSize(Dimension d) {
		super.setMaximumSize(new Dimension(d.width, Defaults.getInstance().getControlHeight()));
	}

	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(new Dimension(d.width, Defaults.getInstance().getControlHeight()));
	}
	
	/**
	 * L&auml;sst Swing automatisch die Gr&ouml;&szlig;e f&uuml;r diese Komponente bestimmen
	 */
	public void setAutoPreferredSize() {
		super.setPreferredSize(null);
	}

	@Override
	public void setToken(String token) {
		cib.setToolTipToken(token);
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}

	@Override
	public void removeCib() {
		cib = null;
	}

	@Override
	public String getToken() {
		return cib.getToolTipToken();
	}

	@Override
	public boolean hasContent() throws Throwable {
		return true;
	}

	// override getIcon Methods for CornerInfoButton Position
	@Override
	public Icon getIcon() {
		if (super.getIcon() == null)
			return null;
		for (Map.Entry<?, ?> map : UIManager.getDefaults().entrySet()) {
			if (map.getValue() instanceof Icon)
				System.out.println(map.getKey());
		}
		return new CibIconWrapper(super.getIcon());
	}

	@Override
	public Icon getDisabledIcon() {
		if (super.getDisabledIcon() == null)
			return null;
		return new CibIconWrapper(super.getDisabledIcon());
	}

	@Override
	public Icon getDisabledSelectedIcon() {
		if (super.getDisabledSelectedIcon() == null)
			return null;
		return new CibIconWrapper(super.getDisabledSelectedIcon());
	}

	@Override
	public Icon getPressedIcon() {
		if (super.getPressedIcon() == null)
			return null;
		return new CibIconWrapper(super.getPressedIcon());
	}

	@Override
	public Icon getRolloverIcon() {
		if (super.getRolloverIcon() == null)
			return null;
		return new CibIconWrapper(super.getRolloverIcon());
	}

	@Override
	public Icon getRolloverSelectedIcon() {
		if (super.getRolloverSelectedIcon() == null)
			return null;
		return new CibIconWrapper(super.getRolloverSelectedIcon());
	}

	@Override
	public Icon getSelectedIcon() {
		if (super.getSelectedIcon() == null)
			return null;
		return new CibIconWrapper(super.getSelectedIcon());
	}

	@Override
	public Rectangle getIconRect() {
		return iconRect;
	}
}
