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
package com.lp.client.finanz;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayout.Align;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.AutoAuszugsnummerField;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGeschaeftsjahrDateField;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.HelperTimestamp;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KassenbuchungsteuerartDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

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
 * @author unbekannt
 * @version $Revision: 1.20 $
 */
public class PanelFinanzBuchungDetailsKassenbuch extends PanelBasis {

	private static final long serialVersionUID = 1061584162861363425L;

	private TabbedPaneKassenbuch tabbedPaneKassenbuch = null;
	private BuchungDto buchungDto = null;
	private BuchungdetailDto buchungdetailDto = null;
	private BuchungdetailDto gegenbuchungDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private KontoDto kontoDto = null;
	private BigDecimal bdSteuerbetrag = null;

	// Default wird auf sachkonten umgebucht
	private String kontotypKonto = FinanzServiceFac.KONTOTYP_SACHKONTO;

	private PanelQueryFLR panelQueryFLRKonto = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private static final String ACTION_SPECIAL_KONTO = "action_special_ub_konto";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_ub_kostenstelle";
	private static final String ACTION_SPECIAL_RB_KONTO = "action_special_ub_rb_konto";
	private static final String ACTION_SPECIAL_RB_BELEG = "action_special_ub_rb_beleg";
	private static final String ACTION_SPECIAL_CO_UST = "action_special_ub_co_ust";

	private JLabel wlaAusgang = new JLabel();
	private WrapperNumberField wnfAusgang = new WrapperNumberField();
	private WrapperGeschaeftsjahrDateField wdfDatum;
	private JLabel wlaEingang = new JLabel();
	private WrapperNumberField wnfEingang = new WrapperNumberField();
	private WrapperRadioButton wrbKontoSachkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbKontoDebitorenkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbKontoKreditorenkonto = new WrapperRadioButton();
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
	private JLabel wlaDatum = new JLabel();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private JLabel wlaText = new JLabel();
	private WrapperTextField wtfText = new WrapperTextField();
	private JLabel wlaBeleg = new JLabel();
	private WrapperRadioButton wrbBelegAuto = new WrapperRadioButton();
	private WrapperRadioButton wrbBelegHand = new WrapperRadioButton();
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private JLabel wlaWaehrungEingang = new JLabel();
	private JLabel wlaWaehrungAusgang = new JLabel();
	private ButtonGroup buttongroupKonto = new ButtonGroup();
	private ButtonGroup buttongroupBeleg = new ButtonGroup();
	private WrapperComboBox wcoUst = new WrapperComboBox();
	private WrapperRadioButton wrbUst = new WrapperRadioButton();
	private WrapperRadioButton wrbVst = new WrapperRadioButton();
	private ButtonGroup buttongroupSteuerart = new ButtonGroup();
	private java.util.Date previousBookingDate = null;
	private BigDecimal previousEingangDecimal = null;
	private BigDecimal previousAusgangDecimal = null;
	private JLabel wlaAuszug = new JLabel();
	private WrapperTextNumberField wnfAuszug = new WrapperTextNumberField();
	HvLayout hvLayout = null;

	public PanelFinanzBuchungDetailsKassenbuch(InternalFrame internalFrame, TabbedPaneKassenbuch tabbedPaneKassenbuch)
			throws Throwable {
		super(internalFrame, LPMain.getTextRespectUISPr("finanz.kassenbuch"));
		this.tabbedPaneKassenbuch = tabbedPaneKassenbuch;
		jbInit();
		setDefaults();
		initComponents();
	}

	protected InternalFrameFinanz getInternalFrameFinanz() {
		return (InternalFrameFinanz) getInternalFrame();
	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, ACTION_DELETE };
		enableToolsPanelButtons(aWhichButtonIUse);

		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		wlaAusgang.setText(LPMain.getTextRespectUISPr("finanz.ausgang"));
		wlaEingang.setText(LPMain.getTextRespectUISPr("finanz.eingang"));
		wrbKontoSachkonto.setText(LPMain.getTextRespectUISPr("lp.sachkonto"));
		wrbKontoSachkonto.addActionListener(this);
		wrbKontoDebitorenkonto.setText(LPMain.getTextRespectUISPr("lp.debitorenkonto"));
		wrbKontoDebitorenkonto.addActionListener(this);
		wrbKontoKreditorenkonto.setText(LPMain.getTextRespectUISPr("lp.kreditorenkonto"));
		wrbKontoKreditorenkonto.addActionListener(this);
		wbuKonto.setText(LPMain.getTextRespectUISPr("button.konto"));
		wbuKonto.setText(LPMain.getTextRespectUISPr("button.konto.tooltip"));
		wtfKontoNummer.setActivatable(false);
		wtfKontoNummer.setMandatoryField(true);
		wtfKontoBezeichnung.setActivatable(false);
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wbuKostenstelle.setText(LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wtfKostenstelleNummer.setActivatable(false);
		wtfKostenstelleNummer.setMandatoryField(true);
		wlaText.setText(LPMain.getTextRespectUISPr("lp.text"));
		wtfText.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_TEXT);
		wtfText.setMandatoryField(true);
		wnfEingang.setMandatoryField(false);
		wnfEingang.setDependenceField(true);
//		wnfEingang.setMinimumSize(new Dimension(100, 23));
//		wnfEingang.setPreferredSize(new Dimension(100, 23));
		wlaBeleg.setText(LPMain.getTextRespectUISPr("lp.beleg"));
		wrbBelegAuto.setText(LPMain.getTextRespectUISPr("lp.automatisch"));
		wrbBelegHand.setText(LPMain.getTextRespectUISPr("label.handeingabe"));
		wtfBeleg.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_BELEG);
		wtfBeleg.setActivatable(false);
		wtfBeleg.setMandatoryField(true);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wdfDatum = new WrapperGeschaeftsjahrDateField(getInternalFrameFinanz().getIAktuellesGeschaeftsjahr());
		wdfDatum.setMandatoryField(true);
		wdfDatum.setMandatoryFieldDB(false);
		wnfAusgang.setMandatoryField(false);
		wnfAusgang.setDependenceField(true);
		wrbUst.setText(LPMain.getTextRespectUISPr("fb.label.ust"));
		wrbVst.setText(LPMain.getTextRespectUISPr("fb.label.vst"));
		buttongroupSteuerart.add(wrbUst);
		buttongroupSteuerart.add(wrbVst);
		buttongroupBeleg.add(wrbBelegAuto);
		buttongroupBeleg.add(wrbBelegHand);
		buttongroupKonto.add(wrbKontoDebitorenkonto);
		buttongroupKonto.add(wrbKontoKreditorenkonto);
		buttongroupKonto.add(wrbKontoSachkonto);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuKonto.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKostenstelle.addActionListener(this);
		wrbBelegAuto.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegHand.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegAuto.addActionListener(this);
		wrbBelegHand.addActionListener(this);
		wrbKontoDebitorenkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoDebitorenkonto.addActionListener(this);
		wrbKontoKreditorenkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoKreditorenkonto.addActionListener(this);
		wrbKontoSachkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoSachkonto.addActionListener(this);
		wcoUst.setMandatoryFieldDB(true);
		wcoUst.setActionCommand(ACTION_SPECIAL_CO_UST);
		wcoUst.addActionListener(this);
		wlaAuszug.setText(LPMain.getTextRespectUISPr("label.auszug"));
		if (automatischeAuszugsnummer()) {
			wnfAuszug = new AutoAuszugsnummerField(wdfDatum);
		}
		wnfAuszug.setMaximumDigits(FinanzFac.MAX_UMBUCHUNG_AUSZUG);
		wrbBelegAuto.setSelected(true);
		wrbKontoSachkonto.setSelected(true);

		hvLayout = HvLayoutFactory.createWithoutInset(this);
		hvLayout.add(getToolsPanel()).spanAndWrap(3);

		String gapLeft = "gapleft 6px";
		Integer buttonWidth = 90;
		Integer numberWidth = 142;
		Integer textWidth = 430;

		hvLayout.addAligned(Align.RIGHT, wlaDatum).add(wdfDatum).add(wrbKontoSachkonto, gapLeft).split(3)
				.add(wrbKontoDebitorenkonto).add(wrbKontoKreditorenkonto).wrap();

//		hvLayout
//			.add(wrbKontoSachkonto, gapLeft)
//			.add(wrbKontoDebitorenkonto)
//			.add(wrbKontoKreditorenkonto).wrap();

		hvLayout.add(wbuKonto, gapLeft, buttonWidth).add(wtfKontoNummer, numberWidth)
				.add(wtfKontoBezeichnung, textWidth).wrap();

		hvLayout.add(wbuKostenstelle, gapLeft, buttonWidth).add(wtfKostenstelleNummer, numberWidth)
				.add(wtfKostenstelleBezeichnung, textWidth).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaEingang).add(wnfEingang).add(wlaWaehrungEingang).split(4).add(wcoUst, 200)
				.add(wrbUst).add(wrbVst).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaAusgang).add(wnfAusgang).add(wlaWaehrungAusgang).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaText).add(wtfText, 576).spanAndWrap(2);

		hvLayout.addAligned(Align.RIGHT, wlaBeleg).add(wrbBelegHand).add(wtfBeleg, textWidth).wrap();

		hvLayout.add(wrbBelegAuto).skipAndWrap(1);

		hvLayout.addAligned(Align.RIGHT, wlaAuszug).add(wnfAuszug, numberWidth);
	}

	private java.util.Date getPreviousBookingDate() {
		if (null == previousBookingDate) {
			previousBookingDate = new java.util.Date(System.currentTimeMillis());
		}

		return previousBookingDate;
	}

	private void setPreviousBookingDate(java.util.Date date) {
		previousBookingDate = date;
	}

	private void setPreviousValuesFromComponent() {
		try {
			previousEingangDecimal = wnfEingang.getBigDecimal();
			previousAusgangDecimal = wnfAusgang.getBigDecimal();
		} catch (ExceptionLP e) {
		}
	}

	private void resetPreviousValues() {
		previousEingangDecimal = null;
		previousAusgangDecimal = null;
	}

	/**
	 * Defaults setzen.
	 *
	 * @throws Exception
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		wcoUst.setMap(DelegateFactory.getInstance().getMandantDelegate()
				.getAllMwstsatzbez(LPMain.getTheClient().getMandant()));
		wdfDatum.setDate(getPreviousBookingDate());
		wlaWaehrungEingang.setText(LPMain.getTheClient().getSMandantenwaehrung());
		wlaWaehrungAusgang.setText(LPMain.getTheClient().getSMandantenwaehrung());
	}

	private void setNewGeschaeftsjahr(int newValue) {
		int previousValue = wdfDatum.getGeschaeftsjahr();
		wdfDatum.setGeschaeftsjahr(newValue);
		if (newValue != previousValue) {
			setPreviousBookingDate(null);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		leereAlleFelder(this);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			setNewGeschaeftsjahr(getInternalFrameFinanz().getIAktuellesGeschaeftsjahr());
			wdfDatum.setDate(getPreviousBookingDate());
			kostenstelleDto = getInternalFrameFinanz().getDefaultKostenstelle();
			dto2ComponentsKostenstelle();

		} else {

			buchungdetailDto = DelegateFactory.getInstance().getBuchenDelegate()
					.buchungdetailFindByPrimaryKey((Integer) key);

			buchungDto = DelegateFactory.getInstance().getBuchenDelegate()
					.buchungFindByPrimaryKey(buchungdetailDto.getBuchungIId());

			getGegenbuchung(buchungdetailDto);
			dto2Components();
		}
	}

	private void getGegenbuchung(BuchungdetailDto detail) throws ExceptionLP, Throwable {
		BuchungdetailDto[] details = DelegateFactory.getInstance().getBuchenDelegate()
				.buchungdetailsFindByBuchungIId(buchungDto.getIId());

		for (int i = 0; i < details.length; i++) {
			// if ((details[i].getKontoIId() != buchungdetailDto.getKontoIId())
			// never ever compare Integer with ==
			if (!detail.getKontoIId().equals(details[i].getKontoIId())
					&& !details[i].getNBetrag().equals(detail.getNUst())) {
				gegenbuchungDto = details[i];
				break;
			}
		}
	}

	protected void dto2Components() throws Throwable {
		wtfText.setText(buchungDto.getCText());
		wtfBeleg.setText(buchungDto.getCBelegnummer().trim());
		wdfDatum.setDate(buchungDto.getDBuchungsdatum());

		bdSteuerbetrag = buchungdetailDto.getNUst();
		// if
		// (buchungdetailDto.getBuchungdetailartCNr().equals(BuchenFac.HabenBuchung))
		// {

		kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(gegenbuchungDto.getKontoIId());

		if (buchungdetailDto.getBuchungdetailartCNr().equals(BuchenFac.SollBuchung)) {
			wnfEingang.setBigDecimal(buchungdetailDto.getNBetrag());
			wnfAusgang.setBigDecimal(null);
		} else {
			wnfEingang.setBigDecimal(null);
			wnfAusgang.setBigDecimal(buchungdetailDto.getNBetrag());
		}

		if (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			wrbKontoSachkonto.setSelected(true);
			showSteuerbuttons(true);
			if (isBank(kontoDto)) {
				showAuszug(true);
				wnfAuszug.setInteger(buchungdetailDto.getIAuszug());
			} else {
				showAuszug(false);
			}
		}
		if (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
			wrbKontoKreditorenkonto.setSelected(true);
			showSteuerbuttons(false);
			showAuszug(false);
		}
		if (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			wrbKontoDebitorenkonto.setSelected(true);
			showSteuerbuttons(false);
			showAuszug(false);
		}

		KassenbuchungsteuerartDto kbstDto = DelegateFactory.getInstance().getBuchenDelegate()
				.getKassenbuchungSteuerart(buchungdetailDto.getBuchungIId());
		if (kbstDto != null) {
			wcoUst.setSelectedItem(kbstDto.getMwstsatzbezBezeichnung());
			if (kbstDto.isUstBuchung())
				wrbUst.setSelected(true);
			else
				wrbVst.setSelected(true);
		}

		wrbBelegHand.setSelected(true);

		kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
				.kostenstelleFindByPrimaryKey(buchungDto.getKostenstelleIId());
		dto2ComponentsKonto();
		dto2ComponentsKostenstelle();
	}

	private boolean isBank(KontoDto kontoDto) throws ExceptionLP {
		if (kontoDto != null) {
			BankverbindungDto bankDto = DelegateFactory.getInstance().getFinanzDelegate()
					.bankverbindungFindByKontoIIdOhneExc(kontoDto.getIId());
			if (bankDto != null) {
				return true;
			}
		}
		return false;
	}

	private void showAuszug(boolean b) {
		wlaAuszug.setVisible(b);
		wnfAuszug.setVisible(b);
		wnfAuszug.setMandatoryField(b);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		wrbKontoSachkonto.setSelected(true);
		wrbBelegAuto.setSelected(true);
		wtfBeleg.setMandatoryField(false);
		showSteuerbuttons(true);
		// wrbKontoDebitorenkonto.setSelected(false) ;
		// wrbKontoKreditorenkonto.setSelected(false) ;
		refreshKontotyp();

		resetPreviousValues();
		buchungDto = new BuchungDto();
		buchungdetailDto = new BuchungdetailDto();
	}

	private boolean isSaldoAllowed(KassenbuchDto kassenbuchDto, BigDecimal newEingang, BigDecimal newAusgang,
			BigDecimal oldEingang, BigDecimal oldAusgang) throws Throwable {
		if (kassenbuchDto.getBNegativErlaubt() != 0)
			return true;

		if (null == newEingang)
			newEingang = BigDecimal.ZERO;
		if (null == newAusgang)
			newAusgang = BigDecimal.ZERO;
		if (null == oldEingang)
			oldEingang = BigDecimal.ZERO;
		if (null == oldAusgang)
			oldAusgang = BigDecimal.ZERO;

		BigDecimal kontoSaldo = DelegateFactory.getInstance().getBuchenDelegate().getSaldoVonKonto(
				kassenbuchDto.getKontoIId(), getInternalFrameFinanz().getIAktuellesGeschaeftsjahr(), 12, true);
		if (null == kontoSaldo)
			kontoSaldo = BigDecimal.ZERO;

		BigDecimal ebKonto = DelegateFactory.getInstance().getBuchenDelegate().getSummeEroeffnungKontoIId(
				kassenbuchDto.getKontoIId(), getInternalFrameFinanz().getIAktuellesGeschaeftsjahr(), 12, true);
		if (null == ebKonto)
			ebKonto = BigDecimal.ZERO;
		kontoSaldo = ebKonto.add(kontoSaldo);

		kontoSaldo = kontoSaldo.subtract(oldEingang).add(oldAusgang);
		kontoSaldo = kontoSaldo.add(newEingang).subtract(newAusgang);
		return kontoSaldo.signum() >= 0;
	}

	@Override
	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		// Der Anwender kann das Geschaeftsjahr geaendert haben
		setNewGeschaeftsjahr(getInternalFrameFinanz().getIAktuellesGeschaeftsjahr());
		setPreviousValuesFromComponent();
	}

	@Override
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (buchungDto != null && buchungDto.getIId() != null)
			DelegateFactory.getInstance().getBuchenDelegate().storniereBuchung(buchungDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
													// ueberschreiben
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			// Eingang oder Ausgang muss <> 0 sein, NICHT beide duerfen einen
			// Betrag haben
			if (wnfEingang.getDouble() == null && wnfAusgang.getDouble() == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("finanz.error.betragdefinieren"));
				return;
			}
			if (wnfEingang.getDouble() != null && wnfAusgang.getDouble() != null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("finanz.error.eingangoderausgangwaehlen"));
				return;
			}
			if (wnfEingang.getDouble() != null && wnfEingang.getDouble().doubleValue() == 0.0) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("finanz.error.betragdefinieren"));
				return;
			}
			if (wnfAusgang.getDouble() != null && wnfAusgang.getDouble().doubleValue() == 0.0) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("finanz.error.betragdefinieren"));
				return;
			}

			boolean buchungDurchfuehren = true;

			KassenbuchDto kassenbuchDto = tabbedPaneKassenbuch.getKassenbuchDto();
			if (!isSaldoAllowed(kassenbuchDto, wnfEingang.getBigDecimal(), wnfAusgang.getBigDecimal(),
					previousEingangDecimal, previousAusgangDecimal)) {
				buchungDurchfuehren = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("fb.kassenbuch.saldonegativ.buchen"),
						LPMain.getTextRespectUISPr("fb.kassenbuch.saldonegativ.titel"));
			}

			if (buchungDurchfuehren) {
				components2Dto();

				KassenbuchungsteuerartDto kstDto = new KassenbuchungsteuerartDto();
				kstDto.setMwstsatzbezIId((Integer) wcoUst.getKeyOfSelectedItem());
				kstDto.setMwstsatzbezBezeichnung(wcoUst.getSelectedItem().toString());
				kstDto.setUstBuchung(wrbUst.isSelected());
				buchungDto = DelegateFactory.getInstance().getBuchenDelegate().verbucheKassenbuchung(buchungDto,
						gegenbuchungDto, kassenbuchDto.getKontoIId(), kstDto);
				setPreviousBookingDate(buchungDto.getDBuchungsdatum());
				// setKeyWhenDetailPanel(buchungDto.getIId());
				setKeyWhenDetailPanel(findIdFuerBuchung(buchungDto, kassenbuchDto));
				getGegenbuchung(gegenbuchungDto);

				super.eventActionSave(e, false);
			}
		}
	}

	protected Integer findIdFuerBuchung(BuchungDto buchungDto, KassenbuchDto kassenbuchDto) {
		try {
			BuchungdetailDto[] details = DelegateFactory.getInstance().getBuchenDelegate()
					.buchungdetailsFindByBuchungIId(buchungDto.getIId());
			for (BuchungdetailDto buchungdetailDto : details) {
				if (buchungdetailDto.getKontoIId().equals(kassenbuchDto.getKontoIId())) {
					return buchungdetailDto.getIId();
				}
			}
		} catch (Throwable t) {
		}

		return buchungDto.getIId();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RB_BELEG)) {
			boolean auto = wrbBelegAuto.isSelected();
			wtfBeleg.setEditable(!auto);
			wtfBeleg.setMandatoryField(!auto);
			if (auto)
				wtfBeleg.setText(null);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RB_KONTO)) {
			if (e.getSource().equals(wrbKontoDebitorenkonto) || e.getSource().equals(wrbKontoKreditorenkonto)) {
				showSteuerbuttons(false);
			} else if (e.getSource().equals(wrbKontoSachkonto)) {
				showSteuerbuttons(true);
			}
			refreshKontotyp();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CO_UST)) {
			if (wcoUst.getSelectedItem() != null) {
				calc();
			}
		}
	}

	private void showSteuerbuttons(boolean show) throws ExceptionLP, Throwable {
		wrbUst.setVisible(show);
		wrbVst.setVisible(show);
		setSteuersatz(false);
	}

	private void setSteuersatz(boolean bsetSteuerfrei) throws ExceptionLP, Throwable {
		MwstsatzbezDto mwstbezDto = DelegateFactory.getInstance().getMandantDelegate().getMwstsatzbezSteuerfrei();
		boolean bSteuerzulaessig = wrbKontoSachkonto.isSelected();
		wcoUst.setActivatable(bSteuerzulaessig);
		if (bSteuerzulaessig || bsetSteuerfrei)
			if (mwstbezDto != null) {
				wcoUst.setSelectedItem(mwstbezDto.getCBezeichnung());
			}
	}

	protected void refreshKontotyp() throws ExceptionLP {
		String newKontotyp = null;
		if (wrbKontoDebitorenkonto.isSelected()) {
			newKontotyp = FinanzServiceFac.KONTOTYP_DEBITOR;
		} else if (wrbKontoKreditorenkonto.isSelected()) {
			newKontotyp = FinanzServiceFac.KONTOTYP_KREDITOR;
		} else {
			newKontotyp = FinanzServiceFac.KONTOTYP_SACHKONTO;
		}
		// hat er sich geaendert?
		if (!kontotypKonto.equals(newKontotyp)) {
			kontotypKonto = newKontotyp;
			kontoDto = null;
			dto2ComponentsKonto();
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_FINANZ_KONTO;
	}

	private void setDetailBetraege(BuchungdetailDto buchungdetailDto, BigDecimal nbetrag, BigDecimal nust,
			boolean bucheAufSachkonto) {
		if (nust != null) {
			if (bucheAufSachkonto)
				// auf Sachkonten wird Netto gebucht
				buchungdetailDto.setNBetrag(nbetrag.subtract(nust));
			else
				// Debitoren/Kreditoren buchen brutto, ust ist nur zur Info!
				buchungdetailDto.setNBetrag(nbetrag);
			buchungdetailDto.setNUst(nust);
		} else {
			buchungdetailDto.setNBetrag(nbetrag);
			buchungdetailDto.setNUst(BigDecimal.ZERO);
		}
	}

	private void components2Dto() throws Throwable {
		// zuerst die Kopfdaten der Buchung
		calc();

		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_KASSENBUCHUNG);
		buchungDto.setCBelegnummer(wtfBeleg.getText());
		buchungDto.setCText(wtfText.getText());
		buchungDto.setDBuchungsdatum(wdfDatum.getDate());
		Integer geschaeftsjahr = getInternalFrameFinanz().getIAktuellesGeschaeftsjahr();
		buchungDto.setIGeschaeftsjahr(geschaeftsjahr);
		buchungDto.setKostenstelleIId(kostenstelleDto.getIId());

		gegenbuchungDto = new BuchungdetailDto();
		// dann Detail Gegenbuchung!!
		if (wnfEingang.getDouble() != null) {
			// buchungdetailDto.setBuchungdetailartCNr(BuchenFac.SollBuchung);
			gegenbuchungDto.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			setDetailBetraege(gegenbuchungDto, wnfEingang.getBigDecimal(), bdSteuerbetrag,
					wrbKontoSachkonto.isSelected());
		} else {
			// buchungdetailDto.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			gegenbuchungDto.setBuchungdetailartCNr(BuchenFac.SollBuchung);
			setDetailBetraege(gegenbuchungDto, wnfAusgang.getBigDecimal(), bdSteuerbetrag,
					wrbKontoSachkonto.isSelected());
		}

		gegenbuchungDto.setKontoIId(kontoDto.getIId());
		if (wnfAuszug.isVisible())
			gegenbuchungDto.setIAuszug(wnfAuszug.getInteger());
	}

	private void dto2ComponentsKostenstelle() {
		if (kostenstelleDto != null) {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		}
	}

	private void dto2ComponentsKonto() throws ExceptionLP {
		if (kontoDto != null) {
			wtfKontoNummer.setText(kontoDto.getCNr());
			wtfKontoBezeichnung.setText(kontoDto.getCBez());
			if (isBank(kontoDto)) {
				showAuszug(true);
			} else {
				showAuszug(false);
			}
		} else {
			wtfKontoNummer.setText(null);
			wtfKontoBezeichnung.setText(null);
			showAuszug(false);
		}
	}

	void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().createPanelFLRKostenstelle(getInternalFrame(),
				false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	void dialogQueryKonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKKonten(this.kontotypKonto);

		// SP9314
		FilterKriterium[] filtersNew = new FilterKriterium[filters.length + 1];
		for (int i = 0; i < filters.length; i++) {
			filtersNew[i] = filters[i];
		}

		filtersNew[filters.length] = new FilterKriterium(FinanzFac.FLR_KONTO_I_ID, true,
				"( "+tabbedPaneKassenbuch.getKassenbuchDto().getKontoIId() + ")", FilterKriterium.OPERATOR_NOT_IN, false);

		panelQueryFLRKonto = new PanelQueryFLR(qt, filtersNew, QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("finanz.liste.konten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
		new DialogQuery(panelQueryFLRKonto);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
						.kostenstelleFindByPrimaryKey((Integer) key);
				dto2ComponentsKostenstelle();
			} else if (e.getSource() == panelQueryFLRKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey((Integer) key);
				dto2ComponentsKonto();
			}
		}
	}

	/**
	 * Sobald eine UST eingegeben wurde, wird die UST in Prozent berechnet
	 *
	 * @throws Throwable
	 */
	void wnfBetrag_focusLost() throws Throwable {
		calc();
	}

	private void calc() throws Throwable {
		Timestamp timestamp = HelperTimestamp.cutOrToday(wdfDatum.getTimestamp());
		MwstsatzDto mwstsatzDto = DelegateFactory.mandant().mwstsatzFindZuDatum((Integer) wcoUst.getKeyOfSelectedItem(),
				timestamp);
		/*
		 * if (wdfDatum.getTimestamp() == null) { mwstsatzDto = DelegateFactory
		 * .getInstance() .getMandantDelegate()
		 * .mwstsatzFindByMwstsatzbezIIdAktuellster( (Integer)
		 * wcoUst.getKeyOfSelectedItem()); } else { mwstsatzDto = DelegateFactory
		 * .getInstance() .getMandantDelegate() .mwstsatzFindZuDatum( (Integer)
		 * wcoUst.getKeyOfSelectedItem(), wdfDatum.getTimestamp()); }
		 */
		if (mwstsatzDto == null) {
			bdSteuerbetrag = null;
			JOptionPane.showMessageDialog(this,
					"Mwstsatz zu Datum " + wdfDatum.getTimestamp().toString() + " kann nicht bestimmt werden");
		} else {
			// BigDecimal bdMwstSatz = new
			// BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2);
			// if (wnfEingang.getBigDecimal() != null) {
			// bdSteuerbetrag =
			// Helper.getMehrwertsteuerBetrag(wnfEingang.getBigDecimal(),
			// bdMwstSatz);
			// } else if (wnfAusgang.getBigDecimal() != null) {
			// bdSteuerbetrag =
			// Helper.getMehrwertsteuerBetrag(wnfAusgang.getBigDecimal(),
			// bdMwstSatz);
			// } else {
			// bdSteuerbetrag = null;
			// }
			Double mwstsatz = mwstsatzDto.getFMwstsatz();
			if (wnfEingang.getBigDecimal() != null) {
				bdSteuerbetrag = Helper.getMehrwertsteuerBetrag(wnfEingang.getBigDecimal(), mwstsatz);
			} else if (wnfAusgang.getBigDecimal() != null) {
				bdSteuerbetrag = Helper.getMehrwertsteuerBetrag(wnfAusgang.getBigDecimal(), mwstsatz);
			} else {
				bdSteuerbetrag = null;
			}
		}
	}

	// void reset() throws Throwable {
	// this.wnfEingang.setBigDecimal(new BigDecimal(0.00));
	// this.wnfAusgang.setBigDecimal(new BigDecimal(0.00));
	// wtfBeleg.removeContent();
	//
	// if (kontoDto != null) {
	// kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
	// .kontoFindByPrimaryKey(kontoDto.getIId());
	// }
	// }

	protected javax.swing.JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatum;
	}

	private boolean automatischeAuszugsnummer() {
		try {
			ParametermandantDto pm = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_AUSZUGSNUMMER_BEI_BANK_ANGEBEN);
			return !pm.asBoolean();
		} catch (Throwable e) {
			return false;
		}
	}

}
