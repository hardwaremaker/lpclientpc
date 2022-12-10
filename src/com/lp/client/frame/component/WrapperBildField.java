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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.ImageFileOpener;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2011/03/07 15:04:46 $
 * 
 * @todo scrollpane auf bild PJ 3416
 * @todo texte uebersetzen PJ 3416
 */
public class WrapperBildField extends PanelBasis implements IControl {

	private static final long serialVersionUID = 1L;

	private final static String ACTION_SPECIAL_DATEI = "action_special_datei";
	private final static String ACTION_SPECIAL_ANZEIGEN = "action_special_anzeigen";
	private final static String ACTION_SPECIAL_SPEICHERN = "action_special_speichern";
	private static final String ACTION_SPECIAL_LEEREN = "action_special_datefield_leeren";
	private WrapperTextField wtfDatei = new WrapperTextField();
	private JPanel jpaWorkingOn = new JPanel();
	// private PanelImage jpaBild = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperButton wbuDatei = new WrapperButton();
	private WrapperButton wbuAnzeigen = new WrapperButton();
	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperLabel wlaInfos = new WrapperLabel();
	private File temp;

	private WrapperTextField fieldToDisplayFileName = null;

	private File sLetzteDatei = null;

	private ImageViewer imageviewer = new ImageViewer((byte[]) null);

	private String bildExtension = ".png";
	private boolean bWithoutButtons = false;
	private byte[] imageOriginalBytes = null;

	public WrapperButton getButtonDatei() {
		return wbuDatei;
	}

	private JButton jbuSetNull = null;
	

	private boolean isActivatable = true;

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		jbuSetNull.setEnabled(enabled);
		
		wbuDatei.setEnabled(enabled);
		wbuAnzeigen.setEnabled(!enabled && getImage() != null);
		wbuSpeichern.setEnabled(!enabled && getImage() != null);
	}

	public void setOnlyBildVisible() {

		jbuSetNull.setVisible(false);
		
		wbuDatei.setVisible(false);
		wbuAnzeigen.setVisible(false);
		wbuSpeichern.setVisible(false);
		wtfDatei.setVisible(false);
		wlaInfos.setVisible(false);
	}

	public WrapperBildField(InternalFrame internalFrame, String addTitel) throws Throwable {
		super(internalFrame, addTitel);
		jbInit();
		initComponents();
	}

	public WrapperButton getDateiButton() {
		return wbuDatei;
	}

	public WrapperBildField(InternalFrame internalFrame, String addTitel, WrapperTextField fieldToDisplayFileName)
			throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		jbInit();
		initComponents();
	}

	public WrapperBildField(InternalFrame internalFrame, String addTitel, WrapperTextField fieldToDisplayFileName,
			boolean bWithoutButtons) throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		this.bWithoutButtons = bWithoutButtons;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wbuDatei.setText(LPMain.getTextRespectUISPr("lp.datei"));
		wbuAnzeigen.setText(LPMain.getTextRespectUISPr("lp.anzeigen"));
		wbuSpeichern.setText(LPMain.getTextRespectUISPr("lp.speichern"));
		wlaInfos.setText(" ");
		jpaWorkingOn.setLayout(gridBagLayout2);

		HvDropTarget dt = new HvDropTarget(this);
		dt.addDropListener(new FileImportDropHandlerBF(this));
		/*
		 * wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance()
		 * .getControlHeight())); wbuDatei.setPreferredSize(new Dimension(80,
		 * Defaults.getInstance() .getControlHeight()));
		 */
		// wbuDatei.setMaximumSize(HelperClient.getSizeFactoredDimension(50));
		HelperClient.setMinimumAndPreferredSize(wbuDatei, HelperClient.getSizeFactoredDimension(10));

		wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
		wbuDatei.addActionListener(this);
		wbuAnzeigen.setActionCommand(ACTION_SPECIAL_ANZEIGEN);
		wbuAnzeigen.addActionListener(this);
		wbuSpeichern.setActionCommand(ACTION_SPECIAL_SPEICHERN);
		wbuSpeichern.addActionListener(this);
		wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
		wtfDatei.setActivatable(false);

		jbuSetNull = ButtonFactory.createJButton(IconFactory.getClear(), ACTION_SPECIAL_LEEREN);
		jbuSetNull.addActionListener(this);

		HelperClient.setMinimumAndPreferredSize(jbuSetNull,
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

		

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		int iZeile = 0;
		jpaWorkingOn.add(imageviewer, new GridBagConstraints(3, iZeile, 1, 7, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (!bWithoutButtons) {
			jpaWorkingOn.add(wbuDatei, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, Defaults.sizeFactor(22)), 100, 0));
			
			jpaWorkingOn.add(jbuSetNull, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wtfDatei, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wlaInfos, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 40, 0));
			iZeile++;
			jpaWorkingOn.add(wbuAnzeigen, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wbuSpeichern, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}

	}

	private boolean hasPngHeader(byte[] bytes) {
		if (bytes.length >= 8) {
			return Arrays.equals(Arrays.copyOfRange(bytes, 0, 8), new byte[] { (byte) 0x89, (byte) 0x50, (byte) 0x4E,
					(byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A });
		} else {
			return false;
		}
	}

	protected void toTempFile() throws IOException {
		if (getImage() == null) {
			temp = null;
			return;
		}
		// BufferedImage image = getImage();
		String name = getDateiname();
		if (name == null) {
			if (getBildExtension() == null)
				setBildExtension(".gif");
			name = "tempImageHV" + getBildExtension();
		}
		String mime = Helper.getMime(name);
		temp = File.createTempFile(Helper.getName(name), mime.isEmpty() ? null : mime);
		if (mime.toLowerCase().equals(".png") && !hasPngHeader(imageOriginalBytes)) {
			myLogger.info("Falsches png Format, Versuch zu konvertieren");
			try {
				ImageIO.write(imageviewer.originalImage, Helper.getMime(name).replace(".", ""), temp);
				return;
			} catch (Exception e) {
				myLogger.error("Image nicht konvertierbar", e);
			}
		}
		FileOutputStream fOutputStream = new FileOutputStream(temp);
		fOutputStream.write(imageOriginalBytes);
		fOutputStream.close();

		// ImageIO.write(image, Helper.getMime(name).replace(".", ""), temp);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(jbuSetNull)) {
			setImage(null);
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_ANZEIGEN)) {
			toTempFile();
			HelperClient.desktopTryToOpenElseSave(temp, getInternalFrame());
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_SPEICHERN)) {
			toTempFile();
			HelperClient.showSaveFileDialog(temp, getDateiname() == null ? null : new File(getDateiname()),
					getInternalFrame(), getDateiname() == null ? null : Helper.getMime(getDateiname()));
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI)) {
			actionSelectFile();
		}
	}

	/**
	 * @throws Throwable
	 * @throws IOException
	 * @throws ExceptionLP
	 */
	private void actionSelectFile() throws Throwable, IOException, ExceptionLP {
		ImageFileOpener fileOpener = new ImageFileOpener(
				getInternalFrame(), FileChooserConfigToken.ImportLast);
		HvOptional<WrapperFile> wf = fileOpener.selectSingle();
		if (!wf.isPresent())
			return;

		FileValidator validator = new FileValidator();
		long size = wf.get().getFile().length() / 1024l;
		if (!validator.validateFileSize(new BigDecimal(size))) {
			imageviewer.setImage((byte[]) null);
			return;
		}

		// darstellen
		imageOriginalBytes = wf.get().getBytes();
		try {
			BufferedImage image = Helper.byteArrayToImage(imageOriginalBytes);
			File file = wf.get().getFile();
			imageviewer.setImage(image);
			if (imageviewer.originalImage == null) {
				// Die Ausgewaehlte Datei ist kein Bild
				imageviewer.setImage((byte[]) null);
				imageOriginalBytes = null;
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.error.keinbildgewaehlt"));
			} else {
				if (fieldToDisplayFileName != null) {
					fieldToDisplayFileName.setText(file.getName());
				}
				wtfDatei.setText(file.getName());
				wbuDatei.setToolTipText(file.getAbsolutePath());

				wlaInfos.setText(getImage().getWidth(this) + "x" + getImage().getHeight(this) + "("
						+ Helper.formatZahl(new Double(size), 1, LPMain.getTheClient().getLocUi()) + " kb)");
			}
		} catch (Throwable e) {
			imageviewer.setImage((byte[]) null);
			imageOriginalBytes = null;
			String extensions = fileOpener.getAllowedExtensions();
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getMessageTextRespectUISPr(
							"lp.mediacontrol.ungueltiges.dateiformat", extensions));
		}
	}

	public String getBildExtension() {
		return bildExtension;
	}

	public String getDateiname() {
		return wtfDatei.getText();
	}

	public void setDateiname(String dateiname) {
		wtfDatei.setText(dateiname);
	}

	public void setBildExtension(String bildExtension) {
		this.bildExtension = bildExtension;
	}

	public java.awt.image.BufferedImage getImage() {
		return imageviewer.originalImage;
	}

	public byte[] getImageOriginalBytes() {
		return imageOriginalBytes;
	}

	protected String getLockMeWer() {
		return null;
	}

	public void setImage(byte[] image) throws Throwable {
		temp = null;
		imageOriginalBytes = image;
		imageviewer.setImage(image);
		if (image != null && getImage() != null) {

			wlaInfos.setText(getImage().getWidth(this) + "x" + getImage().getHeight(this) + "("
					+ Helper.formatZahl(new Double(image.length / 1024), 1, LPMain.getTheClient().getLocUi()) + " kb)");

		} else {
			// Entweder Daten == null, oder kein Bild
			wtfDatei.removeContent();
			imageOriginalBytes = null;
		}
	}

	public void cleanup() {
		imageviewer.cleanup();
		jpaWorkingOn.remove(imageviewer);
		imageviewer = null;
		setToolBar(null);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuDatei;
	}

	@Override
	public boolean isMandatoryField() {
		return wtfDatei.isMandatoryField();
	}

	@Override
	public void setMandatoryField(boolean isMandatoryField) {
		wtfDatei.setMandatoryField(isMandatoryField);
	}

	@Override
	public boolean isActivatable() {
		return isActivatable;
	}

	@Override
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		if (!isActivatable) {
			setEnabled(false);
		}
	}

	@Override
	public void removeContent() throws Throwable {
		setImage(null);
		setDateiname(null);
	}

	@Override
	public boolean hasContent() throws Throwable {
		return wtfDatei.hasContent();
	}
}

class FileImportDropHandlerBF implements DropListener {

	private WrapperBildField wmc = null;

	public FileImportDropHandlerBF(WrapperBildField wmc) {
		this.wmc = wmc;
	}

	private boolean canImport() {

		if (wmc.getDateiButton().isEnabled()) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void filesDropped(List<File> files) {
		if (!canImport()) {
			return;
		}
		try {
			for (File f : files) {
				boolean accept = false;
				String filename = f.getName();
				String extension = filename.substring(filename.lastIndexOf(".") + 1);

				WrapperMediaControl wmcParent = null;
				if (wmc.getParent() != null && wmc.getParent().getParent() != null
						&& wmc.getParent().getParent() instanceof WrapperMediaControl) {
					wmcParent = (WrapperMediaControl) wmc.getParent().getParent();
					wmcParent.setOMedia(Helper.getBytesFromFile(f));
					wmcParent.setDateiname(filename);
					if (extension.toLowerCase().equals("pdf")) {
						wmcParent.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
						wmcParent.setOMedia(Helper.getBytesFromFile(f));
					} else if (extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("jpeg")) {
						wmcParent.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
						wmcParent.setOMedia(Helper.getBytesFromFile(f));
					} else if (extension.toLowerCase().equals("gif")) {
						wmcParent.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF);
						wmcParent.setOMedia(Helper.getBytesFromFile(f));
					} else if (extension.toLowerCase().equals("png")) {
						wmcParent.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
						wmcParent.setOMedia(Helper.getBytesFromFile(f));
					} else if (extension.toLowerCase().equals("tiff")) {
						wmcParent.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF);
						wmcParent.setOMedia(Helper.getBytesFromFile(f));
					}

					return;
				}

				if (extension.toUpperCase().equals("GIF")) {
					wmc.setImage(Helper.getBytesFromFile(f));
					accept = true;
				} else if (extension.toUpperCase().equals("PNG")) {
					wmc.setImage(Helper.getBytesFromFile(f));
					accept = true;
				} else if (extension.toUpperCase().equals("JPG") || extension.toUpperCase().equals("JPEG")) {
					wmc.setImage(Helper.getBytesFromFile(f));
					accept = true;
				} else if (extension.toUpperCase().equals("TIFF")) {
					wmc.setImage(Helper.getBytesFromFile(f));
					accept = true;
				}
				if (accept) {
					wmc.setDateiname(filename);
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
