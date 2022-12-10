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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.text.MessageFormat;
import java.util.ArrayList;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.IHvValueHolderListener;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSlider;
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
import com.lp.server.angebot.service.AngebotauftragDto;
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
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalverfuegbarkeitDto;
import com.lp.server.personal.service.PersonalzeitmodellDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.VonBisErfassungTagesdatenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFavoritenDto;
import com.lp.server.personal.service.ZeitmodellDto;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.personal.service.ZeitverteilungDto;
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

public class PanelZeitdaten extends PanelBasis implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private ZeitdatenDto zeitdatenDto = null;
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
	private PanelQueryFLR panelQueryFLRAngebotAuftrag = null;

	private PanelQueryFLR panelQueryMaschinenZeitdaten = null;

	private Integer letzterProjektBereich = null;

	private boolean bRechtNurBuchen = true;
	private boolean bDarfKommtGehtAendern = true;
	private WrapperLabel wlaZeit = new WrapperLabel();
	private WrapperTimeField wtfZeit = new WrapperTimeField();
	private WrapperTimeField wtfZeit_Bis = new WrapperTimeField();
	private WrapperDurationField wdfDauer = new WrapperDurationField();
	private WrapperLabel wlaDauer;
	private WrapperComboBox wcoSonderTaetigkeit = new WrapperComboBox();
	private WrapperComboBox wcbVerrechenbar = createWcbVerrechenbar();
	private boolean bProjektMitTaetigkeiten = false;

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperLabel wlaBetriebskalender = new WrapperLabel();
	private WrapperLabel wlaSonderzeiten = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();
	private JButton wbuTagZurueck = ButtonFactory.createJButton();
	private JButton wbuNaechsterTag = ButtonFactory.createJButton();
	private WrapperCheckBox wcbRelativ = new WrapperCheckBox();
	static final public String ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE = "action_taetigkeit_from_liste";

	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";
	static final public String ACTION_SPECIAL_ANGEBOT_FROM_LISTE = "action_angebot_from_liste";
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_los_from_liste";
	static final public String ACTION_SPECIAL_PROJEKT_FROM_LISTE = "action_projekt_from_liste";

	private PanelQueryFLR panelQueryFLRMaschine = null;
	static final public String ACTION_SPECIAL_MASCHINE_FROM_LISTE = "action_maschine_from_liste";
	private WrapperButton wbuMaschine = new WrapperButton();
	private WrapperTextField wtfMaschine = new WrapperTextField();

	static final public String ACTION_SPECIAL_POSITION_FROM_LISTE = "action_position_from_liste";
	static final public String ACTION_SPECIAL_ZEITMODELL_FROM_LISTE = "ACTION_SPECIAL_ZEITMODELL_FROM_LISTE";

	static final public String ACTION_SPECIAL_KOMMT = "action_special_kommt";
	static final public String ACTION_SPECIAL_GEHT = "action_special_geht";
	static final public String ACTION_SPECIAL_UNTER = "action_special_unter";
	static final public String ACTION_SPECIAL_ENDE = "action_special_ende";

	public final static String MY_OWN_NEW_GOTO_MASCHINENZEIT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_GOTO_MASCHINENZEIT";

	private WrapperLabel wlaKalenderwochewochentag = new WrapperLabel();
	private WrapperLabel wlaTagesarbeitszeit = new WrapperLabel();
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

	private WrapperButton wbuKommt = new WrapperButton();
	private WrapperButton wbuGeht = new WrapperButton();
	private WrapperButton wbuUnter = new WrapperButton();
	private WrapperButton wbuEnde = new WrapperButton();

	private WrapperButton wbuProjekt = new WrapperButton();
	private WrapperButton wbuAngebot = new WrapperButton();
	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperButton wbuLos = new WrapperButton();

	private WrapperLabel wlaErfuellungsgrad = new WrapperLabel();

	private boolean hatModulStueckrueckmeldung = false;
	private boolean bLosbuchungOhnePositionbezug = false;
	private boolean bAuftragsbuchungOhnePositionbezug = false;
	private boolean bLosAGIstPflichtfeld = false;

	private WrapperEditorField wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(),
			LPMain.getTextRespectUISPr("lp.bemerkung"));

	private WrapperEditorField wefKommentarIntern = new WrapperEditorFieldKommentar(getInternalFrame(),
			LPMain.getTextRespectUISPr("lp.bemerkung"));

	private JButton wbuZeitmodell = ButtonFactory.createJButton();
	private WrapperTextField wtfZeitmodell = new WrapperTextField();
	private int iOptionSollzeitpruefeung = 0;

	private WrapperLabel wlaFehlerInZeitdaten = new WrapperLabel();
	private JButton wlaOffeneZeitverteilung = new JButton();

	private Integer zeitmodellIId = null;
	private boolean bZeitdatenAufErledigteBuchbar = false;
	private boolean bZeitdatenAufAngelegteLoseBuchbar = false;

	Integer taetigkeitIIdKommt = null;
	Integer taetigkeitIIdUnter = null;
	Integer taetigkeitIIdGeht = null;
	Integer taetigkeitIIdEnde = null;

	private Map<Integer, String> sondertaetigkeitenOhneVersteckt;
	private Map<Integer, String> sondertaetigkeitenMitVersteckt;

	private boolean bArbeitszeitartikelauspersonalverfuegbarkeit = false;
	private boolean bArbeitszeitartikelausAuftragsposition = false;

	private boolean bAG_AB_Gemeinsam = false;

	private ZeiterfassungPruefer zeiterfassungPruefer = null;

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfZeit;
	}

	public WrapperDateField getWdfDatum() {
		return wdfDatum;
	}

	public InternalFrameZeiterfassung getInternalFrameZeiterfassung() {
		return internalFrameZeiterfassung;
	}

	public PanelZeitdaten(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);

		hatModulStueckrueckmeldung = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKRUECKMELDUNG);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_LOSBUCHUNG_OHNE_POSITIONSBEZUG_BEI_STUECKRUECK,
						ParameterFac.KATEGORIE_PERSONAL, LPMain.getTheClient().getMandant());

		bLosbuchungOhnePositionbezug = (Boolean) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_AUFTRAGSBUCHUNG_OHNE_POSITIONSBEZUG, ParameterFac.KATEGORIE_PERSONAL,
				LPMain.getTheClient().getMandant());

		bAuftragsbuchungOhnePositionbezug = (Boolean) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_LOS_ARBEITSGANG_IST_PFLICHT, ParameterFac.KATEGORIE_PERSONAL,
				LPMain.getTheClient().getMandant());

		bLosAGIstPflichtfeld = (Boolean) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_PROJEKT_MIT_TAETIGKEIT, ParameterFac.KATEGORIE_PROJEKT,
				LPMain.getTheClient().getMandant());

		bProjektMitTaetigkeiten = (Boolean) parameter.getCWertAsObject();

		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;

		bRechtNurBuchen = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITEINGABE_NUR_BUCHEN);
		bDarfKommtGehtAendern = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_DARF_KOMMT_GEHT_AENDERN);
		taetigkeitIIdKommt = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT).getIId();
		taetigkeitIIdGeht = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT).getIId();
		taetigkeitIIdUnter = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_UNTER).getIId();
		taetigkeitIIdEnde = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE).getIId();
		zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());

		jbInit();
		setDefaults();
		initComponents();

	}

	public void propertyChange(PropertyChangeEvent e) {
		System.out.println(e.getPropertyName());
		if (e.getSource() == wdfDatum.getDisplay() && e.getNewValue() instanceof Date
				&& e.getPropertyName().equals("date")) {
			wdfDatum.setDate((Date) e.getNewValue());
			try {

				// wrbSondertaetigkeit.setSelected(true);

				aktualisiereDaten();

				Object sKey = getInternalFrame().getKeyWasForLockMe();

				getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung().refreshQPZeitdaten(
						getInternalFrameZeiterfassung().getPersonalDto().getIId(), wdfDatum.getDate(),
						LPMain.getLockMeForNew().equals(sKey));

			} catch (Throwable ex) {
				// brauche ich
				handleException(ex, false);
			}
		}
	}

	public boolean isHeute0() {

		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		ts = Helper.cutTimestamp(ts);

		java.sql.Timestamp tHeute = Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

		if (tHeute.getTime() == ts.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isHeute() {
		Timestamp ts = Helper.cutTimestamp(wdfDatum.getTimestamp());
		Timestamp tsHeute = Helper.cutTimestamp(getNowZeroSecondsAsTimestamp());
		return ts.getTime() == tsHeute.getTime();
	}

	private void gotoMaschinenzeitdaten(Integer maschinenzeitdatenIId) throws Throwable {

		InternalFrameZeiterfassung ifAB = (InternalFrameZeiterfassung) LPMain.getInstance().getDesktop()
				.holeModul(LocaleFac.BELEGART_ZEITERFASSUNG);

		MaschinenzeitdatenDto bDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.maschinenzeitdatenFindByPrimaryKey(maschinenzeitdatenIId);

		ifAB.geheZu(ifAB.IDX_TABBED_PANE_MASCHINEN, TabbedPaneMaschinen.IDX_PANEL_ZEITDATEN,
				bDto.getMaschineIId(), maschinenzeitdatenIId,
				ZeiterfassungFilterFactory.getInstance().createFKPersonalKey(bDto.getMaschineIId()));

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		wlaErfuellungsgrad.setText(null);
		wlaBeleg.setText(null);
		// Wenn nicht heute

		wbuKommt.setEnabled(false);
		wbuGeht.setEnabled(false);
		wbuUnter.setEnabled(false);
		wbuEnde.setEnabled(false);

		wbuAngebot.setEnabled(false);
		wbuAuftrag.setEnabled(false);
		wbuProjekt.setEnabled(false);
		wbuLos.setEnabled(false);

		if (isHeute()) {
			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD)) {
				wbuKommt.setEnabled(true);
				wbuGeht.setEnabled(true);
				wbuUnter.setEnabled(true);
				wbuEnde.setEnabled(true);

				wbuAngebot.setEnabled(true);
				wbuAuftrag.setEnabled(true);
				wbuProjekt.setEnabled(true);
				wbuLos.setEnabled(true);
			}
		}

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
					if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdGeht)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdKommt)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdUnter)) {
						wtfZeit.setEnabled(false);
					} else {
						wtfZeit.setEnabled(true);
					}
				}

			}
			if (wcoSonderTaetigkeit.getKeyOfSelectedItem() == null) {
				wcbRelativ.setEnabled(true);
			} else {
				wcbRelativ.setEnabled(false);
			}
		} else {
			zeitdatenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.zeitdatenFindByPrimaryKey((Integer) getKeyWhenDetailPanel());

			if (zeitdatenDto.getCBelegartnr() != null) {

				if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {
					enableBisZeit(true);
				} else {
					wcbRelativ.setVisible(true);
				}

			} else {
				if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {

					if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null
							&& (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdKommt)
									|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdGeht))) {

						enableBisZeit(false);
					} else {
						enableBisZeit(true);
					}
				} else {
					wcbRelativ.setVisible(false);
				}
			}

			dto2Components();
			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung().refreshTitle();

			if (bDarfKommtGehtAendern == false) {

				if (zeitdatenDto.getTaetigkeitIId() != null) {

					if (!zeitdatenDto.getTaetigkeitIId().equals(taetigkeitIIdEnde)) {
						LPButtonAction o = getHmOfButtons().get(PanelBasis.ACTION_UPDATE);
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

			if (getHmOfButtons().containsKey(MY_OWN_NEW_GOTO_MASCHINENZEIT)) {
				LPButtonAction o = getHmOfButtons().get(MY_OWN_NEW_GOTO_MASCHINENZEIT);
				if (o != null) {
					o.getButton().setEnabled(false);

					if (zeitdatenDto.getCBelegartnr() != null
							&& zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)
							&& zeitdatenDto.getIBelegartpositionid() != null) {

						ArrayList alIds = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.getMaschinenzeitenEinesTagesEinesAGsUndEinerPerson(
										internalFrameZeiterfassung.getPersonalDto().getIId(),
										zeitdatenDto.getIBelegartpositionid(), wdfDatum.getDate());

						if (alIds.size() > 0) {
							o.getButton().setEnabled(true);
						}

					}

				}
			}

		}
		berechneTageszeit();
		wdfDatum.setEnabled(true);
		aktualisiereSichtbarkeitMaschine();

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		// SP8418
		if (lockStateValue.getIState() == PanelBasis.LOCK_IS_NOT_LOCKED) {
			if (bDarfKommtGehtAendern == false) {
				if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {
					if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdGeht)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdKommt)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdUnter)) {
						lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					}
				}
			}
		}
		return lockStateValue;
	}

	private void enableBisZeit(boolean b) {
		wtfZeit_Bis.setVisible(b);
		wtfZeit_Bis.setMandatoryField(b);
		wdfDauer.setVisible(b);
		wdfDauer.setMandatoryField(b);
		wlaDauer.setVisible(b);
	}

	public void erstelleVorschlagFuerZeitbuchung(ZeiterfassungFavoritenDto zfDto) throws Throwable {
		zeitdatenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.zeitdatenFindByPrimaryKey(zfDto.getZeitdatenIId());

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
		zeitdatenDto.setTZeit(ts);
		zeitdatenDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		zeitdatenDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		if (zfDto.getArtikelIId() != null) {
			zeitdatenDto.setArtikelIId(zfDto.getArtikelIId());
		}
		
		
		//SP9875
		zeitdatenDto.setPersonalIIdErledigt(null);
		zeitdatenDto.setTErledigt(null);
		zeitdatenDto.setFDauerUebersteuert(null);
		zeitdatenDto.setBAutomatikbuchung(Helper.boolean2Short(false));
		
		
		
		
		dto2Components();

		zeitdatenDto.setIId(null);

	}

	protected void dto2Components() throws Throwable {
		wtfZeit.setTime(new java.sql.Time(zeitdatenDto.getTZeit().getTime()));
		wefKommentar.setText(zeitdatenDto.getXKommentar());
		wefKommentarIntern.setText(zeitdatenDto.getXKommentarIntern());

		if (zeitdatenDto.getMaschineIId() != null) {

			MaschineDto maschineDto = null;
			maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.maschineFindByPrimaryKey(zeitdatenDto.getMaschineIId());
			zeitdatenDto.setMaschineIId(maschineDto.getIId());
			wtfMaschine.setText(maschineDto.getBezeichnung());

		} else {
			wtfMaschine.setText(null);
		}

		if (zeitdatenDto.gettZeit_Bis() != null) {
			wtfZeit_Bis.setTime(new java.sql.Time(zeitdatenDto.gettZeit_Bis().getTime()));
			wdfDauer.setDuration(zeitdatenDto.gettZeit_Bis().getTime() - zeitdatenDto.getTZeit().getTime());
		}

		if (zeitdatenDto.getTaetigkeitIId() != null) {

			jpaWorkingOn.repaint();

			TaetigkeitDto taetigkeit = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.taetigkeitFindByPrimaryKey(zeitdatenDto.getTaetigkeitIId());
			if (taetigkeit != null && Helper.short2boolean(taetigkeit.getBVersteckt())) {
				wcoSonderTaetigkeit.setMap(sondertaetigkeitenMitVersteckt);
			} else {
				wcoSonderTaetigkeit.setMap(sondertaetigkeitenOhneVersteckt);
			}
			wcoSonderTaetigkeit.setKeyOfSelectedItem(zeitdatenDto.getTaetigkeitIId());
			wtfTaetigkeit.setText(null);
			wtfBeleg.setText(null);
			wtfPosition.setText(null);

			wtfPosition.setMandatoryField(false);
			wtfBeleg.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);

			wbuBeleg.setActivatable(false);
			wcbRelativ.setActivatable(false);
			wbuPosition.setActivatable(false);
			wbuTaetigkeit.setActivatable(false);
			wtfBemerkung.setText(zeitdatenDto.getCBemerkungZuBelegart());

			// PJ20931
			if (taetigkeit.getCNr().equals(ZeiterfassungFac.TAETIGKEIT_TELEFON)) {

				try {
					TelefonzeitenDto tzDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.telefonzeitenFindByPersonalIIdTVon(zeitdatenDto.getPersonalIId(), zeitdatenDto.getTZeit());
					wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN);
					wbuBeleg.setOKey(tzDto.getIId());
					wlaBeleg.setText(taetigkeit.getBezeichnung());
				} catch (Throwable e) {
				}

			}

		} else {
			selectedBelegart = zeitdatenDto.getCBelegartnr();
			wbuBeleg.setOKey(zeitdatenDto.getIBelegartid());
			wcoSonderTaetigkeit.setKeyOfSelectedItem(null);

			wcbVerrechenbar.setKeyOfSelectedItem(zeitdatenDto.getFVerrechenbar());

			if (wcbRelativ.isSelected()) {
				java.sql.Time zeit = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.getRelativeZeitFuerRelativesAendernAmClient(zeitdatenDto.getPersonalIId(),
								zeitdatenDto.getTZeit());
				wtfZeit.setTime(zeit);
			}

			if (zeitdatenDto.getArtikelIId() != null) {

				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(zeitdatenDto.getArtikelIId());

				wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
			}
			wtfBemerkung.setText(zeitdatenDto.getCBemerkungZuBelegart());

			if (zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
				wtfPosition.setText(null);

				AuftragpositionDto auftragpositionDto = null;
				try {
					auftragpositionDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
							.auftragpositionFindByPrimaryKey(zeitdatenDto.getIBelegartpositionid());

					// PJ21782 Artiklenummer aus AB-Position anzeigen
					if (auftragpositionDto.getArtikelIId() != null) {
						ArtikelDto artikelDtoABPos = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId());
						if (auftragpositionDto.getCBez() != null
								&& !artikelDtoABPos.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
							wtfPosition.setText(artikelDtoABPos.getCNr() + " " + auftragpositionDto.getCBez());
						} else if (auftragpositionDto.getCBez() == null
								&& !artikelDtoABPos.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
							wtfPosition.setText(artikelDtoABPos.getCNr());
						}

					} else {
						wtfPosition.setText(auftragpositionDto.getCBez());
					}

				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								(LPMain.getTextRespectUISPr("zeiterfassung.auftragspositiongeloescht")));
					} else {
						handleException(ex, false);
					}
				}

				erfuellungsgradBerechnen(null, auftragpositionDto);

				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(zeitdatenDto.getIBelegartid());

				String sProjBez = "";
				if (auftragDto.getCBezProjektbezeichnung() != null) {
					sProjBez = ", " + auftragDto.getCBezProjektbezeichnung();
				}

				String kunde = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse()).getPartnerDto()
						.formatTitelAnrede();
				wlaBeleg.setText("AB" + auftragDto.getCNr());
				wtfBeleg.setText("->" + sProjBez + ", " + kunde);
				selectedBeleg = auftragDto.getIId();
				selectedBelegart = LocaleFac.BELEGART_AUFTRAG;
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
				if (auftragpositionDto != null) {
					zeitdatenDto.setIBelegartpositionid(auftragpositionDto.getIId());
					if (auftragpositionDto.getArtikelIId() != null && auftragpositionDto.getCBez() == null) {
						wtfPosition.setText(DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId())
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

				wcbRelativ.setActivatable(true);

				wbuBeleg.setActivatable(true);

				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)) {
				wtfPosition.setText(null);

				if (zeitdatenDto.getIBelegartpositionid() != null) {
					LossollarbeitsplanDto lospositionDto = null;
					try {
						lospositionDto = DelegateFactory.getInstance().getFertigungDelegate()
								.lossollarbeitsplanFindByPrimaryKey(zeitdatenDto.getIBelegartpositionid());
						wtfPosition.setText(lospositionDto.getIId() + "");
					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("zeiterfassung.lospositiongeloescht"));
						} else {
							handleException(ex, false);
						}
					}
					if (lospositionDto != null) {
						zeitdatenDto.setIBelegartpositionid(lospositionDto.getIId());
						if (lospositionDto.getArtikelIIdTaetigkeit() != null) {
							wtfPosition.setText(DelegateFactory.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(lospositionDto.getArtikelIIdTaetigkeit()).getCNr());
						}

					}
					erfuellungsgradBerechnen(lospositionDto, null);
				} else {

					erfuellungsgradBerechnen(null, null);
				}
				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(zeitdatenDto.getIBelegartid());

				String sProjBez = "";
				if (losDto.getCProjekt() != null) {
					sProjBez = losDto.getCProjekt();
				}
				wlaBeleg.setText(losDto.getCNr());
				wtfBeleg.setText("->" + sProjBez);
				selectedBeleg = losDto.getIId();
				selectedBelegart = LocaleFac.BELEGART_LOS;
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_FERTIGUNG_AUSWAHL);

				if (!hatModulStueckrueckmeldung) {
					if (bLosAGIstPflichtfeld == true) {
						wtfPosition.setMandatoryField(true);

					} else {
						wtfPosition.setMandatoryField(false);
					}
				} else {
					if (bLosbuchungOhnePositionbezug == true) {
						wtfPosition.setMandatoryField(false);
					} else {
						wtfPosition.setMandatoryField(true);
					}
				}
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);

				wcbRelativ.setActivatable(true);

				wbuBeleg.setActivatable(true);

				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_PROJEKT)) {
				wtfPosition.setText(null);

				ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate()
						.projektFindByPrimaryKey(zeitdatenDto.getIBelegartid());

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

				wcbRelativ.setActivatable(true);

				wbuPosition.setActivatable(false);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_ANGEBOT)) {
				wtfPosition.setText(null);

				if (zeitdatenDto.getIBelegartpositionid() != null) {
					AngebotpositionDto angebotpositionDto = null;
					try {
						angebotpositionDto = DelegateFactory.getInstance().getAngebotpositionDelegate()
								.angebotpositionFindByPrimaryKey(zeitdatenDto.getIBelegartpositionid());

					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("zeiterfassung.angebotpositiongeloescht"));
						} else {
							handleException(ex, false);
						}
					}
					if (angebotpositionDto != null) {
						zeitdatenDto.setIBelegartpositionid(angebotpositionDto.getIId());
						if (angebotpositionDto.getArtikelIId() != null) {
							wtfPosition.setText(angebotpositionDto.getCBez());
							if (angebotpositionDto.getArtikelIId() != null && angebotpositionDto.getCBez() == null) {
								wtfPosition.setText(DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(angebotpositionDto.getArtikelIId())
										.formatArtikelbezeichnung());
							}

						}
					}

				}
				AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate()
						.angebotFindByPrimaryKey(zeitdatenDto.getIBelegartid());

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

				wcbRelativ.setActivatable(true);

				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);
			}
		}
		updatePosAndTaetigkeitFelder(zeitdatenDto.getCBelegartnr(), zeitdatenDto.getTaetigkeitIId());

		this.setStatusbarPersonalIIdAendern(zeitdatenDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(zeitdatenDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(zeitdatenDto.getTAnlegen());
		this.setStatusbarTAendern(zeitdatenDto.getTAendern());
	}

	private void updatePosAndTaetigkeitFelder(String belegartCNr, Object sonderTaetigkeitIId) {
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
			if (!hatModulStueckrueckmeldung) {
				if (bLosAGIstPflichtfeld == true) {
					wtfPosition.setMandatoryField(true);
				} else {
					wtfPosition.setMandatoryField(false);
				}

			} else {
				wtfPosition.setMandatoryField(!bLosbuchungOhnePositionbezug);
			}
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
					PersonalFilterFactory.getInstance()
							.createFKPersonal(internalFrameZeiterfassung.getPersonalDto().getIId()),
					QueryParameters.UC_ID_PERSONALVERFUEGBARKEIT, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("title.artikelauswahlliste"));
			new DialogQuery(panelQueryFLRArtikel);

		} else {

			if (bProjektMitTaetigkeiten && selectedBelegart != null
					&& selectedBelegart.equals(LocaleFac.BELEGART_PROJEKT) && selectedBeleg != null) {

				ProjekttaetigkeitDto[] ptDtos = DelegateFactory.getInstance().getProjektServiceDelegate()
						.projekttaetigkeitFindByProjektIId(selectedBeleg);

				if (ptDtos.length == 0) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("proj.projekttaetigkeiten.keinedefiniert"));
					return;
				} else {
					panelQueryFLRArtikel = new PanelQueryFLR(null,
							ArtikelFilterFactory.getInstance().createFKTaetigkeitenEinesProjektes(ptDtos),
							QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse, internalFrameZeiterfassung,
							LPMain.getTextRespectUISPr("title.artikelauswahlliste"));

					FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory.getInstance()
							.createFKDArtikelnummer(getInternalFrame());
					FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory.getInstance().createFKDVolltextsuche();
					panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

					panelQueryFLRArtikel.setSelectedId(zeitdatenDto.getArtikelIId());

					new DialogQuery(panelQueryFLRArtikel);
				}

			} else {

				panelQueryFLRArtikel = new PanelQueryFLR(null,
						ArtikelFilterFactory.getInstance().createFKArtikellisteNurArbeitszeit(),
						QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse, internalFrameZeiterfassung,
						LPMain.getTextRespectUISPr("title.artikelauswahlliste"),
						ArtikelFilterFactory.getInstance().createFKVArtikel(), null);

				FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory.getInstance()
						.createFKDArtikelnummer(getInternalFrame());
				FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory.getInstance().createFKDVolltextsuche();
				panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

				panelQueryFLRArtikel.setSelectedId(zeitdatenDto.getArtikelIId());

				new DialogQuery(panelQueryFLRArtikel);
			}

		}
	}

	void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN, PanelBasis.ACTION_FILTER };

		FilterKriterium[] kriterien = null;

		if (bZeitdatenAufErledigteBuchbar) {
			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);
			FilterKriterium krit2 = new FilterKriterium(AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true,
					"('" + AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')", FilterKriterium.OPERATOR_NOT_IN, false);
			kriterien[0] = krit1;
			kriterien[1] = krit2;
		} else {

			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('" + AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT
							+ "','" + AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);

			kriterien[0] = krit1;
			kriterien[1] = krit2;
		}
		panelQueryFLRAuftrag = new PanelQueryFLR(AuftragFilterFactory.getInstance().createQTPanelAuftragAuswahl(),
				kriterien, QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("title.auftragauswahlliste"));

		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname(),
				AuftragFilterFactory.getInstance().createFKVAuftrag());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance().createFKDProjekt());

		Integer selectedBelegIId = null;
		if (zeitdatenDto != null) {
			selectedBelegIId = zeitdatenDto.getIBelegartid();
		}

		panelQueryFLRAuftrag.setSelectedId(selectedBelegIId);

		new DialogQuery(panelQueryFLRAuftrag);

	}

	void dialogQueryAngebotAuftragFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN, PanelBasis.ACTION_FILTER };

		FilterKriterium[] kriterien = null;

		if (bZeitdatenAufErledigteBuchbar) {
			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);
			FilterKriterium krit2 = new FilterKriterium("status_c_nr", true,
					"('" + AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')", FilterKriterium.OPERATOR_NOT_IN, false);
			kriterien[0] = krit1;
			kriterien[1] = krit2;
		} else {

			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium("status_c_nr", true,
					"('" + AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "','" + AuftragServiceFac.AUFTRAGSTATUS_STORNIERT
							+ "')",
					FilterKriterium.OPERATOR_NOT_IN, false);

			kriterien[0] = krit1;
			kriterien[1] = krit2;
		}
		panelQueryFLRAngebotAuftrag = new PanelQueryFLR(
				AuftragFilterFactory.getInstance().createQTPanelAuftragAuswahl(), kriterien,
				QueryParameters.UC_ID_ANGEBOTAUFTRAG, aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("title.auftragauswahlliste"));

		panelQueryFLRAngebotAuftrag.befuellePanelFilterkriterienDirekt(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname());
		panelQueryFLRAngebotAuftrag.addDirektFilter(AuftragFilterFactory.getInstance().createFKDProjekt());

		Integer selectedBelegIId = null;
		if (zeitdatenDto != null) {
			selectedBelegIId = zeitdatenDto.getIBelegartid();
		}

		panelQueryFLRAngebotAuftrag.setSelectedId(selectedBelegIId);

		new DialogQuery(panelQueryFLRAngebotAuftrag);

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		Integer selectedBelegIId = null;
		if (zeitdatenDto != null) {
			selectedBelegIId = zeitdatenDto.getIBelegartid();
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_LOSAUSWAHL_TECHNIKERFILTER, ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && ((Boolean) parameter.getCWertAsObject()) == true) {

			// Dialog'Wiedervorlage erstellen
			getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = new DialogLoseEinesTechnikers(
					getInternalFrame(), bZeitdatenAufErledigteBuchbar, selectedBelegIId,
					getInternalFrameZeiterfassung().getPersonalDto().getIId(), bZeitdatenAufAngelegteLoseBuchbar);

			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(getInternalFrameZeiterfassung().dialogLoseEinesTechnikers);
			getInternalFrameZeiterfassung().dialogLoseEinesTechnikers.setVisible(true);

		} else {
			panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRBebuchbareLose(getInternalFrame(),
					bZeitdatenAufErledigteBuchbar, true, bZeitdatenAufAngelegteLoseBuchbar, selectedBelegIId, false);
			// PJ17681

			Map<?, ?> mEingeschraenkteFertigungsgruppen = DelegateFactory.getInstance().getStuecklisteDelegate()
					.getEingeschraenkteFertigungsgruppen();

			if (mEingeschraenkteFertigungsgruppen != null) {
				panelQueryFLRLos.setFilterComboBox(mEingeschraenkteFertigungsgruppen,
						new FilterKriterium("flrlos.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false),
						true, null, false);
				panelQueryFLRLos.eventActionRefresh(null, true);
			} else {

				panelQueryFLRLos.setFilterComboBox(
						DelegateFactory.getInstance().getStuecklisteDelegate().getAllFertigungsgrupe(),
						new FilterKriterium("flrlos.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false),
						false, LPMain.getTextRespectUISPr("lp.alle"), false);
			}
			if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung() == false
					&& panelQueryFLRLos != null && (zeitdatenDto == null || zeitdatenDto.getIId() == null)) {

				parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_ZEITVERTEILUNG_TYP, ParameterFac.KATEGORIE_PERSONAL,
								LPMain.getTheClient().getMandant());

				int zeitverteilungTyp = (Integer) parameter.getCWertAsObject();
				if (zeitverteilungTyp == 0) {
					panelQueryFLRLos.setMultipleRowSelectionEnabled(true);
				}
				panelQueryFLRLos.addButtonAuswahlBestaetigen(null);
			}
		}

		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		Integer selectedBelegIId = null;
		if (zeitdatenDto != null) {
			selectedBelegIId = zeitdatenDto.getIBelegartid();
		}

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance().createPanelFLRProjekt(getInternalFrame(),
				selectedBelegIId, false);

		if (letzterProjektBereich != null) {
			panelQueryFLRProjekt.setKeyOfFilterComboBox(letzterProjektBereich);
		}

		new DialogQuery(panelQueryFLRProjekt);

	}

	void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {

		Integer selectedBelegIId = null;
		if (zeitdatenDto != null) {
			selectedBelegIId = zeitdatenDto.getIBelegartid();
		}

		panelQueryFLRAngebot = AngebotFilterFactory.getInstance().createPanelFLRAngebot(getInternalFrame(), false,
				false, AngebotFilterFactory.getInstance().createFKAngebotAngelegteUndOffene(), selectedBelegIId);
		new DialogQuery(panelQueryFLRAngebot);

	}

	void dialogQueryZeitmodellFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRZeitmodell = PersonalFilterFactory.getInstance().createPanelFLRZeitmodell(getInternalFrame(), null,
				false);
		new DialogQuery(panelQueryFLRZeitmodell);
	}

	void dialogQueryAuftragpositionFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		QueryType[] querytypes = null;

		if (bAuftragsbuchungOhnePositionbezug) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
		}

		panelQueryFLRAuftragposition = new PanelQueryFLR(querytypes,
				AuftragFilterFactory.getInstance().createFKUmsatzrelevantePositionen(selectedBeleg),
				QueryParameters.UC_ID_AUFTRAGPOSITION_ZEITERFASSUNG, aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("auft.title.panel.positionen"));

		new DialogQuery(panelQueryFLRAuftragposition);

	}

	void dialogQueryAngebotpositionFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] aFilterKrit = null;

		if (selectedBeleg != null) {
			aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT + ".i_id",
					true, selectedBeleg.toString(), FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;

			FilterKriterium krit2 = new FilterKriterium(AngebotpositionFac.FLR_ANGEBOTPOSITION_POSITIONART_C_NR, true,
					"'" + AngebotServiceFac.ANGEBOTPOSITIONART_IDENT + "'", FilterKriterium.OPERATOR_EQUAL, false);
			aFilterKrit[1] = krit2;

		}
		QueryType[] querytypes = null;
		panelQueryFLRAngebotposition = new PanelQueryFLR(querytypes, aFilterKrit, QueryParameters.UC_ID_ANGEBOTPOSITION,
				aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("auft.title.panel.positionen"));

		new DialogQuery(panelQueryFLRAngebotposition);

	}

	private void dialogQueryLospositionFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLosposition = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollarbeitsplan(getInternalFrame(), selectedBeleg, null);
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

		wcbRelativ.setShort(Helper.boolean2Short(false));
		wcbRelativ.addActionListener(new PanelZeitdaten_wcbRelativ_actionAdapter(this));
		wcbRelativ.addChangeListener(new PanelZeitdaten_wcbRelativ_changeAdapter(this));
		wcbRelativ.addPropertyChangeListener(new PanelZeitdaten_wcbRelativ_propertyChangeAdapter(this));
		wcbRelativ.setVisible(false);
		wlaZeit.setText(LPMain.getTextRespectUISPr("lp.zeit"));

		wcoSonderTaetigkeit.addActionListener(new PanelZeitdaten_wcoSonderTaetigkeit_actionAdapter(this));
		wdfDatum.setMandatoryField(true);
		wdfDatum.setActivatable(false);

		wtfTaetigkeit.setActivatable(false);
		wtfZeitmodell.setActivatable(false);
		wtfTaetigkeit.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfZeitmodell.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfPosition.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung() == true
				&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBKommtGehtBuchen() == false) {
//			wdfDatum.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
			wdfDatum.setTimestamp(getNowZeroSecondsAsTimestamp());
		} else {
			java.sql.Timestamp t = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(
							internalFrameZeiterfassung.getPersonalDto().getIId());
			if (t != null) {
				wdfDatum.setTimestamp(t);
			} else {
//				wdfDatum.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
				wdfDatum.setTimestamp(getNowZeroSecondsAsTimestamp());
			}
		}

		wbuMaschine.setText(LPMain.getTextRespectUISPr("lp.maschine") + "...");
		wbuMaschine.setActionCommand(ACTION_SPECIAL_MASCHINE_FROM_LISTE);
		wbuMaschine.addActionListener(this);
		wtfMaschine.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfMaschine.setActivatable(false);

		wdfDatum.getDisplay().addPropertyChangeListener(this);
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wbuTagZurueck.setMinimumSize(new Dimension(40, 10));
		wbuTagZurueck.setText("<");
		wbuTagZurueck.addActionListener(new PanelZeitdaten_wbuTagZurueck_actionAdapter(this));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ARBEITSZEITARTIKEL_AUS_PERSONALVERFUEGBARKEIT,
						ParameterFac.KATEGORIE_PERSONAL, LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bArbeitszeitartikelauspersonalverfuegbarkeit = true;
		}

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH, ParameterFac.KATEGORIE_PERSONAL,
				LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufErledigteBuchbar = true;
		}

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH, ParameterFac.KATEGORIE_PERSONAL,
				LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufAngelegteLoseBuchbar = true;
		}

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_SOLLZEITPRUEFUNG, ParameterFac.KATEGORIE_ALLGEMEIN,
				LPMain.getTheClient().getMandant());

		iOptionSollzeitpruefeung = (Integer) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_AZ_ARTIKEL_AUS_AUFTRAGSPOSITION, ParameterFac.KATEGORIE_PERSONAL,
				LPMain.getTheClient().getMandant());
		bArbeitszeitartikelausAuftragsposition = (Boolean) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_ZEITBUCHUNG_AG_AB_GEMEINSAM, ParameterFac.KATEGORIE_PERSONAL,
				LPMain.getTheClient().getMandant());
		bAG_AB_Gemeinsam = (Boolean) parameter.getCWertAsObject();

		wbuNaechsterTag.setText(">");
		wbuNaechsterTag.setMinimumSize(new Dimension(40, 10));
		wbuNaechsterTag.addActionListener(new PanelZeitdaten_wbuNaechsterTag_actionAdapter(this));

		wlaKalenderwochewochentag.setText(LPMain.getTextRespectUISPr("lp.kalenderwoche_kurz"));
		wlaKalenderwochewochentag.setHorizontalAlignment(SwingConstants.LEFT);

		wlaBetriebskalender.setHorizontalAlignment(SwingConstants.LEFT);
		wlaTagesarbeitszeit.setText(LPMain.getTextRespectUISPr("zeiterfassung.zeitdaten.anwesenheitszeit"));

		wbuPosition.setText(LPMain.getTextRespectUISPr("lp.position") + "...");
		wbuPosition.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_POSITION_FROM_LISTE);
		wbuPosition.addActionListener(this);
		wbuPosition.setMnemonic('O');
		wtfBeleg.setActivatable(false);
		wtfBeleg.setText("");
		wtfBeleg.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfPosition.setActivatable(false);
		wtfPosition.setText("");
		wtfBemerkung.setColumnsMax(ZeiterfassungFac.MAX_ZEITDATEN_BEMERKUNG);
		wtfZeit.setMandatoryField(true);

		wcbRelativ.setText(LPMain.getTextRespectUISPr("zeiterfassung.relativezeitbuchung"));

		wbuTaetigkeit.setText(LPMain.getTextRespectUISPr("lp.taetigkeit") + "...");
		wbuTaetigkeit.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE);
		wbuTaetigkeit.addActionListener(this);
		wbuTaetigkeit.setMnemonic('I');

		wefKommentar.setToolTipText("Ext");

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));

		wlaBemerkung.setDisplayedMnemonic('B');
		wlaBemerkung.setLabelFor(wtfBemerkung);

		wlaZeit.setDisplayedMnemonic('Z');
		wlaZeit.setLabelFor(wtfZeit);

		wcbRelativ.setMnemonic('R');

		wlaDatum.setDisplayedMnemonic('D');
		wlaDatum.setLabelFor(wdfDatum);

		wbuZeitmodell.setText(LPMain.getTextRespectUISPr("lp.zeitmodell") + "...");
		wbuZeitmodell.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_ZEITMODELL_FROM_LISTE);
		wbuZeitmodell.addActionListener(this);
		wbuZeitmodell.setMnemonic('M');

		wlaDauer = new WrapperLabel(LPMain.getTextRespectUISPr("lp.dauer"));
		VonBisDauerListener vbdListener = new VonBisDauerListener();
		wdfDauer.addValueChangedListener(vbdListener);
		wtfZeit.addValueChangedListener(vbdListener);
		wtfZeit_Bis.addValueChangedListener(vbdListener);

		String[] aWhichButtonIUse = null;

		if (bRechtNurBuchen) {
			aWhichButtonIUse = new String[] { ACTION_SAVE, ACTION_DISCARD, };
			wtfZeit.setActivatable(false);
		} else {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		}

		enableToolsPanelButtons(aWhichButtonIUse);

		jpaWorkingOn = new JPanel(new MigLayout("wrap 7, hidemode 3",
				"[12.5%,fill|12.5%,fill|12.5%,fill|12.5%,fill|12.5%,fill|11.0%,fill|24%,fill]"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaBetriebskalender, "skip 3, span 2");
		jpaWorkingOn.add(wlaSonderzeiten, "span");

		jpaWorkingOn.add(wlaDatum);
		JPanel panelDatum = new JPanel(new MigLayout("wrap 3, insets 0", "[fill||fill]"));
		panelDatum.add(wbuTagZurueck);
		panelDatum.add(wdfDatum, "w 0:pref:max");
		panelDatum.add(wbuNaechsterTag);
		jpaWorkingOn.add(panelDatum, "span 3");
		jpaWorkingOn.add(wlaKalenderwochewochentag, "span 2");
		jpaWorkingOn.add(wlaErfuellungsgrad, "span");

		jpaWorkingOn.add(wlaZeit);
		jpaWorkingOn.add(wtfZeit, "split, span 3, grow 50");

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {
			jpaWorkingOn.add(new WrapperLabel("-"), "grow 0, c");
			jpaWorkingOn.add(wtfZeit_Bis, "grow 50, hidemode 0");
			jpaWorkingOn.add(wlaDauer, "skip, hidemode 0");
			jpaWorkingOn.add(wdfDauer, "hidemode 0");
			jpaWorkingOn.add(wlaTagesarbeitszeit, "span, wrap");
		} else {
			jpaWorkingOn.add(wcbRelativ, "hidemode 0, grow");
			jpaWorkingOn.add(wlaTagesarbeitszeit, "skip, span, wrap");
		}

		wbuKommt.setText(DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdKommt).getBezeichnung());
		wbuKommt.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_KOMMT);
		wbuKommt.addActionListener(this);
		wbuKommt.setMnemonic('K');

		wbuUnter.setText(DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdUnter).getBezeichnung());
		wbuUnter.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_UNTER);
		wbuUnter.addActionListener(this);
		wbuUnter.setMnemonic('U');

		wbuEnde.setText(DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdEnde).getBezeichnung());
		wbuEnde.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_ENDE);
		wbuEnde.addActionListener(this);
		wbuEnde.setMnemonic('E');

		wbuGeht.setText(DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByPrimaryKey(taetigkeitIIdGeht).getBezeichnung());
		wbuGeht.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_GEHT);
		wbuGeht.addActionListener(this);
		wbuGeht.setMnemonic('G');

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBKommtGehtBuchen() == false
				&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung() == true) {
			jpaWorkingOn.add(new WrapperLabel(""));
		} else {
			jpaWorkingOn.add(wbuKommt);
		}

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {
			jpaWorkingOn.add(new WrapperLabel(""));
		} else {
			jpaWorkingOn.add(wbuUnter);
		}

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBKommtGehtBuchen() == false
				&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung() == true) {
			jpaWorkingOn.add(new WrapperLabel(""));
		} else {
			jpaWorkingOn.add(wbuGeht);
		}

		if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung().isBVonBisErfassung()) {
			jpaWorkingOn.add(new WrapperLabel(""));
		} else {
			jpaWorkingOn.add(wbuEnde);
		}

		jpaWorkingOn.add(wcoSonderTaetigkeit);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_ZEITMODELL_TAGEWEISE_AENDERN)) {
			jpaWorkingOn.add(wbuZeitmodell);
		} else {
			jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.zeitmodell")));
		}

		jpaWorkingOn.add(wtfZeitmodell, "span");

		wbuProjekt.setText(LPMain.getTextRespectUISPr("label.projekt"));
		wbuProjekt.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_PROJEKT_FROM_LISTE);
		wbuProjekt.addActionListener(this);
		wbuProjekt.setMnemonic('P');

		wbuAngebot.setText(LPMain.getTextRespectUISPr("angb.angebot"));
		wbuAngebot.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_ANGEBOT_FROM_LISTE);
		wbuAngebot.addActionListener(this);
		wbuAngebot.setMnemonic('T');

		wbuAuftrag.setText(LPMain.getTextRespectUISPr("auft.auftrag"));
		wbuAuftrag.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setMnemonic('A');

		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title"));
		wbuLos.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		wbuLos.setMnemonic('L');

		if (!DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD)) {
			wbuKommt.setActivatable(false);
			wbuEnde.setActivatable(false);
			wbuUnter.setActivatable(false);
			wbuGeht.setActivatable(false);

			wbuAuftrag.setActivatable(false);
			wbuLos.setActivatable(false);
			wbuAngebot.setActivatable(false);
			wbuProjekt.setActivatable(false);

		}

		Map<?, ?> m = DelegateFactory.getInstance().getZeiterfassungDelegate().getBebuchbareBelegarten();
		if (m.containsKey(LocaleFac.BELEGART_PROJEKT)) {
			jpaWorkingOn.add(wbuProjekt, "top");
		} else {
			jpaWorkingOn.add(new WrapperLabel(""));
		}

		if (bAG_AB_Gemeinsam
				&& DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_AUFT_AUFTRAG_R)
				&& DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_R)
				&& m.containsKey(LocaleFac.BELEGART_ANGEBOT) && m.containsKey(LocaleFac.BELEGART_AUFTRAG)) {

			wbuAngebot.setText(LPMain.getTextRespectUISPr("pers.zeiterfassung.angebotauftrag"));

			jpaWorkingOn.add(wbuAngebot, "top, span 2");
		} else {
			if (m.containsKey(LocaleFac.BELEGART_ANGEBOT)) {
				jpaWorkingOn.add(wbuAngebot, "top");
			} else {
				jpaWorkingOn.add(new WrapperLabel(""));
			}

			if (m.containsKey(LocaleFac.BELEGART_AUFTRAG)) {
				jpaWorkingOn.add(wbuAuftrag, "top");
			} else {
				jpaWorkingOn.add(new WrapperLabel(""));
			}
		}

		if (m.containsKey(LocaleFac.BELEGART_LOS)) {
			jpaWorkingOn.add(wbuLos, "top");
		} else {
			jpaWorkingOn.add(new WrapperLabel(""));
		}

		JLabel lE = new JLabel("E:");
		lE.setToolTipText(LPMain.getTextRespectUISPr("lp.externerkommentar"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG)) {

			wcbVerrechenbar.setVisible(true);
			jpaWorkingOn.add(wcbVerrechenbar, "split 2, w 75:75:75");
			lE.setHorizontalAlignment(SwingConstants.RIGHT);
			jpaWorkingOn.add(lE, "top");

			wcbVerrechenbar.setKeyOfSelectedItem(100D);

			jpaWorkingOn.add(wefKommentar, " span 3 2, wrap, w 0:0:max, pushx");

		} else {
			lE.setHorizontalAlignment(SwingConstants.RIGHT);
			jpaWorkingOn.add(lE);
			jpaWorkingOn.add(wefKommentar, " span 3 2, wrap, w 0:0:max, pushx");
		}

		jpaWorkingOn.add(wlaBeleg, "top");
		jpaWorkingOn.add(wbuBeleg.getWrapperButtonGoTo(), "split 2, grow 5, span 4, top");
		jpaWorkingOn.add(wtfBeleg, "grow, top");

		jpaWorkingOn.add(wbuPosition);
		jpaWorkingOn.add(wtfPosition, "span 4, growx, split 2");

		JLabel lI = new JLabel("I:");
		lI.setToolTipText(LPMain.getTextRespectUISPr("lp.internerkommentar"));

		jpaWorkingOn.add(lI, "top");

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG)) {
			jpaWorkingOn.add(wefKommentarIntern, " span 3 3, wrap, w 0:0:max, pushx");
		} else {
			jpaWorkingOn.add(wefKommentarIntern, " span 3 3, wrap, w 0:0:max, pushx");
		}

		jpaWorkingOn.add(wbuTaetigkeit);
		jpaWorkingOn.add(wtfTaetigkeit, "span 4");

		jpaWorkingOn.add(wlaBemerkung, " top");
		jpaWorkingOn.add(wtfBemerkung, "span 4, top");

		jpaWorkingOn.add(wbuMaschine, " top");
		jpaWorkingOn.add(wtfMaschine, "span 4, top");

		aktualisiereSichtbarkeitMaschine();

		wlaFehlerInZeitdaten.setForeground(Color.RED);
		wlaFehlerInZeitdaten.setMinimumSize(new Dimension(350, HelperClient.getToolsPanelButtonDimension().height));
		wlaFehlerInZeitdaten.setPreferredSize(new Dimension(350, HelperClient.getToolsPanelButtonDimension().height));

		getToolBar().getToolsPanelRight().add(wlaFehlerInZeitdaten);

		jpaButtonAction.add(wlaFehlerInZeitdaten);
		wlaOffeneZeitverteilung.setMinimumSize(new Dimension(200, HelperClient.getToolsPanelButtonDimension().height));
		wlaOffeneZeitverteilung
				.setPreferredSize(new Dimension(200, HelperClient.getToolsPanelButtonDimension().height));
		wlaOffeneZeitverteilung.setText(LPMain.getTextRespectUISPr("pers.zeiterfassung.zeitverteilungvorhanden"));
		wlaOffeneZeitverteilung.setForeground(Color.RED);
		wlaOffeneZeitverteilung.addActionListener(this);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {
			getToolBar().addButtonCenter("/com/lp/client/res/data_gear.png",
					LPMain.getTextRespectUISPr("pers.zeitdaten.zugehoerige.maschinenzeitdaten"),
					MY_OWN_NEW_GOTO_MASCHINENZEIT, null, null);
		}

		getToolBar().getToolsPanelRight().add(wlaOffeneZeitverteilung);
		// jpaButtonAction.add(wbuOffeneZeitverteilung);
	}

	private void aktualisiereSichtbarkeitMaschine() {

		wbuMaschine.setVisible(false);
		wtfMaschine.setVisible(false);
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {

			if (zeitdatenDto != null && zeitdatenDto.getCBelegartnr() != null
					&& zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)) {

				wbuMaschine.setVisible(true);
				wtfMaschine.setVisible(true);
			}

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		leereAlleFelder(this);
		wdfDatum.setTimestamp(ts);

		// super.eventActionNew(eventObject, true, false);
		zeitdatenDto = new ZeitdatenDto();

		selectedBeleg = null;
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(System.currentTimeMillis());
//		c.set(Calendar.MILLISECOND, 0);
//		c.set(Calendar.SECOND, 0);
//		wtfZeit.setTime(new java.sql.Time(c.getTimeInMillis()));
		wtfZeit.setTime(getNowZeroSeconds());

		wtfPosition.setMandatoryField(true);
		wtfBeleg.setMandatoryField(true);
		wtfTaetigkeit.setMandatoryField(true);
		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {

			// PJ18440
			Timestamp tsLetzteZeit = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.getLetzteGebuchteZeit(internalFrameZeiterfassung.getPersonalDto().getIId(), ts);

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_VON_BIS_VERSATZ, ParameterFac.KATEGORIE_PERSONAL,
							LPMain.getTheClient().getMandant());

			int iVersatz = (Integer) parameter.getCWertAsObject();

			// Millisekunden abschneiden

			if (tsLetzteZeit != null) {
				Calendar cMSAbschneiden = Calendar.getInstance();
				cMSAbschneiden.setTimeInMillis(tsLetzteZeit.getTime());
				cMSAbschneiden.set(Calendar.MILLISECOND, 0);
				tsLetzteZeit = new java.sql.Timestamp(cMSAbschneiden.getTimeInMillis());
			}

			if (tsLetzteZeit != null) {
				wtfZeit.setTime(new java.sql.Time(tsLetzteZeit.getTime()));

				Calendar cBis = Calendar.getInstance();
				cBis.setTimeInMillis(tsLetzteZeit.getTime());
				cBis.add(Calendar.MINUTE, iVersatz);

				wtfZeit_Bis.setTime(new java.sql.Time(cBis.getTimeInMillis()));
			} else {

				Calendar cMSAbschneiden = Calendar.getInstance();
//				cMSAbschneiden.setTimeInMillis(c.getTimeInMillis());
				cMSAbschneiden.setTimeInMillis(wtfZeit.getTime().getTime());
				cMSAbschneiden.set(Calendar.MILLISECOND, 0);
				cMSAbschneiden.add(Calendar.MINUTE, iVersatz);

				wtfZeit_Bis
						.setTime(new java.sql.Time(new java.sql.Timestamp(cMSAbschneiden.getTimeInMillis()).getTime()));
			}

		}
		wcbRelativ.setVisible(true);

		wbuNaechsterTag.setEnabled(false);
		wbuTagZurueck.setEnabled(false);
		wbuZeitmodell.setEnabled(false);
		wtfBemerkung.setText(null);
		wdfDatum.setEnabled(false);
		Object key = wcoSonderTaetigkeit.getKeyOfSelectedItem();
		wcoSonderTaetigkeit.setMap(sondertaetigkeitenOhneVersteckt);
		wcoSonderTaetigkeit.setKeyOfSelectedItem(key);
		wcbVerrechenbar.setKeyOfSelectedItem(100D);

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten().getSelectedId() == null) {
			wcoSonderTaetigkeit.setKeyOfSelectedItem(DelegateFactory.getInstance().getZeiterfassungDelegate()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT).getIId());
		}

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		wbuNaechsterTag.setEnabled(true);
		wbuTagZurueck.setEnabled(true);
		wbuZeitmodell.setEnabled(true);
		wdfDatum.setEnabled(true);

	}

	private ZeitdatenDto neuesZeitdatenDtoFuerHeuteTaetigkeitVorbesetzen(Integer taetigkeitIId) throws Throwable {
		ZeitdatenDto zDto = new ZeitdatenDto();
		zDto.setTaetigkeitIId(taetigkeitIId);
		zDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto().getIId());

//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(System.currentTimeMillis());
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//
//		java.sql.Timestamp tsHeute = new Timestamp(c.getTimeInMillis());
		Timestamp tsHeute = getNowZeroSecondsAsTimestamp();
		zDto.setTZeit(tsHeute);
		zDto.setCBemerkungZuBelegart(wtfBemerkung.getText());

		zDto.setCWowurdegebucht("Client: " + Helper.getPCName());

		zDto.setXKommentar(wefKommentar.getText());
		zDto.setTaetigkeitIId(taetigkeitIId);

		return zDto;
	}

	private void neuesZeitdatenDtoFuerHeuteBelegSpeichern(String belegartCNr, Integer belegIId,
			Integer belegpositionIId) throws Throwable {
		ZeitdatenDto zDto = new ZeitdatenDto();
		zDto.setCBelegartnr(belegartCNr);
		zDto.setIBelegartid(belegIId);
		zDto.setIBelegartpositionid(belegpositionIId);
		zDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto().getIId());
		zDto.setFVerrechenbar((Double) wcbVerrechenbar.getKeyOfSelectedItem());

//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(System.currentTimeMillis());
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//
//		java.sql.Timestamp tsHeute = new Timestamp(c.getTimeInMillis());
		Timestamp tsHeute = getNowZeroSecondsAsTimestamp();
		zDto.setTZeit(tsHeute);

		zDto.setCWowurdegebucht("Client: " + Helper.getPCName());

		// SP4919
		// zDto.setCBemerkungZuBelegart(wtfBemerkung.getText());
		// zDto.setXKommentar(wefKommentar.getText());

		if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
			com.lp.server.auftrag.service.AuftragpositionDto[] auftragpositionDtos = DelegateFactory.getInstance()
					.getAuftragpositionDelegate().auftragpositionFindByAuftrag(belegIId);

			zDto.setFVerrechenbar(100D);

			if (auftragpositionDtos != null && auftragpositionDtos.length > 0) {
				for (int i = 0; i < auftragpositionDtos.length; i++) {
					AuftragpositionDto dto = auftragpositionDtos[i];

					if (dto.getAuftragpositionstatusCNr() != null) {
						zDto.setIBelegartpositionid(auftragpositionDtos[0].getIId());

						if (Helper.short2boolean(auftragpositionDtos[0].getBPauschal())) {
							zDto.setFVerrechenbar(0D);
						}

						if (auftragpositionDtos[0].getArtikelIId() != null
								&& auftragpositionDtos[0].getCBez() == null) {
							zDto.setArtikelIId(auftragpositionDtos[0].getArtikelIId());

							break;
						}
					}
				}

			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("zeiterfassung.auftragkeinepositionen"));
				return;
			}
		}

		// AZ-Artikel

		if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
			Integer artikelIId = DelegateFactory.getInstance().getPersonalDelegate()
					.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
							internalFrameZeiterfassung.getPersonalDto().getIId());
			if (artikelIId != null) {
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId);

				zDto.setArtikelIId(artikelDto.getIId());
			}
		}

		if (zDto.getArtikelIId() == null) {
			// DEFAULT-AZ-Artikel
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
							ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());

			if (parameter.getCWert() != null && !parameter.getCWertAsObject().equals("")) {
				try {
					ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByCNr(parameter.getCWert());

					zDto.setArtikelIId(artikelDto.getIId());
				} catch (Throwable ex) {
					throw new ExceptionLP(EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
							new Exception("FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
				}
			}
		}
		try {
			Integer iId = DelegateFactory.getInstance().getZeiterfassungDelegate().createZeitdaten(zDto);
			setKeyWhenDetailPanel(iId);
			internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten().eventYouAreSelected(false);
			internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten().setSelectedId(iId);
			internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten().eventYouAreSelected(false);
		} catch (ExceptionLP ex) {
			// Wenn Kommt vorher, dann keine Nachfrage

			// if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_BUCHUNG_BEREITS_VORHANDEN) {

				ZeitdatenDto zDto_Vorhanden = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitdatenFindByPersonalIIdTZeit(zDto.getPersonalIId(), zDto.getTZeit());

				if (zDto_Vorhanden != null && zDto_Vorhanden.getTaetigkeitIId() != null
						&& zDto_Vorhanden.getTaetigkeitIId().equals(taetigkeitIIdKommt)) {
					zDto.setTZeit(new java.sql.Timestamp(zDto_Vorhanden.getTZeit().getTime() + 10));

					Integer iId = DelegateFactory.getInstance().getZeiterfassungDelegate().createZeitdaten(zDto);
					setKeyWhenDetailPanel(iId);
					internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten()
							.eventYouAreSelected(false);
					internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten().setSelectedId(iId);
					internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten()
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
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PROJEKT_FROM_LISTE)) {
			dialogQueryProjektFromListe(e);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {

			dialogQueryAuftragFromListe(e);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {

			dialogQueryLosFromListe(e);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MASCHINE_FROM_LISTE)) {

			Integer selectedMaschineIId = null;
			if (zeitdatenDto != null) {
				selectedMaschineIId = zeitdatenDto.getMaschineIId();
			}

			panelQueryFLRMaschine = ZeiterfassungFilterFactory.getInstance().createPanelFLRMaschinen(getInternalFrame(),
					selectedMaschineIId, true);

			new DialogQuery(panelQueryFLRMaschine);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANGEBOT_FROM_LISTE)) {

			if (bAG_AB_Gemeinsam) {
				dialogQueryAngebotAuftragFromListe(e);
			} else {
				dialogQueryAngebotFromListe(e);
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ZEITMODELL_FROM_LISTE)) {
			dialogQueryZeitmodellFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOMMT) || e.getActionCommand().equals(ACTION_SPECIAL_GEHT)
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

			if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY
					|| lockstate == PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY) {
				ZeitdatenDto zDto = neuesZeitdatenDtoFuerHeuteTaetigkeitVorbesetzen(taetigkeitIId);
				zDto.setCBemerkungZuBelegart(null);
				zDto.setXKommentar(null);
				Integer iId = DelegateFactory.getInstance().getZeiterfassungDelegate().createZeitdaten(zDto);
				setKeyWhenDetailPanel(iId);
				internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten()
						.eventActionRefresh(null, true);
				internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten().setSelectedId(iId);

				setKeyWhenDetailPanel(internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten()
						.getSelectedId());

				eventYouAreSelected(false);
			} else if (lockstate == PanelBasis.LOCK_FOR_NEW || lockstate == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
				wcoSonderTaetigkeit.setKeyOfSelectedItem(taetigkeitIId);

				if (bRechtNurBuchen == true || (bRechtNurBuchen == false && bDarfKommtGehtAendern == false)) {

					if (!taetigkeitIId.equals(taetigkeitIIdEnde)) {
						// Zeit auf jetzt setzen
//						Calendar c = Calendar.getInstance();
//						c.setTimeInMillis(System.currentTimeMillis());
//						c.set(Calendar.MILLISECOND, 0);
//						wtfZeit.setTime(new java.sql.Time(c.getTimeInMillis()));
						wtfZeit.setTime(getNow());
					}

				}

				eventActionSave(null, true);

			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_POSITION_FROM_LISTE)) {

			if (selectedBeleg != null && selectedBelegart != null) {
				if (selectedBelegart.equals(LocaleFac.BELEGART_AUFTRAG)) {

					dialogQueryAuftragpositionFromListe(e);
				} else if (selectedBelegart.equals(LocaleFac.BELEGART_LOS)) {
					dialogQueryLospositionFromListe(e);
				} else if (selectedBelegart.equals(LocaleFac.BELEGART_ANGEBOT)) {
					dialogQueryAngebotpositionFromListe(e);
				}
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("zeiterfassung.belegauswaehlen"));
			}
		} else if (e.getSource().equals(wlaOffeneZeitverteilung)) {
			ZeitverteilungDto[] zv = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.zeitverteilungFindByPersonalIIdUndTag(internalFrameZeiterfassung.getPersonalDto().getIId(),
							wdfDatum.getTimestamp());
			if (zv != null && zv.length > 0) {

				JList list = new JList();

				String s = "";

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("pers.offenezeitverteilung.loeschen"));
				if (b == true) {
					DelegateFactory.getInstance().getZeiterfassungDelegate().removeZeitverteilungByPersonalIIdUndTag(
							internalFrameZeiterfassung.getPersonalDto().getIId(), wdfDatum.getTimestamp());
					wlaOffeneZeitverteilung.setVisible(false);
				}
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_GOTO_MASCHINENZEIT)) {

			ArrayList<Integer> alIds = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.getMaschinenzeitenEinesTagesEinesAGsUndEinerPerson(
							internalFrameZeiterfassung.getPersonalDto().getIId(), zeitdatenDto.getIBelegartpositionid(),
							wdfDatum.getDate());
			if (alIds.size() > 0) {

				if (alIds.size() == 1) {
					gotoMaschinenzeitdaten(alIds.get(0));
				} else {

					FilterKriterium[] filters = new FilterKriterium[1];

					filters[0] = new FilterKriterium("i_id", true, Helper.arrayToSqlInList(alIds),
							FilterKriterium.OPERATOR_IN, false);

					panelQueryMaschinenZeitdaten = new PanelQueryFLR(null, filters,
							QueryParameters.UC_ID_MASCHINENZEITDATEN, null, getInternalFrame(),
							LPMain.getTextRespectUISPr("fert.tab.oben.zeitdaten.title"), true);

					panelQueryMaschinenZeitdaten.befuellePanelFilterkriterienDirekt(
							ZeiterfassungFilterFactory.getInstance().createFKDMaschinenzeitdatenPersonalname(),
							ZeiterfassungFilterFactory.getInstance().createMaschinenzeitdatenFKDLosnummer());
					new DialogQuery(panelQueryMaschinenZeitdaten);

				}
			}

		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZEITDATEN;
	}

	protected void setDefaults() throws Throwable {

		if (bRechtNurBuchen == true) {
			sondertaetigkeitenMitVersteckt = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenNurBDEBuchbar();
			sondertaetigkeitenOhneVersteckt = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenNurBDEBuchbarOhneVersteckt();
		} else {
			sondertaetigkeitenMitVersteckt = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.getAllSprSondertaetigkeiten();
			sondertaetigkeitenOhneVersteckt = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenOhneVersteckt();
		}

		// Ende
		Integer taetigkeitIId_Ende = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE).getIId();
		// Kommt
		Integer taetigkeitIId_Kommt = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT).getIId();
		// Geht
		Integer taetigkeitIId_Geht = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT).getIId();

		// Telefon
		Integer taetigkeitIId_Telefon = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON).getIId();

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
				ParameterFac.PARAMETER_URLAUBSANTRAG);
		boolean bUrlaubsantrag = (java.lang.Boolean) parameter.getCWertAsObject();

		boolean bSonderzeitenCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SONDERZEITEN_CUD);

		if (bSonderzeitenCUD == false) {
			if (bUrlaubsantrag == true) {
				Iterator it = sondertaetigkeitenOhneVersteckt.keySet().iterator();

				// Unter
				Integer taetigkeitIId_Unter = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_UNTER).getIId();

				Set<Integer> toRemove = new HashSet<Integer>();

				while (it.hasNext()) {

					Integer taetigkeitIId = (Integer) it.next();

					if (taetigkeitIId.equals(taetigkeitIId_Ende) || taetigkeitIId.equals(taetigkeitIId_Kommt)
							|| taetigkeitIId.equals(taetigkeitIId_Geht) || taetigkeitIId.equals(taetigkeitIId_Unter)
							|| taetigkeitIId.equals(taetigkeitIId_Telefon)) {

					} else {
						TaetigkeitDto dtoTemp = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.taetigkeitFindByPrimaryKey(taetigkeitIId);

						if (!Helper.short2boolean(dtoTemp.getBDarfSelberBuchen())) {
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

		if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung().isBVonBisErfassung()) {

			if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung().isBKommtGehtBuchen() == false) {

				if (sondertaetigkeitenMitVersteckt.containsKey(taetigkeitIId_Kommt)) {
					sondertaetigkeitenMitVersteckt.remove(taetigkeitIId_Kommt);
				}
				if (sondertaetigkeitenOhneVersteckt.containsKey(taetigkeitIId_Kommt)) {
					sondertaetigkeitenOhneVersteckt.remove(taetigkeitIId_Kommt);
				}
				if (sondertaetigkeitenMitVersteckt.containsKey(taetigkeitIId_Geht)) {
					sondertaetigkeitenMitVersteckt.remove(taetigkeitIId_Geht);
				}
				if (sondertaetigkeitenOhneVersteckt.containsKey(taetigkeitIId_Geht)) {
					sondertaetigkeitenOhneVersteckt.remove(taetigkeitIId_Geht);
				}

			}

			if (sondertaetigkeitenMitVersteckt.containsKey(taetigkeitIId_Ende)) {
				sondertaetigkeitenMitVersteckt.remove(taetigkeitIId_Ende);
			}
			if (sondertaetigkeitenOhneVersteckt.containsKey(taetigkeitIId_Ende)) {
				sondertaetigkeitenOhneVersteckt.remove(taetigkeitIId_Ende);
			}

			boolean hatTelefonzeiterfassung = LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TELEFONZEITERFASSUNG);

			if (hatTelefonzeiterfassung == false) {
				if (sondertaetigkeitenMitVersteckt.containsKey(taetigkeitIId_Telefon)) {
					sondertaetigkeitenMitVersteckt.remove(taetigkeitIId_Telefon);
				}
				if (sondertaetigkeitenOhneVersteckt.containsKey(taetigkeitIId_Telefon)) {
					sondertaetigkeitenOhneVersteckt.remove(taetigkeitIId_Telefon);
				}
			}

		}

		wcoSonderTaetigkeit.setMap(sondertaetigkeitenMitVersteckt);

		boolean bHatAngebotszeiterfassung = false;
		boolean bHatProjektzeiterfassung = false;

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)
				&& LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG)) {
			bHatProjektzeiterfassung = true;
		}
		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
				&& LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG)) {
			bHatAngebotszeiterfassung = true;
		}

		if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)
				&& !LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)
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

	protected void components2Dto() throws Throwable {
		zeitdatenDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto().getIId());
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

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {

			cDatum.set(Calendar.MILLISECOND, 0);
			ts.setTime(cDatum.getTimeInMillis() + 10);

		} else {

			// SP9500

			if (cZeit.get(Calendar.MILLISECOND) > 0 && zeitdatenDto.getIId() != null) {

				ZeitdatenDto zeitdatenDtoOriginal = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitdatenFindByPrimaryKey(zeitdatenDto.getIId());

				Calendar cZeitOriginal = Calendar.getInstance();
				cZeitOriginal.setTimeInMillis(zeitdatenDtoOriginal.getTZeit().getTime());

				if (cZeitOriginal.get(Calendar.MILLISECOND) > 0
						&& zeitdatenDtoOriginal.getTZeit().getTime() != cDatum.getTimeInMillis()) {
					cDatum.set(Calendar.MILLISECOND, 0);

				}

			}

			ts.setTime(cDatum.getTimeInMillis());
		}

		zeitdatenDto.setTZeit(ts);
		zeitdatenDto.setCBemerkungZuBelegart(wtfBemerkung.getText());

		zeitdatenDto.setCWowurdegebucht("Client: " + Helper.getPCName());

		zeitdatenDto.setXKommentar(wefKommentar.getText());
		zeitdatenDto.setXKommentarIntern(wefKommentarIntern.getText());

		zeitdatenDto.setCBelegartnr(selectedBelegart);
		zeitdatenDto.setIBelegartid(selectedBeleg);

		zeitdatenDto.setFVerrechenbar((Double) wcbVerrechenbar.getKeyOfSelectedItem());

		// Bis

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {
			java.sql.Timestamp tsBis = wdfDatum.getTimestamp();
			tsBis = Helper.cutTimestamp(tsBis);

			Calendar cDatumBis = Calendar.getInstance();
			cDatumBis.setTimeInMillis(tsBis.getTime());

			Calendar cZeitBis = Calendar.getInstance();
			cZeitBis.setTimeInMillis(wtfZeit_Bis.getTime().getTime());
			cZeitBis.set(Calendar.MILLISECOND, 0);

			cDatumBis.set(Calendar.HOUR_OF_DAY, cZeitBis.get(Calendar.HOUR_OF_DAY));
			cDatumBis.set(Calendar.MINUTE, cZeitBis.get(Calendar.MINUTE));
			cDatumBis.set(Calendar.SECOND, cZeitBis.get(Calendar.SECOND));
			cDatumBis.set(Calendar.MILLISECOND, cZeitBis.get(Calendar.MILLISECOND));
			tsBis.setTime(cDatumBis.getTimeInMillis());
			zeitdatenDto.settZeit_Bis(tsBis);
		}

		if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {
			zeitdatenDto.setCBelegartnr(null);
			zeitdatenDto.setIBelegartpositionid(null);
			zeitdatenDto.setTaetigkeitIId((Integer) wcoSonderTaetigkeit.getKeyOfSelectedItem());
		} else {
			zeitdatenDto.setTaetigkeitIId(null);

		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		wbuTagZurueck.setEnabled(false);
		wbuNaechsterTag.setEnabled(false);
		wbuZeitmodell.setEnabled(false);

		if (zeitdatenDto.getTaetigkeitIId() != null) {
			Integer telefonIId = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON).getIId();
			if (telefonIId.equals(wcoSonderTaetigkeit.getKeyOfSelectedItem())) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
				return;
			}
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		wbuKommt.setEnabled(false);
		wbuGeht.setEnabled(false);
		wbuUnter.setEnabled(false);
		wbuEnde.setEnabled(false);

		wcoSonderTaetigkeit.setKeyOfSelectedItem(zeitdatenDto.getTaetigkeitIId());

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (zeiterfassungPruefer.pruefeObBuchungMoeglich(zeitdatenDto.getTZeit(), zeitdatenDto.getPersonalIId())) {

			if (zeitdatenDto.getTaetigkeitIId() != null) {
				Integer telefonIId = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON).getIId();
				if (telefonIId.equals(wcoSonderTaetigkeit.getKeyOfSelectedItem())) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
					return;
				}
			}

			DelegateFactory.getInstance().getZeiterfassungDelegate().removeZeitdaten(zeitdatenDto);
			aktualisiereDaten();
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, true, true);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung()) {

				if (wcoSonderTaetigkeit.getKeyOfSelectedItem() == null && zeitdatenDto.gettZeit_Bis() != null
						&& zeitdatenDto.gettZeit_Bis().before(zeitdatenDto.getTZeit())) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("zeiterfassung.error.bisvorvon"));
					return;
				}

				if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {

					if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdKommt)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdGeht)) {

					} else {
						if (zeitdatenDto.gettZeit_Bis().before(zeitdatenDto.getTZeit())) {

							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("zeiterfassung.error.bisvorvon"));
							return;
						}

					}

				}

			}

			if (zeiterfassungPruefer.pruefeObBuchungMoeglich(zeitdatenDto.getTZeit(), zeitdatenDto.getPersonalIId())) {

				Timestamp tUnterschriebenBis = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.sindZeitenBereitsUnterschrieben(zeitdatenDto.getPersonalIId(), zeitdatenDto.getTZeit());

				if (tUnterschriebenBis != null) {
					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getMessageTextRespectUISPr("pers.zeitdaten.unterschriebenbis",
									Helper.formatTimestamp(tUnterschriebenBis, LPMain.getTheClient().getLocUi())));
					if (b == false) {
						return;
					}
				}

				//
				try {

					if (zeitdatenDto.getTaetigkeitIId() != null) {
						Integer telefonIId = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON).getIId();
						if (telefonIId.equals(wcoSonderTaetigkeit.getKeyOfSelectedItem())) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr(
											"zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
							return;
						}
					}

					// SP7425
					if (zeitdatenDto.getMaschineIId() != null && !Helper.short2boolean(wcbRelativ.getShort())) {

						ZeitdatenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.pruefeObMaschineBelegt(zeitdatenDto.getPersonalIId(), zeitdatenDto.getMaschineIId(),
										zeitdatenDto.getTZeit());
						if (zDto != null) {

							MaschineDto mDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.maschineFindByPrimaryKey(zeitdatenDto.getMaschineIId());

							PersonalDto pDto = DelegateFactory.getInstance().getPersonalDelegate()
									.personalFindByPrimaryKey(zDto.getPersonalIId());

							boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									LPMain.getMessageTextRespectUISPr("pers.zeitdaten.maschine.bereitsgestartet",
											mDto.getCIdentifikationsnr(),
											Helper.formatTimestamp(zDto.getTZeit(), LPMain.getTheClient().getLocUi()),
											pDto.getPartnerDto().formatFixName1Name2()),
									LPMain.getTextRespectUISPr("lp.frage"));
							if (b == false) {
								return;
							}
						}

					}

					if (zeitdatenDto.getIId() == null) {

						if (Helper.short2boolean(wcbRelativ.getShort()) && internalFrameZeiterfassung
								.getTabbedPaneZeiterfassung().isBVonBisErfassung() == false) {
							try {
								zeitdatenDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate()
										.bucheZeitRelativ(zeitdatenDto, false));
								setKeyWhenDetailPanel(zeitdatenDto.getIId());
							} catch (ExceptionLP ex1) {
								if (ex1.getICode() == EJBExceptionLP.FEHLER_GEHT_VOR_ENDE) {
									zeitdatenDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate()
											.bucheZeitRelativ(zeitdatenDto, true));
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
											LPMain.getTextRespectUISPr("lp.error.gehtvorende"));
								} else if (ex1.getICode() == EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_ENDE_FEHLT) {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
											LPMain.getTextRespectUISPr("lp.error.relativletzterauftragnichtbeendet"));
									return;
								} else if (ex1
										.getICode() == EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_GESAMTE_ZEIT_VERBUCHT) {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
											LPMain.getTextRespectUISPr("lp.error.relativegesamtezeitverbucht"));
									return;
								} else {
									handleException(ex1, false);
									return;
								}
							}
						} else {

							if (zeitdatenDto.getLosIIdsFuerZeitverteilung() != null
									&& zeitdatenDto.getLosIIdsFuerZeitverteilung().size() > 1
									&& zeitdatenDto.getArtikelIId() != null) {
								for (int i = 0; i < zeitdatenDto.getLosIIdsFuerZeitverteilung().size(); i++) {
									ZeitverteilungDto zeitverteilungDto = new ZeitverteilungDto();
									zeitverteilungDto.setLosIId(zeitdatenDto.getLosIIdsFuerZeitverteilung().get(i));
									zeitverteilungDto
											.setTZeit(new Timestamp(zeitdatenDto.getTZeit().getTime() + i * 1000));
									zeitverteilungDto.setPersonalIId(zeitdatenDto.getPersonalIId());
									if (i == zeitdatenDto.getLosIIdsFuerZeitverteilung().size() - 1) {
										zeitverteilungDto.setArtikelIId(zeitdatenDto.getArtikelIId());
									}

									try {
										DelegateFactory.getInstance().getZeiterfassungDelegate()
												.createZeitverteilung(zeitverteilungDto);
									} catch (ExceptionLP e1) {
										if (e1.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {

											LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
													.losFindByPrimaryKey(
															zeitdatenDto.getLosIIdsFuerZeitverteilung().get(i));

											DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
													LPMain.getMessageTextRespectUISPr(
															"fert.gemeinsamefertigungstarten.error", losDto.getCNr()));
										} else {
											throw e1;
										}

									}

								}

								super.eventActionSave(e, true);
								return;

							} else {

								boolean bGespreichert = false;
								while (bGespreichert == false) {
									try {
										zeitdatenDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate()
												.createZeitdaten(zeitdatenDto));
										bGespreichert = true;
									} catch (ExceptionLP ex) {
										// if (ex.getICode() ==
										// EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE)
										// {
										if (ex.getICode() == EJBExceptionLP.FEHLER_BUCHUNG_BEREITS_VORHANDEN) {

											Object[] options = { LPMain.getTextRespectUISPr("pers.zeitdaten.vorher"),
													LPMain.getTextRespectUISPr("pers.zeitdaten.nachher"),
													LPMain.getTextRespectUISPr("lp.abbrechen") };

											int iOption = DialogFactory.showModalDialog(getInternalFrame(),
													LPMain.getTextRespectUISPr(
															"pers.warning.zeitbuchungbereitsvorhanden"),
													"", options, options[1]);
											if (iOption == JOptionPane.YES_OPTION) {
												zeitdatenDto.setTZeit(
														new java.sql.Timestamp(zeitdatenDto.getTZeit().getTime() - 10));
											} else if (iOption == JOptionPane.NO_OPTION) {
												zeitdatenDto.setTZeit(
														new java.sql.Timestamp(zeitdatenDto.getTZeit().getTime() + 10));
											} else if (iOption == JOptionPane.CANCEL_OPTION) {
												return;
											} else if (iOption == JOptionPane.CLOSED_OPTION) {
												return;
											}
										} else if (ex.getICode() == EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_ENDE_FEHLT) {
											DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
													LPMain.getTextRespectUISPr(
															"lp.error.relativletzterauftragnichtbeendet"));
											return;
										} else {
											handleException(ex, false);
											return;
										}
									}
								}
								setKeyWhenDetailPanel(zeitdatenDto.getIId());
							}
						}
					} else {
						boolean bGespeichert = false;
						while (bGespeichert == false) {
							try {
								if (Helper.short2boolean(wcbRelativ.getShort())
										&& zeitdatenDto.getCBelegartnr() != null) {
									bGespeichert = true;
									DelegateFactory.getInstance().getZeiterfassungDelegate()
											.aendereZeitRelativ(zeitdatenDto, wtfZeit.getTime());

								} else {
									DelegateFactory.getInstance().getZeiterfassungDelegate()
											.updateZeitdaten(zeitdatenDto);
									bGespeichert = true;

								}
							} catch (ExceptionLP ex) {
								// if (ex.getICode() ==
								// EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
								if (ex.getICode() == EJBExceptionLP.FEHLER_BUCHUNG_BEREITS_VORHANDEN) {

									Object[] options = { LPMain.getTextRespectUISPr("pers.zeitdaten.vorher"),
											LPMain.getTextRespectUISPr("pers.zeitdaten.nachher") };

									int iOption = DialogFactory.showModalDialog(getInternalFrame(),
											LPMain.getTextRespectUISPr("pers.warning.zeitbuchungbereitsvorhanden"), "",
											options, options[1]);
									if (iOption == JOptionPane.YES_OPTION) {
										zeitdatenDto.setTZeit(
												new java.sql.Timestamp(zeitdatenDto.getTZeit().getTime() - 10));
									} else if (iOption == JOptionPane.NO_OPTION) {
										zeitdatenDto.setTZeit(
												new java.sql.Timestamp(zeitdatenDto.getTZeit().getTime() + 10));
									}
								} else if (ex
										.getICode() == EJBExceptionLP.FEHLER_RELATIVES_AENDERN_MIT_SONDERTAETIGKEITEN_NICHT_MOEGLICH) {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr(
													"zeiterfassung.error.realtivesaendernsondertaetigkeiten"));
								} else if (ex.getICode() == EJBExceptionLP.FEHLER_RELATIVES_AENDERN_ZUWENIG_ZEIT) {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain
											.getTextRespectUISPr("zeiterfassung.error.realtivesaendernzuwenigzeit"));

								} else {
									// Fallback fuer nicht explizit aufgefuehrte
									// Exceptions
									handleException(ex, false);
									return;
								}
							}
						}
					}

					super.eventActionSave(e, true);
					if (getInternalFrame().getKeyWasForLockMe() == null) {
						getInternalFrame().setKeyWasForLockMe(zeitdatenDto.getIId().toString());
					}
					zeitdatenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.zeitdatenFindByPrimaryKey(zeitdatenDto.getIId());
					aktualisiereDaten();
					eventYouAreSelected(false);
					wbuNaechsterTag.setEnabled(true);
					wbuTagZurueck.setEnabled(true);
					wbuZeitmodell.setEnabled(true);
				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_GEHT_VOR_ENDE) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("zeiterfassung.gehtbuchungvorende"));

					} else if (ex
							.getICode() == EJBExceptionLP.FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("zeiterfassung.sondertaetigkeitbeenden"));
					} else {
						throw ex;
					}
				}
			}
		}
	}

	private void setZeitdatenDtoBelegart(String belegart, Integer belegIId) {
		zeitdatenDto.setCBelegartnr(belegart);
		zeitdatenDto.setIBelegartid(belegIId);
		zeitdatenDto.setArtikelIId(null);
		wtfTaetigkeit.setText(null);
		zeitdatenDto.setIBelegartpositionid(null);
		zeitdatenDto.setLosIIdsFuerZeitverteilung(null);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		int lockstate = getLockedstateDetailMainKey().getIState();

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikel) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				com.lp.server.artikel.service.ArtikelDto artikelDto = null;
				if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
					PersonalverfuegbarkeitDto dto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalverfuegbarkeitFindByPrimaryKey(key);
					artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(dto.getArtikelIId());
				} else {
					artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(key);
				}
				zeitdatenDto.setArtikelIId(artikelDto.getIId());
				wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());

			} else if (e.getSource() == panelQueryFLRMaschine) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				MaschineDto maschineDto = null;
				maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate().maschineFindByPrimaryKey(key);
				zeitdatenDto.setMaschineIId(maschineDto.getIId());
				wtfMaschine.setText(maschineDto.getBezeichnung());
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				auftragAusgewaehlt(key);

			} else if (e.getSource() == panelQueryFLRLos
					|| (getInternalFrameZeiterfassung().dialogLoseEinesTechnikers != null
							&& e.getSource() == getInternalFrameZeiterfassung().dialogLoseEinesTechnikers
									.getPanelQueryLose())) {

				if (panelQueryFLRLos != null && panelQueryFLRLos.getDialog() != null) {
					panelQueryFLRLos.getDialog().setVisible(false);
				}

				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				wcoSonderTaetigkeit.setKeyOfSelectedItem(null);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				if (!hatModulStueckrueckmeldung) {
					if (bLosAGIstPflichtfeld == true) {
						wtfPosition.setMandatoryField(true);

					} else {
						wtfPosition.setMandatoryField(false);
					}
				} else {
					if (bLosbuchungOhnePositionbezug == true) {
						wtfPosition.setMandatoryField(false);
					} else {
						wtfPosition.setMandatoryField(true);
					}
				}
				wtfPosition.setText(null);

				if (wtfPosition.isMandatoryField() == false
						&& (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY)) {

					neuesZeitdatenDtoFuerHeuteBelegSpeichern(LocaleFac.BELEGART_LOS, key, null);

					if (getInternalFrameZeiterfassung().dialogLoseEinesTechnikers != null) {
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers.dispose();
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = null;
					}
				} else {

					if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY) {
						zeitdatenDto = new ZeitdatenDto();
					}

					LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate().losFindByPrimaryKey(key);

					wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
					wbuBeleg.setOKey(selectedBeleg);

					setZeitdatenDtoBelegart(LocaleFac.BELEGART_LOS, losDto.getIId());

					if (panelQueryFLRLos != null && panelQueryFLRLos.getSelectedIds() != null
							&& panelQueryFLRLos.getSelectedIds().length > 1) {
						ArrayList<Integer> al = new ArrayList<Integer>();
						for (int i = 0; i < panelQueryFLRLos.getSelectedIds().length; i++) {
							al.add((Integer) panelQueryFLRLos.getSelectedIds()[i]);
						}
						zeitdatenDto.setLosIIdsFuerZeitverteilung(al);
						wcbRelativ.setSelected(false);
						wcbRelativ.setEnabled(false);
					}

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

					if (zeitdatenDto.getArtikelIId() == null) {
						if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
							Integer artikelIId = DelegateFactory.getInstance().getPersonalDelegate()
									.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
											internalFrameZeiterfassung.getPersonalDto().getIId());
							if (artikelIId != null) {
								ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(artikelIId);
								wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
								zeitdatenDto.setArtikelIId(artikelDto.getIId());
							}
						}

						if (zeitdatenDto.getArtikelIId() == null) {
							// DEFAULT-AZ-Artikel
							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
									.getParameterDelegate()
									.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
											ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());

							if (parameter.getCWert() != null && !parameter.getCWertAsObject().equals("")) {
								try {
									ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
											.artikelFindByCNr(parameter.getCWert());
									wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
									zeitdatenDto.setArtikelIId(artikelDto.getIId());
								} catch (Throwable ex) {
									// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
								}
							}
						}
					}
					erfuellungsgradBerechnen(null, null);

					if (getInternalFrameZeiterfassung().dialogLoseEinesTechnikers != null) {
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers.dispose();
						getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = null;
					}

					if (wtfPosition.isMandatoryField() == true) {
						dialogQueryLospositionFromListe(null);
					}

				}

				enableBisZeit(true);
			} else if (e.getSource() == panelQueryFLRAngebot) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				angebotAusgewaehlt(key);
			} else if (e.getSource() == panelQueryFLRAngebotAuftrag) {
				Integer angebotauftragIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				AngebotauftragDto angebotauftragDto = DelegateFactory.getInstance().getAngebotServiceDelegate()
						.angebotauftragFindByPrimaryKey(angebotauftragIId);
				if (angebotauftragDto.getAngebotIId() != null) {
					angebotAusgewaehlt(angebotauftragDto.getAngebotIId());
				} else {
					auftragAusgewaehlt(angebotauftragDto.getAuftragIId());
				}

			}

			else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				wcoSonderTaetigkeit.setKeyOfSelectedItem(null);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY) {
					neuesZeitdatenDtoFuerHeuteBelegSpeichern(LocaleFac.BELEGART_PROJEKT, key, null);
				} else {

					ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate()
							.projektFindByPrimaryKey(key);

					letzterProjektBereich = projektDto.getBereichIId();
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_PROJEKT, projektDto.getIId());

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
						Integer artikelIId = DelegateFactory.getInstance().getPersonalDelegate()
								.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
										internalFrameZeiterfassung.getPersonalDto().getIId());
						if (artikelIId != null) {
							ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(artikelIId);
							wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
							zeitdatenDto.setArtikelIId(artikelDto.getIId());
						}
					}

					if (zeitdatenDto.getArtikelIId() == null && bProjektMitTaetigkeiten == false) {
						// DEFAULT-AZ-Artikel
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
								.getParameterDelegate()
								.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
										ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());

						if (parameter.getCWert() != null && !parameter.getCWertAsObject().equals("")) {
							try {
								ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByCNr(parameter.getCWert());
								wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
								zeitdatenDto.setArtikelIId(artikelDto.getIId());
							} catch (Throwable ex) {
								// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
							}
						}
					}

				}
				enableBisZeit(true);
			}

			else if (e.getSource() == panelQueryFLRAuftragposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				AuftragpositionDto auftragpositionDto = null;
				auftragpositionDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey(key);
				zeitdatenDto.setIBelegartpositionid(auftragpositionDto.getIId());
				wtfPosition.setText(auftragpositionDto.getCBez());
				if (auftragpositionDto.getArtikelIId() != null) {

					ArtikelDto aDtoAusAbpos = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId());

					if (auftragpositionDto.getCBez() != null
							&& !aDtoAusAbpos.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						wtfPosition.setText(aDtoAusAbpos.getCNr() + " " + auftragpositionDto.getCBez());

					} else if (auftragpositionDto.getCBez() == null
							&& !aDtoAusAbpos.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						wtfPosition.setText(aDtoAusAbpos.formatArtikelbezeichnung());

					}

					if (bArbeitszeitartikelausAuftragsposition == true) {

						if (zeitdatenDto.getArtikelIId() != null) {

							boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									LPMain.getTextRespectUISPr("pers.zeitdaten.taetigkeitnachziehen"));
							if (b == false) {
								return;
							}

						}

						ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId());
						if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
							zeitdatenDto.setArtikelIId(artikelDto.getIId());
						} else {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("pers.zeitdaten.artikel.keinearbeitszeit"));
						}
					}

				}
				erfuellungsgradBerechnen(null, auftragpositionDto);

			} else if (e.getSource() == panelQueryFLRLosposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY) {
					neuesZeitdatenDtoFuerHeuteBelegSpeichern(LocaleFac.BELEGART_LOS, zeitdatenDto.getIBelegartid(),
							key);
				} else {

					LossollarbeitsplanDto auftragpositionDto = DelegateFactory.getInstance().getFertigungDelegate()
							.lossollarbeitsplanFindByPrimaryKey(key);
					zeitdatenDto.setIBelegartpositionid(auftragpositionDto.getIId());
					wtfPosition.setText(DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(auftragpositionDto.getArtikelIIdTaetigkeit())
							.formatArtikelbezeichnung());

					// PJ20375
					if (auftragpositionDto.getArtikelIIdTaetigkeit() != null) {
						ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(auftragpositionDto.getArtikelIIdTaetigkeit());
						wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
						zeitdatenDto.setArtikelIId(artikelDto.getIId());
					}

					aktualisiereSichtbarkeitMaschine();

					// SP7102
					if (auftragpositionDto.getMaschineIId() != null) {
						MaschineDto maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.maschineFindByPrimaryKey(auftragpositionDto.getMaschineIId());
						if (Helper.short2boolean(maschineDto.getBManuelleBedienung()) == true) {
							zeitdatenDto.setMaschineIId(maschineDto.getIId());
							wtfMaschine.setText(maschineDto.getBezeichnung());
						}

					}

					erfuellungsgradBerechnen(auftragpositionDto, null);
				}

			} else if (e.getSource() == panelQueryFLRAngebotposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				AngebotpositionDto angebotpositionDto = null;
				angebotpositionDto = DelegateFactory.getInstance().getAngebotpositionDelegate()
						.angebotpositionFindByPrimaryKey(key);
				zeitdatenDto.setIBelegartpositionid(angebotpositionDto.getIId());
				wtfPosition.setText(DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(angebotpositionDto.getArtikelIId()).formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRZeitmodell) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ZeitmodellDto zeitmodellDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitmodellFindByPrimaryKey((Integer) key);
				wtfZeitmodell.setText(zeitmodellDto.getBezeichnung());
				zeitmodellIId = zeitmodellDto.getIId();

				// Zetimodell aendern, wenn noetig
				DelegateFactory.getInstance().getZeiterfassungDelegate().uebersteuereZeitmodellFuerEinenTag(
						internalFrameZeiterfassung.getPersonalDto().getIId(), zeitmodellIId, wdfDatum.getDate());

			} else if (e.getSource() == panelQueryMaschinenZeitdaten) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				gotoMaschinenzeitdaten((Integer) key);
			}

			updatePosAndTaetigkeitFelder(selectedBelegart, wcoSonderTaetigkeit.getKeyOfSelectedItem());

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag || e.getSource() == panelQueryFLRAngebotAuftrag) {
				wtfBeleg.setText(null);
				wtfPosition.setText(null);
				zeitdatenDto.setCBelegartnr(null);
				zeitdatenDto.setIBelegartpositionid(null);
				zeitdatenDto.setArtikelIId(null);
				if (!hatModulStueckrueckmeldung) {
					if (bLosAGIstPflichtfeld == true) {
						wtfPosition.setMandatoryField(true);

					} else {
						wtfPosition.setMandatoryField(false);
					}

				} else {
					if (bLosbuchungOhnePositionbezug == true) {
						wtfPosition.setMandatoryField(false);
					} else {
						wtfPosition.setMandatoryField(true);
					}
				}

			}
			if (e.getSource() == panelQueryFLRAuftragposition) {

				wtfPosition.setText(null);

				zeitdatenDto.setIBelegartpositionid(null);

			}

			if (e.getSource() == panelQueryFLRMaschine) {
				wtfMaschine.setText(null);

				zeitdatenDto.setMaschineIId(null);

			}
		}
	}

	private void angebotAusgewaehlt(Integer angebotIId) throws Throwable, ExceptionLP {

		int lockstate = getLockedstateDetailMainKey().getIState();

		wcoSonderTaetigkeit.setKeyOfSelectedItem(null);
		wtfBeleg.setMandatoryField(true);
		wtfTaetigkeit.setMandatoryField(true);
		if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY) {
			neuesZeitdatenDtoFuerHeuteBelegSpeichern(LocaleFac.BELEGART_ANGEBOT, angebotIId, null);
		} else {
			AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate()
					.angebotFindByPrimaryKey(angebotIId);
			setZeitdatenDtoBelegart(LocaleFac.BELEGART_ANGEBOT, angebotDto.getIId());
			wtfPosition.setMandatoryField(false);
			wtfPosition.setText(null);

			String projBez = angebotDto.getCBez();
			if (projBez == null) {
				projBez = "";
			}
			wlaBeleg.setText("AG" + angebotDto.getCNr());
			wtfBeleg.setText("->" + projBez);
			selectedBeleg = angebotIId;
			selectedBelegart = LocaleFac.BELEGART_ANGEBOT;
			wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_ANGEBOT_AUSWAHL);
			wbuBeleg.setOKey(selectedBeleg);

			if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
				Integer artikelIId = DelegateFactory.getInstance().getPersonalDelegate()
						.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
								internalFrameZeiterfassung.getPersonalDto().getIId());
				if (artikelIId != null) {
					ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(artikelIId);
					wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
					zeitdatenDto.setArtikelIId(artikelDto.getIId());
				}
			}

			if (zeitdatenDto.getArtikelIId() == null) {
				// DEFAULT-AZ-Artikel
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
								ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());

				if (parameter.getCWert() != null && !parameter.getCWertAsObject().equals("")) {
					try {
						ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByCNr(parameter.getCWert());
						wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
						zeitdatenDto.setArtikelIId(artikelDto.getIId());
					} catch (Throwable ex) {
						// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
					}
				}
			}

		}
		enableBisZeit(true);
	}

	private void auftragAusgewaehlt(Integer auftragIId) throws Throwable, ExceptionLP {

		int lockstate = getLockedstateDetailMainKey().getIState();

		wcoSonderTaetigkeit.setKeyOfSelectedItem(null);

		wtfBeleg.setMandatoryField(true);
		wtfTaetigkeit.setMandatoryField(true);

		if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY) {
			neuesZeitdatenDtoFuerHeuteBelegSpeichern(LocaleFac.BELEGART_AUFTRAG, auftragIId, null);
		} else {

			AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(auftragIId);
			setZeitdatenDtoBelegart(LocaleFac.BELEGART_AUFTRAG, auftragDto.getIId());
			// zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
			// zeitdatenDto.setIBelegartid(auftragDto.getIId());
			wtfTaetigkeit.setText(null);

			if (bAuftragsbuchungOhnePositionbezug == true) {
				wtfPosition.setMandatoryField(false);
			} else {
				wtfPosition.setMandatoryField(true);
			}

			wcbVerrechenbar.setKeyOfSelectedItem(100D);

			wtfPosition.setText(null);
			// zeitdatenDto.setIBelegartpositionid(null);

			String projBez = auftragDto.getCBezProjektbezeichnung();
			if (projBez == null) {
				projBez = "";
			}
			String kunde = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse()).getPartnerDto().formatTitelAnrede();

			wlaBeleg.setText("AB" + auftragDto.getCNr());
			wtfBeleg.setText("->" + projBez + ", " + kunde);
			selectedBeleg = auftragIId;
			selectedBelegart = LocaleFac.BELEGART_AUFTRAG;
			wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
			wbuBeleg.setOKey(selectedBeleg);

			if (bArbeitszeitartikelausAuftragsposition == false) {

				com.lp.server.auftrag.service.AuftragpositionDto[] auftragpositionDtos = DelegateFactory.getInstance()
						.getAuftragpositionDelegate().auftragpositionFindByAuftrag(auftragDto.getIId());

				if (auftragpositionDtos != null && auftragpositionDtos.length > 0) {
					for (int i = 0; i < auftragpositionDtos.length; i++) {
						AuftragpositionDto dto = auftragpositionDtos[i];

						if (dto.getAuftragpositionstatusCNr() != null) {
							zeitdatenDto.setIBelegartpositionid(auftragpositionDtos[0].getIId());

							if (Helper.short2boolean(auftragpositionDtos[0].getBPauschal())) {
								wcbVerrechenbar.setKeyOfSelectedItem(0D);
							}

							wtfPosition.setText(auftragpositionDtos[0].getCBez());
							if (auftragpositionDtos[0].getArtikelIId() != null
									&& auftragpositionDtos[0].getCBez() == null) {
								wtfPosition.setText(DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(auftragpositionDtos[0].getArtikelIId())
										.formatArtikelbezeichnung());
								break;
							}

						}
					}

				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("zeiterfassung.auftragkeinepositionen"));
					return;
				}
				if (zeitdatenDto.getArtikelIId() == null) {

					if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
						Integer artikelIId = DelegateFactory.getInstance().getPersonalDelegate()
								.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
										internalFrameZeiterfassung.getPersonalDto().getIId());
						if (artikelIId != null) {
							ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(artikelIId);
							wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
							zeitdatenDto.setArtikelIId(artikelDto.getIId());
						}
					}

					if (zeitdatenDto.getArtikelIId() == null) {

						// DEFAULT-AZ-Artikel
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
								.getParameterDelegate()
								.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
										ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());

						if (parameter.getCWert() != null && !parameter.getCWertAsObject().equals("")) {
							try {
								ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByCNr(parameter.getCWert());
								wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
								zeitdatenDto.setArtikelIId(artikelDto.getIId());
							} catch (Throwable ex) {
								throw new ExceptionLP(EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
										new Exception("FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
							}

						}
					}
				}
			}
			erfuellungsgradBerechnen(null, null);

		}

		enableBisZeit(true);
	}

	private void erfuellungsgradBerechnen(LossollarbeitsplanDto lossollarbeitsplanDto,
			AuftragpositionDto auftragpositionDto) throws ExceptionLP, Throwable {

		if (iOptionSollzeitpruefeung != 0) {

			if (zeitdatenDto.getCBelegartnr() != null) {
				boolean bZuvieleZeitbuchungen = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.sindZuvieleZeitdatenEinesBelegesVorhanden(zeitdatenDto.getCBelegartnr(),
								zeitdatenDto.getIBelegartid());
				String s = "";

				if (zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)) {
					s = "Soll: ";
					if (lossollarbeitsplanDto != null) {
						s += Helper.formatZahl(lossollarbeitsplanDto.getNGesamtzeit(), 1,
								LPMain.getTheClient().getLocUi()) + " Std., Ist: ";
					} else {
						// Sollzeit aller Positionen
						BigDecimal bdGesamt = new BigDecimal(0);

						LossollarbeitsplanDto[] sollarbeitsplanDtos = DelegateFactory.getInstance()
								.getFertigungDelegate().lossollarbeitsplanFindByLosIId(zeitdatenDto.getIBelegartid());
						for (int i = 0; i < sollarbeitsplanDtos.length; i++) {
							bdGesamt = bdGesamt.add(sollarbeitsplanDtos[i].getNGesamtzeit());
						}
						s += Helper.formatZahl(bdGesamt, 1, LPMain.getTheClient().getLocUi()) + " Std., Ist: ";
					}

					if (bZuvieleZeitbuchungen == false) {
						Integer lossollarbeitsplanIId = null;
						if (lossollarbeitsplanDto != null) {
							lossollarbeitsplanIId = lossollarbeitsplanDto.getIId();
						}

						try {
							Double d = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_LOS, zeitdatenDto.getIBelegartid(),
											lossollarbeitsplanIId, null, null, null);
							s += Helper.formatZahl(d, 1, LPMain.getTheClient().getLocUi()) + " Std.";
						} catch (ExceptionLP e) {
							if (e.getICode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {

								wlaFehlerInZeitdaten.setText(LPMain.getTextRespectUISPr("zeiterfassung.error.fehler"));
								wlaFehlerInZeitdaten.setToolTipText(LPMain.getInstance().getMsg(e));
							} else {
								handleException(e, false);
							}
						}
					} else {
						s += "? Std.";
					}
				} else if (zeitdatenDto.getCBelegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					s = "Soll: ";
					if (iOptionSollzeitpruefeung > 1) {
						if (auftragpositionDto != null && iOptionSollzeitpruefeung == 2) {
							if (auftragpositionDto.getNMenge() != null) {
								s += Helper.formatZahl(auftragpositionDto.getNMenge(), 1,
										LPMain.getTheClient().getLocUi()) + " Std., Ist: ";
							}
						} else {
							// Sollzeit aller Positionen
							BigDecimal bdGesamt = new BigDecimal(0);

							AuftragpositionDto[] sollarbeitsplanDtos = DelegateFactory.getInstance()
									.getAuftragpositionDelegate()
									.auftragpositionFindByAuftrag(zeitdatenDto.getIBelegartid());
							for (int i = 0; i < sollarbeitsplanDtos.length; i++) {

								if (sollarbeitsplanDtos[i].getArtikelIId() != null) {
									ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
											.artikelFindByPrimaryKey(sollarbeitsplanDtos[i].getArtikelIId());
									if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
										bdGesamt = bdGesamt.add(sollarbeitsplanDtos[i].getNMenge());
									}
								}
							}
							s += Helper.formatZahl(bdGesamt, 1, LPMain.getTheClient().getLocUi()) + " Std., Ist: ";
						}

						if (bZuvieleZeitbuchungen == false) {
							Integer auftragpositionIId = null;
							if (auftragpositionDto != null && iOptionSollzeitpruefeung == 2) {
								auftragpositionIId = auftragpositionDto.getIId();
							}

							try {
								Double d = DelegateFactory.getInstance().getZeiterfassungDelegate()
										.getSummeZeitenEinesBeleges(LocaleFac.BELEGART_AUFTRAG,
												zeitdatenDto.getIBelegartid(), auftragpositionIId, null, null, null);
								s += Helper.formatZahl(d, 1, LPMain.getTheClient().getLocUi()) + " Std.";
							} catch (ExceptionLP e) {
								if (e.getICode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
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
				wlaErfuellungsgrad.setText(s);
			}
		}
	}

	void wbuTagZurueck_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	public void setzteKalenderWochewochentag() throws Throwable {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(wdfDatum.getDate());
		int kw = cal.get(java.util.Calendar.WEEK_OF_YEAR);

		// Kurze Tagebezeichnungen holen
		String[] kurzeWochentage = new DateFormatSymbols(LPMain.getTheClient().getLocUi()).getWeekdays();

		wlaKalenderwochewochentag.setText(kurzeWochentage[cal.get(Calendar.DAY_OF_WEEK)] + ", "
				+ LPMain.getTextRespectUISPr("lp.kalenderwoche_kurz") + " " + kw);

	}

	void wbuNaechsterTag_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	public void aktualisiereDaten() throws Throwable {
		wlaFehlerInZeitdaten.setText(null);
		wlaFehlerInZeitdaten.setToolTipText(null);
		java.sql.Date dDate = wdfDatum.getDate();
		java.sql.Timestamp tTimestamp = new java.sql.Timestamp(dDate.getTime());
		if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung() != null) {
			FilterKriterium[] fk = ZeiterfassungFilterFactory.getInstance().createFKZeitdatenZuPersonalUndDatum(
					getInternalFrameZeiterfassung().getPersonalDto().getIId(), dDate);
			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung().getPanelQueryZeitdaten().setDefaultFilter(fk);
		}
		setzteKalenderWochewochentag();

		if (bRechtNurBuchen == true) {
			LPButtonAction o = (LPButtonAction) getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.getPanelQueryZeitdaten().getHmOfButtons().get(PanelBasis.ACTION_NEW);
//			if (Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis()))
//					.equals(Helper.cutTimestamp(tTimestamp))) {
			if (Helper.cutTimestamp(getNowZeroSecondsAsTimestamp()).equals(Helper.cutTimestamp(tTimestamp))) {
				o.getButton().setVisible(true);
			} else {
				o.getButton().setVisible(false);
			}
		}

		try {
			BetriebskalenderDto dto = DelegateFactory.getInstance().getPersonalDelegate()
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
			SonderzeitenDto[] dtos = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.sonderzeitenFindByPersonalIIdDDatum(getInternalFrameZeiterfassung().getPersonalDto().getIId(),
							Helper.cutTimestamp(tTimestamp));
			if (dtos != null && dtos.length > 0) {
				String sSonder = LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sonderzeiten") + ":";
				sSonder += " " + DelegateFactory.getInstance().getZeiterfassungDelegate()
						.taetigkeitFindByPrimaryKey(dtos[0].getTaetigkeitIId()).getBezeichnung();
				if (Helper.short2boolean(dtos[0].getBTag()) == true) {
					sSonder += " " + LPMain.getTextRespectUISPr("zeiterfassung.ganztags");
				} else if (Helper.short2boolean(dtos[0].getBHalbtag()) == true) {
					sSonder += " " + LPMain.getTextRespectUISPr("zeiterfassung.halbtags");
				} else {
					sSonder += " " + dtos[0].getUStunden().toString().substring(0, 5) + " "
							+ LPMain.getTextRespectUISPr("lp.stunden");

				}

				wlaSonderzeiten.setText(sSonder);
			} else {
				wlaSonderzeiten.setText("");
			}
		} catch (ExceptionLP ex1) {
			wlaBetriebskalender.setText("");
			// Kein Betriebskalender-Eintrag
		}

		PersonalzeitmodellDto dto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalzeitmodellFindZeitmodellZuDatum(internalFrameZeiterfassung.getPersonalDto().getIId(),
						tTimestamp);
		if (dto != null) {
			wtfZeitmodell.setText(dto.getZeitmodellDto().getBezeichnung());
			zeitmodellIId = dto.getZeitmodellIId();
		} else {
			wtfZeitmodell.setText(LPMain.getTextRespectUISPr("zeiterfassung.keinzeitmodellzugeordnet"));
		}
		berechneTageszeit();
	}

	protected void berechneTageszeit() throws Throwable {
		java.sql.Date dDate = wdfDatum.getDate();

		Double dAnwesenheitszeit = null;
		Double dArbeitszeitzeit = null;
		Double dSollzeit = null;
		try {

			if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung() == true
					&& internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBKommtGehtBuchen() == false) {
				VonBisErfassungTagesdatenDto vbDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.berechneTagesArbeitszeitVonBisZeiterfassungOhneKommtGeht(
								internalFrameZeiterfassung.getPersonalDto().getIId(), dDate);

				dAnwesenheitszeit = vbDto.getdIst();

			} else {
				dAnwesenheitszeit = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.berechneTagesArbeitszeit(internalFrameZeiterfassung.getPersonalDto().getIId(), dDate);

				dArbeitszeitzeit = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.berechneTagesArbeitszeit(internalFrameZeiterfassung.getPersonalDto().getIId(), dDate, false);

			}

		} catch (ExceptionLP ex) {
			// if (ex.getICode() !=
			// EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_FEHLT) {
			wlaFehlerInZeitdaten.setText(LPMain.getTextRespectUISPr("zeiterfassung.error.fehler"));
			// }
		}

		ZeitmodelltagDto zmtagDto = DelegateFactory.getInstance().getZeiterfassungDelegate().getZeitmodelltagZuDatum(
				internalFrameZeiterfassung.getPersonalDto().getIId(), wdfDatum.getTimestamp(),
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG).getIId(),
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG).getIId());

		if (zmtagDto != null && zmtagDto.getZeitmodellIId() != null && zmtagDto.getUSollzeit() != null) {
			dSollzeit = Helper.time2Double(zmtagDto.getUSollzeit());
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

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung().isBVonBisErfassung() == true) {
			java.math.BigDecimal bd = new java.math.BigDecimal(dAnwesenheitszeit.doubleValue());
			String sAnwesend = Helper.formatZahl(bd, 2, LPMain.getTheClient().getLocUi());

			wlaTagesarbeitszeit
					.setText(LPMain.getTextRespectUISPr("zeiterfassung.zeitdaten.anwesenheitszeit") + " " + sAnwesend);
		} else {

			wlaTagesarbeitszeit.setText(LPMain.getMessageTextRespectUISPr("zeiterfassung.zeitdaten.anwesendarbeitsoll",
					new String[] { Helper.formatZahl(dAnwesenheitszeit, 2, LPMain.getTheClient().getLocUi()),
							Helper.formatZahl(dArbeitszeitzeit, 2, LPMain.getTheClient().getLocUi()),
							Helper.formatZahl(dSollzeit, 2, LPMain.getTheClient().getLocUi()) }));
		}

		ZeitverteilungDto[] zv = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.zeitverteilungFindByPersonalIIdUndTag(internalFrameZeiterfassung.getPersonalDto().getIId(),
						wdfDatum.getTimestamp());

		wlaOffeneZeitverteilung.setVisible(zv != null && zv.length > 0);

		if (zv != null && zv.length > 0) {

			String sLabelVorhandene = "<html><font face=\"monospace\">";

			for (int k = 0; k < zv.length; k++) {

				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(zv[k].getLosIId());

				sLabelVorhandene += losDto.getCNr() + " / "
						+ Helper.formatTimestamp(zv[k].getTZeit(), LPMain.getTheClient().getLocUi());

				if (zv[k].getLossollarbeitsplanIId() != null) {
					LossollarbeitsplanDto saDto = DelegateFactory.getInstance().getFertigungDelegate()
							.lossollarbeitsplanFindByPrimaryKey(zv[k].getLossollarbeitsplanIId());

					
					if(zv[k].getMaschineIId()!=null) {
						 MaschineDto mDto=DelegateFactory.getInstance().getZeiterfassungDelegate().maschineFindByPrimaryKey(zv[k].getMaschineIId());
						 sLabelVorhandene += " M:"+mDto.getCIdentifikationsnr();
					}
					
					String aguag="("+saDto.getIArbeitsgangnummer();
					if(saDto.getIUnterarbeitsgang()!=null) {
						aguag+="/"+saDto.getIUnterarbeitsgang();
					}
					
					aguag+=")";
					
					ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(saDto.getArtikelIIdTaetigkeit());
					sLabelVorhandene += " / " + LPMain.getTextRespectUISPr("stkl.arbeitsplan.arbeitsgang")+" "+aguag;
					sLabelVorhandene += ": "
							+ Helper.fitString2LengthAlignRight(saDto.getIArbeitsgangnummer() + "", 3, ' ') + " ";
					sLabelVorhandene += aDto.formatArtikelbezeichnung();
					
					
					sLabelVorhandene += " StkZeit:"+ new BigDecimal(saDto.getLStueckzeit())
					.divide(new BigDecimal(3600000), 2, BigDecimal.ROUND_HALF_EVEN)+"h";
					
					
					
					
				}

				sLabelVorhandene += "<br>";

			}

			sLabelVorhandene += "</html>";

			wlaOffeneZeitverteilung.setToolTipText(sLabelVorhandene);
		}

		// PJ18736
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_AUTOMATISCHE_PAUSEN_NUR_WARNUNG,
						ParameterFac.KATEGORIE_PERSONAL, LPMain.getTheClient().getMandant());

		boolean bNurWarnung = (Boolean) parameter.getCWertAsObject();
		if (bNurWarnung == true
				&& (wlaFehlerInZeitdaten.getText() == null || wlaFehlerInZeitdaten.getText().isEmpty())) {
			wlaFehlerInZeitdaten
					.setText(DelegateFactory.getInstance().getZeiterfassungDelegate().getMeldungFehlenderMindestpauste(
							wdfDatum.getTimestamp(), internalFrameZeiterfassung.getPersonalDto().getIId()));

			if (zmtagDto != null && zmtagDto.getZeitmodellIId() != null) {

				// Fehler Tagesmaximum
				if (wlaFehlerInZeitdaten.getText() == null || wlaFehlerInZeitdaten.getText().isEmpty()) {
					if (zmtagDto.getUErlaubteanwesenheitszeit() != null
							&& zmtagDto.getUErlaubteanwesenheitszeit().getTime() != -3600000) {

						double dMaxAnwesenheit = Helper.time2Double(zmtagDto.getUErlaubteanwesenheitszeit());
						if (dAnwesenheitszeit != null && dAnwesenheitszeit > dMaxAnwesenheit) {
							// Fehler anzeigen

							MessageFormat mf = new MessageFormat(
									LPMain.getTextRespectUISPr("pers.zeitdaten.fehler.tagesmaximum"));
							mf.setLocale(LPMain.getTheClient().getLocUi());

							Object pattern[] = {
									Helper.formatZahl(dMaxAnwesenheit, 2, LPMain.getTheClient().getLocUi()),
									Helper.formatZahl(dAnwesenheitszeit - dMaxAnwesenheit, 2,
											LPMain.getTheClient().getLocUi()) };
							String sMsg = mf.format(pattern);

							wlaFehlerInZeitdaten.setText(sMsg);

						}

					}
				}

			}

			PersonalzeitmodellDto dto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalzeitmodellFindZeitmodellZuDatum(internalFrameZeiterfassung.getPersonalDto().getIId(),
							Helper.cutTimestamp(wdfDatum.getTimestamp()));
			// Fehler Wochenmaximum
			if (dto != null && dto.getZeitmodellIId() != null
					&& (wlaFehlerInZeitdaten.getText() == null || wlaFehlerInZeitdaten.getText().isEmpty())) {

				ZeitmodellDto zmDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitmodellFindByPrimaryKey(dto.getZeitmodellIId());

				if (zmDto.getNMaximalesWochenist() != null && zmDto.getNMaximalesWochenist().doubleValue() > 0) {
					Timestamp[] tVonBisEinerKW = Helper.getTimestampVonBisEinerKW(wdfDatum.getTimestamp());
					Double d = DelegateFactory.getInstance().getZeiterfassungDelegate().berechneArbeitszeitImZeitraum(
							internalFrameZeiterfassung.getPersonalDto().getIId(),
							new java.sql.Date(tVonBisEinerKW[0].getTime()),
							Helper.addiereTageZuDatum(new java.sql.Date(wdfDatum.getDate().getTime()), 1), false);

					if (d != null && d > zmDto.getNMaximalesWochenist().doubleValue()) {
						// Fehler anzeigen

						MessageFormat mf = new MessageFormat(
								LPMain.getTextRespectUISPr("pers.zeitdaten.fehler.wochenmaximum"));
						mf.setLocale(LPMain.getTheClient().getLocUi());

						Object pattern[] = {
								Helper.formatZahl(zmDto.getNMaximalesWochenist().doubleValue(), 2,
										LPMain.getTheClient().getLocUi()),
								Helper.formatZahl(d - zmDto.getNMaximalesWochenist().doubleValue(), 2,
										LPMain.getTheClient().getLocUi()) };
						String sMsg = mf.format(pattern);

						wlaFehlerInZeitdaten.setText(sMsg);

					}

				}

			}

		}

	}

	public void wcbRelativ_propertyChange(PropertyChangeEvent evt) {

	}

	public void wcbRelativ_stateChanged(ChangeEvent e) {
		// wtfZeit.requestFocus();

	}

	private Calendar getNowCalendar() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	private Time getNow() {
		return new Time(getNowCalendar().getTimeInMillis());
	}

	private Time getNowZeroSeconds() {
		Calendar c = getNowCalendar();
		c.set(Calendar.SECOND, 0);
		return new Time(c.getTimeInMillis());
	}

	private Timestamp getNowZeroSecondsAsTimestamp() {
		Calendar c = getNowCalendar();
		c.set(Calendar.SECOND, 0);
		return new Timestamp(c.getTimeInMillis());
	}

	public void wcbRelativ_actionPerformed(ActionEvent e) {
		if (wcbRelativ.isVisible()) {
			if (wcbRelativ.isSelected()) {
				try {
					java.sql.Time zeit = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.getRelativeZeitFuerRelativesAendernAmClient(zeitdatenDto.getPersonalIId(),
									zeitdatenDto.getTZeit());
					wtfZeit.setTime(zeit);
				} catch (Throwable ex) {
					// was tun???

				}
			} else {
//				Calendar c = Calendar.getInstance();
//				c.setTimeInMillis(System.currentTimeMillis());
//				c.set(Calendar.MILLISECOND, 0);
//				c.set(Calendar.SECOND, 0);
//				wtfZeit.setTime(new java.sql.Time(c.getTimeInMillis()));
				wtfZeit.setTime(getNowZeroSeconds());
			}
		}
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
			if (zeitdatenDto != null) {
				zeitdatenDto.setCBelegartnr(null);
				zeitdatenDto.setIBelegartpositionid(null);
				zeitdatenDto.setIBelegartid(null);
				zeitdatenDto.setArtikelIId(null);
			}

			wtfBeleg.setText(null);
			wtfPosition.setText(null);
			wlaBeleg.setText(null);
			wtfTaetigkeit.setText(null);

			// wtfBeleg.setMandatoryField(false);
			// wtfPosition.setMandatoryField(false);
			// wtfTaetigkeit.setMandatoryField(false);

			wcbRelativ.setVisible(false);

			// Nur Wenn Kommt geht nicht sichtbar

			if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdKommt)
					|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdGeht)) {
				enableBisZeit(false);
			} else {
				enableBisZeit(true);
			}

		} else {
			wtfPosition.setMandatoryField(true);
			wtfBeleg.setMandatoryField(true);
			wtfTaetigkeit.setMandatoryField(true);

			if (bDarfKommtGehtAendern == false && isHeute() == false
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
			wcbRelativ.setVisible(true);
			// wtfZeit_Bis.setVisible(true);
			enableBisZeit(true);

			if (lockstate == PanelBasis.LOCK_FOR_NEW || lockstate == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
				wbuPosition.setEnabled(true);
				wbuTaetigkeit.setEnabled(true);
			}
		}

		if (lockstate == PanelBasis.LOCK_IS_NOT_LOCKED || lockstate == PanelBasis.LOCK_FOR_EMPTY) {
			try {
				if (isHeute() && DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD)) {

					wbuKommt.setEnabled(true);
					wbuGeht.setEnabled(true);
					wbuUnter.setEnabled(true);
					wbuEnde.setEnabled(true);

					if (!getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung().isBVonBisErfassung()) {

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

			} catch (Throwable e1) {
				handleException(e1, false);
			}

		}

		if (bDarfKommtGehtAendern == false) {

			if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {

				try {

					if (!wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(taetigkeitIIdEnde)) {
						wtfZeit.setEnabled(false);
//						wtfZeit.setTime(new java.sql.Time(System.currentTimeMillis()));
						wtfZeit.setTime(getNowZeroSeconds());
					} else {
						wtfZeit.setEnabled(true);
					}
				} catch (Throwable e1) {
					handleException(e1, true);
				}

			}

		}
		updatePosAndTaetigkeitFelder(selectedBelegart, wcoSonderTaetigkeit.getKeyOfSelectedItem());

	}

	protected void calcBis() throws ExceptionLP {
		long time = wtfZeit.getTime().getTime() + wdfDauer.getDuration();
		wtfZeit_Bis.setTime(new Time(time));
		if (wtfZeit_Bis.getTime().before(wtfZeit.getTime()))
			wdfDauer.setDuration(0);
	}

	protected void calcDauer() throws ExceptionLP {
		long duration = wtfZeit_Bis.getTime().getTime() - wtfZeit.getTime().getTime();
		duration = Math.round(duration / 60000f) * 60000;
		wdfDauer.setDuration(duration);
	}

	private class VonBisDauerListener implements IHvValueHolderListener {

		@Override
		public void valueChanged(Component reference, Object oldValue, Object newValue) {
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

class PanelZeitdaten_wcoSonderTaetigkeit_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcoSonderTaetigkeit_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoSonderTaetigkeit_actionPerformed(e);
	}
}

class PanelZeitdaten_wcbRelativ_changeAdapter implements ChangeListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcbRelativ_changeAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void stateChanged(ChangeEvent e) {
		adaptee.wcbRelativ_stateChanged(e);
	}
}

class PanelZeitdaten_wcbRelativ_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcbRelativ_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbRelativ_actionPerformed(e);
	}
}

class PanelZeitdaten_wcbRelativ_propertyChangeAdapter implements PropertyChangeListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcbRelativ_propertyChangeAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		adaptee.wcbRelativ_propertyChange(evt);
	}
}

class PanelZeitdaten_wbuNaechsterTag_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wbuNaechsterTag_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuNaechsterTag_actionPerformed(e);
	}
}

class PanelZeitdaten_wbuTagZurueck_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wbuTagZurueck_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuTagZurueck_actionPerformed(e);
	}
}
