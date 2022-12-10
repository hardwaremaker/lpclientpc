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

import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.report.PanelDateRange;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Eingabe der Kriterien fuer die
 * Intrastatmeldung
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2007
 * </p>
 *
 * <p>
 * Erstellung: Martin Bluehweis; 07.08.07
 * </p>
 *
 * <p>
 * @author $Author: valentin $
 * </p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:09:12 $
 */
public class PanelDialogKriterienIntrastat extends PanelDialogKriterien {
	private static final long serialVersionUID = -8394740279553678563L;
	private WrapperRadioButton wrbEingang = null;
	private WrapperRadioButton wrbVersand = null;
	private WrapperNumberField wnfTransportkosten = null;
	private JLabel wlaWaehrung = null;
	private ButtonGroup bgBelegart = null;

	private JLabel wlaTransportkosten = null;
	private PanelDateRange panelDateRange = null;
	private JPanel panel = null;
	private HvLayout hvLayout = null;

	public PanelDialogKriterienIntrastat(InternalFrame oInternalFrameI, String title) throws Throwable {
		super(oInternalFrameI, title);
		jbInitPanel();
		setDefaults();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {
		wlaTransportkosten = new JLabel(LPMain.getTextRespectUISPr("bes.transportkosten"));

		wnfTransportkosten = new WrapperNumberField();
		wnfTransportkosten.setMandatoryField(true);
		wnfTransportkosten.setMinimumValue(0);

		wlaWaehrung = new JLabel();
		wlaWaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		wrbEingang = new WrapperRadioButton();
		wrbEingang.setText(LPMain.getTextRespectUISPr("finanz.intrastat.eingang"));

		wrbVersand = new WrapperRadioButton();
		wrbVersand.setText(LPMain.getTextRespectUISPr("finanz.intrastat.versand"));

		bgBelegart = new ButtonGroup();
		bgBelegart.add(wrbEingang);
		bgBelegart.add(wrbVersand);

		panelDateRange = new PanelDateRange();

		panel = new JPanel();

		hvLayout = HvLayoutFactory.create(panel);

		hvLayout
			.add(wrbEingang).wrap()
			.add(wrbVersand).wrap()
			.add(wlaTransportkosten).split(3)
			.add(wnfTransportkosten, 146)
			.add(wlaWaehrung).wrap()
			.add(panelDateRange);

		jpaWorkingOn.add(panel);

	}

	private void setDefaults() throws Throwable {
		wnfTransportkosten.setBigDecimal(new BigDecimal(0));
		wlaWaehrung.setText(LPMain.getTheClient().getSMandantenwaehrung());
		wrbEingang.setSelected(true);
		panelDateRange.getWdrc().doClickDown();
		panelDateRange.getWdrc().doClickUp();
	}

	public BigDecimal getTTransportkosten() throws Throwable {
		return wnfTransportkosten.getBigDecimal();
	}

	public String getVerfahren() {
		if (wrbEingang.isSelected()) {
			return FinanzReportFac.INTRASTAT_VERFAHREN_WARENEINGANG;
		} else {
			return FinanzReportFac.INTRASTAT_VERFAHREN_VERSAND;
		}
	}

	public java.sql.Date getDVon() {
		return panelDateRange.getWdfVon().getDate();
	}

	public java.sql.Date getDBis() {
		return panelDateRange.getWdfBis().getDate();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfTransportkosten;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (!allMandatoryFieldsSetDlg()) {
				return;
			}
		}
		super.eventActionSpecial(e);
	}
}
