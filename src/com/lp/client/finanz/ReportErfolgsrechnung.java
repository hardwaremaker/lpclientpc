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
import java.math.BigDecimal;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperComboBoxPeriode;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.IPanelReportAction;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.ReportErfolgsrechnungKriterienDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck der Saldenliste
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 15.06.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/04/13 14:19:03 $
 */
public class ReportErfolgsrechnung extends PanelBasis implements
		PanelReportIfJRDS,IPanelReportAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Border border1;

	private WrapperLabel wlaGeschaeftsjahr = null;
	private WrapperComboBox wcoGeschaeftsjahr = null;
	private WrapperLabel wlaPeriode = null;
	private WrapperComboBoxPeriode wcoPeriode = null;

	private WrapperCheckBox wcbDetail = new WrapperCheckBox();

	private Map<String, Object> mapParameters = null;
	
	private final static int BREITE_SPALTE2 = 80;

	protected JPanel jpaWorkingOn = new JPanel();

	public ReportErfolgsrechnung(InternalFrame internalFrame,
			String sAdd2Title, String geschaeftsjahr) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom
		// Framework hergenommen
		super(internalFrame, sAdd2Title);
		jbInit();
		setDefaults();
		initPanel();
		initComponents();
		// this.setVisible(false);
	}

	protected void jbInit() throws Throwable {
		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.setLayout(new GridBagLayout());
		jpaWorkingOn.setBorder(border1);

		wlaGeschaeftsjahr = new WrapperLabel("Gesch\u00E4ftsjahr");
		wcoGeschaeftsjahr = new WrapperComboBox();
		wcoGeschaeftsjahr.addActionListener(this);

		wlaPeriode = new WrapperLabel("Periode");
		wcoPeriode = new WrapperComboBoxPeriode(DelegateFactory.getInstance()
				.getParameterDelegate().getGeschaeftsjahr().toString(),
				FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT, true);

		wcbDetail.setText(LPMain
				.getTextRespectUISPr("fb.erfolgsrechnung.detail"));

		Dimension radioButtonDimension = HelperClient.getSizeFactoredDimension(BREITE_SPALTE2);
		HelperClient.setMinimumAndPreferredSize(wcoGeschaeftsjahr, radioButtonDimension);
		HelperClient.setMinimumAndPreferredSize(wcbDetail, radioButtonDimension);
		HelperClient.setMinimumAndPreferredSize(wlaGeschaeftsjahr, radioButtonDimension);
		HelperClient.setMinimumAndPreferredSize(wlaPeriode, radioButtonDimension);
		
		
		iZeile++;
		jpaWorkingOn.add(wlaGeschaeftsjahr, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoGeschaeftsjahr, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbDetail, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaPeriode, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoPeriode, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		// this.setEinschraenkungDatumBelegnummerSichtbar(false);
	}

	private void initPanel() {
		wlaGeschaeftsjahr.setVisible(true);
		wcoGeschaeftsjahr.setVisible(true);
		wlaPeriode.setVisible(true);
		wcoPeriode.setVisible(true);
	}

	private void setDefaults() {
		try {
			wcoGeschaeftsjahr.setMap(DelegateFactory.getInstance()
					.getSystemDelegate().getAllGeschaeftsjahr());
			wcoGeschaeftsjahr.setKeyOfSelectedItem(DelegateFactory
					.getInstance().getParameterDelegate().getGeschaeftsjahr());
		} catch (ExceptionLP e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FinanzReportFac.REPORT_ERFOLGSRECHNUNG;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP print = DelegateFactory
				.getInstance()
				.getFinanzReportDelegate()
				.printErfolgsrechnung(this.getKriterien(), false,
						wcbDetail.isSelected(), false);
		this.mapParameters = print.getMapParameters();
		return print;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	protected ReportErfolgsrechnungKriterienDto getKriterien() {
		ReportErfolgsrechnungKriterienDto krit = new ReportErfolgsrechnungKriterienDto();

		krit.setIGeschaeftsjahr(Integer.parseInt(wcoGeschaeftsjahr
				.getSelectedItem().toString()));

		krit.setIPeriode(wcoPeriode.getPeriode());

		return krit;
	}

	@Override
	public void eventActionPrint(ActionEvent e) throws Throwable {
		eventActionSave(e, false) ;
	}

	  
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if(e.getSource().equals(wcoGeschaeftsjahr)) {
			if(wcoGeschaeftsjahr
					.getSelectedItem()!=null && wcoGeschaeftsjahr
					.getSelectedItem()!=wcoGeschaeftsjahr.emptyEntry)
			wcoPeriode.setGeschaeftsjahr(wcoGeschaeftsjahr
					.getSelectedItem().toString(),
				FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT, true);
		}
	}
	
	@Override
	public void eventActionSave(ActionEvent e) throws Throwable {
		eventActionSave(e, false) ;
	}

	@Override
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		// jetzt wenn erforderlich die Jahresgewinn-Buchung machen
		GeschaeftsjahrMandantDto gj = DelegateFactory.getInstance().getSystemDelegate().geschaeftsjahrFindByPrimaryKey(this.getKriterien().getIGeschaeftsjahr());
		if (gj.getTSperre() == null) {
			// SP7774 nur wenn Jahr nicht gesperrt
			System.out.println("Jahresgewinn-Buchung");
			System.out.println(" Konten: " + this.mapParameters.get("P_KONTO_I_ID_GEWINNVORTRAG") + " " 
					+ this.mapParameters.get("P_KONTO_I_ID_JAHRESGEWINN"));
			System.out.println(" Saldo: " + this.mapParameters.get("P_JAHRESGEWINN_PERIODE"));
		
			if (this.mapParameters.get("P_KONTO_I_ID_GEWINNVORTRAG") != null && this.mapParameters.get("P_KONTO_I_ID_JAHRESGEWINN") != null) {
				DelegateFactory.getInstance().getFinanzServiceDelegate().createGewinnbuchungen(
					(int)this.mapParameters.get("P_GESCHAEFTSJAHR"), 
					(int)this.mapParameters.get("P_PERIODE"),  
					(BigDecimal)this.mapParameters.get("P_JAHRESGEWINN_PERIODE"));
			}
		}
	}

}
