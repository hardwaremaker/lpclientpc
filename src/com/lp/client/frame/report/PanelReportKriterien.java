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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterName;
import javax.swing.AbstractButton;
import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.bestellung.ReportWepEtikett2;
import com.lp.client.eingangsrechnung.ReportEingangsrechnung;
import com.lp.client.frame.AttachmentDialog;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.IControl;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperComboBoxPeriode;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.ChooserSaveDialogJasper;
import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.WrapperSaveFileChooser;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.IOpenTransBeleg;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.client.util.ExcFactory;
import com.lp.client.zeiterfassung.ReportWochenabschluss;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.OpenTransXmlReportResult;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.ZugferdResult;
import com.lp.server.system.jcr.service.DokumentnichtarchiviertDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ReportkonfDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.StandarddruckerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.type.OrientationEnum;

public class PanelReportKriterien extends PanelDialog implements VetoableChangeListener {
	private static final long serialVersionUID = -2100850848872925207L;

	private javax.swing.Timer timer = null;

	private PanelStandardDrucker panelStandardDrucker = null;
	private PanelVersandEmail panelVersandEmail = null;
	private PanelVersandFax panelVersandFax = null;
	private JPanel jpaTop = null;
	private PanelReport panelReport = null;
	private PanelReportIfJRDS jpaPanelReportIf = null;
	private String LINE_BREAK = "\n";
	private String CELL_BREAK = "\t";
	private List<ReportkonfDto> reportkonfCache;
	private Timestamp berechnungsZeitpunkt = null;

	private boolean bZoomfaktorGesetzt = false;
	private Object letzterZoomfaktor = null;

	public PanelStandardDrucker getPanelStandardDrucker() {
		return panelStandardDrucker;
	}

	private JSplitPane jSplitPane1 = new JSplitPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayoutTop = new GridBagLayout();
	private WrapperButton wbuErweitert = null;
	// private String sBelegnummer = null;

	private ReportkonfDto[] konfDtos = null;
	boolean bVariantenInitialisiert = false;

	// private boolean bMitEmailFax = false;
	// private boolean bNurVorschau = false;
	// private PartnerDto partnerDtoEmpfaenger = null;
	// private Integer ansprechpartnerIId = null;
	// private boolean bDirekt = false;
	private StandarddruckerDto standarddruckerDto = null;
	protected int iKopien = 0;

	private PanelReportKriterienOptions options;

	private static final String ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW = "action_special_" + ALWAYSENABLED
			+ "reportkriterien_preview";
	private static final String ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN = "action_special_" + ALWAYSENABLED
			+ "reportkriterien_fullscreen";
	private static final String ACTION_SPECIAL_DRUCKEN = "action_special_" + ALWAYSENABLED + "reportkriterien_drucken";
	private static final String ACTION_SPECIAL_EMAIL = "action_special_" + ALWAYSENABLED + "reportkriterien_email";
	private static final String ACTION_SPECIAL_FAX = "action_special_" + ALWAYSENABLED + "reportkriterien_fax";
	private static final String ACTION_SPECIAL_CSV = "action_special_" + ALWAYSENABLED + "reportkriterien_csv";
	private static final String ACTION_SPECIAL_SAVE = "action_special_" + ALWAYSENABLED + "reportkriterien_save";
	private static final String ACTION_SPECIAL_ERWEITERT = "action_special_" + ALWAYSENABLED
			+ "reportkriterien_erweitert";
	private static final String ACTION_SPECIAL_DRUCKER_SPEICHERN = "action_special_" + ALWAYSENABLED
			+ "reportkriterien_drucker_speichern";
	private static final String ACTION_SPECIAL_DRUCKER_LOESCHEN = "action_special_" + ALWAYSENABLED
			+ "reportkriterien_drucker_loeschen";

	private static final String ACTION_COPY_TO_CLIPBOARD = "action_special_" + ALWAYSENABLED
			+ "reportkriterien_copy_to_clipboard";

	public PanelReportKriterien(InternalFrame internalFrame, PanelReportIfJRDS panelReportIf, String addTitleI,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId, boolean bDirekt, boolean bMitEmailFax,
			boolean bNurVorschau) throws Throwable {
		this(internalFrame, panelReportIf, addTitleI, partnerDtoEmpfaenger, ansprechpartnerIId, bDirekt, bMitEmailFax,
				bNurVorschau, true);
	}

	public void setzeTimer(int timeInMillis) {
		timer = new javax.swing.Timer(timeInMillis, this);

		timer.setInitialDelay(timeInMillis);
		timer.start();
		timer.setActionCommand(ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW);
	}

	public PanelReportKriterien(InternalFrame internalFrame, PanelReportIfJRDS panelReportIf, String addTitleI,
			PartnerDto partnerDtoEmpfaenger, Integer ansprechpartnerIId, boolean bDirekt, boolean bMitEmailFax,
			boolean bNurVorschau, boolean mitExitButton) throws Throwable {
		super(internalFrame, addTitleI, mitExitButton);
		options = new PanelReportKriterienOptions();
		options.setPartnerDtoEmpfaenger(partnerDtoEmpfaenger);
		options.setAnsprechpartnerIId(ansprechpartnerIId);
		options.setDirekt(bDirekt);
		options.setMitEmailFax(bMitEmailFax);
		options.setNurVorschau(bNurVorschau);

		initialize(panelReportIf);
	}

	public PanelReportKriterien(PanelReportIfJRDS panelReportIf, PanelReportKriterienOptions options) throws Throwable {
		super(options.getInternalFrame(), options.getAddTitleI(), options.isMitExitButton());
		this.options = options;

		initialize(panelReportIf);
	}

	private void initialize(PanelReportIfJRDS panelReportIf) throws Throwable {
		this.jpaPanelReportIf = panelReportIf;

		jbInitPanel();
		initComponents();
		LockStateValue lockstateValue = new LockStateValue(null, null, LOCK_NO_LOCKING);
		this.updateButtons(lockstateValue);
		// Alle Controls aktivieren
		if (panelReportIf instanceof PanelBasis) {
			enableAllComponents(this, true);
		}

		initStandarddrucker();
		// Wenn der Report sofort erstellt und angezeigt werden soll, dann mach
		// das halt
		if (panelReportIf.getBErstelleReportSofort()) {
			preview();
		}

		// Erweiterte Druckereinstellungen: Dieses Feature ist zzt nicht
		// sichtbar
		wbuErweitert.setVisible(false);
		// eventYouAreSelected(false) ;
		updateLpTitle();

		setzeEmailRechnungsempfang();

		if (panelReportIf instanceof PanelReportNotifyIntialize) {
			((PanelReportNotifyIntialize) panelReportIf).setInitialized();
		}

	}

	protected void setzeEmailRechnungsempfang() throws ExceptionLP, Throwable {
		// PJ19901
		if (this.jpaPanelReportIf != null && this.jpaPanelReportIf instanceof ReportBeleg) {
			boolean bRechnungsempfaenger = false;

			if (getInternalFrame() instanceof InternalFrameRechnung) {

				bRechnungsempfaenger = (Boolean) DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_RECHNUNG,
								ParameterFac.PARAMETER_RECHNUNGSEMPFAENGER_NUR_AUS_RECHNUNGS_EMAIL)
						.getCWertAsObject();
			}

			String empfaengerEmailAdresse = ((ReportBeleg) this.jpaPanelReportIf).getEmpfaengerEMailAdresse();
			// PJ20268
			if (empfaengerEmailAdresse != null || bRechnungsempfaenger == true) {
				PanelVersandEmail panelVersandEmail = emailPanelAnzeigen();
				panelVersandEmail.setEmpfaenger(empfaengerEmailAdresse);
			}
		}
	}

	protected PanelReportKriterienOptions getOptions() {
		return options;
	}

	private void jbInitPanel() throws Throwable {

//		String recht = getRechtBeleg();
//
//		// Button Refresh
//		createAndSaveButton("/com/lp/client/res/refresh.png",
//				LPMain.getTextRespectUISPr("lp.drucken.reportaktualisieren"), ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW,
//				KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), null);
//		
//		if (!getOptions().isNurVorschau()) {
//			// Button Druckvorschau
//			// createAndSaveButton("/com/lp/client/res/printer_view.png",
//			// LPMain.getTextRespectUISPr("lp.drucken.vorschau"),
//			// ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN,
//			// KeyStroke.getKeyStroke('R',
//			// java.awt.event.InputEvent.CTRL_MASK), null);
//			createAndSaveButton("/com/lp/client/res/printer_view.png",
//					LPMain.getTextRespectUISPr("lp.drucken.vorschau"), ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN,
//					KeyStroke.getKeyStroke('R', java.awt.event.InputEvent.CTRL_MASK), recht);
//			// Button Drucken
//			createAndSaveButton("/com/lp/client/res/printer.png", LPMain.getTextRespectUISPr("lp.drucken.drucken"),
//					ACTION_SPECIAL_DRUCKEN, KeyStroke.getKeyStroke('P', java.awt.event.InputEvent.CTRL_MASK), null);
//			// Button CSV-Export
//			createAndSaveButton("/com/lp/client/res/document_out.png",
//					LPMain.getTextRespectUISPr("lp.report.csvexport"), ACTION_SPECIAL_CSV, null, null);
//
//			// Button Zwischenablage
//			createAndSaveButton("/com/lp/client/res/copy.png",
//					LPMain.getTextRespectUISPr("lp.inzwischenablagekopieren"), ACTION_COPY_TO_CLIPBOARD, null, null);
//
//			// Buttons fuer Email und Fax, falls erwuenscht
//			if (getOptions().isMitEmailFax()) {
//				if (LPMain.getInstance().getDesktop()
//						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAILVERSAND)) {
//					createAndSaveButton("/com/lp/client/res/mail.png",
//							LPMain.getTextRespectUISPr("lp.drucken.alsemailversenden"), ACTION_SPECIAL_EMAIL,
//							KeyStroke.getKeyStroke('E', java.awt.event.InputEvent.CTRL_MASK),
//							RechteFac.RECHT_LP_DARF_EMAIL_SENDEN);
//				}
//				if (LPMain.getInstance().getDesktop()
//						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_FAXVERSAND)) {
//					createAndSaveButton("/com/lp/client/res/fax.png",
//							LPMain.getTextRespectUISPr("lp.drucken.alsfaxversenden"), ACTION_SPECIAL_FAX,
//							KeyStroke.getKeyStroke('F', java.awt.event.InputEvent.CTRL_MASK), null);
//				}
//			}
//		}
//		// Button Speichern
//		createAndSaveButton("/com/lp/client/res/disk_blue.png", LPMain.getTextRespectUISPr("lp.drucken.speichern"),
//				ACTION_SPECIAL_SAVE, KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK), null);
//		// Das Action-Array fuer das ToolsPanel erstellen
//		String[] aWhichButtonIUse;
//
//		if (getOptions().isNurVorschau()) {
//			aWhichButtonIUse = new String[] { ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW };
//		}
//
//		else {
//			if (getOptions().isMitEmailFax()) {
//				aWhichButtonIUse = new String[] { ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW,
//						ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN, ACTION_SPECIAL_DRUCKEN, ACTION_SPECIAL_EMAIL,
//						ACTION_SPECIAL_FAX, ACTION_SPECIAL_SAVE, ACTION_SPECIAL_CSV, ACTION_COPY_TO_CLIPBOARD };
//			} else {
//				aWhichButtonIUse = new String[] { ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW,
//						ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN, ACTION_SPECIAL_DRUCKEN, ACTION_SPECIAL_SAVE,
//						ACTION_SPECIAL_CSV, ACTION_COPY_TO_CLIPBOARD };
//			}
//		}
//
//		enableToolsPanelButtons(aWhichButtonIUse);

		ReportToolsPanelButtonCreator buttonCreator = new ReportToolsPanelButtonCreator();
		enableToolsPanelButtons(buttonCreator.getToolsPanelButtons());

		// Splitpane ausrichten
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);

		jSplitPane1.setAutoscrolls(true);

		jpaWorkingOn.setLayout(gridBagLayout1);

		// Das Druckerpanel
		panelStandardDrucker = new PanelStandardDrucker();
		wbuErweitert = new WrapperButton();
		wbuErweitert.setHeavyOperation(true);

		wbuErweitert.setActionCommand(ACTION_SPECIAL_ERWEITERT);

		wbuErweitert.addActionListener(this);

		wbuErweitert.setText(LPMain.getTextRespectUISPr("button.erweitert"));

		wbuErweitert.setToolTipText(LPMain.getTextRespectUISPr("button.erweitert.tooltip"));

		wbuErweitert.setMinimumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));

		wbuErweitert.setPreferredSize(new Dimension(120, Defaults.getInstance().getControlHeight()));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(jSplitPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if (jpaPanelReportIf instanceof JPanel) {
			jpaTop = new JPanel(gridBagLayoutTop);
			jpaTop.add(panelStandardDrucker, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jpaTop.add(wbuErweitert, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaTop.add((JPanel) jpaPanelReportIf, new GridBagConstraints(0, iZeile, 2, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			iZeile++;
			jSplitPane1.add(jpaTop, JSplitPane.TOP);
			jSplitPane1.add(new JPanel(), JSplitPane.BOTTOM);
			ActionListener buttonListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateRefreshButton();
				}
			};

			for (AbstractButton btn : getAllButtons((JPanel) jpaPanelReportIf)) {
				btn.addActionListener(buttonListener);
			}
		} else {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		FocusManager.getCurrentManager().addVetoableChangeListener(this);
	}

	public Integer getReportVariante() {
		return getPanelStandardDrucker().getVariante();
	}

	private List<AbstractButton> getAllButtons(JPanel panel) {
		ArrayList<AbstractButton> btns = new ArrayList<AbstractButton>();
		for (Component c : panel.getComponents()) {
			if (c instanceof JPanel) {
				btns.addAll(getAllButtons((JPanel) c));
			} else if (c instanceof AbstractButton) {
				btns.add((AbstractButton) c);
			}
		}
		return btns;
	}

	@Override
	protected void onEscOrClosePanelDialog() throws Throwable {
		// PJ18547
		if (jpaPanelReportIf instanceof ReportWepEtikett2) {
			ReportWepEtikett2 report = (ReportWepEtikett2) jpaPanelReportIf;
			report.unlock();
		}

		FocusManager.getCurrentManager().removeVetoableChangeListener(this);
		super.onEscOrClosePanelDialog();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		// if (e.getActionCommand().equals(PanelBasis.ESC)
		// || e.getActionCommand()
		// .equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
		//
		// // PJ18547
		// if (jpaPanelReportIf instanceof ReportWepEtikett2) {
		// ReportWepEtikett2 report = (ReportWepEtikett2) jpaPanelReportIf;
		// report.unlock();
		//
		// }
		//
		// FocusManager.getCurrentManager().removeVetoableChangeListener(this);
		// }
		super.eventActionSpecial(e);
		// Refresh ... erzeugt den Report neu und zeigt ihn an
		if (e.getActionCommand().equals(ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW)) {
			preview();
			// initStandarddrucker();
		}
		// grosser Vorschaudialog
		else if (e.getActionCommand().equals(ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN)) {
			// print erzeugen, falls das noch nicht passiert ist.
			if (panelReport == null || panelReport.getPrint() == null) {
				preview();
			}
			// der im unteren panel angezeigte Print wird in die Vorschau
			// uebernommen
			if (panelReport != null && panelReport.getPrint() != null
					&& panelReport.getPrint().getPrint().getPages().size() > 0) {
				// PJ19712
				boolean bDruckvorschauAktiviert = (Boolean) DelegateFactory
						.getInstance().getParameterDelegate().getMandantparameter(LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_GROSSE_DRUCKVORSCHAU_AKTIVIERT)
						.getCWertAsObject();

				if (bDruckvorschauAktiviert) {
					aktiviereBeleg();
				}
				// den Timer im Hintergrund abdrehen
				getInternalFrame().getFrameProgress().pause();
				JasperPrint mergedPrint = mergeWithPrintTypeReport(panelReport.getPrint().getPrint(),
						JasperPrintLP.DRUCKTYP_FULLSCREEN);
				// und jetzt den Dialog zeigen
				JDialog dialog = new JDialogDruckVorschau(LPMain.getInstance().getDesktop(),
						LPMain.getTextRespectUISPr("lp.drucken.vorschau") + ": " + this.getAdd2Title(), mergedPrint);
				dialog.setModal(true);
				if (bDruckvorschauAktiviert) {
					JasperPrint previewPrint = panelReport.getPrint().getPrint();
					panelReport.getPrint().setPrintLP(mergedPrint);
					archiviereBeleg(panelReport.getPrint());
					panelReport.getPrint().setPrintLP(previewPrint);
					// TODO: soll hier nicht besser Drucktyp print verwendet
					// werden
					setzeVersandstatus(JasperPrintLP.DRUCKTYP_PREVIEW);
				}
			}
		}
		// Email
		else if (e.getActionCommand().equals(ACTION_SPECIAL_EMAIL)) {
			emailPanelAnzeigen();
		}

		// Fax
		else if (e.getActionCommand().equals(ACTION_SPECIAL_FAX)) {
			// Fax-Panel anzeigen
			if (!getPanelVersandFax(true).isVisible()) {
				getPanelVersandFax(true).setVisible(true);
			}
			// Wenn es das Email-Panel schon gibt, muss ich das wegschalten
			if (getPanelVersandEmail(false) != null && getPanelVersandEmail(true).isVisible()) {
				getPanelVersandEmail(true).setVisible(false);
			}
			// SplitPane anpassen (moeglichst platzsparend)
			jSplitPane1.setDividerLocation(jpaTop.getMinimumSize().height);
		}
		// Versandauftrag senden
		else if (e.getActionCommand().equals(PanelVersand.ACTION_SPECIAL_SENDEN)) {
			doActionSenden();
			/*
			 * Integer iKopien = null; if (jpaPanelReportIf instanceof ReportBeleg) {
			 * iKopien = ((ReportBeleg) jpaPanelReportIf).getKopien(); }
			 * 
			 * aktiviereBeleg(); String sDrucktyp = null; // Fax ... if
			 * (getPanelVersandFax(true).isVisible()) {
			 * createVersandauftrag(getPanelVersandFax(true) .getVersandauftragDto(),
			 * JasperPrintLP.DRUCKTYP_FAX); sDrucktyp = JasperPrintLP.DRUCKTYP_FAX; } // ...
			 * oder Email else if (getPanelVersandEmail(true).isVisible()) {
			 * createVersandauftrag(getPanelVersandEmail(true) .getVersandauftragDto(),
			 * JasperPrintLP.DRUCKTYP_MAIL); sDrucktyp = JasperPrintLP.DRUCKTYP_MAIL; }
			 * 
			 * if (jpaPanelReportIf instanceof ReportBeleg) { ((ReportBeleg)
			 * jpaPanelReportIf).setKopien(iKopien); }
			 * 
			 * // PJ19901 lt. WH werden ab sofort beim Email-Versand die Kopien am //
			 * Drucker gedruckt print(true);
			 * 
			 * archiviereBeleg(); setzeVersandstatus(sDrucktyp);
			 */
		} else if (PanelVersandEmail.ACTION_SPECIAL_SENDEN_OHNE_KOPIEN.equals(e.getActionCommand())) {
//			Integer savedKopien = null;
//			ReportBeleg reportBeleg = null;
//			if (jpaPanelReportIf instanceof ReportBeleg) {
//				reportBeleg = (ReportBeleg) jpaPanelReportIf;
//				savedKopien = reportBeleg.getKopien();
//				reportBeleg.setKopien(null);
//			}
//
//			doActionSenden();
//
//			if (reportBeleg != null) {
//				reportBeleg.setKopien(savedKopien);
//			}
			doActionSendenOhneKopien();
		} else if (PanelVersandEmail.ACTION_SPECIAL_SENDEN_ZUGFERD.equals(e.getActionCommand())) {
			doActionSendenAlsZugferd();
		}
		// Hinzufugen von Anhaengen an eine Mail.
		else if (e.getActionCommand().equals(PanelVersandEmail.ACTION_SPECIAL_ATTACHMENT)) {
			doActionAttachment();

		} else if (e.getActionCommand().equals(PanelVersandEmail.ACTION_SPECIAL_REMOVE_ATTACHMENT)) {
			doActionRemoveAttachment();

		} else if (ACTION_SPECIAL_SAVE.equals(e.getActionCommand())) {
			if (!shouldDoActionSave())
				return;

			actionSaveJasperPrint(e);
			// actionSaveJasperPrintOld(e);
		} else if (ACTION_SPECIAL_CSV.equals(e.getActionCommand())) {
			actionCsv();
		} else if (ACTION_COPY_TO_CLIPBOARD.equals(e.getActionCommand())) {
			// Den Report erstellen, falls dies noch nicht geschehen ist.
			if (panelReport == null) {
				preview();
			}
			JasperPrintLP print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(true, JasperPrintLP.DRUCKTYP_CSV);

			Object[][] csvDaten = print.getDatenMitUeberschrift();
			StringBuffer excelStr = new StringBuffer();

			if (csvDaten != null && csvDaten.length > 1) {

				// Daten
				for (int i = 0; i < csvDaten.length; i++) {
					Object[] zeile = csvDaten[i];

					if (zeile != null) {
						for (int j = 0; j < zeile.length; j++) {

							if (zeile[j] instanceof Number) {
								try {
									excelStr.append(
											Helper.formatZahl((Number) zeile[j], 6, LPMain.getTheClient().getLocUi()));
								} catch (Throwable e1) {
									//
								}
							} else if (zeile[j] instanceof java.sql.Timestamp) {
								try {
									excelStr.append(Helper.formatTimestamp((java.sql.Timestamp) zeile[j],
											LPMain.getTheClient().getLocUi()));
								} catch (Throwable e1) {
									//
								}
							} else if (zeile[j] instanceof java.sql.Time) {
								try {
									excelStr.append(
											Helper.formatTime(new Timestamp(((java.sql.Time) zeile[j]).getTime()),
													LPMain.getTheClient().getLocUi()));
								} catch (Throwable e1) {
									//
								}
							} else if (zeile[j] instanceof java.util.Date) {
								try {
									excelStr.append(Helper.formatDatum((java.util.Date) zeile[j],
											LPMain.getTheClient().getLocUi()));
								} catch (Throwable e1) {
									//
								}
							} else if (zeile[j] instanceof String) {
								String s = (String) zeile[j];
								excelStr.append("\"" + escape(s.replaceAll("\"", "\"\"")) + "\"");
							} else {
								excelStr.append(escape(zeile[j]));
							}

							if (j < zeile.length - 1) {
								excelStr.append(CELL_BREAK);
							}
						}
					}
					excelStr.append(LINE_BREAK);
				}
			}

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelStr.toString()),
					null);

		}
		// Ausdrucken
		else if (e.getActionCommand().equals(ACTION_SPECIAL_DRUCKEN)) {
			if (!shouldDoActionPrint())
				return;
			doActionPrint(e);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ERWEITERT)) {
			// Den Report erstellen, falls dies noch nicht geschehen ist.
			if (panelReport == null) {
				preview();
			}
			if (panelReport != null) {
				panelReport.doClickOnPrint();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DRUCKER_SPEICHERN)) {
			// Druckereinstellung speichern
			saveStandarddrucker();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DRUCKER_LOESCHEN)) {
			// Druckereinstellung speichern
			removeStandarddrucker();
		} else if (PanelVersandEmail.ACTION_SPECIAL_SENDEN_OPENTRANS.equals(e.getActionCommand())) {
			doActionSendenMitOpenTransXml();
		}
	}

	private void doActionRemoveAttachment() throws Throwable {
		AttachmentDialog attachmentDialog = new AttachmentDialog(panelVersandEmail.getjtfAnhaengeText());
		if (attachmentDialog.remove()) {
			panelVersandEmail.setjtfAnhaengeText(attachmentDialog.getAttachmentPathsAsString());
		}
	}

	private void doActionAttachment() {
		AttachmentDialog attachmentDialog = new AttachmentDialog(panelVersandEmail.getjtfAnhaengeText());
		if (attachmentDialog.chooseFiles(this.getInternalFrame())) {
			panelVersandEmail.setjtfAnhaengeText(attachmentDialog.getAttachmentPathsAsString());
		}
	}

	private void doActionPrint(ActionEvent e) throws Throwable {
		aktiviereBeleg();
		doActionPrintImpl(e);
	}

	private void doActionPrintImpl(ActionEvent e) throws Throwable {
		JasperPrintLP jasperPrintLP = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(false,
				JasperPrintLP.DRUCKTYP_DRUCKER);
		mergeWithPrintTypeReport(jasperPrintLP, JasperPrintLP.DRUCKTYP_DRUCKER);

		OriginalCopiesCtrl ocCtrl = new OriginalCopiesCtrl(jasperPrintLP.getPrint(), iKopien);
		print(jasperPrintLP);
		if (jpaPanelReportIf instanceof IPanelReportAction) {
			((IPanelReportAction) jpaPanelReportIf).eventActionPrint(e);
		}

		ocCtrl.setPagesOriginal(jasperPrintLP);
		archiviereBeleg(jasperPrintLP);
		setzeVersandstatus(JasperPrintLP.DRUCKTYP_DRUCKER);
	}

	private WrapperSaveFileChooser<JasperPrint> buildJasperPrintSaver() throws ExceptionLP, Throwable {
		String reporttoken = jpaPanelReportIf.getReportname();
		ChooserSaveDialogJasper chooserSaveDialog = FileChooserBuilder.createReportSaveDialog(reporttoken, this);
		setFilename(chooserSaveDialog);
		WrapperSaveFileChooser<JasperPrint> saver = chooserSaveDialog.locale(this.getLocale()).addJasperPdfSaver()
				.preselected().addJasperCsvSaver().addJasperSingleSheetXlsSaver().addJasperOdtSaver()
				.addJasperPrintSaver().build();

		return saver;
	}

	private Optional<String> getReportFilename() throws ExceptionLP, Throwable {
		if (!(jpaPanelReportIf instanceof ReportBeleg)) {
			return Optional.empty();
		}
		String belegartCNr = ((ReportBeleg) jpaPanelReportIf).getBelegartCNr();
		Integer belegIId = ((ReportBeleg) jpaPanelReportIf).getIIdBeleg();
		MailtextDto mailtextDto = getMailtextDto();
		if (mailtextDto == null)
			mailtextDto = getDefaultMailtextDto(jpaPanelReportIf);

		String filename = DelegateFactory.getInstance().getVersandDelegate()
				.getUebersteuertenDateinamenForBeleg(mailtextDto, belegartCNr, belegIId);

		if (Helper.isStringEmpty(filename)) {
			return Optional.empty();
		}

		filename = filename.replace("/", "_");
		Integer idxExtension = filename.lastIndexOf(".");
		if (Integer.signum(idxExtension) > 0)
			filename = filename.substring(0, idxExtension);
		return Optional.of(filename);
	}

	private void setFilename(ChooserSaveDialogJasper chooserSaveDialog) throws ExceptionLP, Throwable {
		getReportFilename().ifPresent(chooserSaveDialog::filename);
	}

	private void actionSaveJasperPrint(ActionEvent e) throws Throwable {
		JasperPrintLP print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(true, JasperPrintLP.DRUCKTYP_SAVE);
		mergeWithPrintTypeReport(print, JasperPrintLP.DRUCKTYP_SAVE);

		try {
			if (panelReport == null) {
				preview();
				if (panelReport == null)
					return;
			}

			aktiviereBeleg();
			WrapperSaveFileChooser<JasperPrint> fileSaver = buildJasperPrintSaver();
			fileSaver.save(print.getPrint());
		} catch (JRException ex) {
			myLogger.error("Fehler beim Speichern des Reports '" + jpaPanelReportIf.getReportname() + "'", ex);
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern");
		}

		if (jpaPanelReportIf instanceof IPanelReportAction) {
			((IPanelReportAction) jpaPanelReportIf).eventActionSave(e);
		}
		archiviereBeleg(print);
		setzeVersandstatus(JasperPrintLP.DRUCKTYP_SAVE);
	}

	private void doActionSenden() throws Throwable {
		aktiviereBeleg();

		// Fax ...
		if (getPanelVersandFax(true).isVisible()) {
			doActionSendenImpl(getPanelVersandFax(true).getVersandauftragDto(), JasperPrintLP.DRUCKTYP_FAX);
		}
		// ... oder Email
		else if (getPanelVersandEmail(true).isVisible()) {

			if (getPanelVersandEmail(true).getWtfAbsender() != null
					&& getPanelVersandEmail(true).getWtfAbsender().getText() == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.error.absender.email.leer"));
			} else {
				doActionSendenImpl(getPanelVersandEmail(true).getVersandauftragDto(), JasperPrintLP.DRUCKTYP_MAIL);
			}
		}
	}

	private void doActionSendenOhneKopien() throws Throwable {
		aktiviereBeleg();
		VersandauftragDto versandauftragDto = null;
		String druckType = null;
		// Fax ...
		if (getPanelVersandFax(true).isVisible()) {
			versandauftragDto = getPanelVersandFax(true).getVersandauftragDto();
			druckType = JasperPrintLP.DRUCKTYP_FAX;
		}
		// ... oder Email
		else if (getPanelVersandEmail(true).isVisible()) {
			versandauftragDto = getPanelVersandEmail(true).getVersandauftragDto();
			druckType = JasperPrintLP.DRUCKTYP_MAIL;
		}

		JasperPrintLP jasperPrintLP = getPrintForVersandauftragMitKopien(druckType);
		OriginalCopiesCtrl ocCtrl = new OriginalCopiesCtrl(jasperPrintLP.getPrint(), iKopien);
		ocCtrl.setPagesOriginal(jasperPrintLP);
		setupCreateVersandauftrag(versandauftragDto, jasperPrintLP.getPrint());

		archiviereBeleg(jasperPrintLP);
		setzeVersandstatus(druckType);
	}

	private void doActionSendenImpl(VersandauftragDto versandauftragDto, String sDrucktyp) throws Throwable {
		JasperPrintLP jasperPrintLP = null;
		if (isLogoImmerDrucken()) {
			jasperPrintLP = getPrintForVersandauftragMitKopien(sDrucktyp);
			OriginalCopiesCtrl ocCtrl = new OriginalCopiesCtrl(jasperPrintLP.getPrint(), iKopien);
			ocCtrl.setPagesOriginal(jasperPrintLP);
			setupCreateVersandauftrag(versandauftragDto, jasperPrintLP.getPrint());

			// PJ19901 lt. WH werden ab sofort beim Email-Versand die Kopien am
			// Drucker gedruckt
			ocCtrl.setPagesAll(jasperPrintLP);
			print(jasperPrintLP, true);
		} else {
			jasperPrintLP = getPrintForVersandauftragOhneKopien(sDrucktyp);
			setupCreateVersandauftrag(versandauftragDto, jasperPrintLP.getPrint());
			if (iKopien > 0) {
				print(true, sDrucktyp);
			}
		}

		archiviereBeleg(jasperPrintLP);
		setzeVersandstatus(sDrucktyp);
	}

	private void doActionSendenAlsZugferd() throws Throwable {
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZUGFERD)
				|| !(jpaPanelReportIf instanceof IZugferdBeleg)) {
			return;
		}

		IZugferdBeleg zugferdBeleg = (IZugferdBeleg) jpaPanelReportIf;
		ZugferdResult result = zugferdBeleg.createZugferdResult();

		aktiviereBeleg();
		VersandauftragDto versandauftragDto = getPanelVersandEmail(true).getVersandauftragDto();
		setupVersandauftragDefaults(versandauftragDto);

		if (result.getPdfData() == null) {
			showDialogKeineDatenZuDrucken();
			return;
		}

		versandauftragDto.setOInhalt(result.getPdfData());
		versandauftragDto.setCDateiname(result.getPdfName());
		createVersandauftragImpl(versandauftragDto);

		// PJ19901 lt. WH werden ab sofort beim Email-Versand die Kopien am
		// Drucker gedruckt
		print(true, JasperPrintLP.DRUCKTYP_MAIL);

		DelegateFactory.getInstance().getRechnungDelegate().archiviereZugferdResult(result);
		archiviereBeleg(result.getJasperPrintLP());
		setzeVersandstatus(JasperPrintLP.DRUCKTYP_MAIL);
	}

	private void doActionSendenMitOpenTransXml() throws Throwable {
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)
				|| !(jpaPanelReportIf instanceof IOpenTransBeleg)) {
			return;
		}

		IOpenTransBeleg otBeleg = (IOpenTransBeleg) jpaPanelReportIf;
		OpenTransXmlReportResult result = otBeleg.createOpenTransResult();

		aktiviereBeleg();

		if (result.getPrints() == null || result.getPrints().length < 1 || result.getXmlOtOrder() == null) {
			showDialogKeineDatenZuDrucken();
			return;
		}

		JasperPrintLP print = result.getPrints()[0];
		for (int i = 1; i < result.getPrints().length; i++) {
			print = Helper.addReport2Report(print, result.getPrints()[i].getPrint());
		}

		if (print.getPrint().getPages().size() < 1) {
			showDialogKeineDatenZuDrucken();
			return;
		}

		if (jpaPanelReportIf instanceof ReportBeleg) {
			ReportBeleg reportBeleg = (ReportBeleg) jpaPanelReportIf;
			iKopien = reportBeleg.getKopien() != null ? reportBeleg.getKopien() : 0;
		}
		mergeWithPrintTypeReport(print, JasperPrintLP.DRUCKTYP_MAIL);
		OriginalCopiesCtrl ocCtrl = new OriginalCopiesCtrl(print.getPrint(), iKopien);
		ocCtrl.setPagesOriginal(print);

		VersandauftragDto versandauftragDto = getPanelVersandEmail(true).getVersandauftragDto();
		setupVersandauftragDefaults(versandauftragDto);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		HVPDFExporter exporter = new HVPDFExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print.getPrint());
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		versandauftragDto.setOInhalt(baos.toByteArray());

		versandauftragDto = createVersandauftragImpl(versandauftragDto);
		VersandanhangDto xmlAnhang = new VersandanhangDto();
		xmlAnhang.setVersandauftragIId(versandauftragDto.getIId());
		xmlAnhang.setCDateiname(result.getFilename());
		xmlAnhang.setOInhalt(result.getXmlOtOrder().getBytes("UTF-8"));
		DelegateFactory.getInstance().getVersandDelegate().createVersandanhang(xmlAnhang);

		ocCtrl.setPagesAll(print);
		print(print, true);

		DelegateFactory.getInstance().getBestellungDelegate().archiviereOpenTransResult(result);
		archiviereBeleg(print);
		setzeVersandstatus(JasperPrintLP.DRUCKTYP_MAIL);
	}

	private PanelVersandEmail emailPanelAnzeigen() throws Throwable {
		// Email - Betreff setzen
		if (jpaPanelReportIf.getMailtextDto() != null && jpaPanelReportIf.getMailtextDto().getMailBetreff() != null) {
			getPanelVersandEmail(true).setBetreff(jpaPanelReportIf.getMailtextDto().getMailBetreff());

		}
		// Email-Panel anzeigen
		if (!getPanelVersandEmail(true).isVisible()) {
			getPanelVersandEmail(true).setVisible(true);
		}
		// Wenn es das Fax-Panel schon gibt, muss ich das wegschalten
		if (getPanelVersandFax(false) != null && getPanelVersandFax(true).isVisible()) {
			getPanelVersandFax(true).setVisible(false);
		}
		// SplitPane anpassen (moeglichst platzsparend)
		jSplitPane1.setDividerLocation(jpaTop.getMinimumSize().height);
		return getPanelVersandEmail(true);
	}

	/**
	 * @throws Throwable
	 */
	private void actionCsv() throws Throwable {
		// Den Report erstellen, falls dies noch nicht geschehen ist.
		if (panelReport == null) {
			preview();
		}

		JasperPrintLP print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(true, JasperPrintLP.DRUCKTYP_CSV);
//		List<JRPrintPage> list = print.getPrint().getPages();

		Object[][] csvDaten = print.getDatenMitUeberschrift();

		// Derzeit einfach auf String umwandeln

		if (!(csvDaten != null && csvDaten.length > 1))
			return;

		Object[][] csv = new Object[csvDaten.length][csvDaten[0].length];

		for (int i = 0; i < csvDaten.length; i++) {
			for (int z = 0; z < csvDaten[0].length; z++) {
				try {
					if (csvDaten[i][z] != null) {

						if (csvDaten[i][z] instanceof LPDatenSubreport) {
							csv[i][z] = "SUBREPORT";
						} else {
							csv[i][z] = csvDaten[i][z];
						}

					}
				} catch (Exception e1) {
					int u = 0;
				}
			}
			iZeile++;
		}

		DialogFactory.showReportCSVExportDialog(csv, LPMain.getTheClient().getLocUi());
	}

	private void aktiviereBeleg() throws Throwable {
		if (jpaPanelReportIf instanceof IAktiviereBelegReport) {
			IAktiviereBelegReport rep = (IAktiviereBelegReport) jpaPanelReportIf;
			rep.aktiviereBeleg(berechnungsZeitpunkt);
			rep.refreshPanelInBackground();
		}
	}

	private void berecheneBeleg() throws Throwable {
		if (jpaPanelReportIf instanceof IAktiviereBelegReport)
			berechnungsZeitpunkt = ((IAktiviereBelegReport) jpaPanelReportIf).berechneBeleg();
	}

	public void druckeArchiviereUndSetzeVersandstatusEinesBelegs(boolean bNurEmail) throws Throwable {
		if (berechnungsZeitpunkt == null)
			berecheneBeleg();
		aktiviereBeleg();

		// PJ19901
		if (jpaPanelReportIf instanceof ReportBeleg) {
			ReportBeleg reportBeleg = (ReportBeleg) jpaPanelReportIf;
			String empfaengerEmailAdresse = reportBeleg.getEmpfaengerEMailAdresse();
			if (empfaengerEmailAdresse != null) {
				PanelVersandEmail panelVersandEmail = emailPanelAnzeigen();
				panelVersandEmail.setEmpfaenger(empfaengerEmailAdresse);

				doActionSendenImpl(panelVersandEmail.getVersandauftragDto(), JasperPrintLP.DRUCKTYP_MAIL);
				return;
			}
		}

		if (bNurEmail == false) {
			doActionPrintImpl(new ActionEvent(this, 0, ACTION_SPECIAL_DRUCKEN));
		}
	}

	public void druckeArchiviereUndSetzeVersandstatusEinesBelegs() throws Throwable {
		druckeArchiviereUndSetzeVersandstatusEinesBelegs(false);
	}

	private String escape(Object cell) {
		if (cell == null || cell.toString() == null) {
			return "";
		} else {
			return cell.toString().replace(LINE_BREAK, " ").replace(CELL_BREAK, " ");
		}

	}

	private String getDateinameFuerAnhang(String belegartCnr, Integer belegIId) throws ExceptionLP, Throwable {
		String sBelegnummer = null;

		if (getPanelVersandEmail(true).getPartnerDtoEmpfaenger() != null
				&& getPanelVersandEmail(true).getPartnerDtoEmpfaenger().getLocaleCNrKommunikation() != null) {
			sBelegnummer = DelegateFactory.getInstance().getVersandDelegate().getDefaultAnhangForBelegEmail(
					getMailtextDto(), belegartCnr, belegIId,
					getPanelVersandEmail(true).getPartnerDtoEmpfaenger().getLocaleCNrKommunikation());
		} else {
			sBelegnummer = DelegateFactory.getInstance().getVersandDelegate()
					.getDefaultAnhangForBelegEmail(getMailtextDto(), belegartCnr, belegIId);
		}

		return sBelegnummer == null ? null : sBelegnummer.replaceAll("\\s+", "").replace("/", "_");
	}

	private void setupVersandauftragDefaults(VersandauftragDto versandauftragDto) throws Throwable {
		String belegartCnr = null;
		Integer belegIId = null;
		if (jpaPanelReportIf instanceof ReportBeleg) {
			belegartCnr = ((ReportBeleg) jpaPanelReportIf).getBelegartCNr();
			belegIId = ((ReportBeleg) jpaPanelReportIf).getIIdBeleg();
			versandauftragDto.setBelegartCNr(belegartCnr);
			versandauftragDto.setIIdBeleg(belegIId);

			if (options.getPartnerDtoEmpfaenger() != null) {
				versandauftragDto.setPartnerIIdEmpfaenger(options.getPartnerDtoEmpfaenger().getIId());
			}
		}
		versandauftragDto.setCDateiname(getDateinameFuerAnhang(belegartCnr, belegIId));
	}

	private VersandauftragDto createVersandauftragImpl(VersandauftragDto versandauftragDto) throws Throwable {
		boolean dokumenteAnhaengen = false;
		PanelVersandEmail panelEmail = getPanelVersandEmail(true);
		boolean emailVersand = panelEmail.isVisible();

		byte[] bAGBs = null;
		if (emailVersand) {
			dokumenteAnhaengen = panelEmail.getWcbDokumenteAnhaengen().isSelected();

			// PJ21318
			if (versandauftragDto.getBelegartCNr() != null && versandauftragDto.getPartnerIIdEmpfaenger() != null) {

				MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate()
						.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());
				if (Helper.short2boolean(mandantDto.getBAgbAnhang())) {

					PartnerDto pDto = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(versandauftragDto.getPartnerIIdEmpfaenger());

					if ((versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)
							&& Helper.short2boolean(mandantDto.getBAgbAuftrag()))
							|| (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_ANGEBOT)
									&& Helper.short2boolean(mandantDto.getBAgbAngebot()))
							|| (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_ANFRAGE)
									&& Helper.short2boolean(mandantDto.getBAgbAnfrage()))
							|| (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)
									&& Helper.short2boolean(mandantDto.getBAgbLieferschein()))
							|| (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_RECHNUNG)
									&& Helper.short2boolean(mandantDto.getBAgbRechnung()))
							|| (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_BESTELLUNG)
									&& Helper.short2boolean(mandantDto.getBAgbBestellung()))) {

						bAGBs = DelegateFactory.getInstance().getMandantDelegate()
								.getAGBs_PDF(Helper.string2Locale(pDto.getLocaleCNrKommunikation()));

					}

				}
			}

		}

		String[] attachments = getAttachments(panelEmail);
		int attachmentSize = getAttachmentSizeInKB(attachments);

		if (bAGBs != null) {
			attachmentSize += bAGBs.length / 1024;
		}

		int maxSize = getMaxAttachmentSize();
		if (attachmentSize > maxSize) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.drucken.anhangzugros") + " (" + maxSize + "KB)");
			// Dient nur dazu, sauber aus dem ganzen Vorgang rauszukommen
			throw new ExceptionLP(EJBExceptionLP.FEHLER_ATTACHMENTS_ZU_GROSS,
					"Attachments in KB(" + attachmentSize + ") > max (" + maxSize + ")", new Exception(),
					new Integer(attachmentSize), new Integer(maxSize));
		}

		List<VersandanhangDto> alAnhaenge = new ArrayList<VersandanhangDto>();
		for (String attachment : attachments) {
			File f = new File(attachment);
			VersandanhangDto versandanhangDto = new VersandanhangDto();
			versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
			versandanhangDto.setCDateiname(f.getName());
			FileInputStream fiStream = new FileInputStream(f);
			byte[] fileData = new byte[(int) f.length()];
			fiStream.read(fileData);
			fiStream.close();
			versandanhangDto.setOInhalt(fileData);
			alAnhaenge.add(versandanhangDto);
		}

		if (bAGBs != null) {
			VersandanhangDto versandanhangDto = new VersandanhangDto();
			versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
			versandanhangDto.setCDateiname("AGB.pdf");
			versandanhangDto.setOInhalt(bAGBs);
			alAnhaenge.add(versandanhangDto);
		}

		VersandauftragDto versandDto = DelegateFactory.getInstance().getVersandDelegate()
				.createVersandauftrag(versandauftragDto, alAnhaenge, dokumenteAnhaengen);
		getInternalFrame().closePanelDialog();

		return versandDto;
	}

	private String[] getAttachments(PanelVersandEmail panelEmail) {
		String allNames = panelEmail.getjtfAnhaengeText();
		if (allNames == null || allNames.length() == 0) {
			return new String[] {};
		}

		return allNames.split(";");
	}

	private int getAttachmentSizeInKB(String[] attachments) {
		int totalSize = 0;
		for (String attachment : attachments) {
			File f = new File(attachment);
			totalSize += f.length() / 1024;
		}
		return totalSize;
	}

	private int getMaxAttachmentSize() throws Throwable {
		int maxsize = Integer.parseInt(DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), "ALLGEMEIN", ParameterFac.PARAMETER_SIZE_E_MAILANHANG).getCWert());
		return maxsize;
	}

	private boolean isAttachmentSizeOk(String[] attachments) throws Throwable {
		int totalSize = getAttachmentSizeInKB(attachments);

		// Keine Anhaenge / keine Groesse, dann Server-Call einsparen
		if (totalSize == 0)
			return true;

		int maxsize = getMaxAttachmentSize();
		return maxsize >= totalSize;
	}

	private void setupCreateVersandauftrag(VersandauftragDto versandauftragDto, JasperPrint print) throws Throwable {
		setupVersandauftragDefaults(versandauftragDto);

		if (versandauftragDto.getCEmpfaenger() == null
				|| (getPanelVersandEmail(true).isVisible() && versandauftragDto.getCAbsenderadresse() == null)) {
			showDialogPflichtfelderAusfuellen();
			return;
		}

		if (print.getPages().size() > 0) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			HVPDFExporter exporter = new HVPDFExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

			exporter.exportReport();

			versandauftragDto.setOInhalt(baos.toByteArray());

			createVersandauftragImpl(versandauftragDto);
		} else {
			showDialogKeineDatenZuDrucken();
		}
	}

	private JasperPrintLP getPrintForVersandauftragOhneKopien(String sDruckType) throws Throwable {
		ReportBeleg reportBeleg = null;
		if (jpaPanelReportIf instanceof ReportBeleg) {
			reportBeleg = (ReportBeleg) jpaPanelReportIf;
			iKopien = reportBeleg.getKopien() != null ? reportBeleg.getKopien() : 0;
		}

		// Merken wie viele Kopien eingestellt waren
		int iKopienAktuell = iKopien;
		if (reportBeleg != null) {
			reportBeleg.setKopien(null);
		}

		JasperPrintLP print = getPrintForVersandauftragMitKopien(sDruckType);
		iKopien = iKopienAktuell;

		if (reportBeleg != null) {
			reportBeleg.setKopien(iKopienAktuell);
		}
		return print;
	}

	private JasperPrintLP getPrintForVersandauftragMitKopien(String sDruckType) throws Throwable {
		if (jpaPanelReportIf instanceof ReportBeleg) {
			ReportBeleg reportBeleg = (ReportBeleg) jpaPanelReportIf;
			iKopien = reportBeleg.getKopien() != null ? reportBeleg.getKopien() : 0;
			reportBeleg.setBPrintLogo(true);
		}

		JasperPrintLP print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(true, sDruckType);
		mergeWithPrintTypeReport(print, sDruckType);

		return print;
	}

	/**
	 * createVersandauftrag
	 * 
	 * @param versandauftragDto VersandauftragDto
	 * @throws Throwable
	 */
	private void createVersandauftrag0(VersandauftragDto versandauftragDto, String sDruckType) throws Throwable {

		int iKopienAktuell = 0;
		String belegartCnr = null;
		Integer belegIId = null;
		if (jpaPanelReportIf instanceof ReportBeleg) {

			// if (((ReportBeleg) jpaPanelReportIf).wnfKopien.getInteger() ==
			// null) {
			if (((ReportBeleg) jpaPanelReportIf).getKopien() == null) {
				iKopien = 0;
			} else {
				// iKopien = ((ReportBeleg)
				// jpaPanelReportIf).wnfKopien.getInteger();
				iKopien = ((ReportBeleg) jpaPanelReportIf).getKopien();
			}
			belegartCnr = ((ReportBeleg) jpaPanelReportIf).getBelegartCNr();
			belegIId = ((ReportBeleg) jpaPanelReportIf).getIIdBeleg();
			versandauftragDto.setBelegartCNr(belegartCnr);
			versandauftragDto.setIIdBeleg(belegIId);
		}
		versandauftragDto.setCDateiname(getDateinameFuerAnhang(belegartCnr, belegIId));

		if (versandauftragDto.getCEmpfaenger() == null
				|| (getPanelVersandEmail(true).isVisible() && versandauftragDto.getCAbsenderadresse() == null)) {
			showDialogPflichtfelderAusfuellen();
		} else {
			boolean sizeIsOk = true;
			JasperPrint print = null;
			if (iKopien > 0) {
				// Merken wie viele Kopien eingestellt waren
				iKopienAktuell = iKopien;
				// ((ReportBeleg) jpaPanelReportIf).wnfKopien.setInteger(0);
				((ReportBeleg) jpaPanelReportIf).setKopien(null);
				iKopien = 0;
				((ReportBeleg) jpaPanelReportIf).setBPrintLogo(true);
				print = createPrint(sDruckType).getPrint();
			} else {
				print = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(true, sDruckType).getPrint();
			}
			// Kopieanzahl zuruecksetzen
			iKopien = iKopienAktuell;
			if (print.getPages().size() > 0) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				HVPDFExporter exporter = new HVPDFExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

				exporter.exportReport();

				versandauftragDto.setOInhalt(baos.toByteArray());

				boolean bDokumenteAnhaengen = false;

				if (getPanelVersandEmail(true).isVisible()) {

					if (getPanelVersandEmail(true).getWcbDokumenteAnhaengen().isSelected()) {
						bDokumenteAnhaengen = true;
					}
				}

				versandauftragDto = DelegateFactory.getInstance().getVersandDelegate()
						.updateVersandauftrag(versandauftragDto, bDokumenteAnhaengen);
				if (getPanelVersandEmail(true).isVisible()) {
					// Anhaenge verarbeiten
					String[] sAttachments = null;
					if (!panelVersandEmail.getjtfAnhaengeText().equals("")) {
						sAttachments = panelVersandEmail.getjtfAnhaengeText().split(";");
					}
					if (sAttachments != null) {
						// check wie grosz die anhaenge sind:
						int size = 0;
						for (int y = 0; y < sAttachments.length; y++) {
							File file = new File(sAttachments[y]);
							size += file.length() / 1024; // umrechnung in KB
						}
						// holen der maximalgroesze
						int maxsize = Integer.parseInt(DelegateFactory.getInstance().getParameterDelegate()
								.getMandantparameter(LPMain.getTheClient().getMandant(), "ALLGEMEIN",
										ParameterFac.PARAMETER_SIZE_E_MAILANHANG)
								.getCWert());

						if (size > maxsize) {
							sizeIsOk = false;
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.drucken.anhangzugros") + " (" + maxsize + "KB)");
							panelVersandEmail.setjtfAnhaengeText("");
						}
						if (sizeIsOk) {

							ArrayList<VersandanhangDto> alAnhaenge = new ArrayList<VersandanhangDto>();

							for (int i = 0; i < sAttachments.length; i++) {
								File f = new File(sAttachments[i]);
								VersandanhangDto versandanhangDto = new VersandanhangDto();
								versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
								versandanhangDto.setCDateiname(f.getName());
								FileInputStream fiStream = new FileInputStream(f);
								byte[] fileData = new byte[(int) f.length()];
								fiStream.read(fileData);
								fiStream.close();
								versandanhangDto.setOInhalt(fileData);

								alAnhaenge.add(versandanhangDto);

							}

							DelegateFactory.getInstance().getVersandDelegate().createVersandanhaenge(alAnhaenge);
						}
					}

				}
				if (sizeIsOk) {
					getInternalFrame().closePanelDialog();
				}
			} else {
				showDialogKeineDatenZuDrucken();
			}
		}
	}

	private void preview() throws Throwable {
		myLogger.debug("Erzeuge Report: " + jpaPanelReportIf.getModul() + "|" + jpaPanelReportIf.getReportname());
		long lTime = System.currentTimeMillis();
		boolean bAllowed = true;
		// Pflichtfelder pruefen
		if (jpaPanelReportIf instanceof PanelBasis) {
			if (!((PanelBasis) jpaPanelReportIf).allMandatoryFieldsSetDlg()) {
				bAllowed = false;
			}
		}
		if (bAllowed) {
			// Print erzeugen
			berecheneBeleg();
			JasperPrintLP print = null;
			try {
				print = createPrint(JasperPrintLP.DRUCKTYP_PREVIEW);
				if (print == null) {
					// nicht jede Option gibt auch wirklich einen print retour
					myLogger.debug("Report=null nach: " + (System.currentTimeMillis() - lTime) + " ms.");
					return;
				}
				// Erstellungszeit loggen
				myLogger.debug("Report erzeugt nach: " + (System.currentTimeMillis() - lTime) + " ms.");
				saveValuesToCache();
				setRefreshNecessary(false);
			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT) {
					if (ex.getCause() != null) {
						if (ex.getCause().getCause() != null) {
							if (ex.getCause().getCause() instanceof EJBExceptionLP) {
								EJBExceptionLP ejbex = (EJBExceptionLP) ex.getCause().getCause();
								if (ejbex.getCode() == EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT) {
									LPMain.getInstance();
									String sMsg = LPMain.getTextRespectUISPr("rech.warning.hvwertreportwert");
									List<?> al = ejbex.getAlInfoForTheClient();
									if (al != null && al.size() > 0) {
										sMsg += " (" + al.get(0) + ") ";
									}
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), sMsg);
									return;
								}
							} else {
								if (ex.getCause().getCause().getCause() != null) {
									if (ex.getCause().getCause().getCause() instanceof EJBExceptionLP) {
										EJBExceptionLP ejbex = (EJBExceptionLP) ex.getCause().getCause().getCause();
										if (ejbex
												.getCode() == EJBExceptionLP.FEHLER_RECHNUNG_HVWERT_UNGLEICH_REPORTWERT) {
											LPMain.getInstance();
											String sMsg = LPMain.getTextRespectUISPr("rech.warning.hvwertreportwert");
											List<?> al = ejbex.getAlInfoForTheClient();
											if (al != null && al.size() > 0) {
												sMsg += " (" + al.get(0) + ") ";
											}
											DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
													sMsg);
											return;
										}
									}
								}
							}
						}
					}

				}

				boolean bHandled = false;
				// individuelle Fehlerbehandlung in PanelBasis
				if (jpaPanelReportIf instanceof PanelBasis) {
					bHandled = ((PanelBasis) jpaPanelReportIf).handleOwnException(ex);
				}
				if (!bHandled) {
					// ansonsten Fehlerdialog anzeigen
					String msg = LPMain.getInstance().getMsg(ex);
					// CK 03.05.2006:WH moechte keine leeren Fehlermeldungen
					// sehen
					if (msg == null || ex.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT) {
						msg = "Fehlercode " + ex.getICode();
						msg = msg + "\n" + ex.getSMsg();
						new DialogError(LPMain.getInstance().getDesktop(), ex, DialogError.TYPE_ERROR);
					} else {
						new DialogError(LPMain.getInstance().getDesktop(), ex, DialogError.TYPE_INFORMATION);
					}

				}
				// Fehler loggen
				myLogger.info("getModul():                  " + jpaPanelReportIf.getModul());
				myLogger.info("getReportname():             " + jpaPanelReportIf.getReportname());
				myLogger.info(
						"Report-Fehler " + ex.getICode() + " nach: " + (System.currentTimeMillis() - lTime) + " ms.");
				return;
			}
			if (print.getPrint().getPages().size() == 0) {
				showDialogKeineDatenZuDrucken();
				if (panelReport != null) {
					panelReport.clearPrint();
				}
			} else {

				if (panelReport != null) {
					letzterZoomfaktor = panelReport.getJpaViewer().getCmbZoom().getSelectedItem();
				}

				panelReport = new PanelReport(getInternalFrame(), this.getAdd2Title(), print, false);

				panelReport.setReportFilenameSupplier(() -> {
					try {
						return getReportFilename();
					} catch (Throwable e1) {
						return Optional.empty();
					}
				});

				panelReport
						.setPreferredSize(new Dimension(getInternalFrame().getWidth(), getInternalFrame().getHeight()));

				if (letzterZoomfaktor != null && bZoomfaktorGesetzt == true) {
					panelReport.getJpaViewer().getCmbZoom().setSelectedItem(letzterZoomfaktor);
				}

				jSplitPane1.add(panelReport, JSplitPane.BOTTOM);

				if (bZoomfaktorGesetzt == false) {

					if (konfDtos != null) {
						for (int i = 0; i < konfDtos.length; i++) {
							ReportkonfDto dto = konfDtos[i];

							if (dto.getCKomponentenname().equals("ZOOMFAKTOR")) {

								try {
									Float zoom;
									zoom = new Float(dto.getCKey());

									panelReport.getJpaViewer().setZoomRatio(zoom);
									bZoomfaktorGesetzt = true;
									letzterZoomfaktor = zoom;
								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else if (dto.getCKomponentenname()
									.equals(panelReport.getJpaViewer().getBtnActualSize().getName())) {
								setJToggleButton(panelReport.getJpaViewer().getBtnActualSize());
							} else if (dto.getCKomponentenname()
									.equals(panelReport.getJpaViewer().getBtnFitPage().getName())) {
								setJToggleButton(panelReport.getJpaViewer().getBtnFitPage());
							} else if (dto.getCKomponentenname()
									.equals(panelReport.getJpaViewer().getBtnFitWidth().getName())) {
								setJToggleButton(panelReport.getJpaViewer().getBtnFitWidth());
							}

						}
					}
				}
				saveValuesToCache();

			}
			// Titel setzen
			String sTitle = "(" + LPMain.getTextRespectUISPr("lp.report") + "=" + print.getSReportName() + ")";
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
		}
	}

	private void reloadPanelVersandComponents() throws Throwable {
		if (panelVersandEmail == null)
			return;

		panelVersandEmail.setMailtextDto(getMailtextDto());
		panelVersandEmail.setDefaultsPanel();
	}

	private void setJToggleButton(JToggleButton jToggleButton) {
		jToggleButton.setSelected(true);
	}

	/**
	 * showDialogKeineDatenZuDrucken
	 */
	private void showDialogKeineDatenZuDrucken() {
		// das untere panel weg
		JPanel jpaPanel = new JPanel();
		jpaPanel.setLayout(new BorderLayout());
		JLabel jlaNoPages = new JLabel(LPMain.getTextRespectUISPr("lp.hint.nopages"));
		jlaNoPages.setHorizontalAlignment(SwingConstants.CENTER);
		jpaPanel.add(jlaNoPages, BorderLayout.CENTER);
		jSplitPane1.add(jpaPanel, JSplitPane.BOTTOM);
	}

	protected PanelVersandEmail getPanelVersandEmail(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelVersandEmail == null && bNeedInstantiationIfNull) {
			String belegartCNr = null;
			Integer belegIId = null;
			if (jpaPanelReportIf instanceof ReportBeleg) {
				belegartCNr = ((ReportBeleg) jpaPanelReportIf).getBelegartCNr();
				belegIId = ((ReportBeleg) jpaPanelReportIf).getIIdBeleg();
			}

			MailtextDto mtDto = getMailtextDto();

			panelVersandEmail = new PanelVersandEmail(getInternalFrame(), mtDto, belegartCNr, belegIId,
					jpaPanelReportIf, this, getOptions().getPartnerDtoEmpfaenger());
			panelVersandEmail.setDefaultAbsender(getOptions().getPartnerDtoEmpfaenger(),
					getOptions().getAnsprechpartnerIId());
			// Direktversand
			if (getOptions().isDirekt()) {
				panelVersandEmail.setEditorFieldVisible(false);
			}
			panelVersandEmail.installActionListeners(this);
			// panelVersandEmail.getWbuSenden().addActionListener(this);
			// panelVersandEmail.getwbuAnhangWaehlen().addActionListener(this);
			// panelVersandEmail.getwbuAnhangLoeschen().addActionListener(this);

			jpaTop.add(panelVersandEmail, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			panelVersandEmail.setVisible(false);
			iZeile++;
		}
		return panelVersandEmail;
	}

	private MailtextDto getMailtextDto() throws Throwable, ExceptionLP {
		MailtextDto mtDto = jpaPanelReportIf.getMailtextDto();
		if (mtDto != null) {

			if (panelStandardDrucker.getVariante() != null) {
				ReportvarianteDto varDto = DelegateFactory.getInstance().getDruckerDelegate()
						.reportvarianteFindByPrimaryKey(panelStandardDrucker.getVariante());
				mtDto.setParamXslFile(varDto.getCReportnamevariante());
			}

		}
		return mtDto;
	}

	protected PanelVersandFax getPanelVersandFax(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelVersandFax == null && bNeedInstantiationIfNull) {
			String belegartCNr = null;
			Integer belegIId = null;
			if (jpaPanelReportIf instanceof ReportBeleg) {
				belegartCNr = ((ReportBeleg) jpaPanelReportIf).getBelegartCNr();
				belegIId = ((ReportBeleg) jpaPanelReportIf).getIIdBeleg();
			}
			panelVersandFax = new PanelVersandFax(getInternalFrame(), belegartCNr, belegIId, jpaPanelReportIf, this,
					getOptions().getPartnerDtoEmpfaenger());
			panelVersandFax.setDefaultAbsender(getOptions().getPartnerDtoEmpfaenger(),
					getOptions().getAnsprechpartnerIId());
			panelVersandFax.getWbuSenden().addActionListener(this);
			jpaTop.add(panelVersandFax, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			panelVersandFax.setVisible(false);
			iZeile++;
		}
		return panelVersandFax;
	}

	private JasperPrintLP createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(boolean bMitLogo, String sDrucktype)
			throws Throwable {
		int iKopienAktuell = 0;
		if (jpaPanelReportIf instanceof ReportBeleg) {
			Integer iiKopienAktuell = ((ReportBeleg) jpaPanelReportIf).getKopien();

			if (iiKopienAktuell != null) {
				iKopienAktuell = iiKopienAktuell.intValue();
			}
		}

		// PJ 16106
		if (jpaPanelReportIf instanceof ReportEingangsrechnung) {
			((ReportEingangsrechnung) jpaPanelReportIf).schecknummerZurueckschreiben();
		}

		JasperPrintLP print = null;
		// wenn er noch nicht erzeugt wurde, muss er jetzt auf jeden fall
		// erzeugt werden
		if (panelReport == null || panelReport.getPrint() == null || isRefreshNecessary()) {
			if (jpaPanelReportIf instanceof PanelReportLogoIfJRDS) {
				((PanelReportLogoIfJRDS) jpaPanelReportIf).setBPrintLogo(bMitLogo);
			}
			print = createPrint(sDrucktype);
			panelReport = new PanelReport(getInternalFrame(), this.getAdd2Title(), print, false);
			panelReport.setReportFilenameSupplier(() -> {
				try {
					return getReportFilename();
				} catch (Throwable e1) {
					return Optional.empty();
				}
			});
		} else {
			// er ist schon da
			print = panelReport.getPrint();

			boolean hasLogoChanged = isLogoImmerDrucken() ? false
					: (jpaPanelReportIf instanceof PanelReportLogoIfJRDS) & (bMitLogo != print.getBMitLogo());
			boolean hasKopienChanged = iKopien != iKopienAktuell;

			if (hasLogoChanged) {
				((PanelReportLogoIfJRDS) jpaPanelReportIf).setBPrintLogo(bMitLogo);
			}
			if (hasLogoChanged || hasKopienChanged) {
				print = createPrint(sDrucktype);
			}

			/*
			 * // jetzt muss noch geprueft werden, ob das logo fuer Belege drauf // sein
			 * soll oder nicht if (jpaPanelReportIf instanceof ReportBeleg) { ((ReportBeleg)
			 * jpaPanelReportIf).setBPrintLogo(bMitLogo); // wenn der in der falschen form
			 * da ist ... if (bMitLogo != print.getBMitLogo() || iKopien != iKopienAktuell)
			 * { // ... muss er nochmal erzeugt werden print = createPrint(sDrucktype); } }
			 * 
			 * else if (jpaPanelReportIf instanceof com.lp.client.partner.ReportSerienbrief)
			 * { ((com.lp.client.partner.ReportSerienbrief) jpaPanelReportIf)
			 * .setBPrintLogo(bMitLogo); // wenn der in der falschen form da ist ... if
			 * (bMitLogo != print.getBMitLogo() || iKopien != iKopienAktuell) { // ... muss
			 * er nochmal erzeugt werden print = createPrint(sDrucktype); } } else if
			 * (jpaPanelReportIf instanceof com.lp.client.partner.ReportKurzbrief) {
			 * ((com.lp.client.partner.ReportKurzbrief) jpaPanelReportIf)
			 * .setBPrintLogo(bMitLogo); // wenn der in der falschen form da ist ... if
			 * (bMitLogo != print.getBMitLogo() || iKopien != iKopienAktuell) { // ... muss
			 * er nochmal erzeugt werden print = createPrint(sDrucktype); } } else if
			 * (jpaPanelReportIf instanceof com.lp.client.partner.ReportLastschrift) {
			 * ((com.lp.client.partner.ReportLastschrift) jpaPanelReportIf)
			 * .setBPrintLogo(bMitLogo); // wenn der in der falschen form da ist ... if
			 * (bMitLogo != print.getBMitLogo() || iKopien != iKopienAktuell) { // ... muss
			 * er nochmal erzeugt werden print = createPrint(sDrucktype); } }
			 */
		}
		// Kopien merken
		iKopien = iKopienAktuell;
		return print;
	}

	private JasperPrintLP createPrint(String sDrucktype) throws Throwable {
		JasperPrintLP print = null;
		try {
			LPMain.getTheClient().setReportvarianteIId(panelStandardDrucker.getVariante());
			print = jpaPanelReportIf.getReport(sDrucktype);
			LPMain.getTheClient().setReportvarianteIId(null);
		} catch (ExceptionLP ex) {
			// Reportexceptions hier speziell behandeln
			if (!handleOwnException(ex)) {
				// weiter werfen, fass sie nicht behandelt werden konnte
				throw ex;
			}
		}
		return print;
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		if (exfc.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND) {
			String message = LPMain.getTextRespectUISPr("lp.error.reportfilenotfound");
			if (exfc.getAlInfoForTheClient() != null && !exfc.getAlInfoForTheClient().isEmpty()) {
				for (Iterator<?> iter = exfc.getAlInfoForTheClient().iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					message = message + "\n" + item;
				}
			}
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), message);
			return true;
		} else if (exfc.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DATEN_ZU_DRUCKEN) {
			showDialogKeineDatenZuDrucken();
			return true;
		} else if (exfc.getICode() == EJBExceptionLP.FEHLER_ATTACHMENTS_ZU_GROSS) {
			return true;
		} else {
			return false;
		}
	}

	public static MailtextDto getDefaultMailtextDto(PanelReportIfJRDS panelReportIfJRDS) throws Throwable {
		// report_email: 1 hier wird ein standard-MailtextDto erstellt
		MailtextDto mailtextDto = new MailtextDto();
		mailtextDto.setParamLocale(LPMain.getTheClient().getLocUi());
		mailtextDto.setParamMandantCNr(LPMain.getTheClient().getMandant());
		mailtextDto.setParamModul(panelReportIfJRDS.getModul());
		mailtextDto.setParamXslFile(panelReportIfJRDS.getReportname());

		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	public void print() throws Throwable {
		print(false, JasperPrintLP.DRUCKTYP_DRUCKER);
	}

	public void print(JasperPrintLP jasperPrintLP) throws Throwable {
		print(jasperPrintLP, false);
	}

	public void print(boolean bNurKopien, String sDrucktyp) throws Throwable {
		JasperPrintLP jasperPrintLP = createPrintIfPrintWasNotCreatedOrUpdateIfNecessary(false, sDrucktyp);
		if (bNurKopien) {
			jasperPrintLP = mergeWithPrintTypeReport(jasperPrintLP, sDrucktyp);
		}
		print(jasperPrintLP, bNurKopien);
	}

	public void print(JasperPrintLP jasperPrintLP, boolean bNurKopien) throws Throwable {
		boolean bCloseDialog = true;
		JasperPrint print = jasperPrintLP.getPrint();

		if (print.getPages().size() > 0) {
			PrintService printer = panelStandardDrucker.getSelectedPrintService();

			String sUbersteuerterDrucker = DelegateFactory.getInstance().getSystemDelegate().getPrinterNameForReport(
					jpaPanelReportIf.getModul(), jpaPanelReportIf.getReportname(),
					LPMain.getInstance().getUISprLocale(), null);
			if (sUbersteuerterDrucker != null) {
				PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, null);
				for (int i = 0; i < printService.length; i++) {
					if (printService[i].getName().equals(sUbersteuerterDrucker)) {
						printer = printService[i];
					}
				}
			}
			if (printer != null) {
				try {

					PrintService printerKopien = null;
					Integer kopien = null;
					if (jpaPanelReportIf instanceof ReportBeleg) {
						ReportBeleg reportBeleg = ((ReportBeleg) jpaPanelReportIf);
						printerKopien = reportBeleg.getSelectedPrintServiceKopien();
						kopien = reportBeleg.getKopien();
					}

					if (printerKopien != null && kopien != null && kopien > 0
							&& print.getPages().size() % (kopien + 1) == 0) {

						int iSeitenProKopie = print.getPages().size() / (kopien + 1);

						if (iSeitenProKopie > 0 && iSeitenProKopie < print.getPages().size()) {

							List<JRPrintPage> pages = new ArrayList<JRPrintPage>(print.getPages());

							print.getPages().clear();
							print.getPages().addAll(pages.subList(0, iSeitenProKopie));

							// Zuerst Original
							if (bNurKopien == false) {
								ausdrucken(print, printer);
							}

							// Dann Kopien
							print.getPages().clear();
							print.getPages().addAll(pages.subList(iSeitenProKopie, pages.size()));
							ausdrucken(print, printerKopien);

							// SP5208 Damit das Original fuer die
							// Dokumentenablage zur Verfuegung steht
							print.getPages().clear();
							print.getPages().addAll(pages.subList(0, iSeitenProKopie));

						} else {
							ausdrucken(print, printer);
						}

					} else {

						if (printerKopien != null && kopien != null && kopien > 0
								&& print.getPages().size() % (kopien + 1) != 0) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
									LPMain.getTextRespectUISPr("lp.kopiendrucker.splittingnichtmoeglich"));
						}

						if (bNurKopien) {

							if (kopien != null && kopien > 0) {
								// Original auslassen
								int iSeitenProKopie = print.getPages().size() / (kopien + 1);

								if (iSeitenProKopie > 0) {
									List<JRPrintPage> pages = new ArrayList<JRPrintPage>(print.getPages());

									print.getPages().clear();
									print.getPages().addAll(pages.subList(iSeitenProKopie, pages.size()));
									ausdrucken(print, printer);

									// SP5208 Damit das Original fuer die
									// Dokumentenablage zur Verfuegung steht
									print.getPages().clear();
									print.getPages().addAll(pages.subList(0, iSeitenProKopie));

								}
							}
						} else {
							ausdrucken(print, printer);
						}

					}

				} catch (Exception ex) {
					if (ex instanceof ExceptionLP && ((ExceptionLP) ex)
							.getICode() == EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT) {
						bCloseDialog = false;
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.drucken.keindruckerinstalliert"));
					}

					else if (ex instanceof JRException) {
						// throw new JRException(ex);
						if (ex.getCause() != null) {
							if (ex.getCause() instanceof PrinterException) {
								// z.B Fehler wie "Printer is not Accepting Job"
								// oder "Invalid Name of PrinterService".
								throw new ExceptionLP(EJBExceptionLP.FEHLER_FEHLER_BEIM_DRUCKEN, ex);
							}
						}
					} else {
						new DialogError(LPMain.getInstance().getDesktop(), ex, DialogError.TYPE_ERROR);
					}
				}
				// PJ 17341
				boolean bAutomatischVerlassen = (Boolean) DelegateFactory
						.getInstance().getParameterDelegate().getMandantparameter(LPMain.getTheClient().getMandant(),
								"ALLGEMEIN", ParameterFac.PARAMETER_DRUCKVORSCHAU_AUTOMATISCH_BEENDEN)
						.getCWertAsObject();

				if (bCloseDialog && bAutomatischVerlassen == true) {

					// PJ18621
					if (jpaPanelReportIf instanceof ReportWochenabschluss) {
						ReportWochenabschluss rw = (ReportWochenabschluss) jpaPanelReportIf;
						if (rw.wcoInVorschauBleiben.isSelected()) {
							return;
						}
					}

					getInternalFrame().closePanelDialog();
					if (jpaPanelReportIf instanceof ReportBeleg) {
						((ReportBeleg) jpaPanelReportIf).printDialogClosed();
					}
				}
			} else {
				showDialogKeineDatenZuDrucken();
			}
		}
	}

	private void ausdrucken(JasperPrint print, PrintService printer) throws Throwable, JRException {
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		// SP6676
		// printRequestAttributeSet.add(new PrinterResolution(600, 600,
		// ResolutionSyntax.DPI));

		int printableWidth = 0;
		int printableHeight = 0;

		if (print.getOrientationValue() == OrientationEnum.LANDSCAPE) {
			printableWidth = print.getPageHeight();
			printableHeight = print.getPageWidth();
		} else {
			printableWidth = print.getPageWidth();
			printableHeight = print.getPageHeight();
		}

		if ((printableWidth != 0) && (printableHeight != 0)) {
			printRequestAttributeSet.add(new MediaPrintableArea(0.0F, 0.0F, printableWidth / 72.0F,
					printableHeight / 72.0F, MediaPrintableArea.INCH));
		}

		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(printer.getName(), null));

		// AD JR LPJRPrintServiceExporter exporter = new
		// LPJRPrintServiceExporter();
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);

		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

		// print it
		int iAnzahl = 1;
		// Etiketten koennen mehrfach gedruckt werden
		/*
		 * if (jpaPanelReportIf instanceof ReportEtikett) { Integer iExemplare =
		 * ((ReportEtikett) jpaPanelReportIf).getAnzahlExemplare(); if (iExemplare !=
		 * null && iExemplare.intValue() >= 1) { iAnzahl = iExemplare.intValue(); } }
		 * 
		 * if (jpaPanelReportIf instanceof ReportWepEtikett) { Integer iExemplare =
		 * ((ReportWepEtikett) jpaPanelReportIf).getAnzahlExemplare(); if (iExemplare !=
		 * null && iExemplare.intValue() >= 1) { iAnzahl = iExemplare.intValue(); } }
		 */

		PrintService psOverride = overrideAcceptingJobs(printer);
		if (psOverride != null) {
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, psOverride);

			// mehrere Exemplare geht auch
			/**
			 * @todo MR->VF exporter.exportReport() als Thread laufen lassen.
			 */
			for (int i = 0; i < iAnzahl; i++) {
				exporter.exportReport();
			}
		}
	}

	private boolean isAcceptingPrintJobs(PrintService printService) {
		// PrintService[] printers = PrinterJob.lookupPrintServices();
		// for(int i = 0 ; i < printers.length; i++) {
		// PrintServiceAttributeSet attributes = printers[i].getAttributes() ;
		// Attribute as[] = attributes.toArray() ;
		// for(int j = 0 ; j < as.length; j++) {
		// System.out.println("Attribute " + as[j].getName() + " " +
		// as[j].toString()) ;
		// }
		//
		// PrinterIsAcceptingJobs acceptingJob =
		// printers[i].getAttribute(PrinterIsAcceptingJobs.class) ;
		// System.out.println("actual state is " + acceptingJob.getValue() +
		// " for " + printers[i].getName()) ;
		// }
		//
		// PrintService tempPrinter =
		// PrintServiceLookup.lookupDefaultPrintService();
		// PrinterIsAcceptingJobs acceptingJob =
		// tempPrinter.getAttribute(PrinterIsAcceptingJobs.class) ;
		// if(null != acceptingJob) {
		// System.out.println("actual state is " + acceptingJob.getValue()) ;
		// }

		PrinterIsAcceptingJobs attribute = printService.getAttribute(PrinterIsAcceptingJobs.class);
		if (null == attribute)
			return true;
		return 1 == attribute.getValue();
		// return false ;
	}

	private PrintService overrideAcceptingJobs(PrintService printService) {
		if (!isAcceptingPrintJobs(printService)) {
			boolean shouldPrint = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.drucken.hinweisprinterisnotacceptingjob"),
					LPMain.getTextRespectUISPr("lp.achtung"), JOptionPane.QUESTION_MESSAGE, JOptionPane.NO_OPTION);
			if (shouldPrint) {
				PrintService psOverride = new ForcedPrintServiceAcceptingJob(printService, true);
				printService = psOverride;
			} else {
				printService = null;
			}
		}

		return printService;
	}

	private void setDruckeinstellungenGeladen(boolean b) {
		panelStandardDrucker.getWbuLoeschen().setEnabled(b);
//		panelStandardDrucker.getWbuSpeichern().setBackground(b ? new Color(0, 200, 0) : null);
//		panelStandardDrucker.getWbuSpeichern().setOpaque(true);
		panelStandardDrucker.getWbuSpeichern().setPrintPropertiesSaved(b);
	}

	public void refreshVarianten() throws Throwable {
		Map m = DelegateFactory.getInstance().getDruckerDelegate().holeAlleVarianten(jpaPanelReportIf.getReportname());
		panelStandardDrucker.getWcoVariante().setMap(m);
	}

	private void initStandarddrucker() throws Throwable {
		if (standarddruckerDto == null) {
			standarddruckerDto = new StandarddruckerDto();
		}
		standarddruckerDto.setCPc(Helper.getPCName());
		if (jpaPanelReportIf instanceof ReportBeleg) {
			Integer kstIId = ((ReportBeleg) jpaPanelReportIf).getKostenstelleIId();
			standarddruckerDto.setKostenstelleIId_notInDB(kstIId);

		}
		/**
		 * @todo das Locale des Reports
		 */
		standarddruckerDto.setLocale_notInDB(LPMain.getTheClient().getLocUi());
		standarddruckerDto.setMandantCNr(LPMain.getTheClient().getMandant());
		standarddruckerDto.setSFilename_notInDB(jpaPanelReportIf.getReportname());
		standarddruckerDto.setSModul_notInDB(jpaPanelReportIf.getModul());

		if (bVariantenInitialisiert == true && panelStandardDrucker.getVariante() == null) {
			standarddruckerDto.setReportvarianteIId(null);
			// Drucker holen
			standarddruckerDto = DelegateFactory.getInstance().getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneVariante(standarddruckerDto);
		} else {
			standarddruckerDto.setReportvarianteIId(panelStandardDrucker.getVariante());
			// Drucker holen
			standarddruckerDto = DelegateFactory.getInstance().getDruckerDelegate()
					.standarddruckerFindByPcReportnameOhneExc(standarddruckerDto);
		}

		if (bVariantenInitialisiert == false) {

			Map m = DelegateFactory.getInstance().getDruckerDelegate()
					.holeAlleVarianten(jpaPanelReportIf.getReportname());
			panelStandardDrucker.getWcoVariante().setMap(m);

			panelStandardDrucker.getWcoVariante()
					.addActionListener(new PanelReportKriterien_wcoVariante_actionAdapter(this));
			panelStandardDrucker.getWcoVariante().addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (ItemEvent.SELECTED == e.getStateChange()) {
						try {
							reloadPanelVersandComponents();
						} catch (Throwable e1) {
							myLogger.error(e1);
						}
					}
				}
			});
			bVariantenInitialisiert = true;
		}

		// Drucker vorselektieren
		boolean druckerEinstellungenGeladen = false;
		if (standarddruckerDto != null) {

			panelStandardDrucker.setSelectedPrinter(standarddruckerDto.getCDrucker());

			if (jpaPanelReportIf instanceof ReportBeleg) {

				((ReportBeleg) jpaPanelReportIf).setPrinterKopien(standarddruckerDto.getCDruckerKopien());

			}

			if (standarddruckerDto.getReportvarianteIId() != null) {
				panelStandardDrucker.getWcoVariante().setKeyOfSelectedItem(standarddruckerDto.getReportvarianteIId());
			}

			konfDtos = DelegateFactory.getInstance().getDruckerDelegate()
					.reportkonfFindByStandarddruckerIId(standarddruckerDto.getIId());
			druckerEinstellungenGeladen = (konfDtos != null && konfDtos.length > 0);
			// Nun Einstellungen wieder in die Komponenenten schreiben
			if (druckerEinstellungenGeladen) {
				schreibeReportKonfInDieKomponentenZurueck(konfDtos);
			}
		}

		// Buttons anpassen
		setDruckeinstellungenGeladen(druckerEinstellungenGeladen);

		panelStandardDrucker.getWbuSpeichern().addActionListener(this);
		panelStandardDrucker.getWbuSpeichern().setActionCommand(ACTION_SPECIAL_DRUCKER_SPEICHERN);
		panelStandardDrucker.getWbuLoeschen().addActionListener(this);
		panelStandardDrucker.getWbuLoeschen().setActionCommand(ACTION_SPECIAL_DRUCKER_LOESCHEN);

		if (panelVersandEmail != null) {
			panelVersandEmail.bUebersteuerterEmpaengerVorschlagGesetzt = false;
			panelVersandEmail.bUebersteuerterEmpaengerVorschlagCCGesetzt = false;
			panelVersandEmail.setVorschlag();
		}

	}

	private void schreibeReportKonfInDieKomponentenZurueck(ReportkonfDto[] dtos) throws Throwable {

		// Zuerst alle Check-Boxen und Radio-Buttons

		for (int i = 0; i < dtos.length; i++) {
			ReportkonfDto dto = dtos[i];

			if (dto.getCKomponententyp().equals(DruckerFac.REPORTKONF_KOMPONENTENTYP_RADIOBUTTON)
					|| dto.getCKomponententyp().equals(DruckerFac.REPORTKONF_KOMPONENTENTYP_CHECKBOX)) {

				sucheKomponente(dto, ((PanelBasis) jpaPanelReportIf).getComponents());
			}

		}

		for (int i = 0; i < dtos.length; i++) {
			ReportkonfDto dto = dtos[i];

			sucheKomponente(dto, ((PanelBasis) jpaPanelReportIf).getComponents());

		}

		reportKonfGeladen(jpaPanelReportIf);
	}

	protected void reportKonfGeladen(PanelReportIfJRDS panelReport) {
		if (panelReport instanceof PanelReportIfJRDSHasNonPersistentFields) {
			((PanelReportIfJRDSHasNonPersistentFields) panelReport).reportKonfGeladen();
		}
	}

	private void sucheKomponente(ReportkonfDto dto, Component[] components) throws Throwable {

		try {
			for (int i = 0; i < components.length; ++i) {

				if (components[i].getName() != null && components[i].getName().equals(dto.getCKomponentenname())) {
					if (components[i] instanceof WrapperSelectField) {
						WrapperSelectField wsf = (WrapperSelectField) components[i];

						wsf.setSTyp(dto.getCTyp());

						try {
							Integer iKey = new Integer(dto.getCKey());
							wsf.setKey(iKey);
						} catch (Exception e) {
							wsf.setKey(dto.getCKey());
						}

					} else if (components[i] instanceof WrapperTextField) {

						WrapperTextField wtf = (WrapperTextField) components[i];

						if (wtf.isSaveReportInformation() == true && wtf.isActivatable() == true) {
							if (wtf.isEditable() == true) {
								wtf.setText(dto.getCKey());
							}
						}

					} else if (components[i] instanceof WrapperCheckBox) {

						WrapperCheckBox wcb = (WrapperCheckBox) components[i];

						if (Helper.short2boolean(new Short(dto.getCKey())) == true) {
							if (wcb.isSelected() == false) {
								wcb.setSelected(false);
								wcb.doClick();
							}
						} else {
							if (wcb.isSelected() == true) {
								wcb.setSelected(true);
								wcb.doClick();
							}
						}

					} else if (components[i] instanceof WrapperRadioButton || components[i] instanceof JRadioButton) {

						JRadioButton wcb = (JRadioButton) components[i];

						wcb.doClick();

					} else if (components[i] instanceof WrapperComboBoxPeriode) {

						WrapperComboBoxPeriode wcb = (WrapperComboBoxPeriode) components[i];

						try {
							Integer iKey = new Integer(dto.getCKey());
							wcb.setPeriode(iKey);
						} catch (Exception e) {
							//
						}

					} else if (components[i] instanceof WrapperComboBox) {

						WrapperComboBox wcb = (WrapperComboBox) components[i];

						try {
							Integer iKey = new Integer(dto.getCKey());
							wcb.setKeyOfSelectedItem(iKey);
						} catch (Exception e) {
							wcb.setKeyOfSelectedItem(dto.getCKey());
						}

					} else if (components[i] instanceof WrapperDateField) {

						WrapperDateField wcb = (WrapperDateField) components[i];
						Long iKey = new Long(dto.getCKey());
						wcb.setDate(new java.sql.Date(iKey));
					} else if (components[i] instanceof WrapperNumberField) {
						WrapperNumberField wcb = (WrapperNumberField) components[i];
						wcb.setBigDecimal(new BigDecimal(dto.getCKey()));
					} else if (components[i] instanceof WrapperSpinner) {
						WrapperSpinner wcb = (WrapperSpinner) components[i];
						wcb.setInteger(new Integer(dto.getCKey()));
					}

				}
				if ((components[i] instanceof java.awt.Container) && !(components[i] instanceof IControl)) {

					sucheKomponente(dto, ((java.awt.Container) components[i]).getComponents());
				}
			}
		} catch (Exception e) {
			// WENN FEHLER, dann wird der Eintrag geloescht
			DelegateFactory.getInstance().getDruckerDelegate().removeReportkonf(dto.getIId());
		}

	}

	private ReportkonfDto[] getKomponentenEinesReports() throws Throwable {
		ArrayList<?> al = getReportkonfDtos(new ArrayList(), ((PanelBasis) jpaPanelReportIf).getComponents());

		getZoomFaktor(al);

		ReportkonfDto[] a = new ReportkonfDto[al.size()];
		return al.toArray(a);

	}

	private ArrayList getReportkonfDtos(ArrayList alKomponenten, Component[] components) throws Throwable {

		for (int i = 0; i < components.length; ++i) {

			if ((components[i] instanceof java.awt.Container) && !(components[i] instanceof IControl)
					&& !(components[i] instanceof JRadioButton)) {
				if (!(components[i] instanceof WrapperLabel)) {
					alKomponenten = getReportkonfDtos(alKomponenten,
							((java.awt.Container) components[i]).getComponents());
				}
			} else {
				ReportkonfDto reportkonfDto = new ReportkonfDto();
				reportkonfDto.setCKomponentenname(components[i].getName());
				if (components[i].getName() != null && components[i].isEnabled()) {
					if (components[i] instanceof WrapperSelectField) {
						WrapperSelectField wsf = (WrapperSelectField) components[i];

						if (wsf.getOKey() != null) {

							reportkonfDto.setCKey(wsf.getOKey().toString());
							reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_SELECTFIELD);
							reportkonfDto.setCTyp(wsf.getSTyp());

						}

					} else if (components[i] instanceof WrapperTextField) {

						WrapperTextField wtf = (WrapperTextField) components[i];
						if (wtf.isSaveReportInformation() == true && wtf.getText() != null && wtf.isActivatable()) {

							if (wtf.isEditable() == true) {

								reportkonfDto.setCKey(wtf.getText());
								reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_TEXTFIELD);
							}
						}
					} else if (components[i] instanceof WrapperCheckBox) {

						WrapperCheckBox wcb = (WrapperCheckBox) components[i];
						if (wcb.isSaveReportInformation() == true) {
							reportkonfDto.setCKey(wcb.getShort().toString());
							reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_CHECKBOX);
						}

					} else if (components[i] instanceof WrapperRadioButton || components[i] instanceof JRadioButton) {

						JRadioButton wcb = (JRadioButton) components[i];

						if (wcb.isSelected() == true) {
							reportkonfDto.setCKey(Helper.boolean2Short(wcb.isSelected()).toString());
							reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_RADIOBUTTON);
						}

					} else if (components[i] instanceof WrapperComboBoxPeriode) {

						WrapperComboBoxPeriode wcb = (WrapperComboBoxPeriode) components[i];

						reportkonfDto.setCKey(wcb.getPeriode() + "");
						reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_COMBOBOX_PERIODE);

					} else if (components[i] instanceof WrapperComboBox) {

						WrapperComboBox wcb = (WrapperComboBox) components[i];
						if (wcb.getKeyOfSelectedItem() != null) {
							reportkonfDto.setCKey(wcb.getKeyOfSelectedItem().toString());
							reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_COMBOBOX);
						}
					} else if (components[i] instanceof WrapperDateField) {

						WrapperDateField wcb = (WrapperDateField) components[i];
						if (wcb.getDate() != null) {
							reportkonfDto.setCKey(wcb.getDate().getTime() + "");
							reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_DATEFIELD);
						}
					} else if (components[i] instanceof WrapperNumberField) {

						WrapperNumberField wcb = (WrapperNumberField) components[i];
						// SP7918
						if (wcb.isEditable()) {
							if (wcb.getBigDecimal() != null) {
								reportkonfDto.setCKey(wcb.getBigDecimal().toString());
								reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_NUMBERFIELD);
							}
						}
					} else if (components[i] instanceof WrapperSpinner) {

						WrapperSpinner wsp = (WrapperSpinner) components[i];
						if (wsp.getInteger() != null) {
							reportkonfDto.setCKey(wsp.getInteger().toString());
							reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_SPINNER);
						}
					}

					if (reportkonfDto.getCKey() != null) {
						alKomponenten.add(reportkonfDto);
					}

				}
			}

		}
		return alKomponenten;
	}

	private void getZoomFaktor(ArrayList alKomponenten) {

		if (panelReport != null) {
			JToggleButton jToggleButton = getJToggleButton();

			Float zoom = 1F;
			zoom = panelReport.getJpaViewer().getZoom();

			if (jToggleButton == null) {
				ReportkonfDto reportkonfDto = new ReportkonfDto();
				reportkonfDto.setCKomponentenname("ZOOMFAKTOR");
				reportkonfDto.setCKey(zoom + "");
				reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_COMBOBOX);
				alKomponenten.add(reportkonfDto);
			}

			else if (jToggleButton != null) {
				ReportkonfDto reportkonfDto = new ReportkonfDto();
				reportkonfDto.setCKomponentenname(jToggleButton.getName().toString());
				reportkonfDto.setCKey("true");
				reportkonfDto.setCKomponententyp(DruckerFac.REPORTKONF_KOMPONENTENTYP_JTOGGLEBUTTON);
				alKomponenten.add(reportkonfDto);
			}
		}
	}

	private JToggleButton getJToggleButton() {
		JToggleButton jToggleButton = null;
		if (panelReport.getJpaViewer().getBtnActualSize().isSelected())
			jToggleButton = panelReport.getJpaViewer().getBtnActualSize();
		else if (panelReport.getJpaViewer().getBtnFitPage().isSelected())
			jToggleButton = panelReport.getJpaViewer().getBtnFitPage();
		else if (panelReport.getJpaViewer().getBtnFitWidth().isSelected())
			jToggleButton = panelReport.getJpaViewer().getBtnFitWidth();
		return jToggleButton;
	}

	private void saveStandarddrucker() throws Throwable {
		try {
			if (standarddruckerDto == null) {
				standarddruckerDto = new StandarddruckerDto();
			}
			if (Helper.getPCName() != null && jpaPanelReportIf.getModul() != null
					&& jpaPanelReportIf.getReportname() != null) {
				// nur, wenn auch ein Drucker ausgewaehlt ist
				if (panelStandardDrucker.getSelectedPrinter() != null) {
					standarddruckerDto.setCPc(Helper.getPCName());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						Integer kstIId = ((ReportBeleg) jpaPanelReportIf).getKostenstelleIId();
						standarddruckerDto.setKostenstelleIId_notInDB(kstIId);
						standarddruckerDto
								.setCDruckerKopien(((ReportBeleg) jpaPanelReportIf).getSelectedPrinterKopien());

					}
					/**
					 * @todo das Locale des Reports
					 */
					standarddruckerDto.setLocale_notInDB(LPMain.getTheClient().getLocUi());
					standarddruckerDto.setMandantCNr(LPMain.getTheClient().getMandant());
					standarddruckerDto.setSFilename_notInDB(jpaPanelReportIf.getReportname());
					standarddruckerDto.setSModul_notInDB(jpaPanelReportIf.getModul());
					standarddruckerDto.setCDrucker(panelStandardDrucker.getSelectedPrinter());
					standarddruckerDto.setBStandard(Helper.boolean2Short(true));

					standarddruckerDto.setReportvarianteIId(panelStandardDrucker.getVariante());
					if (jpaPanelReportIf instanceof ReportBeleg) {
						standarddruckerDto
								.setKostenstelleIId_notInDB(((ReportBeleg) jpaPanelReportIf).getKostenstelleIId());
					}
					Integer iId = DelegateFactory.getInstance().getDruckerDelegate()
							.updateStandarddrucker(standarddruckerDto);
					standarddruckerDto.setIId(iId);

					// PJ 14057 Nun auch Einstellung mitspeichern
					ReportkonfDto[] dtos = getKomponentenEinesReports();

					if (dtos != null && dtos.length > 0) {
						DelegateFactory.getInstance().getDruckerDelegate().saveReportKonf(standarddruckerDto.getIId(),
								dtos);
						setDruckeinstellungenGeladen(true);
					}

				}
			}
		} catch (ExceptionLP ex) {
			if (ex.getICode() == EJBExceptionLP.FEHLER_BEIM_ANLEGEN) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.drucken.druckereinstellungkonntenichtgespeichertwerden"));
			} else {
				throw ex;
			}
		}
	}

	private void removeStandarddrucker() throws Throwable {
		if (standarddruckerDto != null) {
			DelegateFactory.getInstance().getDruckerDelegate().deleteReportKonf(standarddruckerDto.getIId());
			DelegateFactory.getInstance().getDruckerDelegate().removeStandarddrucker(standarddruckerDto);
			standarddruckerDto = null;

			setDruckeinstellungenGeladen(false);

		}
	}

	private void archiviereBeleg() throws Throwable {
		if (panelReport != null) {
			JasperPrintLP print = panelReport.getPrint();
			archiviereBeleg(print);

		} else {
			JasperPrintLP print = createPrint(null);
			archiviereBeleg(print);
		}
	}

	private void archiviereBeleg(JasperPrintLP print) throws ExceptionLP, Throwable {
		String sBelegnummer = null;
		String sRow = null;
		LPMain.getInstance();
		TheClientDto theClientDto = LPMain.getTheClient();
		// Path Seperator vom server holen damit der Name richtig angezeigt
		// werden kann
		String sPathSeperator = DelegateFactory.getInstance().getSystemDelegate().getServerPathSeperator();
		String sName = print.getSReportName().substring(print.getSReportName().lastIndexOf(sPathSeperator) + 1);
		// Wennn ein FLR-Druck dann is der Reportname ""
		if (!sName.equals("")) {
			DokumentnichtarchiviertDto docNichtArchivDto = DelegateFactory.getInstance().getJCRDocDelegate()
					.dokumentnichtarchiviertfindbyMandantCReportname(theClientDto.getMandant(), sName);
			if (docNichtArchivDto == null) {
				PrintInfoDto oInfo = print.getOInfoForArchive();
				DocPath docPath = null;
				if (oInfo != null) {
					docPath = oInfo.getDocPath();
				}
				if (docPath != null) {
					// Nur archivieren wenn nicht in dieser Tabelle vorhanden
					JCRDocDto jcrDocDto = new JCRDocDto();
					sBelegnummer = docPath.getLastDocNode().asVisualPath();

					if (sBelegnummer == null) {
						sBelegnummer = "0000000";
					}
					sRow = docPath.getLastDocNode().asPath();
					sRow = sRow == null ? " " : sRow;
					Integer iPartnerIId;
					if (oInfo.getiId() != null) {
						iPartnerIId = oInfo.getiId();
					} else {
						// Das PartnerDto kann gesetzt, aber leer sein
						if (getOptions().getPartnerDtoEmpfaenger() != null
								&& getOptions().getPartnerDtoEmpfaenger().getIId() != null) {
							iPartnerIId = getOptions().getPartnerDtoEmpfaenger().getIId();
						} else {
							// Wenn kein Partner uebergeben dann Default
							MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate()
									.mandantFindByPrimaryKey(theClientDto.getMandant());
							iPartnerIId = mandantDto.getPartnerIId();
						}
					}

					// File file = File.createTempFile(sName, ".jrprint");
					// file.delete();
					// JRSaver.saveObject(print.getPrint(), file);

					Helper.setJcrDocBinaryData(jcrDocDto, print.getPrint());

					// AD JR JRPrintSaveContributor saver = new
					// JRPrintSaveContributor(getLocale(),));
					// AD JR saver.save(print.getPrint(), file);
					// jcrDocDto.setbData(Helper.getBytesFromFile(file));
					jcrDocDto.setJasperPrint(print.getPrint());
					// file.delete();
					jcrDocDto.setDocPath(docPath.add(new DocNodeFile(sName)));
					jcrDocDto.setlPartner(iPartnerIId);
					jcrDocDto.setsBelegnummer(sBelegnummer);
					jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
					jcrDocDto.setlAnleger(LPMain.getTheClient().getIDPersonal());
					jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
					jcrDocDto.setsSchlagworte(" ");
					jcrDocDto.setsName(sName);
					jcrDocDto.setsFilename(sName + ".jrprint");
					if (oInfo.getTable() != null) {
						jcrDocDto.setsTable(oInfo.getTable());
					} else {
						jcrDocDto.setsTable(" ");
					}
					jcrDocDto.setsRow(sRow);
					jcrDocDto.setsMIME(".jrprint");
					jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
					jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
					jcrDocDto.setbVersteckt(false);
					jcrDocDto.setlVersion(
							DelegateFactory.getInstance().getJCRDocDelegate().getNextVersionNumer(jcrDocDto));
					DelegateFactory.getInstance().getJCRDocDelegate().addNewDocumentOrNewVersionOfDocument(jcrDocDto);
				} else {

					// PJ21088

					if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_SYSTEM_CUD)) {

						String[] optionen = new String[] { LPMain.getTextRespectUISPr("button.ok"),
								LPMain.getTextRespectUISPr("lp.jcr.keinbelegpfad.button.lpadmin") };

						int iOption = DialogFactory.showModalDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("lp.jcr.keinbelegpfad"),
								LPMain.getTextRespectUISPr("lp.hint"), optionen, optionen[0]);

						if (iOption == 1) {
							// Archivierung deaktivieren
							DelegateFactory.getInstance().getJCRDocDelegate()
									.deaktiviereArchivierung(theClientDto.getMandant(), sName);
						}

					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("lp.jcr.keinbelegpfad"));
					}

				}
			}
		}
	}

	public void wcoVariante_actionPerformed(ActionEvent e) {
		try {
			initStandarddrucker();
		} catch (Throwable ex) {
			ex.printStackTrace();

		}
	}

	private void setzeVersandstatus(String sDruckart) throws Throwable {
		JasperPrintLP print;
		if (panelReport != null) {
			print = panelReport.getPrint();
		} else {
			print = createPrint(null);
		}
		String sBelegart = (String) print.getAdditionalInformation(JasperPrintLP.KEY_BELEGART);

		Integer iIId = (Integer) print.getAdditionalInformation(JasperPrintLP.KEY_BELEGIID);

		if (LocaleFac.BELEGART_BESTELLUNG.equals(sBelegart)) {
			DelegateFactory.getInstance().getBestellungDelegate().setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_ANFRAGE.equals(sBelegart)) {
			DelegateFactory.getInstance().getAnfrageDelegate().setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_ANGEBOT.equals(sBelegart)) {
			DelegateFactory.getInstance().getAngebotDelegate().setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_AUFTRAG.equals(sBelegart)) {
			DelegateFactory.getInstance().getAuftragDelegate().setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_LIEFERSCHEIN.equals(sBelegart)) {
			DelegateFactory.getInstance().getLsDelegate().setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}

		if (LocaleFac.BELEGART_RECHNUNG.equals(sBelegart)) {
			DelegateFactory.getInstance().getRechnungDelegate().setzeVersandzeitpunktAufJetzt(iIId, sDruckart);
		}
		if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(sBelegart)) {
			DelegateFactory.getInstance().getEingangsrechnungDelegate().updateEingangsrechnungGedruckt(iIId);
		}
		if (jpaPanelReportIf instanceof IAktiviereBelegReport) {
			IAktiviereBelegReport rep = (IAktiviereBelegReport) jpaPanelReportIf;
			rep.refreshPanelInBackground();
		}

		Integer fasessionIId = (Integer) print.getAdditionalInformation(ArtikelReportFac.ADD_INFO_FASESSION);
		if (fasessionIId != null) {
			DelegateFactory.getInstance().getFehlmengeDelegate().schliesseAufgeloesteFehlmengenSessionAb(fasessionIId);
		}

	}

	private boolean checkValuesForChanges() {
		if (reportkonfCache == null)
			return true;
		ReportkonfDto[] actualKonf = null;
		try {
			actualKonf = getKomponentenEinesReports();
		} catch (Throwable e) {
			return true; // Vorsichtshalber true zurueckgeben
		}
		if (actualKonf.length != reportkonfCache.size())
			return true;
		for (int i = 0; i < actualKonf.length; i++) {
			ReportkonfDto actual = actualKonf[i];
			ReportkonfDto cache = reportkonfCache.get(i);
			if ((actual.getCKey() == null) != (cache.getCKey() == null)) {
				return true;
			}
			if (actual.getCKey() != null && !actual.getCKey().equals(cache.getCKey())) {
				return true;
			}
		}
		return false;
	}

	private void saveValuesToCache() {
		try {
			reportkonfCache = Arrays.asList(getKomponentenEinesReports());
		} catch (Throwable e) {
			handleException(e, false);
		}
	}

	private boolean isRefreshNecessary() {
		AbstractButton refreshBtn = getHmOfButtons().get(ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW).getButton();

		if (refreshBtn.getBackground().equals(Color.blue)) {
			return true;

		} else {
			return false;
		}
	}

	private void setRefreshNecessary(boolean necessary) {
		AbstractButton refreshBtn = getHmOfButtons().get(ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW).getButton();
		refreshBtn.setOpaque(true);
		// refreshBtn.setBorderPainted(necessary);
		refreshBtn.setBackground(necessary ? Color.blue : UIManager.getColor("Button.background"));
	}

	@Override
	public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
		if ("focusOwner".equals(evt.getPropertyName()) && evt.getOldValue() instanceof Component) {
			if (SwingUtilities.isDescendingFrom((Component) evt.getOldValue(), (Component) jpaPanelReportIf)) {
				updateRefreshButton();
			}
		}
	}

	private void updateRefreshButton() {
		boolean changed = checkValuesForChanges();
		setRefreshNecessary(changed);
	}

	public PanelReportIfJRDS getPanelReportIfJRDS() {
		return jpaPanelReportIf;
	}

	private boolean shouldDoActionPrint() throws Throwable {
		return shouldDoActionIfEmailPanelIsVisible("lp.drucken.frage.beimailversand.drucken");
	}

	private boolean shouldDoActionSave() throws Throwable {
		return shouldDoActionIfEmailPanelIsVisible("lp.drucken.frage.beimailversand.speichern");
	}

	private boolean shouldDoActionIfEmailPanelIsVisible(String tokenQuestion) throws Throwable {
		PanelVersandEmail panelEmail = getPanelVersandEmail(false);
		if (panelEmail == null || !panelEmail.isVisible()) {
			return true;
		}
		return DialogFactory.showModalJaNeinDialog(getInternalFrame(), LPMain.getTextRespectUISPr(tokenQuestion));
	}

	private JasperPrintLP mergeWithPrintTypeReport(JasperPrintLP printLP, String druckType) throws Throwable {
		printLP.setPrintLP(mergeWithPrintTypeReport(printLP.getPrint(), druckType));
		return printLP;
	}

	private JasperPrint mergeWithPrintTypeReport(JasperPrint print, String druckType) throws Throwable {
		if (jpaPanelReportIf instanceof IDruckTypeReport) {
			print = DelegateFactory.getInstance().getSystemReportDelegate().mergeWithPrintTypePrint(print, druckType);
		}
		return print;
	}

	private class OriginalCopiesCtrl {
		private List<JRPrintPage> pagesOriginal;
		private List<JRPrintPage> pagesCopies;

		public OriginalCopiesCtrl(JasperPrint print, Integer kopien) {
			pagesOriginal = new ArrayList<JRPrintPage>();
			pagesCopies = new ArrayList<JRPrintPage>();
			split(print, kopien);
		}

		private void split(JasperPrint print, Integer kopien) {
			List<JRPrintPage> pagesAll = print.getPages();
			int size = print.getPages().size();
			if (kopien == null || kopien == 0 || size % (kopien + 1) != 0) {
				pagesOriginal.addAll(print.getPages());
				return;
			}

			int pagesPerCopy = size / (kopien + 1);
			if (pagesPerCopy < 1 || pagesPerCopy >= size) {
				pagesOriginal.addAll(print.getPages());
				return;
			}

			pagesOriginal.addAll(pagesAll.subList(0, pagesPerCopy));
			pagesCopies.addAll(pagesAll.subList(pagesPerCopy, size));
		}

		public void setPagesOriginal(JasperPrintLP printLP) {
			JasperPrint print = printLP.getPrint();
			print.getPages().clear();
			print.getPages().addAll(pagesOriginal);
		}

		public void setPagesCopies(JasperPrintLP printLP) {
			JasperPrint print = printLP.getPrint();
			print.getPages().clear();
			print.getPages().addAll(pagesCopies);
		}

		public void setPagesAll(JasperPrintLP printLP) {
			JasperPrint print = printLP.getPrint();
			print.getPages().clear();
			print.getPages().addAll(pagesOriginal);
			print.getPages().addAll(pagesCopies);
		}
	}

	private class ReportToolsPanelButtonCreator {

		public String[] getToolsPanelButtons() throws Throwable {
			List<String> buttonsIUse = new ArrayList<String>();

			if (getOptions().isNurVorschau()) {
				addButtonsNurVorschau(buttonsIUse);

			} else if (getOptions().isNurVorschauMitDrucken()) {
				addButtonsNurVorschauMitDrucken(buttonsIUse);

			} else {
				addButtonsAll(buttonsIUse);
			}

			return buttonsIUse.toArray(new String[] {});
		}

		private void addButtonsNurVorschau(List<String> buttonsIUse) {
			addRefresh(buttonsIUse);
		}

		private void addButtonsNurVorschauMitDrucken(List<String> buttonsIUse) throws Throwable {
			addRefresh(buttonsIUse);
			addFullscreen(buttonsIUse);
			addPrint(buttonsIUse);
		}

		private void addButtonsAll(List<String> buttonsIUse) throws Throwable {
			addRefresh(buttonsIUse);
			addFullscreen(buttonsIUse);
			addPrint(buttonsIUse);
			if (getOptions().isMitEmailFax()) {
				addMail(buttonsIUse);
				addFax(buttonsIUse);
			}
			addSave(buttonsIUse);
			addCsvExport(buttonsIUse);
			addCopyToClipboard(buttonsIUse);
		}

		private void addRefresh(List<String> buttonsIUse) {
			createAndSaveButton("/com/lp/client/res/refresh.png",
					LPMain.getTextRespectUISPr("lp.drucken.reportaktualisieren"),
					ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), null);
			buttonsIUse.add(ACTION_SPECIAL_REPORTKRITERIEN_PREVIEW);
		}

		private void addFullscreen(List<String> buttonsIUse) throws Throwable {

			// SP8659 Wenn man im Beleg kein Schreibrecht hat, man darf die Preise jedoch
			// trotzdem sehen, dann darf man den Beleg auch drucken
			String recht = null;
			if (getInternalFrame() instanceof InternalFrameAngebot) {

				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {
					recht = RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF;
				} else {
					recht = RechteFac.RECHT_ANGB_ANGEBOT_CUD;
				}

			} else if (getInternalFrame() instanceof InternalFrameAuftrag) {

				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {
					recht = RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF;
				} else {
					recht = RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF;
				}

			} else if (getInternalFrame() instanceof InternalFrameLieferschein) {
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {
					recht = RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF;
				} else {
					recht = RechteFac.RECHT_LS_LIEFERSCHEIN_CUD;
				}
			} else if (getInternalFrame() instanceof InternalFrameRechnung) {
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {
					recht = RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF;
				} else {
					recht = RechteFac.RECHT_RECH_RECHNUNG_CUD;
				}
			} else if (getInternalFrame() instanceof InternalFrameBestellung) {

				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF)) {
					recht = RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF;
				} else {
					recht = RechteFac.RECHT_BES_BESTELLUNG_CUD;
				}

			}

			createAndSaveButton("/com/lp/client/res/printer_view.png",
					LPMain.getTextRespectUISPr("lp.drucken.vorschau"), ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN,
					KeyStroke.getKeyStroke('R', java.awt.event.InputEvent.CTRL_MASK), recht);
			buttonsIUse.add(ACTION_SPECIAL_REPORTKRITERIEN_FULLSCREEN);
		}

		private void addPrint(List<String> buttonsIUse) {
			createAndSaveButton("/com/lp/client/res/printer.png", LPMain.getTextRespectUISPr("lp.drucken.drucken"),
					ACTION_SPECIAL_DRUCKEN, KeyStroke.getKeyStroke('P', java.awt.event.InputEvent.CTRL_MASK), null);
			buttonsIUse.add(ACTION_SPECIAL_DRUCKEN);
		}

		private void addSave(List<String> buttonsIUse) {
			createAndSaveButton("/com/lp/client/res/disk_blue.png", LPMain.getTextRespectUISPr("lp.drucken.speichern"),
					ACTION_SPECIAL_SAVE, KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK), null);
			buttonsIUse.add(ACTION_SPECIAL_SAVE);
		}

		private void addCsvExport(List<String> buttonsIUse) {
			createAndSaveButton("/com/lp/client/res/document_out.png",
					LPMain.getTextRespectUISPr("lp.report.csvexport"), ACTION_SPECIAL_CSV, null, null);
			buttonsIUse.add(ACTION_SPECIAL_CSV);
		}

		private void addCopyToClipboard(List<String> buttonsIUse) {
			createAndSaveButton("/com/lp/client/res/copy.png",
					LPMain.getTextRespectUISPr("lp.inzwischenablagekopieren"), ACTION_COPY_TO_CLIPBOARD, null, null);
			buttonsIUse.add(ACTION_COPY_TO_CLIPBOARD);
		}

		private void addMail(List<String> buttonsIUse) {
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAILVERSAND)) {
				createAndSaveButton("/com/lp/client/res/mail.png",
						LPMain.getTextRespectUISPr("lp.drucken.alsemailversenden"), ACTION_SPECIAL_EMAIL,
						KeyStroke.getKeyStroke('E', java.awt.event.InputEvent.CTRL_MASK),
						RechteFac.RECHT_LP_DARF_EMAIL_SENDEN);
			}
			buttonsIUse.add(ACTION_SPECIAL_EMAIL);
		}

		private void addFax(List<String> buttonsIUse) {
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_FAXVERSAND)) {
				createAndSaveButton("/com/lp/client/res/fax.png",
						LPMain.getTextRespectUISPr("lp.drucken.alsfaxversenden"), ACTION_SPECIAL_FAX,
						KeyStroke.getKeyStroke('F', java.awt.event.InputEvent.CTRL_MASK), null);
			}
			buttonsIUse.add(ACTION_SPECIAL_FAX);
		}
	}

	private boolean isLogoImmerDrucken() throws Throwable {
		return Boolean.TRUE.equals(DelegateFactory.getInstance().getParameterDelegate().getLogoImmerDrucken());
	}
}

class PanelReportKriterien_wcoVariante_actionAdapter implements ActionListener {
	private PanelReportKriterien adaptee;

	PanelReportKriterien_wcoVariante_actionAdapter(PanelReportKriterien adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoVariante_actionPerformed(e);
		try {
			adaptee.setzeEmailRechnungsempfang();
		} catch (Throwable ex) {
			adaptee.handleException(ex, false);
		}
	}
}
