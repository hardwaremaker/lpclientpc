package com.lp.neweditor.ui.menu;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperFileSizeLabel;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.ImageScaling;
import com.lp.neweditor.data.block.ImageBlockModel;
import com.lp.neweditor.event.ModelChangedEvent;
import com.lp.neweditor.event.ModelChangedListener;
import com.lp.neweditor.ui.block.ImageBlockController;
import com.lp.server.system.service.HvImageDto;

public class ImageEditorStatusBar extends EditorStatusBar
		implements ModelChangedListener<ImageBlockModel>, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private WrapperFileSizeLabel displayStorage;
	private JLabel displaySize;

	public ImageEditorStatusBar(ImageBlockController controller) {
		this();
		install(controller);
	}

	public ImageEditorStatusBar() {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel labelSize = new JLabel(LpEditorMessages.getInstance().getString("Status.ImageSize") + ": ");
		displaySize = new JLabel();

		JLabel labelStorage = new JLabel(LpEditorMessages.getInstance().getString("Status.ImageBytes") + ": ");
		displayStorage = new WrapperFileSizeLabel();
		displayStorage.setPreferredSize(HelperClient.getSizeFactoredDimension(40));
		displayStorage.setHorizontalAlignment(JLabel.LEFT);

		add(labelSize);
		add(displaySize);

		add(labelStorage);
		add(displayStorage);
	}

	public void install(ImageBlockController controller) {
		controller.addModelChangeListener(this);
		setModel(controller.getModel());
	}

	private void setModel(ImageBlockModel model) {
		if (model != null) {
			HvImageDto dto = model.getImageDtoForDB();
			setImageData(dto);
			model.addPropertyChangeListener(this);
		}
	}

	private void setImageData(HvImageDto dto) {
		if (dto != null) {
			BufferedImage imgForSize = dto.getBufferedImage();
			String strSize = String.format("%dpx * %dpx", imgForSize.getWidth(), imgForSize.getHeight());
			displaySize.setText(strSize);
			displayStorage.setFileSize(dto.getImageBinaryData().length);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ImageBlockModel.propertyImage)) {
			setImageData((HvImageDto) evt.getNewValue());
		}
	}

	@Override
	public void dataChanged(ModelChangedEvent<ImageBlockModel> e) {
		setModel(e.getDataAfter());
	}
}
