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
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.PruefkombinationsprDto;
import com.lp.server.stueckliste.service.StuecklisteFac;

public class PanelPruefkombination extends PanelBasis {

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
	private PruefkombinationDto pruefkombinationDto = null;

	private WrapperSelectField wsfArtikel_Kontakt = new WrapperSelectField(
			WrapperSelectField.ARTIKEL_OHNE_ARBEISZEIT, getInternalFrame(),
			false);
	private WrapperSelectField wsfArtikel_Litze = new WrapperSelectField(
			WrapperSelectField.ARTIKEL_OHNE_ARBEISZEIT, getInternalFrame(),
			false);

	private WrapperSelectField wsfArtikel_Litze2 = new WrapperSelectField(
			WrapperSelectField.ARTIKEL_OHNE_ARBEISZEIT, getInternalFrame(),
			false);

	private WrapperSelectField wsfVerschleissteil = new WrapperSelectField(
			WrapperSelectField.VERSCHLEISSTEIL, getInternalFrame(), false);
	private WrapperCheckBox wcbStandard = new WrapperCheckBox();
	private WrapperCheckBox wcbDoppelanschlag = new WrapperCheckBox();

	private WrapperComboBox wcoPruefart = new WrapperComboBox();

	private WrapperLabel wlaCrimphoeheDraht = new WrapperLabel();
	private WrapperLabel wlaCrimphoeheInsolation = new WrapperLabel();
	private WrapperLabel wlaCrimpBreiteDraht = new WrapperLabel();
	private WrapperLabel wlaCrimpBreiteInsolation = new WrapperLabel();

	private WrapperNumberField wnfCrimphoeheDraht = new WrapperNumberField();
	private WrapperNumberField wnfCrimphoeheInsolation = new WrapperNumberField();
	private WrapperNumberField wnfCrimpBreiteDraht = new WrapperNumberField();
	private WrapperNumberField wnfCrimpBreiteInsolation = new WrapperNumberField();

	private WrapperLabel wlaAbzugskraft1 = new WrapperLabel();
	private WrapperLabel wlaAbzugskraft2 = new WrapperLabel();

	private WrapperNumberField wnfAbzugskraft1 = new WrapperNumberField();
	private WrapperNumberField wnfAbzugskraft2 = new WrapperNumberField();

	private WrapperLabel wlaToleranz1 = new WrapperLabel();
	private WrapperLabel wlaToleranz2 = new WrapperLabel();

	private WrapperLabel wlaEinheitWert = new WrapperLabel();
	private WrapperLabel wlaWert = new WrapperLabel();

	private WrapperLabel wlaEinheitMM1 = new WrapperLabel("mm");
	private WrapperLabel wlaEinheitMM2 = new WrapperLabel("mm");
	private WrapperLabel wlaEinheitMM3 = new WrapperLabel("mm");
	private WrapperLabel wlaEinheitMM4 = new WrapperLabel("mm");

	private WrapperNumberField wnfToleranzCrimphoeheDraht = new WrapperNumberField();
	private WrapperNumberField wnfToleranzCrimphoeheInsolation = new WrapperNumberField();
	private WrapperNumberField wnfToleranzCrimpBreiteDraht = new WrapperNumberField();
	private WrapperNumberField wnfToleranzCrimpBreiteInsolation = new WrapperNumberField();

	private WrapperNumberField wnfWert = new WrapperNumberField();
	private WrapperNumberField wnfToleranzWert = new WrapperNumberField();

	private WrapperTextField wtfKommentar = new WrapperTextField(3000);

	private PanelQuery pq = null;

	public PanelPruefkombination(InternalFrame internalFrame,
			String add2TitleI, Object pk, PanelQuery pq) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.pq = pq;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wcoPruefart.setMandatoryField(true);
		Map<?, ?> m = DelegateFactory.getInstance().getStuecklisteDelegate()
				.getAllPruefartenFuerPruefkombinationen();
		wcoPruefart.setMap(m);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfArtikel_Kontakt.getWrapperButton();
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		pruefkombinationDto = new PruefkombinationDto();
	}

	protected void dto2Components() throws Throwable {
		wsfArtikel_Kontakt.setKey(pruefkombinationDto.getArtikelIIdKontakt());
		wsfArtikel_Litze.setKey(pruefkombinationDto.getArtikelIIdLitze());

		wsfArtikel_Litze2.setKey(pruefkombinationDto.getArtikelIIdLitze2());

		wnfAbzugskraft1.setBigDecimal(pruefkombinationDto
				.getNAbzugskraftLitze());
		wnfAbzugskraft2.setBigDecimal(pruefkombinationDto
				.getNAbzugskraftLitze2());

		wsfVerschleissteil.setKey(pruefkombinationDto.getVerschleissteilIId());
		wcbStandard.setShort(pruefkombinationDto.getBStandard());
		wnfCrimpBreiteDraht.setBigDecimal(pruefkombinationDto
				.getNCrimpbreitDraht());
		wnfCrimphoeheDraht.setBigDecimal(pruefkombinationDto
				.getNCrimphoeheDraht());
		wnfCrimphoeheInsolation.setBigDecimal(pruefkombinationDto
				.getNCrimphoeheIsolation());
		wnfCrimpBreiteInsolation.setBigDecimal(pruefkombinationDto
				.getNCrimpbreiteIsolation());

		wnfToleranzCrimpBreiteDraht.setBigDecimal(pruefkombinationDto
				.getNToleranzCrimpbreitDraht());
		wnfToleranzCrimphoeheDraht.setBigDecimal(pruefkombinationDto
				.getNToleranzCrimphoeheDraht());
		wnfToleranzCrimphoeheInsolation.setBigDecimal(pruefkombinationDto
				.getNToleranzCrimphoeheIsolation());
		wnfToleranzCrimpBreiteInsolation.setBigDecimal(pruefkombinationDto
				.getNToleranzCrimpbreiteIsolation());

		wnfWert.setBigDecimal(pruefkombinationDto.getNWert());

		wnfToleranzWert.setBigDecimal(pruefkombinationDto.getNToleranzWert());

		wcbDoppelanschlag.setShort(pruefkombinationDto.getBDoppelanschlag());

		wcoPruefart.setKeyOfSelectedItem(pruefkombinationDto.getPruefartIId());

		if (pruefkombinationDto.getPruefkombinationsprDto() != null) {
			wtfKommentar.setText(pruefkombinationDto
					.getPruefkombinationsprDto().getCBez());
		} else {
			wtfKommentar.setText(null);
		}

		setStatusbarPersonalIIdAendern(pruefkombinationDto
				.getPersonalIIdAendern());
		setStatusbarPersonalIIdAnlegen(pruefkombinationDto
				.getPersonalIIdAnlegen());

		setStatusbarTAendern(pruefkombinationDto.getTAendern());
		setStatusbarTAnlegen(pruefkombinationDto.getTAnlegen());

	}

	protected void components2Dto() throws Throwable {
		pruefkombinationDto.setPruefartIId((Integer) wcoPruefart
				.getKeyOfSelectedItem());
		pruefkombinationDto.setArtikelIIdKontakt(wsfArtikel_Kontakt.getIKey());
		pruefkombinationDto.setArtikelIIdLitze(wsfArtikel_Litze.getIKey());
		pruefkombinationDto.setVerschleissteilIId(wsfVerschleissteil.getIKey());
		pruefkombinationDto.setBStandard(wcbStandard.getShort());

		pruefkombinationDto.setNCrimpbreitDraht(wnfCrimpBreiteDraht
				.getBigDecimal());
		pruefkombinationDto.setNCrimphoeheDraht(wnfCrimphoeheDraht
				.getBigDecimal());
		pruefkombinationDto.setNCrimphoeheIsolation(wnfCrimphoeheInsolation
				.getBigDecimal());
		pruefkombinationDto.setNCrimpbreiteIsolation(wnfCrimpBreiteInsolation
				.getBigDecimal());

		pruefkombinationDto
				.setNToleranzCrimpbreitDraht(wnfToleranzCrimpBreiteDraht
						.getBigDecimal());
		pruefkombinationDto
				.setNToleranzCrimphoeheDraht(wnfToleranzCrimphoeheDraht
						.getBigDecimal());
		pruefkombinationDto
				.setNToleranzCrimphoeheIsolation(wnfToleranzCrimphoeheInsolation
						.getBigDecimal());
		pruefkombinationDto
				.setNToleranzCrimpbreiteIsolation(wnfToleranzCrimpBreiteInsolation
						.getBigDecimal());

		pruefkombinationDto.setNWert(wnfWert.getBigDecimal());

		pruefkombinationDto.setNToleranzWert(wnfToleranzWert.getBigDecimal());

		if (pruefkombinationDto.getPruefkombinationsprDto() == null) {
			pruefkombinationDto
					.setPruefkombinationsprDto(new PruefkombinationsprDto());
		}
		pruefkombinationDto.getPruefkombinationsprDto().setCBez(
				wtfKommentar.getText());
		pruefkombinationDto.setBDoppelanschlag(wcbDoppelanschlag.getShort());

		if (wcbDoppelanschlag.isSelected()) {
			pruefkombinationDto
					.setArtikelIIdLitze2(wsfArtikel_Litze2.getIKey());
		} else {
			pruefkombinationDto.setArtikelIIdLitze2(null);
		}

		pruefkombinationDto.setNAbzugskraftLitze(wnfAbzugskraft1
				.getBigDecimal());
		pruefkombinationDto.setNAbzugskraftLitze2(wnfAbzugskraft2
				.getBigDecimal());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
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

		wlaToleranz1.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.toleranz"));
		wlaToleranz2.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.toleranz"));

		wlaWert.setText(LPMain.getTextRespectUISPr("stk.pruefkombination.wert"));

		wcbStandard.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.standard"));
		wlaCrimpBreiteDraht.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.crimpbreitedraht"));
		wlaCrimphoeheDraht.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.crimphoehedraht"));
		wlaCrimphoeheInsolation
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.crimphoeheisolation"));
		wlaCrimpBreiteInsolation
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.crimpbreiteisolation"));

		wcbDoppelanschlag.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.doppelanschlag"));
		wcbDoppelanschlag.addActionListener(this);
		wlaAbzugskraft1
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.abzugskraft")
						+ " 1");
		wlaAbzugskraft2
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.abzugskraft")
						+ " 2");

		wcoPruefart
				.addActionListener(new PanelPruefkombination_wcbPruefart_actionAdapter(
						this));

		wlaEinheitWert.setHorizontalAlignment(SwingConstants.LEFT);

		wsfArtikel_Kontakt.setMandatoryField(true);

		wsfArtikel_Kontakt.getWrapperGotoButton().setText(
				LPMain.getTextRespectUISPr("stk.pruefkombination.kontakt")
						+ "...");
		wsfArtikel_Litze.getWrapperGotoButton().setText(
				LPMain.getTextRespectUISPr("stk.pruefkombination.litze")
						+ "...");
		wsfArtikel_Litze2.getWrapperGotoButton().setText(
				LPMain.getTextRespectUISPr("stk.pruefkombination.litze")
						+ "2...");

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
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("stkl.pruefart")),
				new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wcoPruefart, new GridBagConstraints(1, iZeile, 0, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("lp.kommentar")),
				new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 0, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfArtikel_Kontakt.getWrapperGotoButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfArtikel_Kontakt.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 7, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfArtikel_Litze.getWrapperGotoButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfArtikel_Litze.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 7, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfArtikel_Litze2.getWrapperGotoButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfArtikel_Litze2.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 7, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfVerschleissteil.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfVerschleissteil.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 7, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;

		jpaWorkingOn.add(wcbStandard, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 80, 0));

		jpaWorkingOn.add(wlaToleranz1, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbDoppelanschlag, new GridBagConstraints(4, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 80, 0));

		jpaWorkingOn.add(wlaToleranz2, new GridBagConstraints(6, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaCrimphoeheDraht, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfCrimphoeheDraht, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfToleranzCrimphoeheDraht, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wlaEinheitMM1, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));

		jpaWorkingOn.add(wlaCrimphoeheInsolation, new GridBagConstraints(4,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfCrimphoeheInsolation, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfToleranzCrimphoeheInsolation,
				new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wlaEinheitMM2, new GridBagConstraints(7, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));

		iZeile++;
		jpaWorkingOn.add(wlaCrimpBreiteDraht, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfCrimpBreiteDraht, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wnfToleranzCrimpBreiteDraht, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wlaEinheitMM3, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaCrimpBreiteInsolation, new GridBagConstraints(4,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfCrimpBreiteInsolation, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfToleranzCrimpBreiteInsolation,
				new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wlaEinheitMM4, new GridBagConstraints(7, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));

		iZeile++;
		jpaWorkingOn.add(wlaWert, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWert, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wnfToleranzWert, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wlaEinheitWert, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));

		iZeile++;
		jpaWorkingOn.add(wlaAbzugskraft1, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfAbzugskraft1, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wlaAbzugskraft2, new GridBagConstraints(4, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfAbzugskraft2, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wcbDoppelanschlag)) {
			if (wcbDoppelanschlag.isSelected()) {
				wsfArtikel_Litze2.getWrapperTextField().setVisible(true);
				wsfArtikel_Litze2.getWrapperGotoButton().setVisible(true);
				wsfArtikel_Litze2.setMandatoryField(true);
				wlaAbzugskraft2.setVisible(true);
				wnfAbzugskraft2.setVisible(true);
			} else {
				wsfArtikel_Litze2.getWrapperTextField().setVisible(false);
				wsfArtikel_Litze2.getWrapperGotoButton().setVisible(false);
				wsfArtikel_Litze2.setMandatoryField(false);
				wlaAbzugskraft2.setVisible(false);
				wnfAbzugskraft2.setVisible(false);
			}
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable, Throwable {
		DelegateFactory.getInstance().getStuecklisteDelegate()
				.removePruefkombination(pruefkombinationDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
			wsfArtikel_Litze2.getWrapperTextField().setVisible(
					wcbDoppelanschlag.isSelected());
			wsfArtikel_Litze2.getWrapperGotoButton().setVisible(
					wcbDoppelanschlag.isSelected());
			wsfArtikel_Litze2.setMandatoryField(wcbDoppelanschlag.isSelected());
			wlaAbzugskraft2.setVisible(wcbDoppelanschlag.isSelected());
			wnfAbzugskraft2.setVisible(wcbDoppelanschlag.isSelected());

		} else {
			pruefkombinationDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.pruefkombinationFindByPrimaryKey((Integer) key);
			dto2Components();
		}

	}

	protected PropertyVetoException eventActionVetoableChangeLP()
			throws Throwable {
		PropertyVetoException pve = super.eventActionVetoableChangeLP();
		DelegateFactory.getInstance().getStuecklisteDelegate()
				.removeLockDerPruefkombinationWennIchIhnSperre();
		return pve;
	}

	public void wcbPruefart_actionPerformed(ActionEvent e) {
		Integer pruefartIId = (Integer) wcoPruefart.getKeyOfSelectedItem();

		try {

			// Zuerst alle unsichtbar und nicht Mandantory
			wsfArtikel_Kontakt.setMandatoryField(false);
			wsfArtikel_Kontakt.getWrapperGotoButton().setVisible(false);
			wsfArtikel_Kontakt.getWrapperTextField().setVisible(false);

			wcbDoppelanschlag.setVisible(false);

			wsfArtikel_Litze.setMandatoryField(false);
			wsfArtikel_Litze.getWrapperGotoButton().setVisible(false);
			wsfArtikel_Litze.getWrapperTextField().setVisible(false);

			wsfArtikel_Litze2.setMandatoryField(false);
			wsfArtikel_Litze2.getWrapperGotoButton().setVisible(false);
			wsfArtikel_Litze2.getWrapperTextField().setVisible(false);

			wlaAbzugskraft1.setVisible(false);
			wnfAbzugskraft1.setVisible(false);

			wlaAbzugskraft2.setVisible(false);
			wnfAbzugskraft2.setVisible(false);

			wsfVerschleissteil.getWrapperButton().setVisible(false);
			wsfVerschleissteil.getWrapperTextField().setVisible(false);
			wsfVerschleissteil.setMandatoryField(false);

			wlaEinheitMM1.setVisible(false);
			wlaEinheitMM2.setVisible(false);
			wlaEinheitMM3.setVisible(false);
			wlaEinheitMM4.setVisible(false);

			wnfCrimpBreiteDraht.setVisible(false);
			wnfCrimpBreiteInsolation.setVisible(false);
			wnfCrimphoeheDraht.setVisible(false);
			wnfCrimphoeheInsolation.setVisible(false);

			wnfToleranzCrimpBreiteDraht.setVisible(false);
			wnfToleranzCrimpBreiteInsolation.setVisible(false);
			wnfToleranzCrimphoeheDraht.setVisible(false);
			wnfToleranzCrimphoeheInsolation.setVisible(false);

			wlaCrimpBreiteDraht.setVisible(false);
			wlaCrimpBreiteInsolation.setVisible(false);
			wlaCrimphoeheDraht.setVisible(false);
			wlaCrimphoeheInsolation.setVisible(false);

			wnfCrimpBreiteDraht.setMandatoryField(false);
			wnfCrimpBreiteInsolation.setMandatoryField(false);
			wnfCrimphoeheDraht.setMandatoryField(false);
			wnfCrimphoeheInsolation.setMandatoryField(false);

			wlaWert.setVisible(false);
			wnfWert.setVisible(false);
			wnfWert.setMandatoryField(false);
			wnfToleranzWert.setVisible(false);
			wlaEinheitWert.setVisible(false);

			wlaToleranz1.setVisible(false);
			wlaToleranz2.setVisible(false);

			wsfArtikel_Kontakt.getWrapperGotoButton().setText(
					LPMain.getTextRespectUISPr("stk.pruefkombination.kontakt")
							+ "...");

			wlaWert.setText(LPMain
					.getTextRespectUISPr("stk.pruefkombination.wert"));

			String sPruefart = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.pruefartFindByPrimaryKey(pruefartIId).getCNr();

			if (sPruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
					|| sPruefart
							.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {

				wsfArtikel_Kontakt.setMandatoryField(true);
				wsfArtikel_Kontakt.getWrapperGotoButton().setVisible(true);
				wsfArtikel_Kontakt.getWrapperTextField().setVisible(true);

				wlaAbzugskraft1.setVisible(true);
				wlaAbzugskraft2.setVisible(true);
				wnfAbzugskraft1.setVisible(true);
				wnfAbzugskraft2.setVisible(true);

				wcbDoppelanschlag.setVisible(true);

				wsfArtikel_Litze.setMandatoryField(true);
				wsfArtikel_Litze.getWrapperGotoButton().setVisible(true);
				wsfArtikel_Litze.getWrapperTextField().setVisible(true);

				wsfArtikel_Litze2.setMandatoryField(true);
				wsfArtikel_Litze2.getWrapperGotoButton().setVisible(true);
				wsfArtikel_Litze2.getWrapperTextField().setVisible(true);

				wsfVerschleissteil.getWrapperButton().setVisible(true);
				wsfVerschleissteil.getWrapperTextField().setVisible(true);
				wsfVerschleissteil.setMandatoryField(true);
			}

			if (sPruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {
				wlaEinheitMM1.setVisible(true);
				wlaEinheitMM2.setVisible(true);
				wlaEinheitMM3.setVisible(true);
				wlaEinheitMM4.setVisible(true);

				wnfCrimpBreiteDraht.setVisible(true);
				wnfCrimpBreiteInsolation.setVisible(true);
				wnfCrimphoeheDraht.setVisible(true);
				wnfCrimphoeheInsolation.setVisible(true);

				wnfToleranzCrimpBreiteDraht.setVisible(true);
				wnfToleranzCrimpBreiteInsolation.setVisible(true);
				wnfToleranzCrimphoeheDraht.setVisible(true);
				wnfToleranzCrimphoeheInsolation.setVisible(true);

				wlaCrimpBreiteDraht.setVisible(true);
				wlaCrimpBreiteInsolation.setVisible(true);
				wlaCrimphoeheDraht.setVisible(true);
				wlaCrimphoeheInsolation.setVisible(true);

				wnfCrimpBreiteDraht.setMandatoryField(true);
				wnfCrimpBreiteInsolation.setMandatoryField(true);
				wnfCrimphoeheDraht.setMandatoryField(true);
				wnfCrimphoeheInsolation.setMandatoryField(true);
				wlaToleranz1.setVisible(true);
				wlaToleranz2.setVisible(true);

				wsfArtikel_Litze2.getWrapperTextField().setVisible(
						wcbDoppelanschlag.isSelected());
				wsfArtikel_Litze2.getWrapperGotoButton().setVisible(
						wcbDoppelanschlag.isSelected());
				wsfArtikel_Litze2.setMandatoryField(wcbDoppelanschlag
						.isSelected());

				wlaAbzugskraft2.setVisible(wcbDoppelanschlag.isSelected());
				wnfAbzugskraft2.setVisible(wcbDoppelanschlag.isSelected());

			} else if (sPruefart
					.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
				wlaEinheitMM1.setVisible(true);
				wlaEinheitMM3.setVisible(true);

				wnfCrimpBreiteDraht.setVisible(true);

				wnfCrimphoeheDraht.setVisible(true);

				wnfToleranzCrimpBreiteDraht.setVisible(true);

				wnfToleranzCrimphoeheDraht.setVisible(true);

				wlaCrimpBreiteDraht.setVisible(true);

				wlaCrimphoeheDraht.setVisible(true);

				wnfCrimpBreiteDraht.setMandatoryField(true);

				wnfCrimphoeheDraht.setMandatoryField(true);

				wlaToleranz1.setVisible(true);

				wsfArtikel_Litze2.getWrapperTextField().setVisible(
						wcbDoppelanschlag.isSelected());
				wsfArtikel_Litze2.getWrapperGotoButton().setVisible(
						wcbDoppelanschlag.isSelected());
				wsfArtikel_Litze2.setMandatoryField(wcbDoppelanschlag
						.isSelected());

				wlaAbzugskraft2.setVisible(wcbDoppelanschlag.isSelected());
				wnfAbzugskraft2.setVisible(wcbDoppelanschlag.isSelected());

			} else if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)
					|| sPruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {

				wsfArtikel_Litze.getWrapperGotoButton().setVisible(true);
				wsfArtikel_Litze.getWrapperTextField().setVisible(true);

				if (sPruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
					wsfArtikel_Litze.setMandatoryField(true);
					wlaWert.setVisible(true);
					wnfWert.setVisible(true);
					wnfWert.setMandatoryField(true);
					wlaEinheitWert.setVisible(true);
					wlaWert.setText(LPMain
							.getTextRespectUISPr("stk.pruefkombination.mindestwert"));

					wsfArtikel_Kontakt.setMandatoryField(true);
					wsfArtikel_Kontakt.getWrapperGotoButton().setVisible(true);
					wsfArtikel_Kontakt.getWrapperTextField().setVisible(true);

				} else {

					wnfToleranzWert.setVisible(true);
					wlaEinheitWert.setVisible(true);
					wlaToleranz1.setVisible(true);
				}

				if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {
					wlaEinheitWert.setText("mm");
				} else {
					wlaEinheitWert.setText("N");
				}

			} else if (sPruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {

			} else if (sPruefart
					.equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)
					|| sPruefart
							.equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)) {

				wsfArtikel_Kontakt.getWrapperGotoButton().setVisible(true);
				wsfArtikel_Kontakt.getWrapperTextField().setVisible(true);

				wsfArtikel_Kontakt
						.getWrapperGotoButton()
						.setText(
								LPMain.getTextRespectUISPr("stk.pruefkombination.artikel")
										+ "...");

			}
		} catch (Throwable e1) {
			handleException(e1, true);
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		if (pq.getSelectedIds() != null && pq.getSelectedIds().length > 1) {

			DialogCrimpwerteAendern d = new DialogCrimpwerteAendern(
					pq.getSelectedIds());
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

			if (d.wnfCrimphoeheDraht.getBigDecimal() != null
					&& d.wnfCrimphoeheInsolation.getBigDecimal() != null
					&& d.wnfCrimpBreiteDraht.getBigDecimal() != null
					&& d.wnfCrimpBreiteInsolation.getBigDecimal() != null) {

				for (int i = 0; i < pq.getSelectedIds().length; i++) {
					PruefkombinationDto pkDto = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.pruefkombinationFindByPrimaryKey(
									(Integer) pq.getSelectedIds()[i]);
					pkDto.setNCrimpbreitDraht(d.wnfCrimpBreiteDraht
							.getBigDecimal());
					pkDto.setNCrimpbreiteIsolation(d.wnfCrimpBreiteInsolation
							.getBigDecimal());
					pkDto.setNCrimphoeheDraht(d.wnfCrimphoeheDraht
							.getBigDecimal());
					pkDto.setNCrimphoeheIsolation(d.wnfCrimphoeheInsolation
							.getBigDecimal());

					DelegateFactory.getInstance().getStuecklisteDelegate()
							.updatePruefkombination(pkDto);

				}

			}

		} else {

			super.eventActionUpdate(aE, bNeedNoUpdateI);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (pruefkombinationDto.getIId() == null) {

				pruefkombinationDto.setIId(DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.createPruefkombination(pruefkombinationDto));
				setKeyWhenDetailPanel(pruefkombinationDto.getIId());
			} else {
				DelegateFactory.getInstance().getStuecklisteDelegate()
						.updatePruefkombination(pruefkombinationDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						pruefkombinationDto.getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	class PanelPruefkombination_wcbPruefart_actionAdapter implements
			ActionListener {
		private PanelPruefkombination adaptee;

		PanelPruefkombination_wcbPruefart_actionAdapter(
				PanelPruefkombination adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wcbPruefart_actionPerformed(e);
		}
	}

}
