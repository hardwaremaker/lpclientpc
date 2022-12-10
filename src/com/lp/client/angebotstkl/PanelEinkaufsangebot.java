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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFile;
import com.lp.client.frame.filechooser.open.XlsxFile;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.ImportLumiQuoteXlsxDto;
import com.lp.server.auftrag.service.ImportVATXlsxDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;

@SuppressWarnings("static-access")
public class PanelEinkaufsangebot extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EinkaufsangebotDto einkaufsangebotDto = null;
	private KundeDto kundeDto = null;
	private InternalFrameAngebotstkl internalFrameAngebotstkl = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperGotoButton wbuKunde = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKunde = new WrapperTextField();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperEditorField wefKommentar = null;

	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperTextField wtfProjekt = new WrapperTextField();

	private WrapperLabel wlaMenge1 = new WrapperLabel();
	private WrapperNumberField wnfMenge1 = new WrapperNumberField();
	private WrapperLabel wlaMenge2 = new WrapperLabel();
	private WrapperNumberField wnfMenge2 = new WrapperNumberField();
	private WrapperLabel wlaMenge3 = new WrapperLabel();
	private WrapperNumberField wnfMenge3 = new WrapperNumberField();
	private WrapperLabel wlaMenge4 = new WrapperLabel();
	private WrapperNumberField wnfMenge4 = new WrapperNumberField();
	private WrapperLabel wlaMenge5 = new WrapperLabel();
	private WrapperNumberField wnfMenge5 = new WrapperNumberField();

	private WrapperLabel wlaSummeAbfragen = new WrapperLabel();
	private WrapperNumberField wnfSummeAbfragen = new WrapperNumberField();

	private WrapperLabel wlaFertigungsterminGeplant = new WrapperLabel();
	private WrapperDateField wdfFertigungsterminGeplant = new WrapperDateField();

	private WrapperLabel wlaLieferterminGeplant = new WrapperLabel();
	private WrapperDateField wdfLieferterminGeplant = new WrapperDateField();

	private WrapperCheckBox wcbRohs = new WrapperCheckBox();
	private WrapperCheckBox wcbKundeExportieren = new WrapperCheckBox();

	private WrapperLabel wlaBelegdatum = new WrapperLabel();
	private WrapperDateField wdfBelegdatum = new WrapperDateField();

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kunde_from_liste";
	public static final String ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE = "action_special_ansprechpartner_kunde";
	Integer partnerIId;

	public final static String MY_OWN_NEW_LUMIQUOTE_IMPORT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_LUMIQUOTE_IMPORT";

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return internalFrameAngebotstkl;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKunde;
	}

	public PanelEinkaufsangebot(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameAngebotstkl = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId();

		if (key != null) {
			einkaufsangebotDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.einkaufsangebotFindByPrimaryKey(getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId());

			dto2Components();

			String cBez = "";
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				if (getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCProjekt() != null) {
					cBez = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCProjekt();
				}
			}
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCNr() + ", " + cBez);

		} else {
			leereAlleFelder(this);
			wdfBelegdatum.setDate(new java.sql.Date(System.currentTimeMillis()));
			wnfMenge1.setInteger(0);
			wnfMenge2.setInteger(0);
			wnfMenge3.setInteger(0);
			wnfMenge4.setInteger(0);
			wnfMenge5.setInteger(0);
		}

	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		wtfProjekt.setText(einkaufsangebotDto.getCProjekt());
		wnfMenge1.setBigDecimal(einkaufsangebotDto.getNMenge1());
		wnfMenge2.setBigDecimal(einkaufsangebotDto.getNMenge2());
		wnfMenge3.setBigDecimal(einkaufsangebotDto.getNMenge3());
		wnfMenge4.setBigDecimal(einkaufsangebotDto.getNMenge4());
		wnfMenge5.setBigDecimal(einkaufsangebotDto.getNMenge5());

		wefKommentar.setText(einkaufsangebotDto.getCKommentar());

		wdfLieferterminGeplant.setTimestamp(einkaufsangebotDto.getTLiefertermin());
		wdfFertigungsterminGeplant.setTimestamp(einkaufsangebotDto.getTFertigungstermin());
		wcbRohs.setShort(einkaufsangebotDto.getBRoHs());
		wcbKundeExportieren.setShort(einkaufsangebotDto.getBKundeExportieren());
		wnfSummeAbfragen.setInteger(einkaufsangebotDto.getIAnzahlwebabfragen());

		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(einkaufsangebotDto.getKundeIId());
		// Goto Kunde Ziel setzen
		wbuKunde.setOKey(kundeDto.getIId());
		wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
		partnerIId = kundeDto.getPartnerDto().getIId();
		if (einkaufsangebotDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(einkaufsangebotDto.getAnsprechpartnerIId());
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		wdfBelegdatum.setTimestamp(einkaufsangebotDto.getTBelegdatum());
		this.setStatusbarPersonalIIdAendern(einkaufsangebotDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(einkaufsangebotDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(einkaufsangebotDto.getTAnlegen());
		this.setStatusbarTAendern(einkaufsangebotDto.getTAendern());

	}

	void dialogQueryKundeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true, false,
				einkaufsangebotDto.getKundeIId());
		new DialogQuery(panelQueryFLRKunde);
	}

	void dialogQueryAnsprechartnerFromListe(ActionEvent e) throws Throwable {

		if (partnerIId != null) {
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
					getInternalFrame(), partnerIId, einkaufsangebotDto.getAnsprechpartnerIId(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		} else {
			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("lp.error.kundenichtgewaehlt")); // UW->CK
																								// Konstante
		}
	}

	private void jbInit() throws Throwable {
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
		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr("button.kunde"));

		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));
		wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(), LPMain.getTextRespectUISPr("lp.kommentar"));

		wtfKunde.setMandatoryField(true);
		wtfKunde.setColumnsMax(PartnerFac.MAX_NAME);
		wtfKunde.setActivatable(false);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaBelegdatum = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("label.belegdatum"));

		wnfMenge1.setMandatoryField(true);
		wnfMenge2.setMandatoryField(true);
		wnfMenge3.setMandatoryField(true);
		wnfMenge4.setMandatoryField(true);
		wnfMenge5.setMandatoryField(true);

		wlaSummeAbfragen = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.summeabfragen"));
		wnfSummeAbfragen.setActivatable(false);
		wlaLieferterminGeplant = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.liefertermin.geplant"));

		wlaFertigungsterminGeplant = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.fertigungstermin.geplant"));

		wcbRohs.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.rohs"));
		wcbKundeExportieren
				.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.kundeexportieren"));

		wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr("label.projekt"));

		wbuKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner.setActivatable(false);

		wdfBelegdatum.setMandatoryField(true);

		wlaMenge1.setText(LPMain.getInstance().getTextRespectUISPr("label.menge") + " 1");
		wlaMenge2.setText(LPMain.getInstance().getTextRespectUISPr("label.menge") + " 2");
		wlaMenge3.setText(LPMain.getInstance().getTextRespectUISPr("label.menge") + " 3");
		wlaMenge4.setText(LPMain.getInstance().getTextRespectUISPr("label.menge") + " 4");
		wlaMenge5.setText(LPMain.getInstance().getTextRespectUISPr("label.menge") + " 5");

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 3, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 3, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge1, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfMenge1, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		jpaWorkingOn.add(wlaMenge2, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge2, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMenge3, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge3, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMenge4, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge4, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMenge5, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge5, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EK_ANGEBOT_ANFRAGE)) {
			iZeile++;
			jpaWorkingOn.add(wlaFertigungsterminGeplant, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wdfFertigungsterminGeplant, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 2, 2, 2), 0, 0));
			wdfFertigungsterminGeplant.setMandatoryField(true);
			iZeile++;
			jpaWorkingOn.add(wcbKundeExportieren, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(20, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wefKommentar, new GridBagConstraints(1, iZeile, 1, 10, 1, 1, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)) {

			iZeile++;
			jpaWorkingOn.add(wlaLieferterminGeplant, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wdfLieferterminGeplant, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 2, 2, 2), 0, 0));

			iZeile++;

			jpaWorkingOn.add(wcbRohs, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

			iZeile++;
			jpaWorkingOn.add(wlaSummeAbfragen, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wnfSummeAbfragen, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EK_ANGEBOT_ANFRAGE)) {

			getToolBar().addButtonRight("/com/lp/client/res/document_into.png",
					LPMain.getTextRespectUISPr("agstkl.lumiquote.import"), MY_OWN_NEW_LUMIQUOTE_IMPORT, null, null);
		}

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, ACTION_PRINT };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		einkaufsangebotDto = new EinkaufsangebotDto();
		kundeDto = new KundeDto();
		partnerIId = null;
		getInternalFrameAngebotstkl().setEinkaufsangebotDto(einkaufsangebotDto);
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKundeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE)) {
			dialogQueryAnsprechartnerFromListe(e);
		} else if (MY_OWN_NEW_LUMIQUOTE_IMPORT.equals(e.getActionCommand())) {

			importiereLumiQuote();
		}
	}

	private void importiereLumiQuote() throws ExceptionLP, Throwable, InvalidFormatException, IOException {
		List<Integer> positionIIds = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(einkaufsangebotDto.getIId());

		if (positionIIds == null || positionIIds.size() == 0) {

			HvOptional<XlsxFile> xlsxFile = FileOpenerFactory.lumiQuoteXlsx(this);
			if (xlsxFile.isPresent()) {

				TreeMap<BigDecimal, LinkedHashMap<String, ArrayList<ImportLumiQuoteXlsxDto>>> hmNachMengenUndLieferantenGruppiert = new TreeMap<BigDecimal, LinkedHashMap<String, ArrayList<ImportLumiQuoteXlsxDto>>>();

				// Finds the workbook instance for XLSX file
				XSSFWorkbook myWorkBook = xlsxFile.get().createWorkbook();

				Iterator<Sheet> sheetIterator = myWorkBook.sheetIterator();

				if (sheetIterator.hasNext()) {
					sheetIterator.next();

					// Erste Mappe auslassen
					while (sheetIterator.hasNext()) {
						Sheet sheet = sheetIterator.next();

						Iterator<Row> rowIterator = sheet.iterator();

						if (rowIterator.hasNext()) {

							int DESIGNATOR = 0;

							int QTY = 1;

							int APPROVED_PARTS = 2;

							int IPN_HV_ARTIKELNUMMER = 5;

							int MPN = 7;

							int AGGREGATED_QTY = 13;
							int SUPPLIER = 17;
							int SKU = 18;

							int UNIT_PREIS = 19;

							int TOTAL_PURCHASE_QTY = 15;

							int TOTAL_PRICE = 21;

							int ORIGINAL_TOTAL_PRICE = 22;

							int LEAD_TIME = 24;

							int FACTORY_LEAD_TIME = 27;

							int STOCK = 25;

							int NOTES = 36;

							BigDecimal bdMengeFuerKopfdaten = null;

							HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

							Row ersteZeile = rowIterator.next();
							Iterator<Cell> cellIteratorErsteZeile = ersteZeile.cellIterator();
							int i = 0;

							while (cellIteratorErsteZeile.hasNext()) {
								Cell cell = cellIteratorErsteZeile.next();
								hmVorhandeneSpalten.put(cell.getStringCellValue(), new Integer(i));

								i++;
							}

							if (hmVorhandeneSpalten.containsKey("Designator")) {
								DESIGNATOR = hmVorhandeneSpalten.get("Designator");
							}

							if (hmVorhandeneSpalten.containsKey("Qty")) {
								QTY = hmVorhandeneSpalten.get("Qty");
							}

							if (hmVorhandeneSpalten.containsKey("Approved parts")) {
								APPROVED_PARTS = hmVorhandeneSpalten.get("Approved parts");
							}

							if (hmVorhandeneSpalten.containsKey("Internal part number")) {
								IPN_HV_ARTIKELNUMMER = hmVorhandeneSpalten.get("Internal part number");
							}
							if (hmVorhandeneSpalten.containsKey("MPN")) {
								MPN = hmVorhandeneSpalten.get("MPN");
							}
							if (hmVorhandeneSpalten.containsKey("Aggregated qty")) {
								AGGREGATED_QTY = hmVorhandeneSpalten.get("Aggregated qty");
							}

							if (hmVorhandeneSpalten.containsKey("Unit price")) {
								UNIT_PREIS = hmVorhandeneSpalten.get("Unit price");
							}

							if (hmVorhandeneSpalten.containsKey("Total purchase qty")) {
								TOTAL_PURCHASE_QTY = hmVorhandeneSpalten.get("Total purchase qty");
							} else if (hmVorhandeneSpalten.containsKey("Purchase qty")) {
								TOTAL_PURCHASE_QTY = hmVorhandeneSpalten.get("Purchase qty");
							}

							if (hmVorhandeneSpalten.containsKey("Original total price")) {
								ORIGINAL_TOTAL_PRICE = hmVorhandeneSpalten.get("Original total price");
							} else if (hmVorhandeneSpalten.containsKey("Original purchase price")) {
								ORIGINAL_TOTAL_PRICE = hmVorhandeneSpalten.get("Original purchase price");
							}

							if (hmVorhandeneSpalten.containsKey("Total price")) {
								TOTAL_PRICE = hmVorhandeneSpalten.get("Total price");
							} else if (hmVorhandeneSpalten.containsKey("Purchase price")) {
								TOTAL_PRICE = hmVorhandeneSpalten.get("Purchase price");
							}

							if (hmVorhandeneSpalten.containsKey("Lead time (days)")) {
								LEAD_TIME = hmVorhandeneSpalten.get("Lead time (days)");
							}

							if (hmVorhandeneSpalten.containsKey("Std. factory lead time (days)")) {
								FACTORY_LEAD_TIME = hmVorhandeneSpalten.get("Std. factory lead time (days)");
							}

							if (hmVorhandeneSpalten.containsKey("Stock")) {
								STOCK = hmVorhandeneSpalten.get("Stock");
							}

							if (hmVorhandeneSpalten.containsKey("Supplier")) {
								SUPPLIER = hmVorhandeneSpalten.get("Supplier");
							}

							if (hmVorhandeneSpalten.containsKey("SKU")) {
								SKU = hmVorhandeneSpalten.get("SKU");
							}

							if (hmVorhandeneSpalten.containsKey("Notes")) {
								NOTES = hmVorhandeneSpalten.get("Notes");
							}

							while (rowIterator.hasNext()) {

								Row row = rowIterator.next();

								// Erste Zeile ist ueberschrift
								String position = null;
								String lieferant = null;
								String hv_artikelnummer = null;
								BigDecimal bdMenge = null;

								BigDecimal gesamtmenge = null;
								BigDecimal lagerstandLF = null;

								Integer lieferzeitInTagen = 0;

								Integer factoryLeadTimeInTagen = null;

								boolean bInStock = false;
								boolean bOnOrder = false;

								String bemerkungLF = null;

								String artikelnummerLF = null;
								String herstellernummer = null;

								String bezeichnung = null;

								BigDecimal ekMenge = null;
								BigDecimal gesamtpreisOriginalWaehrung = null;
								BigDecimal gesamtpreis = null;

								Iterator<Cell> cellIterator = row.cellIterator();
								while (cellIterator.hasNext()) {

									Cell cell = cellIterator.next();

									if (cell.getColumnIndex() == QTY && cell.getCellType() == CellType.STRING) {

										bdMenge = new BigDecimal(cell.getStringCellValue());

									} else if (cell.getColumnIndex() == DESIGNATOR
											&& cell.getCellType() == CellType.STRING) {
										position = cell.getStringCellValue();
									} else if (cell.getColumnIndex() == SUPPLIER
											&& cell.getCellType() == CellType.STRING) {
										lieferant = cell.getStringCellValue();
									} else if (cell.getColumnIndex() == IPN_HV_ARTIKELNUMMER
											&& cell.getCellType() == CellType.STRING) {
										hv_artikelnummer = cell.getStringCellValue();
									} else if (cell.getColumnIndex() == SKU && cell.getCellType() == CellType.STRING) {
										artikelnummerLF = cell.getStringCellValue();
									} else if (cell.getColumnIndex() == MPN && cell.getCellType() == CellType.STRING) {
										herstellernummer = cell.getStringCellValue();
									} else if (cell.getColumnIndex() == AGGREGATED_QTY
											&& cell.getCellType() == CellType.STRING) {
										gesamtmenge = new BigDecimal(cell.getStringCellValue());
									} else if (cell.getColumnIndex() == TOTAL_PURCHASE_QTY
											&& cell.getCellType() == CellType.STRING) {
										ekMenge = new BigDecimal(cell.getStringCellValue());
									} else if (cell.getColumnIndex() == ORIGINAL_TOTAL_PRICE
											&& cell.getCellType() == CellType.NUMERIC) {
										gesamtpreisOriginalWaehrung = new BigDecimal(cell.getNumericCellValue());
									} else if (cell.getColumnIndex() == TOTAL_PRICE
											&& cell.getCellType() == CellType.NUMERIC) {
										gesamtpreis = new BigDecimal(cell.getNumericCellValue());
									} else if (cell.getColumnIndex() == STOCK
											&& cell.getCellType() == CellType.NUMERIC) {
										lagerstandLF = new BigDecimal(cell.getNumericCellValue());
									} else if (cell.getColumnIndex() == LEAD_TIME
											&& cell.getCellType() == CellType.STRING) {

										if (cell.getStringCellValue() != null
												&& cell.getStringCellValue().equals("In stock")) {
											bInStock = true;
											lieferzeitInTagen = 0;
										} else if (cell.getStringCellValue() != null
												&& cell.getStringCellValue().equals("On order")) {
											bOnOrder = true;
										} else {
											lieferzeitInTagen = new Integer(cell.getStringCellValue());
										}

									} else if (cell.getColumnIndex() == NOTES
											&& cell.getCellType() == CellType.STRING) {
										bemerkungLF = cell.getStringCellValue();
									} else if (cell.getColumnIndex() == APPROVED_PARTS
											&& cell.getCellType() == CellType.STRING) {
										bezeichnung = cell.getStringCellValue();
									} else if (cell.getColumnIndex() == FACTORY_LEAD_TIME
											&& cell.getCellType() == CellType.STRING) {
										factoryLeadTimeInTagen = new Integer(cell.getStringCellValue());
									}

								}

								if (bdMengeFuerKopfdaten == null) {

									bdMengeFuerKopfdaten = gesamtmenge.divide(bdMenge, 2, BigDecimal.ROUND_HALF_EVEN);

								}

								if (lieferant != null) {

									BigDecimal bdEinzelpreis = BigDecimal.ZERO;

									if (ekMenge != null && gesamtpreisOriginalWaehrung != null
											&& ekMenge.doubleValue() != 0) {
										bdEinzelpreis = gesamtpreisOriginalWaehrung.divide(ekMenge,
												Defaults.getInstance().getIUINachkommastellenPreiseEK(),
												BigDecimal.ROUND_HALF_EVEN);
									} else if (ekMenge != null && gesamtpreis != null && ekMenge.doubleValue() != 0) {
										bdEinzelpreis = gesamtpreis.divide(ekMenge,
												Defaults.getInstance().getIUINachkommastellenPreiseEK(),
												BigDecimal.ROUND_HALF_EVEN);
									}

									if (bdMengeFuerKopfdaten == null) {

										bdMengeFuerKopfdaten = gesamtmenge.divide(bdMenge, 2,
												BigDecimal.ROUND_HALF_EVEN);

									}

									LinkedHashMap<String, ArrayList<ImportLumiQuoteXlsxDto>> hmMenge = null;
									if (hmNachMengenUndLieferantenGruppiert.containsKey(bdMengeFuerKopfdaten)) {
										hmMenge = hmNachMengenUndLieferantenGruppiert.get(bdMengeFuerKopfdaten);
									} else {
										hmMenge = new LinkedHashMap<String, ArrayList<ImportLumiQuoteXlsxDto>>();
									}

									ArrayList<ImportLumiQuoteXlsxDto> alZeilenLieferant = null;
									if (hmMenge.containsKey(lieferant)) {
										alZeilenLieferant = hmMenge.get(lieferant);
									} else {
										alZeilenLieferant = new ArrayList<ImportLumiQuoteXlsxDto>();
									}

									ImportLumiQuoteXlsxDto zeileXLSX = new ImportLumiQuoteXlsxDto();

									zeileXLSX.setMenge(bdMenge);
									zeileXLSX.setPreis(bdEinzelpreis);
									zeileXLSX.setHVArtikelnummer(hv_artikelnummer);
									zeileXLSX.setLf_artikelnummer(artikelnummerLF);

									if (bInStock) {
										lieferzeitInTagen = 0;
									} else if (bOnOrder) {

										if (factoryLeadTimeInTagen != null) {
											lieferzeitInTagen = factoryLeadTimeInTagen;
										} else {
											lieferzeitInTagen = 999 * 7;
										}

									}

									zeileXLSX.setiLieferzeitInTagen(lieferzeitInTagen);
									zeileXLSX.setBemerkungLF(bemerkungLF);
									zeileXLSX.setLagerstandLF(lagerstandLF);
									zeileXLSX.setBezeichnung(bezeichnung);
									zeileXLSX.setPosition(position);
									zeileXLSX.setHerstellernummer(herstellernummer);

									alZeilenLieferant.add(zeileXLSX);

									hmMenge.put(lieferant, alZeilenLieferant);

									hmNachMengenUndLieferantenGruppiert.put(bdMengeFuerKopfdaten, hmMenge);
								}

							}

						}

					}

				}

				ArrayList<String> alNeueLieferanten = DelegateFactory.getInstance().getAngebotstklDelegate()
						.leseLumiquoteXLSEin(einkaufsangebotDto.getIId(), hmNachMengenUndLieferantenGruppiert);

				if (alNeueLieferanten != null && alNeueLieferanten.size() > 0) {

					String lieferanten = "";

					Iterator it = alNeueLieferanten.iterator();

					while (it.hasNext()) {
						String lieferant = (String) it.next();

						lieferanten += "\r\n" + lieferant;

						if (it.hasNext()) {
							lieferanten += ",";
						}

					}

					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
							LPMain.getInstance().getTextRespectUISPr("agstkl.lumiquote.import.lieferanten.angelegt")
									+ lieferanten);

				}

				eventYouAreSelected(false);
			}

		} else {

			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("agstkl.lumiquote.import.error.positionenvorhanden"));
		}
	}

	@Override
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getInternalFrameAngebotstkl().getTabbedPaneEinkaufsangebot().printEinkaufsangebot();
		eventYouAreSelected(false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {
		einkaufsangebotDto.setCProjekt(wtfProjekt.getText());
		einkaufsangebotDto.setTBelegdatum(wdfBelegdatum.getTimestamp());
		einkaufsangebotDto.setNMenge1(wnfMenge1.getBigDecimal());
		einkaufsangebotDto.setNMenge2(wnfMenge2.getBigDecimal());
		einkaufsangebotDto.setNMenge3(wnfMenge3.getBigDecimal());
		einkaufsangebotDto.setNMenge4(wnfMenge4.getBigDecimal());
		einkaufsangebotDto.setNMenge5(wnfMenge5.getBigDecimal());

		einkaufsangebotDto.setCKommentar(wefKommentar.getText());

		einkaufsangebotDto.setTLiefertermin(wdfLieferterminGeplant.getTimestamp());
		einkaufsangebotDto.setTFertigungstermin(wdfFertigungsterminGeplant.getTimestamp());
		einkaufsangebotDto.setBRoHs(wcbRohs.getShort());
		einkaufsangebotDto.setBKundeExportieren(wcbKundeExportieren.getShort());
		// einkaufsangebotDto.setIAnzahlwebabfragen(wnfSummeAbfragen.getInteger());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (einkaufsangebotDto.getIId() == null) {
				einkaufsangebotDto.setIId(DelegateFactory.getInstance().getAngebotstklDelegate()
						.createEinkaufsangebot(einkaufsangebotDto));
				setKeyWhenDetailPanel(einkaufsangebotDto.getIId());
				einkaufsangebotDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.einkaufsangebotFindByPrimaryKey(einkaufsangebotDto.getIId());
				kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(einkaufsangebotDto.getKundeIId());
				internalFrameAngebotstkl.setEinkaufsangebotDto(einkaufsangebotDto);
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate().updateEinkaufsangebot(einkaufsangebotDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(einkaufsangebotDto.getIId().toString());
			}

			eventYouAreSelected(false);

			einkaufsangebotDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.einkaufsangebotFindByPrimaryKey(einkaufsangebotDto.getIId());
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey((Integer) key);

				wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
				partnerIId = kundeDto.getPartnerDto().getIId();

				einkaufsangebotDto.setKundeIId(kundeDto.getIId());
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) key);

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
				einkaufsangebotDto.setAnsprechpartnerIId(ansprechpartnerDto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

}
