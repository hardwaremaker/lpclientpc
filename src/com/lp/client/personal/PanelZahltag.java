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
package com.lp.client.personal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZahltagDto;
import com.lp.server.system.ejb.Theclient;

@SuppressWarnings("static-access")
public class PanelZahltag extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private ZahltagDto zahltagDto = null;

	private WrapperLabel wlaMonat = new WrapperLabel();
	private WrapperComboBox wcoMonat = new WrapperComboBox();

	private WrapperLabel wlaStichtagNetto = new WrapperLabel();
	private WrapperNumberField wnfStichtagNetto = new WrapperNumberField();
	private WrapperCheckBox wcbMonatsletzter = new WrapperCheckBox();

	private WrapperLabel wlaFaktor = new WrapperLabel();
	private WrapperNumberField wnfFaktor = new WrapperNumberField();

	private WrapperLabel wlaStichtagLohnsteuer = new WrapperLabel();
	private WrapperNumberField wnfStichtagLohnsteuer = new WrapperNumberField();

	private WrapperLabel wlaStichtagSozialabgaben = new WrapperLabel();
	private WrapperNumberField wnfStichtagSozialabgaben = new WrapperNumberField();

	public PanelZahltag(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoMonat;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zahltagDto = new ZahltagDto();

		leereAlleFelder(this);
		if (wcbMonatsletzter.isSelected()) {
			wlaStichtagNetto.setText(LPMain.getInstance().getTextRespectUISPr(
					"pers.zahltag.banktage"));
		} else {
			wlaStichtagNetto.setText(LPMain.getInstance().getTextRespectUISPr(
					"pers.zahltag.stichtagnetto"));
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wcbMonatsletzter)) {

			if (wcbMonatsletzter.isSelected()) {
				wlaStichtagNetto
						.setText(LPMain.getInstance().getTextRespectUISPr(
								"pers.zahltag.banktage"));
			} else {
				wlaStichtagNetto.setText(LPMain.getInstance()
						.getTextRespectUISPr("pers.zahltag.stichtagnetto"));
			}

		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeZahltag(zahltagDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {

		zahltagDto.setMandantCNr(LPMain.getTheClient().getMandant());
		zahltagDto.setIMonat((Integer) wcoMonat.getKeyOfSelectedItem());
		zahltagDto.setBMonatsletzterNetto(wcbMonatsletzter.getShort());
		zahltagDto.setFFaktor(wnfFaktor.getDouble());
		zahltagDto
				.setIStichtagLohnsteuerRelativZumLetzten(wnfStichtagLohnsteuer
						.getInteger());
		zahltagDto.setIStichtagNetto(wnfStichtagNetto.getInteger());
		zahltagDto
				.setIStichtagSozialabgabenRelativZumLetzten(wnfStichtagSozialabgaben
						.getInteger());

	}

	protected void dto2Components() throws Throwable {
		wcoMonat.setKeyOfSelectedItem(zahltagDto.getIMonat());
		wnfStichtagNetto.setInteger(zahltagDto.getIStichtagNetto());
		wnfStichtagLohnsteuer.setInteger(zahltagDto
				.getIStichtagLohnsteuerRelativZumLetzten());
		wnfStichtagSozialabgaben.setInteger(zahltagDto
				.getIStichtagSozialabgabenRelativZumLetzten());
		wnfFaktor.setDouble(zahltagDto.getFFaktor());
		wcbMonatsletzter.setShort(zahltagDto.getBMonatsletzterNetto());

		if (wcbMonatsletzter.isSelected()) {
			wlaStichtagNetto.setText(LPMain.getInstance().getTextRespectUISPr(
					"pers.zahltag.banktage"));
		} else {
			wlaStichtagNetto.setText(LPMain.getInstance().getTextRespectUISPr(
					"pers.zahltag.stichtagnetto"));
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (zahltagDto.getIId() == null) {
				zahltagDto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate().createZahltag(zahltagDto));
				setKeyWhenDetailPanel(zahltagDto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateZahltag(zahltagDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(zahltagDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaMonat.setText(LPMain.getTextRespectUISPr("lp.monat1"));
		wcoMonat.setMandatoryField(true);
		DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance()
				.getUISprLocale());
		String[] defaultMonths = symbols.getMonths();
		Map<Integer, String> m = new TreeMap<Integer, String>();
		for (int i = 0; i < defaultMonths.length - 1; i++) {
			m.put(new Integer(i), defaultMonths[i]);

		}
		wcoMonat.setMap(m);

		wlaStichtagNetto.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zahltag.stichtagnetto"));
		wlaStichtagLohnsteuer.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zahltag.stichtag.lohnsteuer"));
		wlaStichtagSozialabgaben.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.zahltag.stichtag.sozial"));
		wlaFaktor.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zahltag.faktor"));
		wcbMonatsletzter.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.zahltag.monatsletzter"));

		wnfFaktor.setMandatoryField(true);
		wnfFaktor.setFractionDigits(2);
		wnfStichtagSozialabgaben.setMandatoryField(true);
		wnfStichtagSozialabgaben.setFractionDigits(0);
		wnfStichtagNetto.setMandatoryField(true);
		wnfStichtagNetto.setFractionDigits(0);
		wnfStichtagNetto.setMaximumValue(30);
		wnfStichtagNetto.setMinimumValue(-30);
		wnfStichtagLohnsteuer.setMandatoryField(true);
		wnfStichtagLohnsteuer.setFractionDigits(0);
		wnfStichtagLohnsteuer.setMaximumValue(365);
		wnfStichtagLohnsteuer.setMinimumValue(-365);
		wnfStichtagSozialabgaben.setMaximumValue(365);
		wnfStichtagSozialabgaben.setMinimumValue(-365);

		wcbMonatsletzter.addActionListener(this);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMonat, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoMonat, new GridBagConstraints(1, iZeile, 1, 1, 0.3,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wlaStichtagNetto, new GridBagConstraints(0, iZeile, 1,
				1, 0.4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 250, 0));
		jpaWorkingOn.add(wnfStichtagNetto, new GridBagConstraints(1, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMonatsletzter, new GridBagConstraints(2, iZeile, 1,
				1, 0.4, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 300, 0));
		iZeile++;

		jpaWorkingOn.add(wlaStichtagLohnsteuer, new GridBagConstraints(0,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfStichtagLohnsteuer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaStichtagSozialabgaben, new GridBagConstraints(0,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfStichtagSozialabgaben, new GridBagConstraints(1,
				iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaFaktor, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFaktor, new GridBagConstraints(1, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZULAGE;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			zahltagDto = DelegateFactory.getInstance().getPersonalDelegate()
					.zahltagFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}
}
