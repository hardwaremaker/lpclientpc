package com.lp.client.angebotstkl.webabfrage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.JButtonWithData;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.IWebpartnerDto;

public class NeueLieferantenModel extends AbstractTableModel implements ActionListener, IDataUpdateListener {
	
	private static final long serialVersionUID = -5111679639817379563L;
	public static final String ACTION_BUTTON_ZUORDNEN = "action_button_zuordnen";

	private List<IWebpartnerDto> undefinedWebpartner;
	private List<JButtonWithData<Integer>> zuordnenButtons; 
	private INeueLieferantenModelController controller;
	
	public static final int IDX_NAME = 0;
	public static final int IDX_ID = 1;
	public static final int IDX_BUTTON_ZUORDNEN = 2;
	
	private String [] columnNames = new String[] {
		LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.distributorname"),
		LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.distributorid"),
		""
	};
	private Class[] columnClasses = new Class[] {String.class, String.class, JButton.class};

	public NeueLieferantenModel(INeueLieferantenModelController controller) {
		undefinedWebpartner = new ArrayList<IWebpartnerDto>();
		zuordnenButtons = new ArrayList<JButtonWithData<Integer>>();
		this.controller = controller;
		
		this.controller.registerNeueLieferantenDataUpdateListener(this);
	}

	public List<IWebpartnerDto> getWebpartner() {
		return undefinedWebpartner;
	}

	public void setWebpartner(List<IWebpartnerDto> webpartner) {
		this.undefinedWebpartner = Collections.synchronizedList(webpartner);
		Collections.sort(undefinedWebpartner, new Comparator<IWebpartnerDto>() {

			@Override
			public int compare(IWebpartnerDto webpartner1, IWebpartnerDto webpartner2) {
				Boolean isWebpartner1Null = webpartner1 == null || webpartner1.getName() == null;
				Boolean isWebpartner2Null = webpartner2 == null || webpartner2.getName() == null;
				
				if (!isWebpartner1Null && !isWebpartner2Null) {
					return webpartner1.getName().compareToIgnoreCase(webpartner2.getName());
				}
				return isWebpartner1Null.compareTo(isWebpartner2Null);
			}
		});
		
		zuordnenButtons.clear();
		for (IWebpartnerDto dto : webpartner) {
			final JButtonWithData<Integer> button = new JButtonWithData<Integer>(
					LPMain.getTextRespectUISPr("agstkl.webabfrage.button.zuordnen"),
					dto.getIId());
			button.addActionListener(this);
			button.setActionCommand(ACTION_BUTTON_ZUORDNEN);
			zuordnenButtons.add(button);
		}
	}
	
	public Boolean isWebpartnerInModel(IWebpartnerDto webpartner) {
		if (webpartner == null || webpartner.getId() == null || webpartner.getWebabfrageIId() == null) return false;
		
		synchronized(undefinedWebpartner) {
			for (IWebpartnerDto dto : undefinedWebpartner) {
				if (webpartner.getWebabfrageIId().equals(webpartner.getWebabfrageIId()) 
						&& webpartner.getId().equals(dto.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event == null) return;
		
		if (ACTION_BUTTON_ZUORDNEN.equals(event.getActionCommand())) {
			JButtonWithData<Integer> button = (JButtonWithData<Integer>) event.getSource();
			controller.actionZuordnen(button.getData());
		}
	}
	
	
	@Override
	public void dataUpdated() {
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnClasses.length;
	}

	@Override
	public int getRowCount() {
		return undefinedWebpartner.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= undefinedWebpartner.size() 
				|| rowIndex >= zuordnenButtons.size()) return null;
		
		IWebpartnerDto dto = undefinedWebpartner.get(rowIndex);
		
		if (IDX_NAME == columnIndex) {
			return dto.getName();
		} else if (IDX_ID == columnIndex) {
			return dto.getId();
		} else if (IDX_BUTTON_ZUORDNEN == columnIndex) {
			return zuordnenButtons.get(rowIndex);
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
		if (IDX_BUTTON_ZUORDNEN == columnIndex) return true;
		
		return false;
	}

}
