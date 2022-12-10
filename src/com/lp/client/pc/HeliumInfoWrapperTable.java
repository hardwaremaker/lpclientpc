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
package com.lp.client.pc;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.NonEditableTableModel;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.pc.DauerPing.TotalPingSystemInfo;
import com.lp.client.util.resolution.ScreenChangeResolutionAdapter;
import com.lp.client.util.resolution.DPIUtils.ScreenInfo;

public class HeliumInfoWrapperTable extends WrapperTable {
	private static final long serialVersionUID = -2706024330719141269L;

	private static final int PING_ROW_OFFSET = 0;
	private static final int DPI_ROW_OFFSET = 1;

	private TotalPingSystemInfo totalInfo;
	private int rN;

	public HeliumInfoWrapperTable(Vector<Vector<String>> vectorData, Vector<String> vectorColumns) {
		super((InternalFrame) null, new NonEditableTableModel(vectorData, vectorColumns));
		rN = vectorData.size() - 1;
		ScreenChangeResolutionAdapter resAdapter = ScreenChangeResolutionAdapter.createForComponent(this);
		resAdapter.addPropertyChangeListener(ScreenChangeResolutionAdapter.PROPERTY_SCREEN_INFO, this::screenInfoChange);
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		String tip = null;
		java.awt.Point p = event.getPoint();
		int rowIndex = rowAtPoint(p);
		int colIndex = columnAtPoint(p);

//        if(rowIndex != INFO_COLUMN) return super.getToolTipText() ;
		if (rowIndex != rN)
			return super.getToolTipText();

		if (colIndex == 0) {
			tip = "round-trip erhalten: " + totalInfo.getPingsReceived() + " von gesendet: " + totalInfo.getPingsSent();
		}

		else if (colIndex == 1) {
			tip = "Min-H\u00F6chstwert " + totalInfo.getMinTime() + "ms";
		}

		else if (colIndex == 2) {
			tip = "Max-H\u00F6chstwert " + totalInfo.getMaxTime() + "ms";
		}

		else if (colIndex == 3) {
			tip = "Med-H\u00F6chstwert " + totalInfo.getMedianTime() + "ms";
		}

		else {
			tip = "";
		}

		return tip;
	}

	// *** wp ***
	// ***

	public void setPingInfo(PingSystemInfo pingInfo, TotalPingSystemInfo totalInfo) {
		this.totalInfo = totalInfo;

		setValueAt("round-trip (" + this.totalInfo.getPingsReceived() + ")", getPingRow(), 0);
		setValueAt("min " + pingInfo.getMinTime() + " ms", getPingRow(), 1);
		setValueAt("max " + pingInfo.getMaxTime() + " ms", getPingRow(), 2);
		setValueAt("med " + pingInfo.getMedianTime() + " ms", getPingRow(), 3);
	}

	private void screenInfoChange(PropertyChangeEvent e) {
		updateScreenInfo((ScreenInfo) e.getNewValue());
	}
	
	public void updateScreenInfo(ScreenInfo screenInfo) {
		setValueAt(String.format("DPI: %.1f", screenInfo.dpi), getDpiRow(), 1);
		int realWidth = (int) (screenInfo.width * screenInfo.scalingFactor);
		int realHeight = (int) (screenInfo.height * screenInfo.scalingFactor);
		setValueAt(String.format("Auflösung: %dx%d (%dx%d)", (int) screenInfo.width, (int) screenInfo.height, realWidth, realHeight), getDpiRow(), 2);
		setValueAt(String.format("Skalierungsfaktor: %d%%", (int)(100 * screenInfo.scalingFactor)), getDpiRow(), 3);
	}

	// ***
	// *** wp ***

	private int getPingRow() {
		return rN - PING_ROW_OFFSET;
	}

	private int getDpiRow() {
		return rN - DPI_ROW_OFFSET;
	}

}
