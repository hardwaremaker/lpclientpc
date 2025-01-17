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
package com.lp.client.lieferschein;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.report.JasperPrintLP;

/*
 * <p>Report Lieferschein Adressetikett. <p>Copyright Logistik Pur Software GmbH
 * (c) 2004-2008</p> <p>Erstellungsdatum 28.09.05</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.1 $
 */
public class ReportVersandetiketten extends ReportEtikett implements
		PanelReportIfJRDS {
	private static final long serialVersionUID = 1L;
	private LieferscheinDto lieferscheinDto = null;

	private WrapperLabel wlaPakete = null;
	private WrapperNumberField wnfPakete = null;
//	private WrapperLabel wlaLiefertermin = null;
//	private WrapperDateField wdfLiefertermin = null;
	private WrapperLabel wlaGewicht = null;
	private WrapperNumberField wnfGewicht = null;
	private WrapperLabel wlaEinheit = null;
	private WrapperLabel wlaVersandnummer = null;
	private WrapperTextField wtfVersandnummer = null;

	private WrapperLabel wlaVersandnummer2 = null;
	private WrapperTextField wtfVersandnummer2 = null;

	private PanelAuslieferdatum panelAuslieferdatum = null ;
	
	public ReportVersandetiketten(InternalFrame internalFrame,
			LieferscheinDto lieferscheinDto, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);
		this.lieferscheinDto = lieferscheinDto;
		jbInit();
		setDefaults();
	}

	private void setDefaults() {
	}

	public String getModul() {
		return LieferscheinReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_VERSANDETIKETTEN;

	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getLieferscheinReportDelegate()
				.printVersandetikett(lieferscheinDto.getIId());
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	private void jbInit() throws Throwable {
		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };

		wlaPakete = new WrapperLabel(LPMain.getTextRespectUISPr(
				"ls.pakete"));
		wnfPakete = new WrapperNumberField();
		wnfPakete.setFractionDigits(0);
		wnfPakete.setMinimumValue(0);

		wnfPakete.setInteger(DelegateFactory.getInstance()
				.getLieferscheinReportDelegate()
				.getGesamtzahlPakete(lieferscheinDto.getIId()));
		wnfPakete.setActivatable(false);

//		wlaLiefertermin = new WrapperLabel(LPMain
//		.getTextRespectUISPr("lp.auslieferdatum"));
//		wdfLiefertermin = new WrapperDateField();
//		wdfLiefertermin.setMandatoryFieldDB(false);
//		wdfLiefertermin.setShowRubber(false);

		panelAuslieferdatum = new PanelAuslieferdatum(getInternalFrame(), "") ;
		
		wlaGewicht = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.gewicht"));
		wnfGewicht = new WrapperNumberField();
		wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_KILOGRAMM.trim());
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		HelperClient.setDefaultsToComponent(wlaEinheit, 25);

		wlaVersandnummer = new WrapperLabel(LPMain.getTextRespectUISPr("ls.versandnummer"));
		wtfVersandnummer = new WrapperTextField();

		wlaVersandnummer2 = new WrapperLabel(LPMain.getTextRespectUISPr("ls.versandnummer2"));
		wtfVersandnummer2 = new WrapperTextField();
		
		HelperClient.setMinimumAndPreferredSize(wlaVersandnummer, HelperClient.getSizeFactoredDimension(100));

		panelAuslieferdatum = new PanelAuslieferdatum(getInternalFrame()) ;
		
		iZeile++;
		jpaWorkingOn.add(wlaVersandnummer, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersandnummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVersandnummer2, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersandnummer2, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(panelAuslieferdatum.getWlaAuslieferdatum(), new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		JPanel panelDatum = new JPanel(new MigLayout("insets 0", "[left,fill,90%][right,fill]")) ;
		panelDatum.add(panelAuslieferdatum.getWdfAuslieferdatum()) ;
		panelDatum.add(panelAuslieferdatum.getBtnAuslieferdatumJetzt()) ;

		jpaWorkingOn.add(panelDatum, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

//		jpaWorkingOn.add(panelAuslieferdatum.getWdfAuslieferdatum(), new GridBagConstraints(1, iZeile, 1,
//				1, 0.0, 0.0, GridBagConstraints.WEST,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		jpaWorkingOn.add(panelAuslieferdatum.getBtnAuslieferdatumJetzt(), new GridBagConstraints(2, iZeile, 1,
//				1, 0.0, 0.0, GridBagConstraints.WEST,
//				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
//		jpaWorkingOn.add(wlaLiefertermin, new GridBagConstraints(0, iZeile, 1,
//				1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		jpaWorkingOn.add(wdfLiefertermin, new GridBagConstraints(1, iZeile, 1,
//				1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPakete, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPakete, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		enableToolsPanelButtons(aWhichButtonIUse);
		getInternalFrame().addItemChangedListener(this);

		this.add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		super.eventActionSave(e, true);
		components2Dto();
		if (lieferscheinDto != null)
			DelegateFactory.getInstance().getLsDelegate()
					.updateLieferscheinOhneWeitereAktion(lieferscheinDto);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (lieferscheinDto.getStatusCNr().equals(
				LieferscheinFac.LSSTATUS_ANGELEGT)
				|| lieferscheinDto.getStatusCNr().equals(
						LieferscheinFac.LSSTATUS_OFFEN)
				|| lieferscheinDto.getStatusCNr().equals(
						LieferscheinFac.LSSTATUS_GELIEFERT)) {
			super.eventActionUpdate(aE, false); // Buttons schalten
			panelAuslieferdatum.enableForcedEingabe();
		} else {
			MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr(
							"ls.warning.lskannnichtgeaendertwerden"));
			mf.setLocale(LPMain.getTheClient().getLocUi());
			Object pattern[] = { lieferscheinDto.getStatusCNr() };
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setDefaults();
		dto2Components();
	}

	public void updateButtons() throws Throwable {
		super.updateButtons();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
//		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	protected void dto2Components() throws Throwable {
		wnfGewicht.setDouble(lieferscheinDto.getFGewichtLieferung());
		panelAuslieferdatum.dto2Components();
//		if (lieferscheinDto.getTLiefertermin() != null) {
//			wdfLiefertermin.setDate(lieferscheinDto.getTLiefertermin());
//		}
		wtfVersandnummer.setText(lieferscheinDto.getCVersandnummer());
		wtfVersandnummer2.setText(lieferscheinDto.getCVersandnummer2());
	}

	protected void components2Dto() throws Throwable {
		if (wnfGewicht.getDouble() != null)
			lieferscheinDto.setFGewichtLieferung(wnfGewicht.getDouble());
		panelAuslieferdatum.components2Dto();
//		lieferscheinDto.setTLiefertermin(wdfLiefertermin.getTimestamp());
		lieferscheinDto.setCVersandnummer(wtfVersandnummer.getText());
		lieferscheinDto.setCVersandnummer2(wtfVersandnummer2.getText());

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}
}