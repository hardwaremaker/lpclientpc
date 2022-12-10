package com.lp.client.frame.report;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.util.HVPDFExporter;
import com.lp.util.report.ReportPatch;

public class ReportCopyViewer extends ReportViewer implements ActionListener {
	private static final long serialVersionUID = -4351711275018996127L;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private JasperPrint jrPrintOriginal;
	private ActionListener[] printListener;

	public ReportCopyViewer(JasperPrint jrPrint) throws JRException {
		super(jrPrint);
		jrPrintOriginal = jrPrint;
		initPrintListener();
	}

	@Override
	public void loadReport(JasperPrint jrPrint) {
		jrPrintOriginal = jrPrint;
		super.loadReport(jrPrint);
	}

	/**
	 * Alle Listener des Druckbuttons (aus der JRViewer-Basisklasse) rausnehmen und
	 * mich selbst als Listener hinzufuegen
	 */
	private void initPrintListener() {
		printListener = getBtnPrint().getActionListeners();
		for (ActionListener l : printListener) {
			getBtnPrint().removeActionListener(l);
		}

		getBtnPrint().addActionListener(this);

		getBtnPDF().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				if (jrPrintOriginal != null && jrPrintOriginal.getPages().size() > 0) {

					ByteArrayOutputStream baos = new ByteArrayOutputStream();

					HVPDFExporter exporter = new HVPDFExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrPrintOriginal);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

					try {
						exporter.exportReport();

						SwingController controllerGrosseAnzeige = new SwingController();
						SwingViewBuilder factory = new SwingViewBuilder(controllerGrosseAnzeige);

						// Use the factory to build a JPanel that is pre-configured
						// with a complete, active Viewer UI.
						JPanel viewerComponentPanel = factory.buildViewerPanel();

						// add copy keyboard command
						ComponentKeyBinding.install(controllerGrosseAnzeige, viewerComponentPanel);

						JFrame grosseAnzeige = new JFrame(LPMain.getTextRespectUISPr("lp.print.pdfvorschau"));

//						grosseAnzeige
//								.setIconImage(ImageIO.read(getClass().getResource("/com/lp/client/res/heliumv.png")));
						grosseAnzeige.setIconImage(IconFactory.getHeliumv().getImage());
						grosseAnzeige.getContentPane().add(viewerComponentPanel);

						grosseAnzeige.pack();
						grosseAnzeige.setVisible(true);

						byte[] pdf = baos.toByteArray();

						controllerGrosseAnzeige.openDocument(pdf, 0, pdf.length, "", "/" + "");

					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
//					} catch (IOException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
					}

				}

			}

		});

	}

	private void notifyPrintListener(ActionEvent printEvent) {
		for (ActionListener l : printListener) {
			l.actionPerformed(printEvent);
		}
	}

	private JasperPrint loadCopy(JasperPrint jrPrint) {
		if (jrPrint == null)
			return jrPrint;

		try {

			List<JRPrintElement> elementsToCopy = DelegateFactory.getInstance().getSystemReportDelegate()
					.getReportCopy(jrPrint, "");

			for (JRPrintPage page : jrPrint.getPages()) {
				for (JRPrintElement element : elementsToCopy) {
					page.addElement(element);
				}
			}

			ReportPatch reportPatch = new ReportPatch(jrPrint);
			reportPatch.apply();
			return jrPrint;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jrPrint;
	}

	@Override
	public void actionPerformed(ActionEvent printEvent) {
		JasperPrint jrPrintCopy = loadCopy(jrPrintOriginal);

//		super.loadReport(jrPrintCopy);
//		notifyPrintListener(printEvent);
//		super.loadReport(jrPrintOriginal);
		actionPrint(jrPrintCopy);
	}

	/**
	 * Code aus JRViewer-Basisklasse aus dem actionPerformed des Druckbuttons
	 * uebernommen, da diese Methode nur package-visible ist. Damit kann die Kopie
	 * gedruckt werden und das Original bleibt in der Ansicht.
	 * 
	 * @param jrPrint
	 */
	private void actionPrint(final JasperPrint jrPrint) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					getBtnPrint().setEnabled(false);
					ReportCopyViewer.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					JasperPrintManager.getInstance(viewerContext.getJasperReportsContext()).print(jrPrint, true);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(ReportCopyViewer.this, getBundleString("error.printing"));
				} finally {
					ReportCopyViewer.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					getBtnPrint().setEnabled(true);
				}
			}
		});
		thread.start();
	}

}
