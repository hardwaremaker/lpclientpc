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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p>Report Lieferschein Adressetikett. <p>Copyright Logistik Pur Software GmbH
 * (c) 2004-2008</p> <p>Erstellungsdatum 28.09.05</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.12 $
 */
public class ReportLieferscheinWAEetikett extends PanelBasis implements PanelReportIfJRDS {
	private static final long serialVersionUID = 1L;
	private LieferscheinDto lieferscheinDto = null;
	private Integer lieferscheinpositionIId = null;

	private WrapperLabel wlaPaketnummer = null;
	protected WrapperNumberField wnfPaketnummer = null;
	private WrapperLabel wlaPaketanzahl = null;
	private WrapperCheckBox wcbAlle = null;
	private WrapperCheckBox wcbSpeichern = null;

	private WrapperLabel wlaPakete = null;
	private WrapperNumberField wnfPakete = null;
	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;
	private WrapperLabel wlaGewicht = null;
	private WrapperNumberField wnfGewicht = null;
	private WrapperLabel wlaEinheit = null;
	private WrapperLabel wlaVersandnummer = null;
	private WrapperTextField wtfVersandnummer = null;
	private WrapperLabel wlaVersandnummer2 = null;
	private WrapperTextField wtfVersandnummer2 = null;
	private WrapperLabel wlaHandmenge = null;
	private WrapperNumberField wnfHandmenge = null;

	private WrapperLabel wlaExemplare = null;
	protected WrapperNumberField wnfExemplare = null;

	public ReportLieferscheinWAEetikett(InternalFrame internalFrame, LieferscheinDto lieferscheinDto,
			Integer lieferscheinpositionIId, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		this.lieferscheinDto = lieferscheinDto;
		this.lieferscheinpositionIId = lieferscheinpositionIId;
		jbInit();
		setDefaults();
	}

	private void setDefaults() {

	}

	public String getModul() {
		return LieferscheinReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_WA_ETIKETT;

	}

	public boolean istLieferscheinErledigtOderVerrechnet() {
		if (lieferscheinDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)
				|| lieferscheinDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_ERLEDIGT)) {
			return true;
		} else {
			return false;
		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Integer iPaketnummer;
		if (wcbAlle.isSelected()) {
			iPaketnummer = null;
		} else {
			iPaketnummer = wnfPaketnummer.getInteger();
		}

		JasperPrintLP jpr=DelegateFactory.getInstance().getLieferscheinReportDelegate().printLieferscheinWAEtikett(
				lieferscheinDto.getIId(), lieferscheinpositionIId, iPaketnummer, wnfHandmenge.getBigDecimal(),
				wnfExemplare.getInteger(), wnfGewicht.getDouble(), wdfLiefertermin.getTimestamp(),
				wnfPakete.getInteger(), wtfVersandnummer.getText(), wtfVersandnummer2.getText(),
				wcbSpeichern.isSelected());
		
		return jpr;

	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	private void jbInit() throws Throwable {
		iZeile++;

		JPanel jpaWorkingOn = new JPanel();
		jpaWorkingOn.setLayout(new GridBagLayout());

		wlaExemplare = new WrapperLabel();
		wlaExemplare.setText(LPMain.getTextRespectUISPr("report.exemplare"));
		HelperClient.setMinimumAndPreferredSize(wlaExemplare, HelperClient.getSizeFactoredDimension(100));
		wnfExemplare = new WrapperNumberField();
		wnfExemplare.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
		wnfExemplare.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
		wnfExemplare.setFractionDigits(0);
		wnfExemplare.setMandatoryField(true);
		wnfExemplare.setInteger(1);

		wlaPaketnummer = new WrapperLabel();
		wlaPaketnummer.setText(LPMain.getInstance().getTextRespectUISPr("label.paketnummer"));
		wnfPaketnummer = new WrapperNumberField();
		wnfPaketnummer.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
		wnfPaketnummer.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
		wnfPaketnummer.setFractionDigits(0);
		wnfPaketnummer.setMaximumIntegerDigits(4);
		wlaPaketanzahl = new WrapperLabel();
		wlaPaketanzahl.setHorizontalAlignment(SwingConstants.LEFT);
		wlaPaketanzahl
				.setText(LPMain.getInstance().getTextRespectUISPr("lp.von") + " " + lieferscheinDto.getIAnzahlPakete());

		wcbAlle = new WrapperCheckBox();
		wcbAlle.setText(LPMain.getInstance().getTextRespectUISPr("lp.alle"));

		wcbSpeichern = new WrapperCheckBox();
		wcbSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.speichern"));
		wcbSpeichern.setSaveReportInformation(false);

		boolean rechtCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
		
		
		wlaPakete = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("ls.pakete"));
		wnfPakete = new WrapperNumberField();
		wnfPakete.setFractionDigits(0);
		wnfPakete.setMinimumValue(0);
		wlaLiefertermin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.auslieferdatum"));

		wlaHandmenge = new WrapperLabel();
		wlaHandmenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.handmenge"));
		wnfHandmenge = new WrapperNumberField();

		LieferscheinpositionDto[] posDtos = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
				.getLieferscheinPositionenByLieferschein(lieferscheinDto.getIId());

		if (lieferscheinpositionIId != null) {

			for (int i = 0; i < posDtos.length; i++) {
				if (posDtos[i].getNMenge() != null) {
					if (lieferscheinpositionIId.equals(posDtos[i].getIId())) {
						wnfHandmenge.setBigDecimal(posDtos[i].getNMenge());
						break;
					}
				}
			}
		}

		wdfLiefertermin = new WrapperDateField();
		wdfLiefertermin.setMandatoryFieldDB(false);
		wdfLiefertermin.setShowRubber(false);
		// wdfLiefertermin.getDisplay().addPropertyChangeListener(this);

		wlaGewicht = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.gewicht"));
		wnfGewicht = new WrapperNumberField();
		wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_KILOGRAMM.trim());
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		HelperClient.setDefaultsToComponent(wlaEinheit, 25);

		wlaVersandnummer = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("ls.versandnummer"));
		wtfVersandnummer = new WrapperTextField();

		wlaVersandnummer2 = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("ls.versandnummer2"));
		wtfVersandnummer2 = new WrapperTextField();

		iZeile++;
		jpaWorkingOn.add(wlaExemplare, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfExemplare, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVersandnummer, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersandnummer, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVersandnummer2, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersandnummer2, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLiefertermin, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfLiefertermin, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPakete, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPakete, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaPaketnummer, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPaketnummer, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaPaketanzahl, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wcbAlle, new GridBagConstraints(3, iZeile, 1, 1, 8.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 400, 0));
		iZeile++;
		jpaWorkingOn.add(wlaHandmenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfHandmenge, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbSpeichern, new GridBagConstraints(3, iZeile, 1, 1, 8.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 400, 0));

		iZeile++;

		this.add(jpaWorkingOn, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		if (istLieferscheinErledigtOderVerrechnet()) {
			wcbSpeichern.setSelected(false);
		} else {
			
			wcbSpeichern.setSelected(true);
			
		}
		
		if (!rechtCUD) {
			wcbSpeichern.setSelected(false);
			wcbSpeichern.setEnabled(false);
		}
		
		getInternalFrame().addItemChangedListener(this);

		lieferscheinDto=DelegateFactory.getInstance().getLsDelegate().lieferscheinFindByPrimaryKey(lieferscheinDto.getIId());
		
		wnfGewicht.setDouble(lieferscheinDto.getFGewichtLieferung());
		if (lieferscheinDto.getTLiefertermin() != null) {
			wdfLiefertermin.setDate(lieferscheinDto.getTLiefertermin());
		}
		wnfPakete.setInteger(lieferscheinDto.getIAnzahlPakete());
		wtfVersandnummer.setText(lieferscheinDto.getCVersandnummer());
		wtfVersandnummer2.setText(lieferscheinDto.getCVersandnummer2());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}


	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}


}
