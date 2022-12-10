package com.lp.client.benutzer;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.personal.ejb.HvmaparameterPK;
import com.lp.server.personal.fastlanereader.generated.service.FLRHvmaparameterPK;
import com.lp.server.personal.service.HvmabenutzerDto;
import com.lp.server.personal.service.HvmabenutzerParameterDto;
import com.lp.server.personal.service.HvmaparameterDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>
 * In diesem Fenster werden Parameter fuer den Mandanten erfasst.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2004-09-29
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 * @version $Revision: 1.3 $
 */
public class PanelHvmabenutzerParameter extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Cache for convenience. */
	private InternalFrameBenutzer intFrame = null;

	private HvmabenutzerParameterDto arbeitsplatzparameterDto = null;

	private WrapperButton wbuParameter = new WrapperButton();
	private WrapperTextField wtfParameter = new WrapperTextField();

	private WrapperLabel wlaBemerkunglarge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
	private WrapperEditorField wefBemerkunglarge = null;

	private WrapperLabel wlaWert = new WrapperLabel();

	private WrapperTextField wtfWert = new WrapperTextField();

	private WrapperLabel wlaDatentyp = new WrapperLabel();
	private WrapperTextField wtfDatentyp = new WrapperTextField();

	private WrapperLabel wlaKategorie = null;
	private WrapperTextField wtfKategorie = null;
	
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;

	private PanelQueryFLR panelQueryFLRParameter = null;

	static final public String ACTION_SPECIAL_PARAMETER_FROM_LISTE = "ACTION_SPECIAL_PARAMETER_FROM_LISTE";

	public PanelHvmabenutzerParameter(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameBenutzer) internalFrame;

		jbInit();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		getInternalFrame().addItemChangedListener(this);
		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wbuParameter.setText(LPMain.getInstance().getTextRespectUISPr("lp.parameter") + "...");

		wbuParameter.setActionCommand(ACTION_SPECIAL_PARAMETER_FROM_LISTE);
		wbuParameter.addActionListener(this);

		wlaKategorie = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.kategorie"));
		wtfKategorie = new WrapperTextField();
		wtfKategorie.setActivatable(false);
		wtfKategorie.setMandatoryField(true);
		
		wlaWert = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.wert"));
		wlaDatentyp = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.datentyp"));
		wtfWert.setColumnsMax(ParameterFac.MAX_PARAMETER_KENNUNG);
		wtfWert.setActivatable(false);
		wtfDatentyp = new WrapperTextField();
		wtfDatentyp.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfDatentyp.setActivatable(false);
		wtfParameter.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfParameter.setActivatable(false);
		wtfParameter.setMandatoryField(true);
		wtfWert = new WrapperTextField();
		wtfWert.setMandatoryField(true);
		wtfWert.setColumnsMax(100);

		wefBemerkunglarge = new WrapperEditorField(intFrame, LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
		wefBemerkunglarge.setActivatable(false);

		// Zeile
		jPanelWorkingOn.add(wbuParameter, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfParameter, new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jPanelWorkingOn.add(wlaKategorie, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKategorie, new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaWert, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfWert, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaDatentyp, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfDatentyp, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaBemerkunglarge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefBemerkunglarge, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRParameter) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				FLRHvmaparameterPK flrPK = (FLRHvmaparameterPK) key;

				HvmaparameterPK pk = new HvmaparameterPK(flrPK.getC_nr(), flrPK.getC_kategorie());

				HvmaparameterDto parameterDto = DelegateFactory.getInstance().getHvmaDelegate()
						.parameterFindByPrimaryKey(pk);
				wtfParameter.setText(parameterDto.getCNr());
				wtfDatentyp.setText(parameterDto.getDatentyp());
				wtfKategorie.setText(parameterDto.getKategorie());
				wtfWert.setText(parameterDto.getDefaultWert());
				wefBemerkunglarge.setText(parameterDto.getBemerkung());

				arbeitsplatzparameterDto.setCNr(parameterDto.getCNr());
				arbeitsplatzparameterDto.setKategorie(parameterDto.getKategorie());
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARAMETER_FROM_LISTE)) {
			dialogQueryParameterFromListe(e);
		}

	}

	void dialogQueryParameterFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRParameter = SystemFilterFactory.getInstance().createPanelFLRHvmaParameter(getInternalFrame(),
				arbeitsplatzparameterDto.getCNr());

		new DialogQuery(panelQueryFLRParameter);
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		HvmabenutzerParameterDto dto = new HvmabenutzerParameterDto();
		dto.setIId((Integer) getKeyWhenDetailPanel());
		DelegateFactory.getInstance().getHvmaDelegate().removeHvmabenutzerparameter(dto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
		arbeitsplatzparameterDto = new HvmabenutzerParameterDto();
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (Helper.istWertVomTyp(wtfWert.getText(), wtfDatentyp.getText())) {
				components2Dto();

				if (arbeitsplatzparameterDto.getIId() == null) {
					arbeitsplatzparameterDto.setIId(DelegateFactory.getInstance().getHvmaDelegate()
							.createHvmabenutzerparameter(arbeitsplatzparameterDto));
					arbeitsplatzparameterDto = DelegateFactory.getInstance().getHvmaDelegate()
							.hvmabenutzerparameterFindByPrimaryKey(arbeitsplatzparameterDto.getIId());
					setKeyWhenDetailPanel(arbeitsplatzparameterDto.getIId());
				} else {
					DelegateFactory.getInstance().getHvmaDelegate()
							.updateHvmabenutzerparameter(arbeitsplatzparameterDto);
				}

				// buttons schalten
				super.eventActionSave(e, true);

				eventYouAreSelected(false);
			} else {
				if (wtfDatentyp.getText() != null) {
					if (wtfDatentyp.getText().equals("java.lang.Boolean")) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								LPMain.getInstance().getTextRespectUISPr("system.nurbooleanwerte"));
					} else if (wtfDatentyp.getText().equals("java.lang.Integer")) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								LPMain.getInstance().getTextRespectUISPr("system.nurganzzahligewerte"));
					} else if (wtfDatentyp.getText().equals("java.lang.Double")) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								LPMain.getInstance().getTextRespectUISPr("system.nurzahlenewerte"));
					} else if (wtfDatentyp.getText().equals("java.math.BigDecimal")) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								LPMain.getInstance().getTextRespectUISPr("system.nurzahlenewerte"));
					} else {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								LPMain.getInstance().getTextRespectUISPr("system.wertnichtunterstuetzt"));
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr("system.wertnichtunterstuetzt"));
				}
			}
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		resetPanel();
	}

	private void resetPanel() throws Throwable {
		arbeitsplatzparameterDto = new HvmabenutzerParameterDto();
		leereAlleFelder(this);
	}

	private void dto2Components() throws Throwable {

		HvmaparameterPK pk = new HvmaparameterPK(arbeitsplatzparameterDto.getCNr(),
				arbeitsplatzparameterDto.getKategorie());

		HvmaparameterDto parameterDto = DelegateFactory.getInstance().getHvmaDelegate().parameterFindByPrimaryKey(pk);
		wtfParameter.setText(parameterDto.getCNr());
		wtfKategorie.setText(parameterDto.getKategorie());
		wtfDatentyp.setText(parameterDto.getDatentyp());
		wtfParameter.setText(arbeitsplatzparameterDto.getCNr());
		wtfWert.setText(arbeitsplatzparameterDto.getWert());
		wefBemerkunglarge.setText(parameterDto.getBemerkung());
	}

	private void components2Dto() throws Throwable {
		arbeitsplatzparameterDto
				.setHvmabenutzerId((Integer) intFrame.getTabbedPaneHvma().getPanelQueryHvmabenutzer().getSelectedId());
		arbeitsplatzparameterDto.setWert(wtfWert.getText());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		// setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {

			arbeitsplatzparameterDto = DelegateFactory.getInstance().getHvmaDelegate()
					.hvmabenutzerparameterFindByPrimaryKey((Integer) oKey);
			dto2Components();
		} else {
			leereAlleFelder(this);
		}

		Integer hvmabenutzerIId = (Integer) intFrame.getTabbedPaneHvma().getPanelQueryHvmabenutzer().getSelectedId();
		HvmabenutzerDto hvmabenutzerDto = DelegateFactory.getInstance().getHvmaDelegate()
				.hvmabenutzerFindByPrimaryKey(hvmabenutzerIId);

		BenutzerDto benutzerDto = DelegateFactory.getInstance().getBenutzerDelegate()
				.benutzerFindByPrimaryKey(hvmabenutzerDto.getBenutzerIId());

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				LPMain.getInstance().getTextRespectUISPr("lp.parametermandant"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				intFrame.getTabbedPaneHvma().getHvmalizenzDto().getCNr() +" "+ LPMain.getInstance().getTextRespectUISPr("pers.hvmabenutzer")+": "+benutzerDto.getCBenutzerkennung());

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARAMETERMANDANT;
	}
}
