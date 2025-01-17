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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSlider;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

public class PanelReisezeiten extends PanelBasis implements PropertyChangeListener, ChangeListener {

	private static final long serialVersionUID = 5817369209583609394L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	protected ReiseDto reiseDto = null;

	private Integer selectedBelegIId = null;

	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;

	private WrapperRadioButton wrbBeginn = new WrapperRadioButton();
	private WrapperRadioButton wrbEnde = new WrapperRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperLabel wlaZeit = new WrapperLabel();
	private WrapperTimestampField wtfZeit = new WrapperTimestampField();

	private JPanel panelBeginn = new JPanel(new GridBagLayout());
	private JPanel panelEnde = new JPanel(new GridBagLayout());

	private WrapperLabel wlaErstesKommt = new WrapperLabel();
	private WrapperLabel wlaLetztesGeht = new WrapperLabel();

	private boolean bZeitdatenAufErledigteBuchbar = false;

	private WrapperLabel wlaMitahrer = new WrapperLabel();
	private WrapperNumberField wnfMitfahrer = new WrapperNumberField(0, 99);

	private WrapperGotoButton wbuKunde = new WrapperGotoButton(GotoHelper.GOTO_KUNDE_AUSWAHL);
	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperButton wbuLand = new WrapperButton();
	private WrapperLabel wlaKmBeginnOderEnde = new WrapperLabel();
	private WrapperLabel wlaEntfernung = new WrapperLabel();
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperLabel wlaFahrzeug = new WrapperLabel();
	private WrapperLabel wlaSpesen = new WrapperLabel();
	private WrapperLabel wlaZusaetzlicheSpesen = new WrapperLabel();
	private WrapperTextField wtfReiseland = new WrapperTextField();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	public WrapperTextField wtfFahrzeug = new WrapperTextField();
	private WrapperNumberField wnfKmBeginnOderEnde = new WrapperNumberField(0, 9999999);
	private WrapperNumberField wnfEntfernung = new WrapperNumberField(0, 9999999);
	private WrapperNumberField wnfSpesen = new WrapperNumberField(0, 9999999);
	private WrapperNumberField wnfZusaetzlicheSpesen = new WrapperNumberField(0, 9999999);

	public WrapperButton wbuPersonalfahrzeug = new WrapperButton();
	public WrapperTextField wtfPersonalfahrzeug = new WrapperTextField();

	private WrapperLabel wlaLetzterKmBeginn = new WrapperLabel();
	private WrapperNumberField wnfLetzterKmBeginn = new WrapperNumberField(0, 9999999);

	private WrapperComboBox wcoBeleg = new WrapperComboBox();
	private WrapperGotoButton wbuBeleg = new WrapperGotoButton(-1);
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRProjekt = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	private WrapperComboBox wcbVerrechenbar = createWcbVerrechenbar();

	private WrapperLabel wlaWaehrungSpesen = new WrapperLabel();
	private WrapperLabel wlaWaehrungSpesen2 = new WrapperLabel();

	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRReiseland = null;
	private AnsprechpartnerDto ansprechpartnerDto = null;

	private Timestamp tLetzterTimestamp = null;

	private Integer diaetenIId_Reiseland_Zuletztgewaehlt = null;

	private static final String ACTION_SPECIAL_KUNDE = "ACTION_SPECIAL_KUNDE";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER = "ACTION_SPECIAL_ANSPRECHPARTNER";
	static final public String ACTION_SPECIAL_LAND_FROM_LISTE = "ACTION_SPECIAL_LAND_FROM_LISTE";
	static final public String ACTION_SPECIAL_BELEG_FROM_LISTE = "action_beleg_from_liste";

	private ZeiterfassungPruefer zeiterfassungPruefer = null;

	private PanelQueryFLR panelQueryFLRFahrzeug = null;

	static final public String ACTION_SPECIAL_FAHRZEUG_FROM_LISTE = "ACTION_SPECIAL_FAHRZEUG_FROM_LISTE";

	public PanelReisezeiten(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
		zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfZeit;
	}

	protected void setDefaults() {
	}

	public void propertyChange(PropertyChangeEvent e) {

		if ((e.getSource() == wtfZeit.getWdfDatum().getDisplay()
				&& (e.getPropertyName().equals("date") || e.getPropertyName().equals("value"))
				&& wtfZeit.getWdfDatum().getTimestamp() != null)) {

			try {

				// Letzten KM-Bginn holen
				if (e.getNewValue() != null && e.getNewValue() instanceof java.util.Date) {
					zeitVorschlagen();
					labelsLetzteKommtErstesGehtAktualisieren();

					java.util.Date d = (Date) e.getNewValue();

					GregorianCalendar gcZeit = new GregorianCalendar();
					gcZeit.setTimeInMillis(wtfZeit.getWtfZeit().getTime().getTime());

					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(wtfZeit.getTimestamp());
					gc.set(GregorianCalendar.HOUR_OF_DAY, gcZeit.get(GregorianCalendar.HOUR_OF_DAY));
					gc.set(GregorianCalendar.MINUTE, gcZeit.get(GregorianCalendar.MINUTE));

					ReiseDto reiseDto = DelegateFactory.getInstance().getZeiterfassungDelegate().getReiseDtoVorgaenger(
							new Timestamp(d.getTime()), internalFrameZeiterfassung.getPersonalDto().getIId());
					if (reiseDto != null && Helper.short2Boolean(reiseDto.getBBeginn())) {
						wnfLetzterKmBeginn.setInteger(reiseDto.getIKmbeginn());
						wlaLetzterKmBeginn.setVisible(true);
						wnfLetzterKmBeginn.setVisible(true);

						wlaEntfernung.setVisible(true);
						wnfEntfernung.setVisible(true);
					} else {
						wlaLetzterKmBeginn.setVisible(false);
						wnfLetzterKmBeginn.setVisible(false);

						wlaEntfernung.setVisible(false);
						wnfEntfernung.setVisible(false);
					}
				}

			} catch (Throwable e1) {
				handleException(e1, true);
			}

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		reiseDto = new ReiseDto();

		leereAlleFelder(this);

		wcbVerrechenbar.setKeyOfSelectedItem(100D);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER)) {
			if (reiseDto.getPartnerIId() != null) {
				dialogQueryAnsprechpartner(e);
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("rechnung.kundewaehlen"));
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAND_FROM_LISTE)) {
			dialogQueryLandFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FAHRZEUG_FROM_LISTE)) {
			dialogQueryFahrzeugFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BELEG_FROM_LISTE)) {
			if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_AUFTRAG)) {
				dialogQueryAuftragFromListe(e);
			} else if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_PROJEKT)) {
				dialogQueryProjektFromListe(e);
			}
		}

		if (e.getSource().equals(wcoBeleg)) {
			wtfBeleg.setText(null);
			selectedBelegIId = null;

			if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_AUFTRAG)) {
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
			} else if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_PROJEKT)) {
				wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_PROJEKT_AUSWAHL);
			}

		}

	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance().createPanelFLRProjekt(getInternalFrame(),
				reiseDto.getIBelegartid(), true);
		new DialogQuery(panelQueryFLRProjekt);

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
		panelQueryFLRAuftrag.setSelectedId(reiseDto.getIBelegartid());

		new DialogQuery(panelQueryFLRAuftrag);

	}

	/**
	 * Dialogfenster zur Ansprechpartnerauswahl.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		Integer ansprechpartnerIId = null;
		if (ansprechpartnerDto != null) {
			ansprechpartnerIId = ansprechpartnerDto.getIId();
		}

		panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
				getInternalFrame(), reiseDto.getPartnerIId(), ansprechpartnerIId, true, true);

		new DialogQuery(panelQueryFLRAnsprechpartner);
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true, true);
		if (reiseDto.getPartnerIId() != null) {
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByiIdPartnercNrMandantOhneExc(
					reiseDto.getPartnerIId(), LPMain.getTheClient().getMandant());
			if (kundeDto != null) {
				panelQueryFLRKunde.setSelectedId(kundeDto.getIId());
			}
		}

		new DialogQuery(panelQueryFLRKunde);
	}

	private void dialogQueryLandFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRReiseland = new PanelQueryFLR(null, null, QueryParameters.UC_ID_DIAETEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("title.landauswahlliste"));

		FilterKriteriumDirekt fKD1 = new FilterKriteriumDirekt(ZeiterfassungFac.FLR_DIAETEN_FLRLAND + ".c_lkz", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("lp.land"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, true, Facade.MAX_UNBESCHRAENKT);

		panelQueryFLRReiseland.befuellePanelFilterkriterienDirekt(fKD1, null);

		panelQueryFLRReiseland.setSelectedId(reiseDto.getDiaetenIId());

		new DialogQuery(panelQueryFLRReiseland);
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (zeiterfassungPruefer.pruefeObBuchungMoeglich(wtfZeit.getTimestamp(),
				internalFrameZeiterfassung.getPersonalDto().getIId())) {
			DelegateFactory.getInstance().getZeiterfassungDelegate().removeReise(reiseDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false);
		}
	}

	protected void components2Dto() throws ExceptionLP {

		if (wrbBeginn.isSelected()) {
			reiseDto.setBBeginn(Helper.boolean2Short(true));
			reiseDto.setIKmbeginn(wnfKmBeginnOderEnde.getInteger());
			reiseDto.setFVerrechenbar((Double) wcbVerrechenbar.getKeyOfSelectedItem());
			reiseDto.setNSpesen(wnfSpesen.getBigDecimal());

			reiseDto.setIMitfahrer(wnfMitfahrer.getInteger());

			reiseDto.setCFahrzeug(wtfFahrzeug.getText());
			if (wtfFahrzeug.getText() != null) {
				reiseDto.setFahrzeugIId(null);
			}
		} else {
			reiseDto.setBBeginn(Helper.boolean2Short(false));
			reiseDto.setDiaetenIId(null);
			reiseDto.setIKmende(wnfKmBeginnOderEnde.getInteger());
		}
		reiseDto.setTZeit(wtfZeit.getTimestamp());
		reiseDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto().getIId());

		reiseDto.setCKommentar(wtfKommentar.getText());

	}

	void dialogQueryFahrzeugFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_FILTER,
				PanelBasis.ACTION_LEEREN };

		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium(PersonalFac.FILTER_BERECHTIGTE_FAHRZEUGE, true,
				internalFrameZeiterfassung.getPersonalDto().getIId() + "", FilterKriterium.OPERATOR_EQUAL, false);

		panelQueryFLRFahrzeug = new PanelQueryFLR(null, kriterien, QueryParameters.UC_ID_FAHRZEUG, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("pers.fahrzeug"));
		panelQueryFLRFahrzeug
				.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

		panelQueryFLRFahrzeug.setSelectedId(reiseDto.getFahrzeugIId());

		new DialogQuery(panelQueryFLRFahrzeug);
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		if (Helper.short2Boolean(reiseDto.getBBeginn())) {
			wrbBeginn.setSelected(true);
			wrbBeginn_actionPerformed(null);
			if (reiseDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(reiseDto.getAnsprechpartnerIId());
				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatFixTitelName1Name2());
			} else {
				wtfAnsprechpartner.setText(null);
			}
			if (reiseDto.getPartnerIId() != null) {
				PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
						.partnerFindByPrimaryKey(reiseDto.getPartnerIId());

				KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByiIdPartnercNrMandantOhneExc(reiseDto.getPartnerIId(),
								LPMain.getTheClient().getMandant());

				wtfKunde.setText(partnerDto.formatFixTitelName1Name2());
				if (kdDto != null) {
					wbuKunde.setOKey(kdDto.getIId());
				}
			} else {
				wtfKunde.setText(null);
			}
			DiaetenDto diaetenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.diaetenFindByPrimaryKey(reiseDto.getDiaetenIId());
			wtfReiseland.setText(diaetenDto.getCBez());
			wcbVerrechenbar.setKeyOfSelectedItem(reiseDto.getFVerrechenbar());

			wnfKmBeginnOderEnde.setInteger(reiseDto.getIKmbeginn());

		} else {

			wrbEnde.setSelected(true);
			wrbEnde_actionPerformed(null);
			enableAllComponents(panelEnde, false);
			wtfAnsprechpartner.setText(null);
			wtfKunde.setText(null);
			wtfReiseland.setText(null);
			wnfKmBeginnOderEnde.setInteger(reiseDto.getIKmende());
		}

		wnfZusaetzlicheSpesen.setBigDecimal(DelegateFactory.getInstance().getZeiterfassungDelegate()
				.getZusaetzlicheReisespesenInMandantenwaehrung(reiseDto.getIId()));

		wtfZeit.setTimestamp(reiseDto.getTZeit());
		wtfFahrzeug.setText(reiseDto.getCFahrzeug());
		wnfMitfahrer.setInteger(reiseDto.getIMitfahrer());
		wtfKommentar.setText(reiseDto.getCKommentar());

		if (reiseDto.getFahrzeugIId() != null) {
			FahrzeugDto fzDto = DelegateFactory.getInstance().getPersonalDelegate()
					.fahrzeugFindByPrimaryKey(reiseDto.getFahrzeugIId());
			wtfPersonalfahrzeug.setText(fzDto.getCBez());
		} else {
			wtfPersonalfahrzeug.setText(null);
		}

		if (wnfKmBeginnOderEnde.getInteger() != null && wnfLetzterKmBeginn.getInteger() != null) {
			wnfEntfernung.setInteger(wnfKmBeginnOderEnde.getInteger() - wnfLetzterKmBeginn.getInteger());
		} else {
			wnfEntfernung.setInteger(null);
		}

		wnfSpesen.setBigDecimal(reiseDto.getNSpesen());

		wcoBeleg.setKeyOfSelectedItem(reiseDto.getBelegartCNr());
		wbuBeleg.setOKey(reiseDto.getIBelegartid());

		if (reiseDto.getBelegartCNr() != null) {
			if (reiseDto.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {

				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(reiseDto.getIBelegartid());

				String sProjBez = "";
				if (auftragDto.getCBezProjektbezeichnung() != null) {
					sProjBez = ", " + auftragDto.getCBezProjektbezeichnung();
				}

				wtfBeleg.setText(auftragDto.getCNr() + sProjBez);
				selectedBelegIId = auftragDto.getIId();
			} else if (reiseDto.getBelegartCNr().equals(LocaleFac.BELEGART_PROJEKT)) {

				ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate()
						.projektFindByPrimaryKey(reiseDto.getIBelegartid());

				String sProjBez = "";
				if (projektDto.getCTitel() != null) {
					sProjBez = ", " + projektDto.getCTitel();
				}
				wtfBeleg.setText(projektDto.getCNr() + sProjBez);
				selectedBelegIId = projektDto.getIId();

			}
		} else {
			wtfBeleg.setText(null);
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {

		if (zeiterfassungPruefer.pruefeObBuchungMoeglich(wtfZeit.getTimestamp(),
				internalFrameZeiterfassung.getPersonalDto().getIId())) {

			super.eventActionUpdate(aE, bNeedNoUpdateI);
			enableAllComponents(panelEnde, true);
			enableAllComponents(panelBeginn, true);

			// Wenn Fahrzeug manuell eingetragen wurde, dann auswahl ueber Button
			// nicht m?glich
			if (wtfFahrzeug.getText() != null) {
				wbuPersonalfahrzeug.setEnabled(false);
			}
		} else {
			return;
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if (zeiterfassungPruefer.pruefeObBuchungMoeglich(wtfZeit.getTimestamp(),
					internalFrameZeiterfassung.getPersonalDto().getIId())) {
				components2Dto();
				/*
				 * if (wnfBeginnOderEnde.getInteger() != null && wnfKmEnde.getInteger() != null)
				 * { if (wnfBeginnOderEnde.getInteger() > wnfKmEnde.getInteger()) {
				 * DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
				 * LPMain.getTextRespectUISPr("pers.reisezeiten.error")); return; } }
				 */

				if (reiseDto.getIId() == null) {
					components2Dto();
					tLetzterTimestamp = wtfZeit.getTimestamp();
					reiseDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate().createReise(reiseDto));
					setKeyWhenDetailPanel(reiseDto.getIId());
				} else {
					DelegateFactory.getInstance().getZeiterfassungDelegate().updateReise(reiseDto);
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(reiseDto.getIId() + "");
				}
				eventYouAreSelected(false);
			}
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) key);
				reiseDto.setAnsprechpartnerIId(ansprechpartnerDto.getIId());
				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRKunde) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(key);
				reiseDto.setPartnerIId(kundeDto.getPartnerIId());
				wtfKunde.setText(kundeDto.getPartnerDto().formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRFahrzeug) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				FahrzeugDto apDto = DelegateFactory.getInstance().getPersonalDelegate()
						.fahrzeugFindByPrimaryKey((Integer) key);
				wtfPersonalfahrzeug.setText(apDto.getCBez());
				reiseDto.setFahrzeugIId(apDto.getIId());

			} else if (e.getSource() == panelQueryFLRReiseland) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				DiaetenDto diaetenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.diaetenFindByPrimaryKey((Integer) key);

				diaetenIId_Reiseland_Zuletztgewaehlt = diaetenDto.getIId();

				wtfReiseland.setText(diaetenDto.getCBez());
				reiseDto.setDiaetenIId(diaetenDto.getIId());
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					AuftragDto auftragDto = null;
					auftragDto = DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(key);

					reiseDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
					reiseDto.setIBelegartid(auftragDto.getIId());

					// Kunde und Reiseland vorbesetzen

					String projBez = ", " + auftragDto.getCBezProjektbezeichnung();

					wtfBeleg.setText(auftragDto.getCNr() + projBez);
					selectedBelegIId = key;

					KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());
					reiseDto.setPartnerIId(kundeDto.getPartnerIId());
					wtfKunde.setText(kundeDto.getPartnerDto().formatFixTitelName1Name2());
					wtfReiseland.setText(null);
					reiseDto.setDiaetenIId(null);
					if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
						DiaetenDto[] diaetenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.diaetenFindByLandIId(
										kundeDto.getPartnerDto().getLandplzortDto().getLandDto().getIID());

						if (diaetenDto.length > 0) {
							wtfReiseland.setText(diaetenDto[0].getCBez());
							reiseDto.setDiaetenIId(diaetenDto[0].getIId());
						}

					}
					wcbVerrechenbar.setKeyOfSelectedItem(100D);
				}
			} else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					ProjektDto projektDto = null;
					projektDto = DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(key);
					reiseDto.setBelegartCNr(LocaleFac.BELEGART_PROJEKT);
					reiseDto.setIBelegartid(projektDto.getIId());

					PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(projektDto.getPartnerIId());

					reiseDto.setPartnerIId(partnerDto.getIId());
					wtfKunde.setText(partnerDto.formatFixTitelName1Name2());
					wtfReiseland.setText(null);
					reiseDto.setDiaetenIId(null);
					if (partnerDto.getLandplzortDto() != null) {
						DiaetenDto[] diaetenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.diaetenFindByLandIId(partnerDto.getLandplzortDto().getLandDto().getIID());

						if (diaetenDto.length > 0) {
							wtfReiseland.setText(diaetenDto[0].getCBez());
							reiseDto.setDiaetenIId(diaetenDto[0].getIId());
						}

					}

					String projBez = projektDto.getCTitel();
					if (projBez == null) {
						projBez = "";
					}

					wtfBeleg.setText(projektDto.getCNr() + ", " + projBez);
					selectedBelegIId = key;

				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKunde) {
				reiseDto.setPartnerIId(null);
				wtfKunde.setText(null);
			}

			if (e.getSource() == panelQueryFLRAuftrag || e.getSource() == panelQueryFLRProjekt) {
				reiseDto.setBelegartCNr(null);
				reiseDto.setIBelegartid(null);
				wtfBeleg.setText(null);
				selectedBelegIId = null;
			}
			if (e.getSource() == panelQueryFLRFahrzeug) {
				reiseDto.setFahrzeugIId(null);
				wtfPersonalfahrzeug.setText(null);
			}
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH,
						ParameterFac.KATEGORIE_PERSONAL, LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufErledigteBuchbar = true;
		}

		wrbBeginn.setText(LPMain.getTextRespectUISPr("lp.beginn"));
		wrbEnde.setText(LPMain.getTextRespectUISPr("lp.ende"));

		wrbBeginn.setMnemonic('B');
		wrbBeginn.setSelected(true);
		wrbBeginn.addActionListener(new PanelReisezeiten_wrbBeginn_actionAdapter(this));
		wrbEnde.addActionListener(new PanelReisezeiten_wrbEnde_actionAdapter(this));
		wrbEnde.setMnemonic('E');

		wlaZeit.setText(LPMain.getTextRespectUISPr("lp.datum"));

		wlaErstesKommt.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.ersteskommt"));
		wlaLetztesGeht.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.letztesgeht"));

		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE);
		wbuKunde.addActionListener(this);

		wbuBeleg.setText(LPMain.getTextRespectUISPr("lp.beleg") + "...");
		wbuBeleg.setActionCommand(ACTION_SPECIAL_BELEG_FROM_LISTE);
		wbuBeleg.addActionListener(this);

		wbuPersonalfahrzeug.setText(LPMain.getTextRespectUISPr("pers.fahrzeug") + "...");
		wbuPersonalfahrzeug.setActionCommand(ACTION_SPECIAL_FAHRZEUG_FROM_LISTE);
		wbuPersonalfahrzeug.addActionListener(this);
		wtfPersonalfahrzeug.setActivatable(false);

		Map<String, String> mBelegarten = new TreeMap<String, String>();
		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
			mBelegarten.put(LocaleFac.BELEGART_AUFTRAG, LPMain.getTextRespectUISPr("auft.auftrag"));
		}
		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)) {
			mBelegarten.put(LocaleFac.BELEGART_PROJEKT, LPMain.getTextRespectUISPr("proj.projekt"));
		}
		wcoBeleg.setMandatoryField(true);
		wcoBeleg.setMap(mBelegarten);
		wtfBeleg.setActivatable(false);
		wtfBeleg.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wcoBeleg.addActionListener(this);

		wbuAnsprechpartner.setText(LPMain.getTextRespectUISPr("button.ansprechpartner.long"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wbuLand.setText(LPMain.getTextRespectUISPr("pers.reiseland") + "...");
		wbuLand.setActionCommand(ACTION_SPECIAL_LAND_FROM_LISTE);
		wbuLand.addActionListener(this);
		wlaKmBeginnOderEnde.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.kmbeginn"));
		wlaEntfernung.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.entfernung"));
		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));
		wlaFahrzeug.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.fahrzeug"));
		wlaSpesen.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.spesen"));
		wlaZusaetzlicheSpesen.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.zusspesen"));

		wlaLetzterKmBeginn.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.kmbeginnvorgaenger"));

		wlaWaehrungSpesen.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungSpesen.setText(DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr());

		wlaWaehrungSpesen2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungSpesen2.setText(DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr());

		wnfEntfernung.addFocusListener(new PanelReisezeiten_wnfEntfernung_focusAdapter(this));
		wnfKmBeginnOderEnde.addFocusListener(new PanelReisezeiten_wnfKmBeginn_focusAdapter(this));

		wnfLetzterKmBeginn.setActivatable(false);
		wnfLetzterKmBeginn.setDependenceField(true);

		wtfReiseland.setActivatable(false);
		wtfAnsprechpartner.setActivatable(false);
		wtfKunde.setActivatable(false);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfKommentar.setColumnsMax(ZeiterfassungFac.MAX_REISEZEITEN_KOMMENTAR);

		wtfZeit.setMandatoryField(true);
		wtfZeit.getWdfDatum().getDisplay().addPropertyChangeListener(this);
		wtfZeit.getWtfZeit().getWspStunden().addChangeListener(this);
		wtfZeit.getWtfZeit().getWspMinuten().addChangeListener(this);

		wlaZeit.setLabelFor(wtfZeit);
		wlaZeit.setDisplayedMnemonic('D');

		wlaKommentar.setLabelFor(wtfKommentar);
		wlaKommentar.setDisplayedMnemonic('M');

		wbuAnsprechpartner.setMnemonic('A');
		wbuKunde.setMnemonic('K');
		wbuLand.setMnemonic('R');

		wtfFahrzeug.addKeyListener(new PanelReisezeiten_wtfFahrzeug_keyListener(this));

		wbuBeleg.setMnemonic('L');

		wlaFahrzeug.setLabelFor(wtfFahrzeug);
		wlaFahrzeug.setDisplayedMnemonic('F');

		wlaMitahrer.setText(LPMain.getTextRespectUISPr("pers.reise.mitfahrer"));

		wlaEntfernung.setLabelFor(wnfEntfernung);
		wlaEntfernung.setDisplayedMnemonic('U');

		wlaSpesen.setLabelFor(wnfSpesen);
		wlaSpesen.setDisplayedMnemonic('S');

		wnfZusaetzlicheSpesen.setActivatable(false);
		wnfZusaetzlicheSpesen.setDependenceField(true);

		wlaKmBeginnOderEnde.setLabelFor(wnfKmBeginnOderEnde);
		wlaKmBeginnOderEnde.setDisplayedMnemonic('G');
		wlaKmBeginnOderEnde.setLabelFor(wnfKmBeginnOderEnde);
		wlaKmBeginnOderEnde.setDisplayedMnemonic('N');
		wbuPersonalfahrzeug.setMnemonic('Z');

		wtfReiseland.setMandatoryField(true);

		wnfKmBeginnOderEnde.setFractionDigits(0);
		wnfKmBeginnOderEnde.setFractionDigits(0);
		wnfEntfernung.setFractionDigits(0);

		wnfMitfahrer.setFractionDigits(0);

		HelperClient.setMinimumAndPreferredSize(wtfZeit, HelperClient.getSizeFactoredDimension(50));

		HelperClient.setMinimumAndPreferredSize(wnfKmBeginnOderEnde, HelperClient.getSizeFactoredDimension(50));

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wrbBeginn, new GridBagConstraints(0, 0, 1, 1, 0.15, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbEnde, new GridBagConstraints(1, 0, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaErstesKommt, new GridBagConstraints(4, 0, 3, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaZeit, new GridBagConstraints(0, 1, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZeit, new GridBagConstraints(1, 1, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 170, 0));
		jpaWorkingOn.add(wlaLetztesGeht, new GridBagConstraints(4, 1, 3, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaLetzterKmBeginn, new GridBagConstraints(1, 2, 1, 1, 0.15, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfLetzterKmBeginn, new GridBagConstraints(2, 2, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wlaEntfernung, new GridBagConstraints(3, 2, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfEntfernung, new GridBagConstraints(4, 2, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wlaKmBeginnOderEnde, new GridBagConstraints(5, 2, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfKmBeginnOderEnde, new GridBagConstraints(6, 2, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// PANELBEGINN
		int iZeileBeginn = 0;

		panelBeginn.add(wbuBeleg, new GridBagConstraints(0, iZeileBeginn, 1, 1, 0.15, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		panelBeginn.add(wcoBeleg, new GridBagConstraints(1, iZeileBeginn, 1, 1, 0.15, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		panelBeginn.add(wtfBeleg, new GridBagConstraints(2, iZeileBeginn, 1, 1, 0.4, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		panelBeginn.add(wlaFahrzeug, new GridBagConstraints(3, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wtfFahrzeug, new GridBagConstraints(4, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelBeginn.add(wlaMitahrer, new GridBagConstraints(5, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 40, 0));
		panelBeginn.add(wnfMitfahrer, new GridBagConstraints(6, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 60, 0));

		iZeileBeginn++;

		panelBeginn.add(wbuKunde, new GridBagConstraints(0, iZeileBeginn, 1, 1, 0.15, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wtfKunde, new GridBagConstraints(1, iZeileBeginn, 2, 1, 0.5, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelBeginn.add(wbuPersonalfahrzeug, new GridBagConstraints(3, iZeileBeginn, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		panelBeginn.add(wtfPersonalfahrzeug, new GridBagConstraints(4, iZeileBeginn, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG)) {

			panelBeginn.add(new WrapperLabel(LPMain.getTextRespectUISPr("pers.reisezeiten.verr")),
					new GridBagConstraints(5, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 40, 0));

			panelBeginn.add(wcbVerrechenbar, new GridBagConstraints(6, iZeileBeginn, 1, 1, 0.1, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

			wcbVerrechenbar.setKeyOfSelectedItem(100D);
		}

		iZeileBeginn++;

		panelBeginn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeileBeginn, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeileBeginn, 2, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wlaSpesen, new GridBagConstraints(3, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		panelBeginn.add(wnfSpesen, new GridBagConstraints(4, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		panelBeginn.add(wlaWaehrungSpesen, new GridBagConstraints(5, iZeileBeginn, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));

		iZeileBeginn++;
		panelBeginn.add(wbuLand, new GridBagConstraints(0, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wtfReiseland, new GridBagConstraints(1, iZeileBeginn, 2, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelBeginn.add(wlaZusaetzlicheSpesen, new GridBagConstraints(3, iZeileBeginn, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wnfZusaetzlicheSpesen, new GridBagConstraints(4, iZeileBeginn, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wlaWaehrungSpesen2, new GridBagConstraints(5, iZeileBeginn, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));
		iZeileBeginn++;
		panelBeginn.add(wlaKommentar, new GridBagConstraints(0, iZeileBeginn, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelBeginn.add(wtfKommentar, new GridBagConstraints(1, iZeileBeginn, 6, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// PANELENDE

		int iZeileEnde = 0;

		iZeileEnde++;

		iZeileEnde++;
		panelEnde.add(new WrapperLabel(""), new GridBagConstraints(0, iZeileEnde, 1, 1, 0.1, .5,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// -------------
		jpaWorkingOn.add(panelBeginn, new GridBagConstraints(0, 2, 5, 5, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		buttonGroup.add(wrbBeginn);
		buttonGroup.add(wrbEnde);

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_REISE;
	}

	public void labelsLetzteKommtErstesGehtAktualisieren() throws Throwable {

		Timestamp tErstesKommt = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.getErstesKommtEinesTages(internalFrameZeiterfassung.getPersonalDto().getIId(), wtfZeit.getTimestamp());
		Timestamp tLetztesGeht = DelegateFactory.getInstance().getZeiterfassungDelegate()
				.getLetztesGehtEinesTages(internalFrameZeiterfassung.getPersonalDto().getIId(), wtfZeit.getTimestamp());

		String erstesKommt = LPMain.getTextRespectUISPr("pers.reisezeiten.ersteskommt");

		if (tErstesKommt != null) {
			erstesKommt += " " + Helper.formatTimestamp(tErstesKommt, LPMain.getTheClient().getLocUi());
		} else {
			erstesKommt += " X";
		}

		wlaErstesKommt.setText(erstesKommt);

		String letztesGeht = LPMain.getTextRespectUISPr("pers.reisezeiten.letztesgeht");

		if (tLetztesGeht != null) {
			letztesGeht += " " + Helper.formatTimestamp(tLetztesGeht, LPMain.getTheClient().getLocUi());
		} else {
			letztesGeht += " X";
		}

		wlaLetztesGeht.setText(letztesGeht);

	}

	// PJ21203
	private Timestamp zeitAufMintenRunden(Timestamp t) {

		if (t != null) {
			Calendar cl = Calendar.getInstance();
			cl.setTimeInMillis(t.getTime());
			cl.set(Calendar.MILLISECOND, 0);
			int sekunden = cl.get(Calendar.SECOND);

			if (sekunden > 0) {
				if (sekunden >= 31) {
					cl.add(Calendar.MINUTE, 1);
				}
				cl.set(Calendar.SECOND, 0);
			}

			t = new Timestamp(cl.getTimeInMillis());
		}
		return t;
	}

	public void zeitVorschlagen() throws Throwable {
		Timestamp tZeit = null;

		if (reiseDto == null || reiseDto.getIId() == null) {
			if (wtfZeit.getTimestamp() != null && getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {

				Timestamp tErstesKommt = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.getErstesKommtEinesTages(internalFrameZeiterfassung.getPersonalDto().getIId(),
								wtfZeit.getTimestamp());
				Timestamp tLetztesGeht = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.getLetztesGehtEinesTages(internalFrameZeiterfassung.getPersonalDto().getIId(),
								wtfZeit.getTimestamp());

				if (wrbBeginn.isSelected()) {
					// Darf nur bei der ersten Beginnbuchung vorgeschlagen
					// werden

					if (DelegateFactory.getInstance().getZeiterfassungDelegate().sindReisezeitenZueinemTagVorhanden(
							internalFrameZeiterfassung.getPersonalDto().getIId(), wtfZeit.getTimestamp()) == false) {

						tZeit = zeitAufMintenRunden(tErstesKommt);
					}
				} else {
					tZeit = zeitAufMintenRunden(tLetztesGeht);
				}
				if (tZeit != null) {
					wtfZeit.getWtfZeit().setTime(new java.sql.Time(tZeit.getTime()));
				}
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

			if (tLetzterTimestamp == null) {
				wtfZeit.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
			} else {
				wtfZeit.setTimestamp(tLetzterTimestamp);

			}

			if (key != null) {
				// PJ15280

				if (diaetenIId_Reiseland_Zuletztgewaehlt != null) {

					DiaetenDto diaetenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.diaetenFindByPrimaryKey(diaetenIId_Reiseland_Zuletztgewaehlt);
					wtfReiseland.setText(diaetenDto.getCBez());
					reiseDto.setDiaetenIId(diaetenIId_Reiseland_Zuletztgewaehlt);

				} else {

					MandantDto mDto = DelegateFactory.getInstance().getMandantDelegate()
							.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());
					if (mDto.getPartnerDto().getLandplzortDto() != null) {
						DiaetenDto[] dtos = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.diaetenFindByLandIId(mDto.getPartnerDto().getLandplzortDto().getIlandID());
						if (dtos.length > 0) {
							diaetenIId_Reiseland_Zuletztgewaehlt = dtos[0].getIId();
							wtfReiseland.setText(dtos[0].getCBez());
							reiseDto.setDiaetenIId(diaetenIId_Reiseland_Zuletztgewaehlt);
						}
					}

				}

				zeitVorschlagen();
				labelsLetzteKommtErstesGehtAktualisieren();

			}
			wcbVerrechenbar.setKeyOfSelectedItem(100D);
			if (key != null && key.equals(LPMain.getLockMeForNew())) {
				enableAllComponents(panelEnde, true);
				enableAllComponents(panelBeginn, true);

			}
		} else {
			reiseDto = DelegateFactory.getInstance().getZeiterfassungDelegate().reiseFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}

	void wrbBeginn_actionPerformed(ActionEvent e) {
		jpaWorkingOn.remove(panelEnde);

		wlaKmBeginnOderEnde.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.kmbeginn"));

		jpaWorkingOn.add(panelBeginn, new GridBagConstraints(0, 3, 7, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.repaint();
		LPMain.getInstance().getDesktop().repaint();
		try {
			zeitVorschlagen();
		} catch (Throwable e1) {
			handleException(e1, true);
		}

	}

	void wrbEnde_actionPerformed(ActionEvent e) {
		jpaWorkingOn.remove(panelBeginn);

		wlaKmBeginnOderEnde.setText(LPMain.getTextRespectUISPr("pers.reisezeiten.kmende"));
		jpaWorkingOn.add(panelEnde, new GridBagConstraints(0, 3, 7, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		try {

			zeitVorschlagen();
		} catch (Throwable e1) {
			handleException(e1, true);
		}

		jpaWorkingOn.repaint();
		LPMain.getInstance().getDesktop().repaint();

	}

	void wnfEntfernung_focusLost(FocusEvent e) {
		try {

			Integer letzerBeginn = 0;
			if (wnfLetzterKmBeginn.getInteger() != null) {
				letzerBeginn = wnfLetzterKmBeginn.getInteger();
			}

			if (wnfEntfernung.getInteger() != null) {
				wnfKmBeginnOderEnde.setInteger(letzerBeginn + wnfEntfernung.getInteger());

			}
		} catch (ExceptionLP ex) {
			handleException(ex, true);
		}
	}

	void wnfKmBeginn_focusLost(FocusEvent e) {
		try {

			Integer letzerBeginn = 0;
			if (wnfLetzterKmBeginn.getInteger() != null) {
				letzerBeginn = wnfLetzterKmBeginn.getInteger();
			}

			if (wnfKmBeginnOderEnde.getInteger() != null) {
				wnfEntfernung.setInteger(wnfKmBeginnOderEnde.getInteger() - letzerBeginn);
			}
		} catch (ExceptionLP ex) {
			handleException(ex, true);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == wtfZeit.getWtfZeit().getWspStunden()
				|| e.getSource() == wtfZeit.getWtfZeit().getWspMinuten()) {

			try {

				// Letzten KM-Bginn holen

				java.util.Date d = wtfZeit.getTimestamp();

				if (d != null) {
					// zeitVorschlagen();
					labelsLetzteKommtErstesGehtAktualisieren();

					ReiseDto reiseDto = DelegateFactory.getInstance().getZeiterfassungDelegate().getReiseDtoVorgaenger(
							new Timestamp(d.getTime()), internalFrameZeiterfassung.getPersonalDto().getIId());
					if (reiseDto != null && Helper.short2Boolean(reiseDto.getBBeginn())) {
						wnfLetzterKmBeginn.setInteger(reiseDto.getIKmbeginn());
						wlaLetzterKmBeginn.setVisible(true);
						wnfLetzterKmBeginn.setVisible(true);

						wlaEntfernung.setVisible(true);
						wnfEntfernung.setVisible(true);
					} else {
						wlaLetzterKmBeginn.setVisible(false);
						wnfLetzterKmBeginn.setVisible(false);

						wlaEntfernung.setVisible(false);
						wnfEntfernung.setVisible(false);
					}
				}

			} catch (Throwable e1) {
				handleException(e1, true);
			}

		}
	}
}

class PanelReisezeiten_wrbBeginn_actionAdapter implements java.awt.event.ActionListener {
	PanelReisezeiten adaptee;

	PanelReisezeiten_wrbBeginn_actionAdapter(PanelReisezeiten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wrbBeginn_actionPerformed(e);
	}
}

class PanelReisezeiten_wrbEnde_actionAdapter implements java.awt.event.ActionListener {
	PanelReisezeiten adaptee;

	PanelReisezeiten_wrbEnde_actionAdapter(PanelReisezeiten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wrbEnde_actionPerformed(e);
	}
}

class PanelReisezeiten_wnfEntfernung_focusAdapter extends java.awt.event.FocusAdapter {
	PanelReisezeiten adaptee;

	PanelReisezeiten_wnfEntfernung_focusAdapter(PanelReisezeiten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfEntfernung_focusLost(e);
	}
}

class PanelReisezeiten_wnfKmBeginn_focusAdapter extends java.awt.event.FocusAdapter {
	PanelReisezeiten adaptee;

	PanelReisezeiten_wnfKmBeginn_focusAdapter(PanelReisezeiten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfKmBeginn_focusLost(e);
	}
}

class PanelReisezeiten_wtfFahrzeug_keyListener implements java.awt.event.KeyListener {
	PanelReisezeiten adaptee;

	PanelReisezeiten_wtfFahrzeug_keyListener(PanelReisezeiten adaptee) {
		this.adaptee = adaptee;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (adaptee.wtfFahrzeug.getText() == null) {
			adaptee.wbuPersonalfahrzeug.setEnabled(true);
		} else {
			adaptee.wbuPersonalfahrzeug.setEnabled(false);

			try {
				adaptee.reiseDto.setFahrzeugIId(null);
				adaptee.wtfPersonalfahrzeug.setText(null);
			} catch (Throwable e1) {
				adaptee.handleException(e1, true);
			}
		}
	}
}
