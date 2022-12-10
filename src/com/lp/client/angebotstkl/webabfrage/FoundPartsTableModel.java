package com.lp.client.angebotstkl.webabfrage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import com.lp.client.frame.component.JButtonWithData;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

public class FoundPartsTableModel extends AbstractTableModel implements IFoundPartsTableModelListener, ActionListener {

	private static final long serialVersionUID = -8928959420026049075L;
	private static final String ACTION_BUTTON_BUYERURL = "action_button_buyerurl";
	
	public static final int IDX_URL = 0;
	public static final int IDX_PARTNAME = 1;
	public static final int IDX_BESCHREIBUNG = 2;
	public static final int IDX_LIEFERANT = 3;
	public static final int IDX_HERSTELLER = 4;
	public static final int IDX_BESTAND = 5;
	public static final int IDX_MENGE = 6;
	public static final int IDX_PREIS = 7;
	
	private List<NormalizedWebabfragePart> parts;
	private IFoundPartsTableModelController controller;
	private List<JButtonWithData<String>> urlButtons;
	
	private String[] columnNames = new String[] {
			LPMain.getTextRespectUISPr("agstkl.webabfrage.url"),
			LPMain.getTextRespectUISPr("lp.name"),
			LPMain.getTextRespectUISPr("lp.beschreibung"),
			LPMain.getTextRespectUISPr("lp.lieferant"),
			LPMain.getTextRespectUISPr("lp.hersteller"),
			LPMain.getTextRespectUISPr("agstkl.webabfrage.bestand"),
			LPMain.getTextRespectUISPr("agstkl.webabfrage.benoetigtemenge"),
			LPMain.getTextRespectUISPr("lp.preis")
	};
	private Class[] columnClasses = new Class[] {JButton.class, String.class, String.class, String.class, 
			String.class, BigDecimal.class, BigDecimal.class, String.class};

	public FoundPartsTableModel(IFoundPartsTableModelController controller) {
		this.controller = controller;
		this.controller.registerDataUpdateListener(this);
		parts = new ArrayList<NormalizedWebabfragePart>();
		urlButtons = new ArrayList<JButtonWithData<String>>();
	}

	public void setParts(List<NormalizedWebabfragePart> parts) {
		this.parts = parts;
		
		urlButtons.clear();
		for (NormalizedWebabfragePart part : parts) {
			JButtonWithData<String> button = new JButtonWithData<String>(
					new ImageIcon(getClass().getResource("/com/lp/client/res/earth_view.png")), 
					part.getUrl());
			button.addActionListener(this);
			button.setActionCommand(ACTION_BUTTON_BUYERURL);
			button.setToolTipText(LPMain.getMessageTextRespectUISPr("agstkl.webabfrage.tooltip.buyerurl", part.getUrl()));
			urlButtons.add(button);
		}
	}

	@Override
	public void dataUpdated() {
		setParts(controller.getParts());
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return parts.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= parts.size() || rowIndex >= urlButtons.size()) return null;
		
		NormalizedWebabfragePart part = parts.get(rowIndex);
		
		if (columnIndex == IDX_URL) {
			return urlButtons.get(rowIndex);
		}
		if (columnIndex == IDX_PARTNAME) {
			return part.getPartName();
		}
		if (columnIndex == IDX_BESCHREIBUNG) {
			return part.getDescription();
		}
		if (columnIndex == IDX_LIEFERANT) {
			return part.getDistributor().getName();
		}
		if (columnIndex == IDX_HERSTELLER) {
			return part.getManufacturer();
		}
		if (columnIndex == IDX_BESTAND) {
			return Helper.formatZahl(part.getStock(), LPMain.getInstance().getUISprLocale());
		}

		PartPrice price = part.getPriceByQuantity(controller.getSelectedMenge().multiply(controller.getSelectedPosition().getMenge()));
		if (columnIndex == IDX_MENGE) {
			return price != null && price.getQuantityFrom() != null ? 
					Helper.formatZahl(price.getQuantityFrom(), WebabfrageBaseModel.getIUINachkommastellenMenge(), 
							LPMain.getInstance().getUISprLocale()) : null;
		}
		if (columnIndex == IDX_PREIS) {
			String sPrice = price != null && price.getPrice() != null ? 
					Helper.formatZahl(price.getPrice(), WebabfrageBaseModel.getIUINachkommastellenPreiseEK(), 
							LPMain.getInstance().getUISprLocale()) : null;
			if (price != null && sPrice != null) {
				sPrice = sPrice + " " + price.getCurrency();
			}
			return sPrice;
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
		return false;
	}

	@Override
	public void setValueAt(Object arg0, int rowIndex, int columnIndex) {
	}

	public NormalizedWebabfragePart getPartAtRow(int row) {
		return parts != null && parts.size() > row ? parts.get(row) : null;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event == null) return;
		
		if (ACTION_BUTTON_BUYERURL.equals(event.getActionCommand())) {
			JButtonWithData<String> button = (JButtonWithData<String>) event.getSource();
			controller.actionUrlButtonPressed(button.getData());
		}
	}

}
