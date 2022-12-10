
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelFilterKriteriumDirekt;
import com.lp.client.frame.component.PanelFilterKriteriumDirektTyped;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.ReportSerienbrief;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.personal.ReportPersonalliste;
import com.lp.client.rechnung.ReportRechnung;
import com.lp.client.stueckliste.PanelDialogStuecklisteKommentar;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.angebotstkl.service.AngebotstklreportFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ProjektzeitenDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.rechnung.service.AbrechnungsvorschlagDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.ZeitinfoTransferDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVWriter;

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
 * @version $Revision: 1.5 $
 */
public class TabbedPaneZeitnachweis extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryZeitinfo = null;
	private PanelSplit panelSplitZeitinfo = null;
	private PanelZeitnachweis panelZeitinfoBottom = null;

	private final static int IDX_PANEL_AUSWAHL = 0;

	private final static String EXTRA_GOTO_ZD = "goto_zd";
	WrapperGotoButton wbuZeitbuchung = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_ZEITERFASSUNG_ZEITDATEN);
	WrapperGotoButton wbuTelefonzeiten = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN);

	private final static String EXTRA_GOTO_PROJEKT = "goto_projekt";
	WrapperGotoButton wbuProjekt = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_PROJEKT_AUSWAHL);

	private static final String ACTION_SPECIAL_NEW_EMAIL = "action_special_" + PanelBasis.ALWAYSENABLED
			+ "new_email_entry";

	private static final String ACTION_SPECIAL_NEW_PRINT = "action_special_" + PanelBasis.ALWAYSENABLED
			+ "new_print_entry";

	private static final String ACTION_SPECIAL_KOMMENTAR_INTERN = "action_special_" + PanelBasis.ALWAYSENABLED
			+ "new_kommentar_intern";

	private static final String ACTION_SPECIAL_KOMMENTAR_EXTERN = "action_special_" + PanelBasis.ALWAYSENABLED
			+ "new_kommentar_extern";

	private WrapperMenuBar wrapperMenuBar = null;

	private PanelDialogZeitinfoKommentar pdZeitinfoKommentar = null;

	public TabbedPaneZeitnachweis(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("rech.zeitinfo"));
		jbInit();
		initComponents();

	}

	public PanelQuery getPanelQueryWiederholende() {
		return panelQueryZeitinfo;
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("rech.zeitinfo"), null, null,
				LPMain.getInstance().getTextRespectUISPr("rech.zeitinfo"), IDX_PANEL_AUSWAHL);

		refreshAuswahl();

		panelQueryZeitinfo.eventYouAreSelected(false);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private PanelSplit refreshAuswahl() throws Throwable {
		if (panelSplitZeitinfo == null) {

			panelZeitinfoBottom = new PanelZeitnachweis(getInternalFrame(),
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag"), null, this);

			String[] aWhichButtonIUse = new String[] {};

			// Filter
			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("zeitdaten.flrpersonal.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL,
					false);

			panelQueryZeitinfo = new PanelQuery(null, kriterien, QueryParameters.UC_ID_ZEITINFO, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("rech.zeitinfo"), true);

			Map m = DelegateFactory.getInstance().getProjektServiceDelegate().getAllBereich();
			Object defaultId = null;
			/*
			 * if(m.size()>0) { defaultId=m.keySet().iterator().next(); }
			 */

			panelQueryZeitinfo.setFilterComboBox(m,
					new FilterKriterium("zeitdaten.flrprojekt." + ProjektFac.FLR_PROJEKT_BEREICH_I_ID, true, "" + "",
							FilterKriterium.OPERATOR_EQUAL, false),
					false, LPMain.getTextRespectUISPr("lp.alle"), false, defaultId);

			panelQueryZeitinfo.createAndSaveAndShowButton("/com/lp/client/res/clock16x16.png",
					LPMain.getInstance().getTextRespectUISPr("rech.zeitinfo.zuzeidatenwechseln"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_ZD, null);

			panelQueryZeitinfo.createAndSaveAndShowButton("/com/lp/client/res/briefcase2_document16x16.png",
					LPMain.getInstance().getTextRespectUISPr("rech.zeitinfo.zuprojektwechseln"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_PROJEKT, null);

			panelQueryZeitinfo.createAndSaveAndShowButton("/com/lp/client/res/printer.png",
					LPMain.getTextRespectUISPr("rech.zeitinfo.print"), ACTION_SPECIAL_NEW_PRINT, null, null);

			panelQueryZeitinfo.createAndSaveAndShowButton("/com/lp/client/res/mail.png",
					LPMain.getTextRespectUISPr("rech.zeitinfo.email.versenden"), ACTION_SPECIAL_NEW_EMAIL, null, null);

			panelQueryZeitinfo.createAndSaveAndShowButton("/com/lp/client/res/notebook.png",
					LPMain.getTextRespectUISPr("rech.zeitinfo.externerkommentar"), ACTION_SPECIAL_KOMMENTAR_EXTERN,
					null, null);

			panelQueryZeitinfo.createAndSaveAndShowButton("/com/lp/client/res/notebook.png",
					LPMain.getTextRespectUISPr("rech.zeitinfo.internerkommentar"), ACTION_SPECIAL_KOMMENTAR_INTERN,
					null, null);

			panelQueryZeitinfo.setMultipleRowSelectionEnabled(true);

			java.util.Date dGestern = Helper.addiereTageZuDatum(new java.util.Date(), -1);

			FilterKriteriumDirekt fkdVon = new FilterKriteriumDirekt("zeitdaten.t_zeit", "",
					FilterKriterium.OPERATOR_GTE, LPMain.getTextRespectUISPr("lp.von"),
					FilterKriteriumDirekt.PROZENT_NONE, true, false, Facade.MAX_UNBESCHRAENKT,
					FilterKriteriumDirekt.TYP_DATE);
			fkdVon.setDefaultWert(dGestern);

			FilterKriteriumDirekt fkdBis = new FilterKriteriumDirekt("zeitdaten.t_zeit", "",
					FilterKriterium.OPERATOR_LT, LPMain.getTextRespectUISPr("lp.bis"),
					FilterKriteriumDirekt.PROZENT_NONE, true, // wrapWithSingleQuotes
					false, Facade.MAX_UNBESCHRAENKT, FilterKriteriumDirekt.TYP_DATE);
			fkdBis.setDefaultWert(dGestern);

			panelQueryZeitinfo.addDirektFilterDatumVonBis(fkdVon, dGestern, fkdBis, dGestern);

			panelQueryZeitinfo.addDirektFilter(new FilterKriteriumDirekt(
					"zeitdaten.flrprojekt.flrpartner." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("lp.firma"),
					FilterKriteriumDirekt.PROZENT_BOTH, true, true, Facade.MAX_UNBESCHRAENKT));

			panelSplitZeitinfo = new PanelSplit(getInternalFrame(), panelZeitinfoBottom, panelQueryZeitinfo, 300);

			setComponentAt(IDX_PANEL_AUSWAHL, panelSplitZeitinfo);

		}
		return panelSplitZeitinfo;
	}

	public InternalFrameRechnung getInternalFrameRechnung() {
		return (InternalFrameRechnung) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryZeitinfo.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {

			if (e.getSource() == panelQueryZeitinfo) {

				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					getInternalFrameRechnung().setKeyWasForLockMe(panelQueryZeitinfo.getSelectedId() + "");
				}

				panelZeitinfoBottom.setKeyWhenDetailPanel(key);
				panelZeitinfoBottom.eventYouAreSelected(false);
				panelQueryZeitinfo.updateButtons();

				panelQueryZeitinfo.enableToolsPanelButtons(
						panelQueryZeitinfo.getSelectedIds() != null && panelQueryZeitinfo.getSelectedIds().length > 0,
						PanelBasis.ACTION_DELETE);

				if (panelQueryZeitinfo.getHmOfButtons().containsKey(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_ZD)) {
					panelQueryZeitinfo.getHmOfButtons().get(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_ZD).getButton()
							.setEnabled(true);
				}

				if (panelQueryZeitinfo.getHmOfButtons()
						.containsKey(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_PROJEKT)) {
					panelQueryZeitinfo.getHmOfButtons().get(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_PROJEKT)
							.getButton().setEnabled(true);
				}

			}

			if (e.getSource() == panelQueryZeitinfo) {
				if (panelQueryZeitinfo.getSelectedId() != null) {
					getInternalFrameRechnung().setKeyWasForLockMe(panelQueryZeitinfo.getSelectedId() + "");

					if (panelQueryZeitinfo.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
					}
					if (panelQueryZeitinfo.getHmOfButtons().containsKey(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_ZD)) {
						panelQueryZeitinfo.getHmOfButtons().get(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_ZD)
								.getButton().setEnabled(true);
					}

					if (panelQueryZeitinfo.getHmOfButtons()
							.containsKey(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_PROJEKT)) {
						panelQueryZeitinfo.getHmOfButtons().get(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_PROJEKT)
								.getButton().setEnabled(true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == panelZeitinfoBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryZeitinfo.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelZeitinfoBottom) {
				panelSplitZeitinfo.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelZeitinfoBottom) {
				Object oKey = panelZeitinfoBottom.getKeyWhenDetailPanel();
				panelQueryZeitinfo.eventYouAreSelected(false);
				panelQueryZeitinfo.setSelectedId(oKey);
				panelSplitZeitinfo.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelZeitinfoBottom) {
				Object oKey = panelQueryZeitinfo.getSelectedId();

				// holt sich alten key wieder
				getInternalFrame().setKeyWasForLockMe((oKey == null) ? null : oKey + "");

				if (panelZeitinfoBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitinfo.getId2SelectAfterDelete();
					panelQueryZeitinfo.setSelectedId(oNaechster);
				}

				panelSplitZeitinfo.eventYouAreSelected(false); // refresh
																// auf
																// das
																// gesamte
																// 1:n
																// panel

				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels deaktivieren
				if (panelQueryZeitinfo.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (e.getSource() == panelQueryZeitinfo) {
				PanelQuery pq = (PanelQuery) e.getSource();

				java.sql.Date dateBis = null;
				java.sql.Date dateVon = null;
				Iterator<Integer> it = panelQueryZeitinfo.getHmDirektFilter().keySet().iterator();
				while (it.hasNext()) {
					PanelFilterKriteriumDirekt panelFkd = (PanelFilterKriteriumDirekt) panelQueryZeitinfo
							.getHmDirektFilter().get(it.next());
					if (panelFkd.iTyp == PanelFilterKriteriumDirekt.TYP_DATE_BIS
							&& panelFkd.wdfFkdirektValue1 != null) {
						dateBis = panelFkd.wdfFkdirektValue1.getDate();
					}
					
					if (panelFkd.iTyp == PanelFilterKriteriumDirekt.TYP_DATE_VON
							&& panelFkd.wdfFkdirektValue1 != null) {
						dateVon = panelFkd.wdfFkdirektValue1.getDate();
					}
				}

				if (ACTION_SPECIAL_NEW_EMAIL.equals(pq.getAspect())) {

					PersonalDto personalDto_Angemeldet = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

					if (personalDto_Angemeldet.getCEmail() == null) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								personalDto_Angemeldet.getPartnerDto().formatFixName1Name2() + " / "
										+ personalDto_Angemeldet.getPartnerDto().formatFixName1Name2() + " "
										+ LPMain.getTextRespectUISPr("pers.sonderzeiten.urlaubsantrag.keinabsender"));
						return;
					}

					ArrayList<Integer> projektzeitenIIds = panelQueryZeitinfo.getSelectedIdsAsInteger();

					ArrayList<ZeitinfoTransferDto> prints = DelegateFactory.getInstance().getRechnungDelegate()
							.printZeitnachweis(projektzeitenIIds,dateVon, dateBis);

					int iAnzahlMailsVersendet = 0;
					for (int i = 0; i < prints.size(); i++) {
						ZeitinfoTransferDto ztDto = prints.get(i);

						if (ztDto.getcEmailEmfaenger() != null) {

							PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
									.partnerFindByPrimaryKey(ztDto.getPartnerIId());

							com.lp.server.system.service.MailtextDto m = new com.lp.server.system.service.MailtextDto();

							m.setParamXslFile(RechnungReportFac.REPORT_ZEITNACHWEIS);
							m.setParamLocale(LPMain.getTheClient().getLocUi());
							m.setParamMandantCNr(LPMain.getTheClient().getMandant());
							m.setParamModul(RechnungReportFac.REPORT_MODUL);
							m.setMailPartnerIId(ztDto.getPartnerIId());

							m.setParamLocale(Helper.string2Locale(partnerDto.getLocaleCNrKommunikation()));

							m.setMailBezeichnung(
									LPMain.getTextRespectSpezifischesLocale("rech.zeitnachweis.mailbezeichnung",
											Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())));

							PersonalDto personalDtoBearbeiter = DelegateFactory.getInstance().getPersonalDelegate()
									.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
							m.setMailVertreter(personalDtoBearbeiter);
							m.setBisDatum(dateBis);

							String text = DelegateFactory.getInstance().getVersandDelegate()
									.getDefaultTextForBelegEmail(m);

							String betreff = DelegateFactory.getInstance().getVersandDelegate()
									.getDefaultBetreffForBelegEmail(m, null, null);

							byte[] pdfFile = JasperExportManager.exportReportToPdf(ztDto.getPrint().getPrint());

							String filename = "zeitnachweis.pdf";

							VersandauftragDto versDto = new VersandauftragDto();
							versDto.setCEmpfaenger(ztDto.getcEmailEmfaenger());
							versDto.setCAbsenderadresse(personalDto_Angemeldet.getCEmail());

							versDto.setCDateiname(filename);

							versDto.setCBetreff(betreff);
							versDto.setOInhalt(pdfFile);

							versDto.setCText(text);

							DelegateFactory.getInstance().getVersandDelegate().updateVersandauftrag(versDto, false);
							iAnzahlMailsVersendet++;
						}

					}

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), LPMain
							.getMessageTextRespectUISPr("rech.zeitnachweis.mailsversandt", iAnzahlMailsVersendet + ""));

				} else if (ACTION_SPECIAL_NEW_PRINT.equals(pq.getAspect())) {
					ArrayList<Integer> zeitdatenIIds = panelQueryZeitinfo.getSelectedIdsAsInteger();

					if (zeitdatenIIds != null && zeitdatenIIds.size() > 0) {

						getInternalFrame().showReportKriterien(
								new ReportZeitnachweis(getInternalFrame(), zeitdatenIIds,dateVon, dateBis, ""), null, null,
								false, false);
					}

				} else if (ACTION_SPECIAL_KOMMENTAR_EXTERN.equals(pq.getAspect())) {
					if (panelQueryZeitinfo.getSelectedId() != null) {
						ProjektzeitenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.projektzeitenFindByPrimaryKey((Integer) panelQueryZeitinfo.getSelectedId());

						pdZeitinfoKommentar = new PanelDialogZeitinfoKommentar(getInternalFrame(), zDto,
								textFromToken("lp.kommentar"), false);
						getInternalFrame().showPanelDialog(pdZeitinfoKommentar);
					}

				} else if (ACTION_SPECIAL_KOMMENTAR_INTERN.equals(pq.getAspect())) {
					if (panelQueryZeitinfo.getSelectedId() != null) {
						ProjektzeitenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.projektzeitenFindByPrimaryKey((Integer) panelQueryZeitinfo.getSelectedId());

						pdZeitinfoKommentar = new PanelDialogZeitinfoKommentar(getInternalFrame(), zDto,
								textFromToken("lp.kommentar"), true);
						getInternalFrame().showPanelDialog(pdZeitinfoKommentar);
					}
				}

			}

		} else if ((e.getID() == ItemChangedEvent.ACTION_NEW) || (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {
			// btnnew: einen neuen machen.

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (e.getSource() == panelQueryZeitinfo) {
				// goto AG
				if (panelQueryZeitinfo.getSelectedId() != null) {

					ProjektzeitenDto projektzeitenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.projektzeitenFindByPrimaryKey((Integer) panelQueryZeitinfo.getSelectedId());

					if (projektzeitenDto.getZeitdatenIId() != null) {
						ZeitdatenDto saDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.zeitdatenFindByPrimaryKey(projektzeitenDto.getZeitdatenIId());
						if (sAspectInfo.endsWith(EXTRA_GOTO_ZD)) {

							wbuZeitbuchung.setOKey(saDto.getIId());
							wbuZeitbuchung
									.actionPerformed(new ActionEvent(wbuZeitbuchung, 0, WrapperGotoButton.ACTION_GOTO));

						}

						if (sAspectInfo.endsWith(EXTRA_GOTO_PROJEKT)) {

							wbuProjekt.setOKey(saDto.getIBelegartid());
							wbuProjekt.actionPerformed(new ActionEvent(wbuProjekt, 0, WrapperGotoButton.ACTION_GOTO));

						}
					} else if (projektzeitenDto.getTelefonzeitenIId() != null) {
						TelefonzeitenDto tzDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.telefonzeitenFindByPrimaryKey(projektzeitenDto.getTelefonzeitenIId());
						if (sAspectInfo.endsWith(EXTRA_GOTO_ZD)) {

							wbuTelefonzeiten.setOKey(tzDto.getIId());
							wbuTelefonzeiten.actionPerformed(
									new ActionEvent(wbuTelefonzeiten, 0, WrapperGotoButton.ACTION_GOTO));

						}

						if (sAspectInfo.endsWith(EXTRA_GOTO_PROJEKT) && tzDto.getProjektIId() != null) {

							wbuProjekt.setOKey(tzDto.getProjektIId());
							wbuProjekt.actionPerformed(new ActionEvent(wbuProjekt, 0, WrapperGotoButton.ACTION_GOTO));

						}
					}

				}

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {
			if (e.getSource() == panelQueryZeitinfo) {

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1
				|| e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryZeitinfo) {
				int iPos = panelQueryZeitinfo.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryZeitinfo.getSelectedId();

					if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
						DelegateFactory.getInstance().getFertigungDelegate().offenAgsUmreihen(iIdPosition, false);
					} else {
						DelegateFactory.getInstance().getFertigungDelegate().offenAgsUmreihen(iIdPosition, true);
					}

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryZeitinfo.setSelectedId(iIdPosition);

					panelQueryZeitinfo.eventYouAreSelected(false);

				}

			}
		}

	}

	private boolean vergleicheSortReihung(ArrayList<SortierKriterium> al1, ArrayList<SortierKriterium> al2) {

		if (al1.size() == al2.size()) {

			for (int i = 0; i < al1.size(); i++) {

				if (!al1.get(i).kritName.equals(al2.get(i).kritName)) {
					return false;
				}

			}
			return true;

		} else {
			return false;
		}

	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("fert.offeneags"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			refreshAuswahl();
			this.panelSplitZeitinfo.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			this.panelQueryZeitinfo.updateButtons();

			if (panelQueryZeitinfo.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}

			panelQueryZeitinfo.updateButtons(panelZeitinfoBottom.getLockedstateDetailMainKey());
		}

	}

	private String sortReihung1 = "flrmaschine.c_identifikationsnr";
	private String sortReihung2 = "flroffeneags.t_agbeginn";
	private String sortReihung3 = "flroffeneags.i_maschinenversatz_ms";
	private String sortReihung4 = "flroffeneags.flrlos.c_nr";

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneZeitinfo");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

		}

		return wrapperMenuBar;

	}

}
