
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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.util.GotoHelper;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerPanel;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JRHyperlinkListener;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um das Anzeigen von Reports.</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungs: Martin Bluehweis dd.mm.05
 * </p>
 * 
 * @todo MB->MB nach dem Klick auf Speichern soll pdf ausgew&auml;hlt sein. PJ
 *       5159
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class ReportViewer extends JRViewer implements JRHyperlinkListener {

	private static final long serialVersionUID = -3537432347040140083L;

	public final static int DEFAULT_ZOOM = 75;

	protected javax.swing.JButton btnPDF = new javax.swing.JButton();

	ReportViewer(String fileName, boolean isXML) throws JRException {
		super(fileName, isXML);
		setDefaults();
	}

	public void loadReport(JasperPrint jrPrint) {
		if (jrPrint != null) {
			ReportClientPatch reportPatch = new ReportClientPatch(jrPrint);
			reportPatch.apply();
			try {

				reportPatch.checkContent(jrPrint, DefaultJasperReportsContext.getInstance());
			} catch (ExceptionLP e) {
				new DialogError(LPMain.getInstance().getDesktop(), e, DialogError.TYPE_ERROR);
				return;
			}
		}

		viewerContext.loadReport(jrPrint);
	}

	public void refreshPage() {
		viewerContext.refreshPage();
	}

	private void setDefaults() {
		this.setLocale(LPMain.getInstance().getUISprLocale());
		// Tooltiptexte der buttons
		getBtnPrint().setToolTipText(LPMain.getTextRespectUISPr("lp.report.print"));
		getBtnFirst().setToolTipText(LPMain.getTextRespectUISPr("lp.report.firstpage"));
		// getBtnFirst().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
		// getKeyStroke(KeyEvent.VK_HOME, 0), "lp.report.firstpage");
		getBtnPrevious().setToolTipText(LPMain.getTextRespectUISPr("lp.report.previouspage"));
		// getBtnPrevious().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
		// getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "lp.report.previouspage");
		getBtnNext().setToolTipText(LPMain.getTextRespectUISPr("lp.report.nextpage"));
		// getBtnNext().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
		// getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "lp.report.nextpage");
		getBtnLast().setToolTipText(LPMain.getTextRespectUISPr("lp.report.lastpage"));
		// getBtnLast().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.
		// getKeyStroke(KeyEvent.VK_END, 0), "lp.report.lastpage");
		getTxtGoto().setToolTipText(LPMain.getTextRespectUISPr("lp.report.gotopage"));
		getBtnActualSize().setToolTipText(LPMain.getTextRespectUISPr("lp.report.actualsize"));
		getBtnFitPage().setToolTipText(LPMain.getTextRespectUISPr("lp.report.fitpage"));
		getBtnFitWidth().setToolTipText(LPMain.getTextRespectUISPr("lp.report.fitwidth"));
		getBtnZoomIn().setToolTipText(LPMain.getTextRespectUISPr("lp.report.zoomin"));
		getBtnZoomOut().setToolTipText(LPMain.getTextRespectUISPr("lp.report.zoomout"));
		getCmbZoom().setToolTipText(LPMain.getTextRespectUISPr("lp.report.zoomratio"));

		setBackgroundBorderColors();

		btnPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/lp/client/res/pdf.png")));
		btnPDF.setToolTipText(LPMain.getTextRespectUISPr("lp.print.pdfvorschau"));
		btnPDF.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnPDF.setMaximumSize(new java.awt.Dimension(23, 23));
		btnPDF.setMinimumSize(new java.awt.Dimension(23, 23));
		btnPDF.setPreferredSize(new java.awt.Dimension(23, 23));

		// btnPDF.addKeyListener(keyNavigationListener);
		tlbToolBar.add(btnPDF);

		if (getComponents()[1] instanceof JRViewerPanel) {
			JRViewerPanel vp = (JRViewerPanel) getComponents()[1];
			vp.addHyperlinkListener(this);
		}
	}

	public void removePDFButton() {
		tlbToolBar.remove(btnPDF);
	}

	void btnSearchActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_btnZoomOutActionPerformed
	{
		int i = 0;
	}

	private void setBackgroundBorderColors() {
		Color bgColor = Defaults.getInstance().getToggleButtonBgColor();
		Color borderColor = Defaults.getInstance().getToggleButtonBorderColor();
		HelperClient.setButtonBackgroundBorderColor(getBtnFirst(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnLast(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnNext(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnPrevious(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnPrint(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnReload(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnSave(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnZoomIn(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnZoomOut(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnActualSize(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnFitPage(), bgColor, borderColor);
		HelperClient.setButtonBackgroundBorderColor(getBtnFitWidth(), bgColor, borderColor);
	}

	ReportViewer(InputStream is, boolean isXML) throws JRException {
		super(is, isXML);
		setDefaults();
	}

	public ReportViewer(JasperPrint jrPrint) throws JRException {
		super(jrPrint);
		setDefaults();
	}

	void setZoom(int zoom) {

		getCmbZoom().setSelectedItem(zoom + "%");
	}

	JToggleButton getBtnActualSize() {
		return getToggleButton("actualsize.GIF");
	}

	JButton getBtnFirst() {
		return getButton("first.GIF");
	}

	JToggleButton getBtnFitPage() {
		return getToggleButton("fitpage.GIF");
	}

	JToggleButton getBtnFitWidth() {
		return getToggleButton("fitwidth.GIF");
	}

	JButton getBtnLast() {
		return getButton("last.GIF");
	}

	JButton getBtnNext() {
		return getButton("next.GIF");
	}

	JButton getBtnPrevious() {
		return getButton("previous.GIF");
	}

	public JButton getButton(String s) {

		JRViewerToolbar tb = tlbToolBar;

		for (int i = 0; i < tb.getComponents().length; i++) {
			Component c = tb.getComponents()[i];
			if (c instanceof JButton) {
				JButton jbu = (JButton) tb.getComponents()[i];

				if (jbu.getIcon() instanceof ImageIcon) {
					ImageIcon icon = (ImageIcon) jbu.getIcon();
					if (icon.getDescription() != null) {
						String description = icon.getDescription();

						if (description.endsWith(s)) {
							return jbu;
						}

					}

				}

			}

		}
		return new JButton();
	}

	public JToggleButton getToggleButton(String s) {

		JRViewerToolbar tb = tlbToolBar;

		for (int i = 0; i < tb.getComponents().length; i++) {
			Component c = tb.getComponents()[i];
			if (c instanceof JToggleButton) {
				JToggleButton jbu = (JToggleButton) tb.getComponents()[i];

				if (jbu.getIcon() instanceof ImageIcon) {
					ImageIcon icon = (ImageIcon) jbu.getIcon();
					if (icon.getDescription() != null) {
						String description = icon.getDescription();

						if (description.endsWith(s)) {
							return jbu;
						}

					}

				}

			}

		}
		return new JToggleButton();
	}

	JButton getBtnPrint() {
		return getButton("print.GIF");
	}

	JButton getBtnReload() {
		return getButton("reload.GIF");
	}

	JButton getBtnSave() {
		return getButton("save.GIF");
	}

	JButton getBtnZoomIn() {
		return getButton("zoomin.GIF");
	}

	JButton getBtnZoomOut() {
		return getButton("zoomout.GIF");
	}

	JTextField getTxtGoto() {

		JRViewerToolbar tb = tlbToolBar;

		for (int i = 0; i < tb.getComponents().length; i++) {
			Component c = tb.getComponents()[i];
			if (c instanceof JTextField) {
				JTextField txt = (JTextField) tb.getComponents()[i];

				return txt;

			}

		}
		return new JTextField();

	}

	
	public float getZoom() {
		return viewerContext.getZoom();
	}
	
	public JComboBox getCmbZoom() {

		JRViewerToolbar tb = tlbToolBar;

		for (int i = 0; i < tb.getComponents().length; i++) {
			Component c = tb.getComponents()[i];
			if (c instanceof JComboBox) {
				JComboBox zoom = (JComboBox) tb.getComponents()[i];

				return zoom;

			}

		}
		return new JComboBox();

	}

	public void cleanup() {
	}

	JButton getBtnPDF() {
		return btnPDF;
	}

	@Override
	public void gotoHyperlink(JRPrintHyperlink arg0) {

		if (arg0.getHyperlinkTypeValue().equals(HyperlinkTypeEnum.CUSTOM)
				&& arg0.getLinkType().toUpperCase().equals(GotoHelper.HYPERLINK_TYPE_GOTO)) {

			List<JRPrintHyperlinkParameter> params = arg0.getHyperlinkParameters().getParameters();

			Object oWhereToGo = null;
			Object oKey = null;
			Object oDetailKey = null;

			for (int i = 0; i < params.size(); i++) {
				if (params.get(i).getName().equals(GotoHelper.PARAMETER_WHERE_TO_GO)) {
					oWhereToGo = params.get(i).getValue();
				}
				if (params.get(i).getName().equals(GotoHelper.PARAMETER_KEY)) {
					oKey = params.get(i).getValue();
				}
				if (params.get(i).getName().equals(GotoHelper.PARAMETER_DETAILKEY)) {
					oDetailKey = params.get(i).getValue();
				}
			}

			if (oWhereToGo != null && oWhereToGo instanceof Integer && oKey != null) {
				WrapperGotoButton wbu = new WrapperGotoButton((Integer) oWhereToGo);
				wbu.setOKey(oKey);
				wbu.setDetailKey(oDetailKey);
				try {
					wbu.actionPerformed(new ActionEvent(this, 0, WrapperGotoButton.ACTION_GOTO));
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (arg0.getHyperlinkTypeValue().equals(HyperlinkTypeEnum.REFERENCE)
				&& arg0.getHyperlinkReference() != null) {
			try {
				String link = arg0.getHyperlinkReference();
				int i = link.indexOf("://");
				URI uri = new URI((i < 0 ? "http://" : "") + link.trim());
				java.awt.Desktop.getDesktop().browse(uri);
			} catch (URISyntaxException ex1) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.fehlerhafteurl"));
			} catch (IOException ex1) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), ex1.getMessage());
			}
		}

		/*
		 * WrapperGotoButton wbu = new WrapperGotoButton(
		 * WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL); Field[] fields =
		 * wbu.getClass().getDeclaredFields(); if
		 * (arg0.getHyperlinkTypeValue().equals(HyperlinkTypeEnum.CUSTOM)) { for (int i
		 * = 0; i < fields.length; i++) { if (fields[i].getType().equals(int.class) &&
		 * fields[i].getName().equals( arg0.getHyperlinkReference())) { try {
		 * fields[i].getInt(wbu);
		 * 
		 * List<JRPrintHyperlinkParameter> params = arg0
		 * .getHyperlinkParameters().getParameters();
		 * 
		 * if (params.size() > 0) {
		 * 
		 * if (params.get(0).getValue() != null) {
		 * 
		 * if (params .get(0) .getValueClass() .equals(java.lang.String.class
		 * .getName())) {
		 * 
		 * wbu.setOKey(params.get(0).getValue()); } else if (params .get(0)
		 * .getValueClass() .equals(java.lang.Integer.class .getName())) {
		 * 
		 * wbu.setOKey(new Integer(params.get(0) .getValue() + "")); } }
		 * wbu.actionPerformed(new ActionEvent(this, 0, WrapperGotoButton.ACTION_GOTO));
		 * } else { // FEHLER_KEIN_PARAMETER
		 * 
		 * }
		 * 
		 * } catch (IllegalArgumentException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IllegalAccessException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } wbu = new
		 * WrapperGotoButton( WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);
		 * 
		 * int u = 0; } } } else { super.gotoHyperlink(arg0); }
		 */

	}

}
