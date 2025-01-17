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
package com.lp.client.anfrage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.bestellung.ReportBestellVorschlag;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlag2;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagAnhandStuecklistenmindestlagerstand;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagUeberleitung2;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagverdichtung;
import com.lp.client.frame.component.PanelPositionenBestellvorschlag;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFile;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.SheetImportController;
import com.lp.client.system.DialogZuletztErzeugt;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * TabbedPane fuer Modul Anfrage/Anfragevorschlag.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 21.10.05
 * </p>
 * 
 * @todo MB. das ist eigentlich das gleiche wie TPBestellvorschlag -> eine
 *       Klasse daraus machen
 * 
 *       <p>
 *       </p>
 * @author Uli Walch
 * @version $Revision: 1.14 $
 */
public class TabbedPaneAnfragevorschlag extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelSplit panelAnfragevorschlagSP1 = null; // FLR 1:n Liste
	private PanelQuery panelAnfragevorschlagQP1 = null;
	private PanelPositionenBestellvorschlag panelAnfragevorschlagD1 = null;

	protected final static int IDX_PANEL_ANFRAGEVORSCHLAG = 0;

	private PanelDialogKriterienBestellvorschlag2 pdKriterienAnfragevorschlag = null;
	private PanelDialogKriterienBestellvorschlagUeberleitung2 pdKriterienBestellvorschlagUeberleitung = null;
	private PanelDialogKriterienBestellvorschlagverdichtung pdKritBestellvorschlagverdichtung = null;
	private PanelDialogKriterienBestellvorschlagAnhandAngebot pdKritBestellvorschlagAnhandAngebot = null;

	private static final String ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_neuer_bestellvorschlag";
	private static final String ACTION_SPECIAL_ANFRAGEN_AUS_BESTELLVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_anfragen_aus_bestellvorschlag";
	private static final String ACTION_SPECIAL_ANFRAGEN_IN_LIEFERGRUPPENANFRAGEN = PanelBasis.ALWAYSENABLED
			+ "action_special_anfragen_in_liefergruppenafragen_ueberleiten";
	private static final String ACTION_SPECIAL_VERDICHTEN = PanelBasis.ALWAYSENABLED + "action_special_verdichten";
	private static final String ACTION_SPECIAL_IMPORT = PanelBasis.ALWAYSENABLED + "action_special_import";
	private static final String ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_ANGEBOT = PanelBasis.ALWAYSENABLED
			+ "action_special_neuer_bestellvorschlag_anhand_angebot";

	private static final String ACTION_SPECIAL_GEMEINSAME_ARTIKEL_LOESCHEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_gemeinsame_artikel_loeschen";

	private static final String MENU_INFO_ZULETZT_ERZEUGT = "MENU_INFO_ZULETZT_ERZEUGT";

	private static final String MENU_ACTION_JOURNAL_BESTELLVORSCHLAG = "MENU_ACTION_JOURNAL_BESTELLVORSCHLAG";

	public TabbedPaneAnfragevorschlag(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("anf.anfragevorschlag"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("anf.anfragevorschlag"), null, null,
				LPMain.getTextRespectUISPr("anf.anfragevorschlag"), IDX_PANEL_ANFRAGEVORSCHLAG);

		refreshPanelAnfragevorschlag();

		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object[] iId = panelAnfragevorschlagQP1.getSelectedIds();
		BestellpositionDto[] bestellpositionDto = new BestellpositionDto[iId.length];
		for (int i = 0; i < iId.length; i++) {
			BestellvorschlagDto bestvorDto = DelegateFactory.getInstance().getBestellvorschlagDelegate()
					.bestellvorschlagFindByPrimaryKey((Integer) iId[i]);
			if (bestvorDto != null) {
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(bestvorDto.getIArtikelId());
				bestellpositionDto[i] = new BestellpositionDto();
				bestellpositionDto[i].setArtikelIId(bestvorDto.getIArtikelId());
				bestellpositionDto[i].setBDrucken(Helper.boolean2Short(false));
				bestellpositionDto[i].setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				bestellpositionDto[i].setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
				bestellpositionDto[i].setCBez(artikelDto.getArtikelsprDto().getCBez());
				bestellpositionDto[i].setLieferantIIdWennCopyInBestellvorschlag(bestvorDto.getILieferantId());
				bestellpositionDto[i].setEinheitCNr(artikelDto.getEinheitCNr());
				bestellpositionDto[i].setNMenge(bestvorDto.getNZubestellendeMenge());

				bestellpositionDto[i].setTUebersteuerterLiefertermin(bestvorDto.getTLiefertermin());
				if (bestvorDto.getNNettoeinzelpreis() != null) {
					bestellpositionDto[i].setNNettoeinzelpreis(bestvorDto.getNNettoeinzelpreis());
				} else {
					bestellpositionDto[i].setNNettoeinzelpreis(new BigDecimal(0));
				}
				if (bestvorDto.getNNettogesamtpreis() != null) {
					bestellpositionDto[i].setNNettogesamtpreis(bestvorDto.getNNettogesamtpreis());
				} else {
					bestellpositionDto[i].setNNettogesamtpreis(new BigDecimal(0));
				}
				if (bestvorDto.getNRabattbetrag() != null) {
					bestellpositionDto[i].setNRabattbetrag(bestvorDto.getNRabattbetrag());
				} else {
					bestellpositionDto[i].setNRabattbetrag(new BigDecimal(0));
				}
				if (bestvorDto.getDRabattsatz() != null) {
					bestellpositionDto[i].setDRabattsatz(bestvorDto.getDRabattsatz());
				} else {
					bestellpositionDto[i].setDRabattsatz(new Double(0));
				}
				bestellpositionDto[i].setNMaterialzuschlag(new BigDecimal(0));
				bestellpositionDto[i].setBNettopreisuebersteuert(Helper.boolean2Short(false));

				LPMain.getPasteBuffer().writeObjectToPasteBuffer(bestellpositionDto);
			}
		}

	}

	public void einfuegenHV() throws IOException, ParserConfigurationException, SAXException, Throwable {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();
		if (o instanceof BelegpositionDto[]) {
			BestellvorschlagDto[] bestVorDto = DelegateFactory.getInstance().getBelegpostionkonvertierungDelegate()
					.konvertiereNachBestellvorschlagDto((BelegpositionDto[]) o);
			Integer iId = null;
			for (int i = 0; i < bestVorDto.length; i++) {

				iId = DelegateFactory.getInstance().getBestellvorschlagDelegate().createBestellvorschlag(bestVorDto[i]);

			}

			panelAnfragevorschlagQP1.eventYouAreSelected(false);
			panelAnfragevorschlagQP1.setSelectedId(iId);
			panelAnfragevorschlagSP1.eventYouAreSelected(false);

		}
	}

	public PanelSplit getPanelAnfragevorschlagSP1() {
		return panelAnfragevorschlagSP1;
	}

	private PanelSplit refreshPanelAnfragevorschlag() throws Throwable {
		
		FilterKriterium[] fkAnfragevorschlag = BestellungFilterFactory.getInstance().createFKBestellvorschlag();
		
		if (panelAnfragevorschlagSP1 == null) {
			panelAnfragevorschlagD1 = new PanelPositionenBestellvorschlag(getInternalFrame(),
					LPMain.getTextRespectUISPr("anf.anfragevorschlag"), null, // eventuell
																				// gibt
																				// es
																				// noch
																				// keine
																				// position
					100);
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			panelAnfragevorschlagQP1 = new PanelQuery(null, fkAnfragevorschlag, QueryParameters.UC_ID_ANFRAGEVORSCHLAG,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("anf.anfragevorschlag"), true); // flag,
																														// damit
																														// flr
																														// erst
																														// beim
																														// aufruf
																														// des
																														// panels
																														// loslaeuft

			panelAnfragevorschlagQP1.setMultipleRowSelectionEnabled(true);

			panelAnfragevorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/clipboard.png",
					LPMain.getTextRespectUISPr("anf.tooltip.anfragevorschlag"), ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG,
					RechteFac.RECHT_ANF_ANFRAGE_CUD);

			panelAnfragevorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/clipboard_next_down.png",
					LPMain.getTextRespectUISPr("bes.tooltip.bestellvorschlag.anhandangebot"),
					ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_ANGEBOT, RechteFac.RECHT_BES_BESTELLUNG_CUD);

			panelAnfragevorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/clipboard_next.png",
					LPMain.getTextRespectUISPr("anf.tooltip.anfragenausbestellvorschlag"),
					ACTION_SPECIAL_ANFRAGEN_AUS_BESTELLVORSCHLAG, RechteFac.RECHT_ANF_ANFRAGE_CUD);
			panelAnfragevorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/index_new.png",
					LPMain.getTextRespectUISPr("anf.tooltip.liefergruppenanfragenausbestellvorschlag"),
					ACTION_SPECIAL_ANFRAGEN_IN_LIEFERGRUPPENANFRAGEN, RechteFac.RECHT_ANF_ANFRAGE_CUD);

			panelAnfragevorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/branch.png",
					LPMain.getTextRespectUISPr("anf.anfragevorschlagverdichten"), ACTION_SPECIAL_VERDICHTEN,
					RechteFac.RECHT_BES_BESTELLUNG_CUD);

			if (DelegateFactory.getInstance().getParameterDelegate().getPersoenlicherBestellvorschlag()) {
				panelAnfragevorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/delete2.png",
						LPMain.getTextRespectUISPr("bes.bestellvorschlag.gemeinsameloeschen"),
						ACTION_SPECIAL_GEMEINSAME_ARTIKEL_LOESCHEN, RechteFac.RECHT_BES_BESTELLUNG_CUD);
			}

			panelAnfragevorschlagQP1.befuellePanelFilterkriterienDirekt(
					BestellungFilterFactory.getInstance().createFKDLieferantPartnerName(),
					BestellungFilterFactory.getInstance().createFKDBestellvorschlagArtikelnummer());
			panelAnfragevorschlagQP1
					.addDirektFilter(BestellungFilterFactory.getInstance().createFKDBestellvorschlagTextsuche());

			panelAnfragevorschlagSP1 = new PanelSplit(getInternalFrame(), panelAnfragevorschlagD1,
					panelAnfragevorschlagQP1, 200);

			setComponentAt(IDX_PANEL_ANFRAGEVORSCHLAG, panelAnfragevorschlagSP1);
		}

		return panelAnfragevorschlagSP1;
	}

	
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_ANFRAGEVORSCHLAG:
			refreshPanelAnfragevorschlag();
			panelAnfragevorschlagSP1.eventYouAreSelected(false);

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAnfragevorschlagQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_ANFRAGEVORSCHLAG, false);
			}

			panelAnfragevorschlagQP1.updateButtons(panelAnfragevorschlagD1.getLockedstateDetailMainKey());
			break;
		}
	}

	public InternalFrameAnfrage getInternalFrameAnfrage() {
		return (InternalFrameAnfrage) getInternalFrame();
	}

	public void deleteAuswahl() throws ExceptionLP, Throwable {
		if (panelAnfragevorschlagQP1.getSelectedIds() == null)
			return;
		for (Object id : panelAnfragevorschlagQP1.getSelectedIds()) {
			DelegateFactory.getInstance().getBestellvorschlagDelegate().removeBestellvorschlag((Integer) id);
		}
		panelAnfragevorschlagQP1.eventYouAreSelected(false);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (pdKriterienAnfragevorschlag != null) {
			pdKriterienAnfragevorschlag.eventItemchanged(e);
		}

		if (pdKritBestellvorschlagAnhandAngebot != null) {
			pdKritBestellvorschlagAnhandAngebot.eventItemchanged(e);
		}

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelAnfragevorschlagQP1) {

				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					getInternalFrameAnfrage().setKeyWasForLockMe(panelAnfragevorschlagQP1.getSelectedId() + "");
				}

				panelAnfragevorschlagD1.setKeyWhenDetailPanel(key);
				panelAnfragevorschlagD1.eventYouAreSelected(false);
				panelAnfragevorschlagQP1.updateButtons();

				panelAnfragevorschlagQP1.enableToolsPanelButtons(panelAnfragevorschlagQP1.getSelectedIds() != null
						&& panelAnfragevorschlagQP1.getSelectedIds().length > 0, PanelBasis.ACTION_DELETE);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach update im D bei einem 1:n hin.
			if (e.getSource() == panelAnfragevorschlagD1) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAnfragevorschlagQP1.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// Zeile doppelklicken
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelAnfragevorschlagQP1) {
				panelAnfragevorschlagD1.eventActionNew(e, true, false);
				panelAnfragevorschlagD1.eventYouAreSelected(false);
				setSelectedComponent(panelAnfragevorschlagSP1); // ui
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelAnfragevorschlagD1) {
				panelAnfragevorschlagSP1.eventYouAreSelected(false); // das 1:n
																		// Panel
																		// neu
																		// aufbauen
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelAnfragevorschlagD1) {
				Object oKey = panelAnfragevorschlagD1.getKeyWhenDetailPanel();
				panelAnfragevorschlagQP1.eventYouAreSelected(false);
				panelAnfragevorschlagQP1.setSelectedId(oKey);
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// wir landen hier nach der Abfolge Button Neu -> xx -> Button
			// Discard
			if (e.getSource() == panelAnfragevorschlagD1) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();

				panelAnfragevorschlagSP1.eventYouAreSelected(false); // refresh
																		// auf
																		// das
																		// gesamte
																		// 1:n
																		// panel
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == pdKriterienAnfragevorschlag) {
				try {
					// einen neuen Bestellvorschlag erstellen
					if (pdKriterienAnfragevorschlag.getVorhandeneBestellvorschlagEintrageLoeschen()) {
						DelegateFactory.getInstance().getBestellvorschlagDelegate().erstelleBestellvorschlag(
								pdKriterienAnfragevorschlag.getAuftragsvorlaufzeit(),
								pdKriterienAnfragevorschlag.getToleranz(),
								pdKriterienAnfragevorschlag.getLieferterminFuerArtikelOhneReservierung(),
								pdKriterienAnfragevorschlag.getLosIId(), pdKriterienAnfragevorschlag.getAuftragIId(),
								pdKriterienAnfragevorschlag.isbMitNichtlagerbeweirtschaftetenArtikeln(),
								pdKriterienAnfragevorschlag.isbNurBetroffeneLospositionen(),
								pdKriterienAnfragevorschlag.isbVormerklisteLoeschen(),
								pdKriterienAnfragevorschlag.isbNichtFreigegebeneAuftraegen(),
								pdKriterienAnfragevorschlag.getPartnerIIdStandort(),
								pdKriterienAnfragevorschlag.isbArtikelNurAufAuftraegeIgnorieren(),
								pdKriterienAnfragevorschlag.getBExakterAuftragsbezug());
					}
					panelAnfragevorschlagSP1.eventYouAreSelected(false);
				} catch (Throwable t) {
					ExceptionLP efc = (ExceptionLP) t;

					if (efc != null && efc.getICode() == EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
								LPMain.getInstance().getMsg(efc));
					} else {
						throw new ExceptionLP(EJBExceptionLP.FEHLER, t.toString(), null);
					}
				}
			} else if (e.getSource() == pdKriterienBestellvorschlagUeberleitung) {
				// Anfrage aus dem Bestellvorschlag erzeugen
				DelegateFactory.getInstance().getAnfrageDelegate().erzeugeAnfragenAusBestellvorschlag(
						pdKriterienBestellvorschlagUeberleitung.getBestellvorschlagUeberleitungKriterienDto(),
						pdKriterienBestellvorschlagUeberleitung.getBestellvorschlagUeberleitungKriterienDto()
								.getPartnerIIdStandort(), panelAnfragevorschlagQP1.getSelectedIdsAsInteger());
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			} else if (e.getSource() == pdKritBestellvorschlagAnhandAngebot) {

				// PJ18230
				DelegateFactory.getInstance().getBestellvorschlagDelegate().erstelleBestellvorschlagAnhandEinesAngebots(
						pdKritBestellvorschlagAnhandAngebot.angebotIId,
						pdKritBestellvorschlagAnhandAngebot.getLiefertermin());
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			} else if (e.getSource() == pdKritBestellvorschlagverdichtung) {
				// Bestellvorschlag verdichten
				DelegateFactory.getInstance().getBestellvorschlagDelegate().verdichteBestellvorschlag(
						pdKritBestellvorschlagverdichtung.getVerdichtungszeitraum(),
						pdKritBestellvorschlagverdichtung.getMindestbestellmengeBeruecksichtigen(),
						pdKritBestellvorschlagverdichtung.getProjektklammerBeruecksichtigen(),
						pdKritBestellvorschlagverdichtung.getPreiseaktualisieren());
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.equals(ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG)) {
				refreshPdKriterienAnfragevorschlag();
				getInternalFrame().showPanelDialog(pdKriterienAnfragevorschlag);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_ANFRAGEN_AUS_BESTELLVORSCHLAG)) {
				refreshPdKriterienBestellvorschlagUeberleitung();
				getInternalFrame().showPanelDialog(pdKriterienBestellvorschlagUeberleitung);

			} else if (sAspectInfo.equals(ACTION_SPECIAL_ANFRAGEN_IN_LIEFERGRUPPENANFRAGEN)) {
				String projekt = (String) JOptionPane.showInputDialog(this,
						LPMain.getTextRespectUISPr("anf.anfragevorschlagueberleiten.projekt"),
						LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.QUESTION_MESSAGE);
				DelegateFactory.getInstance().getBestellvorschlagDelegate()
						.bestellvorschlagInLiefergruppenanfragenUmwandeln(projekt);
				panelAnfragevorschlagSP1.eventYouAreSelected(false);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_VERDICHTEN)) {
				// DelegateFactory.getInstance().getBestellvorschlagDelegate().
				// verdichteBestellvorschlag();
				// panelBestellungVorschlagSP1.eventYouAreSelected(false);
				refreshPdKriterienBestellvorschlagverdichtung();
				getInternalFrame().showPanelDialog(pdKritBestellvorschlagverdichtung);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_NEUER_BESTELLVORSCHLAG_ANHAND_ANGEBOT)) {
				refreshPdKriterienBestellvorschlagAnhandAngebot();
				getInternalFrame().showPanelDialog(pdKritBestellvorschlagAnhandAngebot);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (e.getSource() == panelAnfragevorschlagQP1) {
				copyHV();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == panelAnfragevorschlagQP1) {

				if (sAspectInfo.equals(ACTION_SPECIAL_GEMEINSAME_ARTIKEL_LOESCHEN)) {
					DelegateFactory.getInstance().getBestellvorschlagDelegate().gemeinsameArtikelLoeschen();
					panelAnfragevorschlagQP1.eventYouAreSelected(false);
				} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {

					einfuegenHV();
				}
			}
		}
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlagAnhandAngebot() throws Throwable {
		pdKritBestellvorschlagAnhandAngebot = new PanelDialogKriterienBestellvorschlagAnhandAngebot(getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.tooltip.bestellvorschlag.anhandangebot"));
		return pdKritBestellvorschlagAnhandAngebot;
	}

	private PanelDialogKriterien refreshPdKriterienBestellvorschlagverdichtung() throws Throwable {
		pdKritBestellvorschlagverdichtung = new PanelDialogKriterienBestellvorschlagverdichtung(getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.title.panelbestellvorschlagverdichten"));
		return pdKritBestellvorschlagverdichtung;
	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 * 
	 * @param panelTitle der Title des aktuellen panel
	 * @throws Throwable
	 */
	public void setTitleAnfrage(String panelTitle) throws Throwable {
		// aktuellen Artikel bestimmen
		StringBuffer artikelBezeichnung = new StringBuffer("");

		if (panelAnfragevorschlagD1.getBestellvorschlagDto() != null
				&& panelAnfragevorschlagD1.getBestellvorschlagDto().getIId() != null) {
			ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(panelAnfragevorschlagD1.getBestellvorschlagDto().getIArtikelId());

			artikelBezeichnung.append(artikelDto.formatArtikelbezeichnung());
		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, artikelBezeichnung.toString());
	}

	public void setKeyWasForLockMe() throws Throwable {
		// es koennte die letzte Zeile geloescht worden sein
		panelAnfragevorschlagQP1.eventYouAreSelected(false);

		Object oKey = panelAnfragevorschlagQP1.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_BESTELLVORSCHLAG)) {
			getInternalFrame().showReportKriterien(
					new ReportBestellVorschlag(getInternalFrame(), LPMain.getTextRespectUISPr("anf.anfragevorschlag")));
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_IMPORT)) {
			HvOptional<XlsFile> xlsFile = FileOpenerFactory.anfrageStuecklistenImportCsv(this);
			if (xlsFile.isPresent()) {
				File f = xlsFile.get().getFile();
				File[] fileArray = new File(f.getParent()).listFiles();
				Workbook workbook = Workbook.getWorkbook(f);
				Sheet sheet = workbook.getSheet(0);

				ArrayList<StrukturierterImportDto> gesamtliste = new SheetImportController().importSheet(sheet,
						fileArray);

				for (int i = 1; i < gesamtliste.size(); i++) {
					StrukturierterImportDto dto = gesamtliste.get(i);

					if (dto.getLiefergruppe() != null && dto.getArtikelnr() != null
							&& dto.getArtikelnr().length() > 0) {

						try {
							Integer liefergruppe = new Integer(dto.getLiefergruppe());
							if (liefergruppe <= 400) {
								gesamtliste.set(i, dto);
							}

						} catch (NumberFormatException e1) {
							gesamtliste.set(i, dto);
						}

					}

				}

				java.sql.Date datum = DialogFactory.showDatumseingabe(LPMain.getTextRespectUISPr("label.liefertermin"));
				if (datum != null) {

					ArrayList alAngelegteArtikel = DelegateFactory.getInstance().getStuecklisteDelegate()
							.importiereStuecklistenstruktur(gesamtliste, true, new java.sql.Timestamp(datum.getTime()));

					if (alAngelegteArtikel.size() > 0) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < alAngelegteArtikel.size(); i++) {
							sb.append("\n");
							sb.append(alAngelegteArtikel.get(i));
						}

						DialogFactory.showMessageMitScrollbar(
								LPMain.getTextRespectUISPr("stkl.import.typ1.artikel.neuangelegt"), sb.toString());

					}

					panelAnfragevorschlagSP1.eventYouAreSelected(false);
				}

				workbook.close();

			}
		} else if (e.getActionCommand().equals(MENU_INFO_ZULETZT_ERZEUGT)) {
			// Dialog

			DialogZuletztErzeugt d = new DialogZuletztErzeugt(DelegateFactory.getInstance()
					.getBestellvorschlagDelegate().getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(),
					getInternalFrame());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);
		JMenu jmModul = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_MODUL);

		JMenuItem menuItemImport = new JMenuItem(LPMain.getTextRespectUISPr("lp.import"));
		menuItemImport.addActionListener(this);

		menuItemImport.setActionCommand(ACTION_SPECIAL_IMPORT);
		jmModul.add(menuItemImport, 0);

		JMenu journal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
		JMenuItem menuItemBestellVorschlag = new JMenuItem(LPMain.getTextRespectUISPr("anf.anfragevorschlag"));
		menuItemBestellVorschlag.addActionListener(this);
		menuItemBestellVorschlag.setActionCommand(MENU_ACTION_JOURNAL_BESTELLVORSCHLAG);
		journal.add(menuItemBestellVorschlag);

		JMenu menuInfo = new WrapperMenu("lp.info", this);

		JMenuItem menuItemZuletztErzeugt = new JMenuItem(LPMain.getTextRespectUISPr("lp.zuletzt.erzeugt"));
		menuItemZuletztErzeugt.addActionListener(this);
		menuItemZuletztErzeugt.setActionCommand(MENU_INFO_ZULETZT_ERZEUGT);

		menuInfo.add(menuItemZuletztErzeugt);

		wrapperMenuBar.add(menuInfo);

		return wrapperMenuBar;

	}

	private void refreshPdKriterienAnfragevorschlag() throws Throwable {
		pdKriterienAnfragevorschlag = new PanelDialogKriterienBestellvorschlag2(getInternalFrame(),
				LPMain.getTextRespectUISPr("anf.kriterien.anfragevorschlag"));
	}

	private void refreshPdKriterienBestellvorschlagUeberleitung() throws Throwable {
		BestellvorschlagDto dto = null;
		if (panelAnfragevorschlagQP1.getSelectedId() != null) {
			dto = DelegateFactory.getInstance().getBestellvorschlagDelegate()
					.bestellvorschlagFindByPrimaryKey((Integer) panelAnfragevorschlagQP1.getSelectedId());
		}

		pdKriterienBestellvorschlagUeberleitung = new PanelDialogKriterienBestellvorschlagUeberleitung2(true,
				getInternalFrame(), dto, LPMain.getTextRespectUISPr("anf.kriterien.bestellvorschlagueberleitung"));
	}
}
