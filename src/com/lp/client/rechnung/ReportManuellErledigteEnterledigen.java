
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
package com.lp.client.rechnung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.rechnung.service.AbrechnungsvorschlagFac;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportManuellErledigteEnterledigen extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaZeitraum = new WrapperLabel();
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperDateRangeController wdrBereich = null;

	private JButton buttonEnterledigen = null;

	private WrapperButton wbuAuftrag = null;
	private WrapperTextField wtfAuftrag = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private final static String ACTION_SPECIAL_AUFTRAG = "action_special_auftrag";

	private final static String ACTION_SPECIAL_ENTERLEDIGEN = "action_special_" + ALWAYSENABLED + "_enterledigen";

	private Integer auftragIId = null;

	HashMap<String, ArrayList<Integer>> hmEnterledigen = null;

	private WrapperSelectField wsfKunde = new WrapperSelectField(WrapperSelectField.KUNDE, getInternalFrame(), true);
	private WrapperSelectField wsfPersonal = new WrapperSelectField(WrapperSelectField.PERSONAL, getInternalFrame(),
			true);

	public ReportManuellErledigteEnterledigen(InternalFrameRechnung internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("artikel.allergen");

		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private void dialogQueryAuftrag() throws Throwable {
		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), true, true,
				null, auftragIId);
		new DialogQuery(panelQueryFLRAuftrag);
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaZeitraum.setText(LPMain.getTextRespectUISPr("lp.zeitraum") + ":");
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		buttonEnterledigen = ButtonFactory.createJButton(null, null, ACTION_SPECIAL_ENTERLEDIGEN);
		buttonEnterledigen.setText(
				LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.manuellerledigte.enterledigen.enterledigen"));
		buttonEnterledigen.setEnabled(false);
		buttonEnterledigen.addActionListener(this);

		wbuAuftrag = new WrapperButton(LPMain.getTextRespectUISPr("button.auftrag"));
		wbuAuftrag.setToolTipText(LPMain.getTextRespectUISPr("button.auftrag.tooltip"));
		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);
		wtfAuftrag = new WrapperTextField();
		wtfAuftrag.setActivatable(false);

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		wdrBereich.doClickDown();
		wdrBereich.doClickDown();

		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		int iZeile = 0;

		jpaWorkingOn.add(wlaZeitraum, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfPersonal.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfPersonal.getWrapperTextField(), new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfKunde.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfKunde.getWrapperTextField(), new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(buttonEnterledigen, new GridBagConstraints(3, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

	}

	public String getModul() {
		return RechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AbrechnungsvorschlagFac.REPORT_MANUELLERLEDIGTEZEITEN;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					auftragIId = (Integer) key;
					AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragIId);
					wtfAuftrag.setText(auftragDto.getCNr());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				auftragIId = null;
				wtfAuftrag.setText(null);
			}
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
			dialogQueryAuftrag();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ENTERLEDIGEN)) {
			if (hmEnterledigen != null) {
				DelegateFactory.getInstance().getRechnungDelegate().enterledigen(hmEnterledigen);
				buttonEnterledigen.setEnabled(false);
			}
		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		hmEnterledigen = null;
		JasperPrintLP print = DelegateFactory.getInstance().getRechnungDelegate().printManuellErledigteEnterledigen(
				wsfPersonal.getIKey(), wsfKunde.getIKey(), auftragIId, wdrBereich.getDatumsfilterVonBis());

		hmEnterledigen = (HashMap<String, ArrayList<Integer>>) print.getMapParameters().get("P_HM_ENTERLEDIGEN");
		if (hmEnterledigen != null && hmEnterledigen.size() > 0) {
			buttonEnterledigen.setEnabled(true);
		}
		return print;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
