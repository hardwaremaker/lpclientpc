package com.lp.neweditor.data.block;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Objects;

import com.lp.neweditor.common.ImageScaling;
import com.lp.neweditor.common.SizeUnit;
import com.lp.neweditor.data.boundproperty.BoundProperty;
import com.lp.neweditor.data.boundproperty.NonNullBoundProperty;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorImageBlockDto;
import com.lp.server.system.service.HvImageDto;
import com.lp.server.system.service.MediaFac;
import com.lp.util.EditorBlockAlignment;

public class ImageBlockModel extends BaseBlockModel {
	public static final String propertyAlignment = "alignment";
	/**
	 * Property image <br>
	 * Typ: {@link Image}
	 */
	public static final String propertyImage = "image";
	/**
	 * Property Gr&ouml;&szig;e des Bildes. Typ: {@link ImageSize}
	 */
	public static final String propertySize = "size";

	private final BoundProperty<HvImageDto> image;
	private final BoundProperty<ImageSize> size;
	private final NonNullBoundProperty<EditorBlockAlignment> alignment;

	public ImageBlockModel() {
		this((HvImageDto) null, null);
	}

	public ImageBlockModel(BufferedImage image, ImageSize size) {
		this(new HvImageDto(image, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG), size);
	}

	public ImageBlockModel(HvImageDto image, ImageSize size) {
		alignment = new NonNullBoundProperty<EditorBlockAlignment>(propertyAlignment, EditorBlockAlignment.LEFT,
				propChangeSupport);
		alignment.addUndoableEditSupport(getUndoSupport());
		this.image = new BoundProperty<>(propertyImage, image, propChangeSupport);
		this.size = new BoundProperty<>(propertySize, size, propChangeSupport);

		this.image.addUndoableEditSupport(getUndoSupport());
		this.size.addUndoableEditSupport(getUndoSupport());
	}

	public ImageSize getSize() {
		return size.getValue();
	}

	public ImageScaling getScaling(double dpi) {
		if (size.getValue() != null) {
			return size.getValue().toScaling(dpi);
		} else {
			return ImageScaling.noScaling;
		}
	}

	public void setSize(ImageSize size) {
		this.size.setValue(size);
	}

	public BufferedImage getImage() {
		if (image.getValue() == null)
			return null;
		return image.getValue().getBufferedImage();
	}

	public void setImage(HvImageDto image) {
		this.image.setValue(image);
	}

	public void setImage(BufferedImage image, String type) {
		this.image.setValue(new HvImageDto(image, type));
	}

	public HvImageDto getImageDtoForDB() {
		return image.getValue();
	}

	public static enum ScaleType {
		/**
		 * Skaliere das Bild um einen relativen Faktor
		 */
		RELATIVE,
		/**
		 * Skaliere das Bild auf eine Zielgr&ouml;&szlig;e
		 */
		FIXED
	}

	@Override
	public void fromDto(EditorBaseBlockDto dto) {
		super.fromDto(dto);
		EditorImageBlockDto imgDto = (EditorImageBlockDto) dto;
		ImageSize size = new ImageSize(imgDto.getWidth(), imgDto.getHeight(), SizeUnit.MM);
		setSize(size);
		setImage(imgDto.getImageDto());
		setAlignment(imgDto.getAlignment());
	}

	@Override
	protected void populateDto(EditorBaseBlockDto dto) {
		super.populateDto(dto);
		EditorImageBlockDto imgDto = (EditorImageBlockDto) dto;
		imgDto.setImageDto(getImageDtoForDB());
		imgDto.setWidth(getSize().width);
		imgDto.setHeight(getSize().height);
		imgDto.setAlignment(getAlignment());
	}

	@Override
	protected EditorBaseBlockDto createEmptyDto() {
		return new EditorImageBlockDto();
	}

	public void setAlignment(EditorBlockAlignment alignment) {
		this.alignment.setValue(Objects.requireNonNull(alignment));
	}

	public EditorBlockAlignment getAlignment() {
		return alignment.getValue();
	}

}
