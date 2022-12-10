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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGeschaeftsjahrDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;


public class PanelFinanzBankbuchung extends PanelBasis implements IGegenkontoListener, IKostenstelleListener, 
		IBetragListener, IMwstListener, IDatumListener, IBankkontoListener, IResetListener, IWaehrungListener, IInfoListener,
		ITextListener, IBelegnummerListener, IBuchungsartListener {
	
	private static final long serialVersionUID = -4159701137961974628L;

	private static final String ACTION_SPECIAL_GEGENKONTO = "action_special_bankbuchung_gegenkonto";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_bankbuchung_kostenstelle";
	private static final String ACTION_SPECIAL_RB_GEGENKONTO = "action_special_bankbuchung_rb_gegenkonto";
	private static final String ACTION_SPECIAL_RB_BELEG = "action_special_bankbuchung_rb_beleg";
	private static final String ACTION_SPECIAL_RB_KONTOSEITE = "action_special_bankbuchung_rb_kontoseite";
	private static final String ACTION_SPECIAL_CO_UST = "action_special_bankbuchung_co_ust";
	private static final String ACTION_SPECIAL_CO_WAEHRUNG = "action_special_bankbuchung_co_waehrung";
	private static final String ACTION_SPECIAL_BUCHUNG_UEBERNEHMEN = "action_special_buchung_uebernehmen";
	private static final String ACTION_SPECIAL_BUCHUNG_RESET = "action_special_buchung_reset";

	private JButton wrbBuchungUebernehmen = new WrapperButton();
	private JButton wrbReset;
	private WrapperLabel wlaInfo = new WrapperLabel();
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();
	private WrapperComboBox wcbSollHaben = new WrapperComboBox();
	private WrapperLabel wlaGegenkonto = new WrapperLabel();
	private WrapperRadioButton wrbSachkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbDebitorenkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbKreditorenkonto = new WrapperRadioButton();
	private ButtonGroup bugrKonto = new ButtonGroup();
	private WrapperButton wbuGegenkonto = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
	private WrapperLabel wlaBankkonto = new WrapperLabel();
	private WrapperTextField wtfBankkontoNummer = new WrapperTextField();
	private WrapperTextField wtfBankkontoBezeichnung = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperLabel wlaAuszug = new WrapperLabel();
	private WrapperTextNumberField wnfAuszug = new WrapperTextNumberField();
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperNumberField wnfBetrag = new WrapperNumberField();
	private WrapperLabel wlaWaehrungBetrag = new WrapperLabel();
	private WrapperLabel wlaMwst = new WrapperLabel();
	private WrapperNumberField wnfMwst = new WrapperNumberField();
	private WrapperLabel wlaWaehrungMwst = new WrapperLabel();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();
	private WrapperComboBox wcoMwst = new WrapperComboBox();
	private WrapperLabel wlaKursBetrag = new WrapperLabel();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperTextField wtfText = new WrapperTextField();
	private WrapperLabel wlaBeleg = new WrapperLabel();
	private WrapperRadioButton wrbBelegAuto = new WrapperRadioButton();
	private WrapperRadioButton wrbBelegHand = new WrapperRadioButton();
	private ButtonGroup bugrBeleg = new ButtonGroup();
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperEditorField wefKommentar;
	private WrapperLabel wlaAuszugGegenkonto = new WrapperLabel();
	private WrapperNumberField wnfAuszugGegenkonto = new WrapperNumberField();
	
	private IFinanzBankbuchungController controller;
	private IFinanzBankbuchungViewController viewController;
	private MyFocusListener focusListener = new MyFocusListener();
	
	public PanelFinanzBankbuchung(IFinanzBankbuchungController controller, IFinanzBankbuchungViewController viewController) throws Throwable {
		super(controller.getInternalFrame(), LPMain.getTextRespectUISPr("fb.menu.umbuchung"));
		this.controller = controller;
		this.viewController = viewController;
		
		viewController.registerGegenkontoListener(this);
		viewController.registerBankkontoListener(this);
		viewController.registerBuchungsbetragListener(this);
		viewController.registerDatumListener(this);
		viewController.registerKostenstelleListener(this);
		viewController.registerMwstListener(this);
		viewController.registerResetListener(this);
		viewController.registerWaehrungListener(this);
		viewController.registerInfoListener(this);
		viewController.registerTextListener(this);
		viewController.registerBelegnummerListener(this);
		viewController.registerBuchungsartListener(this);
		
		jbInit();
		setDefaults();
	}

	private void jbInit() throws Throwable {
		wrbBuchungUebernehmen = ButtonFactory.createJButton(IconFactory.getServerOk(), 
				LPMain.getTextRespectUISPr("fb.sepa.import.buchunguebernehmen"), ACTION_SPECIAL_BUCHUNG_UEBERNEHMEN);
		wrbBuchungUebernehmen.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		wrbBuchungUebernehmen.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		wrbBuchungUebernehmen.setMaximumSize(HelperClient.getToolsPanelButtonDimension());
		wrbBuchungUebernehmen.addActionListener(this);
		
		wrbReset = ButtonFactory.createJButton(IconFactory.getDelete(), 
				LPMain.getTextRespectUISPr("fb.sepa.import.eingabenreset"), ACTION_SPECIAL_BUCHUNG_RESET);
		wrbReset.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		wrbReset.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		wrbReset.setMaximumSize(HelperClient.getToolsPanelButtonDimension());
		wrbReset.addActionListener(this);
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new MigLayout("", ""));
		panelButton.add(wrbBuchungUebernehmen, "align left");
		panelButton.add(wrbReset, "align left");
		
		wlaInfo.setHorizontalAlignment(SwingConstants.LEFT);
		JPanel panelHead = new JPanel();
		panelHead.setLayout(new MigLayout("", "[fill|fill,grow]"));
		panelHead.add(panelButton);
		panelHead.add(wlaInfo, "align left");
		
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wdfDatum = new WrapperGeschaeftsjahrDateField(controller.getAktuellesGeschaeftsjahr());
		wdfDatum.setMandatoryField(true);
		
		wcbSollHaben.addItem(LPMain.getTextRespectUISPr("finanz.soll"));
		wcbSollHaben.addItem(LPMain.getTextRespectUISPr("finanz.haben"));
		wcbSollHaben.setActionCommand(ACTION_SPECIAL_RB_KONTOSEITE);
		wcbSollHaben.addActionListener(this);
		
		wrbBelegAuto.setText(LPMain.getTextRespectUISPr("lp.automatisch"));
		wrbBelegAuto.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegAuto.addActionListener(this);
		wrbBelegHand.setText(LPMain.getTextRespectUISPr("label.handeingabe"));
		wrbBelegHand.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegHand.addActionListener(this);
		bugrBeleg.add(wrbBelegHand);
		bugrBeleg.add(wrbBelegAuto);
		
		wrbSachkonto.setText(LPMain.getTextRespectUISPr("lp.sachkonto"));
		wrbSachkonto.setActionCommand(ACTION_SPECIAL_RB_GEGENKONTO);
		wrbSachkonto.addActionListener(this);
		wrbDebitorenkonto.setText(LPMain.getTextRespectUISPr("lp.debitorenkonto"));
		wrbDebitorenkonto.setActionCommand(ACTION_SPECIAL_RB_GEGENKONTO);
		wrbDebitorenkonto.addActionListener(this);
		wrbKreditorenkonto.setText(LPMain.getTextRespectUISPr("lp.kreditorenkonto"));
		wrbKreditorenkonto.setActionCommand(ACTION_SPECIAL_RB_GEGENKONTO);
		wrbKreditorenkonto.addActionListener(this);
		bugrKonto.add(wrbSachkonto);
		bugrKonto.add(wrbDebitorenkonto);
		bugrKonto.add(wrbKreditorenkonto);

		wlaGegenkonto.setText(LPMain.getTextRespectUISPr("finanz.gegenkonto"));
		wbuGegenkonto.setText(LPMain.getTextRespectUISPr("button.konto"));
		wbuGegenkonto.setActionCommand(ACTION_SPECIAL_GEGENKONTO);
		wbuGegenkonto.addActionListener(this);
		wtfKontoNummer.setMinimumSize(new Dimension(100, 23));
		wtfKontoNummer.setPreferredSize(new Dimension(100, 23));
		wtfKontoNummer.setActivatable(true);
		wtfKontoNummer.setMandatoryField(true);
		wtfKontoNummer.addFocusListener(focusListener);
		wtfKontoBezeichnung.setActivatable(false);
		
		wlaBankkonto.setText(LPMain.getTextRespectUISPr("fb.sepa.import.bankkonto"));
		wtfBankkontoNummer.setMinimumSize(new Dimension(100, 23));
		wtfBankkontoNummer.setPreferredSize(new Dimension(100, 23));
		wtfBankkontoNummer.setActivatable(false);
		wtfBankkontoNummer.setMandatoryField(true);
		wtfBankkontoBezeichnung.setActivatable(false);
		wlaAuszugGegenkonto.setText(LPMain.getTextRespectUISPr("label.auszug"));
		wnfAuszugGegenkonto.setMaximumIntegerDigits(FinanzFac.MAX_UMBUCHUNG_AUSZUG);
		wnfAuszugGegenkonto.setFractionDigits(0);
		wnfAuszugGegenkonto.setVisible(false);
		wnfAuszugGegenkonto.addFocusListener(focusListener);
		
		wbuKostenstelle.setText(LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKostenstelle.addActionListener(this);
		wtfKostenstelleNummer.setActivatable(false);
		wtfKostenstelleNummer.setMandatoryField(true);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wlaAuszug.setText(LPMain.getTextRespectUISPr("label.auszug"));
		wnfAuszug.setMaximumDigits(FinanzFac.MAX_UMBUCHUNG_AUSZUG);
		
		wlaBetrag.setText(LPMain.getTextRespectUISPr("label.betrag"));
		wnfBetrag.setMandatoryField(true);
		wnfBetrag.setDependenceField(true);
		wnfBetrag.addFocusListener(focusListener);
		wlaMwst.setText(LPMain.getTextRespectUISPr("label.mwst"));
		wlaWaehrungBetrag.setText(LPMain.getTextRespectUISPr("fb.sepa.import.eur"));
		wlaWaehrungBetrag.setHorizontalAlignment(SwingConstants.LEFT);
		wnfMwst.setActivatable(false);
		wnfMwst.setDependenceField(true);
		wlaWaehrungMwst.setText(LPMain.getTextRespectUISPr("fb.sepa.import.eur"));
		wlaWaehrungMwst.setHorizontalAlignment(SwingConstants.LEFT);
		wcoMwst.setMandatoryField(true);
		wcoMwst.setActionCommand(ACTION_SPECIAL_CO_UST);
		wcoMwst.addActionListener(this);
		wcoWaehrung.setMandatoryField(true);
		wcoWaehrung.setActionCommand(ACTION_SPECIAL_CO_WAEHRUNG);
		wcoWaehrung.addActionListener(this);
		wlaKursBetrag.setHorizontalAlignment(SwingConstants.LEFT);
		
		wlaText.setText(LPMain.getTextRespectUISPr("lp.text"));
		wtfText.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_TEXT);
		wtfText.setMandatoryField(true);
		wtfText.addFocusListener(focusListener);
		
		wlaBeleg.setText(LPMain.getTextRespectUISPr("lp.beleg"));
		wtfBeleg.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_BELEG);
		wtfBeleg.setMandatoryField(true);
		wtfBeleg.addFocusListener(focusListener);
		
		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));
		wefKommentar = new KommentarField(
				controller.getInternalFrame(), LPMain.getTextRespectUISPr("lp.kommentar"));
		
		this.setLayout(new MigLayout("hidemode 3, wrap 5", "[15%, fill][20%, fill][15%, fill][15%, fill][35%, fill]"));
		
		add(panelHead, "align left, span");
		
		add(wlaDatum);
		add(wdfDatum, "wrap");
		add(wcbSollHaben, "skip 1, wrap");
		
		add(wlaBankkonto);
		add(wtfBankkontoNummer);
		add(wtfBankkontoBezeichnung, "span 3");
		
		add(wlaAuszug);
		add(wnfAuszug, "wrap");
		
		add(wlaGegenkonto);
		add(wrbSachkonto);
		add(wrbDebitorenkonto, "span 2");
		add(wrbKreditorenkonto);
		
		add(wbuGegenkonto);
		add(wtfKontoNummer);
		add(wtfKontoBezeichnung, "span 3");
		
		add(wlaAuszugGegenkonto);
		add(wnfAuszugGegenkonto, "wrap");
		
		add(wbuKostenstelle);
		add(wtfKostenstelleNummer);
		add(wtfKostenstelleBezeichnung, "span 3");
		
		add(wlaBetrag);
		add(wnfBetrag);
		add(wlaWaehrungBetrag, "align left");
		add(wcoWaehrung);
		add(wlaKursBetrag, "gapleft 10, span 2");
		
		add(wlaMwst);
		add(wnfMwst);
		add(wlaWaehrungMwst, "align left");
		add(wcoMwst, "span 2, wrap");
		
		add(wlaText);
		add(wtfText, "span 4");
		
		add(wlaBeleg);
		add(wrbBelegHand);
		add(wtfBeleg, "span 2, wrap");
		
		add(wrbBelegAuto, "skip, wrap");
		
		add(wlaKommentar);
		add(wefKommentar, "span 4");
	}
	
	private void setDefaults() {
		wcbSollHaben.setSelectedIndex(0);
		wrbSachkonto.setSelected(true);
		wrbBelegHand.setSelected(true);
		wdfDatum.setDate(null);
		wtfBankkontoBezeichnung.removeContent();
		wtfBankkontoNummer.removeContent();
		wtfBeleg.removeContent();
		wtfKontoBezeichnung.removeContent();
		wtfKontoNummer.removeContent();
		wtfKostenstelleBezeichnung.removeContent();
		wtfKostenstelleNummer.removeContent();
		wtfText.removeContent();
		
		wnfAuszug.removeContent();
		try {
			wnfBetrag.setBigDecimal(BigDecimal.ZERO);
			wnfMwst.setBigDecimal(BigDecimal.ZERO);
			wcoMwst.setMap(DelegateFactory.getInstance().getMandantDelegate()
					.getAllMwstsatzbez(LPMain.getTheClient().getMandant()));
			wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate().getAllWaehrungen());
			wcoWaehrung.setKeyOfSelectedItem(LPMain.getTheClient().getSMandantenwaehrung());
		} catch (ExceptionLP e) {
			throw new EJBExceptionLP(e);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return null;
	}

	public void eventActionSpecial(ActionEvent event) throws Throwable {
		if (event == null) return;
		
		if (ACTION_SPECIAL_GEGENKONTO.equals(event.getActionCommand())) {
			viewController.actionChooseGegenkonto();
		} else if (ACTION_SPECIAL_KOSTENSTELLE.equals(event.getActionCommand())) {
			viewController.actionChooseKostenstelle();
			
		} else if (ACTION_SPECIAL_RB_BELEG.equals(event.getActionCommand())) {
			Boolean handSelected = wrbBelegHand.isSelected();
			enableBelegTextfield(handSelected);
			
			if (wrbBelegAuto.isSelected()) {
				viewController.actionUpdateAutomatischeBelegnummer();
				wtfBeleg.removeContent();
			} else if (wrbBelegHand.isSelected()) {
				viewController.actionUpdateBelegnummerHandeingabe(null);
			}
			
		} else if (ACTION_SPECIAL_RB_GEGENKONTO.equals(event.getActionCommand())) {
			if (wrbDebitorenkonto.isSelected()) {
				viewController.actionUpdateGegenkontoKontotyp(FinanzServiceFac.KONTOTYP_DEBITOR);
			} else if (wrbSachkonto.isSelected()) {
				viewController.actionUpdateGegenkontoKontotyp(FinanzServiceFac.KONTOTYP_SACHKONTO);
			} else if (wrbKreditorenkonto.isSelected()) {
				viewController.actionUpdateGegenkontoKontotyp(FinanzServiceFac.KONTOTYP_KREDITOR);
			}
			
		} else if (ACTION_SPECIAL_RB_KONTOSEITE.equals(event.getActionCommand())) {
			if (wcbSollHaben.getSelectedIndex() == 0) {
				viewController.actionUpdateBuchungsart(BuchenFac.SollBuchung);
			} else {
				viewController.actionUpdateBuchungsart(BuchenFac.HabenBuchung);
			}
			
		} else if (ACTION_SPECIAL_CO_UST.equals(event.getActionCommand())) {
			viewController.actionUpdateMwstSatz((Integer) wcoMwst.getKeyOfSelectedItem());
			
		} else if (ACTION_SPECIAL_CO_WAEHRUNG.equals(event.getActionCommand())) {
			// action update waehrung
		} else if (ACTION_SPECIAL_BUCHUNG_UEBERNEHMEN.equals(event.getActionCommand())) {
			if (allMandatoryFieldsSetDlg()) {
				viewController.actionSave();
			}
		} else if (ACTION_SPECIAL_BUCHUNG_RESET.equals(event.getActionCommand())) {
			viewController.actionReset();
		}
	}

	private void eventActionFocusLost(FocusEvent focusEvent) {
		if (focusEvent == null) return;
		
		if (wtfKontoNummer == focusEvent.getSource()) {
			viewController.actionUpdateGegenkonto(wtfKontoNummer.getText());
			
		} else if (wnfBetrag == focusEvent.getSource()) {
			try {
				viewController.actionUpdateBetrag(wnfBetrag.getBigDecimal());
			} catch (ExceptionLP e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (wtfText == focusEvent.getSource()) {
			viewController.actionUpdateText(wtfText.getText());
			
		} else if (wtfBeleg == focusEvent.getSource()) {
			viewController.actionUpdateBelegnummerHandeingabe(wtfBeleg.getText());
		} else if (wnfAuszugGegenkonto == focusEvent.getSource()) {
			try {
				viewController.actionUpdateAuszugnummerGegenkonto(wnfAuszugGegenkonto.getInteger());
			} catch (ExceptionLP e) {
				viewController.actionUpdateAuszugnummerGegenkonto(null);
			}
		}
	}
	
	@Override
	public void updateGegenkonto(KontoDto gegenkontoDto) {
		if (gegenkontoDto == null) {
			wtfKontoNummer.removeContent();
			wtfKontoBezeichnung.removeContent();
//			wrbSachkonto.setSelected(true);
		} else {
			wtfKontoNummer.setText(gegenkontoDto.getCNr());
			wtfKontoBezeichnung.setText(gegenkontoDto.getCBez());
			updateKontotypGegenkonto(gegenkontoDto.getKontotypCNr());
		}
	}

	@Override
	public void updateKontotypGegenkonto(String kontotyp) {
		if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontotyp)) {
			wrbSachkonto.setSelected(true);
		} else if (FinanzServiceFac.KONTOTYP_DEBITOR.equals(kontotyp)) {
			wrbDebitorenkonto.setSelected(true);
		} else if (FinanzServiceFac.KONTOTYP_KREDITOR.equals(kontotyp)) {
			wrbKreditorenkonto.setSelected(true);
		}
	}

	@Override
	public void updateMwstBetrag(BigDecimal mwstBetrag) {
		if (mwstBetrag == null) mwstBetrag = BigDecimal.ZERO;
		
		try {
			wnfMwst.setBigDecimal(mwstBetrag);
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void updateMwstSatz(Integer mwstSatzBezIId) {
		wcoMwst.setKeyOfSelectedItem(mwstSatzBezIId);
	}

	@Override
	public void enableMwstSatz(Boolean value) {
		wcoMwst.setEnabled(value);
	}

	@Override
	public void updateBuchungsbetrag(BigDecimal betrag) {
		if (betrag == null) betrag = BigDecimal.ZERO;
		
		try {
			wnfBetrag.setBigDecimal(betrag);
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void updateKurs(WechselkursDto kursDto) {
		try {
			if (kursDto == null 
					|| LPMain.getTheClient().getSMandantenwaehrung().equals(wcoWaehrung.getKeyOfSelectedItem())) {
				wlaKursBetrag.setText(null);
			} else {
				BigDecimal betragMandant = wnfBetrag.getBigDecimal();
				if (BigDecimal.ONE.compareTo(kursDto.getNKurs()) != 0) {
					betragMandant = Helper.rundeKaufmaennisch(
							betragMandant.multiply(kursDto.getNKurs()), FinanzFac.NACHKOMMASTELLEN);
				}
				wlaKursBetrag.setText(LPMain.getMessageTextRespectUISPr("fb.bankbuchung.kursbetrag", 
						new Object[] {kursDto.getNKurs(), betragMandant, LPMain.getTheClient().getSMandantenwaehrung()}));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateKostenstelle(KostenstelleDto kostenstelleDto) {
		if (kostenstelleDto == null) {
			wtfKostenstelleNummer.removeContent();
			wtfKostenstelleBezeichnung.removeContent();
		} else {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		}
	}

	@Override
	public void updateDatum(Date date, Boolean enable) {
		wdfDatum.setDate(date);
		wdfDatum.setEnabled(enable);
	}

	@Override
	public void reset() {
		setDefaults();
	}

	@Override
	public void updateBankkonto(KontoDto bankkontoDto) {
		if (bankkontoDto == null) {
			wtfBankkontoNummer.removeContent();
			wtfBankkontoBezeichnung.removeContent();
		} else {
			wtfBankkontoNummer.setText(bankkontoDto.getCNr());
			wtfBankkontoBezeichnung.setText(bankkontoDto.getCBez());
		}
	}

	@Override
	public void updateAuszugnummer(Integer auszugnummer) {
		wnfAuszug.setInteger(auszugnummer);
	}

	@Override
	public void enableAuszugnummer(Boolean enable) {
		wnfAuszug.setActivatable(enable);
	}

	@Override
	public void updateAuszugnummerGegenkonto(Integer auszugnummer) {
		wnfAuszugGegenkonto.setInteger(auszugnummer);
	}

	@Override
	public void enableAuszugnummerGegenkonto(Boolean enable) {
		wnfAuszugGegenkonto.setMandatoryField(enable);
		wnfAuszugGegenkonto.setVisible(enable);
		wlaAuszugGegenkonto.setVisible(enable);
	}

	@Override
	public void updateWaehrung(String waehrungCNr) {
		wcoWaehrung.setKeyOfSelectedItem(waehrungCNr);
		wlaWaehrungBetrag.setText(waehrungCNr);
		wlaWaehrungMwst.setText(waehrungCNr);
	}

	@Override
	public void enableWaehrung(Boolean enable) {
		wcoWaehrung.setEnabled(enable);
	}
	
	@Override
	public void updateInfo(String text) {
		wlaInfo.setText(text);
	}


	@Override
	public void updateBelegnummerHand(String cBelegnummer) {
		wrbBelegHand.setSelected(true);
		enableBelegTextfield(true);
		wtfBeleg.setText(cBelegnummer);
	}

	@Override
	public void updateBelegnummerAuto() {
		wrbBelegAuto.setSelected(true);
		enableBelegTextfield(false);
		wtfBeleg.setText(null);
	}

	private void enableBelegTextfield(boolean enable) {
		wtfBeleg.setMandatoryField(enable);
		wtfBeleg.setEditable(enable);
	}

	@Override
	public void updateText(String text) {
		wtfText.setText(text);
	}

	@Override
	public void updateBuchungsartBankkonto(String buchungsart) {
		if (BuchenFac.SollBuchung.equals(buchungsart)) {
			wcbSollHaben.setSelectedIndex(0);
		} else {
			wcbSollHaben.setSelectedIndex(1);
		}
	}

	@Override
	public void updateBuchungsartGegenkonto(String buchungsart) {
		if (BuchenFac.SollBuchung.equals(buchungsart)) {
			wlaGegenkonto.setText(LPMain.getTextRespectUISPr("finanz.gegenkonto.soll"));
		} else if (BuchenFac.HabenBuchung.equals(buchungsart)) {
			wlaGegenkonto.setText(LPMain.getTextRespectUISPr("finanz.gegenkonto.haben"));
		} else {
			wlaGegenkonto.setText(LPMain.getTextRespectUISPr("finanz.gegenkonto"));
		}
	}

	protected class MyFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {
		}

		@Override
		public void focusLost(FocusEvent focusEvent) {
			eventActionFocusLost(focusEvent);
		}
		
	}
	
	protected class KommentarField extends WrapperEditorFieldKommentar {

		private static final long serialVersionUID = -2627313164294992983L;

		public KommentarField(InternalFrame internalFrame, String addTitleI) throws Throwable {
			super(internalFrame, addTitleI);
		}

		@Override
		public void setText(String text) {
			super.setText(text);
			try {
				viewController.actionUpdateKommentar(getText());
			} catch (TextBlockOverflowException e) {
			}
		}
		
	}
}
