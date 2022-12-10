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

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.component.ArtikelsetViewController;
import com.lp.client.frame.component.DialogGeaenderteKonditionenVK;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPaneBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.DialogPositionenBarcodeerfassung;
import com.lp.client.lieferschein.DialogPositionenSchnellerfassung;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.util.HelperTimestamp;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.Helper;

/**
 * 
 * <p>
 * Diese Klasse ist eine TabbedPane, die fuer alle Rechnungstypen gleich ist
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 20.11.2004
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.36 $
 */
abstract class TabbedPaneRechnungAll extends TabbedPaneBasis implements ICopyPaste {

	private static final long serialVersionUID = 1L;
	protected final String rechnungsTyp;
	private RechnungDto rechnungDto = null;
	private KundeDto kundeDto = null;
	private RechnungPositionDto rePositionDto = null;
	private LieferscheinDto lieferscheinDto = null;
	private KundeDto kundeDtoLieferschein = null;

	private PanelQuery panelQueryRechnung = null;
	private PanelRechnungKopfdaten panelDetailKopfdaten = null;
	private PanelSplit panelSplitPositionen = null;
	private PanelQuery panelQueryPositionen = null;
	private PanelRechnungPosition panelDetailPositionen = null;

	public PanelRechnungPosition getPanelDetailPositionen() {
		return panelDetailPositionen;
	}

	private PanelSplit panelSplitKontierung = null;
	private PanelQuery panelQueryKontierung = null;

	protected PanelSplit panelSplitZahlungen = null;
	private PanelQuery panelQueryZahlungen = null;
	protected PanelRechnungZahlung panelDetailZahlung = null;

	private PanelRechnungKontierung panelDetailKontierung = null;
	private PanelQueryFLR panelQueryFLRAuftragauswahl = null;
	private PanelQueryFLR panelQueryFLRLieferschein = null;
	private PanelQueryFLR panelQueryFLRRechnung = null;
	private PanelQueryFLR panelQueryFLRAngebot = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;
	private PanelQueryFLR panelQueryFLRStatistikadresse = null;

	private PanelQueryFLR panelQueryFLRRechnungZuErledigen = null;

	protected PanelQueryFLR panelQueryFLRRechnungAktivieren = null;
	protected PanelQueryFLR panelQueryFLRRechnungDrucken = null;
	protected PanelQueryFLR panelQueryFLRRechnungMail = null;

	private static final String ACTION_SPECIAL_SORTIERE_LS_NACH_ANSPRECHPARTNER = "ACTION_SPECIAL_SORTIERE_LS_NACH_ANSPRECHPARTNER";
	private final String BUTTON_SORTIERE_LS_NACH_ANSPRECHPARTNER = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SORTIERE_LS_NACH_ANSPRECHPARTNER;

	private static final String ACTION_SPECIAL_SORTIERE_RE_NACH_AUFTRAGSNUMMER = "ACTION_SPECIAL_SORTIERE_RE_NACH_AUFTRAGSNUMMER";
	private final String BUTTON_SORTIERE_RE_NACH_AUFTRAGSNUMMER = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SORTIERE_RE_NACH_AUFTRAGSNUMMER;

	protected final boolean bAuftragRechnung;

	public static int IDX_RECHNUNGEN = -1;
	public static int IDX_KOPFDATEN = -1;
	public static int IDX_POSITIONEN = -1;
	// wird spaeter gesetzt
	protected int iDX_KONTIERUNG = -1;
	protected int iIDX_ZAHLUNGEN = -1;

	protected final static String MENUE_ACTION_DATEI_DRUCKEN = "MENU_ACTION_DATEI_DRUCKEN";
	protected final static String MENUE_ACTION_NEU_DATUM = "MENUE_ACTION_NEU_DATUM";
	protected static final String MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN";
	protected final static String MENUE_ACTION_DATEI_ZAHLSCHEIN_DRUCKEN = "MENU_ACTION_DATEI_ZAHLSCHEIN_DRUCKEN";

	protected static final String MENUE_ACTION_BEARBEITEN_VERTRETER_AENDERN = "MENUE_ACTION_BEARBEITEN_VERTRETER_AENDERN";
	protected static final String MENUE_ACTION_BEARBEITEN_STATISTIKADRESSE_AENDERN = "MENUE_ACTION_BEARBEITEN_STATISTIKADRESSE_AENDERN";

	protected final static String MENUE_ACTION_OFFENE_LS_MIT_AB_STAFFELPREISEN_VERRECHNEN = "MENUE_ACTION_OFFENE_LS_MIT_AB_STAFFELPREISEN_VERRECHNEN";
	protected final static String MENUE_ACTION_ZAHLUNGSPLAN = "MENUE_ACTION_ZAHLUNGSPLAN";
	protected final static String MENUE_INFO_MATERIALEINSATZ = "MENUE_INFO_MATERIALEINSATZ";

	protected static final String EXTRA_NEU_AUS_AUFTRAG = "rechnung_aus_auftrag";
	protected static final String EXTRA_NEU_AUS_LIEFERSCHEIN = "rechnung_aus_lieferschein";
	protected final static String MY_OWN_NEW_AUS_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_AUFTRAG;
	protected final static String MY_OWN_NEW_AUS_LIEFERSCHEIN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_AUS_LIEFERSCHEIN;
	protected final static String MY_OWN_NEW_AUS_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW + "EXTRA_NEU_AUS_ANGEBOT";

	protected final static String MY_OWN_NEW_AUS_RECHNUNG = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_AUS_RECHNUNG";
	public final static String MY_OWN_NEW_SNRBARCODEERFASSUNG = PanelBasis.ACTION_MY_OWN_NEW + "SNRBARCODEERFASSUNG";
	public final static String EXTRA_NEU_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN = "alle_positionen_aus_auftrag_in_rechnung_uebernehmen";
	public final static String MY_OWN_NEW_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN = PanelBasis.ACTION_MY_OWN_NEW_ENABLED_ON_MULTISELECT
			+ EXTRA_NEU_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN;

	public final static String MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK";

	private final String BUTTON_SORTIERE_NACH_ARTIKELNUMMER = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_SORTIERE_NACH_ARTIKELNUMMER";

	private final String BUTTON_SCHNELLERFASSUNG_POSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_SCHNELLERFASSUNG_POSITIONEN";

	public final static String MY_OWN_NEW_ANZAHLUNGEN_PROZENT = PanelBasis.ACTION_MY_OWN_NEW + "ANZAHLUNGEN_PROZENT";

	public TabbedPaneRechnungAll(InternalFrame internalFrameI, String rechnungsTyp, String sTitle) throws Throwable {
		super(internalFrameI, sTitle);
		this.rechnungsTyp = rechnungsTyp;
		bAuftragRechnung = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_AUFTRAG_RECHNUNG);
		jbInitTPAll();
		initComponents();
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	private void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	public final RechnungDto getRechnungDto() {
		return rechnungDto;
	}

	protected final RechnungPositionDto getRechnungPositionDto() {
		return rePositionDto;
	}

	protected final boolean pruefePositionIstEinLieferschein() throws Throwable {
		boolean bPositionIstEinLS = false;
		// es muss eine RE-Position gewaehlt sein
		if (getRechnungPositionDto() != null) {
			// und diese muss ein Lieferschein sein
			if (getRechnungPositionDto().getRechnungpositionartCNr() != null && getRechnungPositionDto()
					.getRechnungpositionartCNr().equals(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
				// dann hole die Daten
				lieferscheinDto = DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey(getRechnungPositionDto().getLieferscheinIId());
				// wenn Rechnung und LS den gleichen Kunden haben, muss ich den
				// nicht mehr extra holen
				if (getRechnungDto().getKundeIId().equals(getLieferscheinDto().getKundeIIdLieferadresse())) {
					kundeDtoLieferschein = getKundeDto();
				} else {
					kundeDtoLieferschein = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(getLieferscheinDto().getKundeIIdLieferadresse());
				}
				bPositionIstEinLS = true;
			}
		}
		// wenn kein Lieferschein, dann die LS-Infos null setzen
		if (!bPositionIstEinLS) {
			lieferscheinDto = null;
			kundeDtoLieferschein = null;
		}
		return bPositionIstEinLS;
	}

	protected final LieferscheinDto getLieferscheinDto() {
		return lieferscheinDto;
	}

	protected final KundeDto getKundeLieferadresseDto() {
		return kundeDtoLieferschein;
	}

	protected final void setRechnungPositionDto(RechnungPositionDto rePositionDto) {
		this.rePositionDto = rePositionDto;
	}

	protected void setRechnungDto(RechnungDto rechnungDto) throws Throwable {
		if (this.rechnungDto != null && rechnungDto != null && this.rechnungDto.getIId() != null
				&& rechnungDto.getIId() != null) {
			// bin ich in der auswahl oder in den kopfdaten ...
			if (this.getSelectedIndex() == IDX_RECHNUNGEN || this.getSelectedIndex() == IDX_KOPFDATEN) {
				// ... und habe ich auf eine andere rechnung "gewechselt"?
				if (!this.rechnungDto.getIId().equals(rechnungDto.getIId())) {
					// dann hab ich vorlaeufig keine gueltige position mehr
					// ist fuer sicht lieferschein wichtig!
					setRechnungPositionDto(null);
				}
			}
		}
		this.rechnungDto = rechnungDto;
		refreshFilterPositionen();
		refreshFilterKontierung();
		refreshFilterZahlungen();
		if (getRechnungDto() != null) {
			if (getRechnungDto().getKundeIId() != null) {
				setKundeDto(DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(getRechnungDto().getKundeIId()));

				String title = getTitle();
				if (getSelectedIndex() == iIDX_ZAHLUNGEN) {
					if (getKundeDto().getIidDebitorenkonto() != null) {
						KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
								.kontoFindByPrimaryKey(getKundeDto().getIidDebitorenkonto());
						title += LPMain.getMessageTextRespectUISPr("rech.zahlungen.debitorennr", kontoDto.getCNr());
					}
				}

				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, title);
			}
		} else {
			// keine rechnung -> kein kunde
			setKundeDto(null);
			// kein Titel
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}
	}

	protected String getTitle() {
		StringBuffer sTitle = new StringBuffer();
		if (getRechnungDto() != null) {
			if (getRechnungDto().getCNr() != null) {
				sTitle.append(getRechnungDto().getCNr());
				sTitle.append(" ");
			}
			sTitle.append(getKundeDto().getPartnerDto().formatFixTitelName1Name2());
		}
		return sTitle.toString();
	}

	protected String getRechnungstyp() {
		return rechnungsTyp;
	}

	private final void jbInitTPAll() throws Throwable {
		// 1 Liste der Rechnungen
		IDX_RECHNUNGEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"));
		// 2 Kopfdaten
		IDX_KOPFDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getTextRespectUISPr("lp.kopfdaten"));
		// 3 Positionen
		IDX_POSITIONEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("rechnung.tab.oben.positionen.title"), null, null,
				LPMain.getTextRespectUISPr("rechnung.tab.oben.positionen.tooltip"));

		setSelectedComponent(getPanelQueryRechnung());
		// getPanelQueryRechnung().eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		// ItemChangedEvent it = new ItemChangedEvent(getPanelQueryRechnung(),
		// ItemChangedEvent.ITEM_CHANGED);
		// lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	protected String[] getAWhichButtonIUseRechnung() {
		String[] aWhichButtonIUseRechnung = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };
		return aWhichButtonIUseRechnung;
	}

	protected PanelQuery getPanelQueryRechnung() throws Throwable {
		if (panelQueryRechnung == null) {
			String[] aWhichButtonIUseRechnung = getAWhichButtonIUseRechnung();
			int iUsecase;
			FilterKriterium[] filters = null;
			if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				filters = RechnungFilterFactory.getInstance().createFKRechnungen();
				iUsecase = QueryParameters.UC_ID_RECHNUNG;
			} else if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				filters = RechnungFilterFactory.getInstance().createFKGutschriften();
				iUsecase = QueryParameters.UC_ID_GUTSCHRIFT;
			} else {
				filters = RechnungFilterFactory.getInstance().createFKProformarechnungen();
				iUsecase = QueryParameters.UC_ID_PROFORMARECHNUNG;
			}

			panelQueryRechnung = new PanelQuery(RechnungFilterFactory.getInstance().createQTPanelRechnungauswahl(),
					filters, iUsecase, aWhichButtonIUseRechnung, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);
			FilterKriteriumDirekt fkDirekt1;
			if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				fkDirekt1 = RechnungFilterFactory.getInstance().createFKDGutschriftnummer();
			} else {
				fkDirekt1 = RechnungFilterFactory.getInstance().createFKDRechnungnummer();
			}
			FilterKriteriumDirekt fkDirekt2 = RechnungFilterFactory.getInstance().createFKDKundename();
			panelQueryRechnung.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

			// PJ18912

			panelQueryRechnung.addDirektFilter(RechnungFilterFactory.getInstance().createFKDProjekt());

			panelQueryRechnung.addDirektFilter(RechnungFilterFactory.getInstance().createFKDTextSuchen());

			FilterKriteriumDirekt fkDirektStatistikadresse = RechnungFilterFactory.getInstance()
					.createFKDKundestatistikadresse();
			panelQueryRechnung.addDirektFilter(fkDirektStatistikadresse);
			panelQueryRechnung.befuelleFilterkriteriumSchnellansicht(
					RechnungFilterFactory.getInstance().createFKSchnellansicht());

			// PJ19235

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_DEBITORENNUMMER_IN_AUSWAHLLISTE,
							ParameterFac.KATEGORIE_RECHNUNG, LPMain.getTheClient().getMandant());

			if ((Boolean) parameter.getCWertAsObject()) {

				if (!this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {

					FilterKriteriumDirekt fkDirektDebitorenkonto = RechnungFilterFactory.getInstance()
							.createFKDDebitorenkonto();
					panelQueryRechnung.addDirektFilter(fkDirektDebitorenkonto);
				}
			}

			if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)
					|| this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {

				if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
						&& (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_R)
								|| DelegateFactory.getInstance().getTheJudgeDelegate()
										.hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_CUD))) {

					// SP3051 nicht bei Proformarechnung
					if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {

						panelQueryRechnung.createAndSaveAndShowButton("/com/lp/client/res/presentation_chart16x16.png",
								LPMain.getTextRespectUISPr("rech.rechnungausangebot"), MY_OWN_NEW_AUS_ANGEBOT,
								RechteFac.RECHT_RECH_RECHNUNG_CUD);
					}
				}
				if (bAuftragRechnung && this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {

					panelQueryRechnung.createAndSaveAndShowButton("/com/lp/client/res/auftrag16x16.png",
							LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemauftrag"), MY_OWN_NEW_AUS_AUFTRAG,
							RechteFac.RECHT_RECH_RECHNUNG_CUD);
				}
				if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERSCHEIN)
						&& (DelegateFactory.getInstance().getTheJudgeDelegate()
								.hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_R)
								|| DelegateFactory.getInstance().getTheJudgeDelegate()
										.hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_CUD))) {

					String text = LPMain.getTextRespectUISPr("rech.rechnungauslieferschein");
					if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
						text = LPMain.getTextRespectUISPr("rech.proformarechnungauslieferschein");
					}

					panelQueryRechnung.createAndSaveAndShowButton("/com/lp/client/res/truck_red16x16.png", text,
							MY_OWN_NEW_AUS_LIEFERSCHEIN, RechteFac.RECHT_RECH_RECHNUNG_CUD);
				}

				if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
					panelQueryRechnung.createAndSaveAndShowButton("/com/lp/client/res/calculator16x16.png",
							LPMain.getTextRespectUISPr("rech.rechnungausrechnung"), MY_OWN_NEW_AUS_RECHNUNG,
							RechteFac.RECHT_RECH_RECHNUNG_CUD);
				}

			}
			this.setComponentAt(IDX_RECHNUNGEN, panelQueryRechnung);
		}
		return panelQueryRechnung;
	}

	private PanelSplit getPanelSplitPositionen(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitPositionen == null && bNeedInstantiationIfNull) {
			panelSplitPositionen = new PanelSplit(getInternalFrame(), getPanelDetailPositionen(true),
					getPanelQueryPositionen(true), 150);
			setComponentAt(IDX_POSITIONEN, panelSplitPositionen);
		}
		return panelSplitPositionen;
	}

	private PanelSplit getPanelSplitKontierung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitKontierung == null && bNeedInstantiationIfNull) {
			panelSplitKontierung = new PanelSplit(getInternalFrame(), getPanelDetailKontierung(true),
					getPanelQueryKontierung(true), 200);
			setComponentAt(iDX_KONTIERUNG, panelSplitKontierung);
		}
		return panelSplitKontierung;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == getPanelQueryRechnung()) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeRechnungDto(key);
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					setSelectedComponent(getPanelSplitPositionen(true));
				}
			} else if (e.getSource() == panelQueryFLRVertreter) {
				Integer personalIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				DelegateFactory.getInstance().getRechnungDelegate().updateRechnungVertreter(getRechnungDto().getIId(),
						personalIId);
				getPanelQueryRechnung().eventYouAreSelected(false);

				if (panelDetailKopfdaten != null) {
					panelDetailKopfdaten.eventYouAreSelected(false);
				}

			} else if (e.getSource() == panelQueryFLRStatistikadresse) {
				Integer kundeIIdStatikstikadresse = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				DelegateFactory.getInstance().getRechnungDelegate()
						.updateRechnungStatistikadresse(getRechnungDto().getIId(), kundeIIdStatikstikadresse);

			} else if (e.getSource() == panelQueryFLRRechnungZuErledigen) {
				Object[] rechnungIIds = panelQueryFLRRechnungZuErledigen.getSelectedIds();

				for (int i = 0; i < rechnungIIds.length; i++) {
					DelegateFactory.getInstance().getRechnungDelegate().manuellErledigen((Integer) rechnungIIds[i]);
				}

				if (panelQueryFLRRechnungZuErledigen.getDialog() != null) {
					panelQueryFLRRechnungZuErledigen.getDialog().setVisible(false);
				}
				getPanelQueryRechnung().eventYouAreSelected(false);

			} else if (e.getSource() == panelQueryFLRRechnung) {
				Integer rechnungIId = (Integer) panelQueryFLRRechnung.getSelectedId();

				// Vorher die Konditionen gegen den Kunden pruefen
				RechnungDto rDtoVorhanden = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungFindByPrimaryKey(rechnungIId);

				// SP5284
				boolean bZahlungsplanUebernehmen = false;
				if (rDtoVorhanden.getNMtlZahlbetrag() != null && rDtoVorhanden.getIZahltagMtlZahlbetrag() != null) {

					bZahlungsplanUebernehmen = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getMessageTextRespectUISPr("rech.rechnungkopieren.zahlungsplanuebernehmen",
									Helper.formatZahl(rDtoVorhanden.getNMtlZahlbetrag(), 2,
											LPMain.getTheClient().getLocUi()) + " " + rDtoVorhanden.getWaehrungCNr(),
									rDtoVorhanden.getIZahltagMtlZahlbetrag()));
				}

				Integer rechnungIIdNeu = null;

				DialogGeaenderteKonditionenVK dialog = new DialogGeaenderteKonditionenVK(rDtoVorhanden,
						rDtoVorhanden.getKundeIId(), getInternalFrame());
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);

				if (dialog.bKonditionenUnterschiedlich == true) {
					dialog.setVisible(true);

					if (dialog.bAbgebrochen == false) {

						rechnungIIdNeu = DelegateFactory.getInstance().getRechnungDelegate().createRechnungAusRechnung(
								rechnungIId, getInternalFrameRechnung().getNeuDatum(), dialog.bKundeSelektiert,
								bZahlungsplanUebernehmen, getInternalFrame());

					}
				} else {
					rechnungIIdNeu = DelegateFactory.getInstance().getRechnungDelegate().createRechnungAusRechnung(
							rechnungIId, getInternalFrameRechnung().getNeuDatum(), false, bZahlungsplanUebernehmen,
							getInternalFrame());
				}
				if (rechnungIIdNeu != null) {

					getPanelQueryRechnung().clearDirektFilter();
					getPanelQueryRechnung().eventYouAreSelected(false);
					getPanelQueryRechnung().setSelectedId(rechnungIIdNeu);
					getPanelQueryRechnung().eventYouAreSelected(false);
					this.setSelectedComponent(getPanelDetailKopfdaten(true));
					pruefeChronologie(rechnungIIdNeu);
				}
			} else if (e.getSource() == panelQueryFLRAngebot) {
				Integer angebotIId = (Integer) panelQueryFLRAngebot.getSelectedId();

				Integer rechnungIIdNeu = DelegateFactory.getInstance().getRechnungDelegate().createRechnungAusAngebot(
						angebotIId, getInternalFrameRechnung().getNeuDatum(), getInternalFrame());

				if (rechnungIIdNeu != null) {
					getPanelQueryRechnung().clearDirektFilter();
					getPanelQueryRechnung().eventYouAreSelected(false);
					getPanelQueryRechnung().setSelectedId(rechnungIIdNeu);
					getPanelQueryRechnung().eventYouAreSelected(false);
					this.setSelectedComponent(getPanelDetailKopfdaten(true));
					pruefeChronologie(rechnungIIdNeu);
				}

			} else if (e.getSource() == panelQueryFLRLieferschein) {
				Integer rechnungIId = null;
				Object[] o = panelQueryFLRLieferschein.getSelectedIds();

				if (panelQueryFLRLieferschein.getDialog() != null) {
					panelQueryFLRLieferschein.getDialog().setVisible(false);
				}

				if (o != null) {
					if (o.length > 1) {
						rechnungIId = erstelleRechnungAusMehrereLieferscheine(o);
					} else {
						rechnungIId = erstelleRechnungAusLieferschein((Integer) o[0]);
					}
					// Panels wieder einschalten
					getInternalFrame().enableAllOberePanelsExceptMe(this, -1, true);
					if (rechnungIId != null) {
						// // Kopfdaten Panel noetigenfalls initialisieren,
						// damit
						// der key gesetzt wird
						// getPanelDetailKopfdaten(true);
						getPanelQueryRechnung().clearDirektFilter();
						getPanelQueryRechnung().eventYouAreSelected(false);
						getPanelQueryRechnung().setSelectedId(rechnungIId);
						getPanelQueryRechnung().eventYouAreSelected(false);
						this.setSelectedComponent(getPanelDetailKopfdaten(true));
					}
				}
			} else if (e.getSource() == panelQueryFLRAuftragauswahl) {
				Integer iIdAuftragBasis = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				if (iIdAuftragBasis != null) {

					// SP4131
					AuftragDto aDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(iIdAuftragBasis);
					if (aDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("rech.warnung.rechnungauswiederholendemauftrag"));

						if (b == false) {
							return;
						}
					}
					// PJ22373
					boolean schlussrechnung = false;

					if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
						ArrayList<RechnungDto> anzahlungen = DelegateFactory.getInstance().getRechnungDelegate()
								.sindAnzahlungenVorhanden(aDto.getIId());

						if (anzahlungen.size() > 0) {
							schlussrechnung = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									LPMain.getMessageTextRespectUISPr("rech.anzahlungvorhanden.schlussrechnung",
											aDto.getCNr()),
									LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.QUESTION_MESSAGE,
									JOptionPane.YES_OPTION);
						}
					}
					getPanelQueryRechnung().clearDirektFilter();

					Integer rechnungIId = DelegateFactory.getInstance().getRechnungDelegate().createRechnungAusAuftrag(
							iIdAuftragBasis, getInternalFrameRechnung().getNeuDatum(), schlussrechnung, getInternalFrame());

					getPanelQueryRechnung().eventYouAreSelected(false);
					holeRechnungDto(rechnungIId);
					getPanelQueryRechnung().setSelectedId(rechnungIId);
					setSelectedComponent(getPanelDetailKopfdaten(true));
					pruefeChronologie(rechnungIId);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryRechnung()) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_RECHNUNGEN, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_RECHNUNGEN, true);
					holeRechnungDto(key);
				}
				getPanelQueryRechnung().updateButtons();
			} else if (e.getSource() == getPanelQueryPositionen(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelDetailPositionenSelected(key);
			} else if (e.getSource() == getPanelQueryKontierung(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailKontierung(true).setKeyWhenDetailPanel(key);
				getPanelDetailKontierung(true).eventYouAreSelected(false);
				getPanelQueryKontierung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryZahlungen(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelDetailZahlung(true).setKeyWhenDetailPanel(key);
				getPanelDetailZahlung(true).eventYouAreSelected(false);
				getPanelQueryZahlungen(true).updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == getPanelDetailKopfdaten(true)) {
				getPanelDetailKopfdaten(true).allowAuftragLieferschein();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryRechnung()) {
				if (getPanelQueryRechnung().getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelDetailKopfdaten(true).eventActionNew(e, true, false);
				setSelectedComponent(getPanelDetailKopfdaten(true));
				getPanelDetailKopfdaten(true).allowAuftragLieferschein();
			} else if (e.getSource() == getPanelQueryPositionen(false)) {
				if (((InternalFrameRechnung) getInternalFrame()).isUpdateAllowedForRechnungDto(getRechnungDto())) {
					getPanelDetailPositionen(true).eventActionNew(e, true, false);
					getPanelDetailPositionen(true).eventYouAreSelected(false);
				}
			} else if (e.getSource() == getPanelQueryKontierung(false)) {
				getPanelDetailKontierung(true).eventActionNew(e, true, false);
				getPanelDetailKontierung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelQueryZahlungen(false)) {
				getPanelDetailZahlung(true).eventActionNew(e, true, false);
				getPanelDetailZahlung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelDetailPositionen(false)) {
				getPanelSplitPositionen(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailKontierung(false)) {
				getPanelSplitKontierung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailZahlung(false)) {
				getPanelSplitZahlungen(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource() == getPanelDetailPositionen(false)) {
				print();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelDetailPositionen(false)) {
				Object key = getPanelDetailPositionen(true).getKeyWhenDetailPanel();
				getPanelQueryPositionen(true).eventYouAreSelected(false);
				getPanelQueryPositionen(true).setSelectedId(key);
				getPanelQueryPositionen(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailKontierung(false)) {
				Object key = getPanelDetailKontierung(true).getKeyWhenDetailPanel();
				getPanelQueryKontierung(true).eventYouAreSelected(false);
				getPanelQueryKontierung(true).setSelectedId(key);
				getPanelQueryKontierung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailKopfdaten(false)) {
				// cleardirektfilter: hier leeren
				getPanelQueryRechnung().clearDirektFilter();
				Object key = getPanelDetailKopfdaten(true).getKeyWhenDetailPanel();
				getPanelQueryRechnung().eventYouAreSelected(false);
				getPanelQueryRechnung().setSelectedId(key);
				getPanelQueryRechnung().eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailZahlung(false)) {
				Object key = getPanelDetailZahlung(true).getKeyWhenDetailPanel();
				getPanelQueryZahlungen(true).eventYouAreSelected(false);
				getPanelQueryZahlungen(true).setSelectedId(key);
				getPanelQueryZahlungen(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailKopfdaten(false)) {
				setKeyWasForLockMe();
				this.setSelectedComponent(getPanelQueryRechnung());
			} else if (e.getSource() == getPanelDetailPositionen(false)) {
				setKeyWasForLockMe();
				Object oNaechster = getPanelQueryPositionen(true).getId2SelectAfterDelete();
				getPanelQueryPositionen(true).setSelectedId(oNaechster);
				getPanelSplitPositionen(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailKontierung(false)) {
				setKeyWasForLockMe();
				getPanelSplitKontierung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelDetailZahlung(false)) {
				setKeyWasForLockMe();
				getPanelSplitZahlungen(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == getPanelQueryPositionen(false)) {
				int iPos = getPanelQueryPositionen(true).getTable().getSelectedRow();
				// wenn die Position nicht die erste ist
				if (iPos > 0) {

					Integer iIdPosition = (Integer) getPanelQueryPositionen(true).getSelectedId();

					TableModel tm = getPanelQueryPositionen(true).getTable().getModel();

					DelegateFactory.getInstance().getRechnungDelegate().vertauscheRechnungspositionMinus(iPos, tm);

					// Integer iIdPositionMinus1 = (Integer)
					// getPanelQueryPositionen(
					// true).getTable().getValueAt(iPos - 1, 0);
					// DelegateFactory
					// .getInstance()
					// .getRechnungDelegate()
					// .vertauscheRechnungspositionen(iIdPosition,
					// iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					getPanelQueryPositionen(true).setSelectedId(iIdPosition);
					updateISortButtons();
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == getPanelQueryPositionen(false)) {
				int iPos = getPanelQueryPositionen(true).getTable().getSelectedRow();
				// wenn die Position nicht die letzte ist
				if (iPos < getPanelQueryPositionen(true).getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) getPanelQueryPositionen(true).getSelectedId();
					TableModel tm = getPanelQueryPositionen(true).getTable().getModel();
					DelegateFactory.getInstance().getRechnungDelegate().vertauscheRechnungspositionPlus(iPos, tm);
					// DelegateFactory.getInstance().getRechnungDelegate()
					// .vertauscheRechnungspositionen(iIdPosition,
					// iIdPositionPlus1);
					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					getPanelQueryPositionen(true).setSelectedId(iIdPosition);
					updateISortButtons();
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == getPanelQueryPositionen(false)) {
				if (getInternalFrameRechnung().isUpdateAllowedForRechnungDto(getRechnungDto())) {

					if (getRechnungPositionDto() != null) {

						getPanelDetailPositionen(true)
								.setArtikeSetIIdForNewPosition(getRechnungPositionDto().getPositioniIdArtikelset());

						getPanelDetailPositionen(true).eventActionNew(e, true, false);
						getPanelDetailPositionen(true).eventYouAreSelected(false); // Buttons schalten

						// if
						// (getRechnungPositionDto().getPositioniIdArtikelset()
						// == null) {
						//
						// getPanelDetailPositionen(true).eventActionNew(e,
						// true, false);
						// getPanelDetailPositionen(true).eventYouAreSelected(
						// false); // Buttons schalten
						// } else {
						// DialogFactory
						// .showModalDialog(
						// LPMain
						// .getTextRespectUISPr("lp.error"),
						// LPMain
						// .getTextRespectUISPr("lp.error.artikel.kannnichtzwischenseteingefuegtwerden"));
						// return;
						// }

					}

				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN && e.getSource() == getPanelQueryPositionen(false)) {
			copyHV();
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == getPanelQueryRechnung()) {
				if (sAspectInfo.equals(MY_OWN_NEW_AUS_AUFTRAG)) {
					// der Benutzer muss einen Auftrag auswaehlen
					dialogQueryAuftragFromListe();
				} else if (sAspectInfo.equals(MY_OWN_NEW_AUS_LIEFERSCHEIN)) {
					// der Benutzer muss einen Lieferschein auswaehlen
					dialogQueryLieferschein();
				} else if (sAspectInfo.equals(MY_OWN_NEW_AUS_RECHNUNG)) {
					// der Benutzer muss einen Lieferschein auswaehlen
					dialogQueryRechnung();
				} else if (sAspectInfo.equals(MY_OWN_NEW_AUS_ANGEBOT)) {
					// der Benutzer muss einen Lieferschein auswaehlen
					dialogQueryAngebot();
				} else if (sAspectInfo.equals(MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK)) {
					Integer rechnungIId = null;
					Object[] o = panelQueryFLRLieferschein.getSelectedIds();
					if (o.length > 1) {
						rechnungIId = erstelleRechnungAusMehrereLieferscheine(o);
					} else {
						rechnungIId = erstelleRechnungAusLieferschein((Integer) o[0]);
					}
					getInternalFrame().enableAllOberePanelsExceptMe(this, -1, true);
					if (rechnungIId != null) {
						getPanelQueryRechnung().eventYouAreSelected(false);
						getPanelQueryRechnung().setSelectedId(rechnungIId);
						getPanelQueryRechnung().eventYouAreSelected(false);
						this.setSelectedComponent(getPanelDetailKopfdaten(true));
					}
				}
			} else if (e.getSource() == panelQueryPositionen) {
				if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
					if (getInternalFrameRechnung().isUpdateAllowedForRechnungDto(getRechnungDto())) {
						// copypaste
						einfuegenHV();
					}
				} else if (sAspectInfo.equals(BUTTON_SORTIERE_LS_NACH_ANSPRECHPARTNER)) {

					// Vorher fragen

					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("re.sortierenach.frage"));

					if (b == true) {
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
								.getParameterDelegate().getParametermandant(
										ParameterFac.PARAMETER_LIEFERSCHEIN_UEBERNAHME_NACH_ANSPRECHPARTNER,
										ParameterFac.KATEGORIE_RECHNUNG, LPMain.getTheClient().getMandant());

						if ((Boolean) parameter.getCWertAsObject()) {

							DelegateFactory.getInstance().getRechnungDelegate()
									.sortiereNachLieferscheinAnsprechpartner(getRechnungDto().getIId());
							getPanelQueryPositionen(true).eventYouAreSelected(false);
						} else {
							DelegateFactory.getInstance().getRechnungDelegate()
									.sortiereNachLieferscheinNummer(getRechnungDto().getIId());
							getPanelQueryPositionen(true).eventYouAreSelected(false);
						}
					}
				} else if (sAspectInfo.equals(BUTTON_SORTIERE_RE_NACH_AUFTRAGSNUMMER)) {

					// Vorher fragen
					if (getInternalFrameRechnung().isUpdateAllowedForRechnungDto(rechnungDto)) {

						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("re.sortierenach.frage"));

						if (b == true) {

							DelegateFactory.getInstance().getRechnungDelegate()
									.sortiereNachAuftragsnummer(getRechnungDto().getIId());

							getPanelQueryPositionen(true).eventYouAreSelected(false);

						}
					}
				} else if (sAspectInfo.equals(BUTTON_SORTIERE_NACH_ARTIKELNUMMER)) {

					// Vorher fragen
					if (getInternalFrameRechnung().isUpdateAllowedForRechnungDto(rechnungDto)) {

						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("lp.sortierenach.frage"));

						if (b == true) {

							DelegateFactory.getInstance().getRechnungDelegate()
									.sortiereNachArtikelnummer(getRechnungDto().getIId());

							getPanelQueryPositionen(true).eventYouAreSelected(false);

						}
					}
				} else if (sAspectInfo.equals(BUTTON_SCHNELLERFASSUNG_POSITIONEN)) {

					// Vorher fragen
					if (getInternalFrameRechnung().isUpdateAllowedForRechnungDto(rechnungDto)) {

						DialogPositionenSchnellerfassung d = new DialogPositionenSchnellerfassung(panelSplitPositionen,
								getRechnungDto());
						LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);

						getPanelQueryPositionen(true).eventYouAreSelected(false);

					}

				} else {

					if (getInternalFrameRechnung().isUpdateAllowedForRechnungDto(rechnungDto)) {
						if (sAspectInfo.equals(MY_OWN_NEW_SNRBARCODEERFASSUNG)) {
							DialogPositionenBarcodeerfassung d = new DialogPositionenBarcodeerfassung(
									getRechnungDto().getLagerIId(), getInternalFrame());
							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

							d.setVisible(true);
							List<SeriennrChargennrMitMengeDto> alSeriennummern = d.alSeriennummern;

							if (alSeriennummern.size() > 0 && d.artikelIId != null) {
								ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(d.artikelIId);
								RechnungPositionDto rePos = new RechnungPositionDto();
								rePos.setBelegIId(getRechnungDto().getIId());
								rePos.setArtikelIId(d.artikelIId);
								rePos.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);

								rePos.setSeriennrChargennrMitMenge(alSeriennummern);

								rePos.setBNettopreisuebersteuert(Helper.boolean2Short(false));

								KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
										.kundeFindByPrimaryKey(getRechnungDto().getKundeIId());

								Timestamp belegDatum = HelperTimestamp.belegDatum(rechnungDto);
								MwstsatzDto mwstsatzDtoAktuell = DelegateFactory.mandant()
										.mwstsatzFindZuDatum(kundeDto.getMwstsatzbezIId(), belegDatum);
								/*
								 * MwstsatzDto mwstsatzDtoAktuell =
								 * DelegateFactory.getInstance().getMandantDelegate()
								 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId());
								 */
								VkpreisfindungDto vkpreisfindungDto = DelegateFactory.getInstance()
										.getVkPreisfindungDelegate().verkaufspreisfindung(artikelDto.getIId(),
												kundeDto.getIId(), new BigDecimal(alSeriennummern.size()),
//												new java.sql.Date(System.currentTimeMillis()),
												new java.sql.Date(belegDatum.getTime()),
												kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
												mwstsatzDtoAktuell.getIId(), getRechnungDto().getWaehrungCNr());

								VerkaufspreisDto preisDtoInMandantenwaehrung = null;
								try {
									preisDtoInMandantenwaehrung = Helper.getVkpreisBerechnet(vkpreisfindungDto);
								} catch (Throwable t) {
									t.printStackTrace();
								}

								VerkaufspreisDto verkaufspreisDtoInZielwaehrung = null;
								if (preisDtoInMandantenwaehrung != null) {
									if (preisDtoInMandantenwaehrung.waehrungCNr != null
											&& preisDtoInMandantenwaehrung.waehrungCNr
													.equals(getRechnungDto().getWaehrungCNr())) {
										verkaufspreisDtoInZielwaehrung = preisDtoInMandantenwaehrung;
										// TODO: Runden auf wieviele Stellen
										verkaufspreisDtoInZielwaehrung.einzelpreis = Helper
												.rundeKaufmaennisch(verkaufspreisDtoInZielwaehrung.einzelpreis
														.multiply(verkaufspreisDtoInZielwaehrung.tempKurs), 4);
										verkaufspreisDtoInZielwaehrung.rabattsumme = verkaufspreisDtoInZielwaehrung.einzelpreis
												.subtract(verkaufspreisDtoInZielwaehrung.nettopreis);
										// TODO: Rabattsatz hat wieviele Stellen
										// ?
										verkaufspreisDtoInZielwaehrung.rabattsatz = new Double(
												Helper.getProzentsatzBD(verkaufspreisDtoInZielwaehrung.einzelpreis,
														verkaufspreisDtoInZielwaehrung.rabattsumme, 4).doubleValue());

									} else {
										verkaufspreisDtoInZielwaehrung = DelegateFactory.getInstance()
												.getVkPreisfindungDelegate()
												.getPreisdetailsInFremdwaehrung(preisDtoInMandantenwaehrung,
														new BigDecimal(getRechnungDto()
																.getFWechselkursmandantwaehrungzubelegwaehrung()
																.doubleValue()));
									}
								}
								rePos.setMwstsatzIId(mwstsatzDtoAktuell.getIId());

								if (verkaufspreisDtoInZielwaehrung != null) {
									rePos.setNEinzelpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
									rePos.setNNettoeinzelpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
									rePos.setNBruttoeinzelpreis(verkaufspreisDtoInZielwaehrung.bruttopreis);
									rePos.setMwstsatzIId(verkaufspreisDtoInZielwaehrung.mwstsatzIId);
								} else {
									rePos.setNEinzelpreis(new BigDecimal(0));
									rePos.setNNettoeinzelpreis(new BigDecimal(0));
									rePos.setNBruttoeinzelpreis(new BigDecimal(0));
								}

								rePos.setFRabattsatz(0D);
								rePos.setFZusatzrabattsatz(0D);
								rePos.setNMenge(new BigDecimal(alSeriennummern.size()));
								rePos.setEinheitCNr(artikelDto.getEinheitCNr());

								DelegateFactory.getInstance().getRechnungDelegate().createRechnungposition(rePos,
										getRechnungDto().getLagerIId());

							}
							panelQueryPositionen.eventYouAreSelected(false);

						}
					}
				}
			}

		}
	}

	/**
	 * Laden der Info der neu selektierten Position in das PanelDetailPositionen
	 * 
	 * @param key , selektierte Positions-Id
	 * @throws Throwable
	 */
	protected void panelDetailPositionenSelected(Object key) throws Throwable {
		getPanelDetailPositionen(true).setKeyWhenDetailPanel(key);
		getPanelDetailPositionen(true).eventYouAreSelected(false);
		getPanelQueryPositionen(true).updateButtons();
		updateISortButtons();
	}

	private void updateISortButtons() throws Throwable {
		PanelQuery positionen = getPanelQueryPositionen(true);
		if (positionen.getTable().getSelectedRow() == 0)
			positionen.enableToolsPanelButtons(false, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
		else if (positionen.getTable().getSelectedRow() == positionen.getTable().getRowCount() - 1)
			positionen.enableToolsPanelButtons(false, PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
	}

	protected Integer getSelectedIIdRechnung() throws Throwable {
		return (Integer) getPanelQueryRechnung().getSelectedId();
	}

	/**
	 * Eine ausgewaehlte Rechnung holen und die Panels aktualisieren
	 * 
	 * @param key Object
	 * @throws Throwable
	 */
	protected void holeRechnungDto(Object key) throws Throwable {
		if (key != null) {
			RechnungDto reDto = DelegateFactory.getInstance().getRechnungDelegate()
					.rechnungFindByPrimaryKey((Integer) key);
			setRechnungDto(reDto);
			setKeyInternalFrame(key);
		}
	}

	protected void setKeyInternalFrame(Object key) throws Throwable {
		getInternalFrameRechnung().setKeyWasForLockMe(key.toString());
		if (getPanelDetailKopfdaten(false) != null) {
			getPanelDetailKopfdaten(true).setKeyWhenDetailPanel(key);
		}
	}

	public void reloadRechnungDto() throws Throwable {
		if (getRechnungDto() != null) {
			holeRechnungDto(getRechnungDto().getIId());
			if (panelDetailPositionen != null) {
				panelDetailPositionen.setModified(false);
				panelDetailPositionen.eventYouAreSelected(false);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int index = this.getSelectedIndex();

		if (index == IDX_RECHNUNGEN) {

			getPanelQueryRechnung().eventYouAreSelected(false);
		} else if (index == IDX_KOPFDATEN) {

			if (getRechnungDto() != null) {
				getPanelDetailKopfdaten(true).setKeyWhenDetailPanel(getRechnungDto().getIId());
			}
			getPanelDetailKopfdaten(true).eventYouAreSelected(false);
		} else if (index == IDX_POSITIONEN) {
			getPanelSplitPositionen(true).eventYouAreSelected(false);
			// wenn ich aus dem umsatz komme, verlier ich den titel
			this.setRechnungDto(this.getRechnungDto());
		} else if (index == iDX_KONTIERUNG) {

			getPanelSplitKontierung(true).eventYouAreSelected(false);
			// wenn ich aus dem umsatz komme, verlier ich den titel
			this.setRechnungDto(this.getRechnungDto());

		}
	}

	private InternalFrameRechnung getInternalFrameRechnung() {
		return (InternalFrameRechnung) getInternalFrame();
	}

	private void refreshFilterPositionen() throws Throwable {
		if (getRechnungDto() != null) {
			FilterKriterium[] krit = RechnungFilterFactory.getInstance()
					.createFKRechnungpositionenQuery(getRechnungDto().getIId());
			getPanelQueryPositionen(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterZahlungen() throws Throwable {
		if (getRechnungDto() != null) {
			FilterKriterium[] krit = RechnungFilterFactory.getInstance().createFKZahlungen(getRechnungDto().getIId());
			getPanelQueryZahlungen(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterKontierung() throws Throwable {
		if (getRechnungDto() != null) {
			FilterKriterium[] krit = RechnungFilterFactory.getInstance()
					.createFKRechnungkontierung(getRechnungDto().getIId());
			getPanelQueryKontierung(true).setDefaultFilter(krit);
		}
	}

	protected final PanelQuery getPanelQueryPositionen(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryPositionen == null && bNeedInstantiationIfNull) {
			QueryType[] qtPositionen = RechnungFilterFactory.getInstance().createQTRechnungpositionen();
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					// mehrfachselekt: die actions dazu
					PanelBasis.ACTION_KOPIEREN, PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			// int iUsecase = QueryParameters.UC_ID_RECHNUNGPOSITION;
			int iUsecase = 0;
			if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				iUsecase = QueryParameters.UC_ID_RECHNUNGPOSITION;
			} else if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				iUsecase = QueryParameters.UC_ID_GUTSCHRIFTPOSITION;
			} else {
				iUsecase = QueryParameters.UC_ID_PROFORMARECHNUNGPOSITION;
			}
			panelQueryPositionen = new PanelQuery(qtPositionen, null,
					// QueryParameters.UC_ID_GUTSCHRIFTPOSITION,
					iUsecase, aWhichButtonIUsePositionen, getInternalFrame(), "", true);

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_LIEFERSCHEIN_UEBERNAHME_NACH_ANSPRECHPARTNER,
							ParameterFac.KATEGORIE_RECHNUNG, LPMain.getTheClient().getMandant());

			if ((Boolean) parameter.getCWertAsObject()) {
				panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/navigate_close.png",
						LPMain.getTextRespectUISPr("re.sortierenachlsansprechpartner"),
						BUTTON_SORTIERE_LS_NACH_ANSPRECHPARTNER, null);
			} else {
				panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/navigate_close.png",
						LPMain.getTextRespectUISPr("re.sortierenachlsnummer"), BUTTON_SORTIERE_LS_NACH_ANSPRECHPARTNER,
						null);
			}

			panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/scanner16x16.png",
					LPMain.getTextRespectUISPr("auftrag.positionen.schnelleingabe"), BUTTON_SCHNELLERFASSUNG_POSITIONEN,
					RechteFac.RECHT_RECH_RECHNUNG_CUD);

			if (bAuftragRechnung) {

				panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/down_plus.png",
						LPMain.getTextRespectUISPr("re.sortierenachabnummer"), BUTTON_SORTIERE_RE_NACH_AUFTRAGSNUMMER,
						null);
			}

			panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/sort_az_descending.png",
					LPMain.getTextRespectUISPr("ls.sortierenachartikelnummer"), BUTTON_SORTIERE_NACH_ARTIKELNUMMER,
					null);

			// mehrfachselekt: fuer dieses QP aktivieren
			panelQueryPositionen.setMultipleRowSelectionEnabled(true);

			parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_SERIENNUMMER_EINEINDEUTIG);

			if ((Boolean) parameter.getCWertAsObject()) {
				panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/laserpointer.png",
						LPMain.getTextRespectUISPr("ls.positionen.barcodeerfassung"), MY_OWN_NEW_SNRBARCODEERFASSUNG,
						RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
			}

		}
		return panelQueryPositionen;
	}

	private PanelQuery getPanelQueryKontierung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryKontierung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };

			panelQueryKontierung = new PanelQuery(null, null, QueryParameters.UC_ID_RECHNUNGKONTIERUNG,
					aWhichButtonIUsePositionen, getInternalFrame(), "", true);
		}
		return panelQueryKontierung;
	}

	protected PanelRechnungKopfdaten getPanelDetailKopfdaten(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailKopfdaten == null && bNeedInstantiationIfNull) {
			panelDetailKopfdaten = new PanelRechnungKopfdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kopfdaten"), null, this);
			this.setComponentAt(IDX_KOPFDATEN, panelDetailKopfdaten);
		}
		return panelDetailKopfdaten;
	}

	protected final PanelRechnungPosition getPanelDetailPositionen(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailPositionen == null && bNeedInstantiationIfNull) {
			int typ;
			if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				typ = PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUFSNR;
			} else if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				typ = PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELGUTSCHRIFT;
			} else {
				typ = PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUFSNR;
			}

			panelDetailPositionen = new PanelRechnungPosition(getInternalFrame(),
					LPMain.getTextRespectUISPr("rechnung.tab.oben.positionen.title"), null, // leer
					this, typ);
		}
		return panelDetailPositionen;
	}

	private PanelRechnungKontierung getPanelDetailKontierung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailKontierung == null && bNeedInstantiationIfNull) {
			panelDetailKontierung = new PanelRechnungKontierung(getInternalFrame(),
					LPMain.getTextRespectUISPr("rechnung.tab.oben.kontierung.title"), null, // leer
					this);
		}
		return panelDetailKontierung;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK)) {
			// int z=0;
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_DATEI_DRUCKEN)) {
			print();
		} else if (e.getActionCommand().equals(MENUE_ACTION_DATEI_ZAHLSCHEIN_DRUCKEN)) {
			printZahlschein();
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_VERTRETER_AENDERN)) {
			dialogQueryVertreterFromListe();
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_STATISTIKADRESSE_AENDERN)) {
			dialogQueryStatistikadresseFromListe();
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (!getPanelDetailKopfdaten(true).isLockedDlg()) {
				reloadRechnungDto();
				if (getRechnungDto() != null) {
					if (getRechnungDto().getStatusCNr().equals(RechnungFac.STATUS_VERBUCHT)
							|| getRechnungDto().getStatusCNr().equals(RechnungFac.STATUS_OFFEN)
							|| getRechnungDto().getStatusCNr().equals(RechnungFac.STATUS_TEILBEZAHLT)) {
						if (getRechnungDto().getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
							if (DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									"Gutschriftsstatus auf erledigt setzen?")) {
								DelegateFactory.getInstance().getRechnungDelegate()
										.manuellErledigen(getRechnungDto().getIId());
								// Panel aktualisieren
								super.lPEventObjectChanged(null);
							}

						} else {

							int indexJa = 0;
							int indexNein = 1;
							int indexMehrere = 2;
							int iAnzahlOptionen = 3;

							Object[] aOptionen = new Object[iAnzahlOptionen];
							aOptionen[indexJa] = LPMain.getTextRespectUISPr("lp.ja");
							aOptionen[indexNein] = LPMain.getTextRespectUISPr("lp.nein");
							aOptionen[indexMehrere] = LPMain.getTextRespectUISPr("rech.erledigen.frage.mehrere");

							int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
									LPMain.getTextRespectUISPr("rech.erledigen.frage"),
									LPMain.getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[1]);

							if (iAuswahl == indexJa) {
								DelegateFactory.getInstance().getRechnungDelegate()
										.manuellErledigen(getRechnungDto().getIId());
								// Panel aktualisieren
								getPanelQueryRechnung().eventYouAreSelected(false);
							}
							if (iAuswahl == indexMehrere) {
								panelQueryFLRRechnungZuErledigen = RechnungFilterFactory.getInstance()
										.createPanelFLRRechnungOffen(getInternalFrame(), true, false,
												getRechnungDto().getIId());
								panelQueryFLRRechnungZuErledigen.setMultipleRowSelectionEnabled(true);
								panelQueryFLRRechnungZuErledigen.addButtonAuswahlBestaetigen(null);
								new DialogQuery(panelQueryFLRRechnungZuErledigen);
							}

						}
					} else if (getRechnungDto().getStatusCNr().equals(RechnungFac.STATUS_BEZAHLT)) {
						if (DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("er.hint.erledigtstatuszuruecknehmen"))) {
							DelegateFactory.getInstance().getRechnungDelegate()
									.manuellErledigen(getRechnungDto().getIId());
							// Panel aktualisieren
							super.lPEventObjectChanged(null);
						}
					} else {
						if (getRechnungDto().getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									"Gutschrift kann nicht manuell erledigt werden");

						} else {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									"Rechnung kann nicht manuell erledigt werden");
						}
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), "Kein Beleg ausgew\u00E4hlt");
				}
			}
		}
	}

	protected abstract void print() throws Throwable;

	protected abstract void printZahlschein() throws Throwable;

	protected Integer getSelectedIdPositionen() throws Throwable {
		return (Integer) getPanelQueryPositionen(true).getSelectedId();
	}

	/**
	 * Diese Methode setzt des aktuellen Auftrag aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryRechnung().getSelectedId();

		if (oKey != null) {
			holeRechnungDto(oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected boolean pruefeKonditionen(RechnungDto reDto) throws Throwable {
		boolean bErfasst = true;
		// konditionenbest: parameter holen und dann ein if rund um das was
		// schon da war
		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_RECHNUNG,
				ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN);
		Short sValue = new Short(parameter.getCWert());
		if (Helper.short2boolean(sValue)) {
			if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				if (reDto.getCFusstextuebersteuert() != null && reDto.getCKopftextuebersteuert() != null) {
					// passt
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("lp.hint.konditionenerfassen") + "\n" + reDto.getCNr());
					bErfasst = false;
				}
			}
		}
		return bErfasst;
	}

	protected final void gotoAuswahl() {
		setSelectedIndex(IDX_RECHNUNGEN);
	}

	protected final void gotoPositionen() {
		setSelectedIndex(IDX_POSITIONEN);
	}

	public PanelQuery getPanelAuswahl() throws Throwable {
		this.setSelectedIndex(IDX_RECHNUNGEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return getPanelQueryRechnung();
	}

	public PanelRechnungKopfdaten getPanelKopfdaten() throws Throwable {
		this.setSelectedIndex(IDX_KOPFDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelDetailKopfdaten;
	}

	public PanelSplit getPanelPositionen() {
		this.setSelectedIndex(IDX_POSITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return panelSplitPositionen;
	}

	public Object getDto() {
		return rechnungDto;
	}

	/**
	 * @deprecated verwende
	 *             {@link InternalFrameRechnung.isUpdateAllowedForRechnungDto(
	 *             RechnungDto rechnungDto)}
	 *             InternalFrameRechnung.isUpdateAllowedForRechnungDto )
	 * @return boolean
	 */
	public boolean pruefeAktuelleRechnung() {
		boolean bIstGueltig = true;

		if (getRechnungDto() == null || getRechnungDto().getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("ls.warning.keinlieferschein"));
		}

		return bIstGueltig;
	}

	public void dialogQueryAuftragFromListe() throws Throwable {
		FilterKriterium[] fk = LieferscheinFilterFactory.getInstance().createFKPanelQueryFLRAuftragAuswahl();
		panelQueryFLRAuftragauswahl = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), true,
				false, fk);
		new DialogQuery(panelQueryFLRAuftragauswahl);
	}

	public void dialogQueryVertreterFromListe() throws Throwable {

		panelQueryFLRVertreter = PersonalFilterFactory.getInstance().createPanelFLRPersonal(getInternalFrame(), true,
				false);
		new DialogQuery(panelQueryFLRVertreter);
	}

	public void dialogQueryStatistikadresseFromListe() throws Throwable {

		panelQueryFLRStatistikadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true,
				false);
		if (getRechnungDto() != null && getRechnungDto().getKundeIIdStatistikadresse() != null) {
			panelQueryFLRStatistikadresse.setSelectedId(getRechnungDto().getKundeIIdStatistikadresse());
		}
		new DialogQuery(panelQueryFLRStatistikadresse);
	}

	private void dialogQueryLieferschein() throws Throwable {
		FilterKriterium[] fk = LieferscheinFilterFactory.getInstance().createFKGelieferteLieferscheine();
		String sTitle = LPMain.getTextRespectUISPr("ls.print.listenichtverrechnet");
		panelQueryFLRLieferschein = LieferscheinFilterFactory.getInstance()
				.createPanelQueryFLRLieferschein(getInternalFrame(), fk, sTitle, null);
		panelQueryFLRLieferschein.setMultipleRowSelectionEnabled(true);

		panelQueryFLRLieferschein.addButtonAuswahlBestaetigen(RechteFac.RECHT_RECH_RECHNUNG_CUD);

		new DialogQuery(panelQueryFLRLieferschein);
	}

	private void dialogQueryRechnung() throws Throwable {
		panelQueryFLRRechnung = RechnungFilterFactory.getInstance().createPanelFLRRechnung(getInternalFrame(), null);
		panelQueryFLRRechnung.setMultipleRowSelectionEnabled(false);
		/*
		 * panelQueryFLRLieferschein.createAndSaveAndShowButton(
		 * "/com/lp/client/res/check2.png",
		 * LPMain.getTextRespectUISPr("lp.tooltip.kriterienuebernehmen"),
		 * MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK , RechteFac.RECHT_RECH_RECHNUNG_CUD);
		 */
		new DialogQuery(panelQueryFLRRechnung);
	}

	private void dialogQueryAngebot() throws Throwable {
		panelQueryFLRAngebot = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebotErledigteVersteckt(getInternalFrame(), true, false);
		panelQueryFLRAngebot.setMultipleRowSelectionEnabled(false);

		new DialogQuery(panelQueryFLRAngebot);
	}

	/**
	 * erstelle Rechnung aus Lieferschein.
	 * 
	 * @param key Object
	 * @throws Throwable
	 * @return Integer
	 */
	private Integer erstelleRechnungAusLieferschein(Integer key) throws Throwable {
		Integer rechnungIId = null;
		if (key != null) {
			LieferscheinDto ls = DelegateFactory.getInstance().getLsDelegate().lieferscheinFindByPrimaryKey(key);

			String text = LPMain.getTextRespectUISPr("rech.rechnungauslieferschein");
			if (this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)) {
				text = LPMain.getTextRespectUISPr("rech.proformarechnungauslieferschein");
			}

			boolean bAnswer = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					text + " " + ls.getCNr() + " " + LPMain.getTextRespectUISPr("rech.erstellen"),
					LPMain.getTextRespectUISPr("lp.frage"));

			// PJ22373
			boolean schlussrechnung = false;

			if (ls.getAuftragIId() != null && this.rechnungsTyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				ArrayList<RechnungDto> anzahlungen = DelegateFactory.getInstance().getRechnungDelegate()
						.sindAnzahlungenVorhanden(ls.getAuftragIId());

				if (anzahlungen.size() > 0) {

					AuftragDto abDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(ls.getAuftragIId());
					schlussrechnung = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getMessageTextRespectUISPr("rech.anzahlungvorhanden.schlussrechnung",
									abDto.getCNr()),
							LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.QUESTION_MESSAGE,
							JOptionPane.YES_OPTION);
				}
			}

			if (bAnswer) {

				KundeDto kundeDto = DelegateFactory.getInstance().getRechnungDelegate()
						.getKundeFuerRechnungAusLieferschein(ls.getIId(), this.rechnungsTyp);
				if (Helper.short2boolean(kundeDto.getBVersteckterlieferant()) == true) {

					bAnswer = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("rech.ls.uebernehmen.kundeistversteckterlieferant"),
							LPMain.getTextRespectUISPr("lp.frage"));

				}

				if (bAnswer == true) {
					if (Helper.short2boolean(ls.getBVerrechenbar()) == false) {

						bAnswer = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("rech.ls.nichtverrechenbar.trotzdem"),
								LPMain.getTextRespectUISPr("lp.frage"));

					}

					if (bAnswer == true) {

						try {

							RechnungDto rechnungDtoNeu = new RechnungDto();
							if (schlussrechnung) {
								rechnungDtoNeu.setRechnungartCNr(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG);
							}

							rechnungIId = DelegateFactory.getInstance().getRechnungDelegate()
									.createRechnungAusLieferschein((Integer) key, rechnungDtoNeu, this.rechnungsTyp,
											getInternalFrameRechnung().getNeuDatum(), getInternalFrame());

							pruefeChronologie(rechnungIId);
						} catch (Throwable ex) {
							// Liste neu Laden, wegen Panelsichtbarkeit
							getPanelAuswahl().eventYouAreSelected(false);
							// weiter werfen, wird zentral abgehandelt
							throw ex;
						}
					}

				}

			}
		}
		return rechnungIId;
	}

	private Integer erstelleRechnungAusMehrereLieferscheine(Object[] key) throws Throwable {
		// gleiche rechnungsadresse
		Integer rechnungIId = null;
		if (key != null) {
			String CNrs = DelegateFactory.getInstance().getLsDelegate().getLieferscheinCNr(key);
			boolean bAnswer = DialogFactory
					.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("rech.rechnungauslieferschein") + " " + CNrs + " "
									+ LPMain.getTextRespectUISPr("rech.erstellen"),
							LPMain.getTextRespectUISPr("lp.frage"));

			if (bAnswer == true) {

				LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey((Integer) key[key.length - 1]);
				KundeDto kundeDto = DelegateFactory.getInstance().getRechnungDelegate()
						.getKundeFuerRechnungAusLieferschein(lsDto.getIId(), this.rechnungsTyp);

				if (Helper.short2boolean(kundeDto.getBVersteckterlieferant()) == true) {

					bAnswer = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("rech.ls.uebernehmen.kundeistversteckterlieferant"),
							LPMain.getTextRespectUISPr("lp.frage"));
				}

			}

			if (bAnswer) {

				try {
					panelQueryFLRLieferschein.getDialog().dispose();
					rechnungIId = DelegateFactory.getInstance().getRechnungDelegate()
							.createRechnungAusMehrereLieferscheine(key, new RechnungDto(), this.rechnungsTyp,
									getInternalFrameRechnung().getNeuDatum(), getInternalFrame());
					pruefeChronologie(rechnungIId);
				} catch (Throwable ex) {
					// Liste neu Laden, wegen Panelsichtbarkeit
					getPanelAuswahl().eventYouAreSelected(false);
					// weiter werfen, wird zentral abgehandelt
					throw ex;
				}
			}

		}
		return rechnungIId;
	}

	public void pruefeChronologie(Integer rechnungIId) throws Throwable {
		// PJ20588 Chronologie pruefen
		RechnungDto[] rechnungDtoVorhanden = DelegateFactory.getInstance().getRechnungDelegate()
				.pruefeObChronologieDesBlegdatumsDerRechnungStimmt(rechnungIId);

		RechnungDto rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
				.rechnungFindByPrimaryKey(rechnungIId);

		RechnungartDto rechnungartDto = DelegateFactory.getInstance().getRechnungServiceDelegate()
				.rechnungartFindByPrimaryKey(rechnungDto.getRechnungartCNr());
		String rechnungtyp = rechnungartDto.getRechnungtypCNr();

		if (rechnungDtoVorhanden[0] != null) {

			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), LPMain.getMessageTextRespectUISPr(
					"rech.hinweis.chronologie.belegdatum", rechnungtyp, rechnungDto.getCNr(),
					rechnungDtoVorhanden[0].getCNr(),
					Helper.formatDatum(rechnungDtoVorhanden[0].getTBelegdatum(), LPMain.getTheClient().getLocUi())));
		}
		if (rechnungDtoVorhanden[1] != null) {

			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), LPMain.getMessageTextRespectUISPr(
					"rech.hinweis.chronologie.belegdatum2", rechnungtyp, rechnungDto.getCNr(),
					rechnungDtoVorhanden[1].getCNr(),
					Helper.formatDatum(rechnungDtoVorhanden[1].getTBelegdatum(), LPMain.getTheClient().getLocUi())));
		}
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object aoIIdPosition[] = this.panelQueryPositionen.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			RechnungPositionDto[] dtos = new RechnungPositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungPositionFindByPrimaryKey((Integer) aoIIdPosition[i]);
			}

			if (getPanelDetailPositionen(true).getArtikelsetViewController().validateCopyConstraintsUI(dtos)) {
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws Throwable {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		if (!getPanelDetailPositionen(true).getArtikelsetViewController().validatePasteConstraintsUI(o)) {
			return;
		}

		if (o instanceof BelegpositionDto[]) {
			RechnungPositionDto[] positionDtos = DelegateFactory.getInstance().getBelegpostionkonvertierungDelegate()
					.konvertiereNachRechnungpositionDto((BelegpositionDto[]) o, getRechnungDto().getKundeIId(),
							getRechnungDto().getTBelegdatum());

			Integer iId = null;
			if (positionDtos != null) {

				Boolean b = positionAmEndeEinfuegen();
				if (b != null) {

					for (int i = 0; i < positionDtos.length; i++) {
						RechnungPositionDto positionDto = positionDtos[i];

						if (getRechnungDto().getRechnungartCNr().equals(RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
							Map m = DelegateFactory.getInstance().getRechnungServiceDelegate()
									.getAllProformarechnungpositionsart();

							if (!m.containsKey(positionDto.getPositionsartCNr())) {
								continue;
							}

						}

						RechnungPositionDto fehlerMeldungDto = null;
						try {
							positionDto.setIId(null);
							// damits hinten angehaengt wird.

							if (b == false) {
								Integer iIdAktuellePosition = getSelectedIdPositionen();
								if (iIdAktuellePosition != null) {
									RechnungPositionDto aktuellePositionDto = DelegateFactory.getInstance()
											.getRechnungDelegate()
											.rechnungPositionFindByPrimaryKey(iIdAktuellePosition);

									Integer iSortAktuellePosition = aktuellePositionDto.getISort();
									positionDto.setISort(iSortAktuellePosition);

									getPanelDetailPositionen(true).getArtikelsetViewController()
											.setArtikelSetIIdFuerNeuePosition(
													aktuellePositionDto.getPositioniIdArtikelset());
									// Integer iSortAktuellePosition =
									// DelegateFactory
									// .getInstance()
									// .getRechnungDelegate()
									// .rechnungPositionFindByPrimaryKey(
									// iIdAktuellePosition)
									// .getISort();
									// positionDto.setISort(iSortAktuellePosition);
								}
							} else {
								positionDto.setISort(null);
							}
							positionDto.setBelegIId(getRechnungDto().getIId());

							// Wenn Ziel Gutschrift ist, darf kein Lieferschein
							// und
							// Auftrag kopiert werden
							if (getRechnungDto().getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)
									|| getRechnungDto().getRechnungartCNr()
											.equals(RechnungFac.RECHNUNGART_WERTGUTSCHRIFT)) {
								positionDto.setLieferscheinIId(null);
								positionDto.setAuftragpositionIId(null);
							}

							if (positionDto.getRechnungpositionartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
								// Artikel pruefen:
								Integer artikelIID = positionDto.getArtikelIId();
								ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(artikelIID);

								// Seriennummernbehaftet
								if (artikelDto != null
										&& artikelDto.getBSeriennrtragend().equals(Helper.boolean2Short(true))) {
									// Menge auf 0 setzen, und neue
									// Texteingabeposition einfuegen, dass
									// Seriennummer manuell eingetragen werden
									// muss.
									BigDecimal bdPosKopierteMenge = positionDto.getNMenge();
									positionDto.setNMenge(new BigDecimal(0));
									// Seriennr. darf nicht mitkopiert werden.
									positionDto.setSeriennrChargennrMitMenge(null);

									fehlerMeldungDto = new RechnungPositionDto();
									fehlerMeldungDto.setRechnungIId(positionDto.getRechnungIId());
									fehlerMeldungDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
									Object pattern[] = { bdPosKopierteMenge };
									String sText = LPMain.getTextRespectUISPr("ls.paste.seriennummernbehaftet");
									String sTextFormattiert = MessageFormat.format(sText, pattern);
									fehlerMeldungDto.setXTextinhalt(sTextFormattiert);
								}
								// Chargennnummernbehaftet
								else if (artikelDto != null
										&& artikelDto.getBChargennrtragend().equals(Helper.boolean2Short(true))) {
									// Menge auf 0 setzen, und neue
									// Texteingabeposition einfuegen, dass
									// Chargennummer manuell eingetragen werden
									// muss.
									BigDecimal bdPosKopierteMenge = positionDto.getNMenge();
									positionDto.setNMenge(new BigDecimal(0));
									// Chargenr darf nicht mitkopiert werden.
									positionDto.setSeriennrChargennrMitMenge(null);

									fehlerMeldungDto = new RechnungPositionDto();
									fehlerMeldungDto.setRechnungIId(positionDto.getRechnungIId());
									fehlerMeldungDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
									Object pattern[] = { bdPosKopierteMenge };
									String sText = LPMain.getTextRespectUISPr("ls.paste.chargennummernbehaftet");
									String sTextFormattiert = MessageFormat.format(sText, pattern);
									fehlerMeldungDto.setXTextinhalt(sTextFormattiert);
								}
								// lagerbewirtschaftet: Lagerstand ausreichend
								else if (artikelDto != null
										&& artikelDto.getBLagerbewirtschaftet().equals(Helper.boolean2Short(true))) {

									// SP8741 Nicht bei Gutschrift
									if (!getRechnungDto().getRechnungartCNr()
											.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {

										// lagerstand ueberpruefen, damit diese
										// Position
										// nur angelegt wird, wenn genuegend
										// Menge in Lager vorhanden ist
										BigDecimal lagerstand = null;

										ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
												.getInstance().getParameterDelegate().getParametermandant(
														ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR,
														ParameterFac.KATEGORIE_ARTIKEL,
														LPMain.getTheClient().getMandant());

										int bImmerAusreichendVerfuegbar = (Integer) parameter.getCWertAsObject();

										if (bImmerAusreichendVerfuegbar == 0) {
											lagerstand = DelegateFactory.getInstance().getLagerDelegate()
													.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId());
										} else {
											lagerstand = new BigDecimal(9999999);
										}

										BigDecimal bdLagerstandDiff = lagerstand.subtract(positionDto.getNMenge());
										if (bdLagerstandDiff.compareTo(new BigDecimal(0)) < 0) {
											// nur soviel abbuchen wie moeglich,
											// fuer
											// den Rest eine extra
											// Texteingabeposition
											// mit der Fehlermeldung
											// anlegen, dass nicht die ganze Menge
											// gebucht werden konnte.
											BigDecimal bdPosKopierteMenge = positionDto.getNMenge();
											positionDto.setNMenge(lagerstand);

											fehlerMeldungDto = new RechnungPositionDto();
											fehlerMeldungDto.setRechnungIId(positionDto.getRechnungIId());
											fehlerMeldungDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);

											Object pattern[] = { positionDto.getNMenge(), bdPosKopierteMenge };
											String sText = LPMain
													.getTextRespectUISPr("ls.paste.lagerstandfuerartikelleer");
											String sTextFormattiert = MessageFormat.format(sText, pattern);
											fehlerMeldungDto.setXTextinhalt(sTextFormattiert);
										}
									}
								}
							}

							// wir legen eine neue position an
							Integer iLagerIId = getRechnungDto().getLagerIId();
							if (iLagerIId != null) {
								ArtikelsetViewController viewController = getPanelDetailPositionen(true)
										.getArtikelsetViewController();
								List<SeriennrChargennrMitMengeDto> snrs = new ArrayList<SeriennrChargennrMitMengeDto>();

								boolean bDiePositionSpeichern = viewController
										.validateArtikelsetConstraints(positionDto);

								if (bDiePositionSpeichern) {
									positionDto.setPositioniIdArtikelset(
											viewController.getArtikelSetIIdFuerNeuePosition());

									snrs = viewController.handleArtikelsetSeriennummern(iLagerIId, positionDto);
									if (!viewController.isArtikelsetWithSnrsStoreable(positionDto, snrs)) {
										bDiePositionSpeichern = false;
									}
								}

								if (bDiePositionSpeichern) {
									RechnungPositionDto rechnungPositionDto = DelegateFactory.getInstance()
											.getRechnungDelegate().updateRechnungPosition(positionDto, iLagerIId, snrs);
									if (rechnungPositionDto != null) {
										if (iId == null) {
											iId = rechnungPositionDto.getIId();
										}
										positionDtos[i] = rechnungPositionDto;
									}

									// Fehlermeldung als eigene Position
									// anlegen.
									if (fehlerMeldungDto != null) {
										// folgende Felder muessen auch bei
										// Texteingabe
										// gesetzt sein.
										fehlerMeldungDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
										fehlerMeldungDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
										DelegateFactory.getInstance().getRechnungDelegate()
												.updateRechnungPosition(fehlerMeldungDto, iLagerIId);
									}
								}
							}
						} catch (Throwable t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}
				}

				if (getRechnungDto().getLagerIId() != null) {
					ZwsEinfuegenHVRechnungposition cpp = new ZwsEinfuegenHVRechnungposition(
							getRechnungDto().getLagerIId());
					cpp.processZwsPositions(positionDtos, (BelegpositionDto[]) o);
				}

				// die Liste neu aufbauen
				panelQueryPositionen.eventYouAreSelected(false);
				// den Datensatz in der Liste selektieren
				panelQueryPositionen.setSelectedId(iId);
				// im Detail den selektierten anzeigen
				panelDetailPositionen.eventYouAreSelected(false);
			}

		}

	}

	public PanelSplit getPanelSplitZahlungen(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitZahlungen == null && bNeedInstantiationIfNull) {
			panelSplitZahlungen = new PanelSplit(getInternalFrame(), getPanelDetailZahlung(true),
					getPanelQueryZahlungen(true), 165);
			setComponentAt(iIDX_ZAHLUNGEN, panelSplitZahlungen);
		}
		return panelSplitZahlungen;
	}

	protected PanelRechnungZahlung getPanelDetailZahlung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailZahlung == null && bNeedInstantiationIfNull) {
			panelDetailZahlung = new PanelRechnungZahlung(getInternalFrame(),
					LPMain.getTextRespectUISPr("rechnung.title.panel.zahlungen"), this);

		}
		return panelDetailZahlung;
	}

	private PanelQuery getPanelQueryZahlungen(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryZahlungen == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseZahlungen = { PanelBasis.ACTION_NEW };

			panelQueryZahlungen = new PanelQuery(null, null, QueryParameters.UC_ID_ZAHLUNG, aWhichButtonIUseZahlungen,
					getInternalFrame(), "", true);
		}
		return panelQueryZahlungen;
	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI) throws Throwable {

		RechnungPositionDto bsPosDto = (RechnungPositionDto) belegposDtoI;

		bsPosDto.setBelegIId(getRechnungDto().getIId());
		bsPosDto.setISort(xalOfBelegPosI + 1000);

	}

	public PanelQuery getRechnungPositionTop() {
		return panelQueryPositionen;
	}

	public String getRechnungStatus() throws Throwable {
		return DelegateFactory.getInstance().getLocaleDelegate().getStatusCBez(getRechnungDto().getStatusCNr());
	}

}
