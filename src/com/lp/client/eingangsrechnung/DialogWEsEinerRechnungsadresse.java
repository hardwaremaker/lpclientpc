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
package com.lp.client.eingangsrechnung;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogWEsEinerRechnungsadresse extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryWEs = null;

	public PanelQuery getPanelQueryWEs() {
		return panelQueryWEs;
	}

	private JLabel werte = new JLabel();

	private PanelBasis panelDetailTransportkostenWE = null;
	private PanelBasis panelSplitWEs = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private InternalFrame internalFrame = null;
	private EingangsrechnungDto erDto = null;
	private TabbedPaneEingangsrechnung tpER = null;

	public DialogWEsEinerRechnungsadresse() {
		// nothing here
	}

	public DialogWEsEinerRechnungsadresse(EingangsrechnungDto erDto, InternalFrame internalFrame,
			TabbedPaneEingangsrechnung tpER) throws Throwable {
		super(LPMain.getInstance().getDesktop(),
				LPMain.getInstance().getTextRespectUISPr("er.weseinerrechnungadresse.auswaehlen"), true);
		this.tpER = tpER;
		this.internalFrame = internalFrame;
		this.erDto = erDto;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbInit();
		pack();
		setSize(800, 700);

		panelQueryWEs.updateButtons(new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
		panelDetailTransportkostenWE.updateButtons(new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
		aktualisiereWerte();

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			internalFrame.removeItemChangedListener(panelDetailTransportkostenWE);
			panelDetailTransportkostenWE = null;
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("flrbestellung.lieferant_i_id_rechnungsadresse", true,
				erDto.getLieferantIId() + "", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium("eingangsrechnung_i_id", true, "",
				FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NULL, false);

		/*
		 * kriterien[2] = new FilterKriterium( "flrbestellung.bestellungstatus_c_nr",
		 * true, "'" + LocaleFac.STATUS_GELIEFERT + "'", FilterKriterium.OPERATOR_EQUAL,
		 * false);
		 */

		panelQueryWEs = new PanelQuery(null, kriterien, QueryParameters.UC_ID_WE_EINER_RECHNUNGSADRESSE, null,
				internalFrame, LPMain.getInstance().getTextRespectUISPr("er.weseinerrechnungadresse.auswaehlen"), true);

		panelQueryWEs.addDirektFilter(new FilterKriteriumDirekt("flrbestellung.c_nr", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.bestellnummer"), FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung
																											// als
																											// '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT)); // ignore case);
		panelQueryWEs.addDirektFilter(new FilterKriteriumDirekt("c_lieferscheinnr", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("bes.lieferscheinnummer"), FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																												// als
																												// '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT)); // ignore case);

		panelQueryWEs.setMultipleRowSelectionEnabled(true);
		panelQueryWEs.addButtonAuswahlBestaetigen(null);

		panelQueryWEs.eventYouAreSelected(false);

		panelQueryWEs.setMultipleRowSelectionEnabled(true);

		panelQueryWEs.getToolBar().getToolsPanelLeft().add(werte);

		panelDetailTransportkostenWE = new PanelTransportkostenEinesWEs(internalFrame,
				LPMain.getInstance().getTextRespectUISPr("artikel.fehlmengen.aufloesen"), panelQueryWEs, this);

		panelSplitWEs = new PanelSplit(internalFrame, panelDetailTransportkostenWE, panelQueryWEs, 380);

		panelDetailTransportkostenWE.setKeyWhenDetailPanel(panelQueryWEs.getSelectedId());
		panelDetailTransportkostenWE.eventYouAreSelected(false);
		panelQueryWEs.eventYouAreSelected(false);
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(panelSplitWEs, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	public void aktualisiereWerte() throws Throwable {

		BigDecimal bdSummeMarkiertUndBereitszugewiesen = BigDecimal.ZERO;
		BigDecimal bdNettowertER = tpER.getNettowertER();

		// Bereits zugewiesene
		WareneingangDto[] weDtos = DelegateFactory.getInstance().getWareneingangDelegate()
				.wareneingangFindByEingangsrechnungIId(erDto.getIId());

		for (int i = 0; i < weDtos.length; i++) {
			bdSummeMarkiertUndBereitszugewiesen = bdSummeMarkiertUndBereitszugewiesen.add(DelegateFactory.getInstance()
					.getWareneingangDelegate().berechneWertDesWareneingangsInBestellungswaehrung(weDtos[i].getIId()));

		}

		ArrayList<Integer> ids = panelQueryWEs.getSelectedIdsAsInteger();
		if (ids != null && ids.size() > 0) {

			for (int i = 0; i < ids.size(); i++) {
				bdSummeMarkiertUndBereitszugewiesen = bdSummeMarkiertUndBereitszugewiesen
						.add(DelegateFactory.getInstance().getWareneingangDelegate()
								.berechneWertDesWareneingangsInBestellungswaehrung(ids.get(i)));

			}
		}
		BigDecimal diff = bdNettowertER.subtract(bdSummeMarkiertUndBereitszugewiesen);

		if (bdNettowertER.doubleValue() != Helper.rundeKaufmaennisch(bdSummeMarkiertUndBereitszugewiesen, 2)
				.doubleValue()) {
			werte.setForeground(Color.RED);
		} else {
			werte.setForeground(new Color(89, 188, 41));
		}

		werte.setText(LPMain.getMessageTextRespectUISPr("er.we.summemarkierte.unddifferenz",
				Helper.formatZahl(bdNettowertER, 2, LPMain.getTheClient().getLocUi()),
				Helper.formatZahl(bdSummeMarkiertUndBereitszugewiesen, 2, LPMain.getTheClient().getLocUi()),
				Helper.formatZahl(diff, 2, LPMain.getTheClient().getLocUi())));
	}

}
