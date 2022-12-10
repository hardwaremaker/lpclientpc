
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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperButtonZusammenfuehren;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.util.Facade;

/*
 * <p> Diese Klasse kuemmert sich um das Zusammenf&uuml;ren von Partnern</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 * 
 * <p>Erstellung: Vorname Nachname; 01.08</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2012/11/07 09:41:44 $
 */
public class PanelDialogHerstellerZusammenfuehren extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED + "save";
	private final String ACTION_ZIELHERSTELLER_AUSWAEHLEN = "ACTION_ZIELPARTNER_AUSWAEHLEN";
	private final String ACTION_QUELLHERSTELLER_AUSWAEHLEN = "ACTION_QUELLPARTNER_AUSWAEHLEN";

	private JButton saveButton = null;

	private HerstellerDto herstellerZielDto = null;
	private HerstellerDto herstellerQuellDto = null;

	private PanelQueryFLR panelArtikel = null;
	protected boolean bHandleEventInSuperklasse = true;

	private JList listError = new JList();
	private JScrollPane jScrollPaneError = null;

	private JList listWarning = new JList();
	private JScrollPane jScrollPaneWarnig = null;

	private WrapperButton wbuZielHerstellerAuswaehlen = null;
	private WrapperButton wbuQuellHerstellerAuswaehlen = null;

	private WrapperTextField wtfQuellHersteller = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	private WrapperTextField wtfZielHersteller = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	private InternalFrameArtikel internalFrame = null;

	public PanelDialogHerstellerZusammenfuehren(HerstellerDto herstellerQuellDto, InternalFrameArtikel internalFrame,
			String add2TitleI) throws Throwable {

		super(internalFrame, add2TitleI);
		this.internalFrame = internalFrame;
		this.herstellerQuellDto = herstellerQuellDto;

		if (herstellerQuellDto != null) {
			wtfQuellHersteller.setText(herstellerQuellDto.getPartnerDto().formatFixName1Name2());
		}

		jbInit(); // zuerst QuellFelder leer, erst nach Quellauswahl Werte drin
		initComponents();
		setSaveButtonStatus();
		pruefeObZusammenfuehrenMoeglich();
	}

	private void setSaveButtonStatus() throws Throwable {
		if (this.herstellerQuellDto != null && this.herstellerZielDto != null) {
			this.saveButton.setEnabled(true);
		} else {
			this.saveButton.setEnabled(false);
		}
	}

	private void jbInit() throws Throwable {

		jScrollPaneError = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		jScrollPaneWarnig = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Ziel-Partner-Auswahlbutton
		wbuZielHerstellerAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuZielHerstellerAuswaehlen
				.setText(LPMain.getTextRespectUISPr("hersteller.zielhersteller.auswaehlen"));
		wbuZielHerstellerAuswaehlen.setToolTipText(LPMain.getTextRespectUISPr("button.hersteller.tooltip"));
		wbuZielHerstellerAuswaehlen.addActionListener(this);
		wbuZielHerstellerAuswaehlen.setActionCommand(ACTION_ZIELHERSTELLER_AUSWAEHLEN);
		wbuZielHerstellerAuswaehlen.setFocusable(true);

		// Quell-Partner-Auswahlbutton
		wbuQuellHerstellerAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuQuellHerstellerAuswaehlen
				.setText(LPMain.getTextRespectUISPr("hersteller.quellhersteller.auswaehlen"));
		wbuQuellHerstellerAuswaehlen.setToolTipText(LPMain.getTextRespectUISPr("button.hersteller.tooltip"));
		wbuQuellHerstellerAuswaehlen.addActionListener(this);
		wbuQuellHerstellerAuswaehlen.setActionCommand(ACTION_QUELLHERSTELLER_AUSWAEHLEN);
		wbuQuellHerstellerAuswaehlen.setFocusable(true);

		wtfZielHersteller.setMandatoryField(true);
		wtfQuellHersteller.setMandatoryField(true);

		wtfZielHersteller.setActivatable(false);
		wtfQuellHersteller.setActivatable(false);

		iZeile++;

		jpaWorkingOn.add(wbuQuellHerstellerAuswaehlen, new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(new JLabel("->"), new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuZielHerstellerAuswaehlen, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wtfQuellHersteller, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfZielHersteller, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jScrollPaneError.setViewportView(listError);
		jScrollPaneError.setAutoscrolls(true);

		jScrollPaneWarnig.setViewportView(listWarning);
		jScrollPaneWarnig.setAutoscrolls(true);

		jpaWorkingOn.add(new JLabel("Fehler:"), new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(jScrollPaneError, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(new JLabel("Warnungen:"), new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(jScrollPaneWarnig, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		saveButton = createAndSaveButton("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("part.partnerzusammenfuehren.ausfuehren"), ACTION_SPECIAL_SAVE,
				KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK), null);

		String[] aWhichButtonIUse = { ACTION_SPECIAL_SAVE };

		enableButtonAction(aWhichButtonIUse);

		LockStateValue lockstateValue = new LockStateValue(null, null, PanelBasis.LOCK_NO_LOCKING);
		updateButtons(lockstateValue);

		setSaveButtonStatus();
	}

	private void pruefeObZusammenfuehrenMoeglich() throws Throwable {

		this.saveButton.setEnabled(false);

		listError.removeAll();
		listWarning.removeAll();

		ArrayList<String> alError = new ArrayList<String>();
		ArrayList<String> alWarning = new ArrayList<String>();

		if (herstellerZielDto != null && herstellerQuellDto != null) {

			// WARNUNGEN

			// QUELLARTIKEL

			// FELHER

			if (herstellerQuellDto.getCNr().equals(herstellerZielDto.getCNr())) {
				alError.add(LPMain.getTextRespectUISPr("hersteller.zusammenfuehren.error.gleicherhersteller"));
			}

			// Wenn Snr und SNR doppelt, Fehler

			Object[] tempWarning = new Object[alWarning.size()];
			for (int i = 0; i < alWarning.size(); i++) {
				tempWarning[i] = "<html><span style=\"color: #FFA500\">" + alWarning.get(i) + "</span></html>";
			}

			listWarning.setListData(tempWarning);

			Object[] tempError = new Object[alError.size()];
			for (int i = 0; i < alError.size(); i++) {

				tempError[i] = "<html><span style=\"color: #FF0000\">" + alError.get(i) + "</span></html>";

			}

			listError.setListData(tempError);

			if (listError.getModel().getSize() == 0) {
				this.saveButton.setEnabled(true);
			}

		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(PanelBasis.ESC)
				|| e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			bHandleEventInSuperklasse = true;

			if (bHandleEventInSuperklasse) {
				super.eventActionSpecial(e); // close Dialog
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			if (wtfQuellHersteller.getText() == null || wtfZielHersteller.getText() == null) {

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}

			if (listError.getModel().getSize() > 0) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("hersteller.import.zusammenfuehren.vorhanden"));
				return;
			}

			boolean bZusammenfuehren = false;

			if (listWarning.getModel().getSize() > 0) {
				bZusammenfuehren = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("hersteller.zusammenfuehren.warnung.vorhanden"));

				if (bZusammenfuehren == false) {

					return;

				}
			}

			if (bZusammenfuehren == false) {

				bZusammenfuehren = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("hersteller.zusammenfuehren.warnung"));
			}
			if (bZusammenfuehren == true) {
				DelegateFactory.getInstance().getArtikelServiceDelegate()
						.zusammenfuehrenHersteller(herstellerQuellDto.getIId(), herstellerZielDto.getIId());
				getInternalFrame().closePanelDialog();
				if (internalFrame.getTabbedPaneGrunddaten().getPanelSplitHersteller() != null) {

					internalFrame.getTabbedPaneGrunddaten().getPanelSplitHersteller().eventYouAreSelected(false);
				}
			}

		}

		if (e.getActionCommand().equals(ACTION_ZIELHERSTELLER_AUSWAEHLEN)) {

			Integer selectedId = null;
			if (herstellerZielDto != null) {
				selectedId = herstellerZielDto.getIId();
			}

			if (herstellerZielDto == null && herstellerQuellDto != null) {
				selectedId = herstellerQuellDto.getIId();
			}

			panelArtikel = ArtikelFilterFactory.getInstance().createPanelFLRHersteller(getInternalFrame(), selectedId,
					false);

			DialogQuery dialogQueryPartner = new DialogQuery(panelArtikel);

			if (panelArtikel.getSelectedId() != null) {

				Object oKey = panelArtikel.getSelectedId();
				herstellerZielDto = DelegateFactory.getInstance().getArtikelDelegate()
						.herstellerFindBdPrimaryKey((Integer) oKey);

				wtfZielHersteller.setText(herstellerZielDto.getPartnerDto().formatFixName1Name2());

				pruefeObZusammenfuehrenMoeglich();

			}
		}

		if (e.getActionCommand().equals(ACTION_QUELLHERSTELLER_AUSWAEHLEN)) {

			Integer selectedId = null;
			if (herstellerQuellDto != null) {
				selectedId = herstellerQuellDto.getIId();
			}

			panelArtikel = ArtikelFilterFactory.getInstance().createPanelFLRHersteller(getInternalFrame(), selectedId,
					false);

			DialogQuery dialogQueryPartner = new DialogQuery(panelArtikel);

			if (panelArtikel.getSelectedId() != null) {

				Object oKey = panelArtikel.getSelectedId();
				herstellerQuellDto = DelegateFactory.getInstance().getArtikelDelegate()
						.herstellerFindBdPrimaryKey((Integer) oKey);

				wtfQuellHersteller.setText(herstellerQuellDto.getPartnerDto().formatFixName1Name2());

				pruefeObZusammenfuehrenMoeglich();
			}

		}

	}

	public boolean handleMyOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		switch (exfc.getICode()) {

		default:
			bErrorErkannt = false;
			break;
		}

		if (!bErrorErkannt) {

			handleException(exfc, false);

			// new DialogError(LPMain.getInstance().getDesktop(), exfc,
			// DialogError.TYPE_INFORMATION);
		}

		return bErrorErkannt;
	}

}
