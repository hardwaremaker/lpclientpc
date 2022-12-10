
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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.report.PanelDateRange;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

/**
 * <p>
 * <I>Dialog zur Eingabe der Kriterien fuer die Lieferschein Umsatz.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>13. 03. 2005</I>
 * </p>
 * <p>
 * </p>
 *
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelDialogNegativstundenInUrlaubUmwandeln extends PanelDialogKriterien {

	private static final long serialVersionUID = -4755031082378702530L;

	private InternalFrameZeiterfassung intFrame = null;
	private TabbedPaneZeiterfassung tpZeiterfassung = null;

	private JLabel wlaAuswahl = null;
	private WrapperTextField wtfPersonal = new WrapperTextField();
	private WrapperCheckBox wcbPersonalAlle = new WrapperCheckBox();

	private Integer personalIId = null;
	private ZeiterfassungPruefer zeiterfassungPruefer = null;

	private WrapperLabel wlaJahr = new WrapperLabel();
	private WrapperSpinner wspJahr = new WrapperSpinner(new Integer(0), new Integer(0), new Integer(9999),
			new Integer(1));
	private WrapperLabel wlaMonat = new WrapperLabel();
	private WrapperComboBox wcoMonat = new WrapperComboBox();

	public PanelDialogNegativstundenInUrlaubUmwandeln(InternalFrame oInternalFrameI, String title) throws Throwable {
		super(oInternalFrameI, title);

		intFrame = (InternalFrameZeiterfassung) getInternalFrame();
		tpZeiterfassung = intFrame.getTabbedPaneZeiterfassung();
		zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());

		DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance().getUISprLocale());
		String[] defaultMonths = symbols.getMonths();
		Map<Integer, String> m = new TreeMap<Integer, String>();
		for (int i = 0; i < defaultMonths.length - 1; i++) {
			m.put(new Integer(i), defaultMonths[i]);

		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int month = cal.get(Calendar.MONTH);
		wcoMonat.setMandatoryField(true);
		wcoMonat.setMap(m);
		wcoMonat.setSelectedIndex(month);
		wspJahr.setValue(new Integer(cal.get(Calendar.YEAR)));

		jbInit();

		if (intFrame.getPersonalDto() != null) {
			wtfPersonal.setText(intFrame.getPersonalDto().formatAnrede());
			personalIId = intFrame.getPersonalDto().getIId();
		}

		setDefaults();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 *
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// die Gruppe mit nach Zeiteinheit

		wlaAuswahl = new JLabel();
		wlaAuswahl.setText(LPMain.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson"));
		wtfPersonal.setActivatable(false);
		wtfPersonal.setEditable(false);
		wcbPersonalAlle.setText(LPMain.getTextRespectUISPr("lp.alle"));

		wlaJahr.setText(LPMain.getTextRespectUISPr("lp.jahr"));
		wlaMonat.setText(LPMain.getTextRespectUISPr("lp.monat1"));

		jpaWorkingOn.add(wlaAuswahl, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbPersonalAlle, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaJahr, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wspJahr, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wlaMonat, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wcoMonat, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

	}

	public boolean pruefeObBuchungMoeglich() throws ExceptionLP, Throwable {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, (Integer) wcoMonat.getKeyOfSelectedItem());
		cal.set(Calendar.YEAR, (Integer) wspJahr.getValue());
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return zeiterfassungPruefer.pruefeObBuchungMoeglich(new java.sql.Timestamp(cal.getTimeInMillis()));
	}

	private void setDefaults() throws Throwable {
	}

	public Integer getPersonlIId() {
		return personalIId;
	}

	public Integer getJahr() {
		return (Integer) wspJahr.getValue();
	}

	public Integer getMonat() {
		return (Integer) wcoMonat.getKeyOfSelectedItem();
	}

	public boolean isAlleSelected() {
		return wcbPersonalAlle.isSelected();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			allMandatoryFieldsSetDlg();

		}

		// den Dialog verlassen
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			tpZeiterfassung.gotoAuswahl();
		}
	}

	public FilterKriterium[] getAlleFilterKriterien() throws Throwable {
		return buildFilterKriterien();
	}
}
