
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
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.AuszahlungBVADto;
import com.lp.server.personal.service.GleitzeitsaldoDto;
import com.lp.server.personal.service.UebertragBVADto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelAuszahlungBVA extends PanelBasis {

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
	private InternalFramePersonal internalFramePersonal = null;
	private AuszahlungBVADto auszahlungBVADto = null;
	
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();

	private WrapperLabel wlaGleitzeit = new WrapperLabel();
	private WrapperNumberField wnfGleitzeit = new WrapperNumberField();

	private WrapperLabel wlaUest50Gz = new WrapperLabel();
	private WrapperNumberField wnfUest50Gz = new WrapperNumberField();

	private WrapperLabel wlaUest50Gz_Zuschlag = new WrapperLabel();
	private WrapperNumberField wnfUest50Gz_Zuschlag = new WrapperNumberField();

	private WrapperLabel wlaUest50 = new WrapperLabel();
	private WrapperNumberField wnfUest50 = new WrapperNumberField();

	private WrapperLabel wlaUest50Zuschlag = new WrapperLabel();
	private WrapperNumberField wnfUest50_Zuschlag = new WrapperNumberField();

	private WrapperLabel wlaUest100 = new WrapperLabel();
	private WrapperNumberField wnfUest100 = new WrapperNumberField();

	private WrapperLabel wlaUest100Zuschlag = new WrapperLabel();
	private WrapperNumberField wnfUest100Zuschlag = new WrapperNumberField();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField(80);
	
	public PanelAuszahlungBVA(InternalFrame internalFrame, String add2TitleI,
			Object pk)

	throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfGleitzeit;
	}

	protected void setDefaults() {

		

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		auszahlungBVADto = new AuszahlungBVADto();

		leereAlleFelder(this);



	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.removeAuszahlungBVA(auszahlungBVADto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		auszahlungBVADto.setPersonalIId(internalFramePersonal.getPersonalDto()
				.getIId());
		

		auszahlungBVADto.setTDatum(Helper.cutTimestamp(wdfDatum.getTimestamp()));

		auszahlungBVADto.setNGleitzeit(wnfGleitzeit.getBigDecimal());

		auszahlungBVADto.setNUestd50Gz(wnfUest50Gz.getBigDecimal());
		auszahlungBVADto.setNUestd50Gz_Zuschlag(wnfUest50Gz_Zuschlag
				.getBigDecimal());
		auszahlungBVADto.setNUestd50(wnfUest50.getBigDecimal());
		auszahlungBVADto
				.setNUestd50_Zuschlag(wnfUest50_Zuschlag.getBigDecimal());
		auszahlungBVADto.setNUestd100(wnfUest100.getBigDecimal());
		auszahlungBVADto.setNUestd100_Zuschlag(wnfUest100Zuschlag
				.getBigDecimal());
		auszahlungBVADto.setCKommentar(wtfKommentar.getText());

		

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

	
		wdfDatum.setTimestamp(auszahlungBVADto.getTDatum());
		wnfGleitzeit.setBigDecimal(auszahlungBVADto.getNGleitzeit());

		wnfUest50Gz.setBigDecimal(auszahlungBVADto.getNUestd50Gz());
		wnfUest50Gz_Zuschlag.setBigDecimal(auszahlungBVADto
				.getNUestd50Gz_Zuschlag());
		wnfUest50.setBigDecimal(auszahlungBVADto.getNUestd50());
		wnfUest50_Zuschlag
				.setBigDecimal(auszahlungBVADto.getNUestd50_Zuschlag());
		wnfUest100.setBigDecimal(auszahlungBVADto.getNUestd100());
		wnfUest100Zuschlag.setBigDecimal(auszahlungBVADto
				.getnUestd100_Zuschlag());
		
		wtfKommentar.setText(auszahlungBVADto.getCKommentar());

		

		this.setStatusbarPersonalIIdAendern(auszahlungBVADto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(auszahlungBVADto.getTAendern());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (auszahlungBVADto.getIId() == null) {
				auszahlungBVADto.setIId(DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.createAuszahlungBVA(auszahlungBVADto));
				setKeyWhenDetailPanel(auszahlungBVADto.getIId());
			} else {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.updateAuszahlungBVADto(auszahlungBVADto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFramePersonal.getPersonalDto().getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
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

		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));
		
		
		
		
		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		wdfDatum.setMandatoryField(true);
		wlaGleitzeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.gleitzeitsaldo"));

		
		wnfGleitzeit.setMandatoryField(true);

		wnfUest50Gz.setMandatoryField(true);
		wnfUest50Gz_Zuschlag.setMandatoryField(true);
		wnfUest50.setMandatoryField(true);
		wnfUest50_Zuschlag.setMandatoryField(true);
		wnfUest100.setMandatoryField(true);
		wnfUest100Zuschlag.setMandatoryField(true);

		
		
		
		wlaUest50Gz.setText(LPMain.getInstance().getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.gz"));
		wlaUest50Gz_Zuschlag.setText(LPMain.getInstance().getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.gz.zuschlag"));
		wlaUest50.setText(LPMain.getInstance().getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.uestd50"));
		wlaUest50Zuschlag.setText(LPMain.getInstance().getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.uestd50.zuschlag"));
		wlaUest100.setText(LPMain.getInstance().getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.uestd100"));
		wlaUest100Zuschlag.setText(LPMain.getInstance().getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.uestd100.zuschlag"));

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
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1, 0.01,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 70, 0));
	iZeile++;
		jpaWorkingOn.add(wlaGleitzeit, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGleitzeit, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel w1 = new WrapperLabel("h");
		w1.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(w1, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUest50Gz, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUest50Gz, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel w2 = new WrapperLabel("h");
		w2.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(w2, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUest50Gz_Zuschlag, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUest50Gz_Zuschlag, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel w3 = new WrapperLabel("h");
		w3.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(w3, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUest50, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUest50, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel w4 = new WrapperLabel("h");
		w4.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(w4, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUest50Zuschlag, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUest50_Zuschlag, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel w5 = new WrapperLabel("h");
		w5.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(w5, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUest100, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUest100, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel w6 = new WrapperLabel("h");
		w6.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(w6, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUest100Zuschlag, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUest100Zuschlag, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel w7 = new WrapperLabel("h");
		w7.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(w7, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			wdfDatum.setDate(new Date());
			
			wnfGleitzeit.setInteger(0);
			wnfUest50Gz.setInteger(0);
			wnfUest50Gz_Zuschlag.setInteger(0);
			wnfUest50.setInteger(0);
			wnfUest50_Zuschlag.setInteger(0);
			wnfUest100.setInteger(0);
			wnfUest100Zuschlag.setInteger(0);
			
		} else {
			auszahlungBVADto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.auszahlungBVADtoFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}
}
