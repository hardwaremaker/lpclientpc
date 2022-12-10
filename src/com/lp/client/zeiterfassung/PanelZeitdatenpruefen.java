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
package com.lp.client.zeiterfassung;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.IHvValueHolderListener;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.component.durationfield.WrapperDurationField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.PersonalverfuegbarkeitDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.VonBisErfassungTagesdatenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeitdatenpruefenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFavoritenDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjekttaetigkeitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

public class PanelZeitdatenpruefen extends PanelBasis implements
		PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private ZeitdatenpruefenDto zeitdatenpruefenDto = null;
	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRAuftragposition = null;
	private PanelQueryFLR panelQueryFLRAngebotposition = null;
	private PanelQueryFLR panelQueryFLRLosposition = null;
	private PanelQueryFLR panelQueryFLRZeitmodell = null;

	private PanelQueryFLR panelQueryFLRProjekt = null;
	private PanelQueryFLR panelQueryFLRAngebot = null;

	private Integer letzterProjektBereich = null;

	private boolean bRechtNurBuchen = true;
	private boolean bDarfKommtGehtAendern = true;
	private WrapperLabel wlaZeit = new WrapperLabel();
	private WrapperTimeField wtfZeit = new WrapperTimeField();
	private WrapperTimeField wtfZeit_Bis = new WrapperTimeField();
	private WrapperDurationField wdfDauer = new WrapperDurationField();
	private WrapperLabel wlaDauer;
	private WrapperComboBox wcoSonderTaetigkeit = new WrapperComboBox();
	private boolean bProjektMitTaetigkeiten = false;

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperLabel wlaBetriebskalender = new WrapperLabel();
	private WrapperLabel wlaSonderzeiten = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();
//	private JButton wbuTagZurueck = ButtonFactory.createJButton();
//	private JButton wbuNaechsterTag = ButtonFactory.createJButton();
//	private WrapperCheckBox wcbRelativ = new WrapperCheckBox();
	static final public String ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE = "action_taetigkeit_from_liste";

	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";
	static final public String ACTION_SPECIAL_ANGEBOT_FROM_LISTE = "action_angebot_from_liste";
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_los_from_liste";
	static final public String ACTION_SPECIAL_PROJEKT_FROM_LISTE = "action_projekt_from_liste";

	static final public String ACTION_SPECIAL_POSITION_FROM_LISTE = "action_position_from_liste";
	static final public String ACTION_SPECIAL_ZEITMODELL_FROM_LISTE = "ACTION_SPECIAL_ZEITMODELL_FROM_LISTE";

	static final public String ACTION_SPECIAL_KOMMT = "action_special_kommt";
	static final public String ACTION_SPECIAL_GEHT = "action_special_geht";
	static final public String ACTION_SPECIAL_UNTER = "action_special_unter";
	static final public String ACTION_SPECIAL_ENDE = "action_special_ende";

	private WrapperLabel wlaKalenderwochewochentag = new WrapperLabel();
//	private WrapperLabel wlaTagesarbeitszeit = new WrapperLabel();
	private WrapperGotoButton wbuBeleg = new WrapperGotoButton(-1);

	private WrapperLabel wlaBeleg = new WrapperLabel();

	private WrapperButton wbuPosition = new WrapperButton();
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private WrapperTextField wtfPosition = new WrapperTextField();
	private Integer selectedBeleg = null;
	private String selectedBelegart = null;

	private WrapperButton wbuTaetigkeit = new WrapperButton();
	private WrapperTextField wtfTaetigkeit = new WrapperTextField();
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();

	private JButton wbuKommt = new WrapperButton();
	private JButton wbuGeht = new WrapperButton();
	private JButton wbuUnter = new WrapperButton();
	private JButton wbuEnde = new WrapperButton();

	private JButton wbuProjekt = new WrapperButton();
	private JButton wbuAngebot = new WrapperButton();
	private JButton wbuAuftrag = new WrapperButton();
	private JButton wbuLos = new WrapperButton();

//	private WrapperLabel wlaErfuellungsgrad = new WrapperLabel();

//	private boolean hatModulStueckrueckmeldung = false;
//	private boolean bLosbuchungOhnePositionbezug = false;
	private boolean bAuftragsbuchungOhnePositionbezug = false;

	private WrapperEditorField wefKommentar = new WrapperEditorFieldKommentar(
			getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"));

//	private JButton wbuZeitmodell = ButtonFactory.createJButton();
//	private WrapperTextField wtfZeitmodell = new WrapperTextField();
	private int iOptionSollzeitpruefeung = 0;

//	private WrapperLabel wlaFehlerInZeitdaten = new WrapperLabel();
//	private WrapperLabel wlaOffeneZeitverteilung = new WrapperLabel();

//	private Integer zeitmodellIId = null;
	private boolean bZeitdatenAufErledigteBuchbar = false;
	private boolean bZeitdatenAufAngelegteLoseBuchbar = false;

	private Integer taetigkeitIIdKommt = null;
	private Integer taetigkeitIIdUnter = null;
	private Integer taetigkeitIIdGeht = null;
	private Integer taetigkeitIIdEnde = null;

	private Map<Integer, String> sondertaetigkeitenOhneVersteckt;
	private Map<Integer, String> sondertaetigkeitenMitVersteckt;

	private boolean bArbeitszeitartikelauspersonalverfuegbarkeit = false;

	private ZeiterfassungPruefer zeiterfassungPruefer = null;
	private WrapperLabel wlaFehlercode = new WrapperLabel();
	private WrapperTextField wtfFehlercodeText = new WrapperTextField();
	private static final String ACTION_SPECIAL_UEBERLEITEN = "action_special_ueberleiten";

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfZeit;
	}

	public WrapperDateField getWdfDatum() {
		return wdfDatum;
	}

	public InternalFrameZeiterfassung getInternalFrameZeiterfassung() {
		return internalFrameZeiterfassung;
	}

	public PanelZeitdatenpruefen(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAGSBUCHUNG_OHNE_POSITIONSBEZUG,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		bAuftragsbuchungOhnePositionbezug = (Boolean) parameter
				.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_PROJEKT_MIT_TAETIGKEIT,
						ParameterFac.KATEGORIE_PROJEKT,
						LPMain.getTheClient().getMandant());

		bProjektMitTaetigkeiten = (Boolean) parameter.getCWertAsObject();

		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;

		bRechtNurBuchen = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITEINGABE_NUR_BUCHEN);
		bDarfKommtGehtAendern = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_DARF_KOMMT_GEHT_AENDERN);
		taetigkeitIIdKommt = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT)
				.getIId();
		taetigkeitIIdGeht = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT).getIId();
		taetigkeitIIdUnter = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_UNTER)
				.getIId();
		taetigkeitIIdEnde = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE).getIId();
		zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());

		jbInit();
		setDefaults();
		initComponents();

	}

	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource() == wdfDatum.getDisplay()
				&& e.getNewValue() instanceof Date
				&& e.getPropertyName().equals("date")) {
			wdfDatum.setDate((Date) e.getNewValue());
			try {

				// wrbSondertaetigkeit.setSelected(true);

				aktualisiereDaten();

				Object sKey = getInternalFrame().getKeyWasForLockMe();

				getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
						.refreshQPZeitdaten(
								getInternalFrameZeiterfassung()
										.getPersonalDto().getIId(),
								wdfDatum.getDate(),
								LPMain.getLockMeForNew().equals(sKey));

			} catch (Throwable ex) {
				// brauche ich
				handleException(ex, false);
			}
		}
	}

	public boolean isHeute() {

		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		ts = Helper.cutTimestamp(ts);

		java.sql.Timestamp tHeute = Helper.cutTimestamp(new java.sql.Timestamp(
				System.currentTimeMillis()));

		if (tHeute.getTime() == ts.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		wlaBeleg.setText(null);

		wbuKommt.setEnabled(true);
		wbuGeht.setEnabled(true);
		wbuUnter.setEnabled(true);
		wbuEnde.setEnabled(true);

		wbuAngebot.setEnabled(true);
		wbuAuftrag.setEnabled(true);
		wbuProjekt.setEnabled(true);
		wbuLos.setEnabled(true);

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			if (key != null && key.equals(LPMain.getLockMeForNew())) {
				wcoSonderTaetigkeit.setKeyOfSelectedItem(null);
			}

			clearStatusbar();
			wtfBeleg.setText("");
			wtfPosition.setText("");
			wtfTaetigkeit.setText("");
			wtfBemerkung.setText("");
			if (bDarfKommtGehtAendern == false) {
				if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {
					if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
							taetigkeitIIdGeht)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem()
									.equals(taetigkeitIIdKommt)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem()
									.equals(taetigkeitIIdUnter)) {
						wtfZeit.setEnabled(false);
					} else {
						wtfZeit.setEnabled(true);
					}
				}

			}
		} else {
			zeitdatenpruefenDto = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.zeitdatenpruefenFindByPrimaryKey(
							(Integer) getKeyWhenDetailPanel());

			dto2Components();
			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.refreshTitle();

			if (bDarfKommtGehtAendern == false) {

				if (zeitdatenpruefenDto.getTaetigkeitIId() != null) {

					if (!zeitdatenpruefenDto.getTaetigkeitIId().equals(
							taetigkeitIIdEnde)) {
						LPButtonAction o = getHmOfButtons().get(
								PanelBasis.ACTION_UPDATE);
						if (o != null) {
							o.getButton().setEnabled(false);
						}
						o = getHmOfButtons().get(PanelBasis.ACTION_DELETE);
						if (o != null) {
							o.getButton().setEnabled(false);
						}
					}
				}

			}

		}
		berechneTageszeit();
		wdfDatum.setEnabled(true);

	}

	private void enableBisZeit(boolean b) {
		wtfZeit_Bis.setVisible(b);
		wtfZeit_Bis.setMandatoryField(b);
		wdfDauer.setVisible(b);
		wdfDauer.setMandatoryField(b);
		wlaDauer.setVisible(b);
	}

	public void erstelleVorschlagFuerZeitbuchung(ZeiterfassungFavoritenDto zfDto)
			throws Throwable {
		zeitdatenpruefenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.zeitdatenpruefenFindByPrimaryKey(zfDto.getZeitdatenIId());

		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		ts = Helper.cutTimestamp(ts);

		Calendar cDatum = Calendar.getInstance();
		cDatum.setTimeInMillis(ts.getTime());

		Calendar cZeit = Calendar.getInstance();
		cZeit.setTimeInMillis(wtfZeit.getTime().getTime());

		cDatum.set(Calendar.HOUR_OF_DAY, cZeit.get(Calendar.HOUR_OF_DAY));
		cDatum.set(Calendar.MINUTE, cZeit.get(Calendar.MINUTE));
		cDatum.set(Calendar.SECOND, cZeit.get(Calendar.SECOND));
		cDatum.set(Calendar.MILLISECOND, cZeit.get(Calendar.MILLISECOND));
		ts.setTime(cDatum.getTimeInMillis());
		zeitdatenpruefenDto.setTZeit(ts);
		zeitdatenpruefenDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		zeitdatenpruefenDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		if (zfDto.getArtikelIId() != null) {
			zeitdatenpruefenDto.setArtikelIId(zfDto.getArtikelIId());
		}
		dto2Components();

		zeitdatenpruefenDto.setIId(null);

	}

	protected void dto2Components() throws Throwable {
		wdfDatum.setDate(new java.sql.Date(zeitdatenpruefenDto.getTZeit().getTime()));
		wtfZeit.setTime(new java.sql.Time(zeitdatenpruefenDto.getTZeit().getTime()));
		wefKommentar.setText(zeitdatenpruefenDto.getXKommentar());
		wtfFehlercodeText.setText(zeitdatenpruefenDto.getXFehlertext());
		if (zeitdatenpruefenDto.getTaetigkeitIId() != null) {

			jpaWorkingOn.repaint();

			TaetigkeitDto taetigkeit = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.taetigkeitFindByPrimaryKey(zeitdatenpruefenDto.getTaetigkeitIId());
			if (taetigkeit != null
					&& Helper.short2boolean(taetigkeit.getBVersteckt())) {
				wcoSonderTaetigkeit.setMap(sondertaetigkeitenMitVersteckt);
			} else {
				wcoSonderTaetigkeit.setMap(sondertaetigkeitenOhneVersteckt);
			}
			wcoSonderTaetigkeit.setKeyOfSelectedItem(zeitdatenpruefenDto
					.getTaetigkeitIId());
			wtfTaetigkeit.setText(null);
			wtfBeleg.setText(null);
			wtfPosition.setText(null);

			wtfPosition.setMandatoryField(false);
			wtfBeleg.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);

			wbuBeleg.setActivatable(false);
			wbuPosition.setActivatable(false);
			wbuTaetigkeit.setActivatable(false);
			wtfBemerkung.setText(zeitdatenpruefenDto.getCBemerkungzubelegart());
		} else {
			selectedBelegart = zeitdatenpruefenDto.getCBelegartnr();
			wbuBeleg.setOKey(zeitdatenpruefenDto.getIBelegartid());
			wcoSonderTaetigkeit.setKeyOfSelectedItem(null);

			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(zeitdatenpruefenDto.getArtikelIId());

			wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
			wtfBemerkung.setText(zeitdatenpruefenDto.getCBemerkungzubelegart());

			if (zeitdatenpruefenDto.getCBelegartnr()
					.equals(LocaleFac.BELEGART_AUFTRAG)) {
				wtfPosition.setText(null);

				AuftragpositionDto auftragpositionDto = null;
				try {
					auftragpositionDto = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.auftragpositionFindByPrimaryKey(
									zeitdatenpruefenDto.getIBelegartpositionid());
					wtfPosition.setText(auftragpositionDto.getCBez());
				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										(LPMain.getTextRespectUISPr("zeiterfassung.auftragspositiongeloescht")));
					} else {
						handleException(ex, false);
					}
				}

				erfuellungsgradBerechnen(null, auftragpositionDto);

				AuftragDto auftragDto = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(zeitdatenpruefenDto.getIBelegartid());

				String sProjBez = "";
				if (auftragDto.getCBezProjektbezeichnung() != null) {
					sProjBez = ", " + auftragDto.getCBezProjektbezeichnung();
				}

				String kunde = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdAuftragsadresse())
						.getPartnerDto().formatTitelAnrede();
				wlaBeleg.setText("AB" + auftragDto.getCNr());
				wtfBeleg.setText("->" + sProjBez + ", " + kunde);
				selectedBeleg = auftragDto.getIId();
				selectedBelegart = LocaleFac.BELEGART_AUFTRAG;
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
				if (auftragpositionDto != null) {
					zeitdatenpruefenDto.setIBelegartpositionid(auftragpositionDto
							.getIId());
					if (auftragpositionDto.getArtikelIId() != null
							&& auftragpositionDto.getCBez() == null) {
						wtfPosition.setText(DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikelFindByPrimaryKey(
										auftragpositionDto.getArtikelIId())
								.formatArtikelbezeichnung());
					}
				}

				if (bAuftragsbuchungOhnePositionbezug == true) {
					wtfPosition.setMandatoryField(false);
				} else {
					wtfPosition.setMandatoryField(true);
				}

				wtfPosition.setMandatoryField(true);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);

				wbuBeleg.setActivatable(true);

				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenpruefenDto.getCBelegartnr().equals(
					LocaleFac.BELEGART_LOS)) {
				wtfPosition.setText(null);

				if (zeitdatenpruefenDto.getIBelegartpositionid() != null) {
					LossollarbeitsplanDto lospositionDto = null;
					try {
						lospositionDto = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.lossollarbeitsplanFindByPrimaryKey(
										zeitdatenpruefenDto.getIBelegartpositionid());
						wtfPosition.setText(lospositionDto.getIId() + "");
					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("zeiterfassung.lospositiongeloescht"));
						} else {
							handleException(ex, false);
						}
					}
					if (lospositionDto != null) {
						zeitdatenpruefenDto.setIBelegartpositionid(lospositionDto
								.getIId());
						if (lospositionDto.getArtikelIIdTaetigkeit() != null) {
							wtfPosition.setText(DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(
											lospositionDto
													.getArtikelIIdTaetigkeit())
									.getCNr());
						}

					}
					erfuellungsgradBerechnen(lospositionDto, null);
				} else {

					erfuellungsgradBerechnen(null, null);
				}
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losFindByPrimaryKey(zeitdatenpruefenDto.getIBelegartid());

				String sProjBez = "";
				if (losDto.getCProjekt() != null) {
					sProjBez = losDto.getCProjekt();
				}
				wlaBeleg.setText(losDto.getCNr());
				wtfBeleg.setText("->" + sProjBez);
				selectedBeleg = losDto.getIId();
				selectedBelegart = LocaleFac.BELEGART_LOS;
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_FERTIGUNG_AUSWAHL);

				wtfPosition.setMandatoryField(true);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);

				wbuBeleg.setActivatable(true);

				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenpruefenDto.getCBelegartnr().equals(
					LocaleFac.BELEGART_PROJEKT)) {
				wtfPosition.setText(null);

				ProjektDto projektDto = DelegateFactory.getInstance()
						.getProjektDelegate()
						.projektFindByPrimaryKey(zeitdatenpruefenDto.getIBelegartid());

				String sProjBez = "";
				if (projektDto.getCTitel() != null) {
					sProjBez = projektDto.getCTitel();
				}
				wlaBeleg.setText("PJ" + projektDto.getCNr());
				wtfBeleg.setText("->" + sProjBez);
				selectedBeleg = projektDto.getIId();
				selectedBelegart = LocaleFac.BELEGART_PROJEKT;
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_PROJEKT_AUSWAHL);

				wtfPosition.setMandatoryField(false);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);

				wbuPosition.setActivatable(false);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenpruefenDto.getCBelegartnr().equals(
					LocaleFac.BELEGART_ANGEBOT)) {
				wtfPosition.setText(null);

				if (zeitdatenpruefenDto.getIBelegartpositionid() != null) {
					AngebotpositionDto angebotpositionDto = null;
					try {
						angebotpositionDto = DelegateFactory
								.getInstance()
								.getAngebotpositionDelegate()
								.angebotpositionFindByPrimaryKey(
										zeitdatenpruefenDto.getIBelegartpositionid());

					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("zeiterfassung.angebotpositiongeloescht"));
						} else {
							handleException(ex, false);
						}
					}
					if (angebotpositionDto != null) {
						zeitdatenpruefenDto.setIBelegartpositionid(angebotpositionDto
								.getIId());
						if (angebotpositionDto.getArtikelIId() != null) {
							wtfPosition.setText(angebotpositionDto.getCBez());
							if (angebotpositionDto.getArtikelIId() != null
									&& angebotpositionDto.getCBez() == null) {
								wtfPosition.setText(DelegateFactory
										.getInstance()
										.getArtikelDelegate()
										.artikelFindByPrimaryKey(
												angebotpositionDto
														.getArtikelIId())
										.formatArtikelbezeichnung());
							}

						}
					}

				}
				AngebotDto angebotDto = DelegateFactory.getInstance()
						.getAngebotDelegate()
						.angebotFindByPrimaryKey(zeitdatenpruefenDto.getIBelegartid());

				String sProjBez = "";
				if (angebotDto.getCBez() != null) {
					sProjBez = angebotDto.getCBez();
				}

				wlaBeleg.setText("AG" + angebotDto.getCNr());
				wtfBeleg.setText("->" + sProjBez);
				selectedBeleg = angebotDto.getIId();
				selectedBelegart = LocaleFac.BELEGART_ANGEBOT;
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_ANGEBOT_AUSWAHL);

				wtfPosition.setMandatoryField(false);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);
			}
		}
		updatePosAndTaetigkeitFelder(zeitdatenpruefenDto.getCBelegartnr(),
				zeitdatenpruefenDto.getTaetigkeitIId());
		setzteKalenderWochewochentag();
		
		this.setStatusbarPersonalIIdAendern(zeitdatenpruefenDto
				.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(zeitdatenpruefenDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(zeitdatenpruefenDto.getTAnlegen());
		this.setStatusbarTAendern(zeitdatenpruefenDto.getTAendern());
	}

	private void updatePosAndTaetigkeitFelder(String belegartCNr,
			Object sonderTaetigkeitIId) {
		if (sonderTaetigkeitIId != null) {
			wtfPosition.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);
			wbuPosition.setActivatable(false);
			wbuTaetigkeit.setActivatable(false);
			return;
		}
		if (LocaleFac.BELEGART_AUFTRAG.equals(belegartCNr)) {
			if (bAuftragsbuchungOhnePositionbezug) {
				wtfPosition.setMandatoryField(false);
			} else {
				wtfPosition.setMandatoryField(true);
			}

			wtfTaetigkeit.setMandatoryField(true);
			wbuPosition.setActivatable(true);
			wbuTaetigkeit.setActivatable(true);
			return;
		}
		if (LocaleFac.BELEGART_LOS.equals(belegartCNr)) {
			wtfPosition.setMandatoryField(true);
			wtfTaetigkeit.setMandatoryField(true);
			wbuPosition.setActivatable(true);
			wbuTaetigkeit.setActivatable(true);
			return;
		}
		if (LocaleFac.BELEGART_PROJEKT.equals(belegartCNr)) {
			wtfPosition.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(true);
			wbuPosition.setActivatable(false);
			wbuTaetigkeit.setActivatable(true);
			return;
		}
		if (LocaleFac.BELEGART_ANGEBOT.equals(belegartCNr)) {
			wtfPosition.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(true);
			wbuPosition.setActivatable(true);
			wbuTaetigkeit.setActivatable(true);
			return;
		}
		wbuPosition.setActivatable(true);
		wbuTaetigkeit.setActivatable(true);
		wtfPosition.setMandatoryField(true);
		wtfTaetigkeit.setMandatoryField(true);
	}

	private void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
		if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
			panelQueryFLRArtikel = new PanelQueryFLR(null,
					PersonalFilterFactory.getInstance().createFKPersonal(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId()),
					QueryParameters.UC_ID_PERSONALVERFUEGBARKEIT,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("title.artikelauswahlliste"));
			new DialogQuery(panelQueryFLRArtikel);

		} else {

			if (bProjektMitTaetigkeiten && selectedBelegart != null
					&& selectedBelegart.equals(LocaleFac.BELEGART_PROJEKT)
					&& selectedBeleg != null) {

				ProjekttaetigkeitDto[] ptDtos = DelegateFactory.getInstance()
						.getProjektServiceDelegate()
						.projekttaetigkeitFindByProjektIId(selectedBeleg);

				if (ptDtos.length == 0) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.info"),
									LPMain.getTextRespectUISPr("proj.projekttaetigkeiten.keinedefiniert"));
					return;
				} else {
					panelQueryFLRArtikel = new PanelQueryFLR(
							null,
							ArtikelFilterFactory.getInstance()
									.createFKTaetigkeitenEinesProjektes(ptDtos),
							QueryParameters.UC_ID_ARTIKELLISTE,
							aWhichButtonIUse,
							internalFrameZeiterfassung,
							LPMain.getTextRespectUISPr("title.artikelauswahlliste"));

					FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory
							.getInstance().createFKDArtikelnummer(
									getInternalFrame());
					FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory
							.getInstance().createFKDVolltextsuche();
					panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(
							fkDirekt1, fkDirekt2);

					panelQueryFLRArtikel.setSelectedId(zeitdatenpruefenDto
							.getArtikelIId());

					new DialogQuery(panelQueryFLRArtikel);
				}

			} else {
				panelQueryFLRArtikel = new PanelQueryFLR(null,
						ArtikelFilterFactory.getInstance()
								.createFKArtikellisteNurArbeitszeit(),
						QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
						internalFrameZeiterfassung,
						LPMain.getTextRespectUISPr("title.artikelauswahlliste"));

				FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory
						.getInstance().createFKDArtikelnummer(
								getInternalFrame());
				FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory
						.getInstance().createFKDVolltextsuche();
				panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(
						fkDirekt1, fkDirekt2);

				panelQueryFLRArtikel
						.setSelectedId(zeitdatenpruefenDto.getArtikelIId());

				new DialogQuery(panelQueryFLRArtikel);
			}

		}
	}

	void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN, PanelBasis.ACTION_FILTER };

		FilterKriterium[] kriterien = null;

		if (bZeitdatenAufErledigteBuchbar) {
			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true, "'"
							+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			FilterKriterium krit2 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
							+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);
			kriterien[0] = krit1;
			kriterien[1] = krit2;
		} else {

			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true, "'"
							+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
							+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "','"
							+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);

			kriterien[0] = krit1;
			kriterien[1] = krit2;
		}
		panelQueryFLRAuftrag = new PanelQueryFLR(AuftragFilterFactory
				.getInstance().createQTPanelAuftragAuswahl(), kriterien,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("title.auftragauswahlliste"));

		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname(),
				AuftragFilterFactory.getInstance().createFKVAuftrag());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDProjekt());

		Integer selectedBelegIId = null;
		if (zeitdatenpruefenDto != null) {
			selectedBelegIId = zeitdatenpruefenDto.getIBelegartid();
		}

		panelQueryFLRAuftrag.setSelectedId(selectedBelegIId);

		new DialogQuery(panelQueryFLRAuftrag);

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		Integer selectedBelegIId = null;
		if (zeitdatenpruefenDto != null) {
			selectedBelegIId = zeitdatenpruefenDto.getIBelegartid();
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LOSAUSWAHL_TECHNIKERFILTER,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null
				&& ((Boolean) parameter.getCWertAsObject()) == true) {

			// Dialog'Wiedervorlage erstellen
			getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = new DialogLoseEinesTechnikers(
					getInternalFrame(), bZeitdatenAufErledigteBuchbar,
					selectedBelegIId, getInternalFrameZeiterfassung()
							.getPersonalDto().getIId(),
					bZeitdatenAufAngelegteLoseBuchbar);

			LPMain.getInstance()
					.getDesktop()
					.platziereDialogInDerMitteDesFensters(
							getInternalFrameZeiterfassung().dialogLoseEinesTechnikers);
			getInternalFrameZeiterfassung().dialogLoseEinesTechnikers
					.setVisible(true);

		} else {
			panelQueryFLRLos = FertigungFilterFactory.getInstance()
					.createPanelFLRBebuchbareLose(getInternalFrame(),
							bZeitdatenAufErledigteBuchbar, true,
							bZeitdatenAufAngelegteLoseBuchbar,
							selectedBelegIId, false);
			// PJ17681

			Map<?, ?> mEingeschraenkteFertigungsgruppen = DelegateFactory
					.getInstance().getStuecklisteDelegate()
					.getEingeschraenkteFertigungsgruppen();

			if (mEingeschraenkteFertigungsgruppen != null) {
				panelQueryFLRLos.setFilterComboBox(
						mEingeschraenkteFertigungsgruppen, new FilterKriterium(
								"flrlos.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false), true);
				panelQueryFLRLos.eventActionRefresh(null, true);
			} else {

				panelQueryFLRLos.setFilterComboBox(DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.getAllFertigungsgrupe(), new FilterKriterium(
						"flrlos.fertigungsgruppe_i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false, LPMain
						.getTextRespectUISPr("lp.alle"));
			}

		}

		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		Integer selectedBelegIId = null;
		if (zeitdatenpruefenDto != null) {
			selectedBelegIId = zeitdatenpruefenDto.getIBelegartid();
		}

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame(), selectedBelegIId,
						false);

		if (letzterProjektBereich != null) {
			panelQueryFLRProjekt.setKeyOfFilterComboBox(letzterProjektBereich);
		}

		new DialogQuery(panelQueryFLRProjekt);

	}

	void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {

		Integer selectedBelegIId = null;
		if (zeitdatenpruefenDto != null) {
			selectedBelegIId = zeitdatenpruefenDto.getIBelegartid();
		}

		panelQueryFLRAngebot = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebot(
						getInternalFrame(),
						false,
						false,
						AngebotFilterFactory.getInstance()
								.createFKAngebotOffene(), selectedBelegIId);
		new DialogQuery(panelQueryFLRAngebot);

	}

	void dialogQueryZeitmodellFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRZeitmodell = PersonalFilterFactory.getInstance()
				.createPanelFLRZeitmodell(getInternalFrame(), null, false);
		new DialogQuery(panelQueryFLRZeitmodell);
	}

	void dialogQueryAuftragpositionFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		QueryType[] querytypes = null;

		if (bAuftragsbuchungOhnePositionbezug) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
		}

		panelQueryFLRAuftragposition = new PanelQueryFLR(querytypes,
				AuftragFilterFactory.getInstance()
						.createFKUmsatzrelevantePositionen(selectedBeleg),
				QueryParameters.UC_ID_AUFTRAGPOSITION_ZEITERFASSUNG,
				aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("auft.title.panel.positionen"));

		new DialogQuery(panelQueryFLRAuftragposition);

	}

	void dialogQueryAngebotpositionFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] aFilterKrit = null;

		if (selectedBeleg != null) {
			aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT + ".i_id",
					true, selectedBeleg.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;

			FilterKriterium krit2 = new FilterKriterium(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_POSITIONART_C_NR,
					true, "'" + AngebotServiceFac.ANGEBOTPOSITIONART_IDENT
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			aFilterKrit[1] = krit2;

		}
		QueryType[] querytypes = null;
		panelQueryFLRAngebotposition = new PanelQueryFLR(querytypes,
				aFilterKrit, QueryParameters.UC_ID_ANGEBOTPOSITION,
				aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("auft.title.panel.positionen"));

		new DialogQuery(panelQueryFLRAngebotposition);

	}

	private void dialogQueryLospositionFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRLosposition = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollarbeitsplan(getInternalFrame(),
						selectedBeleg, null);
		new DialogQuery(panelQueryFLRLosposition);
	}

	
	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaZeit.setText(LPMain.getTextRespectUISPr("lp.zeit"));
		wcoSonderTaetigkeit
				.addActionListener(new PanelZeitdatenpruefen_wcoSonderTaetigkeit_actionAdapter(
						this));
		wdfDatum.setMandatoryField(true);
		wdfDatum.setActivatable(false);

		wtfTaetigkeit.setActivatable(false);
		wtfTaetigkeit.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfFehlercodeText.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		
		wtfPosition.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.isBVonBisErfassung() == true
				&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
						.isBKommtGehtBuchen() == false) {
			wdfDatum.setTimestamp(new java.sql.Timestamp(System
					.currentTimeMillis()));
		} else {
			java.sql.Timestamp t = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId());
			if (t != null) {
				wdfDatum.setTimestamp(t);
			} else {
				wdfDatum.setTimestamp(new java.sql.Timestamp(System
						.currentTimeMillis()));
			}
		}

		wdfDatum.getDisplay().addPropertyChangeListener(this);
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARBEITSZEITARTIKEL_AUS_PERSONALVERFUEGBARKEIT,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bArbeitszeitartikelauspersonalverfuegbarkeit = true;
		}

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufErledigteBuchbar = true;
		}

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufAngelegteLoseBuchbar = true;
		}

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SOLLZEITPRUEFUNG,
						ParameterFac.KATEGORIE_ALLGEMEIN,
						LPMain.getTheClient().getMandant());

		iOptionSollzeitpruefeung = (Integer) parameter.getCWertAsObject();

		wlaKalenderwochewochentag.setText(LPMain
				.getTextRespectUISPr("lp.kalenderwoche_kurz"));
		wlaKalenderwochewochentag.setHorizontalAlignment(SwingConstants.LEFT);

		wlaBetriebskalender.setHorizontalAlignment(SwingConstants.LEFT);
		wbuPosition.setText(LPMain.getTextRespectUISPr("lp.position") + "...");
		wbuPosition
				.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_POSITION_FROM_LISTE);
		wbuPosition.addActionListener(this);
		wbuPosition.setMnemonic('O');
		wtfBeleg.setActivatable(false);
		wtfBeleg.setText("");
		wtfBeleg.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfPosition.setActivatable(false);
		wtfPosition.setText("");
		wtfBemerkung.setColumnsMax(ZeiterfassungFac.MAX_ZEITDATEN_BEMERKUNG);
		wtfZeit.setMandatoryField(true);

		wbuTaetigkeit.setText(LPMain.getTextRespectUISPr("lp.taetigkeit")
				+ "...");
		wbuTaetigkeit
				.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE);
		wbuTaetigkeit.addActionListener(this);
		wbuTaetigkeit.setMnemonic('I');

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));
		wlaBemerkung.setDisplayedMnemonic('B');
		wlaBemerkung.setLabelFor(wtfBemerkung);

		wlaZeit.setDisplayedMnemonic('Z');
		wlaZeit.setLabelFor(wtfZeit);

		wlaDatum.setDisplayedMnemonic('D');
		wlaDatum.setLabelFor(wdfDatum);

		wlaDauer = new WrapperLabel(LPMain.getTextRespectUISPr("lp.dauer"));
		VonBisDauerListener vbdListener = new VonBisDauerListener();
		wdfDauer.addValueChangedListener(vbdListener);
		wtfZeit.addValueChangedListener(vbdListener);
		wtfZeit_Bis.addValueChangedListener(vbdListener);

		wlaFehlercode.setText(LPMain.getTextRespectUISPr("zeiterfassung.fehlertext"));
		wlaFehlercode.setLabelFor(wtfFehlercodeText);
		
		String[] aWhichButtonIUse = null;

		if (bRechtNurBuchen) {
			aWhichButtonIUse = new String[] { ACTION_SAVE, ACTION_DISCARD, };
			wtfZeit.setActivatable(false);
		} else {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, };
		}

		enableToolsPanelButtons(aWhichButtonIUse);
		createAndSaveAndShowButton("/com/lp/client/res/clock_preferences.png",
				LPMain.getTextRespectUISPr("zeiterfassung.ueberleiten"),
				ACTION_SPECIAL_UEBERLEITEN, KeyStroke.getKeyStroke('Z',
						java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);

		jpaWorkingOn = new JPanel(
				new MigLayout("wrap 7, hidemode 3",
						"[12.5%,fill|12.5%,fill|12.5%,fill|12.5%,fill|12.5%,fill|12.5%,fill|25%,fill]"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaBetriebskalender, "skip 3, span 2");
		jpaWorkingOn.add(wlaSonderzeiten, "span");

		jpaWorkingOn.add(wlaDatum);
		JPanel panelDatum = new JPanel(new MigLayout("wrap 3, insets 0",
				"[fill||fill]"));
		panelDatum.add(wdfDatum, "w 0:pref:max");
//		jpaWorkingOn.add(panelDatum, "span 3");
		jpaWorkingOn.add(panelDatum);
		jpaWorkingOn.add(wlaZeit);
		jpaWorkingOn.add(wtfZeit);	
		jpaWorkingOn.add(wlaKalenderwochewochentag, "span 2, wrap");

//		jpaWorkingOn.add(wlaZeit);
//		jpaWorkingOn.add(wtfZeit, "split, span 3, grow 50");

		wbuKommt.setText(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdKommt)
				.getBezeichnung());
		wbuKommt.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_KOMMT);
		wbuKommt.addActionListener(this);
		wbuKommt.setMnemonic('K');

		wbuUnter.setText(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdUnter)
				.getBezeichnung());
		wbuUnter.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_UNTER);
		wbuUnter.addActionListener(this);
		wbuUnter.setMnemonic('U');

		wbuEnde.setText(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdEnde).getBezeichnung());
		wbuEnde.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_ENDE);
		wbuEnde.addActionListener(this);
		wbuEnde.setMnemonic('E');

		wbuGeht.setText(DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdGeht).getBezeichnung());
		wbuGeht.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_GEHT);
		wbuGeht.addActionListener(this);
		wbuGeht.setMnemonic('G');

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.isBKommtGehtBuchen() == false
				&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
						.isBVonBisErfassung() == true) {
			jpaWorkingOn.add(new WrapperLabel(""));
		} else {
			jpaWorkingOn.add(wbuKommt);
		}

		jpaWorkingOn.add(wbuUnter);

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.isBKommtGehtBuchen() == false
				&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
						.isBVonBisErfassung() == true) {
			jpaWorkingOn.add(new WrapperLabel(""));
		} else {
			jpaWorkingOn.add(wbuGeht);
		}

		if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
				.isBVonBisErfassung()) {
			jpaWorkingOn.add(new WrapperLabel(""));
		} else {
			jpaWorkingOn.add(wbuEnde);
		}

		jpaWorkingOn.add(wcoSonderTaetigkeit, "wrap");
//		jpaWorkingOn.add(wcoSonderTaetigkeit);
//		jpaWorkingOn
//		.add(wefKommentar, "span 2 6, wrap, w 0:0:max, pushx");



		wbuProjekt.setText(LPMain.getTextRespectUISPr("label.projekt"));
		wbuProjekt
				.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_PROJEKT_FROM_LISTE);
		wbuProjekt.addActionListener(this);
		wbuProjekt.setMnemonic('P');

		wbuAngebot.setText(LPMain.getTextRespectUISPr("angb.angebot"));
		wbuAngebot
				.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_ANGEBOT_FROM_LISTE);
		wbuAngebot.addActionListener(this);
		wbuAngebot.setMnemonic('T');

		wbuAuftrag.setText(LPMain.getTextRespectUISPr("auft.auftrag"));
		wbuAuftrag
				.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setMnemonic('A');

		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title"));
		wbuLos.setActionCommand(PanelZeitdatenpruefen.ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		wbuLos.setMnemonic('L');

		Map<?, ?> m = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.getBebuchbareBelegarten();
		if (m.containsKey(LocaleFac.BELEGART_PROJEKT)) {
			jpaWorkingOn.add(wbuProjekt);
		} else {
			jpaWorkingOn.add(new WrapperLabel(""));
		}

		if (m.containsKey(LocaleFac.BELEGART_ANGEBOT)) {
			jpaWorkingOn.add(wbuAngebot);
		} else {
			jpaWorkingOn.add(new WrapperLabel(""));
		}

		if (m.containsKey(LocaleFac.BELEGART_AUFTRAG)) {
			jpaWorkingOn.add(wbuAuftrag);
		} else {
			jpaWorkingOn.add(new WrapperLabel(""));
		}

		if (m.containsKey(LocaleFac.BELEGART_LOS)) {
			jpaWorkingOn.add(wbuLos);
		} else {
			jpaWorkingOn.add(new WrapperLabel(""));
		}

		jpaWorkingOn
				.add(wefKommentar, "skip, span 3 6, wrap, w 0:0:max, pushx");

		jpaWorkingOn.add(wlaBeleg);
		jpaWorkingOn.add(wbuBeleg.getWrapperButtonGoTo(),
				"split 2, grow 5, span 4");
		jpaWorkingOn.add(wtfBeleg, "grow, wrap");

		jpaWorkingOn.add(wbuPosition);
		jpaWorkingOn.add(wtfPosition, "span 4, wrap");

		jpaWorkingOn.add(wbuTaetigkeit);
//		jpaWorkingOn.add(wtfTaetigkeit, "span 4, wrap");
		jpaWorkingOn.add(wtfTaetigkeit, "span 4, wrap");

		jpaWorkingOn.add(wlaBemerkung);
//		jpaWorkingOn.add(wtfBemerkung, "top, span 4, wrap");
		jpaWorkingOn.add(wtfBemerkung, "span 4, wrap");

		jpaWorkingOn.add(wlaFehlercode, "top");
		jpaWorkingOn.add(wtfFehlercodeText, "top, span 4");
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		leereAlleFelder(this);
		wdfDatum.setTimestamp(ts);

		// super.eventActionNew(eventObject, true, false);
		zeitdatenpruefenDto = new ZeitdatenpruefenDto();
		selectedBeleg = null;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		wtfZeit.setTime(new java.sql.Time(c.getTimeInMillis()));

		wtfPosition.setMandatoryField(true);
		wtfBeleg.setMandatoryField(true);
		wtfTaetigkeit.setMandatoryField(true);
		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.isBVonBisErfassung()) {

			// PJ18440
			Timestamp tsLetzteZeit = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.getLetzteGebuchteZeit(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId(), ts);

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_VON_BIS_VERSATZ,
							ParameterFac.KATEGORIE_PERSONAL,
							LPMain.getTheClient().getMandant());

			int iVersatz = (Integer) parameter.getCWertAsObject();

			// Millisekunden abschneiden

			if (tsLetzteZeit != null) {
				Calendar cMSAbschneiden = Calendar.getInstance();
				cMSAbschneiden.setTimeInMillis(tsLetzteZeit.getTime());
				cMSAbschneiden.set(Calendar.MILLISECOND, 0);
				tsLetzteZeit = new java.sql.Timestamp(
						cMSAbschneiden.getTimeInMillis());
			}

			if (tsLetzteZeit != null) {
				wtfZeit.setTime(new java.sql.Time(tsLetzteZeit.getTime()));

				Calendar cBis = Calendar.getInstance();
				cBis.setTimeInMillis(tsLetzteZeit.getTime());
				cBis.add(Calendar.MINUTE, iVersatz);

				wtfZeit_Bis.setTime(new java.sql.Time(cBis.getTimeInMillis()));
			} else {

				Calendar cMSAbschneiden = Calendar.getInstance();
				cMSAbschneiden.setTimeInMillis(c.getTimeInMillis());
				cMSAbschneiden.set(Calendar.MILLISECOND, 0);

				cMSAbschneiden.add(Calendar.MINUTE, iVersatz);

				wtfZeit_Bis.setTime(new java.sql.Time(new java.sql.Timestamp(
						cMSAbschneiden.getTimeInMillis()).getTime()));
			}

		}
		
		wtfBemerkung.setText(null);
		wdfDatum.setEnabled(false);
		Object key = wcoSonderTaetigkeit.getKeyOfSelectedItem();
		wcoSonderTaetigkeit.setMap(sondertaetigkeitenOhneVersteckt);
		wcoSonderTaetigkeit.setKeyOfSelectedItem(key);

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.getPanelQueryZeitdaten().getSelectedId() == null) {
			wcoSonderTaetigkeit.setKeyOfSelectedItem(DelegateFactory
					.getInstance().getZeiterfassungDelegate()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT)
					.getIId());
		}

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		wdfDatum.setEnabled(true);
	}

	private ZeitdatenDto neuesZeitdatenDtoFuerHeuteTaetigkeitVorbesetzen(
			Integer taetigkeitIId) throws Throwable {
		ZeitdatenDto zDto = new ZeitdatenDto();
		zDto.setTaetigkeitIId(taetigkeitIId);
		zDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto()
				.getIId());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		java.sql.Timestamp tsHeute = new Timestamp(c.getTimeInMillis());

		zDto.setTZeit(tsHeute);
		zDto.setCBemerkungZuBelegart(wtfBemerkung.getText());

		zDto.setCWowurdegebucht("Client: " + Helper.getPCName());

		zDto.setXKommentar(wefKommentar.getText());
		zDto.setTaetigkeitIId(taetigkeitIId);

		return zDto;
	}

	private void neuesZeitdatenDtoFuerHeuteBelegSpeichern(String belegartCNr,
			Integer belegIId, Integer belegpositionIId) throws Throwable {
		ZeitdatenDto zDto = new ZeitdatenDto();
		zDto.setCBelegartnr(belegartCNr);
		zDto.setIBelegartid(belegIId);
		zDto.setIBelegartpositionid(belegpositionIId);
		zDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto()
				.getIId());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		java.sql.Timestamp tsHeute = new Timestamp(c.getTimeInMillis());
		zDto.setTZeit(tsHeute);

		zDto.setCWowurdegebucht("Client: " + Helper.getPCName());

		//SP4919
		//zDto.setCBemerkungZuBelegart(wtfBemerkung.getText());
		//zDto.setXKommentar(wefKommentar.getText());

		if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
			com.lp.server.auftrag.service.AuftragpositionDto[] auftragpositionDtos = DelegateFactory
					.getInstance().getAuftragpositionDelegate()
					.auftragpositionFindByAuftrag(belegIId);

			if (auftragpositionDtos != null && auftragpositionDtos.length > 0) {
				for (int i = 0; i < auftragpositionDtos.length; i++) {
					AuftragpositionDto dto = auftragpositionDtos[i];

					if (dto.getAuftragpositionstatusCNr() != null) {
						zDto.setIBelegartpositionid(auftragpositionDtos[0]
								.getIId());
						if (auftragpositionDtos[0].getArtikelIId() != null
								&& auftragpositionDtos[0].getCBez() == null) {
							zDto.setArtikelIId(auftragpositionDtos[0]
									.getArtikelIId());
							break;
						}
					}
				}

			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("zeiterfassung.auftragkeinepositionen"));
				return;
			}
		}

		// AZ-Artikel

		if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
			Integer artikelIId = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId());
			if (artikelIId != null) {
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId);

				zDto.setArtikelIId(artikelDto.getIId());
			}
		}

		if (zDto.getArtikelIId() == null) {
			// DEFAULT-AZ-Artikel
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							LPMain.getTheClient().getMandant());

			if (parameter.getCWert() != null
					&& !parameter.getCWertAsObject().equals("")) {
				try {
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByCNr(parameter.getCWert());

					zDto.setArtikelIId(artikelDto.getIId());
				} catch (Throwable ex) {
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
							new Exception(
									"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
				}
			}
		}
		try {
			Integer iId = DelegateFactory.getInstance()
					.getZeiterfassungDelegate().createZeitdaten(zDto);
			setKeyWhenDetailPanel(iId);
			internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
					.getPanelQueryZeitdaten().eventYouAreSelected(false);
			internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
					.getPanelQueryZeitdaten().setSelectedId(iId);
			internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
					.getPanelQueryZeitdaten().eventYouAreSelected(false);
		} catch (ExceptionLP ex) {
			// Wenn Kommt vorher, dann keine Nachfrage

			// if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_BUCHUNG_BEREITS_VORHANDEN) {

				ZeitdatenDto zDto_Vorhanden = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.zeitdatenFindByPersonalIIdTZeit(zDto.getPersonalIId(),
								zDto.getTZeit());

				if (zDto_Vorhanden != null
						&& zDto_Vorhanden.getTaetigkeitIId() != null
						&& zDto_Vorhanden.getTaetigkeitIId().equals(
								taetigkeitIIdKommt)) {
					zDto.setTZeit(new java.sql.Timestamp(zDto_Vorhanden
							.getTZeit().getTime() + 10));

					Integer iId = DelegateFactory.getInstance()
							.getZeiterfassungDelegate().createZeitdaten(zDto);
					setKeyWhenDetailPanel(iId);
					internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
							.getPanelQueryZeitdaten()
							.eventYouAreSelected(false);
					internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
							.getPanelQueryZeitdaten().setSelectedId(iId);
					internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
							.getPanelQueryZeitdaten()
							.eventYouAreSelected(false);

				}

			} else {
				handleException(ex, false);
				return;
			}

		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		int lockstate = getLockedstateDetailMainKey().getIState();

		if (e.getActionCommand().equals(ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PROJEKT_FROM_LISTE)) {
			dialogQueryProjektFromListe(e);

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {

			dialogQueryAuftragFromListe(e);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {

			dialogQueryLosFromListe(e);

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANGEBOT_FROM_LISTE)) {

			dialogQueryAngebotFromListe(e);

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ZEITMODELL_FROM_LISTE)) {
			dialogQueryZeitmodellFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOMMT)
				|| e.getActionCommand().equals(ACTION_SPECIAL_GEHT)
				|| e.getActionCommand().equals(ACTION_SPECIAL_UNTER)
				|| e.getActionCommand().equals(ACTION_SPECIAL_ENDE)) {

			wtfPosition.setMandatoryField(false);
			wtfBeleg.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);

			Integer taetigkeitIId = null;
			if (e.getActionCommand().equals(ACTION_SPECIAL_KOMMT)) {
				taetigkeitIId = taetigkeitIIdKommt;
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_GEHT)) {
				taetigkeitIId = taetigkeitIIdGeht;
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_UNTER)) {
				taetigkeitIId = taetigkeitIIdUnter;
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_ENDE)) {
				taetigkeitIId = taetigkeitIIdEnde;
			}

			// Wenn NICHT-Aendern-Modus

			if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED
					|| lockstate == PanelBasis.LOCK_FOR_EMPTY) {
				ZeitdatenDto zDto = neuesZeitdatenDtoFuerHeuteTaetigkeitVorbesetzen(taetigkeitIId);
				zDto.setCBemerkungZuBelegart(null);
				zDto.setXKommentar(null);
				Integer iId = DelegateFactory.getInstance()
						.getZeiterfassungDelegate().createZeitdaten(zDto);
				setKeyWhenDetailPanel(iId);
				internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
						.getPanelQueryZeitdaten().eventYouAreSelected(false);
				internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
						.getPanelQueryZeitdaten().setSelectedId(iId);
				setKeyWhenDetailPanel(iId);
				eventYouAreSelected(false);
			} else if (lockstate == PanelBasis.LOCK_FOR_NEW
					|| lockstate == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
				wcoSonderTaetigkeit.setKeyOfSelectedItem(taetigkeitIId);

				if (bRechtNurBuchen == true
						|| (bRechtNurBuchen == false && bDarfKommtGehtAendern == false)) {

					if (!taetigkeitIId.equals(taetigkeitIIdEnde)) {

						// Zeit auf jetzt setzen
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(System.currentTimeMillis());
						c.set(Calendar.MILLISECOND, 0);
						wtfZeit.setTime(new java.sql.Time(c.getTimeInMillis()));
					}

				}

				eventActionSave(null, true);

			}

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_POSITION_FROM_LISTE)) {

			if (selectedBeleg != null && selectedBelegart != null) {
				if (selectedBelegart.equals(LocaleFac.BELEGART_AUFTRAG)) {

					dialogQueryAuftragpositionFromListe(e);
				} else if (selectedBelegart.equals(LocaleFac.BELEGART_LOS)) {
					dialogQueryLospositionFromListe(e);
				} else if (selectedBelegart.equals(LocaleFac.BELEGART_ANGEBOT)) {
					dialogQueryAngebotpositionFromListe(e);
				}
			} else {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), LPMain
						.getTextRespectUISPr("zeiterfassung.belegauswaehlen"));
			}
		} else if (ACTION_SPECIAL_UEBERLEITEN.equals(e.getActionCommand())) {
			DelegateFactory.getInstance().getZeiterfassungDelegate()
				.zeitdatenpruefenInZeitdatenUeberleiten(zeitdatenpruefenDto.getIId());
			setKeyWhenDetailPanel(null);
			internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.getPanelQueryZeitdatenpruefen().eventActionRefresh(e, false);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZEITDATEN;
	}

	protected void setDefaults() throws Throwable {

		if (bRechtNurBuchen == true) {
			sondertaetigkeitenMitVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenNurBDEBuchbar();
			sondertaetigkeitenOhneVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenNurBDEBuchbarOhneVersteckt();
		} else {
			sondertaetigkeitenMitVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate().getAllSprSondertaetigkeiten();
			sondertaetigkeitenOhneVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenOhneVersteckt();
		}

		// Ende
		Integer taetigkeitIId_Ende = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE).getIId();
		// Kommt
		Integer taetigkeitIId_Kommt = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT)
				.getIId();
		// Geht
		Integer taetigkeitIId_Geht = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT).getIId();

		// Telefon
		Integer taetigkeitIId_Telefon = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON)
				.getIId();

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_PERSONAL,
						ParameterFac.PARAMETER_URLAUBSANTRAG);
		boolean bUrlaubsantrag = (java.lang.Boolean) parameter
				.getCWertAsObject();

		boolean bSonderzeitenCUD = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SONDERZEITEN_CUD);

		if (bSonderzeitenCUD == false) {
			if (bUrlaubsantrag == true) {
				Iterator it = sondertaetigkeitenOhneVersteckt.keySet()
						.iterator();

				// Unter
				Integer taetigkeitIId_Unter = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_UNTER)
						.getIId();

				Set<Integer> toRemove = new HashSet<Integer>();

				while (it.hasNext()) {

					Integer taetigkeitIId = (Integer) it.next();

					if (taetigkeitIId.equals(taetigkeitIId_Ende)
							|| taetigkeitIId.equals(taetigkeitIId_Kommt)
							|| taetigkeitIId.equals(taetigkeitIId_Geht)
							|| taetigkeitIId.equals(taetigkeitIId_Unter)
							|| taetigkeitIId.equals(taetigkeitIId_Telefon)) {

					} else {
						TaetigkeitDto dtoTemp = DelegateFactory.getInstance()
								.getZeiterfassungDelegate()
								.taetigkeitFindByPrimaryKey(taetigkeitIId);

						if (!Helper.short2boolean(dtoTemp
								.getBDarfSelberBuchen())) {
							toRemove.add(taetigkeitIId);

						}
					}

				}

				Iterator itKeys = toRemove.iterator();

				while (itKeys.hasNext()) {
					sondertaetigkeitenOhneVersteckt.remove(itKeys.next());
				}

			}
		}

		if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
				.isBVonBisErfassung()) {

			if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.isBKommtGehtBuchen() == false) {

				if (sondertaetigkeitenMitVersteckt
						.containsKey(taetigkeitIId_Kommt)) {
					sondertaetigkeitenMitVersteckt.remove(taetigkeitIId_Kommt);
				}
				if (sondertaetigkeitenOhneVersteckt
						.containsKey(taetigkeitIId_Kommt)) {
					sondertaetigkeitenOhneVersteckt.remove(taetigkeitIId_Kommt);
				}
				if (sondertaetigkeitenMitVersteckt
						.containsKey(taetigkeitIId_Geht)) {
					sondertaetigkeitenMitVersteckt.remove(taetigkeitIId_Geht);
				}
				if (sondertaetigkeitenOhneVersteckt
						.containsKey(taetigkeitIId_Geht)) {
					sondertaetigkeitenOhneVersteckt.remove(taetigkeitIId_Geht);
				}

			}

			if (sondertaetigkeitenMitVersteckt.containsKey(taetigkeitIId_Ende)) {
				sondertaetigkeitenMitVersteckt.remove(taetigkeitIId_Ende);
			}
			if (sondertaetigkeitenOhneVersteckt.containsKey(taetigkeitIId_Ende)) {
				sondertaetigkeitenOhneVersteckt.remove(taetigkeitIId_Ende);
			}

			boolean hatTelefonzeiterfassung = LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_TELEFONZEITERFASSUNG);

			if (hatTelefonzeiterfassung == false) {
				if (sondertaetigkeitenMitVersteckt
						.containsKey(taetigkeitIId_Telefon)) {
					sondertaetigkeitenMitVersteckt
							.remove(taetigkeitIId_Telefon);
				}
				if (sondertaetigkeitenOhneVersteckt
						.containsKey(taetigkeitIId_Telefon)) {
					sondertaetigkeitenOhneVersteckt
							.remove(taetigkeitIId_Telefon);
				}
			}

		}

		wcoSonderTaetigkeit.setMap(sondertaetigkeitenMitVersteckt);

		boolean bHatAngebotszeiterfassung = false;
		boolean bHatProjektzeiterfassung = false;

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)
				&& LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG)) {
			bHatProjektzeiterfassung = true;
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
				&& LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG)) {
			bHatAngebotszeiterfassung = true;
		}

		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)
				&& !LPMain.getInstance().getDesktop()
						.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)
				&& !bHatAngebotszeiterfassung && !bHatProjektzeiterfassung) {

			wlaBeleg.setVisible(false);
			wbuBeleg.setVisible(false);
			wbuBeleg.getWrapperButtonGoTo().setVisible(false);
			wtfBeleg.setVisible(false);
			wbuPosition.setVisible(false);
			wtfPosition.setVisible(false);
			wbuTaetigkeit.setVisible(false);
			wtfTaetigkeit.setVisible(false);

		}

		wefKommentar.setText(null);
	}

	protected void components2Dto() throws Exception {
		zeitdatenpruefenDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto()
				.getIId());
		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		ts = Helper.cutTimestamp(ts);

		Calendar cDatum = Calendar.getInstance();
		cDatum.setTimeInMillis(ts.getTime());

		Calendar cZeit = Calendar.getInstance();
		cZeit.setTimeInMillis(wtfZeit.getTime().getTime());

		cDatum.set(Calendar.HOUR_OF_DAY, cZeit.get(Calendar.HOUR_OF_DAY));
		cDatum.set(Calendar.MINUTE, cZeit.get(Calendar.MINUTE));
		cDatum.set(Calendar.SECOND, cZeit.get(Calendar.SECOND));
		cDatum.set(Calendar.MILLISECOND, cZeit.get(Calendar.MILLISECOND));

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.isBVonBisErfassung()) {

			cDatum.set(Calendar.MILLISECOND, 0);
			ts.setTime(cDatum.getTimeInMillis() + 10);

		} else {
			ts.setTime(cDatum.getTimeInMillis());
		}

		zeitdatenpruefenDto.setTZeit(ts);
		zeitdatenpruefenDto.setCBemerkungzubelegart(wtfBemerkung.getText());

		zeitdatenpruefenDto.setCWowurdegebucht("Client: " + Helper.getPCName());

		zeitdatenpruefenDto.setXKommentar(wefKommentar.getText());

		zeitdatenpruefenDto.setCBelegartnr(selectedBelegart);
		zeitdatenpruefenDto.setIBelegartid(selectedBeleg);

		// Bis

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.isBVonBisErfassung()) {
			java.sql.Timestamp tsBis = wdfDatum.getTimestamp();
			tsBis = Helper.cutTimestamp(tsBis);

			Calendar cDatumBis = Calendar.getInstance();
			cDatumBis.setTimeInMillis(tsBis.getTime());

			Calendar cZeitBis = Calendar.getInstance();
			cZeitBis.setTimeInMillis(wtfZeit_Bis.getTime().getTime());
			cZeitBis.set(Calendar.MILLISECOND, 0);

			cDatumBis.set(Calendar.HOUR_OF_DAY,
					cZeitBis.get(Calendar.HOUR_OF_DAY));
			cDatumBis.set(Calendar.MINUTE, cZeitBis.get(Calendar.MINUTE));
			cDatumBis.set(Calendar.SECOND, cZeitBis.get(Calendar.SECOND));
			cDatumBis.set(Calendar.MILLISECOND,
					cZeitBis.get(Calendar.MILLISECOND));
			tsBis.setTime(cDatumBis.getTimeInMillis());
		}

		if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {
			zeitdatenpruefenDto.setCBelegartnr(null);
			zeitdatenpruefenDto.setIBelegartpositionid(null);
			zeitdatenpruefenDto.setTaetigkeitIId((Integer) wcoSonderTaetigkeit
					.getKeyOfSelectedItem());
		} else {
			zeitdatenpruefenDto.setTaetigkeitIId(null);

		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (zeitdatenpruefenDto.getTaetigkeitIId() != null) {
			Integer telefonIId = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON)
					.getIId();
			if (telefonIId.equals(wcoSonderTaetigkeit.getKeyOfSelectedItem())) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
				return;
			}
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		wbuKommt.setEnabled(false);
		wbuGeht.setEnabled(false);
		wbuUnter.setEnabled(false);
		wbuEnde.setEnabled(false);

		wcoSonderTaetigkeit.setKeyOfSelectedItem(zeitdatenpruefenDto
				.getTaetigkeitIId());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (zeiterfassungPruefer.pruefeObBuchungMoeglich(
				zeitdatenpruefenDto.getTZeit(), zeitdatenpruefenDto.getPersonalIId())) {

			if (zeitdatenpruefenDto.getTaetigkeitIId() != null) {
				Integer telefonIId = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.taetigkeitFindByCNr(
								ZeiterfassungFac.TAETIGKEIT_TELEFON).getIId();
				if (telefonIId.equals(wcoSonderTaetigkeit
						.getKeyOfSelectedItem())) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
					return;
				}
			}

			DelegateFactory.getInstance().getZeiterfassungDelegate()
					.removeZeitdatenpruefen(zeitdatenpruefenDto);
			aktualisiereDaten();
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, true, true);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (zeiterfassungPruefer.pruefeObBuchungMoeglich(
					zeitdatenpruefenDto.getTZeit(), zeitdatenpruefenDto.getPersonalIId())) {

				//
				try {
					if (zeitdatenpruefenDto.getTaetigkeitIId() != null) {
						Integer telefonIId = DelegateFactory
								.getInstance()
								.getZeiterfassungDelegate()
								.taetigkeitFindByCNr(
										ZeiterfassungFac.TAETIGKEIT_TELEFON)
								.getIId();
						if (telefonIId.equals(wcoSonderTaetigkeit
								.getKeyOfSelectedItem())) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
							return;
						}
					}

					if (zeitdatenpruefenDto.getIId() == null) {

					} else {
						zeitdatenpruefenDto = DelegateFactory.getInstance()
								.getZeiterfassungDelegate()
								.updateZeitdatenpruefen(zeitdatenpruefenDto);
					}

					super.eventActionSave(e, true);
					if (getInternalFrame().getKeyWasForLockMe() == null) {
						getInternalFrame().setKeyWasForLockMe(
								zeitdatenpruefenDto.getIId().toString());
					}
					
//					aktualisiereDaten();
					getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
						.getPanelSplitZeitdatenpruefen().eventYouAreSelected(false);
				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_GEHT_VOR_ENDE) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("zeiterfassung.gehtbuchungvorende"));

					} else if (ex.getICode() == EJBExceptionLP.FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("zeiterfassung.sondertaetigkeitbeenden"));
					} else {
						throw ex;
					}
				}
			}
		}
	}

	private void setZeitdatenDtoBelegart(String belegart, Integer belegIId) {
		zeitdatenpruefenDto.setCBelegartnr(belegart);
		zeitdatenpruefenDto.setIBelegartid(belegIId);
		zeitdatenpruefenDto.setArtikelIId(null);
		wtfTaetigkeit.setText(null);
		zeitdatenpruefenDto.setIBelegartpositionid(null);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		int lockstate = getLockedstateDetailMainKey().getIState();

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikel) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				com.lp.server.artikel.service.ArtikelDto artikelDto = null;
				if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
					PersonalverfuegbarkeitDto dto = DelegateFactory
							.getInstance().getPersonalDelegate()
							.personalverfuegbarkeitFindByPrimaryKey(key);
					artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(dto.getArtikelIId());
				} else {
					artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate().artikelFindByPrimaryKey(key);
				}
				zeitdatenpruefenDto.setArtikelIId(artikelDto.getIId());
				wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());

			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				wcoSonderTaetigkeit.setKeyOfSelectedItem(null);

				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);

				if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED
						|| lockstate == PanelBasis.LOCK_FOR_EMPTY) {
					neuesZeitdatenDtoFuerHeuteBelegSpeichern(
							LocaleFac.BELEGART_AUFTRAG, key, null);
				} else {

					AuftragDto auftragDto = DelegateFactory.getInstance()
							.getAuftragDelegate().auftragFindByPrimaryKey(key);
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_AUFTRAG,
							auftragDto.getIId());
					// zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
					// zeitdatenDto.setIBelegartid(auftragDto.getIId());
					wtfTaetigkeit.setText(null);

					if (bAuftragsbuchungOhnePositionbezug == true) {
						wtfPosition.setMandatoryField(false);
					} else {
						wtfPosition.setMandatoryField(true);
					}

					wtfPosition.setText(null);
					// zeitdatenDto.setIBelegartpositionid(null);

					String projBez = auftragDto.getCBezProjektbezeichnung();
					if (projBez == null) {
						projBez = "";
					}
					String kunde = DelegateFactory
							.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(
									auftragDto.getKundeIIdAuftragsadresse())
							.getPartnerDto().formatTitelAnrede();

					wlaBeleg.setText("AB" + auftragDto.getCNr());
					wtfBeleg.setText("->" + projBez + ", " + kunde);
					selectedBeleg = key;
					selectedBelegart = LocaleFac.BELEGART_AUFTRAG;
					wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
					wbuBeleg.setOKey(selectedBeleg);

					com.lp.server.auftrag.service.AuftragpositionDto[] auftragpositionDtos = DelegateFactory
							.getInstance().getAuftragpositionDelegate()
							.auftragpositionFindByAuftrag(auftragDto.getIId());

					if (auftragpositionDtos != null
							&& auftragpositionDtos.length > 0) {
						for (int i = 0; i < auftragpositionDtos.length; i++) {
							AuftragpositionDto dto = auftragpositionDtos[i];

							if (dto.getAuftragpositionstatusCNr() != null) {
								zeitdatenpruefenDto
										.setIBelegartpositionid(auftragpositionDtos[0]
												.getIId());
								wtfPosition.setText(auftragpositionDtos[0]
										.getCBez());
								if (auftragpositionDtos[0].getArtikelIId() != null
										&& auftragpositionDtos[0].getCBez() == null) {
									wtfPosition.setText(DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByPrimaryKey(
													auftragpositionDtos[0]
															.getArtikelIId())
											.formatArtikelbezeichnung());
									break;
								}

							}
						}

					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("zeiterfassung.auftragkeinepositionen"));
						return;
					}
					if (zeitdatenpruefenDto.getArtikelIId() == null) {
						if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
							Integer artikelIId = DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
											internalFrameZeiterfassung
													.getPersonalDto().getIId());
							if (artikelIId != null) {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(artikelIId);
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenpruefenDto.setArtikelIId(artikelDto.getIId());
							}
						}

						if (zeitdatenpruefenDto.getArtikelIId() == null) {

							// DEFAULT-AZ-Artikel
							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.getParametermandant(
											ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
											ParameterFac.KATEGORIE_ALLGEMEIN,
											LPMain.getTheClient().getMandant());

							if (parameter.getCWert() != null
									&& !parameter.getCWertAsObject().equals("")) {
								try {
									ArtikelDto artikelDto = DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByCNr(
													parameter.getCWert());
									wtfTaetigkeit.setText(artikelDto
											.formatArtikelbezeichnung());
									zeitdatenpruefenDto.setArtikelIId(artikelDto
											.getIId());
								} catch (Throwable ex) {
									throw new ExceptionLP(
											EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
											new Exception(
													"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
								}

							}
						}
					}
					erfuellungsgradBerechnen(null, null);

				}

				enableBisZeit(true);

			} else if (e.getSource() == panelQueryFLRLos
					|| (getInternalFrameZeiterfassung().dialogLoseEinesTechnikers != null && e
							.getSource() == getInternalFrameZeiterfassung().dialogLoseEinesTechnikers
							.getPanelQueryLose())) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				wcoSonderTaetigkeit.setKeyOfSelectedItem(null);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				wtfPosition.setMandatoryField(false);
				wtfPosition.setText(null);

				if (wtfPosition.isMandatoryField() == false
						&& (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY)) {

					neuesZeitdatenDtoFuerHeuteBelegSpeichern(
							LocaleFac.BELEGART_LOS, key, null);

					if (getInternalFrameZeiterfassung().dialogLoseEinesTechnikers != null) {
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers
								.dispose();
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = null;
					}
				} else {

					if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED
							|| lockstate == PanelBasis.LOCK_FOR_EMPTY) {
						zeitdatenpruefenDto = new ZeitdatenpruefenDto();
					}

					LosDto losDto = DelegateFactory.getInstance()
							.getFertigungDelegate().losFindByPrimaryKey(key);

					wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
					wbuBeleg.setOKey(selectedBeleg);

					setZeitdatenDtoBelegart(LocaleFac.BELEGART_LOS,
							losDto.getIId());

					String projBez = losDto.getCProjekt();
					if (projBez == null) {
						projBez = "";
					}
					wlaBeleg.setText("LO" + losDto.getCNr());
					wtfBeleg.setText("->" + projBez);

					selectedBeleg = key;
					selectedBelegart = LocaleFac.BELEGART_LOS;
					wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_FERTIGUNG_AUSWAHL);
					wbuBeleg.setOKey(selectedBeleg);

					if (zeitdatenpruefenDto.getArtikelIId() == null) {
						if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
							Integer artikelIId = DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
											internalFrameZeiterfassung
													.getPersonalDto().getIId());
							if (artikelIId != null) {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(artikelIId);
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenpruefenDto.setArtikelIId(artikelDto.getIId());
							}
						}

						if (zeitdatenpruefenDto.getArtikelIId() == null) {
							// DEFAULT-AZ-Artikel
							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.getParametermandant(
											ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
											ParameterFac.KATEGORIE_ALLGEMEIN,
											LPMain.getTheClient().getMandant());

							if (parameter.getCWert() != null
									&& !parameter.getCWertAsObject().equals("")) {
								try {
									ArtikelDto artikelDto = DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByCNr(
													parameter.getCWert());
									wtfTaetigkeit.setText(artikelDto
											.formatArtikelbezeichnung());
									zeitdatenpruefenDto.setArtikelIId(artikelDto
											.getIId());
								} catch (Throwable ex) {
									// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
								}
							}
						}
					}
					erfuellungsgradBerechnen(null, null);

					if (getInternalFrameZeiterfassung().dialogLoseEinesTechnikers != null) {
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers
								.dispose();
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = null;
					}

					if (wtfPosition.isMandatoryField() == true
							&& (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY)) {
						dialogQueryLospositionFromListe(null);
					}

				}
				enableBisZeit(true);
			} else if (e.getSource() == panelQueryFLRAngebot) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				wcoSonderTaetigkeit.setKeyOfSelectedItem(null);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED
						|| lockstate == PanelBasis.LOCK_FOR_EMPTY) {
					neuesZeitdatenDtoFuerHeuteBelegSpeichern(
							LocaleFac.BELEGART_ANGEBOT, key, null);
				} else {
					AngebotDto angebotDto = DelegateFactory.getInstance()
							.getAngebotDelegate().angebotFindByPrimaryKey(key);
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_ANGEBOT,
							angebotDto.getIId());
					wtfPosition.setMandatoryField(false);
					wtfPosition.setText(null);

					String projBez = angebotDto.getCBez();
					if (projBez == null) {
						projBez = "";
					}
					wlaBeleg.setText("AG" + angebotDto.getCNr());
					wtfBeleg.setText("->" + projBez);
					selectedBeleg = key;
					selectedBelegart = LocaleFac.BELEGART_ANGEBOT;
					wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_ANGEBOT_AUSWAHL);
					wbuBeleg.setOKey(selectedBeleg);

					if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
						Integer artikelIId = DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
										internalFrameZeiterfassung
												.getPersonalDto().getIId());
						if (artikelIId != null) {
							ArtikelDto artikelDto = DelegateFactory
									.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(artikelIId);
							wtfTaetigkeit.setText(artikelDto
									.formatArtikelbezeichnung());
							zeitdatenpruefenDto.setArtikelIId(artikelDto.getIId());
						}
					}

					if (zeitdatenpruefenDto.getArtikelIId() == null) {
						// DEFAULT-AZ-Artikel
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										LPMain.getTheClient().getMandant());

						if (parameter.getCWert() != null
								&& !parameter.getCWertAsObject().equals("")) {
							try {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByCNr(parameter.getCWert());
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenpruefenDto.setArtikelIId(artikelDto.getIId());
							} catch (Throwable ex) {
								// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
							}
						}
					}

				}
				enableBisZeit(true);
			}

			else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				wcoSonderTaetigkeit.setKeyOfSelectedItem(null);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED
						|| lockstate == PanelBasis.LOCK_FOR_EMPTY) {
					neuesZeitdatenDtoFuerHeuteBelegSpeichern(
							LocaleFac.BELEGART_PROJEKT, key, null);
				} else {

					ProjektDto projektDto = DelegateFactory.getInstance()
							.getProjektDelegate().projektFindByPrimaryKey(key);

					letzterProjektBereich = projektDto.getBereichIId();
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_PROJEKT,
							projektDto.getIId());

					wlaBeleg.setText("PJ" + projektDto.getCNr());

					wtfPosition.setMandatoryField(false);
					wtfPosition.setText(null);
					// zeitdatenDto.setIBelegartpositionid(null);

					String projBez = projektDto.getCTitel();
					if (projBez == null) {
						projBez = "";
					}

					wtfBeleg.setText("->" + projBez);
					selectedBeleg = key;
					selectedBelegart = LocaleFac.BELEGART_PROJEKT;
					wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_PROJEKT_AUSWAHL);
					wbuBeleg.setOKey(selectedBeleg);

					if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
						Integer artikelIId = DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
										internalFrameZeiterfassung
												.getPersonalDto().getIId());
						if (artikelIId != null) {
							ArtikelDto artikelDto = DelegateFactory
									.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(artikelIId);
							wtfTaetigkeit.setText(artikelDto
									.formatArtikelbezeichnung());
							zeitdatenpruefenDto.setArtikelIId(artikelDto.getIId());
						}
					}

					if (zeitdatenpruefenDto.getArtikelIId() == null
							&& bProjektMitTaetigkeiten == false) {
						// DEFAULT-AZ-Artikel
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										LPMain.getTheClient().getMandant());

						if (parameter.getCWert() != null
								&& !parameter.getCWertAsObject().equals("")) {
							try {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByCNr(parameter.getCWert());
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenpruefenDto.setArtikelIId(artikelDto.getIId());
							} catch (Throwable ex) {
								// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
							}
						}
					}

				}
				enableBisZeit(true);
			}

			else if (e.getSource() == panelQueryFLRAuftragposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AuftragpositionDto auftragpositionDto = null;
				auftragpositionDto = DelegateFactory.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey(key);
				zeitdatenpruefenDto
						.setIBelegartpositionid(auftragpositionDto.getIId());
				wtfPosition.setText(auftragpositionDto.getCBez());
				if (auftragpositionDto.getArtikelIId() != null
						&& auftragpositionDto.getCBez() == null) {
					wtfPosition.setText(DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									auftragpositionDto.getArtikelIId())
							.formatArtikelbezeichnung());
				}

				erfuellungsgradBerechnen(null, auftragpositionDto);

			} else if (e.getSource() == panelQueryFLRLosposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED
						|| lockstate == PanelBasis.LOCK_FOR_EMPTY) {
					neuesZeitdatenDtoFuerHeuteBelegSpeichern(
							LocaleFac.BELEGART_LOS,
							zeitdatenpruefenDto.getIBelegartid(), key);
				} else {

					LossollarbeitsplanDto auftragpositionDto = DelegateFactory
							.getInstance().getFertigungDelegate()
							.lossollarbeitsplanFindByPrimaryKey(key);
					zeitdatenpruefenDto.setIBelegartpositionid(auftragpositionDto
							.getIId());
					wtfPosition.setText(DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									auftragpositionDto
											.getArtikelIIdTaetigkeit())
							.formatArtikelbezeichnung());

					erfuellungsgradBerechnen(auftragpositionDto, null);
				}

			} else if (e.getSource() == panelQueryFLRAngebotposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				AngebotpositionDto angebotpositionDto = null;
				angebotpositionDto = DelegateFactory.getInstance()
						.getAngebotpositionDelegate()
						.angebotpositionFindByPrimaryKey(key);
				zeitdatenpruefenDto
						.setIBelegartpositionid(angebotpositionDto.getIId());
				wtfPosition.setText(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								angebotpositionDto.getArtikelIId())
						.formatArtikelbezeichnung());
			}
			updatePosAndTaetigkeitFelder(selectedBelegart,
					wcoSonderTaetigkeit.getKeyOfSelectedItem());

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				wtfBeleg.setText(null);
				wtfPosition.setText(null);
				zeitdatenpruefenDto.setCBelegartnr(null);
				zeitdatenpruefenDto.setIBelegartpositionid(null);
				zeitdatenpruefenDto.setArtikelIId(null);
				wtfPosition.setMandatoryField(false);
			}
			if (e.getSource() == panelQueryFLRAuftragposition) {

				wtfPosition.setText(null);

				zeitdatenpruefenDto.setIBelegartpositionid(null);

			}
		}
	}

	private void erfuellungsgradBerechnen(
			LossollarbeitsplanDto lossollarbeitsplanDto,
			AuftragpositionDto auftragpositionDto) throws ExceptionLP,
			Throwable {

		if (iOptionSollzeitpruefeung != 0) {

			if (zeitdatenpruefenDto.getCBelegartnr() != null) {
				boolean bZuvieleZeitbuchungen = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.sindZuvieleZeitdatenEinesBelegesVorhanden(
								zeitdatenpruefenDto.getCBelegartnr(),
								zeitdatenpruefenDto.getIBelegartid());
				String s = "";

				if (zeitdatenpruefenDto.getCBelegartnr()
						.equals(LocaleFac.BELEGART_LOS)) {
					s = "Soll: ";
					if (lossollarbeitsplanDto != null) {
						s += Helper.formatZahl(
								lossollarbeitsplanDto.getNGesamtzeit(), 1,
								LPMain.getTheClient().getLocUi())
								+ " Std., Ist: ";
					} else {
						// Sollzeit aller Positionen
						BigDecimal bdGesamt = new BigDecimal(0);

						LossollarbeitsplanDto[] sollarbeitsplanDtos = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.lossollarbeitsplanFindByLosIId(
										zeitdatenpruefenDto.getIBelegartid());
						for (int i = 0; i < sollarbeitsplanDtos.length; i++) {
							bdGesamt = bdGesamt.add(sollarbeitsplanDtos[i]
									.getNGesamtzeit());
						}
						s += Helper.formatZahl(bdGesamt, 1, LPMain
								.getTheClient().getLocUi())
								+ " Std., Ist: ";
					}

					if (bZuvieleZeitbuchungen == false) {
						Integer lossollarbeitsplanIId = null;
						if (lossollarbeitsplanDto != null) {
							lossollarbeitsplanIId = lossollarbeitsplanDto
									.getIId();
						}

						try {
							Double d = DelegateFactory
									.getInstance()
									.getZeiterfassungDelegate()
									.getSummeZeitenEinesBeleges(
											LocaleFac.BELEGART_LOS,
											zeitdatenpruefenDto.getIBelegartid(),
											lossollarbeitsplanIId, null, null,
											null);
							s += Helper.formatZahl(d, 1, LPMain.getTheClient()
									.getLocUi())
									+ " Std.";
						} catch (ExceptionLP e) {
							if (e.getICode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
							} else {
								handleException(e, false);
							}
						}
					} else {
						s += "? Std.";
					}
				} else if (zeitdatenpruefenDto.getCBelegartnr().equals(
						LocaleFac.BELEGART_AUFTRAG)) {
					s = "Soll: ";
					if (iOptionSollzeitpruefeung > 1) {
						if (auftragpositionDto != null
								&& iOptionSollzeitpruefeung == 2) {
							s += Helper.formatZahl(auftragpositionDto
									.getNMenge(), 1, LPMain.getTheClient()
									.getLocUi())
									+ " Std., Ist: ";
						} else {
							// Sollzeit aller Positionen
							BigDecimal bdGesamt = new BigDecimal(0);

							AuftragpositionDto[] sollarbeitsplanDtos = DelegateFactory
									.getInstance()
									.getAuftragpositionDelegate()
									.auftragpositionFindByAuftrag(
											zeitdatenpruefenDto.getIBelegartid());
							for (int i = 0; i < sollarbeitsplanDtos.length; i++) {

								if (sollarbeitsplanDtos[i].getArtikelIId() != null) {
									ArtikelDto aDto = DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByPrimaryKey(
													sollarbeitsplanDtos[i]
															.getArtikelIId());
									if (aDto.getArtikelartCNr().equals(
											ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
										bdGesamt = bdGesamt
												.add(sollarbeitsplanDtos[i]
														.getNMenge());
									}
								}
							}
							s += Helper.formatZahl(bdGesamt, 1, LPMain
									.getTheClient().getLocUi())
									+ " Std., Ist: ";
						}

						if (bZuvieleZeitbuchungen == false) {
							Integer auftragpositionIId = null;
							if (auftragpositionDto != null
									&& iOptionSollzeitpruefeung == 2) {
								auftragpositionIId = auftragpositionDto
										.getIId();
							}

							try {
								Double d = DelegateFactory
										.getInstance()
										.getZeiterfassungDelegate()
										.getSummeZeitenEinesBeleges(
												LocaleFac.BELEGART_AUFTRAG,
												zeitdatenpruefenDto.getIBelegartid(),
												auftragpositionIId, null, null,
												null);
								s += Helper.formatZahl(d, 1, LPMain
										.getTheClient().getLocUi())
										+ " Std.";
							} catch (ExceptionLP e) {
								if (e.getICode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
									DialogFactory.showModalDialog(LPMain
											.getTextRespectUISPr("lp.error"),
											LPMain.getInstance().getMsg(e));
								} else {
									handleException(e, false);
								}
							}
						} else {
							s += "? Std.";
						}
					}
				}
			}
		}
	}

	void wbuTagZurueck_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	private void setzteKalenderWochewochentag() throws Throwable {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(wdfDatum.getDate());
		int kw = cal.get(java.util.Calendar.WEEK_OF_YEAR);

		// Kurze Tagebezeichnungen holen
		String[] kurzeWochentage = new DateFormatSymbols(LPMain.getTheClient()
				.getLocUi()).getWeekdays();

		wlaKalenderwochewochentag.setText(kurzeWochentage[cal
				.get(Calendar.DAY_OF_WEEK)]
				+ ", "
				+ LPMain.getTextRespectUISPr("lp.kalenderwoche_kurz")
				+ " "
				+ kw);
	}

	void wbuNaechsterTag_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	public void aktualisiereDaten() throws Throwable {
		java.sql.Date dDate = wdfDatum.getDate();
		java.sql.Timestamp tTimestamp = new java.sql.Timestamp(dDate.getTime());
		if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung() != null) {
			FilterKriterium[] fk = ZeiterfassungFilterFactory.getInstance()
					.createFKZeitdatenpruefenZuPersonal(
							getInternalFrameZeiterfassung().getPersonalDto()
									.getIId());
			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.getPanelQueryZeitdatenpruefen().setDefaultFilter(fk);
		}
		setzteKalenderWochewochentag();

		if (bRechtNurBuchen == true) {
			LPButtonAction o = (LPButtonAction) getInternalFrameZeiterfassung()
					.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten()
					.getHmOfButtons().get(PanelBasis.ACTION_NEW);
			if (Helper.cutTimestamp(
					new java.sql.Timestamp(System.currentTimeMillis())).equals(
					Helper.cutTimestamp(tTimestamp))) {
				o.getButton().setVisible(true);
			} else {
				o.getButton().setVisible(false);
			}
		}

		try {
			BetriebskalenderDto dto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.betriebskalenderFindByMandantCNrDDatum(tTimestamp);
			if (dto != null) {
				wlaBetriebskalender.setText(dto.getCBez());
			} else {
				wlaBetriebskalender.setText("");
			}
		} catch (ExceptionLP ex1) {
			wlaBetriebskalender.setText("");
			// Kein Betriebskalender-Eintrag
		}

		try {
			SonderzeitenDto[] dtos = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.sonderzeitenFindByPersonalIIdDDatum(
							getInternalFrameZeiterfassung().getPersonalDto()
									.getIId(), Helper.cutTimestamp(tTimestamp));
			if (dtos != null && dtos.length > 0) {
				String sSonder = LPMain
						.getTextRespectUISPr("zeiterfassung.title.tab.sonderzeiten")
						+ ":";
				sSonder += " "
						+ DelegateFactory
								.getInstance()
								.getZeiterfassungDelegate()
								.taetigkeitFindByPrimaryKey(
										dtos[0].getTaetigkeitIId())
								.getBezeichnung();
				if (Helper.short2boolean(dtos[0].getBTag()) == true) {
					sSonder += " "
							+ LPMain.getTextRespectUISPr("zeiterfassung.ganztags");
				} else if (Helper.short2boolean(dtos[0].getBHalbtag()) == true) {
					sSonder += " "
							+ LPMain.getTextRespectUISPr("zeiterfassung.halbtags");
				} else {
					sSonder += " "
							+ dtos[0].getUStunden().toString().substring(0, 5)
							+ " " + LPMain.getTextRespectUISPr("lp.stunden");

				}

				wlaSonderzeiten.setText(sSonder);
			} else {
				wlaSonderzeiten.setText("");
			}
		} catch (ExceptionLP ex1) {
			wlaBetriebskalender.setText("");
			// Kein Betriebskalender-Eintrag
		}

		berechneTageszeit();
	}

	protected void berechneTageszeit() throws Throwable {
		java.sql.Date dDate = wdfDatum.getDate();

		Double dAnwesenheitszeit = null;
		Double dArbeitszeitzeit = null;
		Double dSollzeit = null;
		try {

			if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
					.isBVonBisErfassung() == true
					&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
							.isBKommtGehtBuchen() == false) {
				VonBisErfassungTagesdatenDto vbDto = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.berechneTagesArbeitszeitVonBisZeiterfassungOhneKommtGeht(
								internalFrameZeiterfassung.getPersonalDto()
										.getIId(), dDate);

				dAnwesenheitszeit = vbDto.getdIst();

			} else {
				dAnwesenheitszeit = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.berechneTagesArbeitszeit(
								internalFrameZeiterfassung.getPersonalDto()
										.getIId(), dDate);

				dArbeitszeitzeit = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.berechneTagesArbeitszeit(
								internalFrameZeiterfassung.getPersonalDto()
										.getIId(), dDate, false);

			}

		} catch (ExceptionLP ex) {
		}

		if (dAnwesenheitszeit == null) {
			dAnwesenheitszeit = 0D;
		}
		if (dArbeitszeitzeit == null) {
			dArbeitszeitzeit = 0D;
		}
		if (dSollzeit == null) {
			dSollzeit = 0D;
		}
	}

	public void wcbRelativ_propertyChange(PropertyChangeEvent evt) {

	}

	public void wcbRelativ_stateChanged(ChangeEvent e) {
		// wtfZeit.requestFocus();

	}

	public void wcbRelativ_actionPerformed(ActionEvent e) {
		wtfZeit.requestFocus();
	}

	public void wcoSonderTaetigkeit_actionPerformed(ActionEvent e) {

		int lockstate = -99;
		try {
			lockstate = getLockedstateDetailMainKey().getIState();

		} catch (Throwable e1) {
			handleException(e1, false);
		}

		if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {

			wbuKommt.setEnabled(true);
			wbuGeht.setEnabled(true);
			wbuUnter.setEnabled(true);
			wbuEnde.setEnabled(true);

			wbuAngebot.setEnabled(true);
			wbuAuftrag.setEnabled(true);
			wbuProjekt.setEnabled(true);
			wbuLos.setEnabled(true);
			wbuPosition.setEnabled(false);
			wbuTaetigkeit.setEnabled(false);

			wtfPosition.setMandatoryField(false);
			wtfBeleg.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);
			if (zeitdatenpruefenDto != null) {
				zeitdatenpruefenDto.setCBelegartnr(null);
				zeitdatenpruefenDto.setIBelegartpositionid(null);
				zeitdatenpruefenDto.setIBelegartid(null);
				zeitdatenpruefenDto.setArtikelIId(null);
			}

			wtfBeleg.setText(null);
			wtfPosition.setText(null);
			wlaBeleg.setText(null);
			wtfTaetigkeit.setText(null);

			// wtfBeleg.setMandatoryField(false);
			// wtfPosition.setMandatoryField(false);
			// wtfTaetigkeit.setMandatoryField(false);

			// Nur Wenn Kommt geht nicht sichtbar

			if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
					taetigkeitIIdKommt)
					|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
							taetigkeitIIdGeht)) {
				enableBisZeit(false);
			} else {
				enableBisZeit(true);
			}

		} else {
			wtfPosition.setMandatoryField(true);
			wtfBeleg.setMandatoryField(true);
			wtfTaetigkeit.setMandatoryField(true);

			if (bDarfKommtGehtAendern == false
					&& isHeute() == false
					&& (lockstate == PanelBasis.LOCK_IS_LOCKED_BY_ME || lockstate == PanelBasis.LOCK_FOR_NEW)) {
				wbuKommt.setEnabled(false);
				wbuGeht.setEnabled(false);
				wbuUnter.setEnabled(false);
				wcoSonderTaetigkeit.setEnabled(false);
			} else {
				wbuKommt.setEnabled(true);
				wbuGeht.setEnabled(true);
				wbuUnter.setEnabled(true);
			}

			wbuEnde.setEnabled(true);

			wbuAngebot.setEnabled(true);
			wbuAuftrag.setEnabled(true);
			wbuProjekt.setEnabled(true);
			wbuLos.setEnabled(true);
			// hier relativ UND bisZeit sichtbar machen?
			// wtfZeit_Bis.setVisible(true);
			enableBisZeit(true);

			if (lockstate == PanelBasis.LOCK_FOR_NEW
					|| lockstate == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
				wbuPosition.setEnabled(true);
				wbuTaetigkeit.setEnabled(true);
			}
		}

		if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED
				|| lockstate == PanelBasis.LOCK_FOR_EMPTY) {

			if (isHeute()) {

				wbuKommt.setEnabled(true);
				wbuGeht.setEnabled(true);
				wbuUnter.setEnabled(true);
				wbuEnde.setEnabled(true);

				if (!getInternalFrameZeiterfassung()
						.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {

					wbuAngebot.setEnabled(true);
					wbuAuftrag.setEnabled(true);
					wbuProjekt.setEnabled(true);
					wbuLos.setEnabled(true);
				} else {
					wbuAngebot.setEnabled(false);
					wbuAuftrag.setEnabled(false);
					wbuProjekt.setEnabled(false);
					wbuLos.setEnabled(false);
				}
			} else {
				wbuKommt.setEnabled(false);
				wbuGeht.setEnabled(false);
				wbuUnter.setEnabled(false);
				wbuEnde.setEnabled(false);

				wbuAngebot.setEnabled(false);
				wbuAuftrag.setEnabled(false);
				wbuProjekt.setEnabled(false);
				wbuLos.setEnabled(false);
			}

		}

		if (bDarfKommtGehtAendern == false) {

			if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {

				try {

					if (!wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
							taetigkeitIIdEnde)) {
						wtfZeit.setEnabled(false);
						wtfZeit.setTime(new java.sql.Time(System
								.currentTimeMillis()));
					} else {
						wtfZeit.setEnabled(true);
					}
				} catch (Throwable e1) {
					handleException(e1, true);
				}

			}

		}
		updatePosAndTaetigkeitFelder(selectedBelegart,
				wcoSonderTaetigkeit.getKeyOfSelectedItem());

	}

	protected void calcBis() throws ExceptionLP {
		long time = wtfZeit.getTime().getTime() + wdfDauer.getDuration();
		wtfZeit_Bis.setTime(new Time(time));
		if (wtfZeit_Bis.getTime().before(wtfZeit.getTime()))
			wdfDauer.setDuration(0);
	}

	protected void calcDauer() throws ExceptionLP {
		long duration = wtfZeit_Bis.getTime().getTime()
				- wtfZeit.getTime().getTime();
		duration = Math.round(duration / 60000f) * 60000;
		wdfDauer.setDuration(duration);
	}

	private class VonBisDauerListener implements IHvValueHolderListener {

		@Override
		public void valueChanged(Component reference, Object oldValue,
				Object newValue) {
			try {
				if (reference == wdfDauer || reference == wtfZeit) {
					calcBis();
				} else if (reference == wtfZeit_Bis) {
					calcDauer();
				}
			} catch (ExceptionLP e) {
				handleException(e, true);
			}
		}

	}
}


class PanelZeitdatenpruefen_wcoSonderTaetigkeit_actionAdapter implements ActionListener {
	private PanelZeitdatenpruefen adaptee;

	PanelZeitdatenpruefen_wcoSonderTaetigkeit_actionAdapter(PanelZeitdatenpruefen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoSonderTaetigkeit_actionPerformed(e);
	}
}

