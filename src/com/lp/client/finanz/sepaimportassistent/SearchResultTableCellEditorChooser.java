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
package com.lp.client.finanz.sepaimportassistent;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.util.BelegAdapter;

public class SearchResultTableCellEditorChooser extends JComboBox implements
		TableCellEditor {

	private static final long serialVersionUID = 1L;
	
	private ISepaImportResult result;
	private List<CellEditorListener> listeners;
	private ComboBoxListener cbListener;
	private ISepaCellEditorController controller;

	public SearchResultTableCellEditorChooser(ISepaCellEditorController controller) {
		listeners = new ArrayList<CellEditorListener>();
		cbListener = new ComboBoxListener();
		setRenderer(new SearchResultListCellRenderer());
		this.controller = controller;
	}
	
	@Override
	public void addCellEditorListener(CellEditorListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void cancelCellEditing() {
		hidePopup();
	}

	@Override
	public Object getCellEditorValue() {
		return result;
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(l);
	}

	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		if (getParent() != null) getParent().repaint();
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		return false;
	}

	private void fireEditingStopped() {
		if(listeners.size() > 0) {
			listeners.get(0).editingStopped(new ChangeEvent(this));
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		if (value instanceof ISepaImportResult) {
			result = (ISepaImportResult) value;
			removeItemListener(cbListener);
			removePopupMenuListener(cbListener);
			BelegAdapter[] belegArray = result.getFoundItems()
					.toArray(new BelegAdapter[result.getFoundItems().size()]);
			setModel(new DefaultComboBoxModel(belegArray));
			setSelectedIndex(result.getSelectedIndex() == null ? -1 : result.getSelectedIndex());
			addItemListener(cbListener);
			addPopupMenuListener(cbListener);
			setMinimumSize(getPreferredSize());
			
			return this;
		}
		return new JPanel();
	}
	
	private class ComboBoxListener implements PopupMenuListener, ItemListener {

		private boolean stateChanged = false;
		
		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
//			fireEditingStopped();
			controller.dataUpdated();
			result.setSelectedIndex(getSelectedIndex());
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			fireEditingStopped();
			if (stateChanged == false) {
				controller.actionNewSelection(result, getSelectedIndex());
			}
			stateChanged = false;
//			setSelectedIndex(result.getSelectedIndex());
		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if ((result.getSelectedIndex() == null || result.getSelectedIndex() < 0) && getSelectedIndex() >= 0) {
				result.setEditedByUser(true);
			}
			
			if (e.getStateChange() == ItemEvent.SELECTED) {
				stateChanged = true;
				Integer previousIndex = result.getSelectedIndex();
				result.setSelectedIndex(getSelectedIndex());
				controller.actionNewSelection(result, previousIndex);
				if (getParent() != null) getParent().repaint();
				fireEditingStopped();
			}
		}
		
	}

}
