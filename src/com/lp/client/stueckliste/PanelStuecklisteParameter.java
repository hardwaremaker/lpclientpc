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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.personal.service.ReligionsprDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StklparametersprDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster werden Parameter fuer den Mandanten erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-09-29</p>
 * <p> </p>
 * @author uli walch
 * @version $Revision: 1.6 $
 */
public class PanelStuecklisteParameter extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Cache for convenience. */
	private InternalFrameStueckliste intFrame = null;

	private StklparameterDto stklparameterDto = null;

	private WrapperLabel wlaKennung = null;
	private WrapperLabel wlaBezeichnung = null;
	private WrapperLabel wlaDatentyp = null;

	private WrapperTextField wtfKennung = null;
	private WrapperTextField wtfBezeichnung = null;
	private WrapperComboBox wcoDatentyp = new WrapperComboBox();
	private WrapperCheckBox wcbComboBox = new WrapperCheckBox();
	private WrapperCheckBox wcbMandatory = new WrapperCheckBox();

	private WrapperLabel wlaBereich = null;
	private WrapperTextField wtfBereich = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	private WrapperLabel wlaMin = null;
	private WrapperLabel wlaMax = null;
	private WrapperNumberField wnfMin = new WrapperNumberField();
	private WrapperNumberField wnfMax = new WrapperNumberField();

	public PanelStuecklisteParameter(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameStueckliste) internalFrame;

		jbInit();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		wlaBezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bezeichnung"));
		HelperClient.setDefaultsToComponent(wlaBezeichnung, 90);

		wlaKennung = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));

		wlaDatentyp = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.datentyp"));

		wcbMandatory.setText(LPMain.getInstance().getTextRespectUISPr(
				"stk.parameter.mandatory"));

		wtfKennung = new WrapperTextField();
		wtfKennung.setMandatoryField(true);
		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.setColumnsMax(80);

		wcoDatentyp.setMandatoryField(true);
		wcbComboBox.setText(LPMain.getInstance().getTextRespectUISPr(
				"stk.parameter.combobox"));

		wlaBereich = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"stk.parameter.bereich"));
		wtfBereich = new WrapperTextField();
		wtfBereich.setColumnsMax(600);

		Map<String, String> datentyp = new TreeMap<String, String>();
		datentyp.put(StuecklisteFac.STKLPARAMETER_TYP_STRING,
				StuecklisteFac.STKLPARAMETER_TYP_STRING);
		datentyp.put(StuecklisteFac.STKLPARAMETER_TYP_INTEGER,
				StuecklisteFac.STKLPARAMETER_TYP_INTEGER);
		datentyp.put(StuecklisteFac.STKLPARAMETER_TYP_DOUBLE,
				StuecklisteFac.STKLPARAMETER_TYP_DOUBLE);
		datentyp.put(StuecklisteFac.STKLPARAMETER_TYP_BOOLEAN,
				StuecklisteFac.STKLPARAMETER_TYP_BOOLEAN);
		datentyp.put(StuecklisteFac.STKLPARAMETER_TYP_BIGDECIMAL,
				StuecklisteFac.STKLPARAMETER_TYP_BIGDECIMAL);
		datentyp.put(StuecklisteFac.STKLPARAMETER_TYP_ITEM_ID,
				StuecklisteFac.STKLPARAMETER_TYP_ITEM_ID + " (Artikel)");
		datentyp.put(StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID,
				StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID + " (Kunde)");
		wcoDatentyp.setMap(datentyp);

		wcoDatentyp.addActionListener(this);
		wcoDatentyp.setActionCommand("CHANGE");

		wcbComboBox.addActionListener(this);

		wlaMin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"stk.parameter.min"));
		wlaMax = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"stk.parameter.max"));
		// Zeile

		jPanelWorkingOn.add(wlaDatentyp, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcoDatentyp, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wcbMandatory, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wcbComboBox, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		// Zeile
		jPanelWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile,
				1, 1, 0.05, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile,
				1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaBereich, new GridBagConstraints(0, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBereich, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaMin, new GridBagConstraints(0, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMin, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaMax, new GridBagConstraints(0, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMax, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0,
				iZeile, 1, 1, 0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getStuecklisteDelegate()
				.removeStklparameter(stklparameterDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (stklparameterDto.getIId() == null) {
				Integer stklparameterIId = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.createStklparameter(stklparameterDto);
				stklparameterDto.setIId(stklparameterIId);
				setKeyWhenDetailPanel(stklparameterIId);
			} else {
				DelegateFactory.getInstance().getStuecklisteDelegate()
						.updateStklparameter(stklparameterDto);
			}

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		resetPanel();

	}

	private void resetPanel() throws Throwable {
		stklparameterDto = new StklparameterDto();
		leereAlleFelder(this);
	}

	private void updateTyp() throws Throwable {
		Object key = wcoDatentyp.getKeyOfSelectedItem();

		wcbComboBox.setVisible(false);
		wlaBereich.setVisible(false);
		wtfBereich.setVisible(false);
		wtfBereich.setMandatoryField(false);

		wlaMin.setVisible(false);
		wnfMin.setVisible(false);

		wlaMax.setVisible(false);
		wnfMax.setVisible(false);

		if (key.equals(StuecklisteFac.STKLPARAMETER_TYP_STRING)) {
			wcbComboBox.setVisible(true);

			if (wcbComboBox.isSelected()) {
				wlaBereich.setVisible(true);
				wtfBereich.setVisible(true);
				wtfBereich.setMandatoryField(true);
			}
		} else if (key.equals(StuecklisteFac.STKLPARAMETER_TYP_DOUBLE)
				|| key.equals(StuecklisteFac.STKLPARAMETER_TYP_INTEGER)
				|| key.equals(StuecklisteFac.STKLPARAMETER_TYP_BIGDECIMAL)) {
			wlaMin.setVisible(true);
			wnfMin.setVisible(true);

			wlaMax.setVisible(true);
			wnfMax.setVisible(true);
		}
		// }
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wcoDatentyp)) {
			updateTyp();
		} else if (e.getSource().equals(wcbComboBox)) {

			if (wcbComboBox.isSelected()) {
				wlaBereich.setVisible(true);
				wtfBereich.setVisible(true);
				wtfBereich.setMandatoryField(true);
			} else {
				wlaBereich.setVisible(false);
				wtfBereich.setVisible(false);
				wtfBereich.setMandatoryField(false);
			}

		}
	}

	private void dto2Components() throws Throwable {

		wtfKennung.setText(stklparameterDto.getCNr());

		if (stklparameterDto.getStklparametersprDto() != null) {
			wtfBezeichnung.setText(stklparameterDto.getStklparametersprDto()
					.getCBez());
		} else {
			wtfBezeichnung.setText("");

		}
		wnfMin.setBigDecimal(stklparameterDto.getNMin());
		wnfMax.setBigDecimal(stklparameterDto.getNMax());
		wcbComboBox.setShort(stklparameterDto.getBCombobox());
		wcbMandatory.setShort(stklparameterDto.getBMandatory());
		wtfBereich.setText(stklparameterDto.getCBereich());
		wcoDatentyp.setKeyOfSelectedItem(stklparameterDto.getCTyp().trim());
	}

	private void components2Dto() throws Throwable {
		// es werden keine neuen Parameter angelegt
		stklparameterDto.setStuecklisteIId(intFrame.getStuecklisteDto()
				.getIId());

		stklparameterDto.setCNr(wtfKennung.getText());

		if (stklparameterDto.getStklparametersprDto() == null) {
			stklparameterDto.setStklparametersprDto(new StklparametersprDto());
		}
		stklparameterDto.getStklparametersprDto().setCBez(
				wtfBezeichnung.getText());

		stklparameterDto.setCTyp((String) wcoDatentyp.getKeyOfSelectedItem());

		stklparameterDto.setNMin(wnfMin.getBigDecimal());
		stklparameterDto.setNMax(wnfMax.getBigDecimal());

		stklparameterDto.setBCombobox(wcbComboBox.getShort());
		stklparameterDto.setCBereich(wtfBereich.getText());
		stklparameterDto.setBMandatory(wcbMandatory.getShort());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		updateTyp();
		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		leereAlleFelder(this);

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {

			stklparameterDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.stklparameterFindByPrimaryKey((Integer) oKey);
			dto2Components();

		}

		getInternalFrame()
				.setLpTitle(
						InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
						LPMain.getInstance().getTextRespectUISPr(
								"lp.parametermandant"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				LPMain.getInstance().getTheClient().getMandant());

		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STUECKLISTE;
	}
}