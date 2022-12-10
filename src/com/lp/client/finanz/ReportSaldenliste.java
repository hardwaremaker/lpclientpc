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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperComboBoxPeriode;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.ReportSaldenlisteKriterienDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


/**
 * <p> Diese Klasse kuemmert sich um den Druck der Saldenliste</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 15.06.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/01/02 12:17:57 $
 */
public class ReportSaldenliste extends PanelBasis implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Border border1;

	private WrapperLabel wlaGeschaeftsjahr = null;
	private WrapperComboBox wcoGeschaeftsjahr = null;
	private WrapperTextField wtfGeschaeftsjahr = null;
	private WrapperLabel wlaPeriode = null;
	private WrapperComboBoxPeriode wcoPeriode = null;
	private WrapperRadioButton wrbSachkonten = null;
	private WrapperRadioButton wrbDebitoren = null;
	private WrapperRadioButton wrbKreditoren = null;
	private ButtonGroup jbgKontotyp = null;

	private String geschaeftsjahr = "";
	
	private final static int BREITE_SPALTE2 = 90;

	protected JPanel jpaWorkingOn = new JPanel();

	
public ReportSaldenliste(InternalFrame internalFrame, String sAdd2Title, String geschaeftsjahr) throws Throwable {
    // reporttitel: das PanelReport kriegt einen Titel, der wird vom Framework hergenommen
    super(internalFrame, sAdd2Title);
    this.geschaeftsjahr = geschaeftsjahr;
    jbInit();
    setDefaults();
    initPanel();
    initComponents();
//    this.setVisible(false);
  }

protected void jbInit()
throws Throwable {
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(new GridBagLayout());
    this.add(jpaWorkingOn,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.setLayout(new GridBagLayout());
    jpaWorkingOn.setBorder(border1);

	wlaGeschaeftsjahr = new WrapperLabel("Gesch\u00E4ftsjahr");
	wcoGeschaeftsjahr = new WrapperComboBox();
	wtfGeschaeftsjahr = new WrapperTextField();
	wtfGeschaeftsjahr.setEditable(false);
	
    wlaPeriode = new WrapperLabel("Periode");
	wcoPeriode = new WrapperComboBoxPeriode(geschaeftsjahr, FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT, true);
	
	wrbSachkonten = new WrapperRadioButton("Sachkonten");
	wrbDebitoren = new WrapperRadioButton("Debitoren");
	wrbKreditoren = new WrapperRadioButton("Kreditoren");

	
	Dimension radioButtonDimension = HelperClient.getSizeFactoredDimension(BREITE_SPALTE2);
	HelperClient.setMinimumAndPreferredSize(wrbSachkonten, radioButtonDimension);
	HelperClient.setMinimumAndPreferredSize(wrbKreditoren, radioButtonDimension);
	HelperClient.setMinimumAndPreferredSize(wrbDebitoren, radioButtonDimension);
	HelperClient.setMinimumAndPreferredSize(wlaGeschaeftsjahr, radioButtonDimension);
	HelperClient.setMinimumAndPreferredSize(wlaPeriode, radioButtonDimension);


	jbgKontotyp = new ButtonGroup();
	jbgKontotyp.add(wrbSachkonten);
	jbgKontotyp.add(wrbDebitoren);
	jbgKontotyp.add(wrbKreditoren);

	iZeile++;
	jpaWorkingOn.add(wlaGeschaeftsjahr,
			new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	jpaWorkingOn.add(wtfGeschaeftsjahr,
			new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	iZeile++;
	jpaWorkingOn.add(wlaPeriode,
			new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	jpaWorkingOn.add(wcoPeriode,
			new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	iZeile++;
	jpaWorkingOn.add(wrbSachkonten,
			new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	jpaWorkingOn.add(wrbDebitoren,
			new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	jpaWorkingOn.add(wrbKreditoren,
			new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	iZeile++;

//	this.setEinschraenkungDatumBelegnummerSichtbar(false);
}

private void initPanel() {
    wlaGeschaeftsjahr.setVisible(true);
    wcoGeschaeftsjahr.setVisible(false);
    wtfGeschaeftsjahr.setVisible(true);
	wlaPeriode.setVisible(true);
    wcoPeriode.setVisible(true);
    wrbSachkonten.setVisible(true);
    wrbDebitoren.setVisible(true);
    wrbKreditoren.setVisible(true);
}

private void setDefaults() {
    try {
		//wcoGeschaeftsjahr.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr());
	    //wcoGeschaeftsjahr.setKeyOfSelectedItem(DelegateFactory.getInstance().getParameterDelegate().
        //        getGeschaeftsjahr());
    	wtfGeschaeftsjahr.setText(geschaeftsjahr);
	} catch (Throwable e) {
		e.printStackTrace();
	}
	wrbSachkonten.setSelected(true);
}


  public String getModul() {
    return FinanzReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return FinanzReportFac.REPORT_SALDENLISTE;
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public JasperPrintLP getReport(String sDrucktype)      throws Throwable {
    return DelegateFactory.getInstance().getFinanzReportDelegate().printSaldenliste(this.getKriterien());
  }


  public MailtextDto getMailtextDto() throws Throwable  {
    MailtextDto mailtextDto=PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
  }

  protected ReportSaldenlisteKriterienDto getKriterien() {
	    ReportSaldenlisteKriterienDto krit = new ReportSaldenlisteKriterienDto();
	    
	    krit.setIGeschaeftsjahr(Integer.parseInt(wtfGeschaeftsjahr.getText()));//wcoGeschaeftsjahr.getSelectedItem().toString()));

	    krit.setIPeriode(wcoPeriode.getPeriode());
	    
	    if (wrbSachkonten.isSelected())
	    	krit.setKontotypCNr(FinanzServiceFac.KONTOTYP_SACHKONTO);
	    else if (wrbDebitoren.isSelected())
	    	krit.setKontotypCNr(FinanzServiceFac.KONTOTYP_DEBITOR);
	    else if (wrbKreditoren.isSelected())
	    	krit.setKontotypCNr(FinanzServiceFac.KONTOTYP_KREDITOR);
	    
	    return krit;
	  }

}
