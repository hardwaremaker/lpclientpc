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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;

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
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.client.zeiterfassung.PanelZeitdaten;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;

public class ReportRahmenuebersicht extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer auftragIId = null;
	private Integer auftragpositionIId = null;

	private PanelQueryFLR panelQueryFLRAuftragposition = null;

	private String ACTION_SPECIAL_POSITION_FROM_LISTE = "action_position_from_liste";
	private WrapperButton wbuPosition = new WrapperButton();
	private WrapperTextField wtfPosition = new WrapperTextField();

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	public ReportRahmenuebersicht(InternalFrame internalFrame, Integer iIdAuftragI, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);

		auftragIId = iIdAuftragI;
		jbInit();
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}
	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_POSITION_FROM_LISTE)) {
			dialogQueryAuftragpositionFromListe(e);
		} 
	}
	

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRAuftragposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				AuftragpositionDto auftragpositionDto = null;
				auftragpositionDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey(key);
				auftragpositionIId = auftragpositionDto.getIId();
				wtfPosition.setText(auftragpositionDto.getCBez());
				if (auftragpositionDto.getArtikelIId() != null && auftragpositionDto.getCBez() == null) {
					wtfPosition.setText(DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(auftragpositionDto.getArtikelIId()).formatArtikelbezeichnung());
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftragposition) {
				wtfPosition.setText(null);
				auftragpositionIId = null;
			}
		}
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wbuPosition.setText(LPMain.getTextRespectUISPr("lp.position") + "...");
		wbuPosition.setActionCommand(ACTION_SPECIAL_POSITION_FROM_LISTE);
		wbuPosition.addActionListener(this);
		wbuPosition.setMnemonic('O');

		jpaWorkingOn.add(wbuPosition, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPosition, new GridBagConstraints(1, iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

	}

	public String getReportname() {
		return AuftragReportFac.REPORT_RAHMENUEBERSICHT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory.getInstance().getAuftragReportDelegate().printRahmenuebersicht(auftragIId,
				auftragpositionIId);
	}

	void dialogQueryAuftragpositionFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		FilterKriterium krit1 = new FilterKriterium(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG + ".i_id", true,
				auftragIId.toString(), FilterKriterium.OPERATOR_EQUAL, false);

		panelQueryFLRAuftragposition = new PanelQueryFLR(null, new FilterKriterium[] { krit1 },
				QueryParameters.UC_ID_AUFTRAGPOSITION_ZEITERFASSUNG, aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("auft.title.panel.positionen"));

		new DialogQuery(panelQueryFLRAuftragposition);

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
