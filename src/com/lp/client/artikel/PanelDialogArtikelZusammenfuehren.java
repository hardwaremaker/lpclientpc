
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperButtonZusammenfuehren;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.stueckliste.service.StklpruefplanDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
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
public class PanelDialogArtikelZusammenfuehren extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED + "save";
	private final String ACTION_ZIELARTIKEL_AUSWAEHLEN = "ACTION_ZIELPARTNER_AUSWAEHLEN";
	private final String ACTION_QUELLARTIKEL_AUSWAEHLEN = "ACTION_QUELLPARTNER_AUSWAEHLEN";

	private JButton saveButton = null;

	private ArtikelDto artikelZielDto = null;
	private ArtikelDto artikelQuellDto = null;

	private PanelQueryFLR panelArtikel = null;
	protected boolean bHandleEventInSuperklasse = true;

	private JList listError = new JList();
	private JScrollPane jScrollPaneError = null;

	private JList listWarning = new JList();
	private JScrollPane jScrollPaneWarnig = null;

	private WrapperButton wbuZielArtikelAuswaehlen = null;
	private WrapperButton wbuQuellArtikelAuswaehlen = null;

	private WrapperTextField wtfQuellArtikel = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	private WrapperTextField wtfZielArtikel = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	private InternalFrameArtikel internalFrame = null;

	public PanelDialogArtikelZusammenfuehren(ArtikelDto artikelQuellDto, InternalFrameArtikel internalFrame,
			String add2TitleI) throws Throwable {

		super(internalFrame, add2TitleI + " | " + artikelQuellDto.formatArtikelbezeichnung());
		this.internalFrame = internalFrame;
		this.artikelQuellDto = artikelQuellDto;

		wtfQuellArtikel.setText(artikelQuellDto.formatArtikelbezeichnung());

		jbInit(); // zuerst QuellFelder leer, erst nach Quellauswahl Werte drin
		initComponents();
		setSaveButtonStatus();
		pruefeObZusammenfuehrenMoeglich();
	}

	private void setSaveButtonStatus() throws Throwable {
		if (this.artikelQuellDto != null && this.artikelZielDto != null) {
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
		wbuZielArtikelAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuZielArtikelAuswaehlen.setText(LPMain.getInstance().getTextRespectUISPr("artikel.zielartikel.auswaehlen"));
		wbuZielArtikelAuswaehlen.addActionListener(this);
		wbuZielArtikelAuswaehlen.setActionCommand(ACTION_ZIELARTIKEL_AUSWAEHLEN);
		wbuZielArtikelAuswaehlen.setFocusable(true);

		// Quell-Partner-Auswahlbutton
		wbuQuellArtikelAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuQuellArtikelAuswaehlen.setText(LPMain.getInstance().getTextRespectUISPr("artikel.quellartikel.auswaehlen"));
		wbuQuellArtikelAuswaehlen.addActionListener(this);
		wbuQuellArtikelAuswaehlen.setActionCommand(ACTION_QUELLARTIKEL_AUSWAEHLEN);
		wbuQuellArtikelAuswaehlen.setFocusable(true);

		wtfZielArtikel.setMandatoryField(true);
		wtfQuellArtikel.setMandatoryField(true);

		wtfZielArtikel.setActivatable(false);
		wtfQuellArtikel.setActivatable(false);

		iZeile++;

		jpaWorkingOn.add(wbuQuellArtikelAuswaehlen, new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(new JLabel("->"), new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuZielArtikelAuswaehlen, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wtfQuellArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfZielArtikel, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
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
				LPMain.getTextRespectUISPr("artikel.zusammenfuehren.ausfuehren"), ACTION_SPECIAL_SAVE,
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

		if (artikelZielDto != null && artikelQuellDto != null) {

			// WARNUNGEN

			// QUELLARTIKEL

			// Sind Kommentare vorhanden
			ArtikelkommentarDto[] akDtos = DelegateFactory.getInstance().getArtikelkommentarDelegate()
					.artikelkommentarFindByArtikelIId(artikelQuellDto.getIId());

			if (akDtos != null && akDtos.length > 0) {

				alWarning.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.warnung.kommentare.vorhanden"));
			}

			// Sind Eigenschaften vorhanden
			if (DelegateFactory.getInstance().getPanelDelegate().paneldatenFindByPanelCNrCKey(
					PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, artikelQuellDto.getIId() + "").length > 0) {

				alWarning.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.warnung.eigenschaften.vorhanden"));

			}

			// Sind VK-Preise vorhanden
			VkPreisfindungEinzelverkaufspreisDto vkpfDto = DelegateFactory.getInstance().getVkPreisfindungDelegate()
					.getArtikeleinzelverkaufspreis(artikelQuellDto.getIId(),
							new java.sql.Date(System.currentTimeMillis()),
							LPMain.getTheClient().getSMandantenwaehrung());
			if (vkpfDto != null) {

				alWarning.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.warnung.vkpreise.vorhanden"));

			}

			// Sind EK-PReise vorhanden
			if (DelegateFactory.getInstance().getArtikelDelegate()
					.artikellieferantFindByArtikelIId(artikelQuellDto.getIId()).length > 0) {

				alWarning.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.warnung.ekpreise.vorhanden"));

			}

			// FELHER

			if (artikelQuellDto.getCNr().equals(artikelZielDto.getCNr())) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.gleicherartikel"));
			}

			if (!artikelQuellDto.getBLagerbewirtschaftet().equals(artikelZielDto.getBLagerbewirtschaftet())) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.lagerbewirtschaftet"));
			}

			if (!artikelQuellDto.getBSeriennrtragend().equals(artikelZielDto.getBSeriennrtragend())) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.snrtragend"));
			}

			if (!artikelQuellDto.getBChargennrtragend().equals(artikelZielDto.getBChargennrtragend())) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.chnrtragend"));
			}

			if (!artikelQuellDto.getArtikelartCNr().equals(artikelZielDto.getArtikelartCNr())) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.gleicheartikelart"));
			}

			List<AuftragseriennrnDto> lDoppelte = DelegateFactory.getInstance().getArtikelServiceDelegate()
					.zusammenfuehrenArtikelPruefeAuftragsnnr(artikelQuellDto.getIId(), artikelZielDto.getIId());

			if (lDoppelte.size() > 0) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.gleicheauftragsnrs"));
				for (AuftragseriennrnDto asDto : lDoppelte) {
					alError.add("Auftrag "+asDto.getAuftragCNr_NOT_IN_DB() + ", Seriennummer: " + asDto.getCSeriennr());
				}

			}

			StuecklisteDto stklDto_Quellartikel = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelQuellDto.getIId());

			StuecklisteDto stklDto_Zielartikel = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelZielDto.getIId());

			if (stklDto_Zielartikel == null && stklDto_Quellartikel != null) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.beidestkl"));
			}

			if (stklDto_Quellartikel != null) {

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTENFREIGABE)
						&& stklDto_Quellartikel.getTFreigabe() != null) {
					alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.stkl.freigegeben"));
				}

				StuecklistearbeitsplanDto[] apDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklistearbeitsplanFindByStuecklisteIId(stklDto_Quellartikel.getIId());
				StuecklistepositionDto[] matDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklistepositionFindByStuecklisteIId(stklDto_Quellartikel.getIId());
				StklpruefplanDto[] ppDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stklpruefplanFindByStuecklisteIId(stklDto_Quellartikel.getIId());

				if (apDtos.length > 0) {
					alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.stkl.keineazpos"));
				}
				if (matDtos.length > 0) {
					alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.stkl.keinematpos"));
				}
				if (ppDtos.length > 0) {
					alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.stkl.keinepruefpos"));
				}

				// SP8754
				StuecklistepositionDto[] posDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklistepositionFindByArtikelIId(stklDto_Quellartikel.getArtikelIId());
				if (posDtos.length > 0) {
					alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.stkl.qulleinanderenstkls"));
				}

			}

			if (stklDto_Quellartikel != null && stklDto_Zielartikel != null && !stklDto_Quellartikel
					.getStuecklisteartCNr().equals(stklDto_Zielartikel.getStuecklisteartCNr())) {
				alError.add(LPMain.getTextRespectUISPr("artikel.zusammenfuehren.error.stkl.gleiche.art"));

			}

			// SP9470

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
			if (wtfQuellArtikel.getText() == null || wtfZielArtikel.getText() == null) {

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}

			if (listError.getModel().getSize() > 0) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("artikel.import.zusammenfuehren.vorhanden"));
				return;
			}

			boolean bZusammenfuehren = false;

			if (listWarning.getModel().getSize() > 0) {
				bZusammenfuehren = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("artikel.zusammenfuehren.warnung.vorhanden"));

				if (bZusammenfuehren == false) {

					return;

				}
			}

			if (bZusammenfuehren == false) {

				bZusammenfuehren = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("artikel.zusammenfuehren.warnung"));
			}
			if (bZusammenfuehren == true) {
				DelegateFactory.getInstance().getArtikelServiceDelegate()
						.zusammenfuehrenArtikel(artikelQuellDto.getIId(), artikelZielDto.getIId());
				getInternalFrame().closePanelDialog();
				internalFrame.getTabbedPaneArtikel().getPanelQueryArtikel().eventYouAreSelected(false);
				internalFrame.getTabbedPaneArtikel().getPanelQueryArtikel().setSelectedId(artikelZielDto.getIId());
				internalFrame.getTabbedPaneArtikel().getPanelQueryArtikel().eventYouAreSelected(false);
			}

		}

		if (e.getActionCommand().equals(ACTION_ZIELARTIKEL_AUSWAEHLEN)) {

			Integer selectedId = null;
			if (artikelZielDto != null) {
				selectedId = artikelZielDto.getIId();
			}

			if (artikelZielDto == null && artikelQuellDto != null) {
				selectedId = artikelQuellDto.getIId();
			}

			panelArtikel = ArtikelFilterFactory.getInstance().createPanelFLRArtikelMitArbeitszeit(getInternalFrame(),
					selectedId, false);

			DialogQuery dialogQueryPartner = new DialogQuery(panelArtikel);

			if (panelArtikel.getSelectedId() != null) {

				Object oKey = panelArtikel.getSelectedId();
				artikelZielDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) oKey);

				wtfZielArtikel.setText(artikelZielDto.formatArtikelbezeichnung());

				pruefeObZusammenfuehrenMoeglich();

			}
		}

		if (e.getActionCommand().equals(ACTION_QUELLARTIKEL_AUSWAEHLEN)) {

			Integer selectedId = null;
			if (artikelQuellDto != null) {
				selectedId = artikelQuellDto.getIId();
			}

			panelArtikel = ArtikelFilterFactory.getInstance().createPanelFLRArtikelMitArbeitszeit(getInternalFrame(),
					selectedId, false);

			DialogQuery dialogQueryPartner = new DialogQuery(panelArtikel);

			if (panelArtikel.getSelectedId() != null) {

				Object oKey = panelArtikel.getSelectedId();
				artikelQuellDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) oKey);

				wtfQuellArtikel.setText(artikelQuellDto.formatArtikelbezeichnung());

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
