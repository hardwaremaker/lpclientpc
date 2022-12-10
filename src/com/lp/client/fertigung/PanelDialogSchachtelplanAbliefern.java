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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.util.Helper;

/*
 * <p><I>Dialog zur Nachtraeglichen Entnahme von Material auf ein Los</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17. 10. 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.8 $
 */
public class PanelDialogSchachtelplanAbliefern extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<LosDto> losDtos = null;

	private WrapperLabel[] wlaLosgroesse = null;
	private WrapperLabel[] wlaLosNr = null;
	private WrapperLabel[] wlaStatus = null;
	private WrapperLabel[] wlaArtikelNummer = null;
	private WrapperLabel[] wlaArtikelBezeichnung = null;
	private WrapperNumberField[] wnfAusschuss = null;
	private String schachtelplannumer = null;

	private JScrollPane jspScrollPane = new JScrollPane();

	public PanelDialogSchachtelplanAbliefern(InternalFrame internalFrame,
			String schachtelplannumer, String title) throws Throwable {
		super(internalFrame, title);
		this.schachtelplannumer = schachtelplannumer;
		init();
		setDefaults();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void init() throws Throwable {

		losDtos = DelegateFactory.getInstance().getFertigungDelegate()
				.getAusgegebeneLoseEinerSchachtelplannummer(schachtelplannumer);

		if (losDtos == null || losDtos.size() == 0) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getMessageTextRespectUISPr(
									"stkl.profirstschachtelplan.keineoffenenlosegefunden",
									schachtelplannumer));
			getInternalFrame().closePanelDialog();
		}

		wlaLosgroesse = new WrapperLabel[losDtos.size()];
		wlaStatus = new WrapperLabel[losDtos.size()];
		wlaLosNr = new WrapperLabel[losDtos.size()];

		wlaArtikelBezeichnung = new WrapperLabel[losDtos.size()];

		wlaArtikelNummer = new WrapperLabel[losDtos.size()];
		wnfAusschuss = new WrapperNumberField[losDtos.size()];

		for (int i = 0; i < losDtos.size(); i++) {

			wlaLosNr[i] = new WrapperLabel(losDtos.get(i).getCNr());
			wlaStatus[i] = new WrapperLabel(losDtos.get(i).getStatusCNr());
			wlaLosgroesse[i] = new WrapperLabel(Helper.formatZahl(losDtos
					.get(i).getNLosgroesse(), Defaults.getInstance()
					.getIUINachkommastellenLosgroesse(), LPMain.getTheClient()
					.getLocUi()));

			wlaArtikelNummer[i] = new WrapperLabel("Materialliste");
			wlaArtikelBezeichnung[i] = new WrapperLabel(losDtos.get(i)
					.getCProjekt());

			if (losDtos.get(i).getStuecklisteIId() != null) {
				StuecklisteDto stklDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(
								losDtos.get(i).getStuecklisteIId());
				wlaArtikelNummer[i] = new WrapperLabel(stklDto.getArtikelDto()
						.getCNr());
				wlaArtikelBezeichnung[i] = new WrapperLabel(stklDto
						.getArtikelDto().formatBezeichnung());

			}
			wlaArtikelNummer[i].setHorizontalAlignment(SwingConstants.LEFT);
			wlaArtikelBezeichnung[i]
					.setHorizontalAlignment(SwingConstants.LEFT);
			wlaLosNr[i].setHorizontalAlignment(SwingConstants.LEFT);

			wlaStatus[i].setHorizontalAlignment(SwingConstants.LEFT);

			WrapperNumberField wnf = new WrapperNumberField();
			wnf.setBigDecimal(BigDecimal.ZERO);
			wnf.setFractionDigits(Defaults.getInstance()
					.getIUINachkommastellenLosgroesse());
			wnf.setMandatoryField(true);
			wnfAusschuss[i] = wnf;

		}

		getInternalFrame().addItemChangedListener(this);

		iZeile++;

		WrapperLabel wlaLosnummer = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.losnummer"));
		wlaLosnummer.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaLosnummer, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 80, 0));

		WrapperLabel wlaStueckliste = new WrapperLabel(
				LPMain.getTextRespectUISPr("stkl.stueckliste"));

		wlaStueckliste.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaStueckliste, new GridBagConstraints(1, iZeile, 1,
				1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaBezeichnung = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaBezeichnung.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(2, iZeile, 1,
				1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		WrapperLabel wlaStatusTop = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.status"));
		wlaStatusTop.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaStatusTop, new GridBagConstraints(3, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("label.losgroesse")),
						new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 30, 0));

		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("stkl.profirstschachtelplan.ausschussmenge")),
						new GridBagConstraints(11, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));

		iZeile++;
		for (int i = 0; i < losDtos.size(); i++) {
			jpaWorkingOn.add(wlaLosNr[i], new GridBagConstraints(0, iZeile, 1,
					1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 80,
					0));

			jpaWorkingOn.add(wlaArtikelNummer[i], new GridBagConstraints(1,
					iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 40,
					0));
			jpaWorkingOn.add(wlaArtikelBezeichnung[i], new GridBagConstraints(
					2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 90,
					0));

			jpaWorkingOn.add(wlaStatus[i],
					new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wlaLosgroesse[i], new GridBagConstraints(4,
					iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30,
					0));
			jpaWorkingOn.add(wnfAusschuss[i], new GridBagConstraints(11,
					iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -120, 0));

			iZeile++;

		}

		jspScrollPane.setViewportView(jpaWorkingOn);
		jspScrollPane.setAutoscrolls(true);

		this.add(jspScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {

			if (allMandatoryFieldsSetDlg()) {

				if (losDtos != null && losDtos.size() > 0) {
					HashMap<Integer, BigDecimal> hmLose = new HashMap<Integer, BigDecimal>();

					for (int i = 0; i < losDtos.size(); i++) {
						hmLose.put(losDtos.get(i).getIId(),
								wnfAusschuss[i].getBigDecimal());
					}

					DelegateFactory.getInstance().getFertigungDelegate()
							.loseEinesSchachteplansAbliefern(hmLose);
				}

				getInternalFrame().closePanelDialog();

			}

		}
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

		}
	}

	/**
	 * Dialogfenster zur Artikelauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */

	private void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
