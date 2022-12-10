package com.lp.client.angebotstkl.webabfrage;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

public class PanelFoundPartsDetail extends JPanel implements TableModel {

	private static final long serialVersionUID = -8432316774378744338L;
	public static final String ACTION_PREIS_UEBERNEHMEN = "action_preis_uebernehmen";
	
	private QuantityScale quantityScale = new QuantityScale(new ArrayList<PartPrice>());
	private WrapperLabel wlMengenstaffel = new WrapperLabel();
	private JTable tableMengenstaffel;
	private WrapperButton wbUebernehmen;
	private ActionListener headPanelActionListener;
	
	private static final int IDX_MENGE = 0;
	private static final int IDX_PREIS = 1;
	private static final int IDX_WERT = 2;

	private Class[] columnClasses = {JButton.class, BigDecimal.class, BigDecimal.class};
	private String[] columnNames = {
			LPMain.getTextRespectUISPr("agstkl.webabfrage.benoetigtemenge"),
			LPMain.getTextRespectUISPr("lp.preis"),
			LPMain.getTextRespectUISPr("lp.gesamtwert")
	};
	
	public PanelFoundPartsDetail(ActionListener headPActionListener) {
		this.headPanelActionListener = headPActionListener;
		refresh(null);
//		jbInit();
	}
	
	public void refresh(NormalizedWebabfragePart part) {
		setQuantityScale(part == null ? new QuantityScale(new ArrayList<PartPrice>()) : part.getQuantityScale());
		removeAll();
		jbInit();
		
		validate();
	}

	private void jbInit() {
		wbUebernehmen = new WrapperButton(new ImageIcon(getClass().getResource("/com/lp/client/res/check2.png")));
		wbUebernehmen.addActionListener(headPanelActionListener);
		wbUebernehmen.setActionCommand(ACTION_PREIS_UEBERNEHMEN);
		wbUebernehmen.setToolTipText(LPMain.getTextRespectUISPr("lp.uebernehmen"));
		wlMengenstaffel.setText(LPMain.getTextRespectUISPr("as.agstkl.mengenstaffel"));
		tableMengenstaffel = new JTable();
		tableMengenstaffel.setModel(this);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		tableMengenstaffel.getColumnModel().getColumn(IDX_MENGE).setCellRenderer(rightRenderer);
		tableMengenstaffel.getColumn(tableMengenstaffel.getColumnModel().getColumn(IDX_PREIS).getIdentifier()).
				setPreferredWidth(PanelWebabfrageEinkaufsangebot.COLUMN_SIZE_PRICE);

		setLayout(new MigLayout("wrap 3, hidemode 0, insets 5 0 0 0", "[30px,fill,grow|50%,fill,grow|40%,fill,grow]", "[fill]"));
		add(wbUebernehmen, "aligny top");
		add(wlMengenstaffel, "alignx right, aligny top");
		add(new JScrollPane(tableMengenstaffel), "alignx right");
	}

	private List<PartPrice> getPrices() {
		if (quantityScale == null || quantityScale.getPrices() == null) {
			return new ArrayList<PartPrice>();
		}
		
		return quantityScale.getPrices();
	}

	private void setQuantityScale(QuantityScale quantityScale) {
		this.quantityScale = quantityScale;
	}

	private QuantityScale getQuantityScale() {
		return quantityScale;
	}

	public void setSelectedPriceIndex(Integer selectedIndex) {
		getQuantityScale().setSelectedPriceIndex(selectedIndex);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex >= columnClasses.length ? null : columnClasses[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnClasses.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnIndex >= columnNames.length ? null : columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return getPrices().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= getPrices().size()) return null;
		
		PartPrice partPrice = getPrices().get(rowIndex);
		if (partPrice == null || partPrice.getPrice() == null) return null;
		
		if (IDX_MENGE == columnIndex) {
			return Helper.formatZahl(partPrice.getQuantityFrom(), WebabfrageBaseModel.getIUINachkommastellenMenge(), 
					LPMain.getInstance().getUISprLocale());
		} else if (IDX_PREIS == columnIndex) {
			return Helper.formatZahl(partPrice.getPrice(), WebabfrageBaseModel.getIUINachkommastellenPreiseEK(), 
					LPMain.getInstance().getUISprLocale()) + " " + partPrice.getCurrency();
		} else if (IDX_WERT == columnIndex) {
			BigDecimal wert = partPrice.getPrice().multiply(partPrice.getQuantityFrom() != null ? partPrice.getQuantityFrom() : BigDecimal.ZERO);
			return Helper.formatZahl(wert, WebabfrageBaseModel.getIUINachkommastellenPreiseEK(), 
					LPMain.getInstance().getUISprLocale()) + " " + partPrice.getCurrency();
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
	
		
}
