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
package com.lp.client.angebotstkl.webabfrage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

public class PanelWebabfrageEinkaufsangebot extends PanelBasis implements ListSelectionListener {

	private static final long serialVersionUID = -1611824667545013162L;
	public static final int COLUMN_SIZE_PRICE = 100;
	public static final int COLUMN_SIZE_MENGE = 200;
	
	public static final String ACTION_RADIOBUTTON_ALLELIEFERANTEN = "action_radiobutton_filter_alle_lieferanten";
	public static final String ACTION_NUR_WEBLIEFERANTEN = "action_checkbox_nurweblieferanten";
	public static final String ACTION_FILTER_BESCHREIBUNG = "action_textfield_filter_beschreibung";
	
	private JPanel panelWeblieferanten = new JPanel();
	private WrapperLabel wlWeblieferantenBekannt = new WrapperLabel();
	private JTable tableWeblieferantenBekannt = new JTable();
	private WrapperLabel wlWeblieferantenNeu = new WrapperLabel();;
	private JTable tableWeblieferantenNeu = new JTable();
	
	private WrapperLabel wlWebabfragePositionen = new WrapperLabel();
	private PanelWebabfrageControl panelWebabfrageControl;
	private JPanel panelWebabfragePositionen = new JPanel();
	private WrapperLabel wlFoundParts = new WrapperLabel();
	private WrapperCheckBox wcbNurWeblieferanten = new WrapperCheckBox();
	private WrapperLabel wlBezeichnungFilter = new WrapperLabel();
	private WrapperTextField wtfBezeichungFilter = new WrapperTextField();
	private JPanel panelWebabfrageFoundParts = new JPanel();
	private JTable tablePositionen;
	private JTable tableFoundParts;
	private PanelFoundPartsDetail panelFoundPartsDetail;
	private JSplitPane splitPaneAbfrage;
	private JSplitPane splitPaneErgebnis;
	
	private JPanel panelAbfrageNaviUnten = new JPanel();
	private WrapperLabel wlFortschritt = new WrapperLabel();;
	
	private INeueLieferantenViewController neueLieferantenController;
	private IBekannteLieferantenViewController bekannteLieferantenController;
	private IWebabfrageViewController webabfrageViewController;
	private IFoundPartsViewController foundpartsViewController;
	
	public PanelWebabfrageEinkaufsangebot(InternalFrame internalFrame, IBekannteLieferantenViewController bekannteLieferantenController, 
			INeueLieferantenViewController neueLieferantenController, IWebabfrageViewController webabfrageViewController, 
			IFoundPartsViewController foundpartsViewController) throws Throwable {
		super(internalFrame, LPMain.getTextRespectUISPr("agstkl.webabfrage.title.webabfrage"));
		this.bekannteLieferantenController = bekannteLieferantenController;
		this.neueLieferantenController = neueLieferantenController;
		this.webabfrageViewController = webabfrageViewController;
		this.foundpartsViewController = foundpartsViewController;
		
		panelFoundPartsDetail = new PanelFoundPartsDetail(this);
		panelWebabfrageControl = new PanelWebabfrageControl(this, webabfrageViewController.getEinkaufsangebotMengenstaffel());
		webabfrageViewController.registerWebabfrageControlListener(panelWebabfrageControl);
		
		jbInit();
		validate();
	}
	
	private void jbInit() {
		initTablesLieferanten();
		wlWeblieferantenBekannt.setText(LPMain.getTextRespectUISPr("agstkl.webabfrage.bekanntelieferanten"));
		wlWeblieferantenBekannt.setHorizontalAlignment(SwingConstants.LEFT);
		wlWeblieferantenNeu.setText(LPMain.getTextRespectUISPr("agstkl.webabfrage.neuelieferanten"));
		wlWeblieferantenNeu.setHorizontalAlignment(SwingConstants.LEFT);
		
		panelWeblieferanten.setLayout(new MigLayout("wrap 1, insets 10 10 10 10, nocache", "[fill,grow]", "[fill][45%, fill,grow][10%, fill][45%, fill,grow]"));
		panelWeblieferanten.add(wlWeblieferantenBekannt);
		panelWeblieferanten.add(new JScrollPane(tableWeblieferantenBekannt));
		panelWeblieferanten.add(wlWeblieferantenNeu, "aligny bottom");
		panelWeblieferanten.add(new JScrollPane(tableWeblieferantenNeu));
		
		initTablePositionen();
		wlWebabfragePositionen.setText(LPMain.getTextRespectUISPr("lp.positionen"));
		wlWebabfragePositionen.setHorizontalAlignment(SwingConstants.LEFT);

		panelWebabfragePositionen.setLayout(new MigLayout("wrap 2, insets 10 10 10 10", "[fill,grow][70%,fill,align right]", "[fill,grow, align bottom][fill,grow]"));
		panelWebabfragePositionen.add(wlWebabfragePositionen, "aligny bottom");
		panelWebabfragePositionen.add(panelWebabfrageControl);
		panelWebabfragePositionen.add(new JScrollPane(tablePositionen), "span");
		
		initTableFoundParts();
		wlFoundParts.setText(LPMain.getTextRespectUISPr("agstkl.webabfrage.gefundenebauteile"));
		wlFoundParts.setHorizontalAlignment(SwingConstants.LEFT);
		wlBezeichnungFilter.setText(LPMain.getTextRespectUISPr("lp.beschreibung") + " (Aa|A+B-C)");
		wtfBezeichungFilter.setActivatable(true);
		wtfBezeichungFilter.addActionListener(this);
		wtfBezeichungFilter.setActionCommand(ACTION_FILTER_BESCHREIBUNG);
		wcbNurWeblieferanten.setText("Nur Weblieferanten");
		wcbNurWeblieferanten.addActionListener(this);
		wcbNurWeblieferanten.setActionCommand(ACTION_NUR_WEBLIEFERANTEN);
		
		JPanel panelFoundPartsHeader = new JPanel();
		panelFoundPartsHeader.setLayout(new MigLayout("wrap , hidemode 0", "[fill,grow][120px,fill,grow][10px][fill,grow]", "[fill,grow, align bottom]"));
		panelFoundPartsHeader.add(wlBezeichnungFilter, "alignx right, gapx 10px");
		panelFoundPartsHeader.add(wtfBezeichungFilter);
		panelFoundPartsHeader.add(wcbNurWeblieferanten, "skip, alignx right");

		panelWebabfrageFoundParts.setLayout(new MigLayout("wrap 2, hidemode 0, insets 10 10 10 10", "[fill,grow][fill,grow]", "[fill,grow][50%,fill,grow][40%,fill,grow]"));
		panelWebabfrageFoundParts.add(wlFoundParts, "aligny bottom");
		panelWebabfrageFoundParts.add(panelFoundPartsHeader);
		panelWebabfrageFoundParts.add(new JScrollPane(tableFoundParts), "span");
		panelWebabfrageFoundParts.add(panelFoundPartsDetail, "span");
		
		splitPaneErgebnis = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelWebabfragePositionen, panelWebabfrageFoundParts);
		splitPaneErgebnis.setDividerLocation(0.5);
		splitPaneErgebnis.setDividerSize(7);
		splitPaneErgebnis.setOneTouchExpandable(true);
		
		wlFortschritt.setText("Fortschritt");
		wlFortschritt.setHorizontalAlignment(SwingConstants.CENTER);
		
		splitPaneAbfrage = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelWeblieferanten, splitPaneErgebnis);
		splitPaneAbfrage.setDividerLocation(0.3);
		splitPaneAbfrage.setDividerSize(7);
		splitPaneAbfrage.setOneTouchExpandable(true);
		
		panelAbfrageNaviUnten.setLayout(new MigLayout("", "[20%, fill][60%, fill][20%, fill]"));
		panelAbfrageNaviUnten.add(wlFortschritt);
		
		setLayout(new MigLayout("wrap 1, insets 10 10 10 10", "[fill,grow]", "[fill,grow]"));
		add(splitPaneAbfrage, "growy");
		tablePositionen.setRowSelectionInterval(0, 0);
	}

	/**
	 * 
	 */
	private void initTablesLieferanten() {
		tableWeblieferantenBekannt.setModel(bekannteLieferantenController.getBekannteLieferantenTableModel());
		setPreferredColumnWidth(tableWeblieferantenBekannt, new Integer[] {200, 30});
		
		tableWeblieferantenNeu.setModel(neueLieferantenController.getNeueLieferantenTableModel());
		TableColumn buttonColumn = tableWeblieferantenNeu.getColumnModel().getColumn(NeueLieferantenModel.IDX_BUTTON_ZUORDNEN);
		buttonColumn.setCellRenderer(new ButtonCellRenderer());
		tableWeblieferantenNeu.addMouseListener(new JTableButtonMouseListener(tableWeblieferantenNeu));
	}

	/**
	 * 
	 */
	private void initTableFoundParts() {
		tableFoundParts = new JTable() {
			private static final long serialVersionUID = -309183115746857628L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				
				IWebabfrageResult result = webabfrageViewController.getWebabfrageResultTableModel().getResultAtRow(
						foundpartsViewController.getSelectedResultRow());
				if (result == null) return c;
				NormalizedWebabfragePart part = foundpartsViewController.getFoundPartsTableModel().getPartAtRow(row);
				IColorState selectedState;
				if (part != null && result.getSelectedPart() != null && part.equals(result.getSelectedPart())) {
					selectedState = new ColorSelectedState();
				} else {
					selectedState = new ColorStartState();
				}
				c.setBackground(isRowSelected(row) ? 
						selectedState.getBackgroundColorSelected() : selectedState.getBackgroundColor());
				c.setForeground(isRowSelected(row) ? 
						selectedState.getForegroundColorSelected() : selectedState.getForegroundColor());
				return c;
			}
		};
		tableFoundParts.setModel(foundpartsViewController.getFoundPartsTableModel());
		tableFoundParts.getSelectionModel().addListSelectionListener(this);
		tableFoundParts.setAutoCreateRowSorter(true);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		tableFoundParts.getColumnModel().getColumn(FoundPartsTableModel.IDX_PREIS).setCellRenderer(rightRenderer);
		tableFoundParts.getTableHeader().addMouseListener(new RightMouseClickDisableSortListener(tableFoundParts));
		tableFoundParts.setRowHeight(20);
		TableColumn urlButtonColumn = tableFoundParts.getColumnModel().getColumn(FoundPartsTableModel.IDX_URL);
		urlButtonColumn.setCellRenderer(new ButtonCellRenderer());
		tableFoundParts.getColumn(tableFoundParts.getColumnModel().getColumn(FoundPartsTableModel.IDX_URL).getIdentifier()).setMaxWidth(30);
		tableFoundParts.getColumn(tableFoundParts.getColumnModel().getColumn(FoundPartsTableModel.IDX_PREIS).getIdentifier()).setPreferredWidth(COLUMN_SIZE_PRICE);
		tableFoundParts.addMouseListener(new JTableButtonMouseListener(tableFoundParts));
	}
	
	private void initTablePositionen() {
		tablePositionen = new JTable() {
			private static final long serialVersionUID = -309183115746857628L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				
				IWebabfrageResult result = webabfrageViewController.getWebabfrageResultTableModel().getResultAtRow(row);
				if (result == null) return c;
				
				c.setBackground(isRowSelected(row) ? 
						result.getState().getBackgroundColorSelected() : result.getState().getBackgroundColor());
				c.setForeground(isRowSelected(row) ? 
						result.getState().getForegroundColorSelected() : result.getState().getForegroundColor());
				
				return c;
			}
		};

		tablePositionen.setModel(webabfrageViewController.getWebabfrageResultTableModel());
		tablePositionen.getSelectionModel().addListSelectionListener(this);
	}
	
	private void setPreferredColumnWidth(JTable table, Integer[] preferredSizes) {
		for (int i = 0; i < preferredSizes.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredSizes[i]);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e == null) return;
		
		if (tablePositionen.getSelectionModel() == e.getSource()) {
			int viewRow = tablePositionen.getSelectedRow();
			if (viewRow < 0) return;
			IWebabfrageResult result = webabfrageViewController.getWebabfrageResultTableModel()
					.getResultAtRow(tablePositionen.convertRowIndexToModel(viewRow));
			
			foundpartsViewController.actionResultSelected(result);
			panelFoundPartsDetail.refresh(null);
			splitPaneErgebnis.setDividerLocation(0.5);
			
		} else if (tableFoundParts.getSelectionModel() == e.getSource()) {
			int viewRow = tableFoundParts.getSelectedRow();
			if (viewRow < 0) return;
			NormalizedWebabfragePart part = foundpartsViewController.getFoundPartsTableModel().getPartAtRow(tableFoundParts.convertRowIndexToModel(viewRow));
			panelFoundPartsDetail.refresh(part);
			foundpartsViewController.actionFoundPartSelected(part);
		}
	}

	@Override
	protected void eventActionSpecial(ActionEvent event) throws Throwable {
		if (event == null) return;
		
		myLogger.debug("eventActionSpecial : " + event.getActionCommand());
		if (PanelWebabfrageControl.ACTION_COMBOBOX_MENGENSTAFFEL.equals(event.getActionCommand())) {
			webabfrageViewController.actionMengenstaffelSelected(panelWebabfrageControl.getSelectedMengenstaffel());
			foundpartsViewController.actionMengenstaffelSelected();
		} else if (PanelWebabfrageControl.ACTION_BUTTON_ABFRAGEN.equals(event.getActionCommand())) {
			if (tablePositionen.getSelectedRowCount() >= WebabfrageResultController.WARNING_NUMBER_OF_REQUESTS_LIMIT) {
				if(!showNumberOfRequestsLimitWarning(tablePositionen.getSelectedRowCount())) {
					return;
				}
			}
			if (panelWebabfrageControl.isAlleAbfragenSelected()) {
				webabfrageViewController.actionStartWebabfrageAlle();
			} else if (panelWebabfrageControl.isSelektierteAbfragenSelected()) {
				webabfrageViewController.actionStartWebabfrageSelektierte(tablePositionen.getSelectedRows());
			}

		} else if (PanelWebabfrageControl.ACTION_BUTTON_ABFRAGE_ABBRECHEN.equals(event.getActionCommand())) {
			webabfrageViewController.actionCancelWebabfrage();

		} else if (PanelFoundPartsDetail.ACTION_PREIS_UEBERNEHMEN.equals(event.getActionCommand())) {
			foundpartsViewController.actionSetFoundPartToResult();
			webabfrageViewController.actionRefreshResultTable();
			Integer row = foundpartsViewController.getSelectedResultRow();
			if (row != null) {
				tablePositionen.setRowSelectionInterval(row, row);
			}
		} else if (ACTION_NUR_WEBLIEFERANTEN.equals(event.getActionCommand())) {
			panelFoundPartsDetail.refresh(null);
			if (wcbNurWeblieferanten.isSelected()) {
				foundpartsViewController.actionFilterNurWeblieferanten();
			} else {
				foundpartsViewController.actionFilterAlleLieferanten();
			}

		} else if (ACTION_FILTER_BESCHREIBUNG.equals(event.getActionCommand())) {
			panelFoundPartsDetail.refresh(null);
			foundpartsViewController.actionFilterBezeichnung(wtfBezeichungFilter.getText());
		}
	}
	
	private boolean showNumberOfRequestsLimitWarning(int selectedRows) {
		return DialogFactory.showModalJaNeinDialog(getInternalFrame(), 
				LPMain.getMessageTextRespectUISPr("agstkl.webabfrage.warnung.abfragelimitueberschritten", selectedRows), 
				LPMain.getTextRespectUISPr("lp.warning"));
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	public class RightMouseClickDisableSortListener extends MouseAdapter {
		private final JTable table;

		public RightMouseClickDisableSortListener(JTable table) {
			this.table = table;
		}

		@Override public void mouseClicked(MouseEvent event) {
			if (SwingUtilities.isRightMouseButton(event)) {
				if (table.columnAtPoint(event.getPoint()) >= 0) {
					table.getRowSorter().setSortKeys(null);
				}
			}
		}		
	}
	
}
