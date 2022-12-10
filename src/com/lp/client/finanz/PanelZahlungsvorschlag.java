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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.EingangsrechnungDelegate;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.ErZahlungsempfaenger;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.util.EingangsrechnungId;

import net.miginfocom.swing.MigLayout;

/**
 * <p> Diese Klasse kuemmert sich um den ZV</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 30.09.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:09:12 $
 */
public class PanelZahlungsvorschlag extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanelWorkingOn = new JPanel();

	private WrapperLabel wlaLieferant = null;
	private WrapperTextField wtfLieferant = null;
	private WrapperTextField wtfAdresse = null;
	private WrapperLabel wlaAbteilung = null;
	private WrapperTextField wtfAbteilung = null;

	private WrapperLabel wlaERBrutto = null;
	private WrapperNumberField wnfErBrutto = null;
	private WrapperLabel wlaSkonto = null;
	private WrapperNumberField wnfSkonto = null;

	private WrapperLabel wlaBelegdatum = null;
	private WrapperDateField wdfBelegdatum = null;
	private WrapperLabel wlaFreigabedatum = null;
	private WrapperDateField wdfFreigabedatum = null;

	private WrapperLabel wlaOffen = null;
	private WrapperNumberField wnfOffen = null;
	private WrapperLabel wlaZahlbetrag = null;
	private WrapperNumberField wnfZahlbetrag = null;

	private WrapperLabel wlaWaehrung1 = null;
	private WrapperLabel wlaWaehrung2 = null;
	private WrapperLabel wlaSkontoProzent = null;
	private WrapperLabel wlaWaehrung3 = null;

	private WrapperLabel wlaGesamt = null;
	private WrapperNumberField wnfGesamt = null;
	private WrapperLabel wlaWaehrungGesamt = null;

	private WrapperCheckBox wcbVollstaendigBezahlt = null;

	private ZahlungsvorschlagDto zvDto = null;
	private EingangsrechnungDto erDto = null;

	private TabbedPaneZahlungsvorschlag tabbedPaneZV = null;

	private WrapperGotoButton wbuGotoLieferant = null;
	private WrapperLabel wlaEingangsrechnung = null;
	private WrapperTextField wtfEingangsrechnungCNr = null;
	private WrapperGotoButton wbuGotoEingangsrechnung = null;
	
	private WrapperLabel wlaAbweichendeBankverbindung = null;
	private WrapperLabel wlaBank = null;
	private WrapperTextField wtfBank = null;
	private WrapperLabel wlaBIC = null;
	private WrapperTextField wtfBIC = null;
	private WrapperLabel wlaIBAN = null;
	private WrapperTextField wtfIBAN = null;

	public PanelZahlungsvorschlag(InternalFrame internalFrame,
			String add2TitleI, Object pk,
			TabbedPaneZahlungsvorschlag tabbedPaneZV) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.tabbedPaneZV = tabbedPaneZV;
		jbInit();
		initComponents();
	}

	private void dto2Components() throws Throwable {
		if (zvDto != null) {
			holeEingangsrechnungDto(zvDto.getEingangsrechnungIId());
			// StatusBar
			this.setStatusbarPersonalIIdAnlegen(tabbedPaneZV.getZVlaufDto()
					.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(tabbedPaneZV.getZVlaufDto().getTAnlegen());
			
			wbuGotoLieferant.setOKey(erDto.getLieferantIId());
			wbuGotoEingangsrechnung.setOKey(erDto.getIId());
		}
	}

	private void components2Dto() throws Throwable {
		zvDto.setNZahlbetrag(wnfZahlbetrag.getBigDecimal());
		zvDto.setBWaereVollstaendigBezahlt(wcbVollstaendigBezahlt.getShort());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Integer key = getKeyWhenDetailPanel() == null ? null : (Integer) getKeyWhenDetailPanel();

		if (key == null) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			zvDto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.zahlungsvorschlagFindByPrimaryKey(key);
			dto2Components();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	private void jbInit() throws Throwable {
		wlaLieferant = new WrapperLabel(LPMain.getTextRespectUISPr("label.lieferant"));
		wtfLieferant = new WrapperTextField();
		wtfLieferant.setColumnsMax(100);
		wtfLieferant.setActivatable(false);

		wtfAdresse = new WrapperTextField();
		wtfAdresse.setColumnsMax(100);
		wtfAdresse.setActivatable(false);

		wlaAbteilung = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.abteilung"));
		wtfAbteilung = new WrapperTextField();
		wtfAbteilung.setColumnsMax(100);
		wtfAbteilung.setActivatable(false);

		wlaERBrutto = new WrapperLabel(
				LPMain.getTextRespectUISPr("er.zahlungsvorschlag.erbrutto"));
		wnfErBrutto = new WrapperNumberField();
		wnfErBrutto.setActivatable(false);
		wlaSkonto = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.skonto"));
		wnfSkonto = new WrapperNumberField();
		wnfSkonto.setActivatable(false);
		
		wlaEingangsrechnung = new WrapperLabel(
				LPMain.getTextRespectUISPr("er.eingangsrechnung"));
		wbuGotoEingangsrechnung = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_EINGANGSRECHNUNG_AUSWAHL);
		wbuGotoEingangsrechnung.getWrapperButton().setVisible(false);
		wtfEingangsrechnungCNr = new WrapperTextField();
		wtfEingangsrechnungCNr.setActivatable(false);

		wlaBelegdatum = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.belegdatum"));
		wdfBelegdatum = new WrapperDateField();
		wdfBelegdatum.setActivatable(false);
		wlaFreigabedatum = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.freigabedatum"));
		wdfFreigabedatum = new WrapperDateField();
		wdfFreigabedatum.setActivatable(false);

		wlaOffen = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.offen"));
		wnfOffen = new WrapperNumberField();
		wnfOffen.setActivatable(false);
		wlaZahlbetrag = new WrapperLabel(
				LPMain.getTextRespectUISPr("er.zahlungsvorschlag.zahlbetrag"));
		wnfZahlbetrag = new WrapperNumberField();
		wnfZahlbetrag.setMandatoryFieldDB(true);
		wnfZahlbetrag.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				resetVollstaendigBezahlt();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		wlaWaehrung1 = new WrapperLabel();
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2 = new WrapperLabel();
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSkontoProzent = new WrapperLabel("%");
		wlaSkontoProzent.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3 = new WrapperLabel();
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);

		wlaBank = new WrapperLabel(LPMain.getTextRespectUISPr("part.kund.banken"));
		wtfBank = new WrapperTextField();
		wtfBank.setActivatable(false);
		wlaIBAN = new WrapperLabel(LPMain.getTextRespectUISPr("lp.iban"));
		wtfIBAN = new WrapperTextField();
		wtfIBAN.setActivatable(false);
		wlaBIC = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bic"));
		wtfBIC = new WrapperTextField();
		wtfBIC.setActivatable(false);

		wlaGesamt = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.gesamtwert"));
		wnfGesamt = new WrapperNumberField();
		wnfGesamt.setActivatable(false);

		wcbVollstaendigBezahlt = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("er.zahlungsvorschlag.vollstaendigbezahlt"));

		// Mandantenwaehrung
		wlaWaehrungGesamt = new WrapperLabel(LPMain
				.getTheClient().getSMandantenwaehrung());
		wlaWaehrungGesamt.setHorizontalAlignment(SwingConstants.LEFT);

		wlaLieferant.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaLieferant.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaEingangsrechnung.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaEingangsrechnung.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung1.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung1.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung2.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung2.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaSkonto.setMinimumSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		wlaSkonto.setPreferredSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		wlaSkontoProzent.setMinimumSize(new Dimension(30, Defaults
				.getInstance().getControlHeight()));
		wlaSkontoProzent.setPreferredSize(new Dimension(30, Defaults
				.getInstance().getControlHeight()));
		
		wlaAbweichendeBankverbindung = new WrapperLabel();
		wlaAbweichendeBankverbindung.setHorizontalAlignment(SwingConstants.LEFT);
		
		wbuGotoLieferant = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_LIEFERANT_AUSWAHL);
		wbuGotoLieferant.getWrapperButton().setVisible(false);
		JPanel lieferantPanel = new JPanel(new MigLayout("insets 0, align right", "[fill|fill]"));
		lieferantPanel.add(wlaLieferant);
		lieferantPanel.add(wbuGotoLieferant);
				
		JPanel erPanel = new JPanel(new MigLayout("insets 0, align right", "[fill|fill]"));
		erPanel.add(wlaEingangsrechnung);
		erPanel.add(wbuGotoEingangsrechnung);

		this.setLayout(new GridBagLayout());
		// Actionpanel von Oberklasse holen und anhaengen.
		getInternalFrame().addItemChangedListener(this);
		this.add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel(new MigLayout("wrap 6",
				"[fill,15%|fill,30%|fill,5%|fill,15%|fill,30%|fill,5%]"));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		// Felder

		jPanelWorkingOn.add(erPanel);
		jPanelWorkingOn.add(wtfEingangsrechnungCNr, "span 2, wrap");
		
		jPanelWorkingOn.add(wlaAbweichendeBankverbindung, "skip 4, span 2, wrap");
		
		jPanelWorkingOn.add(lieferantPanel);
		jPanelWorkingOn.add(wtfLieferant, "span 2");
		jPanelWorkingOn.add(wlaBank);
		jPanelWorkingOn.add(wtfBank, "span 2, wrap");

		jPanelWorkingOn.add(wtfAdresse, "skip, span 2");
		jPanelWorkingOn.add(wlaIBAN);
		jPanelWorkingOn.add(wtfIBAN, "span 2, wrap");

		jPanelWorkingOn.add(wlaAbteilung);
		jPanelWorkingOn.add(wtfAbteilung, "span 2");
		jPanelWorkingOn.add(wlaBIC);
		jPanelWorkingOn.add(wtfBIC, "span 2, wrap");

		jPanelWorkingOn.add(wlaBelegdatum);
		jPanelWorkingOn.add(wdfBelegdatum);
		jPanelWorkingOn.add(wlaFreigabedatum, "span 2");
		jPanelWorkingOn.add(wdfFreigabedatum, "span 2");

		jPanelWorkingOn.add(wlaERBrutto);
		jPanelWorkingOn.add(wnfErBrutto);
		jPanelWorkingOn.add(wlaWaehrung1);
		jPanelWorkingOn.add(wlaSkonto);
		jPanelWorkingOn.add(wnfSkonto);
		jPanelWorkingOn.add(wlaSkontoProzent);

		jPanelWorkingOn.add(wlaOffen);
		jPanelWorkingOn.add(wnfOffen);
		jPanelWorkingOn.add(wlaWaehrung2);
		jPanelWorkingOn.add(wlaZahlbetrag);
		jPanelWorkingOn.add(wnfZahlbetrag);
		jPanelWorkingOn.add(wlaWaehrung3);

		jPanelWorkingOn.add(wcbVollstaendigBezahlt, "skip 1");
		jPanelWorkingOn.add(wlaGesamt, " span 2");
		jPanelWorkingOn.add(wnfGesamt);
		jPanelWorkingOn.add(wlaWaehrungGesamt);

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected void resetVollstaendigBezahlt() {
		try {
			BigDecimal zahlbetrag = wnfZahlbetrag.getBigDecimal();
			BigDecimal offenerBetrag = wnfOffen.getBigDecimal();
			
			if (zahlbetrag == null || offenerBetrag == null
					|| zahlbetrag.compareTo(offenerBetrag) < 0) 
				wcbVollstaendigBezahlt.setSelected(false);
		} catch (ExceptionLP e) {
			handleException(e, false);
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ZAHLUNGSVORSCHLAG;
	}

	private void holeEingangsrechnungDto(Integer key) throws Throwable {
		if (key != null) {
			this.erDto = DelegateFactory.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungFindByPrimaryKey(key);
			dto2ComponentsER();
		}
	}

	private void dto2ComponentsER() throws Throwable {
		if (erDto != null) {
			LieferantDto lieferantDto = DelegateFactory.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(erDto.getLieferantIId());
			wtfLieferant.setText(lieferantDto.getPartnerDto()
					.formatFixTitelName1Name2());
			wtfAdresse.setText(lieferantDto.getPartnerDto().formatAdresse());
			wtfAbteilung.setText(lieferantDto.getPartnerDto()
					.getCName3vorname2abteilung());
			wtfEingangsrechnungCNr.setText(erDto.getCNr());
			wdfBelegdatum.setDate(erDto.getDBelegdatum());
			wdfFreigabedatum.setDate(erDto.getDFreigabedatum());
			wlaWaehrung1.setText(erDto.getWaehrungCNr());
			wlaWaehrung2.setText(erDto.getWaehrungCNr());
			wlaWaehrung3.setText(erDto.getWaehrungCNr());

			wnfErBrutto.setBigDecimal(zvDto.getNErBruttoBetrag());

			wnfSkonto.setBigDecimal(zvDto.getNAngewandterskontosatz());
			BigDecimal offen = zvDto.getNErBruttoBetrag().subtract(
					zvDto.getNBereitsBezahlt());
			wnfOffen.setBigDecimal(offen);
			wnfZahlbetrag.setBigDecimal(zvDto.getNZahlbetrag());
			wcbVollstaendigBezahlt.setShort(zvDto
					.getBWaereVollstaendigBezahlt());

			bankverbindung2Components();

		} else {
			wtfLieferant.setText(null);
			wtfAdresse.setText(null);
			wtfAbteilung.setText(null);
			wtfEingangsrechnungCNr.setText(null);
			wdfBelegdatum.setDate(null);
			wdfFreigabedatum.setDate(null);
			wlaWaehrung1.setText(null);
			wlaWaehrung2.setText(null);
			wlaWaehrung3.setText(null);
			wnfErBrutto.setBigDecimal(null);
			wnfSkonto.setBigDecimal(null);
			wnfOffen.setBigDecimal(null);
			wnfZahlbetrag.setBigDecimal(null);
			wtfBank.setText(null);
			wtfBIC.setText(null);
			wtfIBAN.setText(null);
		}
	}
	
	private void bankverbindung2Components() throws ExceptionLP, Throwable {
		// Bankverbindung
		ErZahlungsempfaenger erEmpfaenger = erDelegate().getEingangsrechnungBankverbindung(
				new EingangsrechnungId(zvDto.getEingangsrechnungIId()));
		
		if (erEmpfaenger.exists()) {
			PartnerbankDto bv = erEmpfaenger.getPartnerbankDto();
			BankDto bankDto = erEmpfaenger.getBankDto();
			wtfBank.setText(bankDto.getPartnerDto().getCName1nachnamefirmazeile1());
			wtfBIC.setText(bankDto.getCBic());
			wtfIBAN.setText(bv.getCIban());
		} else {
			wtfBank.setText(null);
			wtfBIC.setText(null);
			wtfIBAN.setText(null);
		}
		
		wlaAbweichendeBankverbindung.setText(erEmpfaenger.isAbweichend()
				? LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.abweichendebankverbindung", 
						erEmpfaenger.getPartnerDto().formatFixName2Name1())
				: null);
	}

	private EingangsrechnungDelegate erDelegate() throws Throwable {
		return DelegateFactory.getInstance().getEingangsrechnungDelegate();
	}

	protected void updateGesamtwert() throws Throwable {
		if (tabbedPaneZV.getZVlaufDto() != null) {
			BigDecimal bdWert = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.getGesamtwertEinesZahlungsvorschlaglaufsInMandantenwaehrung(
							tabbedPaneZV.getZVlaufDto().getIId());
			wnfGesamt.setBigDecimal(bdWert);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfZahlbetrag;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (zvDto != null) {
				ZahlungsvorschlagDto savedDto = DelegateFactory.getInstance()
						.getEingangsrechnungDelegate()
						.updateZahlungsvorschlag(zvDto);
				this.zvDto = savedDto;
				setKeyWhenDetailPanel(zvDto.getIId());
				super.eventActionSave(e, true);
				// jetz den anzeigen
				eventYouAreSelected(false);
				// Gesamtwert neu berechnen
				updateGesamtwert();
			}
		}
	}
}
