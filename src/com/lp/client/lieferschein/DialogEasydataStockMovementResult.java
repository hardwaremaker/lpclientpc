package com.lp.client.lieferschein;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.EasydataImportResult;
import com.lp.server.lieferschein.service.EasydataImportStats;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.errors.StmException;
import com.lp.service.easydata.IEasydataErrorAction;
import com.lp.service.easydata.StmErrorFactory;

public class DialogEasydataStockMovementResult extends JDialog implements ActionListener {
	private static final long serialVersionUID = 5716401467865640251L;

	private EasydataStockMovementImportCtrl importController;
	private EasydataImportResult importResult;
	private List<IEasydataErrorAction> errorActions;
	
	private WrapperLabel wlaStatus;
	private WrapperLabel wlaTotalImports;
	private WrapperLabel wlaErrorCounts;
	private WrapperButton wbuImport;
	private WrapperButton wbuCancel;
	private WrapperButton wbuVerify;
	
	private JPanel panelImport;
	private JTable tableResults;
	private EasydataResultsTableModel tableModel;
	private JScrollPane panelTableResults;
	
	public DialogEasydataStockMovementResult(Frame owner, EasydataStockMovementImportCtrl importController) {
		super(owner, LPMain.getTextRespectUISPr("ls.import.easydata.lagerbewegung.dialogtitel"), false);
		this.importController = importController;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(1000, 400));
		jbInit();
		pack();
		invalidate();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
		
		actionVerify();
	}

	private void setErrors(List<StmException> stmExceptions) {
		StmErrorFactory errorFactory = new StmErrorFactory();
		errorActions.clear();
		for (StmException exc : stmExceptions) {
			getErrors().add(errorFactory.getEasydataErrorAction(exc));
		}
	}
	
	public List<IEasydataErrorAction> getErrors() {
		if (errorActions == null) {
			errorActions = new ArrayList<IEasydataErrorAction>();
		}
		return errorActions;
	}
	
	private boolean hasErrors() {
		return !getErrors().isEmpty();
	}
	
	private EasydataImportResult getResult() {
		return importResult != null ? importResult : new EasydataImportResult();
	}
	
	private EasydataImportStats getStats() {
		return getResult().getStats();
	}
	
	private void jbInit() {
		wlaStatus = new WrapperLabel();
		wlaStatus.setHorizontalAlignment(SwingConstants.LEFT);
		wlaTotalImports = new WrapperLabel();
		wlaTotalImports.setHorizontalAlignment(SwingConstants.LEFT);
		wlaErrorCounts = new WrapperLabel();
		wlaErrorCounts.setHorizontalAlignment(SwingConstants.LEFT);
		wbuCancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbuCancel.addActionListener(this);
		wbuVerify = new WrapperButton(LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.pruefen"));
		wbuVerify.addActionListener(this);
		wbuImport = new WrapperButton(LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.importieren"));
		wbuImport.addActionListener(this);
		wbuImport.setEnabled(false);
		
		tableModel = new EasydataResultsTableModel();
		tableResults = new JTable(tableModel);
		tableResults.setAutoCreateRowSorter(true) ;
//		tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
		panelTableResults = new JScrollPane(tableResults);
//		panelTableResults.setPreferredSize(new Dimension(1000, 250));
		
		TableColumn columnGoto = tableResults.getColumn(tableResults.getColumnName(1));
		columnGoto.setMaxWidth(30);
		columnGoto.setCellRenderer(new GotoButtonCellRenderer());
		tableResults.addMouseListener(new JTableButtonMouseListener(tableResults));

		JPanel panelButtons = new JPanel();
		HvLayout buttonsLayout = HvLayoutFactory.create(panelButtons, "ins 0", "5%[30%,fill]20[30%,fill]20[30%,fill]5%", "");
		buttonsLayout.add(wbuVerify).add(wbuImport).add(wbuCancel);

		panelImport = new JPanel();
		HvLayout dialogLayout = HvLayoutFactory.create(panelImport, "", "[fill,grow]", "[fill,grow|fill]");
		dialogLayout.add(panelTableResults).wrap()
			.add(wlaStatus).wrap()
			.add(wlaTotalImports).wrap()
			.add(wlaErrorCounts).wrap()
			.add(panelButtons);
		
		add(panelImport);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (wbuVerify == event.getSource()) {
			actionVerify();
		} else if (wbuImport == event.getSource()) {
			actionImport();
		} else if (wbuCancel == event.getSource()) {
			actionCancel();
		}
	}
	
	private void actionCancel() {
		dispose();
	}

	private void actionImport() {
		wlaStatus.setText(LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.importieredatei", importController.getFilename()));
		invalidate();
		EasydataImportResult result = importController.doImport();
		if (result == null) {
			actionCancel();
			return;
		}
		
		setImportResult(result);
		if (hasErrors()) {
			wlaStatus.setText(LPMain.getMessageTextRespectUISPr(
					"ls.import.easydata.lagerbewegung.importfehlgeschlagen", importController.getFilename()));
		} else {
			wlaStatus.setText(LPMain.getMessageTextRespectUISPr(
					"ls.import.easydata.lagerbewegung.dateigeprueft", importController.getFilename()));
			wbuCancel.setText(LPMain.getTextRespectUISPr("button.ok"));
		}
		wbuVerify.setEnabled(hasErrors());
		wbuImport.setEnabled(hasErrors());
		
		setFieldsForStatsAfterImport();
	}

	private void actionVerify() {
		wlaStatus.setText(LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.pruefedatei", importController.getFilename()));
		invalidate();
		setImportResult(importController.checkImport());
		
		wlaStatus.setText(LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.dateigeprueft", importController.getFilename()));
		wbuImport.setEnabled(!hasErrors());
		
		setFieldsForStatsAfterVerification();
	}

	private void setFieldsForStatsAfterVerification() {
		if (getStats().getTotalImports().equals(1)) {
			wlaTotalImports.setText(LPMain.getTextRespectUISPr(
					"ls.import.easydata.lagerbewegung.verify.singletotalimports"));
		} else {
			wlaTotalImports.setText(LPMain.getMessageTextRespectUISPr(
					"ls.import.easydata.lagerbewegung.verify.multipletotalimports", 
					String.valueOf(getStats().getTotalImports())));
		}
		setErrorCounts();
	}
	
	private void setFieldsForStatsAfterImport() {
		if (getStats().getTotalImports().equals(1)) {
			wlaTotalImports.setText(LPMain.getTextRespectUISPr(
					"ls.import.easydata.lagerbewegung.singletotalimports")
					+ " " + getAffectedLieferscheine());
		} else {
			wlaTotalImports.setText(LPMain.getMessageTextRespectUISPr(
					"ls.import.easydata.lagerbewegung.multipletotalimports", 
					String.valueOf(getStats().getTotalImports()))
					+ " " + getAffectedLieferscheine());
		}
		
		setErrorCounts();
	}
	
	private void setErrorCounts() {
		if (getStats().getErrorCounts().equals(1)) {
			wlaErrorCounts.setForeground(Color.RED);
			wlaErrorCounts.setText(LPMain.getTextRespectUISPr("ls.import.easydata.lagerbewegung.einfehler"));
		} else if (getStats().getErrorCounts().compareTo(1) > 0) {
			wlaErrorCounts.setForeground(Color.RED);
			wlaErrorCounts.setText(LPMain.getMessageTextRespectUISPr("ls.import.easydata.lagerbewegung.mehrerefehler", 
					String.valueOf(getStats().getErrorCounts())));
		} else {
			wlaErrorCounts.setForeground(new Color(0, 204, 102));
			wlaErrorCounts.setText(LPMain.getTextRespectUISPr("ls.import.easydata.lagerbewegung.keinefehler"));
			wlaErrorCounts.setForeground(new Color(0, 204, 102));
		}
	}

	private String getAffectedLieferscheine() {
		StringBuilder builder = new StringBuilder();
		Iterator<LieferscheinDto> iter = getResult().getLieferscheine().iterator();
		while (iter.hasNext()) {
			builder.append(iter.next().getCNr());
			if (iter.hasNext()) {
				builder.append(", ");
			}
		}
		
		return builder.toString();
	}
	
	private void setImportResult(EasydataImportResult result) {
		if (result == null) return;
		
		setErrors(result.getErrors());
		importResult = result;
		tableModel.fireTableDataChanged();
	}
	
	protected class EasydataResultsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -9055015979722290309L;

		private String[] columnNames;
		private Class<?>[] columnClasses;

		public EasydataResultsTableModel() {
			columnClasses = new Class[] {String.class, JPanel.class};
			columnNames = new String[] {
					LPMain.getTextRespectUISPr("ls.import.easydata.lagerbewegung.fehlerbezeichnung"),
					LPMain.getTextRespectUISPr("ls.import.easydata.lagerbewegung.goto")
			};
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return columnClasses[columnIndex];
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			return getErrors().size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex >= getErrors().size()) return null;
			
			if (columnIndex == 0) return getMessage(rowIndex);
			if (columnIndex == 1) return getGoto(rowIndex);
			
			return null;
		}
	}

	private WrapperGotoButton getGoto(int rowIndex) {
		return getErrors().get(rowIndex).getGoto();
	}

	private String getMessage(int rowIndex) {
		return getErrors().get(rowIndex).getMessage();
	}

	public class JTableButtonMouseListener extends MouseAdapter {
		private final JTable table;

		public JTableButtonMouseListener(JTable table) {
			this.table = table;
		}

		@Override public void mouseClicked(MouseEvent e) {
			int column = table.getColumnModel().getColumnIndexAtX(e.getX());
			int row    = e.getY()/table.getRowHeight(); 

			if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
				Object value = table.getValueAt(row, column);
				if (value instanceof WrapperGotoButton) {
					ActionEvent action  = new ActionEvent(this, 0, "ACTION_GOTO") ;
					((WrapperGotoButton)value).actionPerformed(action);
				}
			}
		}		
	}

	public class GotoButtonCellRenderer extends DefaultTableCellRenderer  {
		private static final long serialVersionUID = 1L;

		public GotoButtonCellRenderer() {
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if(null == value) return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) ; 

			if(value instanceof WrapperGotoButton) {
				WrapperGotoButton button = (WrapperGotoButton) value;
				if(isSelected) {
					button.setForeground(table.getSelectionForeground());
					button.setBackground(table.getSelectionBackground());					
				} else {
					button.setForeground(table.getForeground());
					button.setBackground(table.getBackground());
				}
			}
			
			return value instanceof Component ?
					(Component) value : 
					super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) ;
		}
	}
}
