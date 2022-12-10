package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

public class WebabfrageResultTableModel extends AbstractTableModel implements IWebabfrageResultTableUpdateListener {

	private static final long serialVersionUID = 4869175555321207295L;
	
	public static final int IDX_MENGE = 0;
	public static final int IDX_BEZEICHNUNG = 1;
	public static final int IDX_HERSTELLERARTIKELNUMMER = 2;
	public static final int IDX_USERSTRING = 3;
	public static final int IDX_PREIS = 4;
	public static final int IDX_LIEFERANT = 5;
	
	private String[] columnNames = new String[] {
			LPMain.getTextRespectUISPr("lp.menge"),
			LPMain.getTextRespectUISPr("lp.bezeichnung"),
			LPMain.getTextRespectUISPr("artikel.herstellernr"),
			LPMain.getTextRespectUISPr("agstkl.webabfrage.userstring"),
			LPMain.getTextRespectUISPr("lp.preis"),
			LPMain.getTextRespectUISPr("lp.lieferant")	};
	private Class[] columnClasses = new Class[] {BigDecimal.class, String.class, String.class, String.class, BigDecimal.class, String.class};
	
	private List<IWebabfrageResult> results;
	private IWebabfrageResultModelController controller;

	public WebabfrageResultTableModel(IWebabfrageResultModelController controller, List<IWebabfrageResult> results) {
		this.controller = controller;
		this.controller.registerDataUpdateListener(this);
	}
	
	@Override
	public void dataUpdated() {
		this.results = controller.getWebabfragePositionen();
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return results.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= results.size()) return null;
		
		IWebabfrageResult result = results.get(rowIndex);
		
		if (columnIndex == IDX_BEZEICHNUNG) {
			return result.getName();
		} else if (columnIndex == IDX_MENGE) {
			return Helper.formatZahl(result.getMenge(), WebabfrageBaseModel.getIUINachkommastellenMenge(), 
					LPMain.getInstance().getUISprLocale());
		} else if (columnIndex == IDX_HERSTELLERARTIKELNUMMER) {
			return result.getEinkaufsangebotpositionDto().getCArtikelnrhersteller();
		} else if (columnIndex == IDX_USERSTRING) {
			return result.getUserString();
		} else if (columnIndex == IDX_PREIS) {
			if (result.getSelectedPart() == null) return null;
			PartPrice partPrice = result.getSelectedPart().getPriceByQuantity(
					controller.getSelectedEinkaufsangebotMenge().multiply(result.getMenge()));

			return Helper.formatZahl(partPrice.getPrice(), WebabfrageBaseModel.getIUINachkommastellenPreiseEK(), 
					LPMain.getInstance().getUISprLocale());
		} else if (columnIndex == IDX_LIEFERANT) {
			return result.getSelectedPart() == null ? null : result.getSelectedPart().getDistributor().getName();
		} 
		
		return null;
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
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == IDX_USERSTRING) {
			return true;
		}
		return false;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (columnIndex == IDX_USERSTRING && value instanceof String) {
			IWebabfrageResult result = getResultAtRow(rowIndex);
			result.setUserString((String)value);
		}
	}

	public IWebabfrageResult getResultAtRow(int row) {
		return results != null && results.size() > row ? results.get(row) : null;
	}

}
