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
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIBANField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.FinanzDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.Iso20022BankverbindungDto;
import com.lp.server.finanz.service.Iso20022LastschriftDto;
import com.lp.server.finanz.service.Iso20022PaymentsDto;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.Iso20022ZahlungauftragDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.partner.service.BankDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.BankverbindungId;
import com.lp.server.util.Facade;
import com.lp.server.util.KontoId;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
/**
 * <p><I>Panel zur Anzeige der Bankverbindungskopfdaten</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>18. 11. 2004</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class PanelFinanzBankverbindungKopfdaten
    extends PanelBasis {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private TabbedPaneBankverbindung tabbedPaneBankverbindung = null;

  private KontoDtoSmall kontoDto = null;
  private BankDto bankDto = null;
  private PanelQueryFLR panelQueryFLRBank = null;
  private PanelQueryFLR panelQueryFLRKonto = null;
  private static final String ACTION_SPECIAL_BANK =
      "action_special_bank";
  private static final String ACTION_SPECIAL_KONTO =
      "action_special_konto";

  private JPanel jPanelWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private WrapperTextField wtfKontoNummer = new WrapperTextField();
  private WrapperLabel wlaBankkontoNummer = new WrapperLabel();
  private WrapperTextField wtfBankkontoNummer = new WrapperTextField();
  private WrapperTextField wtfBankBezeichnung1 = new WrapperTextField();
  private WrapperLabel wlaBlz = new WrapperLabel();
  private WrapperTextField wtfBlz = new WrapperTextField();
  private WrapperTextField wtfBankBezeichnung2 = new WrapperTextField();
  private WrapperLabel wlaIban = new WrapperLabel();
  private WrapperIBANField wtfIban = null;
  private WrapperButton wbuBank = new WrapperButton();
  private WrapperButton wbuKonto = new WrapperButton();
  private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
  private WrapperLabel wlaAbstand = new WrapperLabel();
  private WrapperLabel wlaBezeichnung = new WrapperLabel();
  private WrapperTextField wtfBezeichnung = new WrapperTextField();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private WrapperLabel wlaButtonBreiteLinks = new WrapperLabel();
  private WrapperLabel wlaButtonBreiteRechts = new WrapperLabel();
  private WrapperLabel wlaBic=new WrapperLabel();
  private WrapperTextField wtfBic=new WrapperTextField();
  private WrapperCheckBox wcbInLiquiditaetsvorschau;
  private WrapperLabel wlaSepaVerzeichnis = new WrapperLabel();
  private WrapperTextField wtfSepaVerzeichnis = new WrapperTextField(
		  FinanzFac.MAX_BANKVERBINDUNG_SEPAVERZEICHNIS);
  private WrapperCheckBox wcbFuerSepaLastschrift;
  private WrapperCheckBox wcbAlsGeldtransitkonto;

  private WrapperLabel wlaIso20022Standard;
  private WrapperComboBox wcoIso20022Standard;
  private WrapperLabel wlaIso20022Zahlungsauftrag;
  private WrapperComboBox wcoIso20022Zahlungsauftrag;
  private WrapperLabel wlaIso20022Lastschrift;
  private WrapperComboBox wcoIso20022Lastschrift;
  private Map<Iso20022StandardEnum, Iso20022PaymentsDto> isoPaymentsMap;
  private Iso20022BankverbindungDto iso20022BankverbindungDto;
  private String waehrungKonto;
  private WrapperLabel wlaKontowaehrung;
  private WrapperTextField wtfKontowaehrung;

  private WrapperLabel wlaStellenKontoauszug = new WrapperLabel();
  private WrapperNumberField wnfStellenKontoauszug = new WrapperNumberField();
  
  public PanelFinanzBankverbindungKopfdaten(InternalFrame internalFrame,
                                            String add2TitleI, Object key,
                                            TabbedPaneBankverbindung
                                            tabbedPaneBankverbindung) throws Throwable {
    super(internalFrame, add2TitleI, key);
    this.tabbedPaneBankverbindung = tabbedPaneBankverbindung;
    jbInit();
    setDefaults();
    initComponents();
  }


  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit() throws Throwable {

    JPanel panelButtonAction = getToolsPanel();
    getInternalFrame().addItemChangedListener(this);
    jPanelWorkingOn.setLayout(gridBagLayout1);
    
    wcbInLiquiditaetsvorschau = new WrapperCheckBox(LPMain.getTextRespectUISPr("fb.inliquiditaetsvorschau"));
    
    wlaStellenKontoauszug.setText(LPMain.getTextRespectUISPr("fb.stellen.kontoauszugsnummer"));
    wnfStellenKontoauszug.setFractionDigits(0);
    
    wtfKontoNummer.setMandatoryField(true);
    wtfKontoNummer.setActivatable(false);
    wlaBankkontoNummer.setText(LPMain.getTextRespectUISPr("lp.kontonr"));
    wtfBankkontoNummer.setColumnsMax(FinanzFac.MAX_BANKVERBINDUNG_BANKKONTONUMMER);
    wtfBankkontoNummer.setActivatable(true);
    wtfBankBezeichnung1.setMandatoryField(true);
    wtfBankBezeichnung1.setActivatable(false);
    wlaBic.setText(LPMain.getTextRespectUISPr("lp.bic"));
    wtfBic.setActivatable(false);
    wlaBlz.setText(LPMain.getTextRespectUISPr("lp.blz"));
    wtfBlz.setActivatable(false);
    wtfBankBezeichnung2.setActivatable(false);
    wlaIban.setText(LPMain.getTextRespectUISPr("lp.iban"));
    
    wtfIban = new WrapperIBANField();
    wtfIban.setActivatable(true);
    wbuBank.setText(LPMain.getTextRespectUISPr("button.bank"));
    wbuBank.setToolTipText(LPMain.getTextRespectUISPr("button.bank.tooltip"));
    wbuBank.addActionListener(this);
    wbuBank.setActionCommand(ACTION_SPECIAL_BANK);
    wbuKonto.setText(LPMain.getTextRespectUISPr("button.sachkonto"));
    wbuKonto.setToolTipText(LPMain.getTextRespectUISPr("button.sachkonto.tooltip"));
    wbuKonto.addActionListener(this);
    wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
    wtfKontoBezeichnung.setActivatable(false);
    wtfKontoBezeichnung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
    wlaAbstand.setText("  ");
    wlaBezeichnung.setRequestFocusEnabled(true);
    wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
    wtfBezeichnung.setColumnsMax(FinanzFac.MAX_BANKVERBINDUNG_BEZEICHNUNG);
    wtfBezeichnung.setActivatable(true);
    wlaSepaVerzeichnis.setText(LPMain.getTextRespectUISPr("fb.sepa.sepaverzeichnis"));
    wtfSepaVerzeichnis.setActivatable(true);
    wcbFuerSepaLastschrift = new WrapperCheckBox(LPMain.getTextRespectUISPr("fb.fuersepalastschrift"));
    wcbAlsGeldtransitkonto = new WrapperCheckBox(LPMain.getTextRespectUISPr("fb.alsgeldtransitkonto"));
    
    wlaIso20022Standard = new WrapperLabel();
    wlaIso20022Standard.setText(LPMain.getTextRespectUISPr("fb.iso20022.standard"));
    wcoIso20022Standard = new WrapperComboBox();
    wcoIso20022Standard.setMandatoryFieldDB(false);
	Map<Iso20022StandardEnum, String> hmStandards = new HashMap<Iso20022StandardEnum, String>();
	for (Iso20022PaymentsDto paymentsDto : getIsoPaymentsMap().values()) {
		hmStandards.put(paymentsDto.getStandardDto().getStandardEnum(), paymentsDto.getStandardDto().getCBez());
	}
	wcoIso20022Standard.setMap(hmStandards);
	wcoIso20022Standard.addActionListener(this);
    
    wlaIso20022Zahlungsauftrag = new WrapperLabel();
    wlaIso20022Zahlungsauftrag.setText(LPMain.getTextRespectUISPr("fb.iso20022.zahlungsauftrag"));
    wcoIso20022Zahlungsauftrag = new WrapperComboBox();
    wcoIso20022Zahlungsauftrag.setMandatoryFieldDB(false);
    
    wlaIso20022Lastschrift = new WrapperLabel();
    wlaIso20022Lastschrift.setText(LPMain.getTextRespectUISPr("fb.iso20022.lastschrift"));
    wcoIso20022Lastschrift = new WrapperComboBox();
    wcoIso20022Lastschrift.setMandatoryFieldDB(false);
    
    wlaKontowaehrung = new WrapperLabel();
    wlaKontowaehrung.setText(LPMain.getTextRespectUISPr("fb.bv.kontowaehrung"));
    wtfKontowaehrung = new WrapperTextField();
    wtfKontowaehrung.setActivatable(false);
    
    this.setLayout(gridBagLayout2);
    wlaButtonBreiteLinks.setMinimumSize(new Dimension(100, 23));
    wlaButtonBreiteLinks.setPreferredSize(new Dimension(100, 23));
    wlaButtonBreiteRechts.setMinimumSize(new Dimension(70, 23));
    wlaButtonBreiteRechts.setPreferredSize(new Dimension(70, 23));
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    
    HvLayout layoutWorkingOn = HvLayoutFactory.create(jPanelWorkingOn,
    		"wrap 4", "[20%,fill|10%,fill|20%,fill|fill,grow]", "");
    layoutWorkingOn.add(wbuBank).add(wtfBankBezeichnung1).span(3)
    	.add(wtfBankBezeichnung2).skipAndSpan(1, 3)
    	.add(wlaBlz).add(wtfBlz).span(3)
    	.add(wlaBic).add(wtfBic).span(3)
    	.add(wlaBankkontoNummer).add(wtfBankkontoNummer).add(wlaAbstand).span(2)
    	.add(wlaIban).add(wtfIban).span(3)
    	.add(wlaBezeichnung).add(wtfBezeichnung).span(3)
    	.add(wbuKonto).add(wtfKontoNummer).add(wtfKontoBezeichnung).span(2)
    	.add(wlaKontowaehrung).add(wtfKontowaehrung).wrap()
    	.add(wcbInLiquiditaetsvorschau, "skip 1, span 2, wrap")
    	.add(wlaStellenKontoauszug).add(wnfStellenKontoauszug).wrap();
    
    if (!hasZusatzfunktionSepa() && !hasZusatzfunktionSepaLastschrift()) {
    	return;
    }
    
    JPanel panelIso20022 = new JPanel();
    panelIso20022.setBorder(BorderFactory.createTitledBorder("Iso20022 Zahlungen"));
    HvLayout layoutIso20022 = HvLayoutFactory.create(panelIso20022, 
    		"wrap 3, ins 0 0 0 0", "[20%,fill|30%,fill|fill,grow]", "[|||]");
    layoutIso20022
    	.add(wlaIso20022Standard).add(wcoIso20022Standard).wrap()
    	.add(wlaSepaVerzeichnis).add(wtfSepaVerzeichnis).span(2);
    layoutWorkingOn.add(panelIso20022, "span 4, gaptop 20");
    	
    if (hasZusatzfunktionZahlungsvorschlag()) {
    	layoutIso20022.add(wlaIso20022Zahlungsauftrag).add(wcoIso20022Zahlungsauftrag).wrap();
    }
    
    if (hasZusatzfunktionSepaLastschrift()) {
    	layoutIso20022.add(wlaIso20022Lastschrift).add(wcoIso20022Lastschrift).wrap();
    }

  }

  /**
   * Die Daten der Bankverbindung ins Panel schreiben
   *
   * @throws Throwable
   */
  private void dto2Components() throws Throwable {
    BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
        getBankverbindungDto();
    if (bankverbindungDto != null) {
      wtfBankkontoNummer.setText(bankverbindungDto.getCKontonummer());
      wtfIban.setIban(bankverbindungDto.getCIban());
      wtfBezeichnung.setText(bankverbindungDto.getCBez());
      wcbInLiquiditaetsvorschau.setSelected(bankverbindungDto.isbInLiquiditaetsVorschau());
      wnfStellenKontoauszug.setInteger(bankverbindungDto.getIStellenAuszugsnummer());
      wtfSepaVerzeichnis.setText(bankverbindungDto.getCSepaVerzeichnis());
      wcbFuerSepaLastschrift.setSelected(bankverbindungDto.isbFuerSepaLastschrift());
      wcbAlsGeldtransitkonto.setSelected(bankverbindungDto.isbAlsGeldtransitkonto());
      setStatusbarModification(bankverbindungDto) ;

      // Konto und Bank holen
      kontoDto = finanzDelegate().kontoFindByPrimaryKeySmall(
          bankverbindungDto.getKontoIId());
      waehrungKonto = finanzDelegate().getWaehrungOfKonto(new KontoId(bankverbindungDto.getKontoIId()));
      bankDto = DelegateFactory.getInstance().getPartnerbankDelegate().bankFindByPrimaryKey(
          bankverbindungDto.getBankIId());
      
      dto2ComponentsIso20022();
    }
    dto2ComponentsKonto();
    dto2ComponentsBank();
  }
  
  private void dto2ComponentsIso20022() throws ExceptionLP {
	  if (!hasZusatzfunktionSepa()) {
		  return;
	  }
	  
      BankverbindungId bankverbindungId = new BankverbindungId(getTabbedPaneBankverbindung().getBankverbindungDto().getIId());
      iso20022BankverbindungDto = finanzDelegate().iso20022BankverbindungFindByBankverbindungIId(bankverbindungId);
      if (iso20022BankverbindungDto != null) {
    	  wcoIso20022Standard.setKeyOfSelectedItem(iso20022BankverbindungDto.getStandardEnum());
      } else {
    	  iso20022BankverbindungDto = new Iso20022BankverbindungDto();
    	  iso20022BankverbindungDto.setBankverbindungIId(bankverbindungId.id());
    	  wcoIso20022Standard.setKeyOfSelectedItem(null);
    	  return;
      }
      
      reloadIso20022Combos();
      wcoIso20022Zahlungsauftrag.setKeyOfSelectedItem(iso20022BankverbindungDto.getZahlungsauftragSchemaIId());
      wcoIso20022Lastschrift.setKeyOfSelectedItem(iso20022BankverbindungDto.getLastschriftSchemaIId());
  }
  
  private void reloadIso20022Combos() throws ExceptionLP {
	  if (wcoIso20022Standard.isSelectedItemEmpty()) {
		  return;
	  }
	  
      Iso20022PaymentsDto paymentsDto = getIsoPaymentsMap().get(wcoIso20022Standard.getKeyOfSelectedItem());
      Map<Integer, String> hmZahlungsauftrag = new HashMap<Integer, String>();
      for (Iso20022ZahlungauftragDto zaDto : paymentsDto.getZahlungsauftragDtos()) {
    	  hmZahlungsauftrag.put(zaDto.getIId(), zaDto.getSchemaDto().getCBez());
	  }
      wcoIso20022Zahlungsauftrag.setMap(hmZahlungsauftrag);

      Map<Integer, String> hmLastschrift = new HashMap<Integer, String>();
      for (Iso20022LastschriftDto laDto : paymentsDto.getLastschriftDtos()) {
    	  hmLastschrift.put(laDto.getIId(), laDto.getSchemaDto().getCBez());
	  }
   	  wcoIso20022Lastschrift.setActivatable(!paymentsDto.getLastschriftDtos().isEmpty());
      wcoIso20022Lastschrift.setMap(hmLastschrift);
  }

  private void components2Dto()
      throws Throwable {
    BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
        getBankverbindungDto();
    // neue Bankverbindung: neues Dto  ;-)
    if (bankverbindungDto == null) {
      bankverbindungDto = new BankverbindungDto();
      bankverbindungDto.setMandantCNr(LPMain.getInstance().getTheClient().
                                      getMandant());
    }
    if (kontoDto != null) {
      bankverbindungDto.setKontoIId(kontoDto.getIId());
    }
    if (bankDto != null) {
      bankverbindungDto.setBankIId(bankDto.getPartnerIId());
    }
    bankverbindungDto.setCBez(wtfBezeichnung.getText());
    bankverbindungDto.setCIban(wtfIban.getIban());
    bankverbindungDto.setCKontonummer(wtfBankkontoNummer.getText());
    bankverbindungDto.setbInLiquiditaetsVorschau(wcbInLiquiditaetsvorschau.isSelected());
    bankverbindungDto.setCSepaVerzeichnis(wcoIso20022Standard.isSelectedItemEmpty()
    		? null : wtfSepaVerzeichnis.getText());
    bankverbindungDto.setbFuerSepaLastschrift(!wcoIso20022Lastschrift.isSelectedItemEmpty());
    bankverbindungDto.setbAlsGeldtransitkonto(wcbAlsGeldtransitkonto.isSelected());
    
    bankverbindungDto.setIStellenAuszugsnummer(wnfStellenKontoauszug.getInteger());
    
    getTabbedPaneBankverbindung().setBankverbindungDto(bankverbindungDto);
  }

  private void components2DtoIso20022() {
	  if (!hasZusatzfunktionSepa()) {
		  return;
	  }
	  
	  if (iso20022BankverbindungDto == null) {
		  iso20022BankverbindungDto = new Iso20022BankverbindungDto();
	  }
	  iso20022BankverbindungDto.setBankverbindungIId(getTabbedPaneBankverbindung().getBankverbindungDto().getIId());
	  if (wcoIso20022Standard.isSelectedItemEmpty()) {
		  iso20022BankverbindungDto.setZahlungsauftragSchemaIId(null);
		  iso20022BankverbindungDto.setLastschriftSchemaIId(null);
		  return;
	  }
	  
	  iso20022BankverbindungDto.setZahlungsauftragSchemaIId((Integer)wcoIso20022Zahlungsauftrag.getKeyOfSelectedItem());
	  iso20022BankverbindungDto.setLastschriftSchemaIId(
			  wcoIso20022Lastschrift.isSelectedItemEmpty() ? null : (Integer)wcoIso20022Lastschrift.getKeyOfSelectedItem());
  }

  /**
   * Loesche das Konto.
   *
   * @param e ActionEvent
   * @param bAdministrateLockKeyI boolean
   * @param bNeedNoDeleteI boolean
   * @throws Throwable
   */
  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
        getBankverbindungDto();
    if (bankverbindungDto != null) {
      finanzDelegate().removeBankverbindung(bankverbindungDto);
      super.eventActionDelete(e, true, true);
    }
  }

  /**
   * Speichere Daten des Panels
   *
   * @param e ActionEvent
   * @param bNeedNoSaveI boolean
   * @throws Throwable
   */
  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (!pruefeIban()) {
    	  return;
      }
      BankverbindungDto bankverbindungDto = finanzDelegate().updateBankverbindung(getTabbedPaneBankverbindung().
          getBankverbindungDto());
      this.setKeyWhenDetailPanel(bankverbindungDto.getIId());
      getTabbedPaneBankverbindung().setBankverbindungDto(bankverbindungDto);
      
	  if (hasZusatzfunktionSepa()) {
	      components2DtoIso20022();
	      boolean isSelected = iso20022BankverbindungDto.hasLastschriftSchema() || iso20022BankverbindungDto.hasZahlungsauftragSchema();
	      if (iso20022BankverbindungDto.getIId() == null && isSelected) {
	    	  finanzDelegate().createIso20022Bankverbindung(iso20022BankverbindungDto);
	      } else if (iso20022BankverbindungDto.getIId() != null && isSelected) {
	    	  finanzDelegate().updateIso20022Bankverbindung(iso20022BankverbindungDto);
	      } else if (iso20022BankverbindungDto.getIId() != null && !isSelected) {
	    	  finanzDelegate().removeIso20022Bankverbindung(iso20022BankverbindungDto.getIId());
	      }
	  }
      
      super.eventActionSave(e, true);
      eventYouAreSelected(false);
    }
  }

  private boolean pruefeIban() {
      if (!wtfIban.pruefeIBAN()) {
    	  return false;
      }

      if (!hasZusatzfunktionSepa() && !hasZusatzfunktionSepaLastschrift()) {
    	  return true;
      }
      
      if (!wcbFuerSepaLastschrift.isSelected() 
			  && Helper.isStringEmpty(wtfSepaVerzeichnis.getText())) {
		  // wenn nicht fuer Sepa genutzt, dann egal
		  return true;
	  }
	  
	  if (!Helper.isStringEmpty(wtfIban.getIban())) {
		  return true;
	  }
	  
	  DialogFactory.showModalDialog(
			  LPMain.getTextRespectUISPr("lp.error"),
			  LPMain.getTextRespectUISPr("finanz.bankverbindung.error.fuersepa.keineiban"));
	  return false;
  }

protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
      dialogQueryKonto(e);
    }
    if (e.getActionCommand().equals(ACTION_SPECIAL_BANK)) {
      dialogQueryBank(e);
    }
    
    if (e.getSource() == wcoIso20022Standard) {
    	if (wcoIso20022Standard.isSelectedItemEmpty()) {
    		enableIso20022Nachrichten(false);
    	} else {
    		enableIso20022Nachrichten(true);
    	}
    	reloadIso20022Combos();
    }
  }

  protected void eventItemchanged(EventObject eI) throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRKonto) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        kontoDto = finanzDelegate().
            kontoFindByPrimaryKeySmall( (Integer) key);
        waehrungKonto = finanzDelegate().getWaehrungOfKonto(new KontoId(kontoDto.getIId()));

        dto2ComponentsKonto();
      }
      else if (e.getSource() == panelQueryFLRBank) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        bankDto = DelegateFactory.getInstance().getPartnerbankDelegate().bankFindByPrimaryKey( (
            Integer) key);
        dto2ComponentsBank();
      }
    }
  }

  void dialogQueryKonto(ActionEvent e)
      throws Throwable {
	  
	  Integer kontoIId=null;
	  if (kontoDto != null) {
		  kontoIId=kontoDto.getIId();
	    }
	  
	  panelQueryFLRKonto= FinanzFilterFactory.getInstance().createPanelFLRSachKonto(getInternalFrame(),
			  kontoIId, false);
	      new DialogQuery(panelQueryFLRKonto);
  }

  void dialogQueryBank(ActionEvent e)
      throws Throwable {
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_REFRESH,
        };
    QueryType[] qt = null;
    FilterKriterium[] filters = null;
    panelQueryFLRBank = new PanelQueryFLR(
        qt,
        filters,
        QueryParameters.UC_ID_BANK,
        aWhichButtonIUse,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr("part.kund.banken"));
    panelQueryFLRBank.befuellePanelFilterkriterienDirekt(
        PartnerFilterFactory.getInstance().createFKDBankName(),
        PartnerFilterFactory.getInstance().createFKDBankBLZ());
    if(bankDto !=null) {
      panelQueryFLRBank.setSelectedId(bankDto.getPartnerIId());
    }
    new DialogQuery(panelQueryFLRBank);
  }


  /**
   * Neu
   *
   * @param eventObject ActionEvent
   * @param bLockMeI boolean
   * @param bNeedNoNewI boolean
   * @throws Throwable
   */
  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI) throws Throwable {
    super.eventActionNew(eventObject, true, false);
    leereAlleFelder(this);

    getTabbedPaneBankverbindung().setBankverbindungDto(null);
    kontoDto = null;
    bankDto = null;
    waehrungKonto = null;
  }

  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
    super.eventYouAreSelected(false);
    if (!bNeedNoYouAreSelectedI) {
      BankverbindungDto bankverbindungDto = getTabbedPaneBankverbindung().
          getBankverbindungDto();
      if (bankverbindungDto != null) {
        Object key = bankverbindungDto.getIId();
        //muss: alter kunde; einlesen; wenn bereits geloescht: exception nach oben.
        getTabbedPaneBankverbindung().setBankverbindungDto(finanzDelegate().bankverbindungFindByPrimaryKey( (Integer) key));
        dto2Components();
      }
    }
  }
  /**
   * Daten des ausgewaehlten Kontos ins Panel schreiben
   */
  private void dto2ComponentsKonto() {
    if (kontoDto != null) {
      this.wtfKontoNummer.setText(kontoDto.getCNr());
      this.wtfKontoBezeichnung.setText(kontoDto.getCBez());
      wtfKontowaehrung.setText(waehrungKonto);
    }
  }

  /**
   * Daten der ausgewaehlten Bank ins Panel schreiben
   */
  private void dto2ComponentsBank() {
    if (bankDto != null) {
      this.wtfBankBezeichnung1.setText(bankDto.getPartnerDto().getCName1nachnamefirmazeile1());
      this.wtfBankBezeichnung2.setText(bankDto.getPartnerDto().getCName2vornamefirmazeile2());
      this.wtfBlz.setText(bankDto.getCBlz());
      this.wtfBic.setText(bankDto.getCBic());
    }
  }

  private void setDefaults()
      throws Throwable {
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD};
    enableToolsPanelButtons(aWhichButtonIUse);
    
  }

  /**
   * Drucken des Kontoblattes
   *
   * @param e ActionEvent
   * @throws Throwable
   */
  protected void eventActionPrint(ActionEvent e) throws Throwable {
    this.tabbedPaneBankverbindung.printKontoblatt();
  }

  public String getLockMeWer() {
    return HelperClient.LOCKME_FINANZ_BANKVERBINDUNG;
  }

  private TabbedPaneBankverbindung getTabbedPaneBankverbindung() {
    return tabbedPaneBankverbindung;
  }

  public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
    if (exfc.getICode() == EJBExceptionLP.FEHLER_FINANZ_BANKVERBINDUNG_KONTO_SCHON_ZUGEWIESEN) {
      DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
          "lp.error"),
          LPMain.getInstance().getTextRespectUISPr("finanz.error.bankverbindungkonto"));
      return true;
    }
    else {
      return false;
    }
  }


  protected javax.swing.JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuBank;
  }
  
  private boolean hasZusatzfunktionSepa() {
	  return LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SEPA);
  }
  
  private boolean hasZusatzfunktionSepaLastschrift() {
	  return LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SEPA_LASTSCHRIFT);
  }
  
  private boolean hasZusatzfunktionZahlungsvorschlag() {
	  return LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZAHLUNGSVORSCHLAG);
  }
  
  private void loadMapIsoPayments() throws ExceptionLP {
	  isoPaymentsMap = finanzDelegate().getMapOfIso20022Payments();
  }

  public Map<Iso20022StandardEnum, Iso20022PaymentsDto> getIsoPaymentsMap() throws ExceptionLP {
	  if (isoPaymentsMap == null) {
		  loadMapIsoPayments();
	  }
	  return isoPaymentsMap;
  }
  
	private FinanzDelegate finanzDelegate() throws ExceptionLP {
		return DelegateFactory.getInstance().getFinanzDelegate();
	}
	
	private void enableIso20022Nachrichten(boolean enabled) {
		wlaIso20022Zahlungsauftrag.setVisible(enabled);
		wcoIso20022Zahlungsauftrag.setVisible(enabled);
		wcoIso20022Zahlungsauftrag.setMandatoryField(enabled);
		
		wlaIso20022Lastschrift.setVisible(enabled);
		wlaIso20022Lastschrift.setVisible(enabled);
		wcoIso20022Lastschrift.setVisible(enabled);
		
		wlaSepaVerzeichnis.setVisible(enabled);
		wtfSepaVerzeichnis.setVisible(enabled);
	}
}
