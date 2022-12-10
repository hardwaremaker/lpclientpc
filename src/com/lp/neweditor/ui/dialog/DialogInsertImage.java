package com.lp.neweditor.ui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.HvValueHolder;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperButtonSize;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.ImageFileOpener;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.neweditor.common.ImageScaling;
import com.lp.neweditor.common.InsertPosition;
import com.lp.neweditor.common.SizeUnit;
import com.lp.neweditor.data.block.ImageSize;
import com.lp.neweditor.ui.block.ScalingImageUI;
import com.lp.neweditor.ui.common.LeftEllipsisLabelUI;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.service.HvImageDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.logger.LpLogger;
import com.lp.util.EditorBlockAlignment;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 * Dialog zur Frage wo und mit welcher Gr&ouml;&szlig;e ein Bild eingef&uuml;gt
 * werden soll
 * 
 * @author Alexander Daum
 *
 */
public class DialogInsertImage extends JDialog {
	private static final int DEF_DPI = 300;
	private static final int MIN_DPI = 1;
	private static final int MAX_DPI = 3000;

	private static final SizeUnit defSizeUnit = SizeUnit.MM;

	private static final long serialVersionUID = 1L;

	private JCheckBox adjustProportional;
	private JCheckBox compressInDB;
	private JButton btnCancel;
	private JButton btnOK;

	private JLabel fileNameLabel;
	private Optional<HvImageDto> originalImage = Optional.empty();
	/**
	 * Nach unten skaliertes Bild, verwendet wenn Checkbox komprimiert in DB
	 * speichern aktiv ist. Wird asynchron skaliert, deshalb als future
	 */
	private CompletableFuture<HvImageDto> scaledImage = null;

	private SpinnerNumberModel widthModel;
	private SpinnerNumberModel heightModel;
	private SpinnerNumberModel dpiModel;

	HvValueHolder<Number> valueWidthModel;
	HvValueHolder<Number> valueHeightModel;
	
	private ButtonGroup btnGroupPosition;
	private ButtonGroup btnGroupAlignment;

	private JComboBox<SizeUnit> comboEinheit;
	private SizeUnit oldUnit;
	private ScalingImageUI vorschau;

	private boolean wasAccepted;

	public DialogInsertImage(Window parent, boolean newImage) {
		super(parent);
		setModalityType(ModalityType.APPLICATION_MODAL);
		initUI(newImage);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setMinimumSize(getSize());
		setSize(getWidth() + 100, getHeight() + 60);
	}

	private void initUI(boolean newImage) {
		vorschau = new ScalingImageUI();
		JScrollPane spVorschau = new JScrollPane(vorschau);

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "10px[][grow][5px][grow][]10px", "10px[]10px[][][][][][][][][grow]"));

		setTitle(LPMain.getTextRespectUISPr(
				newImage ? "neweditor.insertimg.title" : "neweditor.updateimg.title"));

		JLabel labelFile = new JLabel(LPMain.getTextRespectUISPr("neweditor.datei") + ":");
		fileNameLabel = createFileNameLabel();

		JButton btnFileDialog = new WrapperButtonSize();
		btnFileDialog.setText(LPMain.getTextRespectUISPr("neweditor.insertimg.durchsuchen"));
		btnFileDialog.addActionListener(new FileSelectAction());

		JLabel labelPosition = new JLabel(LPMain.getTextRespectUISPr("neweditor.insertimg.position"));
		labelPosition.setHorizontalAlignment(JLabel.CENTER);

		JRadioButton rbPosAbove = new WrapperRadioButton(LPMain.getTextRespectUISPr("neweditor.insertimg.pos.above"));
		rbPosAbove.setActionCommand(InsertPosition.BEFORE_VERTICAL.name());
		JRadioButton rbPosBelow = new WrapperRadioButton(LPMain.getTextRespectUISPr("neweditor.insertimg.pos.below"));
		rbPosBelow.setActionCommand(InsertPosition.AFTER_VERTICAL.name());
		JRadioButton rbPosLeft = new WrapperRadioButton(LPMain.getTextRespectUISPr("neweditor.insertimg.pos.left"));
		rbPosLeft.setActionCommand(InsertPosition.BEFORE_HORIZONTAL.name());
		JRadioButton rbPosRight = new WrapperRadioButton(LPMain.getTextRespectUISPr("neweditor.insertimg.pos.right"));
		rbPosRight.setActionCommand(InsertPosition.AFTER_HORIZONTAL.name());

		rbPosBelow.setSelected(true);

		btnGroupPosition = createBtnGroup(rbPosAbove, rbPosBelow, rbPosLeft, rbPosRight);

		JLabel labelAlignment = new JLabel(LPMain.getTextRespectUISPr("neweditor.insertimg.alignment"));

		JRadioButton rbAlignLeft = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("neweditor.insertimg.alignment_left"));
		rbAlignLeft.setActionCommand(EditorBlockAlignment.LEFT.name());
		JRadioButton rbAlignCenter = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("neweditor.insertimg.alignment_center"));
		rbAlignCenter.setActionCommand(EditorBlockAlignment.CENTER.name());
		JRadioButton rbAlignRight = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("neweditor.insertimg.alignment_right"));
		rbAlignRight.setActionCommand(EditorBlockAlignment.RIGHT.name());

		btnGroupAlignment = createBtnGroup(rbAlignLeft, rbAlignCenter, rbAlignRight);

		rbAlignLeft.setSelected(true);

		ChangeListener dimensionsOnChanged = ce -> dimensionsChanged(ce);
		ChangeListener refreshImageOnChange = ce -> refreshPreview();

		widthModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 0.5);
		heightModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 0.5);
		dpiModel = new SpinnerNumberModel(DEF_DPI, MIN_DPI, MAX_DPI, 1);
//		widthModel.addChangeListener(refreshImageOnChange);
//		heightModel.addChangeListener(refreshImageOnChange);
		widthModel.addChangeListener(dimensionsOnChanged);
		heightModel.addChangeListener(dimensionsOnChanged);
		dpiModel.addChangeListener(refreshImageOnChange);
		
		JLabel labelSize = new JLabel(LPMain.getTextRespectUISPr("neweditor.insertimg.size"));
		JLabel labelWidth = new JLabel(LPMain.getTextRespectUISPr("label.breite"));
		JLabel labelHeight = new JLabel(LPMain.getTextRespectUISPr("label.hoehe"));
		JLabel labelDPI = new JLabel(LPMain.getTextRespectUISPr("label.dpi"));
//		WrapperSpinner fieldWidth = new WrapperSpinner(widthModel);
		JSpinner fieldWidth = new JSpinner(widthModel);
//		WrapperSpinner fieldHeight = new WrapperSpinner(heightModel);
		JSpinner fieldHeight = new JSpinner(heightModel);
		JSpinner fieldDPI = new JSpinner(dpiModel);

		valueWidthModel = new HvValueHolder<Number>(fieldWidth, widthModel.getNumber());
		valueHeightModel = new HvValueHolder<Number>(fieldHeight, heightModel.getNumber());
		
		JLabel labelEinheit = new JLabel(LPMain.getTextRespectUISPr("label.einheit"));
		comboEinheit = new JComboBox<>(SizeUnit.values());
		comboEinheit.setSelectedItem(defSizeUnit);
		comboEinheit.addItemListener(this::unitSelected);

		compressInDB = new JCheckBox();
		compressInDB.setText(LPMain.getTextRespectUISPr("neweditor.insertimg.compress"));
		compressInDB.setHorizontalAlignment(SwingConstants.LEFT);
		compressInDB.addChangeListener(refreshImageOnChange);

		adjustProportional = new JCheckBox();
		adjustProportional.setText(LPMain.getTextRespectUISPr("neweditor.insertimg.adjust"));
		adjustProportional.setHorizontalAlignment(SwingConstants.LEFT);
		adjustProportional.setSelected(true);
		adjustProportional.addChangeListener(refreshImageOnChange);
		
		JPanel btnPanel = createOKCancelButtons();

		add(labelFile, "align right");
		add(fileNameLabel, new CC().span(3).growX().width("60:60"));
		add(btnFileDialog, "align left, wrap");
		add(new JSeparator(JSeparator.HORIZONTAL), "spanx, wrap, growx");

		if (newImage) {
			add(labelPosition, "align left");
		}
		add(labelAlignment, new CC().spanX(newImage ? 1 : 2).alignX("left"));
		add(new JSeparator(JSeparator.VERTICAL), "spany 7, growy");
		add(labelSize, "span 2, align center, wrap");

		if (newImage) {
			add(rbPosAbove, "align left");
		}
		add(rbAlignLeft, new CC().spanX(newImage ? 1 : 2).alignX("left"));
		add(labelWidth, "align right");
		add(fieldWidth, "align left, growx, wrap");

		if (newImage) {
			add(rbPosBelow, "align left");
		}
		add(rbAlignCenter, new CC().spanX(newImage ? 1 : 2).alignX("left"));
		add(labelHeight, "align right");
		add(fieldHeight, "align left, growx, wrap");

		if (newImage) {
			add(rbPosLeft, "align left");
		}
		add(rbAlignRight, new CC().spanX(newImage ? 1 : 2).alignX("left"));
		add(labelDPI, "align right");
		add(fieldDPI, "align left, growx, wrap");

		if (newImage) {
			add(rbPosRight, "align left");
		}
		add(labelEinheit, new CC().skip(newImage ? 1 : 2).alignX("right"));
		add(comboEinheit, "align left, wrap");

//		add(compressInDB, "skip 2, span 2, align right, growx, wrap");
		add(adjustProportional, "skip 3, align left, growx, wrap");
		add(compressInDB, "skip 3, align left, growx, wrap");

		add(spVorschau, "span, grow");

		add(btnPanel, "dock south");
	}

	private JPanel createOKCancelButtons() {
		btnCancel = new WrapperButton();
		btnCancel.setText(LPMain.getTextRespectUISPr("lp.abbrechen"));
		btnCancel.setPreferredSize(HelperClient.getSizeFactoredDimension(60));
		btnCancel.addActionListener(e -> close(false));
		btnOK = new WrapperButton();
		btnOK.setText(LPMain.getTextRespectUISPr("button.ok"));
		btnOK.setPreferredSize(HelperClient.getSizeFactoredDimension(60));
		btnOK.addActionListener(e -> close(true));
		btnOK.setEnabled(false);

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 2));
		btnPanel.add(btnOK);
		btnPanel.add(btnCancel);
		return btnPanel;
	}

	private JLabel createFileNameLabel() {
		JLabel fileNameLabel = new JLabel();
		fileNameLabel.setBorder(BorderFactory.createLineBorder(Defaults.getInstance().getTextfieldBorderColor()));
		fileNameLabel.setPreferredSize(HelperClient.getSizeFactoredDimension(120));
		fileNameLabel.setUI(new LeftEllipsisLabelUI());
		fileNameLabel.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
		return fileNameLabel;
	}

	private ButtonGroup createBtnGroup(JRadioButton... buttons) {
		ButtonGroup btnGroup = new ButtonGroup();
		for (JRadioButton btn : buttons) {
			btnGroup.add(btn);
		}
		return btnGroup;
	}

	private void imageChange() {
		if (originalImage.isPresent()) {
			BufferedImage bufferedImage = originalImage.get().getBufferedImage();
			int height = bufferedImage.getHeight(null);
			int width = bufferedImage.getWidth(null);
			SizeUnit selectedItem = (SizeUnit) comboEinheit.getSelectedItem();
			if (selectedItem == SizeUnit.PIXEL) {
				widthModel.setValue(width);
				heightModel.setValue(height);
			} else {
				double ppu = selectedItem.getPixelsPerUnit(dpiModel.getNumber().doubleValue());
				widthModel.setValue(width / ppu);
				heightModel.setValue(height / ppu);
			}
		}
		refreshPreview();
	}

	private void unitSelected(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			oldUnit = (SizeUnit) e.getItem();
		} else if (e.getStateChange() == ItemEvent.SELECTED) {
			SizeUnit newUnit = (SizeUnit) e.getItem();
			if (newUnit == oldUnit)
				return;
			double widhtInOldUnit = widthModel.getNumber().doubleValue();
			double heightInOldUnit = heightModel.getNumber().doubleValue();
			double conversionFactor;
			if (newUnit == SizeUnit.PIXEL) {
				conversionFactor = oldUnit.getPixelsPerUnit(dpiModel.getNumber().doubleValue());
			} else if (oldUnit == SizeUnit.PIXEL) {
				conversionFactor = 1 / newUnit.getPixelsPerUnit(dpiModel.getNumber().doubleValue());
			} else {
				conversionFactor = oldUnit.getConversionFactor(newUnit);
			}
			double widthInNewUnit = widhtInOldUnit * conversionFactor;
			double heightInNewUnit = heightInOldUnit * conversionFactor;
			widthModel.setValue(widthInNewUnit);
			heightModel.setValue(heightInNewUnit);
		}
		refreshPreview();
	}

	private void dimensionsChanged(ChangeEvent e) {
		if (originalImage.isPresent() && adjustProportional.isSelected()) {
			if (widthModel == e.getSource()) {
				System.out.println("width changed to " + widthModel.getNumber());
			} 

			if (heightModel == e.getSource()) {
				System.out.println("height changed to " + heightModel.getNumber());				
			}
			
			refreshPreview();
		}
	}
	
	private void refreshPreview() {
		SizeUnit unit = (SizeUnit) comboEinheit.getSelectedItem();
		double targetWidth = widthModel.getNumber().doubleValue();
		double targetHeight = heightModel.getNumber().doubleValue();
		double dpi = dpiModel.getNumber().doubleValue();
		if (targetHeight == 0 || targetWidth == 0 || dpi == 0)
			return;
		ImageScaling realScaling = ImageScaling.toSizeWithDPI(targetWidth, targetHeight, unit, dpi);
		if (unit == SizeUnit.PIXEL) {
			targetWidth *= HvBlockEditor.DISPLAY_DPI / dpi;
			targetHeight *= HvBlockEditor.DISPLAY_DPI / dpi;
		}
		ImageScaling displayScaling = ImageScaling.toSizeWithDPI(targetWidth, targetHeight, unit,
				HvBlockEditor.DISPLAY_DPI);

		// Immer von originalImage ausgehen und scaledImage eventuell neu machen
		if (!originalImage.isPresent()) {
			vorschau.setImage(null, displayScaling);
			btnOK.setEnabled(false);
		} else {
			btnOK.setEnabled(true);
			BufferedImage img = originalImage.get().getBufferedImage();
			if (compressInDB.isSelected()) {
				scaledImage = scaleDownImage(originalImage.get(), realScaling);
				scaledImage.thenAccept(scaled -> vorschau.setImage(scaled.getBufferedImage(), displayScaling));
			} else {
				vorschau.setImage(img, displayScaling);
			}
		}
	}

	private CompletableFuture<HvImageDto> scaleDownImage(HvImageDto realImg, ImageScaling realScaling) {
		Dimension targetDimension = realScaling.scaleSize(new Dimension());
		CompletableFuture<HvImageDto> scaledFuture = CompletableFuture
				.supplyAsync(() -> getScaled(realImg, targetDimension.width, targetDimension.height));
		return scaledFuture.thenApply(img -> chooseSmaller(img, realImg));
	}

	private HvImageDto getScaled(HvImageDto img, int width, int height) {
		BufferedImage downscaled = HelperClient.scaleImage(img.getBufferedImage(), width, height,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		HvImageDto hvImage = new HvImageDto(downscaled, img.getImageFormat());
		return hvImage;
	}

	private HvImageDto chooseSmaller(HvImageDto img1, HvImageDto img2) {
		int size1 = img1.getImageBinaryData().length;
		int size2 = img2.getImageBinaryData().length;
		if (size1 < size2) {
			return img1;
		} else {
			return img2;
		}
	}

	/**
	 * 
	 * @param ok true bei ok, false bei cancel
	 */
	private void close(boolean ok) {
		wasAccepted = ok;
		dispose();
	}

	/**
	 * Gibt true zur&uuml;ck, wenn das Bild eingef&uuml;gt werden soll, false wenn
	 * nicht
	 * 
	 * @return
	 */
	public boolean getAccepted() {
		return wasAccepted;
	}

	public Optional<HvImageDto> getSelectedImage() {
		// Expliter empty return, wenn Bild = leer kann scaledImage noch ein alter Wert
		// sein
		if (!originalImage.isPresent())
			return Optional.empty();
		if (compressInDB.isSelected()) {
			try {
				return Optional.of(scaledImage.get());
			} catch (InterruptedException | ExecutionException e) {
				LpLogger.getLogger(getClass().getName())
						.warn("Konnte Bild nicht skalieren, Originalbild wird verwendet", e);
				return originalImage;
			}
		} else {
			return originalImage;
		}
	}

	public ImageSize getImageSize() {
		SizeUnit unit = (SizeUnit) comboEinheit.getSelectedItem();
		double width = widthModel.getNumber().doubleValue();
		double height = heightModel.getNumber().doubleValue();
		if (unit == SizeUnit.PIXEL) {
			width /= dpiModel.getNumber().doubleValue();
			height /= dpiModel.getNumber().doubleValue();
			unit = SizeUnit.INCH;
		}
		return new ImageSize(width, height, unit);
	}

	public EditorBlockAlignment getAlignment() {
		return EditorBlockAlignment.valueOf(btnGroupAlignment.getSelection().getActionCommand());
	}

	public InsertPosition getInsertPosition() {
		return InsertPosition.valueOf(btnGroupPosition.getSelection().getActionCommand());
	}

	public void setData(HvImageDto image, ImageSize size, EditorBlockAlignment alignment) {
		originalImage = Optional.ofNullable(image);
		this.comboEinheit.setSelectedItem(SizeUnit.MM);
		this.widthModel.setValue(size.width);
		this.heightModel.setValue(size.height);
		Enumeration<AbstractButton> enumeration = btnGroupAlignment.getElements();
		while (enumeration.hasMoreElements()) {
			AbstractButton btn = enumeration.nextElement();
			if (btn.getActionCommand().equals(alignment.name())) {
				btn.setSelected(true);
				break;
			}
		}
	}

	private class FileSelectAction implements ActionListener {

		public FileSelectAction() {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			HvOptional<WrapperFile> wf = new ImageFileOpener(DialogInsertImage.this, FileChooserConfigToken.ImportLast)
					.selectSingle();
			if (wf.isPresent()) {
				fileNameLabel.setText(wf.get().getFile().getAbsolutePath());
				try {
					byte[] bytes = wf.get().getBytes();
					String imagType = HelperClient.getMimeTypeOfFile(new ByteArrayInputStream(bytes)).toString();
					originalImage = Optional.of(new HvImageDto(bytes, imagType));
				} catch (IOException e1) {
					originalImage = Optional.empty();
					fileNameLabel.setText("");
					originalImage = Optional.empty();
					JOptionPane.showMessageDialog(DialogInsertImage.this,
							LPMain.getTextRespectUISPr("neweditor.insertimg.fehlerbeibild"),
							LPMain.getTextRespectUISPr("lp.error"), JOptionPane.ERROR_MESSAGE);
				}
			} else {
				fileNameLabel.setText("");
				originalImage = Optional.empty();
			}
			imageChange();
		}
	}

	public static enum ImageInsertPosition {
		ABOVE, BELOW, AT_CARET
	}
}
