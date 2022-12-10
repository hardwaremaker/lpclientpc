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
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.TiffFileOpener;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

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
 * @author $Author: valentin $
 *         </p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:39:21 $
 *
 * @todo scrollpane auf bild PJ 5350
 * @todo texte uebersetzen PJ 5350
 */
public class WrapperTiffViewer extends PanelBasis implements IControl {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static String ACTION_SPECIAL_DATEI = "action_special_datei";
	private final static String ACTION_SPECIAL_TIFFFIRST = "action_special_tifffirst";
	private final static String ACTION_SPECIAL_TIFFLAST = "action_special_tifflast";
	private final static String ACTION_SPECIAL_TIFFRIGHT = "action_special_tiffright";
	private final static String ACTION_SPECIAL_TIFFLEFT = "action_special_tiffleft";
	private final static String ACTION_SPECIAL_ANZEIGEN = "action_special_anzeigen";
	private final static String ACTION_SPECIAL_SPEICHERN = "action_special_speichern";

	protected WrapperNumberField wnfGroesse = new WrapperNumberField();
	protected WrapperTextField wtfSeite = new WrapperTextField();

	private JPanel paWorkingOn = new JPanel();
	// private PanelImage jpaBild = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperButton wbuDatei = new WrapperButton();
	private WrapperLabel wlaSeite = new WrapperLabel();
	private WrapperLabel wlaKb = new WrapperLabel();
	private WrapperTextField wtfDatei = new WrapperTextField();

	private ButtonGroup bgTiffControls = null;
	private WrapperButton wbuTiffLeft = new WrapperButton();
	private WrapperButton wbuTiffRight = new WrapperButton();
	private WrapperButton wbuTiffFirst = new WrapperButton();
	private WrapperButton wbuTiffLast = new WrapperButton();

	private WrapperTextField fieldToDisplayFileName = null;
	private WrapperButton wbuAnzeigen = new WrapperButton();
	private WrapperButton wbuSpeichern = new WrapperButton();

	private File sLetzteDatei = null;

	private ImageViewer imageviewer = new ImageViewer((byte[])null);

	private String bildExtension = new String(".tiff");
	private ImageDecoder decoder = null;
	private RenderedImage image[] = null;
	private SeekableStream ss = null;
	private byte[] imageOutput = null;

	private int imageIndex = 0;
	private boolean isActivatable = true;

	public WrapperButton getButtonDatei() {
		return wbuDatei;
	}

	public WrapperTiffViewer(InternalFrame internalFrame, String addTitel) throws Throwable {
		super(internalFrame, addTitel);
		jbInit();
		initComponents();
	}

	public WrapperTiffViewer(InternalFrame internalFrame, String addTitel, WrapperTextField fieldToDisplayFileName)
			throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		jbInit();
		initComponents();
	}

	public void setDefaults() throws Throwable {
		decoder = null;
		image = null;
		ss = null;
		imageOutput = null;

	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wbuDatei.setText(LPMain.getTextRespectUISPr("lp.datei"));
		wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
		wtfDatei.setActivatable(false);
		wlaSeite.setText(LPMain.getTextRespectUISPr("artikel.katalogseite.seite"));
		wlaKb.setText("kB");
		paWorkingOn.setLayout(gridBagLayout2);
		wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wtfSeite.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wtfSeite.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wnfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wnfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wnfGroesse.setActivatable(false);
		wtfSeite.setActivatable(false);
		wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
		wbuDatei.addActionListener(this);
		bgTiffControls = new ButtonGroup();
		wbuTiffLeft.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_left.png")));
		wbuTiffLeft.setActionCommand(ACTION_SPECIAL_TIFFLEFT);
		wbuTiffLeft.addActionListener(this);
		wbuTiffRight.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_right.png")));
		wbuTiffRight.setActionCommand(ACTION_SPECIAL_TIFFRIGHT);
		wbuTiffRight.addActionListener(this);
		wbuTiffFirst.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_beginning.png")));
		wbuTiffFirst.setActionCommand(ACTION_SPECIAL_TIFFFIRST);
		wbuTiffFirst.addActionListener(this);
		wbuTiffLast.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_end.png")));
		wbuTiffLast.setActionCommand(ACTION_SPECIAL_TIFFLAST);
		wbuTiffLast.addActionListener(this);
		bgTiffControls.add(wbuTiffLeft);
		bgTiffControls.add(wbuTiffRight);
		bgTiffControls.add(wbuTiffFirst);
		bgTiffControls.add(wbuTiffLast);

		wbuAnzeigen.setText(LPMain.getTextRespectUISPr("lp.anzeigen"));
		wbuSpeichern.setText(LPMain.getTextRespectUISPr("lp.speichern"));
		wbuAnzeigen.setActionCommand(ACTION_SPECIAL_ANZEIGEN);
		wbuAnzeigen.addActionListener(this);
		wbuSpeichern.setActionCommand(ACTION_SPECIAL_SPEICHERN);
		wbuSpeichern.addActionListener(this);

		this.add(paWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		paWorkingOn.add(wbuDatei, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		paWorkingOn.add(wtfDatei, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		paWorkingOn.add(wbuTiffFirst, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		paWorkingOn.add(wbuTiffLast, new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		paWorkingOn.add(wbuTiffLeft, new GridBagConstraints(2, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		paWorkingOn.add(wbuTiffRight, new GridBagConstraints(3, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		paWorkingOn.add(wlaSeite, new GridBagConstraints(0, 3, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 40, 0));
		paWorkingOn.add(wtfSeite, new GridBagConstraints(1, 3, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		paWorkingOn.add(wnfGroesse, new GridBagConstraints(1, 4, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		paWorkingOn.add(wlaKb, new GridBagConstraints(0, 4, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		JPanel panelAnzeigenSpeichern = new JPanel();
		HvLayout layoutAnzeigenSpeichern = HvLayoutFactory.create(panelAnzeigenSpeichern, "", "[fill,grow]", "");
		layoutAnzeigenSpeichern.add(wbuAnzeigen).wrap()
			.add(wbuSpeichern);
		paWorkingOn.add(panelAnzeigenSpeichern, new GridBagConstraints(0, 5, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
//		paWorkingOn.add(wbuAnzeigen, new GridBagConstraints(0, 5, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
//		paWorkingOn.add(wbuSpeichern, new GridBagConstraints(0, 6, 4, 1, 0.0, 1.0, GridBagConstraints.CENTER,
//				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		paWorkingOn.add(imageviewer, new GridBagConstraints(4, 0, 4, 7, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		wbuAnzeigen.setEnabled(!enabled && image != null);
		wbuSpeichern.setEnabled(!enabled && image != null);
	}

	protected void actionShow() throws IOException {
		File file = createTempFile();
		HelperClient.desktopTryToOpenElseSave(file, getInternalFrame());
	}
	
	protected void actionSaveToFile() throws IOException {
		File file = createTempFile();
		HelperClient.showSaveFileDialog(file, 
				getDateiname() == null ? null : new File(getDateiname()), 
				getInternalFrame(), 
				getDateiname() == null ? null : Helper.getMime(getDateiname()));
	}
	
	protected File createTempFile() throws IOException {
		if(getImage() == null) return null;

		String name = getDateiname();
		if(name == null) {
			if (getBildExtension() == null) setBildExtension(".tiff");
			name = "tempImageHV" + getBildExtension();
		}
		String mime = Helper.getMime(name);
		File temp = File.createTempFile(Helper.getName(name), mime);
		FileOutputStream fOutputStream = new FileOutputStream(temp);
		fOutputStream.write(getImage());
		fOutputStream.close();
		return temp;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (ACTION_SPECIAL_ANZEIGEN.equalsIgnoreCase(e.getActionCommand())) {
			actionShow();
		} else if (ACTION_SPECIAL_SPEICHERN.equalsIgnoreCase(e.getActionCommand())) {
			actionSaveToFile();
		} else if (ACTION_SPECIAL_DATEI.equalsIgnoreCase(e.getActionCommand())) {
			actionSelectFile();
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFFIRST)) {
			imageIndex = 0;
			imageviewer.setImage(getImage(imageIndex));
			wtfSeite.setText("" + imageIndex);
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFLAST)) {
			imageIndex = image.length - 1;
			imageviewer.setImage(getImage(imageIndex));
			wtfSeite.setText("" + imageIndex);
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFRIGHT)) {
			imageIndex++;
			if (imageIndex >= 0 && imageIndex <= image.length - 1) {
				imageviewer.setImage(getImage(imageIndex));
				wtfSeite.setText("" + imageIndex);
			}
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFLEFT)) {
			imageIndex--;
			if (imageIndex >= 0 && imageIndex <= image.length) {
				imageviewer.setImage(getImage(imageIndex));
				wtfSeite.setText("" + imageIndex);
			}
		}

	}

	/**
	 * @throws ExceptionLP
	 * @throws Throwable
	 * @throws IOException
	 * @throws Exception
	 * @throws FileNotFoundException
	 */
	private void actionSelectFile() throws ExceptionLP, Throwable, IOException,
			Exception, FileNotFoundException {
		TiffFileOpener fileOpener = new TiffFileOpener(
				this, FileChooserConfigToken.ImportLast);
		HvOptional<WrapperFile> wf = fileOpener.selectSingle();
		if (!wf.isPresent()) return;

		FileValidator validator = new FileValidator();
		double groesseInKB = wf.get().getLengthInKB();
		if (!validator.validateFileSize(new BigDecimal(groesseInKB))) {
			imageviewer.setImage((byte[]) null);
			return;
		}

		File file = wf.get().getFile();
		if (fieldToDisplayFileName != null) {
			fieldToDisplayFileName.setText(file.getName());
		}
		imageOutput = wf.get().getBytes();
		
		int numPages = 0;
		try {
			ss = new FileSeekableStream(file);
			decoder = ImageCodec.createImageDecoder("tiff", ss, null);
			numPages = decoder.getNumPages();

			image = new RenderedImage[numPages];

			for (int i = 0; i < numPages; i++) {
				try {
					image[i] = decoder.decodeAsRenderedImage(i);
					WritableRaster wr = null;
					wr = image[i].copyData(wr);
				} catch (IOException ex2) {
				}
			}

			imageviewer.setImage(getImage(0));
			wtfDatei.setText(file.getName());
			wnfGroesse.setDouble(groesseInKB);
			wtfSeite.setText("" + imageIndex);
			
		} catch (Exception ex1) {
			String extensions = fileOpener.getAllowedExtensions();
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getMessageTextRespectUISPr(
							"lp.mediacontrol.ungueltiges.dateiformat", extensions));
			setDefaults();
		}		
	}


	private void showImage(byte[] array) {
		int numPages = 0;
		try {
			ss = SeekableStream.wrapInputStream(new ByteArrayInputStream(array), true);
			decoder = ImageCodec.createImageDecoder("tiff", ss, null);
			numPages = decoder.getNumPages();
		} catch (IOException ex1) {
		}
		image = new RenderedImage[numPages];
		for (int i = 0; i < numPages; i++) {
			try {
				image[i] = decoder.decodeAsRenderedImage(i);
				WritableRaster wr = null;
				wr = image[i].copyData(wr);

			} catch (IOException ex2) {
			}
		}

	}

	public String getBildExtension() {
		return bildExtension;
	}

	public void setBildExtension(String bildExtension) {
		this.bildExtension = bildExtension;
	}

	public String getDateiname() {
		return wtfDatei.getText();
	}

	public void setDateiname(String dateiname) {
		wtfDatei.setText(dateiname);
	}

	public byte[] getImage() {
		return imageOutput;
	}

	public java.awt.image.BufferedImage getImage(int index) throws IOException {
		image[index] = decoder.decodeAsRenderedImage(index);
		WritableRaster wr = null;
		wr = image[index].copyData(wr);
		ColorModel colorModel = image[index].getColorModel();
		BufferedImage bi = new BufferedImage(colorModel, wr, colorModel.isAlphaPremultiplied(), null);

		return bi;
	}

	public void setImage(byte[] array) throws ExceptionLP {
		imageOutput = array;
		BufferedImage bImage = null;
		try {
			if (array != null) {
				showImage(array);
				bImage = getImage(0);
				imageviewer.setImage(bImage);
			} else {
				image = null;
				imageviewer.setImage(bImage);
			}
		} catch (IOException ex) {
		}
	}

	public void cleanup() {
		imageviewer.cleanup();
		imageviewer = null;
		setToolBar(null);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuDatei;
	}

	private static void copyStream(InputStream src, OutputStream dest) throws Exception {
		byte[] buffer = new byte[16384];
		int len;
		while ((len = src.read(buffer)) > 0) {
			dest.write(buffer, 0, len);
		}
		src.close();
		dest.close();
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

	@Override
	public boolean isMandatoryField() {
		return wtfDatei.isMandatoryField;
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
		if (!isActivatable) setEnabled(false);
	}

}
