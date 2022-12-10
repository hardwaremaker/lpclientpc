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
package com.lp.client.frame.report;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingViewBuilder;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.pdf.HvPdfSwingController;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.HVPDFExporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class PanelReport extends PanelDialog {

	private static final long serialVersionUID = -5039368440124924288L;

	private JasperPrintLP print = null;
	private ReportViewer jpaViewer = null;

	private Supplier<Optional<String>> filenameSupplier = null;

	public ReportViewer getJpaViewer() {
		return jpaViewer;
	}

	public PanelReport(InternalFrame internalFrame, String addTitleI, JasperPrintLP print) throws Throwable {
		this(internalFrame, addTitleI, print, true);
	}

	public PanelReport(InternalFrame internalFrame, String addTitleI, JasperPrintLP print, boolean bShowExitButton)
			throws Throwable {

		super(internalFrame, addTitleI, bShowExitButton);
		this.print = print;
		jbInit();
		initComponents();

		LockStateValue lockstateValue = new LockStateValue(null, null, LOCK_NO_LOCKING);
		updateButtons(lockstateValue);
	}

	/**
	 * Setze einen Supplier, der einen Dateinamen f&uuml;r den Report erzeugt, wenn
	 * er gespeichert werden muss <br>
	 * Mit null wird ein bestehender FilenameSupplier geloescht
	 * 
	 * @param filenameSupplier
	 */
	public void setReportFilenameSupplier(Supplier<Optional<String>> filenameSupplier) {
		this.filenameSupplier = filenameSupplier;
	}

	private void jbInit() throws Throwable {

		JasperPrint jasperPrint = print.getPrint();

		jpaViewer = new ReportViewer(jasperPrint);
		/**
		 * @todo scrollleisten nur sichtbar wenn notwendig PJ 5099
		 */
		// jpaViewer.getBtnFitWidth().doClick();
		// Actionpanel von Superklasse holen und anhaengen.
		JPanel panelButtonAction = getToolBar().getToolsPanelLeft();
		jpaViewer.getBtnSave().setVisible(false);
		jpaViewer.getBtnPrint().setVisible(false);
		jpaViewer.getBtnReload().setVisible(false);
		panelButtonAction.add(jpaViewer.getBtnFirst());
		panelButtonAction.add(jpaViewer.getBtnPrevious());
		panelButtonAction.add(jpaViewer.getBtnNext());
		panelButtonAction.add(jpaViewer.getBtnLast());
		panelButtonAction.add(jpaViewer.getTxtGoto());
		panelButtonAction.add(jpaViewer.getBtnActualSize());
		panelButtonAction.add(jpaViewer.getBtnFitPage());
		panelButtonAction.add(jpaViewer.getBtnFitWidth());
		panelButtonAction.add(jpaViewer.getBtnZoomIn());
		panelButtonAction.add(jpaViewer.getBtnZoomOut());
		panelButtonAction.add(jpaViewer.getCmbZoom());
		panelButtonAction.add(jpaViewer.getBtnPDF());

		jpaViewer.getBtnPDF().addActionListener(new SaveActionListener());

		jpaWorkingOn.add(jpaViewer, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
	}

	void doClickOnPrint() {
		jpaViewer.getBtnPrint().doClick();
	}

	void doClickOnSave() {
		jpaViewer.getBtnSave().doClick();
	}

	public JasperPrintLP getPrint() {
		return print;
	}

	public void clearPrint() {
		print = null;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	private final class SaveActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent evt) {

			if (print != null && print.getPrint().getPages().size() > 0) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				HVPDFExporter exporter = new HVPDFExporter();
				exporter.setExporterInput(new SimpleExporterInput(print.getPrint()));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

				try {
					exporter.exportReport();

					HvPdfSwingController controllerGrosseAnzeige = new HvPdfSwingController(print.getSReportName(), filenameSupplier);
					SwingViewBuilder factory = new SwingViewBuilder(controllerGrosseAnzeige);

					// Use the factory to build a JPanel that is pre-configured
					// with a complete, active Viewer UI.
					JPanel viewerComponentPanel = factory.buildViewerPanel();

					// add copy keyboard command
					ComponentKeyBinding.install(controllerGrosseAnzeige, viewerComponentPanel);

					JFrame grosseAnzeige = new JFrame(LPMain.getTextRespectUISPr("lp.print.pdfvorschau"));

					// grosseAnzeige.setIconImage(ImageIO.read(getClass().getResource("/com/lp/client/res/heliumv.png")));
					grosseAnzeige.setIconImage(IconFactory.getHeliumv().getImage());

					grosseAnzeige.getContentPane().add(viewerComponentPanel);

					grosseAnzeige.pack();
					grosseAnzeige.setVisible(true);

					byte[] pdf = baos.toByteArray();

					controllerGrosseAnzeige.openDocument(pdf, 0, pdf.length, "", "/" + "");

				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// } catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}
	}

}
