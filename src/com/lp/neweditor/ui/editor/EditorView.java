package com.lp.neweditor.ui.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.lp.neweditor.common.SizeUnit;
import com.lp.neweditor.ui.EditorUIComponent;
import com.lp.neweditor.ui.block.BlockView;
import com.lp.neweditor.ui.menu.EditorMenu;
import com.lp.neweditor.ui.menu.EditorStatusBar;

public class EditorView implements EditorUIComponent {
	private JPanel mainPanel;
	private EditorMenu menu;
	private EditorStatusBar statusBar;
	private EditorPagePane pagePanel;

	private boolean menuVisible = false;

	public EditorView() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		pagePanel = new EditorPagePane();
		EditorScrollablePane viewport = new EditorScrollablePane(pagePanel);
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.getViewport().add(viewport);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(8);
		scrollPanel.getHorizontalScrollBar().setUnitIncrement(8);

		pagePanel.setVisible(true);

		mainPanel.add(scrollPanel, BorderLayout.CENTER);
	}

	@Override
	public JComponent getUIComponent() {
		return mainPanel;
	}

	public void setMenu(EditorMenu newMenu) {
		if (menu != null) {
			mainPanel.remove(menu.getUIComponent());
		}
		this.menu = newMenu;
		updateMenu();
	}

	private void updateMenu() {
		if (menu != null) {
			if (menuVisible) {
				mainPanel.add(menu.getUIComponent(), BorderLayout.NORTH);
			} else {
				mainPanel.remove(menu.getUIComponent());
			}
			mainPanel.revalidate();
			mainPanel.repaint();
		}
	}

	public void setStatusBar(EditorStatusBar newStatus) {
		if (statusBar != null) {
			mainPanel.remove(statusBar);
		}
		this.statusBar = newStatus;
		updateStatusBar();
	}

	private void updateStatusBar() {
		if (statusBar != null) {
			if (menuVisible) {
				mainPanel.add(statusBar, BorderLayout.SOUTH);
			} else {
				mainPanel.remove(statusBar);
			}
			mainPanel.revalidate();
			mainPanel.repaint();
		}
	}

	protected void addBlock(BlockView block) {
		pagePanel.addBlock(block);
	}

	protected void addLine() {
		pagePanel.addLine();
	}

	protected void removeAllBlocks() {
		pagePanel.removeAllBlocks();
	}

	protected EditorPagePane getContent() {
		return pagePanel;
	}

	public void setSize(Dimension dim, SizeUnit unit) {
		pagePanel.setSize(dim, unit);
	}

	public void setWidth(double width, SizeUnit unit) {
		pagePanel.setWidth(width, unit);
	}

	public void setHeight(double height, SizeUnit unit) {
		pagePanel.setHeight(height, unit);
	}

	public void setMenuVisible(boolean menuVisible) {
		this.menuVisible = menuVisible;
		updateMenu();
	}

}
