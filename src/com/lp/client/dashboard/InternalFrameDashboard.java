
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
package com.lp.client.dashboard;

import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.lp.client.anfrage.ReportAnfrage;
import com.lp.client.artikel.ReportArtikelbestellt;
import com.lp.client.frame.component.ButtonAbstractAction;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelDialogStackElement;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.NachrichtenartDto;
import com.lp.server.personal.service.NachrichtengruppeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class InternalFrameDashboard extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int IDX_TABBED_PANE_DASHBOARD = 0;

	ReportDashboard reportDashboard = null;

	public InternalFrameDashboard(String title, String belegartCNr, String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// TabbedPane Personal
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr("db.dashboard"), null, null,
				LPMain.getInstance().getTextRespectUISPr("db.dashboard"), IDX_TABBED_PANE_DASHBOARD);

		registerChangeListeners();

		reportDashboard = new ReportDashboard(this, "Dashboard");

		PanelReportKriterien krit = new PanelReportKriterien(this, reportDashboard, "", null, null, true, true, false,
				false);

		int iMinuten = 0;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_DASHBOARD_AKTUALISIEREN, ParameterFac.KATEGORIE_ALLGEMEIN,
						LPMain.getTheClient().getMandant());

		if (parameter != null) {
			iMinuten = (Integer) parameter.getCWertAsObject();
			if (iMinuten > 0) {
				int zeitInMillis = 1000 * 60 * iMinuten;

				krit.setzeTimer(zeitInMillis);
			}

		}

		showPanelDialog(krit);

		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource("/com/lp/client/res/gauge.png"));
		setFrameIcon(iicon);

	}

	public void closePanelDialog() {
		return;
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource()).getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_DASHBOARD) {

		}

	}

}
