package com.lp.neweditor.ui.block;

import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.neweditor.common.ImageScaling;
import com.lp.neweditor.ui.menu.EditorMenu;
import com.lp.neweditor.ui.menu.EditorStatusBar;
import com.lp.neweditor.ui.menu.ImageEditorMenu;
import com.lp.neweditor.ui.menu.ImageEditorStatusBar;
import com.lp.neweditor.ui.popup.BasicPopupMenu;
import com.lp.neweditor.ui.popup.ImagePopupMenu;
import com.lp.util.EditorBlockAlignment;

public class ImageBlockView extends BlockView {
	private final ScalingImageUI ui;
	private final ImageEditorMenu menu;
	private final ImageEditorStatusBar status;

	private BufferedImage img;
	private ImageScaling scaling = ImageScaling.noScaling;
	private FlowLayout flowLayout;
	private JPanel panel;

	public ImageBlockView(ImageBlockController controller) {
		super(controller);
		flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(flowLayout);
		ui = new ScalingImageUI();
		menu = new ImageEditorMenu(controller);
		status = new ImageEditorStatusBar(controller);
		panel.add(ui);
		ui.addMouseListener(popupMenu.getMouseListener());
		ui.setOpaque(false);
		ui.setFocusable(true);
		ui.addFocusListener(new UIFocusBorder(ui));
		ui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ui.requestFocusInWindow();
			}
		});
	}

	@Override
	public JComponent getUIComponent() {
		return panel;
	}

	@Override
	public EditorMenu getMenuForBlock() {
		return menu;
	}

	@Override
	protected BasicPopupMenu createPopup() {
		return new ImagePopupMenu(getController());
	}

	@Override
	public void getFocusClickBehind() {
	}

	@Override
	public ImageBlockController getController() {
		return (ImageBlockController) super.getController();
	}

	public void setImage(BufferedImage img) {
		this.img = img;
		ui.setImage(img, this.scaling);
	}

	public void setAlignment(EditorBlockAlignment alignment) {
		switch (alignment) {
		case CENTER:
			flowLayout.setAlignment(FlowLayout.CENTER);
			break;
		case LEFT:
			flowLayout.setAlignment(FlowLayout.LEFT);
			break;
		case RIGHT:
			flowLayout.setAlignment(FlowLayout.RIGHT);
			break;
		default:
			break;

		}
	}

	public void setScaling(ImageScaling scaling) {
		this.scaling = scaling;
		ui.setImage(this.img, scaling);
	}

	@Override
	public EditorStatusBar getStatusBar() {
		return status;
	}

	private static class UIFocusBorder implements FocusListener {
		private final JComponent ui;

		private final Border noFocusBorder;
		private final Border focusBorder;

		public UIFocusBorder(JComponent comp) {
			this.ui = comp;
			noFocusBorder = BorderFactory.createEmptyBorder();
			focusBorder = BorderFactory.createLineBorder(Defaults.getInstance().getTextfieldBorderColor(), 2);
		}

		@Override
		public void focusGained(FocusEvent e) {
			ui.setBorder(focusBorder);
		}

		@Override
		public void focusLost(FocusEvent e) {
			ui.setBorder(noFocusBorder);
		}

	}

	@Override
	public void setEditable(boolean editable) {
		ui.setFocusable(editable);
	}

	@Override
	public void addFocusListener(FocusListener listener) {
		ui.addFocusListener(listener);
	}

}
