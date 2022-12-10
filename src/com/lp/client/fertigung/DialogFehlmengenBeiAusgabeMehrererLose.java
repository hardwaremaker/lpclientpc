package com.lp.client.fertigung;

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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FehlmengenBeiAusgabeMehrererLoseDto;
import com.lp.util.BigDecimal3;
import com.lp.util.Helper;

public class DialogFehlmengenBeiAusgabeMehrererLose extends JDialog implements
		TableModel, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel panelError = new JPanel();

	TabbedPaneLos tpLos = null;
	Integer losIId = null;

	private JTable jTableResults;
	private JScrollPane jPanelTable;
	private List<FehlmengenBeiAusgabeMehrererLoseDto> importErrors;
	private WrapperButton wbuOkay = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private String[] columnNames = new String[] {
			LPMain.getTextRespectUISPr("fert.losausgabe.mehrere.fehlmengen.lager"),
			LPMain.getTextRespectUISPr("fert.losausgabe.mehrere.fehlmengen.artikel"),
			LPMain.getTextRespectUISPr("fert.losausgabe.mehrere.fehlmengen.bezeichnung"),
			LPMain.getTextRespectUISPr("fert.losausgabe.mehrere.fehlmengen.soll"),
			LPMain.getTextRespectUISPr("fert.losausgabe.mehrere.fehlmengen.lagerstand") };
	private Class[] columnClasses = new Class[] { String.class,
			WrapperGotoButton.class, String.class, BigDecimal3.class,
			BigDecimal3.class };

	public DialogFehlmengenBeiAusgabeMehrererLose(TabbedPaneLos tpLos,
			String title, boolean modal,
			List<FehlmengenBeiAusgabeMehrererLoseDto> fibuErrors)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), title, modal);
		this.tpLos = tpLos;
		losIId = tpLos.getLosDto().getIId();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		importErrors = fibuErrors;

		jbInit();
		pack();
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(this);
	}

	private void jbInit() throws Throwable {
		panelError.setLayout(new GridBagLayout());

		initTableResults();

		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(
				panelError,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						getInsets2(), 0, 0));

		panelError.add(jPanelTable, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				getInsets2(), 0, 0));

		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());

		panelButton.add(wbuOkay, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				getInsets2(), 100, 0));
		panelButton.add(wbuAbbrechen, new GridBagConstraints(1, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				getInsets2(), 100, 0));

		getContentPane().add(
				panelButton,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						getInsets2(), 0, 0));

		wbuOkay.setText(LPMain.getTextRespectUISPr("button.ok"));
		wbuOkay.addActionListener(this);
		wbuOkay.setVisible(true);

		wbuAbbrechen.setText(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbuAbbrechen.addActionListener(this);
		wbuAbbrechen.setVisible(true);
	}

	private Insets getInsets2() {
		return new Insets(2, 2, 2, 2);
	}

	private void initTableResults() throws Throwable {
		jTableResults = new JTable(this);
		jTableResults.setAutoCreateRowSorter(true);
		jTableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn buttonColumn = jTableResults.getColumnModel().getColumn(1);
		buttonColumn.setCellRenderer(new ButtonCellRenderer());
		jTableResults.addMouseListener(new JTableButtonMouseListener(
				jTableResults));

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				jTableResults.getModel());
		sorter.setComparator(1, new Comparator<WrapperGotoButton>() {
			public int compare(WrapperGotoButton b1, WrapperGotoButton b2) {
				return b1.getWrapperButton().getText()
						.compareTo(b2.getWrapperButton().getText());
			}
		});
		jTableResults.setRowSorter(sorter);

		jPanelTable = new JScrollPane(jTableResults);
		jPanelTable.setPreferredSize(new Dimension(750, 305));

		setPreferredColumnWith(jTableResults, new Integer[] { 130, 150, 250,
				100, 100 });
	}

	private void setPreferredColumnWith(JTable table, Integer[] preferredSizes) {
		for (int i = 0; i < preferredSizes.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredSizes[i]);
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return importErrors.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= importErrors.size())
			return null;

		FehlmengenBeiAusgabeMehrererLoseDto ex = importErrors.get(rowIndex);

		if (0 == columnIndex) {
			return ex.getLager();
		}
		if (1 == columnIndex) {

			WrapperGotoButton button = new WrapperGotoButton(
					com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);
			button.setOKey(ex.getArtikelIId());
			button.setText(ex.getArtikelnummer());
			return button;

		}
		if (2 == columnIndex) {
			return ex.getBezeichnung();
		}
		if (3 == columnIndex)
			try {
				return Helper.formatZahl(ex.getSollmenge(), Defaults
						.getInstance().getIUINachkommastellenMenge(), LPMain
						.getTheClient().getLocUi());
			} catch (Throwable e) {
				tpLos.handleException(e, true);
			}

		if (4 == columnIndex)

			try {
				return Helper.formatZahl(ex.getLagerstand(), Defaults
						.getInstance().getIUINachkommastellenMenge(), LPMain
						.getTheClient().getLocUi());
			} catch (Throwable e) {
				tpLos.handleException(e, true);
			}

		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == wbuOkay) {

			try {
				tpLos.loseAnhandStuecklistenbaumAusgeben(losIId);
			} catch (Throwable e1) {
				tpLos.handleException(e1, true);
			}
			setVisible(false);
		} else if (source == wbuAbbrechen) {
			setVisible(false);
		}
	}

	public class ButtonCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ButtonCellRenderer() {
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (null == value)
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);

			if (value instanceof WrapperGotoButton) {
				WrapperButton button = ((WrapperGotoButton) value)
						.getWrapperButton();
				if (isSelected) {
					button.setForeground(table.getSelectionForeground());
					button.setBackground(table.getSelectionBackground());
				} else {
					button.setForeground(table.getForeground());
					button.setBackground(UIManager
							.getColor("Button.background"));
				}
			}

			return value instanceof Component ? (Component) value : super
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
		}
	}

	public class JTableButtonMouseListener extends MouseAdapter {
		private final JTable table;

		public JTableButtonMouseListener(JTable table) {
			this.table = table;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int column = table.getColumnModel().getColumnIndexAtX(e.getX());
			int row = e.getY() / table.getRowHeight();

			if (row < table.getRowCount() && row >= 0
					&& column < table.getColumnCount() && column >= 0) {
				Object value = table.getValueAt(row, column);
				if (value instanceof WrapperGotoButton) {
					ActionEvent action = new ActionEvent(this, 0, "ACTION_GOTO");
					((WrapperGotoButton) value).actionPerformed(action);
				}
			}
		}
	}

}
