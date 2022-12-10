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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Report Auftragnachkalkulation.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 12.07.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class ReportTheoretischeFehlmengenliste extends PanelBasis implements PanelReportIfJRDS {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Integer projektIId = null;
	private Integer auftragIId = null;
	private Integer losIId = null;
	static final public String ACTION_SPECIAL_PROJEKT_FROM_LISTE = "action_projekt_from_liste";
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRProjekt = null;
	private PanelQueryFLR panelQueryFLRLos = null;

	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftrag = new WrapperTextField();

	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();

	private WrapperButton wbuProjekt = new WrapperButton();
	private WrapperTextField wtfProjekt = new WrapperTextField();

	WrapperCheckBox wcbSortierungWieErfasst = new WrapperCheckBox();

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	public ReportTheoretischeFehlmengenliste(InternalFrame internalFrame, Integer losIId, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);

		this.losIId = losIId;
		jbInit();
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	private void dialogQueryAuftrag(ActionEvent e) throws Throwable {

		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), false, true,
				null, auftragIId);
		new DialogQuery(panelQueryFLRAuftrag);
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRLose(getInternalFrame(), null, true);

		panelQueryFLRLos.setSelectedId(losIId);

		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance().createPanelFLRProjekt(getInternalFrame(), projektIId,
				true);
		new DialogQuery(panelQueryFLRProjekt);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_PROJEKT_FROM_LISTE)) {
			dialogQueryProjektFromListe(e);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			dialogQueryAuftrag(e);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			losIId = null;
			wtfLos.setText(null);
			projektIId = null;
			wtfProjekt.setText(null);
			auftragIId = null;
			wtfAuftrag.setText(null);

			if (e.getSource() == panelQueryFLRProjekt) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate()
						.projektFindByPrimaryKey((Integer) key);
				wtfProjekt.setText(projektDto.getCNr());
				projektIId = projektDto.getIId();
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey((Integer) key);

				wtfAuftrag.setText(auftragDto.getCNr());
				auftragIId = auftragDto.getIId();
			} else if (e.getSource() == panelQueryFLRLos) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate().losFindByPrimaryKey((Integer) key);

				wtfLos.setText(losDto.getCNr());
				losIId = losDto.getIId();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRProjekt) {
				projektIId = null;

				wtfProjekt.setText(null);
			} else if (e.getSource() == panelQueryFLRLos) {
				losIId = null;
				wtfLos.setText(null);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				auftragIId = null;
				wtfAuftrag.setText(null);
			}

		}

	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		getInternalFrame().addItemChangedListener(this);

		wbuProjekt.setText(LPMain.getTextRespectUISPr("proj.projekt") + "..");
		wbuAuftrag.setText(LPMain.getTextRespectUISPr("button.auftrag"));
		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title") + "...");

		wtfProjekt.setActivatable(false);
		wtfLos.setActivatable(false);
		wtfAuftrag.setActivatable(false);

		wbuProjekt.setActionCommand(ACTION_SPECIAL_PROJEKT_FROM_LISTE);
		wbuProjekt.addActionListener(this);

		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);

		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);

		wcbSortierungWieErfasst.setText(LPMain.getTextRespectUISPr("fert.theoretischefehlmengenliste.wieerfasst"));

		if (losIId != null) {
			LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate().losFindByPrimaryKey(losIId);
			wtfLos.setText(losDto.getCNr());
		}

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile, 1, 1, 0.12, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbSortierungWieErfasst, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 0.12, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuProjekt, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 1, 1, 0.12, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getReportname() {
		return FertigungReportFac.REPORT_THEORETISCHE_FEHLMENGEN;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory.getInstance().getFertigungDelegate().printTheoretischeFehlmengen(losIId, auftragIId,
				projektIId, wcbSortierungWieErfasst.isSelected());

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
