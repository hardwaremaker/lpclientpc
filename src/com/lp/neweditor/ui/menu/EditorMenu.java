package com.lp.neweditor.ui.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.lp.client.frame.Defaults;
import com.lp.editor.ui.LpToolBarButton;
import com.lp.neweditor.action.BlockEditorActions;
import com.lp.neweditor.common.EditorZoomFaktor;
import com.lp.neweditor.common.ItemToPropertyAdapter;
import com.lp.neweditor.event.PropertyChangeSupplier;
import com.lp.neweditor.ui.EditorUIComponent;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.neweditor.ui.popup.BasicPopupMenu;

/**
 * Basisklasse f&uuml;r EditorMenus des {@link HvBlockEditor}. <br>
 * EditorMenus werden, wenn der Editor im editiermodus ist, ganz oben angezeigt
 * und besitzen Toolbars, etc. <br>
 * F&uuml;r Popup Menus siehe {@link BasicPopupMenu}
 * 
 * @author Alexander Daum
 *
 */
public abstract class EditorMenu implements EditorUIComponent, PropertyChangeSupplier {
	private JComboBox<EditorZoomFaktor> jComboBoxZoom;
	private JComponent toolBar = null;

	protected final EditorBlock<?> controller;
	protected final PropertyChangeSupport propChangeSupport;

	/**
	 * F&uuml;ge alle Menuelemente der Toolbar hinzu. <br>
	 * Diese Methode beim ersten Aufruf von {@link EditorMenu#getUIComponent()}
	 * aufgerufen.
	 * 
	 */
	protected void populateToolBar(JComponent toolBar) {
		BlockEditorActions actions = controller.getEditor().getActions();
		LpToolBarButton lpButtonEditUndo = new LpToolBarButton();
		lpButtonEditUndo.setAction(actions.getUndoAction());
		lpButtonEditUndo.setFocusable(false);
		LpToolBarButton lpButtonEditRedo = new LpToolBarButton();
		lpButtonEditRedo.setAction(actions.getRedoAction());
		lpButtonEditRedo.setFocusable(false);

		JToolBar toolBarUndo = createNewToolbar();
		toolBarUndo.add(lpButtonEditUndo);
		toolBarUndo.add(lpButtonEditRedo);

		toolBar.add(toolBarUndo);
	}

	protected JToolBar createNewToolbar() {
		JToolBar toolBarUndo = new JToolBar();
		toolBarUndo.setFloatable(false);
		toolBarUndo.setRollover(true);
		return toolBarUndo;
	}

	public EditorMenu(EditorBlock<?> controller) {
		this.controller = controller;
		propChangeSupport = new PropertyChangeSupport(this);
		getZoomCombobox().setSelectedItem(controller.getEditor().getZoom());
	}

	private void initUI() {
		toolBar = new JPanel();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEADING));
		populateToolBar(toolBar);

		JToolBar tb = createNewToolbar();
		toolBar.add(tb);
		tb.add(controller.getEditor().getActions().getActionDeleteBlock()).setFocusable(false);

		toolBar.add(getZoomCombobox());
		Dimension d = toolBar.getPreferredSize();
		double prefHeight = d.getHeight();
//		System.out.println("prefHeight toolbar = " + prefHeight);
		d.setSize(d.getWidth(), Math.max(prefHeight, 46d));
		toolBar.setPreferredSize(d);
	}

	private JComboBox<EditorZoomFaktor> getZoomCombobox() {
		if (jComboBoxZoom == null) {
			EditorZoomFaktor[] zoomFaktors = new EditorZoomFaktor[] { new EditorZoomFaktor(50),
					new EditorZoomFaktor(75), new EditorZoomFaktor(100), new EditorZoomFaktor(125),
					new EditorZoomFaktor(150), new EditorZoomFaktor(175), new EditorZoomFaktor(200) };
			jComboBoxZoom = new JComboBox<>(zoomFaktors);
			jComboBoxZoom.setPreferredSize(
					new Dimension(Defaults.getInstance().bySizeFactor(100), jComboBoxZoom.getPreferredSize().height));
			jComboBoxZoom
					.addItemListener(new ItemToPropertyAdapter(HvBlockEditor.propertyZoomFaktor, propChangeSupport));

//			Dimension d = jComboBoxZoom.getPreferredSize();
//			double prefHeight = d.getHeight();
//			System.out.println("prefHeight zoom = " + prefHeight);
		}
		return jComboBoxZoom;
	}

	@Override
	public JComponent getUIComponent() {
		if (toolBar == null) {
			initUI();
		}
		return toolBar;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public void setZoom(EditorZoomFaktor newZoomFactor) {
		getZoomCombobox().setSelectedItem(newZoomFactor);
	}
	
}
