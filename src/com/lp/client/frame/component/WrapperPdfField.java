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
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.PropertiesManager;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.PdfFile;
import com.lp.client.frame.filechooser.open.PdfFileOpener;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

/*
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2010/11/05 07:51:33 $
 *
 * @todo scrollpane auf bild PJ 3416
 *
 * @todo texte uebersetzen PJ 3416
 */
public class WrapperPdfField extends PanelBasis implements IControl {

	private static final long serialVersionUID = 1L;
	private final static String ACTION_SPECIAL_DATEI = "action_special_datei";
	static final public String ACTION_SPECIAL_ANZEIGEN = "ACTION_SPECIAL_ANZEIGEN";
	private final static String ACTION_SPECIAL_SPEICHERN = "ACTION_SPECIAL_SPEICHERN";
	private final static String ACTION_SPECIAL_GROSS_ANZEIGEN = "ACTION_SPECIAL_GROSS_ANZEIGEN";

	private final String ACTION_SPECIAL_BILD_ENTFERNEN = "ACTION_SPECIAL_BILD_ENTFERNEN";

	private WrapperTextField wtfDatei = new WrapperTextField(300);
	private WrapperButton wbuDatei = new WrapperButton();
	private WrapperButton wbuAnzeigen = new WrapperButton();
	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperTextField fieldToDisplayFileName = null;

	private WrapperButton wbuGrossAnzeigen = new WrapperButton();

	private JFrame grosseAnzeige = null;

	private JPanel viewerComponentPanel = null;

	private SwingController controllerVorschau = new SwingController();
	private SwingController controllerGrosseAnzeige = new SwingController();

	private WrapperButton wbuBildEntfernen = new WrapperButton();

	boolean bMitLoeschenButton = false;

	WrapperButton buttonAnzeigen = null;

	private boolean bPDFVorschau = true;

	private static String ANZEIGEN = "ANZEIGEN";

//	private String pdfExtension = new String(".pdf");
	private JPanel jpaWorkingOn = new JPanel();
	byte[] pdf = null;
	private boolean isActivatable = true;

	public WrapperButton getButtonDatei() {
		return wbuDatei;
	}

	public WrapperPdfField(InternalFrame internalFrame, String addTitel) throws Throwable {
		super(internalFrame, addTitel);
		jbInit();
		initComponents();
	}

	public WrapperPdfField(InternalFrame internalFrame, String addTitel, WrapperTextField fieldToDisplayFileName)
			throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		jbInit();
		initComponents();
	}

	public WrapperPdfField(InternalFrame internalFrame, String addTitel, WrapperTextField fieldToDisplayFileName,
			boolean bMitLoeschenButton) throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		this.bMitLoeschenButton = bMitLoeschenButton;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());

		wbuDatei.setText(LPMain.getTextRespectUISPr("lp.datei"));
		jpaWorkingOn.setLayout(new GridBagLayout());
		wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
		wbuDatei.addActionListener(this);
		wbuAnzeigen.setActionCommand(ACTION_SPECIAL_ANZEIGEN);
		wbuAnzeigen.addActionListener(this);
		wbuAnzeigen.setText(LPMain.getTextRespectUISPr("lp.anzeigen"));
		wbuSpeichern.setActionCommand(ACTION_SPECIAL_SPEICHERN);
		wbuSpeichern.addActionListener(this);
		wbuSpeichern.setText(LPMain.getTextRespectUISPr("lp.speichern"));
		wtfDatei.setActivatable(false);

		wbuBildEntfernen = new WrapperButton();
		wbuBildEntfernen.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/leeren.png")));
		wbuBildEntfernen.setActionCommand(ACTION_SPECIAL_BILD_ENTFERNEN);
		wbuBildEntfernen.addActionListener(this);

		wbuGrossAnzeigen = new WrapperButton();
		wbuGrossAnzeigen.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/table_sql_view.png")));
		wbuGrossAnzeigen.setActionCommand(ACTION_SPECIAL_GROSS_ANZEIGEN);
		wbuGrossAnzeigen.addActionListener(this);

		Properties myProperties = new Properties();
		myProperties.setProperty("application.showLocalStorageDialogs", "false");

		PropertiesManager properties = new PropertiesManager(System.getProperties(), myProperties,
								ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));
		
//		PropertiesManager properties = PropertiesManager.getInstance();
		properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION, Boolean.FALSE);
		// properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION_HIGHLIGHT,
		// Boolean.FALSE);
		// properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION_SELECTION,
		// Boolean.FALSE);
		// properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION_TEXT,
		// Boolean.FALSE);
		properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, Boolean.FALSE);
		properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FORMS, Boolean.FALSE);
//		properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_SEARCH, Boolean.FALSE);
		// properties.setBoolean(PropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL,
		// Boolean.FALSE);

		// Build a SwingViewFactory configured with the controller
		SwingViewBuilder factory = new SwingViewBuilder(controllerVorschau, properties);

		// Use the factory to build a JPanel that is pre-configured
		// with a complete, active Viewer UI.
		viewerComponentPanel = factory.buildViewerPanel();

		// add copy keyboard command
		ComponentKeyBinding.install(controllerVorschau, viewerComponentPanel);

		// add interactive mouse link annotation support via callback
		controllerVorschau.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controllerVorschau.getDocumentViewController()));

		ImageIcon anzeigen = new ImageIcon(getClass().getResource("/com/lp/client/res/refresh.png"));
		buttonAnzeigen = new WrapperButton();
		buttonAnzeigen.setToolTipText(LPMain.getTextRespectUISPr("lp.naechstes"));
		buttonAnzeigen.setActionCommand(ANZEIGEN);
		buttonAnzeigen.setIcon(anzeigen);
		buttonAnzeigen.addActionListener(new WrapperPdfField_buttonAnzeigen_actionAdapter(this));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuGrossAnzeigen, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 15, 0));
		jpaWorkingOn.add(wbuDatei, new GridBagConstraints(0, 1, 1, 1, 0.4, 0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 30, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDatei, new GridBagConstraints(1, 1, 1, 1, 3, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(buttonAnzeigen, new GridBagConstraints(2, 1, 1, 1, 00, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 0), 15, 0));

		jpaWorkingOn.add(wbuAnzeigen, new GridBagConstraints(3, 1, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuSpeichern, new GridBagConstraints(4, 1, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		buttonAnzeigen.setVisible(false);

		if (bMitLoeschenButton) {
			jpaWorkingOn.add(wbuBildEntfernen, new GridBagConstraints(5, 1, 0, 1, 0, 0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 50, 0));
		}

		wbuAnzeigen.setEnabled(true);
		wbuDatei.setEnabled(true);

		// Bild
		JPanel panelVorschau = new JPanel();
		panelVorschau.setLayout(new GridBagLayout());

		panelVorschau.add(viewerComponentPanel, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(panelVorschau, new GridBagConstraints(0, 6, 10, 1, 1, 15, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -20, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (ACTION_SPECIAL_DATEI.equalsIgnoreCase(e.getActionCommand())) {
			actionSelectFile();
		} else if (ACTION_SPECIAL_SPEICHERN.equalsIgnoreCase(e.getActionCommand())) {
			actionSaveFile();
		} else if (ACTION_SPECIAL_BILD_ENTFERNEN.equalsIgnoreCase(e.getActionCommand())) {
			setPdf(null);
			setDateiname(null);
		} else if (e.getSource().equals(wbuAnzeigen)) {
			actionShowFile();
		} else if (e.getSource().equals(wbuGrossAnzeigen)) {

			if (pdf != null) {

				String titel = "PDF";
				if (wtfDatei.getText() != null) {
					titel = wtfDatei.getText();
				}

				if (grosseAnzeige == null) {
					SwingViewBuilder factory = new SwingViewBuilder(controllerGrosseAnzeige);

					// Use the factory to build a JPanel that is pre-configured
					// with a complete, active Viewer UI.
					JPanel viewerComponentPanel = factory.buildViewerPanel();

					// add copy keyboard command
					ComponentKeyBinding.install(controllerGrosseAnzeige, viewerComponentPanel);

					grosseAnzeige = new JFrame(titel);

//					grosseAnzeige.setIconImage(ImageIO.read(getClass().getResource("/com/lp/client/res/heliumv.png")));
					grosseAnzeige.setIconImage(IconFactory.getHeliumv().getImage());
					grosseAnzeige.getContentPane().add(viewerComponentPanel);

					grosseAnzeige.pack();
					grosseAnzeige.setVisible(true);

					controllerGrosseAnzeige.openDocument(pdf, 0, pdf.length, titel, "/" + titel);
				} else {
					grosseAnzeige.setTitle(titel);
					controllerGrosseAnzeige.openDocument(pdf, 0, pdf.length, titel, "/" + titel);
					grosseAnzeige.setVisible(true);
				}

			}

		}
	}

	private void actionSaveFile() throws IOException {
		File temp = createTempFile();
		HelperClient.showSaveFileDialog(temp, getDateiname() != null ? new File(getDateiname()) : null,
				getInternalFrame(), getDateiname() != null ? Helper.getMime(getDateiname()) : null);
	}

	private void actionShowFile() throws IOException {
		File temp = createTempFile();
		// PJ 15451
		HelperClient.desktopOpenEx(temp);

		temp.deleteOnExit();

	}

	private File createTempFile() throws IOException {
		if (pdf != null) {
			java.io.File f = File.createTempFile("hvd", ".pdf");
			try {

				java.io.FileOutputStream out = new java.io.FileOutputStream(f);
				out.write(pdf);
				out.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
			}
			return f;
		}
		return null;
	}

	public void setPDFVorschauAnzeigen(boolean bPDFVorschau) {
		this.bPDFVorschau = bPDFVorschau;

		buttonAnzeigen.setVisible(!bPDFVorschau);

	}

	public void buttonAnzeigen_actionPerformed(ActionEvent e) {
		if (pdf != null) {
			controllerVorschau.openDocument(pdf, 0, pdf.length, "DESCRIPTION", "PFAD.PDF");
		}
	}

	/**
	 * @throws IOException
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private void actionSelectFile() throws IOException, ExceptionLP, Throwable {
		HvOptional<PdfFile> wf = new PdfFileOpener(
				getInternalFrame(), FileChooserConfigToken.ImportLast)
				.selectSingle();
		if (!wf.isPresent()) return ;
		
		FileValidator validator = new FileValidator();
		long size = wf.get().getFile().length() / 1024l;
		if (!validator.validateFileSize(new BigDecimal(size))) {
			setPdf((byte[]) null);
			return;
		}
		
		setPdf(wf.get().getBytes());
		File file = wf.get().getFile();
		if (fieldToDisplayFileName != null) {
			fieldToDisplayFileName.setText(file.getName());
		}

		wtfDatei.setText(file.getName());
		wbuDatei.setToolTipText(file.getAbsolutePath());
	}

	public String getDateiname() {
		return wtfDatei.getText();
	}

	public void setDateiname(String dateiname) {
		wtfDatei.setText(dateiname);
	}


	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) throws Throwable {
		this.pdf = pdf;
		// Open a PDF document to view

		if (pdf != null && bPDFVorschau) {

			String titel = "PDF";
			if (wtfDatei.getText() != null) {
				titel = wtfDatei.getText();
			}

			controllerVorschau.openDocument(pdf, 0, pdf.length, titel, "/" + titel);
		} else {
			controllerVorschau.closeDocument();
		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuDatei;
	}

	@Override
	public void removeContent() throws Throwable {
		setPdf(null);
		setDateiname(null);
	}

	@Override
	public boolean hasContent() throws Throwable {
		return wtfDatei.hasContent();
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
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		wbuDatei.setEnabled(enabled);
		wbuAnzeigen.setEnabled(!enabled && getPdf() != null);
		wbuSpeichern.setEnabled(!enabled && getPdf() != null);
		wbuBildEntfernen.setEnabled(enabled);
	}

}

class WrapperPdfField_buttonAnzeigen_actionAdapter implements ActionListener {
	private WrapperPdfField adaptee;

	WrapperPdfField_buttonAnzeigen_actionAdapter(WrapperPdfField adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.buttonAnzeigen_actionPerformed(e);
	}
}
