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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.service.findsteel.FindSteelApiController;
import com.lp.service.findsteel.IFindSteelApiController;
import com.lp.service.findsteel.schema.FindSteelApiSearchResponse;
import com.lp.service.findsteel.schema.ProductEntry;

/**
 * <p>Basisfenster fuer LP5 Positionen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2005-02-11</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class PanelPositionenFindSteelSuche extends PanelPositionenPreiseingabe {
	private static final long serialVersionUID = 1L;
	  
	private WrapperLabel wlaBezeichnung = null;
	private WrapperLabel wlaMessage = null;
	private JScrollPane scrollPane = null;
	
	private IFindSteelApiController apiController;
	
	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param sLockMeWer
	 *            String
	 * @param iSpaltenbreite1I
	 *            die Breites der ersten Spalte
	 * @param bDarfPreiseSehen
	 *            boolean
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelPositionenFindSteelSuche(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			int iSpaltenbreite1I, boolean bDarfPreiseSehen, boolean bDarfPreiseAendern) throws Throwable {
		super(internalFrame, add2TitleI, key, sLockMeWer, 
				bDarfPreiseSehen, bDarfPreiseAendern, iSpaltenbreite1I,null);

		apiController = new FindSteelApiController();		
		jbInit();
	}

	public void setDefaults() {
		if(tableModel != null) {
			tableModel.setResult(new FindSteelApiSearchResponse());			
		}
		scrollPane.setVisible(false);
		wlaMessage.setText("");
	}
	
	private void jbInit() throws Throwable {
		setLayout(new MigLayout("insets 0", "[][grow,fill][]", "[][grow]5"));

		wlaBezeichnung = new WrapperLabel(LPMain.getTextRespectUISPr("anfrage.webabfrage.suchtext"));
		Dimension d = new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight());
		wlaBezeichnung.setMaximumSize(d);
		wlaBezeichnung.setMinimumSize(d);
		wlaBezeichnung.setPreferredSize(d);

		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.getDocument().addDocumentListener(new SearchTextListener());

		wlaMessage = new WrapperLabel();
		wlaMessage.setMaximumSize(d);
		wlaMessage.setMinimumSize(d);
		wlaMessage.setPreferredSize(d);
		
		add(wlaBezeichnung, "width " + iSpaltenbreite1 + "!");
		add(wtfBezeichnung, "growx");
		add(wlaMessage, "width " + iSpaltenbreite1 + "!, wrap");

		scrollPane = new JScrollPane(initTableFoundParts());
		add(scrollPane, "skip, span");
		scrollPane.setVisible(false);
	}
	
	private void jbInit0() throws Throwable {
		setLayout(new GridBagLayout());
		
		wlaBezeichnung = new WrapperLabel(LPMain.getTextRespectUISPr("anfrage.webabfrage.suchtext"));
		Dimension d = new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight());
		wlaBezeichnung.setMaximumSize(d);
		wlaBezeichnung.setMinimumSize(d);
		wlaBezeichnung.setPreferredSize(d);

		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.getDocument().addDocumentListener(new SearchTextListener());

		wlaMessage = new WrapperLabel();
		wlaMessage.setMaximumSize(d);
		wlaMessage.setMinimumSize(d);
		wlaMessage.setPreferredSize(d);

		int row = 0;
		
		add(wlaBezeichnung, new GridBagConstraints(0, row, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wtfBezeichnung, new GridBagConstraints(1, row, 2, 1, 1.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaMessage, new GridBagConstraints(3, row, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		++row;
		
		initTableFoundParts();
		scrollPane = new JScrollPane(tableFoundParts);
		add(scrollPane, new GridBagConstraints(1, row, 3, 1, 1.0,
				1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		scrollPane.setVisible(false);
	}

	private JTable tableFoundParts;
	private SearchResultTableModel tableModel;
	
	private class SearchResultTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -1880094767883473900L;

		public class SearchColumn {
			public final static int ID = 0;
			public final static int CNR = 1; 
			public final static int NAME = 2;
			public final static int DESCRIPTION = 3;
		} ;
		
		private FindSteelApiSearchResponse result = new FindSteelApiSearchResponse();
	
		public void setResult(FindSteelApiSearchResponse response) {
			this.result = response;
			fireTableDataChanged();
		}
		
		@Override
		public int getRowCount() {
			return result.getSuccess() ? result.getContent()[0].getNumberOfProducts() : 1;
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public String getColumnName(int column) {
			if(column == SearchColumn.ID) return "Id";
			if(column == SearchColumn.CNR) return "H.Artnr";
			if(column == SearchColumn.NAME) return "Name";
			return "Bezeichnung" ;
		}
	
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(!result.getSuccess()) {
				return "";
			}
			
			ProductEntry entry = result.getContent()[0].getProducts()[rowIndex];
			if(columnIndex == SearchColumn.ID) return entry.getId();
			if(columnIndex == SearchColumn.CNR) return entry.getFilterValues().length > 0 ? entry.getFilterValues()[0] : "";
			if(columnIndex == SearchColumn.NAME) return entry.getName();
			return StringUtils.join(entry.getDescriptions(), ",");
		}	
	}
	
	private JTable initTableFoundParts() {
		tableModel = new SearchResultTableModel();
		tableFoundParts = new JTable();
		tableFoundParts.setModel(tableModel);
//		tableFoundParts.getSelectionModel().addListSelectionListener(this);
		tableFoundParts.setAutoCreateRowSorter(true);
//		DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
//		tableFoundParts.getColumnModel().getColumn(FoundPartsTableModel.IDX_PREIS).setCellRenderer(tableRenderer);
//		tableFoundParts.getTableHeader().addMouseListener(new RightMouseClickDisableSortListener(tableFoundParts));
		tableFoundParts.setRowHeight(Defaults.getInstance().getControlHeight());
//		TableColumn urlButtonColumn = tableFoundParts.getColumnModel().getColumn(FoundPartsTableModel.IDX_URL);
//		urlButtonColumn.setCellRenderer(new ButtonCellRenderer());
		TableColumnModel cm = tableFoundParts.getColumnModel();
		tableFoundParts.getColumn(cm.getColumn(SearchResultTableModel.SearchColumn.ID).getIdentifier()).setPreferredWidth(60);
		tableFoundParts.getColumn(cm.getColumn(SearchResultTableModel.SearchColumn.ID).getIdentifier()).setMaxWidth(150);
		tableFoundParts.getColumn(cm.getColumn(SearchResultTableModel.SearchColumn.CNR).getIdentifier()).setPreferredWidth(150);
		tableFoundParts.getColumn(cm.getColumn(SearchResultTableModel.SearchColumn.NAME).getIdentifier()).setPreferredWidth(200);
//		tableFoundParts.getColumn(tableFoundParts.getColumnModel().getColumn(FoundPartsTableModel.IDX_PREIS).getIdentifier()).setPreferredWidth(COLUMN_SIZE_PRICE);
//		tableFoundParts.addMouseListener(new JTableButtonMouseListener(tableFoundParts));
		return tableFoundParts;
	}
	
	private List<String> queuedSearches = new ArrayList<String>();
	private Timer searchTimer = new Timer("searchTimer");
	private TimerTask timerTask ;
	
	private void triggerSearch(String searchValue) {
		queuedSearches.add(searchValue);
		if(queuedSearches.size() > 0) {
			queuedSearches.remove(0);
		}

		if(timerTask != null) {
			timerTask.cancel();
		}
		timerTask = new SearchTask(searchValue);
		searchTimer.schedule(timerTask, 150);
	}
	
	private class SearchTask extends TimerTask {
		private String searchValue;
		
		public SearchTask(String searchValue) {
			this.searchValue = searchValue;
		}
		
		@Override
		public void run() {
			queuedSearches.remove(searchValue);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					wlaMessage.setText("Web-Abfrage...");
				}
			});
			try {
				myLogger.warn("About to search for '" + searchValue + "'...");
				final FindSteelApiSearchResponse response = apiController.search(1, searchValue, 1, 50);
				myLogger.warn("Search finished for '" + searchValue + "' with " 
						+ response.getSuccess() + " and " + response.getContent()[0].getNumberOfProducts() + " items" );
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						wlaMessage.setText(
								response.getSuccess() 
									? (response.getContent()[0].getNumberOfProducts() + " Einträge") 
									: "Fehlgeschlagen" );
						scrollPane.setVisible(true);
						tableModel.setResult(response);
						scrollPane.invalidate();
					}
				});
			} catch(ExceptionLP ex) {
				myLogger.error("Searching '" + searchValue + "' exception", ex);
			}
		}		
	}
	
	private class SearchTextListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			myLogger.warn("insertUpdate " + getDocText(e));
			addToSearch(e); 
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			myLogger.warn("removeUpdate " + getDocText(e));
			addToSearch(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			myLogger.warn("changedUpdate " + getDocText(e));
			addToSearch(e);
		}
		
		private String getDocText(DocumentEvent e) {
			Document d = e.getDocument();
			String t = "";
			try {
				t = d.getText(0, d.getLength());
			} catch(BadLocationException ex) {				
			}
			return t;			
		}
		
		private void addToSearch(DocumentEvent e) {
			String s = getDocText(e);
			if(s.length() > 1) {
				triggerSearch(s);
			}
		}
	}
}
