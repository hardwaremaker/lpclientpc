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

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.lp.client.anfrage.AnfrageFilterFactory;
import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.anfrage.TabbedPaneAnfrage;
import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.angebot.TabbedPaneAngebot;
import com.lp.client.angebotstkl.AngebotstklFilterFactory;
import com.lp.client.angebotstkl.InternalFrameAngebotstkl;
import com.lp.client.angebotstkl.TabbedPaneAngebotstkl;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.artikel.TabbedPaneArtikel;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.auftrag.TabbedPaneAuftrag;
import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.bestellung.TabbedPaneBestellung;
import com.lp.client.cockpit.InternalFrameCockpit;
import com.lp.client.cockpit.TabbedPaneCockpit;
import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.fertigung.TabbedPaneLos;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.finanz.TabbedPaneKonten;
import com.lp.client.forecast.InternalFrameForecast;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.inserat.InseratFilterFactory;
import com.lp.client.inserat.InternalFrameInserat;
import com.lp.client.inserat.TabbedPaneInserat;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.lieferschein.TabbedPaneLieferschein;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.InternalFramePartner;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPaneBank;
import com.lp.client.partner.TabbedPaneKunde;
import com.lp.client.partner.TabbedPaneLieferant;
import com.lp.client.partner.TabbedPanePartner;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.InternalFramePersonal;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.personal.TabbedPanePersonal;
import com.lp.client.projekt.InternalFrameProjekt;
import com.lp.client.projekt.TabbedPaneProjekt;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.client.rechnung.TabbedPaneGutschrift;
import com.lp.client.rechnung.TabbedPaneProformarechnung;
import com.lp.client.rechnung.TabbedPaneRechnung;
import com.lp.client.reklamation.InternalFrameReklamation;
import com.lp.client.reklamation.ReklamationFilterFactory;
import com.lp.client.reklamation.TabbedPaneReklamation;
import com.lp.client.remote.RMIClient;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.stueckliste.TabbedPaneStueckliste;
import com.lp.client.system.InternalFrameSystem;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.system.TabbedPaneMandant;
import com.lp.client.zeiterfassung.InternalFrameZeiterfassung;
import com.lp.client.zeiterfassung.TabbedPaneMaschinen;
import com.lp.client.zeiterfassung.TabbedPaneZeiterfassung;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.HelperFuerCockpit;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

/**
 * <p>
 * Gewrappter JButton<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.22 $
 */
public class WrapperGotoButton extends JPanel implements ActionListener, IControl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperButton wrapperButton = null;
	private Object oKey = null;
	private Object detailKey = null;
	private WrapperButton buttonGoto = null;
	private int iWhereToGo = -1;
	private Dialog dialogToClose = null;

	public static final String ACTION_GOTO = "ACTION_GOTO";

	public WrapperGotoButton(int iWhereToGo) {
		this.iWhereToGo = iWhereToGo;
		jbInit();
	}

	public void closeDialogOnGoto(Dialog dialogToClose) {
		this.dialogToClose = dialogToClose;
	}

	public WrapperGotoButton(String sText, int iWhereToGo) {
		this.iWhereToGo = iWhereToGo;
		jbInit();
		wrapperButton.setText(sText);
	}

	public WrapperButton getWrapperButton() {
		return wrapperButton;
	}

	public WrapperButton getWrapperButtonGoTo() {
		return buttonGoto;
	}

	public void setActionCommand(String command) {
		wrapperButton.setActionCommand(command);
	}

	public String getActionCommand() {
		return wrapperButton.getActionCommand();
	}

	public void addActionListener(ActionListener l) {
		wrapperButton.addActionListener(l);
	}

	public void setEnabled(boolean bEnabled) {
		wrapperButton.setEnabled(bEnabled);

		if (oKey == null) {
			buttonGoto.setEnabled(false);
		} else {
			buttonGoto.setEnabled(!bEnabled);
		}
	}

	public void setText(String sText) {
		wrapperButton.setText(sText);
	}

	public void setToolTipText(String sText) {
		wrapperButton.setToolTipText(sText);
	}

	public void setActivatable(boolean isActivatable) {
		wrapperButton.setActivatable(isActivatable);
	}

	public boolean isActivatable() {
		return wrapperButton.isActivatable();
	}

	public void setMandatoryField(boolean isMandatoryField) {
		wrapperButton.setMandatoryField(isMandatoryField);
	}

	public boolean isMandatoryField() {
		return wrapperButton.isMandatoryField();
	}

	public void setWhereToGo(int iWhereToGo) {
		this.iWhereToGo = iWhereToGo;
	}

	public int getiWhereToGo() {
		return iWhereToGo;
	}

	public void removeContent() {
		oKey = null;
	}

	private void jbInit() {
		wrapperButton = new WrapperButton();
		buttonGoto = new WrapperButton();
		buttonGoto.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/data_into.png")));
		buttonGoto.setActionCommand(ACTION_GOTO);
		buttonGoto.addActionListener(this);

		this.setLayout(new GridBagLayout());

		this.add(wrapperButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		/**
		 * @todo MB: buttonGoto nur sichtbar, wenn Modul zur Verfuegung steht.
		 */
		this.add(buttonGoto, new GridBagConstraints(getColumnForGotoButton(), 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 10, 0));
	}

	protected int getColumnForGotoButton() {
		return 1;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_GOTO)) {
			if (oKey != null) {
				// UND hier einsprungspunkt einbauen
				try {
					// gotobutton: 2 Fuer die Konstante ein neues Ziel
					// definieren
					if (iWhereToGo == GotoHelper.GOTO_PARTNER_AUSWAHL) {

						KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
								.kundeFindByiIdPartnercNrMandantOhneExc((Integer) oKey,
										LPMain.getTheClient().getMandant());

						LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
								.lieferantFindByiIdPartnercNrMandantOhneExc((Integer) oKey,
										LPMain.getTheClient().getMandant());

						Integer okeyVohrer = new Integer((Integer) oKey);
						if (kundeDto != null && lieferantDto != null) {

							// Custom button text
							Object[] options = { LPMain.getTextRespectUISPr("label.kunde"),
									LPMain.getTextRespectUISPr("label.lieferant"),
									LPMain.getTextRespectUISPr("part.partner") };
							int n = JOptionPane.showOptionDialog(this,
									LPMain.getTextRespectUISPr("lp.partner.goto.wechsel"),
									LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

							if (n == 0) {
								iWhereToGo = GotoHelper.GOTO_KUNDE_AUSWAHL;
								oKey = kundeDto.getIId();
								actionPerformed(new ActionEvent(this, 0, ACTION_GOTO));
								iWhereToGo = GotoHelper.GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							} else if (n == 1) {
								iWhereToGo = GotoHelper.GOTO_LIEFERANT_AUSWAHL;
								oKey = lieferantDto.getIId();
								actionPerformed(new ActionEvent(this, 0, ACTION_GOTO));
								iWhereToGo = GotoHelper.GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							}

						} else {
							if (kundeDto != null) {
								iWhereToGo = GotoHelper.GOTO_KUNDE_AUSWAHL;
								oKey = kundeDto.getIId();
								actionPerformed(new ActionEvent(this, 0, ACTION_GOTO));
								iWhereToGo = GotoHelper.GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							} else if (lieferantDto != null) {
								iWhereToGo = GotoHelper.GOTO_LIEFERANT_AUSWAHL;
								oKey = lieferantDto.getIId();
								actionPerformed(new ActionEvent(this, 0, ACTION_GOTO));
								iWhereToGo = GotoHelper.GOTO_PARTNER_AUSWAHL;
								oKey = okeyVohrer;
								return;
							}
						}

						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)) {
							InternalFramePartner ifPartner = (InternalFramePartner) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PARTNER);
							ifPartner.geheZu(InternalFramePartner.IDX_PANE_PARTNER, TabbedPanePartner.IDX_PANEL_QP,
									oKey, null, PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey));
						}

					} else if (iWhereToGo == GotoHelper.GOTO_PARTNER_KURZBRIEF) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)) {
							InternalFramePartner ifPartner = (InternalFramePartner) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PARTNER);
							ifPartner.geheZu(InternalFramePartner.IDX_PANE_PARTNER,
									TabbedPanePartner.IDX_PANE_KURZBRIEF_SP, oKey, detailKey,
									PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_KUNDE_AUSWAHL) {
						if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUNDE)) {
							InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_KUNDE);

							KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
									.kundeFindByPrimaryKeyOhneExc((Integer) oKey);
							boolean bVersteckt = true;
							if (kundeDto != null
									&& Helper.short2boolean(kundeDto.getPartnerDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE, TabbedPaneKunde.IDX_PANE_KUNDE, oKey,
									null, PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey),
									bVersteckt);
						}
					} else if (iWhereToGo == GotoHelper.GOTO_PERSONAL_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PERSONAL)) {
							InternalFramePersonal ifPersonal = (InternalFramePersonal) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PERSONAL);

							PersonalDto pDto = DelegateFactory.getInstance().getPersonalDelegate()
									.personalFindByPrimaryKey((Integer) oKey);
							boolean bVersteckt = true;
							if (pDto != null && Helper.short2boolean(pDto.getPartnerDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							ifPersonal.geheZu(ifPersonal.IDX_TABBED_PANE_PERSONAL,
									ifPersonal.getTabbedPanePersonal().IDX_PANEL_AUSWAHL, oKey, null,
									PersonalFilterFactory.getInstance().createFKPersonalKey((Integer) oKey),
									bVersteckt);
						}
					} else if (iWhereToGo == GotoHelper.GOTO_KUNDE_KURZBRIEF) {
						if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUNDE)) {
							InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_KUNDE);
							ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE, TabbedPaneKunde.IDX_PANE_KURZBRIEF, oKey,
									detailKey, PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_INSERAT_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_INSERAT)) {
							InternalFrameInserat ifInserat = (InternalFrameInserat) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_INSERAT);
							ifInserat.geheZu(InternalFrameInserat.IDX_TABBED_PANE_INSERAT,
									TabbedPaneInserat.IDX_PANEL_INSERATAUSWAHL, oKey, null,
									InseratFilterFactory.getInstance().createFKInseratKey((Integer) oKey));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_KUNDE_KONDITIONEN) {
						if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUNDE)) {
							InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_KUNDE);
							ifKunde.getTpKunde().getOnlyPanelKundeQP1().clearDefaultFilters();

							KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
									.kundeFindByPrimaryKeyOhneExc((Integer) oKey);
							boolean bVersteckt = true;
							if (kundeDto != null
									&& Helper.short2boolean(kundeDto.getPartnerDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE, TabbedPaneKunde.IDX_PANE_KONDITIONEN,
									oKey, null, PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey),
									bVersteckt);
							ifKunde.getTpKunde().getOnlyPanelKundeQP1().restoreDefaultFilters();
						}
					} else if (iWhereToGo == GotoHelper.GOTO_LIEFERANT_KONDITIONEN) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERANT)) {
							InternalFrameLieferant ifLieferant = (InternalFrameLieferant) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_LIEFERANT);
							ifLieferant.getTpLieferant().getPanelLieferantenQP1().clearDefaultFilters();

							LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
									.lieferantFindByPrimaryKeyOhneExc((Integer) oKey);
							boolean bVersteckt = true;
							if (lieferantDto != null
									&& Helper.short2boolean(lieferantDto.getPartnerDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							ifLieferant.geheZu(InternalFrameLieferant.IDX_PANE_LIEFERANT,
									TabbedPaneLieferant.IDX_PANE_KONDITIONEN, oKey, null,
									PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey), bVersteckt);
							ifLieferant.getTpLieferant().getPanelLieferantenQP1().restoreDefaultFilters();
						}
					} else if (iWhereToGo == GotoHelper.GOTO_LIEFERANT_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERANT)) {
							InternalFrameLieferant ifLieferant = (InternalFrameLieferant) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_LIEFERANT);

							LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
									.lieferantFindByPrimaryKeyOhneExc((Integer) oKey);
							boolean bVersteckt = true;
							if (lieferantDto != null
									&& Helper.short2boolean(lieferantDto.getPartnerDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							ifLieferant.geheZu(InternalFrameLieferant.IDX_PANE_LIEFERANT,
									TabbedPaneLieferant.IDX_PANE_LIEFERANT, oKey, null,
									PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey), bVersteckt);
						}
					} else if (iWhereToGo == GotoHelper.GOTO_MANDANT_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_SYSTEM)) {
							InternalFrameSystem ifSystem = (InternalFrameSystem) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_SYSTEM);

							MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate()
									.mandantFindByPrimaryKey((String) oKey);
							boolean bVersteckt = true;
							if (mandantDto != null
									&& Helper.short2boolean(mandantDto.getPartnerDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							FilterKriterium[] kriterien = new FilterKriterium[1];
							kriterien[0] = new FilterKriterium("c_nr", true, "'" + mandantDto.getCNr() + "'",
									FilterKriterium.OPERATOR_EQUAL, false);

							ifSystem.geheZu(InternalFrameSystem.IDX_PANE_MANDANT, 0, oKey, null, kriterien, bVersteckt);
						}
					} else if (iWhereToGo == GotoHelper.GOTO_LIEFERANT_KURZBRIEF) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERANT)) {
							InternalFrameLieferant ifLieferant = (InternalFrameLieferant) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_LIEFERANT);
							ifLieferant.geheZu(InternalFrameLieferant.IDX_PANE_LIEFERANT,
									TabbedPaneLieferant.IDX_PANE_KURZBRIEF, oKey, detailKey,
									PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) oKey));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ARTIKEL_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ARTIKEL)) {

							Integer iKey = null;

							boolean bVersteckt = true;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;

								ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKeySmallOhneExc(iKey);

								if (aDto != null && Helper.short2boolean(aDto.getBVersteckt()) == false) {
									bVersteckt = false;
								}

							} else if (oKey instanceof String) {
								ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByCNrOhneExc((String) oKey);

								if (aDto != null) {
									if (Helper.short2boolean(aDto.getBVersteckt()) == false) {
										bVersteckt = false;
									}

									iKey = aDto.getIId();
								}
							}

							if (iKey != null) {
								InternalFrameArtikel ifArtikel = (InternalFrameArtikel) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_ARTIKEL);
								ifArtikel.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
										TabbedPaneArtikel.IDX_PANEL_AUSWAHL, iKey, null,
										ArtikelFilterFactory.getInstance().createFKArtikellisteKey(iKey), bVersteckt);
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_RECHNUNG_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_RECHNUNG)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								RechnungDto rDto = DelegateFactory.getInstance().getRechnungDelegate()
										.findGytschriftByRechnungTypCNr((String) oKey,
												RechnungFac.RECHNUNGTYP_RECHNUNG);

								if (rDto != null) {
									iKey = rDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameRechnung ifRechnung = (InternalFrameRechnung) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_RECHNUNG);
								ifRechnung.geheZu(InternalFrameRechnung.IDX_TABBED_PANE_RECHNUNG,
										TabbedPaneRechnung.IDX_RECHNUNGEN, iKey, null,
										RechnungFilterFactory.getInstance().createFKRechnungKey(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_REKLAMATION_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_REKLAMATION)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								ReklamationDto rDto = DelegateFactory.getInstance().getReklamationDelegate()
										.reklamationFindByCNrMandantCNrOhneExc((String) oKey);

								if (rDto != null) {
									iKey = rDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameReklamation ifRekla = (InternalFrameReklamation) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_REKLAMATION);
								ifRekla.geheZu(InternalFrameReklamation.IDX_TABBED_PANE_STUECKLISTE,
										TabbedPaneReklamation.IDX_PANEL_AUSWAHL, iKey, detailKey,
										ReklamationFilterFactory.getInstance().createFKReklamationKey(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_GUTSCHRIFT_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_GUTSCHRIFT)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								RechnungDto rDto = DelegateFactory.getInstance().getRechnungDelegate()
										.findGytschriftByRechnungTypCNr((String) oKey,
												RechnungFac.RECHNUNGTYP_GUTSCHRIFT);

								if (rDto != null) {
									iKey = rDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameRechnung ifRechnung = (InternalFrameRechnung) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_RECHNUNG);
								ifRechnung.geheZu(ifRechnung.IDX_TABBED_PANE_GUTSCHRIFT,
										TabbedPaneGutschrift.IDX_RECHNUNGEN, iKey, null,
										RechnungFilterFactory.getInstance().createFKRechnungKey((Integer) iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_PROFORMARECHNUNG_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROFORMARECHNUNG)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								RechnungDto rDto = DelegateFactory.getInstance().getRechnungDelegate()
										.findGytschriftByRechnungTypCNr((String) oKey,
												RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG);

								if (rDto != null) {
									iKey = rDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameRechnung ifRechnung = (InternalFrameRechnung) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_RECHNUNG);
								ifRechnung.geheZu(ifRechnung.IDX_TABBED_PANE_PROFORMARECHNUNG,
										TabbedPaneProformarechnung.IDX_RECHNUNGEN, iKey, null,
										RechnungFilterFactory.getInstance().createFKRechnungKey((Integer) iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_LIEFERSCHEIN_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERSCHEIN)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								try {
									LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
											.lieferscheinFindByCNr((String) oKey);

									if (lsDto != null) {
										iKey = lsDto.getIId();
									}
								} catch (Throwable ex) {
									//
								}
							}

							if (iKey != null) {

								InternalFrameLieferschein ifLieferschein = (InternalFrameLieferschein) LPMain
										.getInstance().getDesktop().holeModul(LocaleFac.BELEGART_LIEFERSCHEIN);
								ifLieferschein.geheZu(InternalFrameLieferschein.IDX_TABBED_PANE_LIEFERSCHEIN,
										TabbedPaneLieferschein.IDX_PANEL_LIEFERSCHEINAUSWAHL, iKey, null,
										LieferscheinFilterFactory.getInstance().createFKLieferscheinKey(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ANGEBOTSTKL_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AGSTUECKLISTE)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								try {
									AgstklDto agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
											.agstklFindByCNrMandantCNrOhneExc((String) oKey);

									if (agstklDto != null) {
										iKey = agstklDto.getIId();
									}
								} catch (Throwable ex) {
									//
								}
							}

							if (iKey != null) {

								InternalFrameAngebotstkl ifAgstkl = (InternalFrameAngebotstkl) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_AGSTUECKLISTE);
								ifAgstkl.geheZu(InternalFrameAngebotstkl.IDX_TABBED_PANE_ANGEBOTSTKL,
										TabbedPaneAngebotstkl.IDX_PANEL_AUSWAHL, iKey, null,
										AngebotstklFilterFactory.getInstance().createFKAgstklKey((Integer) iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_BUCHUNGDETAIL) {
						gotoBuchungdetail();
					} else if (iWhereToGo == GotoHelper.GOTO_STUECKLISTE_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {

							Integer iKey = null;
							boolean bVersteckt = true;
							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;

								StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
										.stuecklisteFindByPrimaryKeyOhneExc(iKey);

								if (stklDto != null
										&& Helper.short2boolean(stklDto.getArtikelDto().getBVersteckt()) == false) {
									bVersteckt = false;
								}

							} else if (oKey instanceof String) {
								try {
									ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
											.artikelFindByCNrOhneExc((String) oKey);

									if (aDto != null) {
										if (Helper.short2boolean(aDto.getBVersteckt()) == false) {
											bVersteckt = false;
										}
										StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
												.stuecklisteFindByMandantCNrArtikelIIdOhneExc(aDto.getIId());
										if (stklDto != null) {

											iKey = stklDto.getIId();
										}
									}
								} catch (Throwable ex) {
									//
								}
							}

							if (iKey != null) {

								InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_STUECKLISTE);
								ifStueckliste.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
										TabbedPaneStueckliste.IDX_PANEL_AUSWAHL, iKey, null,
										StuecklisteFilterFactory.getInstance().createFKStuecklisteKey((Integer) iKey),
										bVersteckt);
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_STUECKLISTE_ANDERER_MANDANT_AUSWAHL) {

						StuecklisteDto[] stklDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
								.stuecklisteFindByArtikelIId((Integer) oKey);

						if (stklDtos.length > 0) {
							if (stklDtos[0].getMandantCNr().equals(LPMain.getTheClient().getMandant())) {
								// gleicher Mandant
								if (LPMain.getInstance().getDesktop()
										.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {
									InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain
											.getInstance().getDesktop().holeModul(LocaleFac.BELEGART_STUECKLISTE);
									ifStueckliste.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
											TabbedPaneStueckliste.IDX_PANEL_AUSWAHL, stklDtos[0].getIId(), null,
											StuecklisteFilterFactory.getInstance()
													.createFKStuecklisteKey(stklDtos[0].getIId()));
								}
							} else {
								RMIClient.getInstance().gotoStuecklisteAndererMandant(stklDtos[0].getIId(),
										stklDtos[0].getMandantCNr());
							}
						}

					} else if (iWhereToGo == GotoHelper.GOTO_ARTIKELLIEFERANT_ANDERER_MANDANT) {

						ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikellieferantFindByPrimaryKey((Integer) oKey);

						LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
								.lieferantFindByPrimaryKey(alDto.getLieferantIId());

						if (lfDto.getMandantCNr().equals(LPMain.getTheClient().getMandant())) {
							//
						} else {
							RMIClient.getInstance().gotoArtikellieferantAndererMandant(alDto.getArtikelIId(),
									alDto.getIId(), lfDto.getMandantCNr());
						}

					} else if (iWhereToGo == GotoHelper.GOTO_STUECKLISTE_DETAIL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {
							InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_STUECKLISTE);

							boolean bVersteckt = true;
							StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKeyOhneExc((Integer) oKey);

							if (stklDto != null
									&& Helper.short2boolean(stklDto.getArtikelDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							ifStueckliste.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
									TabbedPaneStueckliste.IDX_PANEL_DETAIL, oKey, null,
									StuecklisteFilterFactory.getInstance().createFKStuecklisteKey((Integer) oKey),
									bVersteckt);
						}
					} else if (iWhereToGo == GotoHelper.GOTO_STUECKLISTE_POSITION) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {
							InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_STUECKLISTE);

							StuecklistepositionDto bDto = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklistepositionFindByPrimaryKey((Integer) oKey);

							FilterKriterium[] kriterien = new FilterKriterium[1];
							kriterien[0] = new FilterKriterium("stueckliste.i_id", true, bDto.getStuecklisteIId() + "",
									FilterKriterium.OPERATOR_EQUAL, false);

							boolean bVersteckt = true;
							StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKey(bDto.getStuecklisteIId());

							if (Helper.short2boolean(stklDto.getArtikelDto().getBVersteckt()) == false) {
								bVersteckt = false;
							}

							ifStueckliste.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
									TabbedPaneStueckliste.IDX_PANEL_POSITIONEN, bDto.getStuecklisteIId(), oKey,
									kriterien, bVersteckt);
						}
					}

					else if (iWhereToGo == GotoHelper.GOTO_FERTIGUNG_AUSWAHL) {
						if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								try {
									LosDto lDto = DelegateFactory.getInstance().getFertigungDelegate()
											.losFindByCNrMandantCNr((String) oKey, LPMain.getTheClient().getMandant());

									if (lDto != null) {
										iKey = lDto.getIId();
									}
								} catch (Throwable ex) {
									//
								}
							}

							if (iKey != null) {

								InternalFrameFertigung ifFertigung = (InternalFrameFertigung) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_LOS);
								ifFertigung.geheZu(InternalFrameFertigung.IDX_TABBED_PANE_LOS,
										TabbedPaneLos.IDX_AUSWAHL, iKey, null,
										FertigungFilterFactory.getInstance().createFKLosKey(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_EINGANGSRECHNUNG_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								EingangsrechnungDto erDto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
										.eingangsrechnungFindByCNrMandantCNr((String) oKey, false);

								if (erDto != null) {
									iKey = erDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameEingangsrechnung ifER = (InternalFrameEingangsrechnung) LPMain
										.getInstance().getDesktop().holeModul(LocaleFac.BELEGART_EINGANGSRECHNUNG);
								ifER.geheZu(InternalFrameEingangsrechnung.IDX_TABBED_PANE_EINGANGSRECHNUNG,
										ifER.getTabbedPaneEingangsrechnung().IDX_EINGANGSRECHNUNGEN, iKey, null,
										SystemFilterFactory.getInstance().createFKKeyAuswahlliste(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ZUSATZKOSTEN_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ER_ZUSATZKOSTEN)
								&& DelegateFactory.getInstance().getTheJudgeDelegate()
										.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								EingangsrechnungDto erDto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
										.eingangsrechnungFindByCNrMandantCNr((String) oKey, true);

								if (erDto != null) {
									iKey = erDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameEingangsrechnung ifER = (InternalFrameEingangsrechnung) LPMain
										.getInstance().getDesktop().holeModul(LocaleFac.BELEGART_EINGANGSRECHNUNG);
								ifER.geheZu(InternalFrameEingangsrechnung.IDX_TABBED_PANE_ZUSATZKOSTEN,
										ifER.getTabbedPaneEingangsrechnungZusatzkosten().IDX_EINGANGSRECHNUNGEN, iKey,
										null, SystemFilterFactory.getInstance().createFKKeyAuswahlliste(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_PROJEKT_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								ProjektDto pjDto = DelegateFactory.getInstance().getProjektDelegate()
										.projektFindByMandantCNrCNrOhneExc((String) oKey);

								if (pjDto != null) {
									iKey = pjDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameProjekt ifPJ = (InternalFrameProjekt) LPMain.getInstance().getDesktop()
										.holeModul(LocaleFac.BELEGART_PROJEKT);
								ifPJ.geheZu(InternalFrameProjekt.IDX_TABBED_PANE_PROJEKT,
										TabbedPaneProjekt.IDX_PANEL_PROJEKTAUSWAHL, iKey, null,
										SystemFilterFactory.getInstance().createFKKeyAuswahlliste((Integer) iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_PROJEKT_HISTORY) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)) {
							InternalFrameProjekt ifPJ = (InternalFrameProjekt) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PROJEKT);
							ifPJ.geheZu(InternalFrameProjekt.IDX_TABBED_PANE_PROJEKT,
									ifPJ.getTabbedPaneProjekt().IDX_PANEL_PROJEKTHISTORY, oKey, detailKey,
									SystemFilterFactory.getInstance().createFKKeyAuswahlliste((Integer) oKey));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ANGEBOT_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								AngebotDto aDto = DelegateFactory.getInstance().getAngebotDelegate()
										.angebotFindByCNr((String) oKey);

								if (aDto != null) {
									iKey = aDto.getIId();
								}
							}

							if (iKey != null) {

								InternalFrameAngebot ifAN = (InternalFrameAngebot) LPMain.getInstance().getDesktop()
										.holeModul(LocaleFac.BELEGART_ANGEBOT);
								ifAN.geheZu(InternalFrameAngebot.IDX_TABBED_PANE_ANGEBOT,
										TabbedPaneAngebot.IDX_PANEL_AUSWAHL, iKey, null,
										SystemFilterFactory.getInstance().createFKKeyAuswahlliste(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ANFRAGE_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANFRAGE)) {
							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								try {
									AnfrageDto auftragDto = DelegateFactory.getInstance().getAnfrageDelegate()
											.anfrageFindByCNrMandantCNrOhneExc((String) oKey);

									if (auftragDto != null) {
										iKey = auftragDto.getIId();
									}
								} catch (Throwable ex) {
									//
								}
							}
							if (iKey != null) {

								InternalFrameAnfrage ifAN = (InternalFrameAnfrage) LPMain.getInstance().getDesktop()
										.holeModul(LocaleFac.BELEGART_ANFRAGE);
								ifAN.geheZu(InternalFrameAnfrage.IDX_TABBED_PANE_ANFRAGE,
										TabbedPaneAnfrage.IDX_PANEL_AUSWAHL, iKey, null,
										SystemFilterFactory.getInstance().createFKKeyAuswahlliste(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_AUFTRAG_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								try {
									AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
											.auftragFindByMandantCNrCNrOhneExc(LPMain.getTheClient().getMandant(),
													(String) oKey);

									if (auftragDto != null) {
										iKey = auftragDto.getIId();
									}
								} catch (Throwable ex) {
									//
								}
							}
							if (iKey != null) {
								InternalFrameAuftrag ifAB = (InternalFrameAuftrag) LPMain.getInstance().getDesktop()
										.holeModul(LocaleFac.BELEGART_AUFTRAG);
								ifAB.geheZu(InternalFrameAuftrag.IDX_TABBED_PANE_AUFTRAG,
										TabbedPaneAuftrag.IDX_PANEL_AUFTRAGAUSWAHL, iKey, null,
										AuftragFilterFactory.getInstance().createFKAuftragKey(iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_BESTELLUNG_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_BESTELLUNG)) {

							Integer iKey = null;

							if (oKey instanceof Integer) {
								iKey = (Integer) oKey;
							} else if (oKey instanceof String) {
								try {
									BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
											.bestellungFindByCNrMandantCNr((String) oKey);

									if (bsDto != null) {
										iKey = bsDto.getIId();
									}
								} catch (Throwable ex) {
									//
								}
							}

							if (iKey != null) {

								InternalFrameBestellung ifAB = (InternalFrameBestellung) LPMain.getInstance()
										.getDesktop().holeModul(LocaleFac.BELEGART_BESTELLUNG);
								ifAB.geheZu(InternalFrameBestellung.IDX_PANE_BESTELLUNG,
										TabbedPaneBestellung.IDX_PANEL_AUSWAHL, iKey, null,
										BestellungFilterFactory.getInstance().createFKBestellungKey((Integer) iKey));
							}
						}
					} else if (iWhereToGo == GotoHelper.GOTO_BESTELLUNG_POSITION) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_BESTELLUNG)) {
							InternalFrameBestellung ifAB = (InternalFrameBestellung) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_BESTELLUNG);

							BestellpositionDto bDto = DelegateFactory.getInstance().getBestellungDelegate()
									.bestellpositionFindByPrimaryKey((Integer) oKey);

							ifAB.geheZu(InternalFrameBestellung.IDX_PANE_BESTELLUNG,
									TabbedPaneBestellung.IDX_PANEL_BESTELLPOSITION, bDto.getBestellungIId(), oKey,
									BestellungFilterFactory.getInstance()
											.createFKBestellungKey(bDto.getBestellungIId()));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ANSPRECHPARTNER_LIEFERANT) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERANT)) {
							InternalFrameLieferant ifAB = (InternalFrameLieferant) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_LIEFERANT);

							AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
									.ansprechpartnerFindByPrimaryKey((Integer) oKey);

							LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
									.lieferantFindByiIdPartnercNrMandantOhneExc(anspDto.getPartnerIId(),
											LPMain.getTheClient().getMandant());

							ifAB.geheZu(InternalFrameLieferant.IDX_PANE_LIEFERANT,
									TabbedPaneLieferant.IDX_PANE_ANSPRECHPARTNER, lieferantDto.getIId(), oKey,
									PartnerFilterFactory.getInstance().createFKPartnerKey(lieferantDto.getIId()));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ANSPRECHPARTNER_KUNDE) {
						if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUNDE)) {
							InternalFrameKunde ifAB = (InternalFrameKunde) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_KUNDE);

							AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
									.ansprechpartnerFindByPrimaryKey((Integer) oKey);

							KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
									.kundeFindByiIdPartnercNrMandantOhneExc(anspDto.getPartnerIId(),
											LPMain.getTheClient().getMandant());

							ifAB.geheZu(InternalFrameKunde.IDX_PANE_KUNDE, TabbedPaneKunde.IDX_PANE_ANSPRECHPARTNER,
									kundeDto.getIId(), oKey,
									PartnerFilterFactory.getInstance().createFKPartnerKey(kundeDto.getIId()));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ANSPRECHPARTNER_PARTNER) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)) {
							InternalFramePartner ifAB = (InternalFramePartner) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PARTNER);

							AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
									.ansprechpartnerFindByPrimaryKey((Integer) oKey);

							ifAB.geheZu(InternalFrameKunde.IDX_PANE_KUNDE, TabbedPanePartner.IDX_PANE_ANSPRECHPARTNER,
									anspDto.getPartnerIId(), oKey,
									PartnerFilterFactory.getInstance().createFKPartnerKey(anspDto.getPartnerIId()));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ANFRAGEPOSITIONLIEFERDATEN) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANFRAGE)) {
							InternalFrameAnfrage ifAB = (InternalFrameAnfrage) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_ANFRAGE);

							AnfragepositionlieferdatenDto aDto = DelegateFactory.getInstance()
									.getAnfragepositionDelegate()
									.anfragepositionlieferdatenFindByPrimaryKey((Integer) oKey);
							AnfragepositionDto apDto = DelegateFactory.getInstance().getAnfragepositionDelegate()
									.anfragepositionFindByPrimaryKey(aDto.getAnfragepositionIId());

							ifAB.geheZu(InternalFrameAnfrage.IDX_TABBED_PANE_ANFRAGE,
									TabbedPaneAnfrage.IDX_PANEL_POSITIONENLIEFERDATEN, apDto.getBelegIId(), oKey,
									AnfrageFilterFactory.getInstance().createFKAnfrageiid(apDto.getBelegIId()));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_ZEITERFASSUNG_ZEITDATEN) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {
							InternalFrameZeiterfassung ifAB = (InternalFrameZeiterfassung) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_ZEITERFASSUNG);

							ZeitdatenDto bDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.zeitdatenFindByPrimaryKey((Integer) oKey);

							ifAB.getTabbedPaneZeiterfassung()
									.setSelectedIndex(TabbedPaneZeiterfassung.IDX_PANEL_ZEITDATEN);

							// Datum + Zeile setzen

							ifAB.getTabbedPaneZeiterfassung().getPanelBottomZeitdaten().getWdfDatum()
									.setDate(bDto.getTZeit());

							ifAB.geheZu(ifAB.IDX_TABBED_PANE_ZEITERFASSUNG, TabbedPaneZeiterfassung.IDX_PANEL_ZEITDATEN,
									bDto.getPersonalIId(), oKey, ZeiterfassungFilterFactory.getInstance()
											.createFKPersonalKey(bDto.getPersonalIId()));

						}
					} else if (iWhereToGo == GotoHelper.GOTO_ZEITERFASSUNG_REISE) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {
							InternalFrameZeiterfassung ifAB = (InternalFrameZeiterfassung) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_ZEITERFASSUNG);

							ReiseDto rDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.reiseFindByPrimaryKey((Integer) oKey);

							ifAB.getTabbedPaneZeiterfassung()
									.setSelectedIndex(TabbedPaneZeiterfassung.IDX_PANEL_REISEZEITEN);

							ifAB.geheZu(ifAB.IDX_TABBED_PANE_ZEITERFASSUNG,
									TabbedPaneZeiterfassung.IDX_PANEL_REISEZEITEN, rDto.getPersonalIId(), oKey,
									ZeiterfassungFilterFactory.getInstance()
											.createFKPersonalKey(rDto.getPersonalIId()));

						}
					} else if (iWhereToGo == GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {
							InternalFrameZeiterfassung ifAB = (InternalFrameZeiterfassung) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_ZEITERFASSUNG);

							TelefonzeitenDto tDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.telefonzeitenFindByPrimaryKey((Integer) oKey);

							ifAB.getTabbedPaneZeiterfassung()
									.setSelectedIndex(TabbedPaneZeiterfassung.IDX_PANEL_TELEFONZEITEN);

							ifAB.geheZu(ifAB.IDX_TABBED_PANE_ZEITERFASSUNG,
									TabbedPaneZeiterfassung.IDX_PANEL_TELEFONZEITEN, tDto.getPersonalIId(), oKey,
									ZeiterfassungFilterFactory.getInstance()
											.createFKPersonalKey(tDto.getPersonalIId()));

						}
					} else if (iWhereToGo == GotoHelper.GOTO_LOS_GUT_SCHLECHT) {

						if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)) {
							InternalFrameFertigung ifFertigung = (InternalFrameFertigung) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_LOS);

							LossollarbeitsplanDto saDo = DelegateFactory.getInstance().getFertigungDelegate()
									.lossollarbeitsplanFindByPrimaryKey((Integer) oKey);

							ifFertigung.geheZu(InternalFrameFertigung.IDX_TABBED_PANE_LOS,
									TabbedPaneLos.IDX_ARBEITSPLAN, saDo.getLosIId(), oKey,
									FertigungFilterFactory.getInstance().createFKLosKey((Integer) saDo.getLosIId()));

							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										ifFertigung.getTabbedPaneLos().setSelectedIndex(TabbedPaneLos.IDX_GUTSCHLECHT);
									} catch (Throwable e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});

						}

					} else if (iWhereToGo == GotoHelper.GOTO_LOSSOLLMATERIAL) {

						if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)) {
							InternalFrameFertigung ifFertigung = (InternalFrameFertigung) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_LOS);

							LossollmaterialDto matDo = DelegateFactory.getInstance().getFertigungDelegate()
									.lossollmaterialFindByPrimaryKey((Integer) oKey);

							ifFertigung.geheZu(InternalFrameFertigung.IDX_TABBED_PANE_LOS, TabbedPaneLos.IDX_MATERIAL,
									matDo.getLosIId(), oKey,
									FertigungFilterFactory.getInstance().createFKLosKey((Integer) matDo.getLosIId()));

						}

					} else if (iWhereToGo == GotoHelper.GOTO_COCKPIT_TELEFONZEITEN) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_COCKPIT)) {
							InternalFrameCockpit ifAB = (InternalFrameCockpit) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_COCKPIT);

							TelefonzeitenDto tDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.telefonzeitenFindByPrimaryKey((Integer) oKey);

							if (tDto.getPartnerIId() != null) {

								// ifAB.getTpCockpit()
								// .setSelectedIndex(TabbedPaneCockpit.IDX_PANEL_TELEFONZEITEN);

								FilterKriterium[] kriterien = new FilterKriterium[1];
								kriterien[0] = new FilterKriterium("i_id", true, tDto.getPartnerIId() + "",
										FilterKriterium.OPERATOR_EQUAL, false);

								ifAB.geheZu(InternalFrameCockpit.IDX_PANE_COCKPIT,
										TabbedPaneCockpit.IDX_PANEL_TELEFONZEITEN, tDto.getPartnerIId(), oKey,
										kriterien);
							} else {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.goto.cockpittelefonzeiten.keinpartner"));
							}

						}
					} else if (iWhereToGo == GotoHelper.GOTO_MASCHINEN_ZEITDATEN) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {

							MaschinenzeitdatenDto mDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.maschinenzeitdatenFindByPrimaryKey((Integer) oKey);

							InternalFrameZeiterfassung ifAB = (InternalFrameZeiterfassung) LPMain.getInstance()
									.getDesktop().holeModul(LocaleFac.BELEGART_ZEITERFASSUNG);
							ifAB.geheZu(ifAB.IDX_TABBED_PANE_MASCHINEN, TabbedPaneMaschinen.IDX_PANEL_ZEITDATEN,
									mDto.getMaschineIId(), oKey, ZeiterfassungFilterFactory.getInstance()
											.createFKPersonalKey(mDto.getMaschineIId()));

						}
					} else if (iWhereToGo == GotoHelper.GOTO_BESTELLUNG_WARENEINGANG) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_BESTELLUNG)) {
							InternalFrameBestellung ifAB = (InternalFrameBestellung) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_BESTELLUNG);

							WareneingangDto wDto = DelegateFactory.getInstance().getWareneingangDelegate()
									.wareneingangFindByPrimaryKey((Integer) oKey);

							ifAB.geheZu(InternalFrameBestellung.IDX_PANE_BESTELLUNG,
									TabbedPaneBestellung.IDX_PANEL_WARENEINGANG, wDto.getBestellungIId(), oKey,
									BestellungFilterFactory.getInstance()
											.createFKBestellungKey((Integer) wDto.getBestellungIId()));

						}
					} else if (iWhereToGo == GotoHelper.GOTO_FORECAST_POSITION) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FORECAST)) {
							InternalFrameForecast ifAB = (InternalFrameForecast) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_FORECAST);

							ForecastpositionDto fpDto = DelegateFactory.getInstance().getForecastDelegate()
									.forecastpositionFindByPrimaryKey((Integer) oKey);

							ifAB.getTpForecast().geheZuForecastposition(fpDto.getIId());

						}
					} else if (iWhereToGo == GotoHelper.GOTO_COCKPIT) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_COCKPIT)) {
							InternalFrameCockpit ifCP = (InternalFrameCockpit) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_COCKPIT);

							HelperFuerCockpit helper = (HelperFuerCockpit) oKey;
							ifCP.geheZu(InternalFrameCockpit.IDX_PANE_COCKPIT,
									TabbedPaneCockpit.IDX_PANEL_3SP_ANSPRECHPARTNER, helper.getPartnerIId(),
									helper.getAnsprechpartnerIId(),
									PartnerFilterFactory.getInstance().createFKPartnerKey(helper.getPartnerIId()));

						}
					} else if (iWhereToGo == GotoHelper.GOTO_DEBITORENKONTO_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
							InternalFrameFinanz ifAB = (InternalFrameFinanz) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_FINANZBUCHHALTUNG);
							ifAB.geheZu(ifAB.IDX_TABBED_PANE_DEBITORENKONTEN, TabbedPaneKonten.iDX_KONTEN, oKey, null,
									FinanzFilterFactory.getInstance().createFKKontoKey((Integer) oKey));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_KREDITORENKONTO_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
							InternalFrameFinanz ifAB = (InternalFrameFinanz) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_FINANZBUCHHALTUNG);
							ifAB.geheZu(ifAB.IDX_TABBED_PANE_KREDITORENKONTEN, TabbedPaneKonten.iDX_KONTEN, oKey, null,
									FinanzFilterFactory.getInstance().createFKKontoKey((Integer) oKey));
						}
					} else if (iWhereToGo == GotoHelper.GOTO_PARTNER_BANK_AUSWAHL) {
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)) {
							InternalFramePartner ifPartner = (InternalFramePartner) LPMain.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_PARTNER);

							ifPartner.geheZu(InternalFramePartner.IDX_PANE_BANK, TabbedPaneBank.IDX_PANE_BANK_QP1, oKey,
									null, PartnerFilterFactory.getInstance().createFKPartner((Integer) oKey));
						}
					} else {
						geheZuImpl(iWhereToGo, oKey);
					}
				} catch (EJBExceptionLP t) {
					if (t.getCode() == EJBExceptionLP.FEHLER_KEINE_MODULBERECHTIGUNG) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.error.keingotomoeglich"));
					} else {
						throw t;
					}

				} catch (ExceptionLP t) {
					throw new EJBExceptionLP(t.getICode(), t);
				} catch (Throwable t) {
					LPMain.getInstance().exitClientNowErrorDlg(t);
				}
			}
		}

		if (dialogToClose != null) {
			dialogToClose.setVisible(false);
			dialogToClose.dispose();
		}

	}

	private int getKvpKontoTypPanel(InternalFrameFinanz ifFinanz, String kontotypCNr) {
		if (FinanzServiceFac.KONTOTYP_DEBITOR.equals(kontotypCNr)) {
			return ifFinanz.IDX_TABBED_PANE_DEBITORENKONTEN;
		} else if (FinanzServiceFac.KONTOTYP_KREDITOR.equals(kontotypCNr)) {
			return ifFinanz.IDX_TABBED_PANE_KREDITORENKONTEN;
		} else if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontotypCNr)) {
			return ifFinanz.IDX_TABBED_PANE_SACHKONTEN;
		}

		return -1;
	}

	protected void gotoBuchungdetail() throws Throwable, ExceptionLP {
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG))
			return;

		InternalFrameFinanz ifFinanz = (InternalFrameFinanz) LPMain.getInstance().getDesktop()
				.holeModul(LocaleFac.BELEGART_FINANZBUCHHALTUNG);

		BuchungdetailDto bdDto = DelegateFactory.getInstance().getBuchenDelegate()
				.buchungdetailFindByPrimaryKey((Integer) oKey);

		KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(bdDto.getKontoIId());

		Integer panelIndex = getKvpKontoTypPanel(ifFinanz, kontoDto.getKontotypCNr());
		if (null != panelIndex) {
			ifFinanz.geheZu(panelIndex, TabbedPaneKonten.iDX_BUCHUNGEN, kontoDto.getIId(), bdDto.getIId(),
					FinanzFilterFactory.getInstance().createFKKontoKey(kontoDto.getIId()));
		}

		// if
		// (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR))
		// {
		// ifFinanz.geheZu(
		// InternalFrameFinanz.IDX_TABBED_PANE_DEBITORENKONTEN,
		// TabbedPaneKontenDebitorenkonten.iDX_BUCHUNGEN,
		// kontoDto.getIId(),
		// bdDto.getIId(),
		// FinanzFilterFactory.getInstance()
		// .createFKKontoKey(
		// (Integer) kontoDto
		// .getIId()));
		// } else if
		// (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR))
		// {
		// ifFinanz.geheZu(
		// InternalFrameFinanz.IDX_TABBED_PANE_KREDITORENKONTEN,
		// TabbedPaneKontenKreditorenkonten.iDX_BUCHUNGEN,
		// kontoDto.getIId(),
		// bdDto.getIId(),
		// FinanzFilterFactory.getInstance()
		// .createFKKontoKey(
		// (Integer) kontoDto
		// .getIId()));
		// } else if
		// (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO))
		// {
		// ifFinanz.geheZu(
		// InternalFrameFinanz.IDX_TABBED_PANE_SACHKONTEN,
		// TabbedPaneKontenSachkonten.iDX_BUCHUNGEN,
		// kontoDto.getIId(),
		// bdDto.getIId(),
		// FinanzFilterFactory.getInstance()
		// .createFKKontoKey(
		// (Integer) kontoDto
		// .getIId()));
		// }
		//
	}

	public void setOKey(Object oKey) {
		this.oKey = oKey;
		// der Goto-Button darf immer dann enabled sein, wenn ein Key vorhanden
		// ist.
		buttonGoto.setEnabled(oKey != null);
	}

	public Object getOKey() {
		return oKey;
	}

	public void setDetailKey(Object detailKey) {
		this.detailKey = detailKey;
	}

	public Object getDetailKey() {
		return detailKey;
	}

	public boolean requestFocusInWindow() {
		return getWrapperButton().requestFocusInWindow();
	}

	public void setRechtCNr(String rechtCNr) {
		getWrapperButton().setRechtCNr(rechtCNr);

	}

	public void setMnemonic(int toSet) {
		wrapperButton.setMnemonic(toSet);
	}

	public void setMnemonic(char toSet) {
		wrapperButton.setMnemonic(toSet);
	}

	@Override
	public boolean hasContent() throws Throwable {
		return true;
	}

	protected void geheZuImpl(Integer where, Object okey) throws Throwable {

	}

	public void setIcon(Icon icon) {
		wrapperButton.setIcon(icon);
	}
}
