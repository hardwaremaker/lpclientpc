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
package com.lp.client.lieferschein;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.bestellung.ReportBestellVorschlag;
import com.lp.client.fertigung.DialogStklGeaendert;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagUeberleitung2;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.DialogZuletztErzeugt;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.lieferschein.service.AusliefervorschlagDto;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
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
public class TabbedPaneAusliefervorschlag extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelSplit panelAusliefervorschlagSP1 = null; // FLR 1:n Liste
	private PanelQuery panelAusliefervorschlagQP1 = null;

	public PanelQuery getPanelAusliefervorschlagQP1() {
		return panelAusliefervorschlagQP1;
	}

	private PanelAusliefervorschlagPosition panelAusliefervorschlagD1 = null;

	protected final static int IDX_PANEL_AUSLIEFERVORSCHLAG = 0;

	private PanelDialogKriterienAusliefervorschlag pdKriterienAusliefervorschlag = null;
	private PanelDialogKriterienAusliefervorschlagUeberleitung pdKriterienAusliefervorschlagUeberleitung = null;

	private static final String ACTION_SPECIAL_NEUER_AUSLIEFERVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_neuer_ausliefervorschlag";
	private static final String ACTION_SPECIAL_LIEFERSCHEINE_AUS_AUSLIEFERVORSCHLAG = PanelBasis.ALWAYSENABLED
			+ "action_special_lieferschein_aus_ausliefervorschlag";

	private static final String ACTION_SPECIAL_VERFUEGBARKEIT_BERECHNEN = PanelBasis.ALWAYSENABLED
			+ "action_special_verfuegbarkeit_berechnen";

	private static final String MENU_ACTION_JOURNAL_AUSLIEFERVORSCHLAG = "MENU_ACTION_JOURNAL_AUSLIEFERVORSCHLAG";
	private static final String MENU_ACTION_JOURNAL_LINIENABRUFE = "MENU_ACTION_JOURNAL_LINIENABRUFE";

	private static final String MENU_INFO_ZULETZT_ERZEUGT = "MENU_INFO_ZULETZT_ERZEUGT";

	public TabbedPaneAusliefervorschlag(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("ls.ausliefervorschlag"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("ls.ausliefervorschlag"), null, null,
				LPMain.getTextRespectUISPr("ls.ausliefervorschlag"), IDX_PANEL_AUSLIEFERVORSCHLAG);

		refreshPanelausliefervorschlag();

		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object[] iId = panelAusliefervorschlagQP1.getSelectedIds();
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

			panelAusliefervorschlagQP1.eventYouAreSelected(false);
			panelAusliefervorschlagQP1.setSelectedId(iId);
			panelAusliefervorschlagSP1.eventYouAreSelected(false);

		}
	}

	public PanelSplit getPanelAnfragevorschlagSP1() {
		return panelAusliefervorschlagSP1;
	}

	private PanelSplit refreshPanelausliefervorschlag() throws Throwable {
		if (panelAusliefervorschlagSP1 == null) {
			panelAusliefervorschlagD1 = new PanelAusliefervorschlagPosition(getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.ausliefervorschlag"), null, this);
			FilterKriterium[] fkPositionen = SystemFilterFactory.getInstance().createFKMandantCNr();

			String[] aWhichButtonIUse = null;

			panelAusliefervorschlagQP1 = new PanelQuery(null, fkPositionen, QueryParameters.UC_ID_AUSLIEFERVORSCHLAG,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("ls.ausliefervorschlag"), true); // flag,
																														// damit
																														// flr
																														// erst
																														// beim
																														// aufruf
																														// des
																														// panels
																														// loslaeuft

			panelAusliefervorschlagQP1.setMultipleRowSelectionEnabled(true);
			
			
			
			Map<String, String> m = new LinkedHashMap<String, String>();
			m.put(AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_FILTER_ALLE, LPMain.getTextRespectUISPr("ls.ausliefervorschlag.filter.verfuegbarkeit.alle"));
			m.put(AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_FILTER_AUSREICHEND_VERFUEGBAR, LPMain.getTextRespectUISPr("ls.ausliefervorschlag.filter.verfuegbarkeit.verfuegbar"));
			m.put(AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_FILTER_TEILMENGE_VERFUEGBAR, LPMain.getTextRespectUISPr("ls.ausliefervorschlag.filter.verfuegbarkeit.teilverfuegbar"));
			m.put(AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_FILTER_NICHT_VERFUEGBAR, LPMain.getTextRespectUISPr("ls.ausliefervorschlag.filter.verfuegbarkeit.nichtverfuegbar"));
			

			panelAusliefervorschlagQP1.setFilterComboBox(m, new FilterKriterium(AusliefervorschlagFac.AUSLIEFERVORSCHLAGHANDLER_VERFUEGBARKEITEN,
					true, "" + "", FilterKriterium.OPERATOR_GT, false), true, "", false);
			
			
			panelAusliefervorschlagQP1.addDirektFilter(new FilterKriteriumDirekt(
					LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("label.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
															// als
					// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));
			panelAusliefervorschlagQP1.addDirektFilter(new FilterKriteriumDirekt(
					"flrkunde_lieferadresse" + "." + KundeFac.FLR_PARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE,
					LPMain.getTextRespectUISPr("ls.ausliefervorschlag.lieferadresse"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
															// als
					// 'XX%'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));
			panelAusliefervorschlagQP1
					.addDirektFilter(new FilterKriteriumDirekt("flrartikel.c_nr", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("label.ident"), FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																												// als
							// 'XX%'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT));

			panelAusliefervorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/clipboard.png",
					LPMain.getTextRespectUISPr("ls.ausliefervorschlag.neuerstellen"),
					ACTION_SPECIAL_NEUER_AUSLIEFERVORSCHLAG, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

			panelAusliefervorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/clipboard_next.png",
					LPMain.getTextRespectUISPr("ls.ausliefervorschlag.ueberleiten"),
					ACTION_SPECIAL_LIEFERSCHEINE_AUS_AUSLIEFERVORSCHLAG, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

			panelAusliefervorschlagQP1.createAndSaveAndShowButton("/com/lp/client/res/folder_refresh.png",
					LPMain.getTextRespectUISPr("ls.ausliefervorschlag.aktualisierelagerstaende"),
					ACTION_SPECIAL_VERFUEGBARKEIT_BERECHNEN, null);

			panelAusliefervorschlagSP1 = new PanelSplit(getInternalFrame(), panelAusliefervorschlagD1,
					panelAusliefervorschlagQP1, 200);

			setComponentAt(IDX_PANEL_AUSLIEFERVORSCHLAG, panelAusliefervorschlagSP1);
		}

		return panelAusliefervorschlagSP1;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_AUSLIEFERVORSCHLAG:
			refreshPanelausliefervorschlag();
			panelAusliefervorschlagSP1.eventYouAreSelected(false);

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAusliefervorschlagQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSLIEFERVORSCHLAG, false);
			}

			panelAusliefervorschlagQP1.updateButtons(panelAusliefervorschlagD1.getLockedstateDetailMainKey());
			break;
		}
	}

	public InternalFrameLieferschein getInternalFrameLieferschein() {
		return (InternalFrameLieferschein) getInternalFrame();
	}

	public void deleteAuswahl() throws ExceptionLP, Throwable {
		if (panelAusliefervorschlagQP1.getSelectedIds() == null)
			return;
		for (Object id : panelAusliefervorschlagQP1.getSelectedIds()) {
			DelegateFactory.getInstance().getBestellvorschlagDelegate().removeBestellvorschlag((Integer) id);
		}
		panelAusliefervorschlagQP1.eventYouAreSelected(false);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (pdKriterienAusliefervorschlag != null) {
			pdKriterienAusliefervorschlag.eventItemchanged(e);
		}

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelAusliefervorschlagQP1) {

				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					getInternalFrameLieferschein().setKeyWasForLockMe(panelAusliefervorschlagQP1.getSelectedId() + "");
				}

				panelAusliefervorschlagD1.setKeyWhenDetailPanel(key);
				panelAusliefervorschlagD1.eventYouAreSelected(false);
				panelAusliefervorschlagQP1.updateButtons();

				panelAusliefervorschlagQP1.enableToolsPanelButtons(panelAusliefervorschlagQP1.getSelectedIds() != null
						&& panelAusliefervorschlagQP1.getSelectedIds().length > 0, PanelBasis.ACTION_DELETE);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach update im D bei einem 1:n hin.
			if (e.getSource() == panelAusliefervorschlagD1) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAusliefervorschlagQP1.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// Zeile doppelklicken
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelAusliefervorschlagQP1) {
				panelAusliefervorschlagD1.eventActionNew(e, true, false);
				panelAusliefervorschlagD1.eventYouAreSelected(false);
				setSelectedComponent(panelAusliefervorschlagSP1); // ui
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelAusliefervorschlagD1) {
				panelAusliefervorschlagSP1.eventYouAreSelected(false); // das
																		// 1:n
																		// Panel
																		// neu
																		// aufbauen
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelAusliefervorschlagD1) {
				Object oKey = panelAusliefervorschlagD1.getKeyWhenDetailPanel();
				panelAusliefervorschlagQP1.eventYouAreSelected(false);
				panelAusliefervorschlagQP1.setSelectedId(oKey);
				panelAusliefervorschlagSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// wir landen hier nach der Abfolge Button Neu -> xx -> Button
			// Discard
			if (e.getSource() == panelAusliefervorschlagD1) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();

				panelAusliefervorschlagSP1.eventYouAreSelected(false); // refresh
																		// auf
																		// das
																		// gesamte
																		// 1:n
																		// panel
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == pdKriterienAusliefervorschlag) {
				try {

					DelegateFactory.getInstance().getAusliefervorschlagDelegate()
							.erstelleAusliefervorschlag(pdKriterienAusliefervorschlag.getAusliefertermin());

					panelAusliefervorschlagSP1.eventYouAreSelected(false);
				} catch (Throwable t) {
					ExceptionLP efc = (ExceptionLP) t;

					if (efc != null && efc.getICode() == EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
								LPMain.getInstance().getMsg(efc));
					} else {
						throw new ExceptionLP(EJBExceptionLP.FEHLER, t.toString(), null);
					}
				}
			} else if (e.getSource() == pdKriterienAusliefervorschlagUeberleitung) {

				// Lieferscheine aus dem Bestellvorschlag erzeugen
				String s= DelegateFactory.getInstance()
						.getAusliefervorschlagDelegate()
						.ausliefervorschlagUeberleiten(pdKriterienAusliefervorschlagUeberleitung.getSetIIds(),
								pdKriterienAusliefervorschlagUeberleitung.getKundeIId(),
								pdKriterienAusliefervorschlagUeberleitung.getKundeIIdLieferadresse());
				panelAusliefervorschlagSP1.eventYouAreSelected(false);

				if (s != null && s.length() > 0) {
					
					DialogAusliefervorschlagUeberleitenError d = new DialogAusliefervorschlagUeberleitenError(s);
						LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.equals(ACTION_SPECIAL_NEUER_AUSLIEFERVORSCHLAG)) {
				refreshPdKriterienAsuliefervorschlag();
				getInternalFrame().showPanelDialog(pdKriterienAusliefervorschlag);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_VERFUEGBARKEIT_BERECHNEN)) {
				DelegateFactory.getInstance().getAusliefervorschlagDelegate().verfuegbareMengeBerechnen();

				panelAusliefervorschlagSP1.eventYouAreSelected(false);

			} else if (sAspectInfo.equals(ACTION_SPECIAL_LIEFERSCHEINE_AUS_AUSLIEFERVORSCHLAG)) {
				refreshPdKriterienAusliefervorschlagUeberleitung();
				getInternalFrame().showPanelDialog(pdKriterienAusliefervorschlagUeberleitung);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (e.getSource() == panelAusliefervorschlagQP1) {
				copyHV();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			if (e.getSource() == panelAusliefervorschlagQP1) {
				einfuegenHV();
			}
		}
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

		if (getPanelAusliefervorschlagQP1().getSelectedId() != null) {

			AusliefervorschlagDto auslDto = DelegateFactory.getInstance().getAusliefervorschlagDelegate()
					.ausliefervorschlagFindByPrimaryKey((Integer) getPanelAusliefervorschlagQP1().getSelectedId());
			ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(auslDto.getArtikelIId());

			artikelBezeichnung.append(artikelDto.formatArtikelbezeichnung());
		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, artikelBezeichnung.toString());
	}

	public void setKeyWasForLockMe() throws Throwable {
		// es koennte die letzte Zeile geloescht worden sein
		panelAusliefervorschlagQP1.eventYouAreSelected(false);

		Object oKey = panelAusliefervorschlagQP1.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_AUSLIEFERVORSCHLAG)) {
			getInternalFrame().showReportKriterien(new ReportAusliefervorschlag(getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.ausliefervorschlag")));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_LINIENABRUFE)) {
			getInternalFrame().showReportKriterien(
					new ReportLinienabrufe(getInternalFrame(), LPMain.getTextRespectUISPr("ls.ausliefervorschlag")));
		} else if (e.getActionCommand().equals(MENU_INFO_ZULETZT_ERZEUGT)) {
			// Dialog

			DialogZuletztErzeugt d = new DialogZuletztErzeugt(
					SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_AUSLIEFERVORSCHLAG
							+ LPMain.getTheClient().getMandant(),
					getInternalFrame());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);
		JMenu jmModul = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_MODUL);

		JMenu journal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
		JMenuItem menuItemAusliefervorschlag = new JMenuItem(LPMain.getTextRespectUISPr("ls.ausliefervorschlag"));
		menuItemAusliefervorschlag.addActionListener(this);
		menuItemAusliefervorschlag.setActionCommand(MENU_ACTION_JOURNAL_AUSLIEFERVORSCHLAG);
		journal.add(menuItemAusliefervorschlag);

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FORECAST)) {

			JMenuItem menuItemLinienabrufe = new JMenuItem(LPMain.getTextRespectUISPr("fc.linienabrufe"));
			menuItemLinienabrufe.addActionListener(this);
			menuItemLinienabrufe.setActionCommand(MENU_ACTION_JOURNAL_LINIENABRUFE);
			journal.add(menuItemLinienabrufe);
		}

		JMenu menuInfo = new WrapperMenu("lp.info", this);

		JMenuItem menuItemZuletztErzeugt = new JMenuItem(LPMain.getTextRespectUISPr("lp.zuletzt.erzeugt"));
		menuItemZuletztErzeugt.addActionListener(this);
		menuItemZuletztErzeugt.setActionCommand(MENU_INFO_ZULETZT_ERZEUGT);

		menuInfo.add(menuItemZuletztErzeugt);
		wrapperMenuBar.add(menuInfo);
		return wrapperMenuBar;

	}

	private void refreshPdKriterienAsuliefervorschlag() throws Throwable {
		pdKriterienAusliefervorschlag = new PanelDialogKriterienAusliefervorschlag(getInternalFrame(),
				LPMain.getTextRespectUISPr("ls.ausliefervorschlag.kriterien"));
	}

	private void refreshPdKriterienAusliefervorschlagUeberleitung() throws Throwable {

		Set<Integer> setIIds = null;

		int indexAlle = 0;
		int indexMarkierte = 1;
		int iAnzahlOptionen = 2;

		Object[] aOptionenVerdichten = new Object[iAnzahlOptionen];

		aOptionenVerdichten[indexAlle] = LPMain.getTextRespectUISPr("ls.ausliefervorschlag.ueberleiten.frage.alle");
		aOptionenVerdichten[indexMarkierte] = LPMain
				.getTextRespectUISPr("ls.ausliefervorschlag.ueberleiten.frage.markierte");

		int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
				LPMain.getTextRespectUISPr("ls.ausliefervorschlag.ueberleiten.frage"),
				LPMain.getTextRespectUISPr("lp.frage"), aOptionenVerdichten, aOptionenVerdichten[0]);

		if (iAuswahl == indexMarkierte) {
			setIIds = new HashSet<Integer>();

			Object[] ids = getPanelAusliefervorschlagQP1().getSelectedIds();
			for (int i = 0; i < ids.length; i++) {
				setIIds.add((Integer) ids[i]);
			}

		}

		pdKriterienAusliefervorschlagUeberleitung = new PanelDialogKriterienAusliefervorschlagUeberleitung(
				getInternalFrame(), LPMain.getTextRespectUISPr("anf.kriterien.bestellvorschlagueberleitung"), setIIds);
	}
}
