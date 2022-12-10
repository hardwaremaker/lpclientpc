package com.lp.client.cockpit;

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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.DialogQuery;
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
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSlider;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.util.IconFactory;
import com.lp.client.zeiterfassung.InternalFrameZeiterfassung;
import com.lp.client.zeiterfassung.ZeiterfassungPruefer;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PanelCockpitTelefonzeiten extends PanelBasis {

	private static final long serialVersionUID = -7074583086617801145L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private TelefonzeitenDto telefonzeitenDto = null;

	private final static String ACTION_SPECIAL_BELEG = "action_special_beleg";
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private WrapperGotoButton wbuBeleg = null;

	private final static String ACTION_SPECIAL_ANRUFEN = "action_special_anrufen";

	private InternalFrameCockpit internalFrameCockpit = null;

	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperLabel wlaBis = new WrapperLabel();

	private PanelQueryFLR panelQueryFLRProjekt = null;
	private PanelQueryFLR panelQueryFLRAngebot = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	private WrapperLabel wlaLfdUhrzeit = new WrapperLabel();

	private WrapperDateField wdfDate = new WrapperDateField();

	private WrapperTimeField wtfVon = new WrapperTimeField();
	private WrapperTimeField wtfBis = new WrapperTimeField();

	private WrapperButton wbuJetzt = new WrapperButton(
			new ImageIcon(getClass().getResource("/com/lp/client/res/clock16x16.png")));

	private WrapperLabel wlaKommentarIntern = new WrapperLabel();
	private WrapperLabel wlaKommentarExtern = new WrapperLabel();
	private WrapperLabel wlaNummerGewaehlt = new WrapperLabel();

	private WrapperEditorField wefKommentarIntern = new WrapperEditorFieldKommentar(getInternalFrame(), "");
	private WrapperEditorField wefKommentarExtern = new WrapperEditorFieldKommentar(getInternalFrame(), "");
	java.util.Timer timer = null;

	protected JButton wbuCall = ButtonFactory.createJButtonNotEnabled(IconFactory.getMobilephone());

	private WrapperTextField wtfNummerGewaehlt = new WrapperTextField();

	private ZeiterfassungPruefer zeiterfassungPruefer = null;

	private WrapperComboBox wcbVerrechenbar = createWcbVerrechenbar();

	private WrapperLabel wlaTitel = new WrapperLabel();
	private WrapperTextField wtfTitel = new WrapperTextField();

	private WrapperLabel wlaWiedervorlage = new WrapperLabel();
	private WrapperTimestampField wtfWiedervorlage = new WrapperTimestampField();

	private WrapperLabel wlaKontaktart = new WrapperLabel();
	private WrapperComboBox wcoKontaktart = new WrapperComboBox();

	private WrapperComboBox wcoBeleg = new WrapperComboBox();

	private WrapperSelectField wsfPerson = null;

	public final static String MY_OWN_NEW_TOGGLE_WIEDERVORLAGE_ERLEDIGT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_TOGGLE_WIEDERVORLAGE_ERLEDIGT";

	public PanelCockpitTelefonzeiten(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameCockpit = (InternalFrameCockpit) internalFrame;
		zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfVon;
	}

	protected void setDefaults() {
		long now = System.currentTimeMillis();
		wdfDate.setDatumHeute();
		wtfVon.setTime(new Time(now));
		wtfBis.setTime(new Time(now));
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		telefonzeitenDto = new TelefonzeitenDto();
		telefonzeitenDto.setPersonalIId(LPMain.getTheClient().getIDPersonal());
		leereAlleFelder(this);

		setDefaults();
	}

	void dialogQueryBelegFromListe(ActionEvent e) throws Throwable {

		if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_PROJEKT)) {
			panelQueryFLRProjekt = ProjektFilterFactory.getInstance().createPanelFLRProjekt(getInternalFrame(), null,
					true);

			new DialogQuery(panelQueryFLRProjekt);
		} else if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_ANGEBOT)) {
			panelQueryFLRAngebot = AngebotFilterFactory.getInstance().createPanelFLRAngebot(getInternalFrame(), false,
					true, AngebotFilterFactory.getInstance().createFKAngebotAngelegteUndOffene(), null);
			new DialogQuery(panelQueryFLRAngebot);
		} else if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_AUFTRAG)) {
			panelQueryFLRAuftrag = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), true,
					true, null);
			new DialogQuery(panelQueryFLRAuftrag);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wbuJetzt)) {
			startUhrzeitAktualisieren();
		}

		if (e.getSource().equals(wbuCall) && wtfNummerGewaehlt.getText() != null) {
			WrapperTelefonField.anrufen(wtfNummerGewaehlt.getText());
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BELEG)) {
			dialogQueryBelegFromListe(e);
		}
		if (e.getSource().equals(wcoBeleg)) {

			if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_ANGEBOT)) {
				wbuBeleg.setIcon(
						new ImageIcon(getClass().getResource("/com/lp/client/res/presentation_chart16x16.png")));
			}
			if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_AUFTRAG)) {
				wbuBeleg.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/auftrag16x16.png")));
			}
			if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_PROJEKT)) {
				wbuBeleg.setIcon(
						new ImageIcon(getClass().getResource("/com/lp/client/res/briefcase2_document16x16.png")));
			}

		}

		if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_WIEDERVORLAGE_ERLEDIGT)) {
			// PJ 17558
			if (telefonzeitenDto != null && telefonzeitenDto.getIId() != null) {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.toggleTelefonzeitenWiedervorlageErledigt(telefonzeitenDto.getIId());

				internalFrameCockpit.getTpCockpit().getPanelSplitTelefonzeiten().eventYouAreSelected(false);
			}
		}

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (pruefeObBuchungMoeglich()) {

			DelegateFactory.getInstance().getZeiterfassungDelegate().removeTelefonzeiten(telefonzeitenDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		if (timer != null) {
			timer.cancel();
			wlaLfdUhrzeit.setText("");
		}
		super.eventActionUnlock(e);
	}

	protected void components2Dto() throws Throwable {

		Calendar date = Calendar.getInstance();
		Calendar temp = Calendar.getInstance();
		date.setTime(wdfDate.getDate());

		temp.setTimeInMillis(wtfVon.getTime().getTime());
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
		telefonzeitenDto.setTVon(new Timestamp(temp.getTimeInMillis() - temp.getTimeInMillis() % 1000));

		temp.setTimeInMillis(wtfBis.getTime().getTime());
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
		telefonzeitenDto.setTBis(new Timestamp(temp.getTimeInMillis() - temp.getTimeInMillis() % 1000));

		telefonzeitenDto.setPartnerIId(internalFrameCockpit.getTpCockpit().getServicePartnerDto().getIId());
		telefonzeitenDto.setXKommentarext(wefKommentarExtern.getText());
		telefonzeitenDto.setXKommentarint(wefKommentarIntern.getText());

		telefonzeitenDto.setFVerrechenbar((Double) wcbVerrechenbar.getKeyOfSelectedItem());

		telefonzeitenDto.setPartnerIId(internalFrameCockpit.getTpCockpit().getServicePartnerDto().getIId());
		telefonzeitenDto.setAnsprechpartnerIId(
				(Integer) internalFrameCockpit.getTpCockpit().getPanelQueryAnsprechpartner().getSelectedId());
		telefonzeitenDto.setTWiedervorlage(wtfWiedervorlage.getTimestamp());
		telefonzeitenDto.setPersonalIIdZugewiesener(wsfPerson.getIKey());
		telefonzeitenDto.setCTitel(wtfTitel.getText());
		telefonzeitenDto.setKontaktartIId((Integer) wcoKontaktart.getKeyOfSelectedItem());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wtfVon.setTime(new Time(telefonzeitenDto.getTVon().getTime()));
		wtfBis.setTime(new Time(telefonzeitenDto.getTBis().getTime()));
		wdfDate.setDate(new Date(telefonzeitenDto.getTVon().getTime()));

		wtfNummerGewaehlt.setText(telefonzeitenDto.getCTelefonnrGewaehlt());

		if (telefonzeitenDto.getCTelefonnrGewaehlt() != null) {
			wbuCall.setEnabled(true);
		}

		if (telefonzeitenDto.getCTelefonnrGewaehlt() != null) {

		}

		wcbVerrechenbar.setKeyOfSelectedItem(telefonzeitenDto.getFVerrechenbar());

		wefKommentarExtern.setText(telefonzeitenDto.getXKommentarext());
		wefKommentarIntern.setText(telefonzeitenDto.getXKommentarint());

		if (telefonzeitenDto.getProjektIId() != null) {
			wcoBeleg.setKeyOfSelectedItem(LocaleFac.BELEGART_PROJEKT);
			ProjektDto pDto = DelegateFactory.getInstance().getProjektDelegate()
					.projektFindByPrimaryKey(telefonzeitenDto.getProjektIId());
			wtfBeleg.setText(pDto.getCNr() + " " + pDto.getCTitel());
			wbuBeleg.setOKey(telefonzeitenDto.getProjektIId());
			wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_PROJEKT_AUSWAHL);
		} else if (telefonzeitenDto.getAngebotIId() != null) {
			wcoBeleg.setKeyOfSelectedItem(LocaleFac.BELEGART_ANGEBOT);
			AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate()
					.angebotFindByPrimaryKey(telefonzeitenDto.getAngebotIId());

			wtfBeleg.setText(angebotDto.getCNr() + " " + angebotDto.getCBez());
			wbuBeleg.setOKey(telefonzeitenDto.getAngebotIId());
			wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_ANGEBOT_AUSWAHL);
		} else if (telefonzeitenDto.getAuftragIId() != null) {
			wcoBeleg.setKeyOfSelectedItem(LocaleFac.BELEGART_AUFTRAG);
			AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(telefonzeitenDto.getAuftragIId());

			wtfBeleg.setText(auftragDto.getCNr() + " " + auftragDto.getCBezProjektbezeichnung());
			wbuBeleg.setOKey(telefonzeitenDto.getAuftragIId());
			wbuBeleg.setWhereToGo(com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
		} else {
			wtfBeleg.setText(null);
		}

		if (wtfBis.getTime() != null) {
			berechneDauer();
		} else {
			wlaLfdUhrzeit.setText("");
		}

		wcoKontaktart.setKeyOfSelectedItem(telefonzeitenDto.getKontaktartIId());
		wtfWiedervorlage.setTimestamp(telefonzeitenDto.getTWiedervorlage());
		wsfPerson.setKey(telefonzeitenDto.getPersonalIIdZugewiesener());
		wtfTitel.setText(telefonzeitenDto.getCTitel());

	}

	private boolean pruefeObBuchungMoeglich() throws ExceptionLP, Throwable {

		Calendar date = Calendar.getInstance();
		Calendar temp = Calendar.getInstance();
		date.setTime(wdfDate.getDate());

		temp.setTimeInMillis(wtfVon.getTime().getTime());
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));

		Timestamp tVon = new Timestamp(temp.getTimeInMillis() - temp.getTimeInMillis() % 1000);

		return zeiterfassungPruefer.pruefeObBuchungMoeglich(tVon, telefonzeitenDto.getPersonalIId());
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (pruefeObBuchungMoeglich()) {
			super.eventActionUpdate(aE, bNeedNoUpdateI);
		} else {
			return;
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			if (pruefeObBuchungMoeglich()) {
				components2Dto();
				stopUhrzeitAktualisieren();
				boolean gespeichert = false;
				while (!gespeichert) {
					try {
						if (telefonzeitenDto.getIId() == null) {
							telefonzeitenDto.setIId(DelegateFactory.getInstance().getZeiterfassungDelegate()
									.createTelefonzeiten(telefonzeitenDto));

							setKeyWhenDetailPanel(telefonzeitenDto.getIId());
						} else {
							DelegateFactory.getInstance().getZeiterfassungDelegate()
									.updateTelefonzeiten(telefonzeitenDto);
						}
						gespeichert = true;
					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_ZEITBUCHUNGEN_VORHANDEN) {

							Object[] options = { LPMain.getTextRespectUISPr("pers.zeitdaten.vorher"),
									LPMain.getTextRespectUISPr("pers.zeitdaten.nachher"),
									LPMain.getTextRespectUISPr("lp.abbrechen") };

							int iOption = DialogFactory.showModalDialog(getInternalFrame(),
									LPMain.getTextRespectUISPr("pers.error.zeitdatenvorhandenum") + " "
											+ Helper.formatTimestamp((Timestamp) ex.getAlInfoForTheClient().get(0),
													LPMain.getTheClient().getLocUi()),
									"", options, options[1]);
							if (iOption == JOptionPane.YES_OPTION) {
								telefonzeitenDto
										.setTVon(new java.sql.Timestamp(telefonzeitenDto.getTVon().getTime() - 10));
								telefonzeitenDto
										.setTBis(new java.sql.Timestamp(telefonzeitenDto.getTBis().getTime() - 10));
							} else if (iOption == JOptionPane.NO_OPTION) {
								telefonzeitenDto
										.setTVon(new java.sql.Timestamp(telefonzeitenDto.getTVon().getTime() + 10));
								telefonzeitenDto
										.setTBis(new java.sql.Timestamp(telefonzeitenDto.getTBis().getTime() + 10));
							} else if (iOption == JOptionPane.CANCEL_OPTION) {
								return;
							}
						} else {
							handleException(ex, false);
							return;
						}
					}
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(telefonzeitenDto.getIId() + "");
				}
				eventYouAreSelected(false);
			}
		}

	}

	private void pruefePartner(PartnerDto partnerDto) throws Throwable {

		String gesperrterMandant = DelegateFactory.getInstance().getProjektDelegate()
				.istPartnerBeiEinemMandantenGesperrt(partnerDto.getIId());

		if (gesperrterMandant != null && !gesperrterMandant.equals(LPMain.getTheClient().getMandant())) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getTextRespectUISPr("projekt.partnerauswahl.kundegesperrt") + " " + gesperrterMandant);
		}

		KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByiIdPartnercNrMandantOhneExc(partnerDto.getIId(), LPMain.getTheClient().getMandant());

		// PJ18400
		if (kdDto != null) {

			DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(kdDto.getIId(), null, getInternalFrame());

			DelegateFactory.getInstance().getKundeDelegate().pruefeKreditlimit(kdDto.getIId(), getInternalFrame());
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		Object key = getKeyWhenDetailPanel();
		if (key != null && key instanceof Integer) {
			Integer telefonzeitenIId = (Integer) getKeyWhenDetailPanel();

			if (telefonzeitenIId != null) {

				TelefonzeitenDto telefonzeitenDtoLocal = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.telefonzeitenFindByPrimaryKey(telefonzeitenIId);
				if (telefonzeitenDtoLocal.getPersonalIId() != null
						&& !telefonzeitenDtoLocal.getPersonalIId().equals(LPMain.getTheClient().getIDPersonal())) {
					lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				}
			}
		}

		return lockStateValue;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			String sPartner = LPMain.getMessageTextRespectUISPr("cp.cockpit.telefon.falscherbeleg",
					internalFrameCockpit.getTpCockpit().getServicePartnerDto().formatFixName1Name2());

			if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) panelQueryFLRProjekt.getSelectedId();
				if (key != null) {

					ProjektDto pDto = DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(key);

					if (pDto.getPartnerIId()
							.equals(internalFrameCockpit.getTpCockpit().getServicePartnerDto().getIId())) {

						telefonzeitenDto.setProjektIId(key);
						telefonzeitenDto.setAngebotIId(null);
						telefonzeitenDto.setAuftragIId(null);
						wtfBeleg.setText(pDto.getCNr() + " " + pDto.getCTitel());
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"), sPartner);
					}

				}
			} else if (e.getSource() == panelQueryFLRAngebot) {
				Integer key = (Integer) panelQueryFLRAngebot.getSelectedId();
				AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate().angebotFindByPrimaryKey(key);

				KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse());
				if (kdDto.getPartnerIId().equals(internalFrameCockpit.getTpCockpit().getServicePartnerDto().getIId())) {

					telefonzeitenDto.setAngebotIId(angebotDto.getIId());
					telefonzeitenDto.setProjektIId(null);
					telefonzeitenDto.setAuftragIId(null);

					wbuBeleg.setOKey(angebotDto.getIId());

					wtfBeleg.setText(angebotDto.getCNr() + " " + angebotDto.getCBez());
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"), sPartner);
				}

			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer key = (Integer) panelQueryFLRAuftrag.getSelectedId();
				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(key);

				KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());
				if (kdDto.getPartnerIId().equals(internalFrameCockpit.getTpCockpit().getServicePartnerDto().getIId())) {

					telefonzeitenDto.setAuftragIId(auftragDto.getIId());
					telefonzeitenDto.setProjektIId(null);
					telefonzeitenDto.setAngebotIId(null);
					wbuBeleg.setOKey(auftragDto.getIId());

					wtfBeleg.setText(auftragDto.getCNr() + " " + auftragDto.getCBezProjektbezeichnung());
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"), sPartner);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRProjekt || e.getSource() == panelQueryFLRAngebot
					|| e.getSource() == panelQueryFLRAuftrag) {
				telefonzeitenDto.setProjektIId(null);
				telefonzeitenDto.setAngebotIId(null);
				telefonzeitenDto.setAuftragIId(null);
				wtfBeleg.setText(null);
				wbuBeleg.setOKey(null);
			}
		}
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wdfDate.setMandatoryField(true);
		wtfVon.setMandatoryField(true);
		wtfBis.setMandatoryField(true);
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wtfVon.setShowSeconds(true);

		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wtfBis.setShowSeconds(true);
		FocusListener focusListener = new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				stopUhrzeitAktualisieren();
			}
		};

		wbuBeleg = new WrapperGotoButton("", com.lp.util.GotoHelper.GOTO_PROJEKT_AUSWAHL);
		wbuBeleg.setActionCommand(ACTION_SPECIAL_BELEG);
		wbuBeleg.addActionListener(this);
		wbuBeleg.setMnemonic(KeyEvent.VK_R);
		wtfBeleg.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfBeleg.setActivatable(false);

		wcoBeleg.setMandatoryField(true);
		wcoBeleg.addActionListener(this);

		LinkedHashMap belege = new LinkedHashMap();
		belege.put(LocaleFac.BELEGART_ANGEBOT, LPMain.getTextRespectUISPr("angb.angebot"));
		belege.put(LocaleFac.BELEGART_AUFTRAG, LPMain.getTextRespectUISPr("auft.auftrag"));
		belege.put(LocaleFac.BELEGART_PROJEKT, LPMain.getTextRespectUISPr("label.projekt"));

		wcoBeleg.setMap(belege);

		wlaTitel.setText(LPMain.getTextRespectUISPr("lp.titel"));

		wlaWiedervorlage.setText(LPMain.getTextRespectUISPr("lp.wiedervorlage"));

		wlaKontaktart.setText(LPMain.getTextRespectUISPr("proj.projekt.label.kontaktart"));
		wcoKontaktart.setMap(DelegateFactory.getInstance().getPartnerServicesDelegate().getAllKontaktart());

		wtfBis.installFocusListener(focusListener);

		wbuJetzt.setToolTipText("Aktuelle Zeit einstellen");
		wbuJetzt.addActionListener(this);

		wsfPerson = new WrapperSelectField(WrapperSelectField.PERSONAL, getInternalFrame(), true);
		wsfPerson.getWrapperButton().setText(LPMain.getTextRespectUISPr("pers.telefonzeiten.zugewiesener"));

		wlaKommentarExtern.setText(LPMain.getTextRespectUISPr("lp.externerkommentar"));
		wlaKommentarIntern.setText(LPMain.getTextRespectUISPr("lp.internerkommentar"));

		wefKommentarExtern.setEditButtonMnemonic(KeyEvent.VK_E);
		wefKommentarExtern.setDefaultButtonMnemonic(KeyEvent.VK_Z);

		wefKommentarIntern.setEditButtonMnemonic(KeyEvent.VK_I);
		wefKommentarIntern.setDefaultButtonMnemonic(KeyEvent.VK_Y);

		wlaNummerGewaehlt.setText(LPMain.getTextRespectUISPr("pers.telefonzeiten.nummergewaehlt"));

		wtfNummerGewaehlt.setActivatable(false);
		wtfNummerGewaehlt.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfVon.setMandatoryField(true);
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		int iZeile = 0;

		jpaWorkingOn.add(wdfDate, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 10, 0));
		jpaWorkingOn.add(wtfVon, new GridBagConstraints(3, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(wlaBis, new GridBagConstraints(4, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 10, 0));
		jpaWorkingOn.add(wtfBis, new GridBagConstraints(6, iZeile, 1, 1, 0.10, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuJetzt, new GridBagConstraints(7, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));
		jpaWorkingOn.add(wlaLfdUhrzeit, new GridBagConstraints(7, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 20, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wcoBeleg, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 50), 0, 0));
		jpaWorkingOn.add(wbuBeleg, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 15, 0));
		jpaWorkingOn.add(wtfBeleg, new GridBagConstraints(1, iZeile, 5, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaWiedervorlage, new GridBagConstraints(6, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wtfWiedervorlage, new GridBagConstraints(7, iZeile, 1, 1, 0.2, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 120, 0));

		iZeile++;

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG)) {
			jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("pers.zeiterfassung.verrechenbar")),
					new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			jpaWorkingOn.add(wcbVerrechenbar, new GridBagConstraints(7, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wlaTitel, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 110, 0));
		jpaWorkingOn.add(wtfTitel, new GridBagConstraints(1, iZeile, 5, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfPerson.getWrapperButton(), new GridBagConstraints(6, iZeile, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfPerson.getWrapperTextField(), new GridBagConstraints(7, iZeile, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TAPISERVICE)) {
			iZeile++;
			jpaWorkingOn.add(wlaNummerGewaehlt, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfNummerGewaehlt, new GridBagConstraints(1, iZeile, 2, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			Dimension d = new Dimension(Defaults.getInstance().getControlHeight(),
					Defaults.getInstance().getControlHeight());
			wbuCall.setMinimumSize(d);
			wbuCall.setPreferredSize(d);
			wbuCall.addActionListener(this);

			jpaWorkingOn.add(wbuCall, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		} else {
			iZeile++;

		}

		jpaWorkingOn.add(wlaKontaktart, new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoKontaktart, new GridBagConstraints(7, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKommentarExtern, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentarExtern, new GridBagConstraints(1, iZeile, 8, 1, 0, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentarIntern, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentarIntern, new GridBagConstraints(1, iZeile, 8, 1, 0, 0.05, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, ACTION_MEDIA};

		enableToolsPanelButtons(aWhichButtonIUse);

		getToolBar().addButtonCenter("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("pers.telefonzeiten.toggle.wiedervorlage.erledigt"),
				MY_OWN_NEW_TOGGLE_WIEDERVORLAGE_ERLEDIGT, null, RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_TELEFONZEITEN;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		wbuCall.setEnabled(false);

		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			if (key == null) {
				leereAlleFelder(this);
			}

			clearStatusbar();
			if (key != null && key.equals(LPMain.getLockMeForNew())) {
				startUhrzeitAktualisieren();
				wcbVerrechenbar.setKeyOfSelectedItem(100D);
			}
		} else {
			telefonzeitenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.telefonzeitenFindByPrimaryKey((Integer) key);
			dto2Components();
		}

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				internalFrameCockpit.getTpCockpit().getServicePartnerDto().formatFixTitelName1Name2());

	}

	private void stopUhrzeitAktualisieren() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}

	private void startUhrzeitAktualisieren() {
		stopUhrzeitAktualisieren();
		wtfBis.setTime(new Time(System.currentTimeMillis()));
		timer = new java.util.Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					wtfBis.setTime(new Time(System.currentTimeMillis()));
					berechneDauer();
				} catch (ExceptionLP e) {
				}
			}
		}, 0, 500);
	}

	public void setzeAusWrapperTelefonField(Integer partnerIId, Integer ansprechpartnerIId, Integer projektIId,
			String nummerGewaehlt) throws Throwable {

		telefonzeitenDto.setPartnerIId(partnerIId);
		telefonzeitenDto.setAnsprechpartnerIId(ansprechpartnerIId);

		telefonzeitenDto.setProjektIId(projektIId);

		telefonzeitenDto.setCTelefonnrGewaehlt(nummerGewaehlt);

		wtfNummerGewaehlt.setText(nummerGewaehlt);

		if (projektIId != null) {
			ProjektDto pDto = DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId);

			wtfBeleg.setText(pDto.getCNr() + " " + pDto.getCTitel());
		}

		// Heutiges Datum und Zeit in richtigem Locale, und Bearbeiter
		// Kurzzeichen einfuegen
		Locale editorLocale = LPMain.getInstance().getUISprLocale();
		GregorianCalendar cal = new GregorianCalendar(editorLocale);
		// Datum
		SimpleDateFormat sdDateFormat = new SimpleDateFormat("dd.MM.yyyy", editorLocale);
		String content = sdDateFormat.format(cal.getTime());
		// Uhrzeit
		SimpleDateFormat sdTimeFormat = new SimpleDateFormat("HH:mm", editorLocale);
		content += " " + sdTimeFormat.format(cal.getTime());
		// Personal Kurzzeichen des Bearbeiters
		try {
			TheClientDto clientDto = LPMain.getTheClient();
			Integer iPersonalID = clientDto.getIDPersonal();
			PersonalDelegate persDelegate = DelegateFactory.getInstance().getPersonalDelegate();
			PersonalDto persDto = persDelegate.personalFindByPrimaryKey(iPersonalID);
			String sPersKurzzeichen = persDto.getCKurzzeichen();
			content += " " + sPersKurzzeichen;
		} catch (Throwable ex) {
		}

		wefKommentarExtern.setText(content);

	}

	private void berechneDauer() throws ExceptionLP {

		if (wtfBis.getTime() != null && wtfVon.getTime() != null) {
			long time = (wtfBis.getTime().getTime() - wtfVon.getTime().getTime());
			wlaLfdUhrzeit.setText(LPMain.getTextRespectUISPr("lp.dauer")
					+ String.format(": %02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
							TimeUnit.MILLISECONDS.toMinutes(time) % 60, TimeUnit.MILLISECONDS.toSeconds(time) % 60));
		}
	}
}
