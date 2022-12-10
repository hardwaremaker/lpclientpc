
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
package com.lp.client.cockpit;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.artikel.TabbedPaneMaterial;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.TabbedPanePartner;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.KommunikationsartDto;
import com.lp.server.partner.service.NewslettergrundDto;
import com.lp.server.partner.service.PartnerartDto;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.partner.service.SelektionDto;

@SuppressWarnings("static-access")
/**
 * <p>
 * Diese Klasse kuemmert sich um den Partner.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 *
 * <p>
 * Organisation:
 * </p>
 *
 * @author $Author: christian $
 * @version $Revision: 1.5 $
 */
public class InternalFrameCockpit extends InternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int IDX_PANE_COCKPIT = 0;
	public static int IDX_PANE_TELEFON_TODO = 1;

	private TabbedPaneCockpit tpCockpit = null;
	private TabbedPaneTelefonTodo tpTelefonTodo = null;

	private String rechtModulweit = null;

	public InternalFrameCockpit(String title, String belegartCNr, String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		rechtModulweit = sRechtModulweitI;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		// 1 unteres tab: Partner; lazy loading; ist auch default.
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr("cp.cockpit"), null, tpCockpit,
				LPMain.getInstance().getTextRespectUISPr("cp.cockpit"), IDX_PANE_COCKPIT);

		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr("cp.cockpit.telefontodo"), null, null,
				LPMain.getInstance().getTextRespectUISPr("cp.cockpit.telefontodo"), IDX_PANE_TELEFON_TODO);

		// Defaulttabbedpane setzen.
		refreshPartnerTP();
		tpCockpit.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tpCockpit);

		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		// awt: listener bin auch ich.
		registerChangeListeners();

		// dem frame das icon setzen
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource("/com/lp/client/res/control_tower.png"));
		setFrameIcon(iicon);
		// Menue
		// setJMenuBar(new WrapperMenuBar(this));
	}

	// TODO-AGILCHANGES
	/**
	 * AGILPRO CHANGES Changed visiblity from protected to public
	 * 
	 * @author Lukas Lisowski
	 * @param e EventObject
	 * @throws Throwable
	 */
	public void lPStateChanged(EventObject e) throws Throwable {

		setRechtModulweit(rechtModulweit);

		// TODO-AGILCHANGES
		/**
		 * AGILPRO CHANGES BEGIN
		 * 
		 * @author Lukas Lisowski
		 */
		int selectedCur = 0;

		try {
			selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();
		} catch (Exception ex) {

			selectedCur = ((Desktop) e.getSource()).getSelectedIndex();
		}

		if (selectedCur == IDX_PANE_COCKPIT) {
			refreshPartnerTP();
			tpCockpit.lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN, LPMain.getInstance().getTextRespectUISPr("cp.cockpit"));
		} else if (selectedCur == IDX_PANE_TELEFON_TODO) {
			createTabbedPaneMaterial(tpTelefonTodo);
			// Info an Tabbedpane, bist selektiert worden.
			tpTelefonTodo.lPEventObjectChanged(null);
		}
	}

	private void createTabbedPaneMaterial(JTabbedPane tabbedPane) throws Throwable {
		if (tpTelefonTodo == null) {
			// lazy loading
			tpTelefonTodo = new TabbedPaneTelefonTodo(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_TELEFON_TODO, tpTelefonTodo);

			initComponents();
		}

	}

	public void lPEventItemChanged(ItemChangedEvent e) {
		// nothing here
	}

	public TabbedPaneCockpit getTpCockpit() {
		return tpCockpit;
	}

	private void refreshPartnerTP() throws Throwable {
		if (tpCockpit == null) {
			tpCockpit = new TabbedPaneCockpit(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_COCKPIT, tpCockpit);
			initComponents();
		}
	}

	protected void menuActionPerformed(ActionEvent e) throws Throwable {

	}

	public PropertyVetoException vetoableChangeLP() throws Throwable {

		return super.vetoableChangeLP();
	}

}
