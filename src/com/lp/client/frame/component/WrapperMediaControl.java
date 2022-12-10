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
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.TransferHandler;

import org.apache.tika.mime.MediaType;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.ReportViewer;
import com.lp.client.pc.LPMain;
import com.lp.client.rechtschreibung.IRechtschreibPruefbar;
import com.lp.client.util.IconFactory;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JasperPrint;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich ...</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 * 
 * <p>@author $Author: heidi $</p>
 * 
 * @version not attributable Date $Date: 2009/04/24 07:55:59 $
 */
public class WrapperMediaControl extends PanelBasis implements IRechtschreibPruefbar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String ACTION_SPECIAL_BILD_ENTFERNEN = "ACTION_SPECIAL_BILD_ENTFERNEN";
	private String jpgExtension = new String(".jpg");
	private String pngExtension = new String(".png");
	private String gifExtension = new String(".gif");
	private String tiffExtension = new String(".tiff");

	private JPanel jPanelArt = new JPanel();
	private JPanel paWorkOn = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperLabel wlaArt = new WrapperLabel();

	private WrapperEditorField wef;
	private WrapperBildField wbf;
	private WrapperTiffViewer wtv;
	private WrapperSonstige ws;
	private WrapperPdfField wpf;
	private WrapperMailViewField wmf;
	private ReportViewer rv;
	private int iSpaltenbreite1 = 80;
	private boolean bMitDefaultbildFeld = false;
	protected boolean bWithoutButtons = false;
	private boolean bDropEnabled = true;

	private WrapperButton jbuFromClipboard = new WrapperButton();

	private WrapperCheckBox wcbDefaultbild = new WrapperCheckBox();
	private WrapperComboBox wcoArt = new WrapperComboBox();
	private WrapperButton wbuBildEntfernen = new WrapperButton();
	private WrapperTextField wtfTextFieldFuerDateiname = null;

	public void setPDFVorschauAnzeigen(boolean bPDFVorschau) {
		wpf.setPDFVorschauAnzeigen(bPDFVorschau);
	}

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel) throws Throwable {
		this(internalFrame, addTitel, false);
	}

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel, boolean bMitDefaultbildFeld,
			WrapperTextField wtfTextFieldFuerDateiname) throws Throwable {
		this(internalFrame, addTitel, bMitDefaultbildFeld);
		this.wtfTextFieldFuerDateiname = wtfTextFieldFuerDateiname;
		jbInit();
	}

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel, boolean bMitDefaultbildFeld)
			throws Throwable {
		this(internalFrame, addTitel, bMitDefaultbildFeld, false);
	}

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel, boolean bMitDefaultbildFeld,
			boolean bWithoutButtons) throws Throwable {
		super(internalFrame, addTitel);
		this.bMitDefaultbildFeld = bMitDefaultbildFeld;
		this.bWithoutButtons = bWithoutButtons;
		jbInit();
		initComponents();
	}

	public void setDropAreaEnabled(boolean enableDropArea) {
		bDropEnabled = enableDropArea;
	}

	public void cleanup() {
		wef.lpEditor.cleanup();
		wef.lpEditor = null;
		wef.jspScrollPane.setViewport(null);
		wef.setToolBar(null);

		wbf.cleanup();
		paWorkOn.remove(wbf);
		wbf = null;
		wtv.cleanup();
		paWorkOn.remove(wtv);
		wtv = null;

		wpf.setToolBar(null);
		ws.setToolBar(null);
		paWorkOn.remove(rv);
		rv.cleanup();
		rv = null;
		wmf.cleanup();

	}

	protected String getLockMeWer() throws Exception {
		return null;
	}

	public Map<String, String> getSelectableMimeTypes(boolean ohneText) {
		Map<String, String> tmArten = new TreeMap<String, String>();
		if (!ohneText) {
			tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML, MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
		}
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF, MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT, MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER, MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822, MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK, MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK);
		return tmArten;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());

		HvDropTarget dt = new HvDropTarget(this);
		dt.addDropListener(new FileImportDropHandler(this));
//		this.setTransferHandler(new FileImportTransferHandlerMC(this));

		jPanelArt.setLayout(gridBagLayout1);
		paWorkOn.setLayout(gridBagLayout1);

		wef = createEditorField();
		wbf = new WrapperBildField(getInternalFrame(), "", wtfTextFieldFuerDateiname, bWithoutButtons);
		wtv = new WrapperTiffViewer(getInternalFrame(), "", wtfTextFieldFuerDateiname);
		ws = new WrapperSonstige(getInternalFrame(), "", wtfTextFieldFuerDateiname);
		wpf = new WrapperPdfField(getInternalFrame(), "", wtfTextFieldFuerDateiname);
		wmf = new WrapperMailViewField(getInternalFrame(), "", bWithoutButtons);
		initReportViewer(new ReportViewer(null));
		wbf.setVisible(false);
		wtv.setVisible(false);
		enableEditorField(false);
		ws.setVisible(false);
		wpf.setVisible(false);
		rv.setVisible(false);
		wmf.setVisible(false);
		wcoArt.setMap(getSelectableMimeTypes(false));
		if (!bWithoutButtons) {
			if (jPanelArt == null) {
				jPanelArt = new JPanel();
				GridBagLayout gridBagLayout = new GridBagLayout();
				jPanelArt.setLayout(gridBagLayout);
				jPanelArt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			}
			wlaArt = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("label.art"));
			wlaArt.setMaximumSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
			wlaArt.setMinimumSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
			wlaArt.setPreferredSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
			// wcoArt.setMandatoryField(true);

			wcbDefaultbild.setText(LPMain.getInstance().getTextRespectUISPr("lp.defaultbild"));

			HelperClient.setMinimumAndPreferredSize(wcbDefaultbild, HelperClient.getSizeFactoredDimension(150));

			wbuBildEntfernen = new WrapperButton();
			wbuBildEntfernen.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/leeren.png")));
			wbuBildEntfernen.setActionCommand(ACTION_SPECIAL_BILD_ENTFERNEN);
			wbuBildEntfernen.addActionListener(this);

			jbuFromClipboard = new WrapperButton();
			jbuFromClipboard.setIcon(IconFactory.getPaste());
			jbuFromClipboard.setActionCommand("FROM_CLIPBOARD");
			jbuFromClipboard.addActionListener(this);
			jbuFromClipboard.setToolTipText(LPMain.getTextRespectUISPr("lp.bildfield.paste"));

			HelperClient.setMinimumAndPreferredSize(jbuFromClipboard, new Dimension(
					Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

			wcoArt.addActionListener(new WrapperMediaControl_jComboBoxPositionsart_actionAdapter(this));
			jPanelArt.add(wlaArt, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 50, 0));

			jPanelArt.add(jbuFromClipboard, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, Defaults.sizeFactor(22)), 0, 0));

			jPanelArt.add(wcoArt, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 25, 2, 2), 0, 0));
			jPanelArt.add(wbuBildEntfernen, new GridBagConstraints(2, 0, 0, 1, 0, 0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 50, 0));

			if (bMitDefaultbildFeld == true) {
				jPanelArt.add(wcbDefaultbild, new GridBagConstraints(5, 0, 0, 1, 0, 0, GridBagConstraints.WEST,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

				/*
				 * jPanelArt.add(wcbDefaultbild, new GridBagConstraints(2, 0, 1, 1, 0, 0.0 ,
				 * GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2),
				 * 100, 0));
				 */
			}
		}
		paWorkOn.add(wbf, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		paWorkOn.add(wtv, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		paWorkOn.add(wef, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		paWorkOn.add(ws, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		paWorkOn.add(wpf, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		paWorkOn.add(wmf, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		this.add(jPanelArt, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		this.add(paWorkOn, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	public WrapperButton getWbuBildEntfernen() {
		return wbuBildEntfernen;
	}

	private void initReportViewer(ReportViewer reportViewer) {
		rv = reportViewer;
		paWorkOn.add(rv, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	/**
	 * Override um anderen Editor zu Instanzieren.
	 * 
	 * @throws Throwable
	 */
	protected WrapperEditorField createEditorField() throws Throwable {
		return new WrapperEditorField(getInternalFrame(), "", bWithoutButtons);
	}

	public void setDefaultbildFeld(Short s) {
		wcbDefaultbild.setShort(s);
	}

	public Short getDefaultbildFeld() {
		return wcbDefaultbild.getShort();
	}

	private String getNormalizedMimeType() {
		String mime = getMimeType();
		if (wcoArt.isSelectedItemEmpty()) {
			mime = MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT;
		}
		return mime;
	}

	public void setOMedia(byte[] media) throws Throwable {

//	Das ist ein No-Op. getMimeType() aus wcoArt.getKeyOfSelected()
// den Wert der jetzt gesetzt wird.
//		wcoArt.setKeyOfSelectedItem(getMimeType());

		String mime = getNormalizedMimeType();
		if (MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML.equals(mime)) {
			setEditorContent(media);
		} else if (MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF.equals(mime)) {
			wtv.setImage(media);
//			if (media != null) {
//				wtv.setImage(media);
//			}
		} else if (MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT.equals(mime)) {
			ws.setDatei(media);
//			if (media != null) {
//				ws.setDatei(media);
//			}
		} else if (MediaFac.DATENFORMAT_MIMETYPE_APP_PDF.equals(mime)) {
			wpf.setPdf(media);
//			if (media != null) {
//				wpf.setPdf(media);
//			}
		} else if (MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER.equals(mime)) {
			if (media != null) {
				ByteArrayInputStream bStream = new ByteArrayInputStream(media);
				ObjectInputStream oStream = new ObjectInputStream(bStream);
				JasperPrint jPrint = (JasperPrint) oStream.readObject();
				rv.loadReport(jPrint);
				rv.refreshPage();
			}
		} else if (MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK.contentEquals(mime)) {
			wmf.setMail(media, mime);
		}

		else {
			try {
				wbf.setImage(media);
			} catch (Exception ex) {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.system.image.format"), "");
			}
		}
	}

	public void setOMediaText(String media) throws Throwable {
		wcoArt.setKeyOfSelectedItem(getMimeType());
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			setEditorContent(media);
		}
	}

	public void setOMediaImage(byte[] media) throws Throwable {

// Ist faktisch ein Noop
//		wcoArt.setKeyOfSelectedItem(getMimeType());
		String mime = getNormalizedMimeType();
		if (MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF.equals(mime)) {
			wtv.setImage(media);
		} else if (MediaFac.DATENFORMAT_MIMETYPE_APP_PDF.equals(mime)) {
			wpf.setPdf(media);
		} else if (MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT.equals(mime)) {
			ws.setDatei(media);
		} else if (MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK.equals(mime)) {
			wmf.setMail(media, mime);
		} else {
			try {
				wbf.setImage(media);
			} catch (Exception ex) {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.system.image.format"), "");
			}
		}
	}

	/**
	 * 
	 * @deprecated AD gibt Probleme mit dem encoding bei Text use setOMediaText oder
	 *             setOMediaimage
	 * @throws Throwable
	 * @return byte[]
	 */
	public byte[] getOMedia() throws Throwable {
		String mime = getNormalizedMimeType();
		if (MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG.equals(mime)) {
			BufferedImage im = wbf.getImage();
			return im != null ? Helper.imageToByteArray(im) : null;
		} else if (MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML.equals(mime)) {
			return getEditorContentAsByte();
		} else if (MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT.equals(mime)) {
			return ws.getDatei();
		} else if (MediaFac.DATENFORMAT_MIMETYPE_APP_PDF.equals(mime)) {
			return wpf.getPdf();
		} else if (MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK.equals(mime)) {
			return wmf.getMailData();
		} else

		{
			// tiff to byte[]
			return wtv.getImage();
		}
	}

	public String getOMediaText() throws Throwable {
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			return getEditorContent();
		} else {
			return null;
		}
	}

	public byte[] getOMediaImage() throws Throwable {
		String mime = getNormalizedMimeType();
		if (MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG.equals(mime)) {
			return wbf.getImageOriginalBytes();
		} else if (MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF.equals(mime)) {
			return wtv.getImage();
		} else if (MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT.equals(mime)) {
			return ws.getDatei();
		} else if (MediaFac.DATENFORMAT_MIMETYPE_APP_PDF.equals(mime)) {
			return wpf.getPdf();
		} else if (MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822.equals(mime)
				|| MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK.equals(mime)) {
			return wmf.getMailData();
		} else {
			return null;
		}
	}

	public String getMimeType() {
		return wcoArt.getSelectedItem().toString();
	}

	public void setMimeType(String mimetype) {
		wcoArt.setKeyOfSelectedItem(mimetype);
		jComboBoxPositionsart_actionPerformed(null);
	}

	public void setDateiname(String dateiname) {
		wbf.getDateiButton().setToolTipText(dateiname);
		wbf.setDateiname(dateiname);
		wtv.setDateiname(dateiname);
		wpf.setDateiname(dateiname);
		ws.setDateiname(dateiname);
		wmf.setDateiname(dateiname);
	}

	public String getDateiname() {
		String dateiName = "";
		Object currentArt = wcoArt.getKeyOfSelectedItem();
		if (currentArt != null) {
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
				dateiName = wtv.getDateiname();
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
				dateiName = wpf.getDateiname();
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
				dateiName = ws.getDateiname();
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822)
					|| currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK)) {
				dateiName = wmf.getDateiname();
			} else {
				dateiName = wbf.getDateiname();
			}
		}
		return dateiName;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_BILD_ENTFERNEN)) {
			wbf.setImage(null);
			wtv.setImage(null);
			wpf.setPdf(null);
			wpf.setDateiname(null);
			wmf.removeContent();
		} else if (e.getSource().equals(jbuFromClipboard)) {
			Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable transferData = systemClipboard.getContents(null);
			if (transferData != null) {
				if (transferData.isDataFlavorSupported(DataFlavor.imageFlavor)) {
					Image img = (Image) transferData.getTransferData(DataFlavor.imageFlavor);
					BufferedImage o = Helper.imageToBufferedImage(img);
					setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
					setDateiname("zwischenablage" + pngExtension);
					wbf.setImage(Helper.imageToByteArrayWithType(o, pngExtension.substring(1)));

				} else if (transferData.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

					@SuppressWarnings("unchecked")
					List<File> l = (List<File>) transferData.getTransferData(DataFlavor.javaFileListFlavor);
					Iterator<File> iter = l.iterator();
					while (iter.hasNext()) {
						File file = iter.next();

						if (file.getPath().toLowerCase().endsWith(".jpg")
								|| file.getPath().toLowerCase().endsWith(".png")
								|| file.getPath().toLowerCase().endsWith(".gif")
								|| file.getPath().toLowerCase().endsWith(".tiff")
								|| file.getPath().toLowerCase().endsWith(".pdf")) {

							String extension = file.getPath().toLowerCase().substring(file.getPath().length() - 3,
									file.getPath().length());
							
							if(file.getPath().toLowerCase().endsWith(".tiff")) {
								 extension = file.getPath().toLowerCase().substring(file.getPath().length() - 4,
											file.getPath().length());
							}
							

							if (extension.toUpperCase().equals("PDF")) {
								setMimeType(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
								setOMedia(Helper.getBytesFromFile(file));
							} else {

								if (extension.toUpperCase().equals("GIF")) {
									setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF);
									setOMediaImage(Helper.getBytesFromFile(file));
								} else if (extension.toUpperCase().equals("PNG")) {
									setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
									setOMediaImage(Helper.getBytesFromFile(file));
								} else if (extension.toUpperCase().equals("JPG")
										|| extension.toUpperCase().equals("JPEG")) {
									setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
									setOMediaImage(Helper.getBytesFromFile(file));
								} else if (extension.toUpperCase().equals("TIFF")) {
									setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF);
									setOMediaImage(Helper.getBytesFromFile(file));
								}

							}

							setDateiname("zwischenablage" + "." + extension);

							break;
						}

					}

				}

			}

		}
	}

	protected void enableEditorField(boolean enable) {
		wef.setVisible(enable);
	}

	protected void setEditorContent(String content) {
		wef.setText(content == null ? "" : content);
	}

	protected void setEditorContent(byte[] content) {
		wef.setText(content == null ? "" : new String(content));
	}

	protected String getEditorContent() throws TextBlockOverflowException {
		return wef.getText();
	}

	protected byte[] getEditorContentAsByte() throws TextBlockOverflowException {
		return wef.getText() != null ? wef.getText().getBytes() : null;
	}

	/**
	 * Auswahl der Positionsart ist erfolgt.
	 * 
	 * @param e ActionEvent
	 */
	void jComboBoxPositionsart_actionPerformed(ActionEvent e) {

		Object currentArt = wcoArt.getKeyOfSelectedItem();
		wpf.setVisible(false);
		wtv.setVisible(false);
		wbf.setVisible(false);
		enableEditorField(false);
		wcbDefaultbild.setVisible(false);
		ws.setVisible(false);
		if (rv != null) {
			rv.setVisible(false);
		}
		wmf.setVisible(false);
		if (currentArt != null) {
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
				enableEditorField(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)) {
				wbf.setVisible(true);
				wbf.setBildExtension(jpgExtension);
				wcbDefaultbild.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
				wbf.setVisible(true);
				wbf.setBildExtension(pngExtension);
				wcbDefaultbild.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
				wbf.setVisible(true);
				wbf.setBildExtension(gifExtension);
				wcbDefaultbild.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
				wtv.setVisible(true);
				wbf.setBildExtension(tiffExtension);
				wcbDefaultbild.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
				ws.setVisible(true);
				wcbDefaultbild.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
				wpf.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER)) {
				rv.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK)) {
				wmf.setVisible(true);
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822)) {
				wmf.setVisible(true);
			}
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoArt;
	}

	public WrapperComboBox getWcoArt() {
		return wcoArt;
	}

	public void setReportViewer(ReportViewer reportViewer) {
		initReportViewer(reportViewer);
	}

	private class FileImportDropHandler implements DropListener {
		WrapperMediaControl wmc=null;
		public FileImportDropHandler(WrapperMediaControl wmc) {
			this.wmc=wmc;
		}

		@Override
		public void filesDropped(List<File> files) {
			if (!wcoArt.isEnabled()) {
				return;
			}
			if (!bDropEnabled) {
				return;
			}
			try {
				for (File f : files) {
					MediaType mimeType = HelperClient.getMimeTypeOfFile(f);
					String filename = f.getName();

					String extension = filename.substring(filename.lastIndexOf(".") + 1);

					setDateiname(filename);
					
					
					FileValidator validator = new FileValidator();
					long size = f.length() / 1024l;
					if (!validator.validateFileSize(new BigDecimal(size))) {
						return;
					}
					

					if (extension.toUpperCase().equals("PDF")) {
						setMimeType(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
						setOMedia(Helper.getBytesFromFile(f));
						getInternalFrame().fireItemChanged(wmc, ItemChangedEvent.GOTO_DETAIL_PANEL);
					} else if (extension.toUpperCase().equals("GIF")) {
						setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF);
						setOMediaImage(Helper.getBytesFromFile(f));
						getInternalFrame().fireItemChanged(wmc, ItemChangedEvent.GOTO_DETAIL_PANEL);
					} else if (extension.toUpperCase().equals("PNG")) {
						setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
						setOMediaImage(Helper.getBytesFromFile(f));
						getInternalFrame().fireItemChanged(wmc, ItemChangedEvent.GOTO_DETAIL_PANEL);
					} else if (extension.toUpperCase().equals("JPG") || extension.toUpperCase().equals("JPEG")) {
						setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
						setOMediaImage(Helper.getBytesFromFile(f));
						getInternalFrame().fireItemChanged(wmc, ItemChangedEvent.GOTO_DETAIL_PANEL);
					} else if (extension.toUpperCase().equals("TIFF")) {
						setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF);
						setOMediaImage(Helper.getBytesFromFile(f));
						getInternalFrame().fireItemChanged(wmc, ItemChangedEvent.GOTO_DETAIL_PANEL);
					} else if (extension.toUpperCase().equals("EML")) {
						setMimeType(MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822);
						setOMedia(Helper.getBytesFromFile(f));
						getInternalFrame().fireItemChanged(wmc, ItemChangedEvent.GOTO_DETAIL_PANEL);
					} else if (extension.toUpperCase().equals("MSG")) {
						setMimeType(MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK);
						setOMedia(Helper.getBytesFromFile(f));
						getInternalFrame().fireItemChanged(wmc, ItemChangedEvent.GOTO_DETAIL_PANEL);
					}else {
						//SP9757
						JOptionPane pane = new JOptionPane(filename);
						final JDialog dialog = pane
								.createDialog(LPMain.getTextRespectUISPr("lp.datei.nicht.hinzugefuegt"));
						dialog.setModal(true);
						dialog.setSize(400, dialog.getHeight());
						
						dialog.setLocationRelativeTo(getInternalFrame());

						dialog.setVisible(true);
					}
					
					
				
					
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void aktiviereRechtschreibpruefung() {
		wef.aktiviereRechtschreibpruefung();
	}

	@Override
	public void deaktiviereRechtschreibpruefung() {
		wef.deaktiviereRechtschreibpruefung();
	}

	@Override
	public void setRechtschreibpruefungLocale(Locale loc) {
		wef.setRechtschreibpruefungLocale(loc);
	}
}

class WrapperMediaControl_jComboBoxPositionsart_actionAdapter implements java.awt.event.ActionListener {
	WrapperMediaControl adaptee;

	WrapperMediaControl_jComboBoxPositionsart_actionAdapter(WrapperMediaControl adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxPositionsart_actionPerformed(e);
	}
}
