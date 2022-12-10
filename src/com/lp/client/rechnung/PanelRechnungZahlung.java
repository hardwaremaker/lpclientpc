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
package com.lp.client.rechnung;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import com.lp.client.eingangsrechnung.EingangsrechnungFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.PanelZahlung;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.RechnungDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.util.Helper;

/*
 * <p>Panel zum Bearbeiten der Kopfdaten einer Zahlung</p> <p>Copyright Logistik
 * Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>22. 11.
 * 2004</I></p> <p> </p>
 *
 * @author Martin Bluehweis
 *
 * @version $Revision: 1.28 $
 */
public class PanelRechnungZahlung extends PanelZahlung {
	private static final long serialVersionUID = 1L;

	public WrapperEditorField wefText = null;

//	private TabbedPaneRechnungAll tabbedPaneRechnungAll = null;
	protected RechnungDto gutschriftDto;
	protected RechnungzahlungDto gutschriftZahlungDto;
	protected EingangsrechnungDto eingangsrechnungDto;

	private RechnungzahlungDto zahlungDto = null;

	public PanelRechnungZahlung(InternalFrame internalFrame, String add2TitleI,
			TabbedPaneRechnungAll tabbedPaneRechnungAll) throws Throwable {
		super(internalFrame, tabbedPaneRechnungAll, add2TitleI);
//		this.tabbedPaneRechnungAll = tabbedPaneRechnungAll;
		jbInit();
		initComponents();
	}

	private TabbedPaneRechnungAll getTabbedPaneRechnungAll() {
		return (TabbedPaneRechnungAll) tabbedPane;
//		return tabbedPaneRechnungAll ;
	}

	/**
	 * jbInit.
	 *
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		wefText = new WrapperEditorFieldKommentar(getInternalFrame(), LPMain.getTextRespectUISPr("label.text"));
	}

	protected RechnungzahlungDto getRechnungzahlungDto() {
		return zahlungDto;
	}

	protected void dialogQueryGutschriftRE(ActionEvent e, Integer KundeIId) throws Throwable {
		// ffcreatespanel: fuer eine dialogquery
		panelQueryFLRGutschrift = FinanzFilterFactory.getInstance().createPanelFLRGutschriftRE(getInternalFrame(),
				KundeIId);
		new DialogQuery(panelQueryFLRGutschrift);
	}

	protected void dialogQueryEingangsrechnung(ActionEvent e, Integer eingangsrechnungIId, Integer lieferantIId)
			throws Throwable {
		// ffcreatespanel: fuer eine dialogquery
		panelQueryFLREingangsrechnung = EingangsrechnungFilterFactory.getInstance().createPanelFLREingangsrechnungGoto(
				getInternalFrame(), eingangsrechnungIId, lieferantIId, false, false, true);
		new DialogQuery(panelQueryFLREingangsrechnung);
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen.
	 *
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (getKeyWhenDetailPanel() != null) {
			zahlungDto = getRechnungDelegate().zahlungFindByPrimaryKey((Integer) getKeyWhenDetailPanel());
//			zahlungDto = getRechnungDelegate().zahlungMitFibuKommentarByPrimaryKey((Integer) getKeyWhenDetailPanel(), LPMain.getTheClient());
		}

		if (zahlungDto != null) {
			wdfDatum.setDate(zahlungDto.getDZahldatum());
			String zahlungsartCNr = zahlungDto.getZahlungsartCNr();
			if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BANK)) {
				bankverbindungDto = DelegateFactory.getInstance().getFinanzDelegate()
						.bankverbindungFindByPrimaryKey(zahlungDto.getBankkontoIId());
				dto2ComponentsBankverbindung();
				wtnfAuszug.setInteger(zahlungDto.getIAuszug());
			} else if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BAR)) {
				kassenbuchDto = DelegateFactory.getInstance().getFinanzDelegate()
						.kassenbuchFindByPrimaryKey(zahlungDto.getKassenbuchIId());
				dto2ComponentsKassenbuch();
			} else if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
				if (zahlungDto.getRechnungzahlungIIdGutschrift() != null) {
					gutschriftZahlungDto = DelegateFactory.getInstance().getRechnungDelegate()
							.zahlungFindByPrimaryKey(zahlungDto.getRechnungzahlungIIdGutschrift());
				}
				gutschriftDto = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungFindByPrimaryKey(zahlungDto.getRechnungIIdGutschrift());
				wtfGutschrift.setText(gutschriftDto.getCNr());
				wcbErledigtGutschrift.setSelected(gutschriftDto.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT));
			} else if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {

				eingangsrechnungDto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(zahlungDto.getEingangsrechnungIId());
				wtfEingangsrechnung.setText(eingangsrechnungDto.getCNr());
				wbuEingangsrechnung.setOKey(eingangsrechnungDto.getIId());
			} else if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)
					&& zahlungDto.getBuchungdetailIId() != null) {
				buchungdetailDto = DelegateFactory.getInstance().getBuchenDelegate()
						.buchungdetailFindByPrimaryKey(zahlungDto.getBuchungdetailIId());
				dto2ComponentsVorauszahlung();
			}
			// restliche felder
			wcoZahlungsart.setKeyOfSelectedItem(zahlungDto.getZahlungsartCNr());
			wnfBetrag.setBigDecimal(zahlungDto.getNBetragfw().add(zahlungDto.getNBetragUstfw()));
			wnfBetragUST.setBigDecimal(zahlungDto.getNBetragUstfw());

			wnfKurs.setBigDecimal(zahlungDto.getNKurs());

			wnfKurs.setForeground(Color.BLACK);

			String waehrung = getRechnungDto().getWaehrungCNr();

			WechselkursDto wDto = DelegateFactory.getInstance().getLocaleDelegate()
					.getKursZuDatum(LPMain.getTheClient().getSMandantenwaehrung(), waehrung, wdfDatum.getDate());

			BigDecimal aktuelleKurs = new BigDecimal(1);

			if (wDto != null) {
				aktuelleKurs = wDto.getNKurs().setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
						BigDecimal.ROUND_HALF_EVEN);
			}

			if (aktuelleKurs != null) {
				if (wnfKurs.getBigDecimal().compareTo(aktuelleKurs) != 0) {
					wnfKurs.setForeground(Color.RED);
				}
			}

			this.setStatusbarPersonalIIdAnlegen(zahlungDto.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(zahlungDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(zahlungDto.getPersonalIIdAendern());
			this.setStatusbarTAendern(zahlungDto.getTAendern());

			// sub-dtos
			dto2ComponentsRechnung();
		} else {
			leereAlleFelder(this);
		}
	}

	/**
	 * Eine neue Zahlung erstellen.
	 *
	 * @param e                 ActionEvent
	 * @param bChangeKeyLockMeI boolean
	 * @param bNeedNoNewI       boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {

		resetEditorButton();

		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
		reDto = DelegateFactory.getInstance().getRechnungDelegate().rechnungFindByPrimaryKey(reDto.getIId());
		getTabbedPaneRechnungAll().setRechnungDto(reDto);
		if (reDto.getStatusCNr().equalsIgnoreCase(RechnungFac.STATUS_STORNIERT)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("rechnung.zahlung.rechnungiststorniert"));
			return;
		}
		if (reDto.getStatusCNr().equalsIgnoreCase(RechnungFac.STATUS_ANGELEGT)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("rechnung.zahlung.rechnungistnochnichtaktiviert"));
			return;
		}
		if (reDto.getStatusCNr().equalsIgnoreCase(RechnungFac.STATUS_BEZAHLT)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("rechnung.zahlung.rechnungistbereitsbezahlt"));
			return;
		}
		this.gutschriftDto = null;
		this.gutschriftZahlungDto = null;
		this.zahlungDto = null;
		wefText.getLpEditor().setText(null);
		super.eventActionNew(e, false, false);
	}

	/**
	 * Loeschen einer Zahlung
	 *
	 * @param e                     ActionEvent
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.zahlungDto != null) {
			if (zahlungDto.getIId() != null) {
				if (!isLockedDlg()) {

					if (tabbedPane instanceof TabbedPaneGutschrift
							&& wcoZahlungsart.getKeyOfSelectedItem().equals(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("rech.zahlung.gutschrift.error"));
						return;

					}

					if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
						gutschriftZahlungDto = DelegateFactory.getInstance().getRechnungDelegate()
								.zahlungFindByPrimaryKey(zahlungDto.getRechnungzahlungIIdGutschrift());
						zahlungDto.setRechnungzahlungIIdGutschrift(null);
						DelegateFactory.getInstance().getRechnungDelegate().updateZahlung(zahlungDto, false);
						DelegateFactory.getInstance().getRechnungDelegate().removeZahlung(gutschriftZahlungDto);
					}

					DelegateFactory.getInstance().getRechnungDelegate().removeZahlung(zahlungDto);
					this.zahlungDto = null;
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (tabbedPane instanceof TabbedPaneGutschrift
				&& wcoZahlungsart.getKeyOfSelectedItem().equals(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {

			DialogFactory.showModalDialogToken("lp.error", "rech.zahlung.gutschrift.error");
			return;

		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		setWdfDatumEnabled();
	}

	private void setWdfDatumEnabled() {
		boolean enabled = zahlungDto == null ? true
				: !RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG.equals(zahlungDto.getZahlungsartCNr());
		wdfDatum.setEnabled(enabled);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			boolean bErledigt = wcbErledigt.isSelected();
			// damit der offene neu gerechnet wird
			myLogger.info("Zahlung Erledigt == " + wcbErledigt.isSelected() + ".");
			focusLostWnfBetrag();

			components2Dto();

			if (tabbedPane instanceof TabbedPaneGutschrift
					&& wcoZahlungsart.getKeyOfSelectedItem().equals(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {

				DialogFactory.showModalDialogToken("lp.error", "rech.zahlung.gutschrift.error");
				return;
			}

			
			pruefeStellenKontoauszugsnummmer();
			
			boolean bSpeichern = true;
			if (wnfOffen.getBigDecimal() != null && wnfOffen.getBigDecimal().signum() < 0) {
				bSpeichern = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("rech.warnung.zahlbetraguebersteigtoffenenbetrag"),
						LPMain.getTextRespectUISPr("lp.frage"));
			}
			if (bErledigt && wnfOffen.getBigDecimal() != null && wnfOffen.getBigDecimal().signum() > 0) {
				if (zahlungDto.isGutschrift()) {
					DialogFactory.showModalDialogToken("lp.hint", "rech.warnung.keinezahlungmitgutschriftundskonto");
					return;
				} else {
					bSpeichern = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("rech.warnung.zahlbetragwenigeralsoffenerbetrag"),
							LPMain.getTextRespectUISPr("lp.frage"));
				}
			}
			if (bSpeichern) {
				if (zahlungDto.isGutschrift()) {
					if (gutschriftZahlungDto == null) {
						// neue Zahlung auf gutschrift
						gutschriftZahlungDto = new RechnungzahlungDto();
						gutschriftZahlungDto.setTAnlegen(new Timestamp(Calendar.getInstance().getTime().getTime()));
						gutschriftZahlungDto.setPersonalIIdAnlegen(zahlungDto.getPersonalIIdAnlegen());
					}
					gutschriftZahlungDto.setRechnungIId(gutschriftDto.getIId());
					gutschriftZahlungDto.setRechnungIIdGutschrift(getRechnungDto().getIId());
					gutschriftZahlungDto.setDZahldatum(zahlungDto.getDZahldatum());
					gutschriftZahlungDto.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_GUTSCHRIFT);
					gutschriftZahlungDto.setNBetrag(zahlungDto.getNBetrag());
					gutschriftZahlungDto.setNBetragfw(zahlungDto.getNBetragfw());
					gutschriftZahlungDto.setNBetragUst(zahlungDto.getNBetragUst());
					gutschriftZahlungDto.setNBetragUstfw(zahlungDto.getNBetragUstfw());
					gutschriftZahlungDto.setNKurs(zahlungDto.getNKurs());
					gutschriftZahlungDto.setPersonalIIdAendern(zahlungDto.getPersonalIIdAendern());
					gutschriftZahlungDto.setPersonalIIdAnlegen(zahlungDto.getPersonalIIdAnlegen());
					gutschriftZahlungDto.setTAendern(new Timestamp(Calendar.getInstance().getTime().getTime()));
					gutschriftZahlungDto.setPersonalIIdAendern(zahlungDto.getPersonalIIdAendern());

					gutschriftZahlungDto.setCKommentar(zahlungDto.getCKommentar());

//					BigDecimal bdBereitsBezahlt = DelegateFactory
//							.getInstance()
//							.getRechnungDelegate()
//							.getBereitsBezahltWertVonRechnungFw(
//									gutschriftDto.getIId(), null);
//
//					BigDecimal bdWertGutschrift = new BigDecimal(0);
//					if (gutschriftDto.getNWertfw() != null) {
//						bdWertGutschrift = gutschriftDto.getNWertfw();
//					}
//
//					boolean gutschriftErledigt = wcbErledigtGutschrift.isVisible() && wcbErledigtGutschrift.isSelected();

//					if (gutschriftZahlungDto.getIId() == null) {
//						gutschriftZahlungDto = getRechnungDelegate()
//								.createZahlung(gutschriftZahlungDto, gutschriftErledigt);
//					} else {
//						getRechnungDelegate().updateZahlung(
//								gutschriftZahlungDto, gutschriftErledigt);
//					}

////					if (gutschriftZahlungDto.getNBetrag().doubleValue() >= bdWertGutschrift
////							.subtract(bdBereitsBezahlt).doubleValue()
////							|| wcbErledigtGutschrift.isVisible() && wcbErledigtGutschrift.isSelected()) {
////
////						getRechnungDelegate().updateRechnungStatus(
////								gutschriftDto.getIId(),
////								RechnungFac.STATUS_BEZAHLT,
////								zahlungDto.getDZahldatum());
////						gutschriftDto.setStatusCNr(RechnungFac.STATUS_BEZAHLT);
////
////					} else {
////
////						getRechnungDelegate().updateRechnungStatus(
////								gutschriftDto.getIId(),
////								RechnungFac.STATUS_TEILBEZAHLT, null);
////						gutschriftDto
////								.setStatusCNr(RechnungFac.STATUS_TEILBEZAHLT);
////					}
//
//					zahlungDto.setRechnungzahlungIIdGutschrift(gutschriftZahlungDto.getIId());
//					zahlungDto.setRechnungIIdGutschrift(gutschriftDto.getIId());
				}

				boolean gutschriftErledigt = wcbErledigtGutschrift.isVisible() && wcbErledigtGutschrift.isSelected();

				// PJ20841
				boolean bNeueZahlung = false;
				if (zahlungDto.getIId() == null) {
					bNeueZahlung = true;

				}

				zahlungDto = getRechnungDelegate().createUpdateZahlung(zahlungDto, bErledigt,
						zahlungDto.isGutschrift() ? gutschriftZahlungDto : null, gutschriftErledigt);

				// PJ208410
				if (bNeueZahlung == true) {
					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(getRechnungDto().getKundeIId(),
							LocaleFac.BELEGART_REZAHLUNG, true, getInternalFrame());

					KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(getRechnungDto().getKundeIId());
					if (kdDto.getTLiefersperream() != null) {
						if (DialogFactory.showModalJaNeinDialog(getInternalFrame(), LPMain.getMessageTextRespectUISPr(
								"rech.zahlung.kundeliefersperre.aufheben", kdDto.getPartnerDto().formatFixName1Name2(),
								Helper.formatDatum(kdDto.getTLiefersperream(), LPMain.getTheClient().getLocUi())))) {

							kdDto.setTLiefersperream(null);
							DelegateFactory.getInstance().getKundeDelegate().updateKunde(kdDto);
						}
					}
				}

//				myLogger.info("Zahlung mit IId " + zahlungDto.getIId() + " speichern. bErledigt = " + bErledigt + ".");
//				if (zahlungDto.getIId() == null) {
//					zahlungDto = getRechnungDelegate().createZahlung(
//							zahlungDto, bErledigt);
//				} else {
//					getRechnungDelegate().updateZahlung(zahlungDto, bErledigt);
//				}

				setKeyWhenDetailPanel(getRechnungDto().getIId());

				super.eventActionSave(e, true);
				// Eine eventuelle statusaenderung auch anzeigen
				getTabbedPaneRechnungAll().reloadRechnungDto();
				eventYouAreSelected(false);
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_RECHNUNG;
	}

	private RechnungDelegate getRechnungDelegate() throws Throwable {
		return DelegateFactory.getInstance().getRechnungDelegate();
	}

	private void components2Dto() throws Throwable {
		RechnungDto rechnungDto = getRechnungDto();
		if (zahlungDto == null) {
			zahlungDto = new RechnungzahlungDto();
			zahlungDto.setRechnungIId(rechnungDto.getIId());
		}
		String zahlungsartCNr = (String) wcoZahlungsart.getKeyOfSelectedItem();
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BANK)) {
			zahlungDto.setBankkontoIId(bankverbindungDto.getIId());
			zahlungDto.setIAuszug(wtnfAuszug.getInteger());

		} else {
			zahlungDto.setBankkontoIId(null);
			zahlungDto.setIAuszug(null);
		}
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_BAR)) {
			zahlungDto.setKassenbuchIId(kassenbuchDto.getIId());
		} else {
			zahlungDto.setKassenbuchIId(null);
		}
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
			if (gutschriftZahlungDto != null) {
				zahlungDto.setRechnungzahlungIIdGutschrift(gutschriftZahlungDto.getIId());
			}

			if (gutschriftDto != null) {
				zahlungDto.setRechnungIIdGutschrift(gutschriftDto.getIId());
			}
		} else {
			zahlungDto.setRechnungIIdGutschrift(null);
			/**
			 * @todo preise PJ 1609
			 */
		}
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
			zahlungDto.setBuchungdetailIId(buchungdetailDto.getIId());
		} else {
			zahlungDto.setBuchungdetailIId(null);
		}
		if (zahlungsartCNr.equalsIgnoreCase(RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
			zahlungDto.setEingangsrechnungIId(eingangsrechnungDto.getIId());
		} else {
			zahlungDto.setEingangsrechnungIId(null);

		}

		zahlungDto.setDZahldatum(wdfDatum.getDate());
		// Wert pruefen
		wnfBetrag.checkMinimumMaximum();
		// netto-wert speichern
		zahlungDto.setNBetragfw(wnfBetrag.getBigDecimal().subtract(wnfBetragUST.getBigDecimal()));
		zahlungDto.setNBetragUstfw(wnfBetragUST.getBigDecimal());
		String waehrung = rechnungDto.getWaehrungCNr();
		// kurs
		String sMandantWaehrung = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		BigDecimal bdBetrag = null;
		BigDecimal bdBetragust = null;
		if (sMandantWaehrung.equals(waehrung)) {
			zahlungDto.setNKurs(new BigDecimal(1));
			bdBetrag = wnfBetrag.getBigDecimal().subtract(wnfBetragUST.getBigDecimal());
			bdBetragust = wnfBetragUST.getBigDecimal();
		} else {
			WechselkursDto kursDto = DelegateFactory.getInstance().getLocaleDelegate().getKursZuDatum(sMandantWaehrung,
					waehrung, wdfDatum.getDate());
			zahlungDto.setNKurs(kursDto.getNKurs());

			bdBetrag = DelegateFactory.getInstance().getLocaleDelegate().rechneUmInAndereWaehrungGerundetZuDatum(
					wnfBetrag.getBigDecimal().subtract(wnfBetragUST.getBigDecimal()), waehrung, sMandantWaehrung,
					wdfDatum.getDate());
			bdBetragust = DelegateFactory.getInstance().getLocaleDelegate().rechneUmInAndereWaehrungGerundetZuDatum(
					wnfBetragUST.getBigDecimal(), waehrung, sMandantWaehrung, wdfDatum.getDate());
		}
		zahlungDto.setNBetrag(bdBetrag);
		zahlungDto.setNBetragUst(bdBetragust);
		zahlungDto.setZahlungsartCNr((String) wcoZahlungsart.getKeyOfSelectedItem());

		if (wefText.getText() != null && wefText.getText().length() > 0) {
			zahlungDto.setCKommentar(wefText.getText());
		} else {
			zahlungDto.setCKommentar(null);
		}

	}

	private void dto2ComponentsRechnung() throws Throwable {
		RechnungDto rechnungDto = getRechnungDto();
		// das erledigt-hakerl
		if (this.getLockedstateDetailMainKey().getIState() != PanelBasis.LOCK_FOR_NEW
				&& this.getLockedstateDetailMainKey().getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			wcbErledigt.setSelected(rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT));
		}
	}

	protected void dto2ComponentsVorauszahlung0() throws ExceptionLP, Throwable {
//		enableToolsPanelButtons(false, ACTION_UPDATE);
		wnfBetrag.setBigDecimal(null);
		berechneOffen();

		wtfVorauszahlung.setText(null);

		if (buchungdetailDto != null) {
			if (buchungdetailDto.getNUst().signum() != 0) {
				/* Eine Vorauszahlung die UST hat wollen/koennen wir nicht behandeln */
				DialogFactory.showMeldung(LPMain.getTextRespectUISPr("finanz.vorauszahlung_ohne_ust"),
						LPMain.getTextRespectUISPr("lp.hint.vorauszahlung_ohne_ust"),
						javax.swing.JOptionPane.DEFAULT_OPTION);
			} else {
				BuchungDto buchungDto = DelegateFactory.getInstance().getBuchenDelegate()
						.buchungFindByPrimaryKey(buchungdetailDto.getBuchungIId());
				wtfVorauszahlung.setText(buchungDto.getCText());
				wdfDatum.setDate(buchungDto.getDBuchungsdatum());
				String sMandantWaehrung = LPMain.getTheClient().getSMandantenwaehrung();
//				String sMandantWaehrung = DelegateFactory
//						.getInstance()
//						.getMandantDelegate()
//						.mandantFindByPrimaryKey(
//								LPMain.getTheClient().getMandant())
//						.getWaehrungCNr();
				BigDecimal fwBetrag = DelegateFactory.getInstance().getLocaleDelegate()
						.rechneUmInAndereWaehrungGerundetZuDatum(buchungdetailDto.getNBetrag(), sMandantWaehrung,
								getRechnungDto().getWaehrungCNr(), wdfDatum.getDate());

				if (fwBetrag.compareTo(wnfGesOffen.getBigDecimal()) > 0) {
					wnfBetrag.setBigDecimal(getWertOffen());
				} else {
					wnfBetrag.setBigDecimal(fwBetrag);
				}
				wdfDatum.setEnabled(false);
			}
		}
		berechneOffen();
	}

	/**
	 * getRechnungDto
	 *
	 * @return RechnungDto
	 */
	private RechnungDto getRechnungDto() {
		return getTabbedPaneRechnungAll().getRechnungDto();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_GUTSCHRIFT)) {
			dialogQueryGutschriftRE(e, getRechnungDto().getKundeIId());
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EINGANGSRECHNUNG)) {
			if (eingangsrechnungDto == null) {
				dialogQueryEingangsrechnung(e, null, null);
			} else {
				dialogQueryEingangsrechnung(e, eingangsrechnungDto.getIId(), null);
			}
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRGutschrift) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				gutschriftDto = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungFindByPrimaryKey((Integer) key);
				wtfGutschrift.setText(gutschriftDto.getCNr());
				BigDecimal bdBezahlt;

				boolean skonto = false;
				ZahlungszielDto zahlungszielDto = new ZahlungszielDto();
				/*
				 * Bei Gutschrift Skonto-Zahlung verwirrt die Anwender. Davon abgesehen, dass
				 * die Skonto-Betrag Beruecksichtigung in der aktuellen Version noch nicht
				 * korrekt ist.
				 * 
				 * Vorerst in Abstimmung mit WH deaktivert (ghp, 5.3.2019)
				 * 
				 * ZahlungszielDto zahlungszielDto = DelegateFactory.getInstance()
				 * .getMandantDelegate()
				 * .zahlungszielFindByPrimaryKey(gutschriftDto.getZahlungszielIId()); boolean
				 * skonto = zahlungszielDto != null && !new
				 * Integer(0).equals(zahlungszielDto.getSkontoAnzahlTage1());
				 * 
				 * if(skonto && zahlungszielDto.getIId().equals(this.getZahlungszielIId())) {
				 * skonto = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
				 * LPMain.getMessageTextRespectUISPr("rech.zahlung.frage.gutschriftmitskonto",
				 * zahlungszielDto.getCBez())); } else { skonto = false; }
				 */

// TODO: Ok, der Anwender will eine Gutschrift mit Skonto zahlen,
// aber das heisst doch noch lange nicht, dass damit die Gutschrift erledigt 
// ist? (ghp, 5.3.2019) Deswegen die nachfolgende Zeile auskommentiert.
//				wcbErledigtGutschrift.setSelected(skonto);

				if (gutschriftZahlungDto != null) {
					bdBezahlt = DelegateFactory.getInstance().getRechnungDelegate()
							.getBereitsBezahltWertVonRechnungFw(gutschriftDto.getIId(), gutschriftZahlungDto.getIId());
					bdBezahlt = bdBezahlt.add(
							DelegateFactory.getInstance().getRechnungDelegate().getBereitsBezahltWertVonRechnungUstFw(
									gutschriftDto.getIId(), gutschriftZahlungDto.getIId()));
				} else {
					bdBezahlt = DelegateFactory.getInstance().getRechnungDelegate()
							.getBereitsBezahltWertVonRechnungFw(gutschriftDto.getIId(), null);
					bdBezahlt = bdBezahlt.add(DelegateFactory.getInstance().getRechnungDelegate()
							.getBereitsBezahltWertVonRechnungUstFw(gutschriftDto.getIId(), null));

				}
				if (gutschriftDto.getNWertfw() != null && gutschriftDto.getNWertustfw() != null) {
					BigDecimal bdBruttoGutschrift = gutschriftDto.getNWertfw().add(gutschriftDto.getNWertustfw());
					if (skonto)
						bdBruttoGutschrift = berechneSkontiertenWert(bdBruttoGutschrift,
								zahlungszielDto.getSkontoProzentsatz1());
					BigDecimal offenGS = bdBruttoGutschrift.subtract(bdBezahlt);

					BigDecimal bdOffenER = getWertGesamtOffenExklusiveZahlung();

					if (offenGS.doubleValue() > bdOffenER.doubleValue()) {
						wnfBetrag.setBigDecimal(bdOffenER);
					} else {
						wnfBetrag.setBigDecimal(offenGS);
					}
				}

				focusLostWnfBetrag();
			}

			if (e.getSource() == panelQueryFLREingangsrechnung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				eingangsrechnungDto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey((Integer) key);
				wtfEingangsrechnung.setText(eingangsrechnungDto.getCNr());

				BigDecimal offenRE = getWert().subtract(getWertBereitsBezahlt());

				BigDecimal bdBezahltER;

				if (gutschriftZahlungDto != null) {
					bdBezahltER = DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.getBezahltBetragFw(eingangsrechnungDto.getIId(), gutschriftZahlungDto.getIId());
				} else {
					bdBezahltER = DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.getBezahltBetragFw(eingangsrechnungDto.getIId(), null);

				}
				if (eingangsrechnungDto.getNBetrag() != null) {
					// Nun von ER-Waehrung nach RE-Waehrung umrechnen

					BigDecimal bdBrutto = DelegateFactory.getInstance().getLocaleDelegate()
							.rechneUmInAndereWaehrungGerundetZuDatum(eingangsrechnungDto.getNBetragfw(),
									eingangsrechnungDto.getWaehrungCNr(), getRechnungDto().getWaehrungCNr(),
									wdfDatum.getDate());

					bdBezahltER = DelegateFactory.getInstance().getLocaleDelegate()
							.rechneUmInAndereWaehrungGerundetZuDatum(bdBezahltER, eingangsrechnungDto.getWaehrungCNr(),
									getRechnungDto().getWaehrungCNr(), wdfDatum.getDate());

					BigDecimal bdOffenER = bdBrutto.subtract(bdBezahltER);

					if (offenRE.doubleValue() > bdOffenER.doubleValue()) {
						wnfBetrag.setBigDecimal(bdOffenER);
					} else {
						wnfBetrag.setBigDecimal(offenRE);
					}

				}

				focusLostWnfBetrag();

			}

		}
	}

	private void setDefaults() throws Throwable {
		boolean bSchlussrechnung = false;
		if (getRechnungDto().getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
			bSchlussrechnung = true;
		}

		wlaAnzahlungen.setVisible(bSchlussrechnung);
		wnfAnzahlungen.setVisible(bSchlussrechnung);
		wlaAnzahlungenUST.setVisible(bSchlussrechnung);
		wnfAnzahlungenUST.setVisible(bSchlussrechnung);
		wlaWaehrungAnz.setVisible(bSchlussrechnung);
		wlaWaehrungAnzUst.setVisible(bSchlussrechnung);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		setDefaults();
		wbuGutschrift.setText(LPMain.getTextRespectUISPr("rechnung.gutschrift"));

		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.
			dto2ComponentsRechnung();
			zahlungDto = null;
		} else {

			// einen alten Eintrag laden.
			zahlungDto = DelegateFactory.getInstance().getRechnungDelegate().zahlungFindByPrimaryKey((Integer) key);

//			zahlungDto = DelegateFactory.getInstance().getRechnungDelegate().zahlungMitFibuKommentarByPrimaryKey((Integer) key, LPMain.getTheClient());

			if (tabbedPane instanceof TabbedPaneGutschrift
					&& zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_GUTSCHRIFT)) {
				wbuGutschrift.setText(LPMain.getTextRespectUISPr("button.rechnung"));
				disableDetailModifyButtons();
			}

			wefText.getLpEditor().setText(zahlungDto.getCKommentar());

			setEditorButtonColor();

			dto2Components();
			dto2ComponentsRechnung();
			berechneOffen();
		}
		setWdfDatumEnabled();
	}

	protected void eventActionText(ActionEvent e) throws Throwable {
		getInternalFrame().showPanelEditor(wefText, this.getAdd2Title(), wefText.getLpEditor().getText(),
				getLockedstateDetailMainKey().getIState());
	}

	protected BigDecimal getWertBereitsBezahlt() throws Throwable {
		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (reDto == null)
			return new BigDecimal(0);
		BigDecimal betragUst = getWertBereitsBezahltUst();
		return getRechnungDelegate()
				.getBereitsBezahltWertVonRechnungFw(reDto.getIId(), zahlungDto != null ? zahlungDto.getIId() : null)
				.add(betragUst);
	}

	protected BigDecimal getWertBereitsBezahltUst() throws Throwable {
		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (reDto == null)
			return new BigDecimal(0);
		return getRechnungDelegate().getBereitsBezahltWertVonRechnungUstFw(reDto.getIId(),
				zahlungDto != null ? zahlungDto.getIId() : null);
	}

	protected BigDecimal getWert() throws Throwable {
		BigDecimal bdWert = new BigDecimal(0);
		if (getTabbedPaneRechnungAll().getRechnungDto().getNWertfw() != null) {
			bdWert = bdWert.add(getTabbedPaneRechnungAll().getRechnungDto().getNWertfw());
		}
		if (getWertUst() != null) {
			bdWert = bdWert.add(getWertUst());
		}
		return bdWert;
	}

	@Override
	protected BigDecimal getWertGesamtOffenExklusiveZahlung() throws Throwable {
		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (reDto.getNWertfw() == null)
			return new BigDecimal(0);
		return reDto.getNWertfw()
				.subtract(getRechnungDelegate().getBereitsBezahltWertVonRechnungFw(reDto.getIId(), null))
				.add(zahlungDto != null ? zahlungDto.getNBetragfw() : new BigDecimal(0))
				.add(getWertGesamtOffenUstExklusiveZahlung());
	}

	@Override
	protected BigDecimal getWertGesamtOffenUstExklusiveZahlung() throws Throwable {
		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (reDto.getNWertustfw() == null)
			return new BigDecimal(0);
		return reDto.getNWertustfw()
				.subtract(getRechnungDelegate().getBereitsBezahltWertVonRechnungUstFw(reDto.getIId(), null))
				.add(zahlungDto != null ? zahlungDto.getNBetragUstfw() : new BigDecimal(0));
	}

	protected BigDecimal getWertUst() throws Throwable {
		return getTabbedPaneRechnungAll().getRechnungDto().getNWertustfw();
	}

	protected String getWaehrung() throws Throwable {
		return getTabbedPaneRechnungAll().getRechnungDto().getWaehrungCNr();
	}

	protected Integer getZahlungszielIId() throws Throwable {
		return getTabbedPaneRechnungAll().getRechnungDto().getZahlungszielIId();
	}

	protected java.sql.Date getDBelegdatum() {
		return new java.sql.Date(getTabbedPaneRechnungAll().getRechnungDto().getTBelegdatum().getTime());
	}

	protected BigDecimal getWertAnzahlungen() throws Throwable {
		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (reDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG))
			return getRechnungDelegate().getAnzahlungenZuSchlussrechnungFw(reDto.getIId()).add(getWertAnzahlungenUst());
		else
			return new BigDecimal(0);
	}

	protected BigDecimal getWertAnzahlungenUst() throws Throwable {
		RechnungDto reDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (reDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG))
			return getRechnungDelegate().getAnzahlungenZuSchlussrechnungUstFw(reDto.getIId());
		else
			return new BigDecimal(0);
	}

	@Override
	protected Integer getKontoIIdAnzahlung() throws Throwable {
		KundeDto kunde = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(getRechnungDto().getKundeIId());
		KontoDto konto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(kunde.getIidDebitorenkonto());
		return konto.getIId();
	}

	@Override
	protected String getBuchungDetailAtCNr() {
		return BuchenFac.HabenBuchung;
	}

	@Override
	protected boolean isGutschriftErlaubt() {
		return !getRechnungDto().getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG);
	}

	@Override
	protected boolean isVorauszahlungErlaubt() {
		return true;
	}

	@Override
	protected boolean darfManuellErledigen() throws Throwable {
		if (!RechnungFac.RECHNUNGART_ANZAHLUNG.equals(getRechnungDto().getRechnungartCNr()))
			return true;
		RechnungDto[] rechArray = DelegateFactory.getInstance().getRechnungDelegate()
				.findByAuftragIId(getRechnungDto().getAuftragIId());
		for (RechnungDto rech : rechArray) {
			if (RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG.equals(rech.getRechnungartCNr()) && !Helper
					.isOneOf(rech.getStatusCNr(), RechnungFac.STATUS_ANGELEGT, RechnungFac.STATUS_STORNIERT)) {
				return false;
			}
		}
		return true;
	}

	private void setEditorButtonColor() {
		getHmOfButtons().get(ACTION_TEXT).getButton()
				.setIcon(zahlungDto.getCKommentar() != null && zahlungDto.getCKommentar().length() > 0
						? IconFactory.getCommentExist()
						: IconFactory.getEditorEdit());
	}

	private void resetEditorButton() {
		getHmOfButtons().get(ACTION_TEXT).getButton().setIcon(IconFactory.getEditorEdit());
	}

	protected boolean isGutschriftZahlung() {
		return tabbedPane instanceof TabbedPaneGutschrift
				&& RechnungFac.ZAHLUNGSART_GUTSCHRIFT.equals(zahlungDto.getZahlungsartCNr());
	}

	protected void disableDetailModifyButtons() {
		getHmOfButtons().get(ACTION_UPDATE).setEnabled(false);
		getHmOfButtons().get(ACTION_DELETE).setEnabled(false);
	}
}
