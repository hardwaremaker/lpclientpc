package com.lp.client.partner;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.lp.client.artikel.IVendidataExportController;
import com.lp.client.frame.DialogError;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.util.EJBExceptionLP;

public abstract class Dialog4VendingPartnerExport extends DialogExportBase implements TableModel {
	private static final long serialVersionUID = -2187512951075424761L;

	private IVendidataExportController<VendidataPartnerExportResult> controller;
	private List<EJBExceptionLP> exportErrors;
	private String[] columnNames;
	private Class[] columnClasses;

	public Dialog4VendingPartnerExport(Frame owner, String title, boolean modal,
			IVendidataExportController<VendidataPartnerExportResult> controller) {
		super(owner, title, modal, controller);
		this.controller = controller;
		setPreferredColumnWidth(new Integer[] {100, 100, 200, 600});
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				actionVerify();
				invalidate();
			}
		});
	}

	protected abstract void initTableModel();

	@Override
	protected void actionExport() {
		try {
			chooseFile();
			actionUIExport();
			setExportResult(controller.doExport());
			actionUIExportDone(controller.getFile() != null);
		} catch (Exception e) {
			this.setVisible(false);
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
			actionUIExportDone(false);
		}
	}

	private void setExportResult(VendidataPartnerExportResult result) {
		if (result == null) return;
		
		exportErrors = result.getExportErrors();
		setStats(result.getStats());
	}
	
	private void setStats(VendidataExportStats stats) {
		setTotalCounts(stats.getTotalExports());
		setErrorCounts(stats.getErrorCounts());
	}
	
	protected List<EJBExceptionLP> getExportErrors() {
		if (exportErrors == null) {
			exportErrors = new ArrayList<EJBExceptionLP>();
		}
		return exportErrors;
	}

	@Override
	protected void actionVerify() {
		actionUIVerify();
		setExportResult(controller.checkExport());
		actionUIVerificationDone(exportErrors.isEmpty());
	}

	protected String[] getColumnNames() {
		return columnNames;
	}

	protected void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	protected Class[] getColumnClasses() {
		return columnClasses;
	}

	protected void setColumnClasses(Class[] columnClasses) {
		this.columnClasses = columnClasses;
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return getColumnNames().length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return getExportErrors().size();
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
	}
	
	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
	}
		
}
