package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzServiceFac;

import net.miginfocom.swing.MigLayout;

public class DialogKontoExport extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private WrapperCheckBox nurVerwendeteKonten;
	private WrapperLabel labelPfad;
	private WrapperTextField exportPfad;
	private KontoExporter kontoExporter;

	private WrapperButton exportieren;
	private WrapperButton abbrechen;
	private InternalFrame iFrame;

	public DialogKontoExport(KontoExporter exporter, InternalFrame internalFrame) {
		super((Frame) null, createTitle(exporter), true);
		this.kontoExporter = exporter;
		this.iFrame = internalFrame;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setIconImage(null);
		Dimension size = HelperClient.getSizeFactoredDimension(350, 150);
		setSize(size);
		setMinimumSize(size);
		setResizable(false);
		jbInit();
	}

	private static String createTitle(KontoExporter exporter) {
		String kontoTyp = "";
		if(exporter.getKontotyp().equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			kontoTyp = LPMain.getTextRespectUISPr("fb.buchungsjournal.exportkonten.debitoren");
		}
		else if(exporter.getKontotyp().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
			kontoTyp = LPMain.getTextRespectUISPr("fb.buchungsjournal.exportkonten.kreditoren");
		}
		
		return LPMain.getMessageTextRespectUISPr("fb.exportdialog.titel", kontoTyp);
	}
	
	private void jbInit() {
		labelPfad = new WrapperLabel();
		labelPfad.setText(LPMain.getTextRespectUISPr("fb.exportdialog.zielpfad"));
		labelPfad.setMinimumSize(HelperClient.getSizeFactoredDimension(50));

		nurVerwendeteKonten = new WrapperCheckBox();
		nurVerwendeteKonten.setSelected(true);
		nurVerwendeteKonten.setText(LPMain.getTextRespectUISPr("fb.exportdialog.nurverwendete"));
		nurVerwendeteKonten.setHorizontalAlignment(SwingConstants.RIGHT);

		exportieren = new WrapperButton();
		abbrechen = new WrapperButton();

		exportieren.setText(LPMain.getTextRespectUISPr("fb.buchungsjournal.export.exportieren"));
		abbrechen.setText(LPMain.getTextRespectUISPr("lp.abbrechen"));

		exportieren.addActionListener(this);
		abbrechen.addActionListener(this);

		exportPfad = new WrapperTextField();
		exportPfad.setEditable(false);
		try {
			exportPfad.setText(kontoExporter.getExportPath());
		} catch (Throwable e) {
		}

		setLayout(new MigLayout("fill", "[][grow 100]"));
		add(labelPfad, "growx");
		add(exportPfad, "growx, wrap");

		add(nurVerwendeteKonten, "growx, spanx 2, align right, wrap");
		add(exportieren, "skip, split 2, growx");
		add(abbrechen, "wrap, growx");

		setLocationRelativeTo(iFrame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		if (e.getSource() == exportieren) {
			try {
				kontoExporter.exportAndSaveKonten(iFrame, nurVerwendeteKonten.isSelected());
			} catch (Throwable e1) {
			}
		} else if (e.getSource() == abbrechen) {
		}
		this.dispose();
	}

}
