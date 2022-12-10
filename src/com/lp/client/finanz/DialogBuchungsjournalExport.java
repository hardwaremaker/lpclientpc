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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BuchungsjournalExportDatumsart;
import com.lp.server.finanz.service.BuchungsjournalExportProperties;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.util.Helper;

public class DialogBuchungsjournalExport extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -7236071859832945050L;

	private WrapperComboBox format;
	private WrapperDateField von;
	private WrapperDateField bis;
	private WrapperCheckBox mitAutoEB;
	private WrapperCheckBox mitAutoB;
	private WrapperCheckBox mitManEB;
	private WrapperCheckBox mitStornierte;
	private WrapperTextField bezeichnung;
	private WrapperButton exportieren;
	private WrapperRadioButton rbBuchungsdatum;
	private WrapperRadioButton rbGebuchtAm;
	private WrapperDateRangeController wdrDatumsbereich;
	
	private BuchungsjournalExportUIProperties nachBuchungsdatumProperties;
	private BuchungsjournalExportUIProperties nachGebuchtAmProperties;
	private BuchungsjournalExportUIProperties selectedProperties;
	
	public DialogBuchungsjournalExport(Frame owner) {
		super(owner, LPMain.getTextRespectUISPr("fb.buchungsjournal.export"));
		setSize(new Dimension(Defaults.getInstance().bySizeFactor(350), Defaults.getInstance().bySizeFactor(350)));
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		jbInit();
		
		loadProperties();
		headPropertiesToComponents();
		propertiesToComponents();
		HelperClient.setComponentNames(this);
	}
	
	private void jbInit() {
		format = new WrapperComboBox(new Object[]{FibuExportFac.DATEV, FibuExportFac.HV_RAW, FibuExportFac.RZL_CSV});
		von = new WrapperDateField();
		von.setMandatoryField(true);

		bis = new WrapperDateField();
		bis.setMandatoryField(true);

		mitAutoEB = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.automatischeeroeffnungsbuchungen"));
		mitAutoEB.setSelected(true);
		mitAutoB = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.automatikbuchungen"));
		mitAutoB.setSelected(true);
		mitManEB = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.manuelleeroeffnungsbuchungen"));
		mitManEB.setSelected(true);
		mitStornierte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.plusstornierte"));
		mitStornierte.setSelected(true);
//		mitStornierte.setEnabled(false);
		bezeichnung = new WrapperTextField(30);
		exportieren = new WrapperButton(
				LPMain.getTextRespectUISPr("fb.buchungsjournal.export.exportieren"));
		exportieren.addActionListener(this);

		format.setLightWeightPopupEnabled(false);
		format.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(FibuExportFac.DATEV.equals(format.getSelectedItem())) {
					mitStornierte.setSelected(true);
//					mitStornierte.setEnabled(false);
				} else {
					mitStornierte.setEnabled(true);
				}
			}
		});
		
		rbBuchungsdatum = new WrapperRadioButton(LPMain.getTextRespectUISPr("fb.buchungsjournal.datumsfilter.buchungsdatum"));
		rbBuchungsdatum.addActionListener(this);
		rbGebuchtAm = new WrapperRadioButton(LPMain.getTextRespectUISPr("fb.buchungsjournal.datumsfilter.gebuchtam"));
		rbGebuchtAm.addActionListener(this);
		
		ButtonGroup rbGroupDatum = new ButtonGroup();
		rbGroupDatum.add(rbBuchungsdatum);
		rbGroupDatum.add(rbGebuchtAm);
		
		wdrDatumsbereich = new WrapperDateRangeController(von, bis);

		Container c = getContentPane();
		c.setLayout(new MigLayout("wrap 2", "15[40%,fill|60%,fill]15", "15[]10[]10[]20[][][][]20[]push"));
//		c.setPreferredSize(new Dimension(3000, 2000));
		
		c.add(format, "span");
		c.add(rbGebuchtAm);
		c.add(rbBuchungsdatum);
		
		JPanel panelDatum = new JPanel();
		HvLayout layoutDatum = HvLayoutFactory.create(panelDatum, "ins 0", "[20,fill|60,fill|20,fill|40,fill|20,fill]push", "[fill]");
		WrapperLabel lVon = new WrapperLabel(LPMain.getTextRespectUISPr("lp.von"));
		lVon.setHorizontalAlignment(JLabel.LEFT);
		WrapperLabel lBis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bis"));
		lBis.setHorizontalAlignment(JLabel.RIGHT);
		layoutDatum.add(lVon).add(von)
			.add(lBis).add(bis)
			.add(wdrDatumsbereich, "align left");
		c.add(panelDatum, "span");

		c.add(mitAutoEB, "span");
		c.add(mitAutoB, "span");
		c.add(mitManEB, "span");
		c.add(mitStornierte, "span");
		
		c.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.bezeichnung")));
		c.add(bezeichnung);
		
		c.add(exportieren, "span");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exportieren) {
			actionExport();
		} else if (e.getSource() == rbBuchungsdatum) {
			actionDatumsartChanged(getNachBuchungsdatumProperties());
		} else if (e.getSource() == rbGebuchtAm) {
			actionDatumsartChanged(getNachGebuchtAmProperties());
		}

	}
	
	private void actionDatumsartChanged(BuchungsjournalExportUIProperties newProperties) {
		componentsToProperties();
		selectedProperties = newProperties;
		propertiesToComponents();
	}

	private void actionExport() {
		try {
			if(von.hasContent() && bis.hasContent()) {
				List<String> zeilen = DelegateFactory.getInstance().getFibuExportDelegate().exportiereBuchungsjournal(
						mapToExportProperties());
				File temp = File.createTempFile("buchungsjournalexport", "csv");
				BufferedWriter fw = new BufferedWriter(new FileWriter(temp));
				for (String zeile : zeilen) {
					fw.write(zeile);
					fw.newLine();
				}
				fw.close();
				File fileUsed = HelperClient.showSaveFileDialog(temp, getExportFile(), this, ".csv");
				if (fileUsed == null) {
					closeDialog(false);
				} else {
					setExportFile(fileUsed);
					closeDialog(true);
				}
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));				
			}
		} catch (Throwable t) {
			handleOwnException(t);
		}
	}
	
	private void closeDialog(boolean saveProperties) {
		if (saveProperties) saveProperties();
		dispose();
	}
	
	private File getExportFile() {
		if (!Helper.isStringEmpty(selectedProperties.getDateipfad())) {
			return new File(selectedProperties.getDateipfad(), getFilename());
		}
		
		return new File(getFilename());
	}
	
	private void setExportFile(File file) {
		selectedProperties.setDateipfad(file.getParent());
	}

	private BuchungsjournalExportProperties mapToExportProperties() {
		BuchungsjournalExportProperties exportProperties = new BuchungsjournalExportProperties();
		exportProperties.setFormat(format.getSelectedItem().toString());
		if (rbGebuchtAm.isSelected()) {
			exportProperties.setDatumsart(BuchungsjournalExportDatumsart.GEBUCHTAM);
		} else {
			exportProperties.setDatumsart(BuchungsjournalExportDatumsart.BUCHUNGSDATUM);
		}
		exportProperties.setVon(von.getDate());
		exportProperties.setBis(bis.getDate());
		exportProperties.setMitAutoEroeffnungsbuchungen(mitAutoEB.isSelected());
		exportProperties.setMitManEroeffnungsbuchungen(mitManEB.isSelected());
		exportProperties.setMitAutoBuchungen(mitAutoB.isSelected());
		exportProperties.setMitStornierte(mitStornierte.isSelected());
		exportProperties.setBezeichnung(bezeichnung.getText());
		return exportProperties;
	}

	public void handleOwnException(Throwable t) {
		new DialogError(LPMain.getInstance().getDesktop(), t,
				DialogError.TYPE_INFORMATION);
	}
	
	private String getFilename() {
		String filename = "";
		
		if(FibuExportFac.RZL_CSV.equals(format.getSelectedItem().toString())) {
			filename += FibuExportFac.RZL_CSV_FILENAME_PREFIX;
		}
		filename += bezeichnung.getText() + ".csv";
		
		return filename;
	}
	
	private BuchungsjournalExportUIProperties setupNachBuchungsdatumDefaults() {
		BuchungsjournalExportUIProperties uiProperties = new BuchungsjournalExportUIProperties();
		uiProperties.setDatumsart(BuchungsjournalExportDatumsart.BUCHUNGSDATUM);
		uiProperties.setFormat(format.getItemAt(0).toString());
		uiProperties.setLetztesBisDatum(Helper.addiereTageZuDatum(getDefaultVon(), -1));
		uiProperties.setMitAutoB(true);
		uiProperties.setMitAutoEB(true);
		uiProperties.setMitManEB(true);
		uiProperties.setMitStornierte(true);
		return uiProperties;
	}
	
	private BuchungsjournalExportUIProperties setupNachGebuchtAmDatumDefaults() {
		BuchungsjournalExportUIProperties uiProperties = new BuchungsjournalExportUIProperties();
		uiProperties.setDatumsart(BuchungsjournalExportDatumsart.GEBUCHTAM);
		uiProperties.setFormat(format.getItemAt(0).toString());
		uiProperties.setLetztesBisDatum(Helper.addiereTageZuDatum(getDefaultVon(), -1));
		uiProperties.setMitAutoB(false);
		uiProperties.setMitAutoEB(false);
		uiProperties.setMitManEB(false);
		uiProperties.setMitStornierte(true);
		return uiProperties;
	}
	
	private void loadProperties() {
		setNachBuchungsdatumProperties(setupNachBuchungsdatumDefaults());
		setNachGebuchtAmProperties(setupNachGebuchtAmDatumDefaults());

		BuchungsjournalExportUIProperties loadedProperties = 
				ClientPerspectiveManager.getInstance().readBuchungsjournalExportProperties();
		if (loadedProperties == null) {
			selectedProperties = getNachBuchungsdatumProperties();
			return;
		}
		
		if (BuchungsjournalExportDatumsart.GEBUCHTAM.equals(loadedProperties.getDatumsart())) {
			setProperties(loadedProperties, getNachGebuchtAmProperties(), getNachBuchungsdatumProperties());
			selectedProperties = getNachGebuchtAmProperties();
		} else {
			setProperties(loadedProperties, getNachBuchungsdatumProperties(), getNachGebuchtAmProperties());
			selectedProperties = getNachBuchungsdatumProperties();
		}
	}

	private void setProperties(BuchungsjournalExportUIProperties loaded,
			BuchungsjournalExportUIProperties selected, BuchungsjournalExportUIProperties notSelected) {
		selected.setDateipfad(loaded.getDateipfad());
		selected.setFormat(loaded.getFormat());
		selected.setLetztesBisDatum(loaded.getLetztesBisDatum());
		selected.setMitAutoB(loaded.isMitAutoB());
		selected.setMitAutoEB(loaded.isMitAutoEB());
		selected.setMitManEB(loaded.isMitManEB());
		selected.setMitStornierte(loaded.isMitStornierte());
		
		notSelected.setDateipfad(loaded.getDateipfad());
		notSelected.setFormat(loaded.getFormat());
		notSelected.setLetztesBisDatum(loaded.getLetztesBisDatum());
	}
	
	public void setNachBuchungsdatumProperties(
			BuchungsjournalExportUIProperties nachBuchungsdatumProperties) {
		this.nachBuchungsdatumProperties = nachBuchungsdatumProperties;
	}
	
	public BuchungsjournalExportUIProperties getNachBuchungsdatumProperties() {
		return nachBuchungsdatumProperties;
	}
	
	public void setNachGebuchtAmProperties(
			BuchungsjournalExportUIProperties nachGebuchtAmProperties) {
		this.nachGebuchtAmProperties = nachGebuchtAmProperties;
	}
	
	public BuchungsjournalExportUIProperties getNachGebuchtAmProperties() {
		return nachGebuchtAmProperties;
	}
	
	private Date getDefaultVon() {
	    GregorianCalendar cal = new GregorianCalendar();	    
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    cal.add(Calendar.MONTH, -1) ;		
		return new Date(cal.getTime().getTime());
	}
	
	private Date getDefaultBis() {
		return Helper.addiereTageZuDatum(new Date(System.currentTimeMillis()), -1);
	}
	
	private void headPropertiesToComponents() {
		format.setSelectedItem(selectedProperties.getFormat());
		von.setDate(Helper.addiereTageZuDatum(selectedProperties.getLetztesBisDatum(), 1));
		bis.setDate(getDefaultBis());
	}
	
	private void propertiesToComponents() {
		mitAutoB.setSelected(selectedProperties.isMitAutoB());
		mitAutoEB.setSelected(selectedProperties.isMitAutoEB());
		mitManEB.setSelected(selectedProperties.isMitManEB());
		mitStornierte.setSelected(selectedProperties.isMitStornierte());
		
		rbBuchungsdatum.removeActionListener(this);
		rbGebuchtAm.removeActionListener(this);
		rbBuchungsdatum.setSelected(BuchungsjournalExportDatumsart.BUCHUNGSDATUM.equals(selectedProperties.getDatumsart()));
		rbGebuchtAm.setSelected(!rbBuchungsdatum.isSelected());
		rbBuchungsdatum.addActionListener(this);
		rbGebuchtAm.addActionListener(this);
	}
	
	private void componentsToHeadProperties() {
		selectedProperties.setFormat(format.getSelectedItem().toString());
		selectedProperties.setLetztesBisDatum(bis.getDate());
	}
	
	private BuchungsjournalExportUIProperties componentsToProperties() {
		selectedProperties.setLetztesBisDatum(bis.getDate());
		selectedProperties.setMitAutoB(mitAutoB.isSelected());
		selectedProperties.setMitAutoEB(mitAutoEB.isSelected());
		selectedProperties.setMitManEB(mitManEB.isSelected());
		selectedProperties.setMitStornierte(mitStornierte.isSelected());
		return selectedProperties;
	}
	
	private void saveProperties() {
		componentsToHeadProperties();
		componentsToProperties();
		ClientPerspectiveManager.getInstance().saveBuchungsjournalExportProperties(selectedProperties);
	}
}
