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
package com.lp.client.nachrichten;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;
import com.lp.client.reklamation.TabbedPaneReklamationGrunddaten;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.personal.service.NachrichtenartDto;
import com.lp.server.personal.service.NachrichtengruppeDto;
import com.lp.server.reklamation.service.ReklamationDto;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class InternalFrameNachrichten extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneNachrichtenart tabbedPaneNachrichtenart = null;
	private TabbedPaneNachrichtengruppe tabbedPaneGruppen = null;
	private TabbedPaneNachrichtenverlauf tabbedPaneVerlauf = null;

	public static final int IDX_TABBED_PANE_NACHRICHTENART = 0;
	public static final int IDX_TABBED_PANE_GRUPPEN = 1;
	public static final int IDX_TABBED_PANE_VERLAUF = 2;

	public NachrichtenartDto nachrichtenartDto = null;
	public NachrichtengruppeDto nachrichtengruppeDto = null;

	public NachrichtengruppeDto getNachrichtengruppeDto() {
		return nachrichtengruppeDto;
	}

	public void setNachrichtengruppeDto(NachrichtengruppeDto nachrichtengruppeDto) {
		this.nachrichtengruppeDto = nachrichtengruppeDto;
	}

	public NachrichtenartDto getNachrichtenartDto() {
		return nachrichtenartDto;
	}

	public void setNachrichtenartDto(NachrichtenartDto nachrichtenartDto) {
		this.nachrichtenartDto = nachrichtenartDto;
	}


	public TabbedPaneNachrichtenart getTabbedPaneReklamation() {
		return tabbedPaneNachrichtenart;
	}

	

	public InternalFrameNachrichten(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {
		super(title, belegartCNr, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// TabbedPane Personal
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenart"),
				IDX_TABBED_PANE_NACHRICHTENART);
		
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"pers.nachrichtengruppen"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.nachrichtengruppen"),
							IDX_TABBED_PANE_GRUPPEN);
			
			
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"pers.nachrichtenverlauf"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.nachrichtenverlauf"),
							IDX_TABBED_PANE_VERLAUF);
		
		registerChangeListeners();
		createTabbedPaneNachrichtenart(null);
		tabbedPaneNachrichtenart.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPaneNachrichtenart);
		// iicon: hier das li/on icon gemacht
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/messages.png"));
		setFrameIcon(iicon);

	}

	private void createTabbedPaneNachrichtenart(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneNachrichtenart = new TabbedPaneNachrichtenart(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_NACHRICHTENART,
					tabbedPaneNachrichtenart);
			initComponents();

		}
	}

	private void createTabbedPaneGruppen(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneGruppen = new TabbedPaneNachrichtengruppe(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUPPEN,
					tabbedPaneGruppen);
			initComponents();
		}
	}
	private void createTabbedPaneVerlauf(JTabbedPane tabbedPane)
			throws Throwable {
		if (tabbedPane == null) {
			// lazy loading
			tabbedPaneVerlauf = new TabbedPaneNachrichtenverlauf(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_VERLAUF,
					tabbedPaneVerlauf);
			initComponents();
		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		JTabbedPane tabbedPane = (JTabbedPane) ((JTabbedPane) e.getSource())
				.getSelectedComponent();
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_TABBED_PANE_NACHRICHTENART) {
			createTabbedPaneNachrichtenart(tabbedPane);
			tabbedPaneNachrichtenart.lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_TABBED_PANE_GRUPPEN) {
			createTabbedPaneGruppen(tabbedPane);
			tabbedPaneGruppen.lPEventObjectChanged(null);
		}else if (selectedCur == IDX_TABBED_PANE_VERLAUF) {
			createTabbedPaneVerlauf(tabbedPane);
			tabbedPaneVerlauf.lPEventObjectChanged(null);
		}

	}

}
