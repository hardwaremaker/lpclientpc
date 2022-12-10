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
package com.lp.client.forecast;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastartDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;

@SuppressWarnings("static-access")
public class PanelForecast extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ForecastDto forecastDto = null;

	private InternalFrameForecast internalFrameForecast = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private PanelQueryFLR panelQueryFLRKunde = null;

	private WrapperGotoButton wbuKunde = new WrapperGotoButton(
			com.lp.util.GotoHelper.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKunde = new WrapperTextField();

	

	private WrapperLabel wlaTageCod = new WrapperLabel();
	private WrapperNumberField wnfTageCod = new WrapperNumberField();
	private WrapperLabel wlaTage = new WrapperLabel();
	

	private WrapperLabel wlaGueltigTage = new WrapperLabel();
	private WrapperNumberField wnfTageGueltig = new WrapperNumberField();
	
	private WrapperLabel wlaWochenCow = new WrapperLabel();
	private WrapperNumberField wnfWochenCow = new WrapperNumberField();
	private WrapperLabel wlaWochen = new WrapperLabel();
	

	private WrapperLabel wlaMonateFca = new WrapperLabel();
	private WrapperNumberField wnfMonateFca = new WrapperNumberField();
	private WrapperLabel wlaMonate = new WrapperLabel();

	
	private WrapperLabel wlaNummer = new WrapperLabel();
	private WrapperTextField wtfNummer = new WrapperTextField();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperTextField wtfProjekt = new WrapperTextField();

	private WrapperLabel wlaForecastartForecastauftrag = new WrapperLabel();
	private WrapperTextField wtfPfadForecastauftrag = new WrapperTextField(300);
	private WrapperButton wbuPfadForecastauftrag = new WrapperButton(
			new ImageIcon(getClass().getResource(
					"/com/lp/client/res/folder.png")));

	private WrapperLabel wlaForecastartCow = new WrapperLabel();
	private WrapperTextField wtfPfadCow = new WrapperTextField(300);
	private WrapperButton wbuPfadCow = new WrapperButton(new ImageIcon(
			getClass().getResource("/com/lp/client/res/folder.png")));

	private WrapperLabel wlaForecastartCod = new WrapperLabel();
	private WrapperTextField wtfPfadCod = new WrapperTextField(300);
	private WrapperButton wbuPfadCod = new WrapperButton(new ImageIcon(
			getClass().getResource("/com/lp/client/res/folder.png")));

	private WrapperLabel wlaForecastartLinienabruf = new WrapperLabel();
	private WrapperTextField wtfPfadLinienabruf = new WrapperTextField(300);
	private WrapperButton wbuPfadLinienabruf = new WrapperButton(new ImageIcon(
			getClass().getResource("/com/lp/client/res/folder.png")));

	private WrapperLabel wlaImportdef = new WrapperLabel();
	private WrapperComboBox wcoImportdefForecastauftrag = new WrapperComboBox();
	private WrapperComboBox wcoImportdefCow = new WrapperComboBox();
	private WrapperComboBox wcoImportdefCod = new WrapperComboBox();
	private WrapperComboBox wcoImportdefLinienabruf = new WrapperComboBox();

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kunde_from_liste";
	
	static final public String ACTION_SPECIAL_PFAD_FORECASTAUFTRAG = "ACTION_SPECIAL_PFAD_FORECASTAUFTRAG";
	static final public String ACTION_SPECIAL_PFAD_COW = "ACTION_SPECIAL_PFAD_COW";
	static final public String ACTION_SPECIAL_PFAD_COD = "ACTION_SPECIAL_PFAD_COD";
	static final public String ACTION_SPECIAL_PFAD_LINIENABRUF = "ACTION_SPECIAL_PFAD_LINIENABRUF";

	private final static String ACTION_SPECIAL_IMPORT = LEAVEALONE + "action_special_import_forecastfiles";
	private WrapperButton wbuImport = new WrapperButton(IconFactory.getImport());
	
	public InternalFrameForecast getInternalFrameForecast() {
		return internalFrameForecast;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfNummer;
	}

	public PanelForecast(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameForecast = (InternalFrameForecast) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getInternalFrameForecast().getTpForecast()
				.getForecastDto().getIId();

		if (key != null) {
			forecastDto = DelegateFactory.getInstance().getForecastDelegate()
					.forecastFindByPrimaryKey((Integer) key);

			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					forecastDto.getCNr());

		} else {
			leereAlleFelder(this);
			setDefaults();

		}

	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		wtfNummer.setText(forecastDto.getCNr());
		wtfProjekt.setText(forecastDto.getCProjekt());

		// Goto Kunde Ziel setzen
		wbuKunde.setOKey(forecastDto.getKundeIId());

		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(forecastDto.getKundeIId());

		wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

		

		wtfPfadCod.setText(forecastDto.getCPfadCod());
		wtfPfadCow.setText(forecastDto.getCPfadCow());
		wtfPfadForecastauftrag.setText(forecastDto.getCPfadForecastauftrag());
		wtfPfadLinienabruf.setText(forecastDto.getCPfadLinienabruf());

		wcoImportdefCod.setKeyOfSelectedItem(forecastDto.getImportdefCNrCod());
		wcoImportdefCow.setKeyOfSelectedItem(forecastDto.getImportdefCNrCow());
		wcoImportdefForecastauftrag.setKeyOfSelectedItem(forecastDto
				.getImportdefCNrForecastauftrag());
		wcoImportdefLinienabruf.setKeyOfSelectedItem(forecastDto
				.getImportdefCNrLinienabruf());
		
		wnfTageCod.setInteger(forecastDto.getITageCod());
		wnfWochenCow.setInteger(forecastDto.getIWochenCow());
		wnfMonateFca.setInteger(forecastDto.getIMonateFca());
		
		wnfTageGueltig.setInteger(forecastDto.getITageGueltig());
		
		setHmButtonEnabled(ACTION_SPECIAL_IMPORT, wtfPfadForecastauftrag.getText() != null);
	}

	void dialogQueryKundeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, false,
						forecastDto.getKundeIId());
		new DialogQuery(panelQueryFLRKunde);
	}

	
	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));

	

		wtfKunde.setMandatoryField(true);
		wtfKunde.setActivatable(false);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		

		wtfNummer.setMandatoryField(true);

		wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));
		wlaNummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));

		wlaTageCod.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.forecast.tagecod"));
		wlaWochenCow.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.forecast.wochencow"));
		wlaMonateFca.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.forecast.monatefca"));
		
		wlaGueltigTage.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.forecast.gueltig"));
		wnfTageGueltig.setFractionDigits(0);
		
		
		wnfMonateFca.setMandatoryField(true);
		wnfMonateFca.setFractionDigits(0);
		wnfTageCod.setMandatoryField(true);
		wnfTageCod.setFractionDigits(0);
		wnfWochenCow.setMandatoryField(true);
		wnfWochenCow.setFractionDigits(0);
		
		
		wlaTage.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.forecast.tage"));
		wlaTage.setHorizontalAlignment(SwingConstants.LEFT);
		
		wlaWochen.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.forecast.wochen"));
		wlaWochen.setHorizontalAlignment(SwingConstants.LEFT);
		
		wlaMonate.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.forecast.monate"));
		wlaMonate.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		wbuKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

	

		ForecastartDto fcaDto = DelegateFactory
				.getInstance()
				.getForecastDelegate()
				.forecastartFindByPrimaryKey(
						ForecastFac.FORECASTART_FORECASTAUFTRAG);
		wlaMonateFca.setText(fcaDto.getBezeichnung());
		wlaForecastartForecastauftrag.setText(fcaDto.getBezeichnung());

		fcaDto = DelegateFactory
				.getInstance()
				.getForecastDelegate()
				.forecastartFindByPrimaryKey(
						ForecastFac.FORECASTART_CALL_OFF_WOCHE);
		wlaWochenCow.setText(fcaDto.getBezeichnung());
		wlaForecastartCow.setText(fcaDto.getBezeichnung());

		fcaDto = DelegateFactory
				.getInstance()
				.getForecastDelegate()
				.forecastartFindByPrimaryKey(
						ForecastFac.FORECASTART_CALL_OFF_TAG);
		wlaTageCod.setText(fcaDto.getBezeichnung());
		wlaForecastartCod.setText(fcaDto.getBezeichnung());

		wlaForecastartLinienabruf.setText(LPMain.getInstance()
				.getTextRespectUISPr("fc.linienabrufe"));

		wlaImportdef.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.importdef"));

		wcoImportdefForecastauftrag.setMap(DelegateFactory.getInstance()
				.getForecastDelegate().getAllImportdef());
		wcoImportdefCow.setMap(DelegateFactory.getInstance()
				.getForecastDelegate().getAllImportdef());
		wcoImportdefCod.setMap(DelegateFactory.getInstance()
				.getForecastDelegate().getAllImportdef());
		wcoImportdefLinienabruf.setMap(DelegateFactory.getInstance()
				.getForecastDelegate().getAllImportdef());

		wbuPfadForecastauftrag
				.setActionCommand(ACTION_SPECIAL_PFAD_FORECASTAUFTRAG);
		wbuPfadForecastauftrag.addActionListener(this);

		wbuPfadCod.setActionCommand(ACTION_SPECIAL_PFAD_COD);
		wbuPfadCod.addActionListener(this);

		wbuPfadCow.setActionCommand(ACTION_SPECIAL_PFAD_COW);
		wbuPfadCow.addActionListener(this);

		wbuPfadLinienabruf.setActionCommand(ACTION_SPECIAL_PFAD_LINIENABRUF);
		wbuPfadLinienabruf.addActionListener(this);

		wbuImport.setActionCommand(ACTION_SPECIAL_IMPORT);
		wbuImport.addActionListener(this);
		
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaNummer, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfNummer, new GridBagConstraints(1, iZeile, 3, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 3, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
	
		iZeile++;
		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaTageCod, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfTageCod, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(10, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wlaTage, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(10, 70, 2, 2), 50, 0));
		
		jpaWorkingOn.add(wlaGueltigTage, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfTageGueltig, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaWochenCow, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfWochenCow, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wlaWochen, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 70, 2, 2), 50, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMonateFca, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfMonateFca, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));
		
		jpaWorkingOn.add(wlaMonate, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 70, 2, 2), 50, 0));
		
		
		
		iZeile++;

		jpaWorkingOn.add(wlaImportdef, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(20, 2, 2, 2), 0, 0));

		iZeile++;
	

		
		jpaWorkingOn.add(wlaForecastartForecastauftrag, new GridBagConstraints(
				0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 300, 0));

		jpaWorkingOn
				.add(wtfPfadForecastauftrag, new GridBagConstraints(1, iZeile,
						1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 40),
						400, 0));

		jpaWorkingOn.add(wbuPfadForecastauftrag, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(wcoImportdefForecastauftrag, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 500, 0));

//		iZeile++;
//		
//		jpaWorkingOn.add(wlaForecastartLinienabruf, new GridBagConstraints(0,
//				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
//
//		jpaWorkingOn.add(wtfPfadLinienabruf, new GridBagConstraints(1, iZeile,
//				1, 1, 0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 40), 0, 0));
//
//		jpaWorkingOn.add(wbuPfadLinienabruf, new GridBagConstraints(1, iZeile,
//				1, 1, 0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
//				new Insets(2, 2, 2, 2), 20, 0));
//
//		jpaWorkingOn.add(wcoImportdefLinienabruf, new GridBagConstraints(3,
//				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);
		getToolBar().addButtonLeft("/com/lp/client/res/import1.png",
				LPMain.getTextRespectUISPr("fc.forecast.import"),
				ACTION_SPECIAL_IMPORT, null,
				RechteFac.RECHT_FC_FORECAST_CUD);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		forecastDto = new ForecastDto();
		forecastDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

		getInternalFrameForecast().getTpForecast().setForecastDto(forecastDto);
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		String cmd = e.getActionCommand();

		if (ACTION_SPECIAL_KUNDE_FROM_LISTE.equals(cmd)) {
			dialogQueryKundeFromListe(e);
		}
		
		if (ACTION_SPECIAL_PFAD_FORECASTAUFTRAG.equals(cmd)) {
//			File[] files = HelperClient.chooseFile(this,
//					HelperClient.FILE_FILTER_EDI, false);
//			if (files != null && files.length > 0) {
//				wtfPfadForecastauftrag.setText(files[0].getAbsolutePath());
//			}
			String baseDirectory = 
					wtfPfadForecastauftrag.getText() != null 
						? wtfPfadForecastauftrag.getText()
						: LPMain.getInstance().getLastImportDirectory();
			HvOptional<DirectoryFile> f = HelperClient.chooseDirectoryNew(this, baseDirectory);
			if(f.isPresent()) {
				wtfPfadForecastauftrag.setText(f.get().getDirectory().getAbsolutePath());				
			}
						
/*						
			File f = HelperClient.chooseDirectory(this, baseDirectory);
			if (f != null) {
				wtfPfadForecastauftrag.setText(f.getAbsolutePath());
			}
*/			
		}
//		if (ACTION_SPECIAL_PFAD_COD.equals(cmd)) {
//			File[] files = HelperClient.chooseFile(this,
//					HelperClient.FILE_FILTER_EDI, false);
//			if (files != null && files.length > 0) {
//				wtfPfadCod.setText(files[0].getAbsolutePath());
//			}
//		}
//		
//		if (ACTION_SPECIAL_PFAD_COW.equals(cmd)) {
//			File[] files = HelperClient.chooseFile(this,
//					HelperClient.FILE_FILTER_EDI, false);
//			if (files != null && files.length > 0) {
//				wtfPfadCow.setText(files[0].getAbsolutePath());
//			}
//		}
//		if (ACTION_SPECIAL_PFAD_LINIENABRUF.equals(cmd)) {
//			File[] files = HelperClient.chooseFile(this,
//					HelperClient.FILE_FILTER_EDI, false);
//			if (files != null && files.length > 0) {
//				wtfPfadLinienabruf.setText(files[0].getAbsolutePath());
//			}
//		}

		if(ACTION_SPECIAL_IMPORT.equals(cmd)) {
			importCalloffEdifact();
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_FORECAST;
	}

	protected void setDefaults() throws Throwable {

		wcoImportdefCod.setKeyOfSelectedItem(null);
		wcoImportdefCow.setKeyOfSelectedItem(null);
		wcoImportdefForecastauftrag.setKeyOfSelectedItem(null);
		wcoImportdefLinienabruf.setKeyOfSelectedItem(null);
	}

	protected void components2Dto() throws ExceptionLP {
		forecastDto.setCProjekt(wtfProjekt.getText());
		forecastDto.setCNr(wtfNummer.getText());

		forecastDto.setCPfadCod(wtfPfadCod.getText());
		forecastDto.setCPfadCow(wtfPfadCow.getText());
		forecastDto.setCPfadForecastauftrag(wtfPfadForecastauftrag.getText());
		forecastDto.setCPfadLinienabruf(wtfPfadLinienabruf.getText());

		forecastDto.setImportdefCNrCod((String) wcoImportdefCod
				.getKeyOfSelectedItem());
		forecastDto.setImportdefCNrCow((String) wcoImportdefCow
				.getKeyOfSelectedItem());
		forecastDto
				.setImportdefCNrForecastauftrag((String) wcoImportdefForecastauftrag
						.getKeyOfSelectedItem());
		forecastDto.setImportdefCNrLinienabruf((String) wcoImportdefLinienabruf
				.getKeyOfSelectedItem());
		
		forecastDto.setITageCod(wnfTageCod.getInteger());
		forecastDto.setIWochenCow(wnfWochenCow.getInteger());
		forecastDto.setIMonateFca(wnfMonateFca.getInteger());
		forecastDto.setITageGueltig(wnfTageGueltig.getInteger());
		
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getForecastDelegate()
				.removeForecast(forecastDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (forecastDto.getIId() == null) {
				forecastDto.setIId(DelegateFactory.getInstance()
						.getForecastDelegate().createForecast(forecastDto));
				setKeyWhenDetailPanel(forecastDto.getIId());
				forecastDto = DelegateFactory.getInstance()
						.getForecastDelegate()
						.forecastFindByPrimaryKey(forecastDto.getIId());

				internalFrameForecast.getTpForecast().setForecastDto(
						forecastDto);
			} else {
				DelegateFactory.getInstance().getForecastDelegate()
						.updateForecast(forecastDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						forecastDto.getIId().toString());
			}

			eventYouAreSelected(false);

			forecastDto = DelegateFactory.getInstance().getForecastDelegate()
					.forecastFindByPrimaryKey(forecastDto.getIId());
			internalFrameForecast.getTpForecast().setForecastDto(forecastDto);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey((Integer) key);

				wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
				forecastDto.setKundeIId(kundeDto.getIId());

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void importCalloffEdifact() throws ExceptionLP, IOException,
			Throwable {
		File f = new File(wtfPfadForecastauftrag.getText());
		File[] localFiles = f.listFiles(new FileFilter() {		
			@Override
			public boolean accept(File pathname) {
				String s = pathname.getName().toLowerCase();
				if(s.endsWith(".edi")) return true;
				return false;
			}
		});
		myLogger.info("Found '" + (localFiles == null ? 0 : localFiles.length) + "' files to import forecastcalloff (edi).");

		if(localFiles == null || localFiles.length == 0) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getMessageTextRespectUISPr(
							"fc.forecast.import.keinedateien", f.getAbsolutePath(), "*.edi"));
			return;
		}
		
//		List<byte[]> ediContents = new ArrayList<byte[]>();
//		for (File file : localFiles) {
//			ediContents.add(readFile(file));
//		}
//

		ICallOffXlsImportController importController = new CallOffEdifactDelforImportController(
				forecastDto.getIId(), localFiles);
		DialogCallOffXlsImport dialog = new DialogCallOffXlsImport(LPMain
				.getInstance().getDesktop(), true, importController);
		dialog.setVisible(true);		
	}
}
