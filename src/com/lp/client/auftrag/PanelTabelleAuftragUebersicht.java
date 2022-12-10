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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragFac;

@SuppressWarnings("static-access")
/**
 * @author Uli Walch
 */
public class PanelTabelleAuftragUebersicht extends PanelTabelle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameAuftrag intFrame = null;
	private TabbedPaneAuftrag tpAuftrag = null;

	private WrapperLabel wlaKritAuswertung = null;
	private WrapperLabel wlaKritAuswertung2 = null;

	// das soll in der optionalen zweiten Zeile stehen
	private WrapperLabel wlaEmpty = null;
	private WrapperLabel wlaOffene = null;
	private WrapperLabel wlaEingang = null;

	/**
	 * PanelTabelle.
	 * 
	 * @param iUsecaseIdI
	 *            die eindeutige UseCase ID
	 * @param sTitelTabbedPaneI
	 *            der Titel des aktuellen TabbedPane
	 * @param oInternalFrameI
	 *            der uebergeordente InternalFrame
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelTabelleAuftragUebersicht(int iUsecaseIdI,
			String sTitelTabbedPaneI, InternalFrame oInternalFrameI)
			throws Throwable {
		super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);

		try {
			intFrame = (InternalFrameAuftrag) oInternalFrameI;
			tpAuftrag = intFrame.getTabbedPaneAuftrag();

			jbInit();
			initComponents();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	/**
	 * Initialisiere alle Komponenten; braucht der JBX-Designer; hier bitte
	 * keine wilden Dinge wie zum Server gehen, etc. machen.
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		wlaKritAuswertung = new WrapperLabel();
		wlaKritAuswertung2 = new WrapperLabel();

		wlaEmpty = new WrapperLabel();
		wlaEmpty.setMaximumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));
		wlaEmpty.setMinimumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));
		wlaEmpty.setPreferredSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));

		wlaOffene = new WrapperLabel("    Offene    ");
		wlaOffene.setHorizontalAlignment(SwingConstants.CENTER);
		wlaEingang = new WrapperLabel("    Eingang    ");
		wlaEingang.setHorizontalAlignment(SwingConstants.CENTER);

		getPanelOptionaleZweiteZeile().add(
				wlaEmpty,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaOffene,
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaEingang,
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		// die erste Spalte ist immer der ZeilenHeader
		TableColumn tcZeilenHeader = table.getColumnModel().getColumn(1);
		tcZeilenHeader.setCellRenderer(new ZeilenHeaderRenderer());
		tcZeilenHeader.setPreferredWidth(SPALTENBREITE_ZEILENHEADER);

		TableColumn tcEmpty = getTable().getColumnModel().getColumn(5); // @todo
																		// not
																		// hardcoded
																		// Pj
																		// 5118
		tcEmpty.setCellRenderer(new LeeresFeldRenderer());
		tcEmpty.setPreferredWidth(SPALTENBREITE_LEERESPALTE);

		setFirstColumnVisible(false);
	}

	/**
	 * eventActionRefresh
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoRefreshI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		wlaKritAuswertung
				.setText(getDefaultFilter()[AuftragFac.IDX_KRIT_AUSWERTUNG]
						.formatFilterKriterium(LPMain.getInstance()
								.getTextRespectUISPr("lp.auswertung"))+" + " +getDefaultFilter()[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_PLUS_JAHRE].value);
		wlaKritAuswertung.setMaximumSize(new Dimension(350, 23));
		wlaKritAuswertung.setMinimumSize(new Dimension(350, 23));
		wlaKritAuswertung.setPreferredSize(new Dimension(350, 23));
		wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaKritAuswertung2
				.setText(LPMain.getInstance().getTextRespectUISPr(
						"lp.auswertung")
						+ " "
						+ getDefaultFilter()[AuftragFac.UMSATZUEBERSICHT_IDX_KRIT_AUSWERTUNG].kritName);
		wlaKritAuswertung2.setMaximumSize(new Dimension(350, 23));
		wlaKritAuswertung2.setMinimumSize(new Dimension(350, 23));
		wlaKritAuswertung2.setPreferredSize(new Dimension(350, 23));
		wlaKritAuswertung2.setHorizontalAlignment(SwingConstants.LEADING);

		getPanelFilterKriterien().add(wlaKritAuswertung);
		getPanelFilterKriterien().add(wlaKritAuswertung2);

		String cNrMandantenwaehrung = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr();

		getTable()
				.getColumnModel()
				.getColumn(1)
				.setHeaderValue(
						LPMain.getInstance()
								.getTextRespectUISPr("lp.anzeigein")
								+ " "
								+ cNrMandantenwaehrung);
	}
}
