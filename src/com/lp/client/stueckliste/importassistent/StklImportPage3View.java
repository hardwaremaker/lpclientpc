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
 package com.lp.client.stueckliste.importassistent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperTristateCheckbox;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.IStklImportResult;

public class StklImportPage3View extends AssistentPageView {

	private static final long serialVersionUID = -6204532490804255105L;
	
	protected StklImportPage3Ctrl controller;
	
	protected JProgressBar progressBar;
	protected JTable table;
	protected WrapperCheckBox zusammengefasst;
	protected WrapperButton mitHandartikelBefuellen;
	protected WrapperTristateCheckbox wtcMappingUpdate;
	protected WrapperButton mitArtikelErzeugen;

	protected PanelQueryFLR panelQueryFLRArtikel;
	protected Listener l;
	protected ResultTableCellEditorChooser cellEditorChooser;
	private String title;
	
	public StklImportPage3View(StklImportPage3Ctrl controller, InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
		cellEditorChooser = new ResultTableCellEditorChooser();
	}
	
	protected void setAndAddListener(Listener l) {
		this.l = l;
		getInternalFrame().addItemChangedListener(l);
		controller.setArtikelAuswahlListener(l);
	}

	@Override
	public void dataUpdated() {
		progressBar.setVisible(getController().isBusyImporting());
		progressBar.setModel(getController().getProgressModel());
		
		zusammengefasst.setVisible(!getController().isBusyImporting());
		zusammengefasst.setSelected(getController().isZusammengefasst());
		
		mitHandartikelBefuellen.setVisible(!getController().isBusyImporting());
		table.setVisible(!getController().isBusyImporting());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		
		table.setModel(getController().getResultTableModel());
		table.getTableHeader().setResizingAllowed(true);
		table.getTableHeader().setReorderingAllowed(false);
		ColumnsAutoSizer.sizeColumnsToFit(table);

		
		int status = getController().getMappingUpdateStatus();
		if(status != WrapperTristateCheckbox.DISABLE) {
			wtcMappingUpdate.setVisible(!getController().isBusyImporting());
			wtcMappingUpdate.setSelection(status);
		} else {
			wtcMappingUpdate.setVisible(false);
		}
	}

	@Override
	public StklImportPage3Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return title != null
				? title
				: LPMain.getTextRespectUISPr("stkl.intelligenterstklimport");
	}
	
	public void setTitle(String pageTitle) {
		this.title = pageTitle;
	}

	@Override
	protected boolean mustInitView() {
		return true;
	}

	@Override
	protected void initViewImpl() {
		Listener l = new Listener();

		progressBar = new JProgressBar(getController().getProgressModel());
		progressBar.setStringPainted(true);
		
		zusammengefasst = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.zusammengefasst"));
		zusammengefasst.addActionListener(l);
		
		mitHandartikelBefuellen = new WrapperButton(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.undefiniertemithandartikelfuellen"));
		mitHandartikelBefuellen.addActionListener(l);
		
		mitArtikelErzeugen = new WrapperButton(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.undefinierteartikelerzeugen"));
		mitArtikelErzeugen.addActionListener(l);

		wtcMappingUpdate = new WrapperTristateCheckbox(
				getController().getMappingUpdateTristateCheckboxText());
		wtcMappingUpdate.addActionListener(l);
		wtcMappingUpdate.setSelected(true);
		wtcMappingUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);

		table = new JTable();
		table.setDefaultRenderer(Object.class, new ResultTableCellRenderer(getController()));
		table.setDefaultRenderer(IStklImportResult.class, new ResultTableCellRendererChooser(getController()));
		table.setDefaultRenderer(Boolean.class, new ResultTableCellRendererCheckbox());
		table.setDefaultEditor(IStklImportResult.class, cellEditorChooser);
		table.setVisible(false);
	}
	
	protected class Listener implements ActionListener, ItemChangedListener, INeedArtikelAuswahlListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == zusammengefasst) {
				getController().setZusammengefasst(zusammengefasst.isSelected());
			} else if(e.getSource() == mitHandartikelBefuellen) {
				getController().undefinierteMitHandartikelBefuellen();
			} else if(e.getSource() == wtcMappingUpdate) {
				getController().selektiereMappingUpdate(wtcMappingUpdate.getSelection());
			} else if (e.getSource() == mitArtikelErzeugen) {
				getController().undefinierteAlsArtikelerzeugen();
			}
		}

		@Override
		public void changed(EventObject e) {
			if(e instanceof ItemChangedEvent) {
				ItemChangedEvent ice = (ItemChangedEvent) e;
				if (ice.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL && ice.getSource() == panelQueryFLRArtikel) {
					Integer iid = (Integer) panelQueryFLRArtikel.getSelectedId();
					getController().setChosenArtikelIId(iid);
					getController().tableModelValueChanged();
				}
			}
		}

		@Override
		public void waehleArtikelAus() {
			try {
				panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
						.createPanelFLRArtikelOhneArbeitszeit(getInternalFrame(),
								null, false);
				
				new DialogQuery(panelQueryFLRArtikel);
			} catch (Throwable e) {
				getInternalFrame().handleException(e, true);
			}
		}


	}

}
