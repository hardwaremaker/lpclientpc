package com.lp.neweditor.ui.block;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.lp.neweditor.common.EditorZoomFaktor;
import com.lp.neweditor.common.ImageScaling;
import com.lp.neweditor.data.block.ImageBlockModel;
import com.lp.neweditor.data.block.ImageSize;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.service.HvImageDto;
import com.lp.util.EditorBlockAlignment;

public class ImageBlockController extends BasicBlockController<ImageBlockModel, ImageBlockView> {

	public ImageBlockController(HvBlockEditor editor) {
		super(editor);
	}

	@Override
	public boolean canBeDragged() {
		return false;
	}

	@Override
	public ImageBlockModel createDefaultModel() {
		return new ImageBlockModel();
	}

	@Override
	protected ImageBlockView createView() {
		return new ImageBlockView(this);
	}

	@Override
	protected void setZoom(EditorZoomFaktor zoom) {
		getView().setScaling(getModel().getScaling(HvBlockEditor.DISPLAY_DPI).andThenDo(zoom));
	}

	@Override
	protected void attachToModel(ImageBlockModel model) {
		super.attachToModel(model);
		getView().setImage(model.getImage());
		getView().setScaling(model.getScaling(HvBlockEditor.DISPLAY_DPI).andThenDo(getZoom()));
	}

	public Image getImage() {
		return getModel().getImage();
	}

	public void setImage(HvImageDto image) {
		getModel().setImage(image);
	}

	public void setSize(ImageSize size) {
		getModel().setSize(size);
	}

	@Override
	protected BlockEventSupport createEventSupport() {
		return new ImageEventSupport();
	}

	protected class ImageEventSupport extends BlockEventSupport {

		@Override
		protected boolean tryHandlePropertyChange(PropertyChangeEvent evt) {
			switch (evt.getPropertyName()) {
			case ImageBlockModel.propertyImage:
				getView().setImage(((HvImageDto) evt.getNewValue()).getBufferedImage());
				return true;
			case ImageBlockModel.propertySize:
				ImageSize scaling = (ImageSize) evt.getNewValue();
				getView().setScaling(scaling.toScaling(HvBlockEditor.DISPLAY_DPI).andThenDo(getZoom()));
				return true;
			case ImageBlockModel.propertyAlignment:
				EditorBlockAlignment alignment = (EditorBlockAlignment) evt.getNewValue();
				getView().setAlignment(alignment);
			default:
				return false;
			}
		}

	}
}
