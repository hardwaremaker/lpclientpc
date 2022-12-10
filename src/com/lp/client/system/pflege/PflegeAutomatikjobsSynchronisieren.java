package com.lp.client.system.pflege;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.server.system.service.AutomatikjobSyncResultDto;
import com.lp.server.system.service.AutomatikjobSyncResultDto.ResultType;

public class PflegeAutomatikjobsSynchronisieren implements IPflegefunktion {

	private JPanel panel;
	private JTable resultTable;
	private ResultTableModel model;

	private AtomicBoolean running = new AtomicBoolean();

	@Override
	public String getKategorie() {
		return KATEGORIE_ALLGEMEIN;
	}

	@Override
	public String getName() {
		return "Automatikjobs Synchronisieren";
	}

	@Override
	public String getBeschreibung() {
		return "F\u00fcgt bei allen Mandanten die Automatikjobs des aktuellen Mandaten ein, falls diese noch nicht exisiteren.\n"
				+ "Alle mit dieser Funktion angelegten Automatikjobs sind deaktiviert.";
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public boolean supportsProgressBar() {
		return false;
	}

	@Override
	public boolean isStartable() {
		return true;
	}

	private void execute() {
		try {
			AutomatikjobSyncResultDto res = DelegateFactory.getInstance().getAutomatikDelegate().syncAutomatikjobs();
			for (String mandant : res.getAlleAktualisiertenMandanten()) {
				Optional<ResultType> mandantRes = res.getResultForMandant(mandant);
				ResultRow row;
				if (!mandantRes.isPresent()) {
					row = new ResultRow(mandant, false, new Exception("Kein Ergebnis fuer Mandant gefunden"));
				} else {
					ResultType result = mandantRes.get();
					row = new ResultRow(mandant, result.success, result.getException());
				}
				model.addRow(row);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			DialogFactory.showModalDialog("Fehler",
					"Beim Synchronisieren der Automatikjobs ist ein schwerwiegender Fehler aufgetreten, Details befinden sich im Client Log.");
		} finally {
			btnStart.setEnabled(true);
			running.set(false);
		}
	}

	@Override
	public void run() {
		if (running.getAndSet(true)) {
			// bereits gestartet
			return;
		}
		btnStart.setEnabled(false);
		model.clear();
		execute();
	}

	@Override
	public void cancel() {

	}

	@Override
	public boolean isRunning() {
		return running.get();
	}

	@Override
	public void addPflegeEventListener(PflegeEventListener listener) {
	}

	@Override
	public void removeAllPflegeEventListeners() {

	}

	@Override
	public void init() {
		panel = new JPanel();
		model = new ResultTableModel();
		resultTable = new JTable(model);
		panel.setLayout(new BorderLayout());
		panel.add(resultTable, BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel();
		btnStart = new JButton("Start");
		btnStart.addActionListener(e -> this.run());
		bottomPanel.add(btnStart);
		panel.add(bottomPanel, BorderLayout.SOUTH);
	}

	@Override
	public void eventYouAreSelected() {

	}

	@Override
	public String toString() {
		return getName();
	}

	private static final int RESULT_COL_COUNT = 2;
	private JButton btnStart;

	private static class ResultRow {
		private static final String statusSuccess = "Erfolgreich";

		private String mandant;
		private boolean success;
		private Exception exception;

		public ResultRow(String mandant, boolean success, Exception exception) {
			super();
			this.mandant = mandant;
			this.success = success;
			this.exception = exception;
		}

		public String getMandant() {
			return mandant;
		}

		public String getStatus() {
			if (success) {
				return statusSuccess;
			} else {
				return exception.getMessage();
			}
		}
	}

	private static class ResultTableModel extends AbstractTableModel {
		private List<ResultRow> data;

		private static final String[] ColNames = new String[] { "Mandant", "Status" };

		public ResultTableModel() {
			data = new ArrayList<>();
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return RESULT_COL_COUNT;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return ColNames[columnIndex];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ResultRow row = data.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return row.getMandant();
			case 1:
				return row.getStatus();
			default:
				throw new IndexOutOfBoundsException("Column: " + columnIndex + " does not exist in this TableModel");
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			throw new RuntimeException("Setting a single Cell value is not supported in this TableModel");
		}

		public void clear() {
			int oldSize = data.size();
			data.clear();
			if (oldSize != 0) {
				fireTableRowsDeleted(0, oldSize - 1);
			}
		}

		public void addRow(ResultRow res) {
			data.add(res);
			fireTableRowsInserted(data.size() - 1, data.size() - 1);
		}

	}

}
