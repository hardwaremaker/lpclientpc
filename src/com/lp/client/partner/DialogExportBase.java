package com.lp.client.partner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayout.Align;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.IFileController;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;

public abstract class DialogExportBase extends JDialog implements ActionListener {

	private static final long serialVersionUID = 9103228612761650138L;
	
	private IFileController fileController;

	protected WrapperButton wbuExport = new WrapperButton();
	private WrapperButton wbuCancel = new WrapperButton();
	private WrapperButton wbuOkay = new WrapperButton();
	protected WrapperLabel wlaVerify = new WrapperLabel();
	private WrapperLabel wlaTotalExports = new WrapperLabel();
	private WrapperLabel wlaErrorCounts = new WrapperLabel();
	private WrapperLabel wlaGoodExports = new WrapperLabel();
	private JPanel panelExport = new JPanel();
	private JTable tableResults;
	private JScrollPane panelTableResults;
	private HvLayout hvLayout;
	private JPanel optionalPanel = new JPanel();

	private String cFilenameSuggestion;
	private int totalCounts;
	private int errorCounts;

	public DialogExportBase(Frame owner, String title, boolean modal,
			IFileController fileController) {
		super(owner, title, modal);
		if (null == fileController) throw new IllegalArgumentException("controller == null");
		this.fileController = fileController;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
		
	}
	
	protected abstract TableModel getTableModel();
	protected abstract void actionExport();
	protected abstract void actionVerify();

	protected void updateOptionalPanel(JPanel panel) {
		optionalPanel.removeAll();
		if (panel != null) {
			optionalPanel.add(panel);
		}
		optionalPanel.revalidate();
		optionalPanel.updateUI();
	}
	
	private void jbInit() {
		initExportLabel(wlaVerify, LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
		initTableResults();
		
		HvLayout hvLayoutOptional = HvLayoutFactory.create(optionalPanel, "", "fill,grow", "fill,grow");
		hvLayout = HvLayoutFactory.create(panelExport, "hidemode 3", "fill,grow", "");
		hvLayout.addAligned(Align.LEFT, wlaVerify).wrap()
			.add(optionalPanel).wrap()
			.add(panelTableResults);
		
		initExportLabel(wlaTotalExports);
		initExportLabel(wlaErrorCounts);
		initExportLabel(wlaGoodExports);
		
		JPanel panelInfo = new JPanel();
		HvLayout hvLayoutInfo = HvLayoutFactory.create(panelInfo, "", "fill,grow", "");
		hvLayoutInfo.addAligned(Align.LEFT, wlaTotalExports).wrap()
			.addAligned(Align.LEFT, wlaErrorCounts).wrap()
			.addAligned(Align.LEFT, wlaGoodExports).wrap();
		
		initButton(wbuExport, getTextImportExportButton(), true, false);
		initButton(wbuOkay, LPMain.getTextRespectUISPr("button.ok"), false, true);
		initButton(wbuCancel, LPMain.getTextRespectUISPr("lp.abbrechen"), true, true);
		
		JPanel panelButton = new JPanel();
		HvLayout hvLayoutButtons = HvLayoutFactory.create(panelButton);
		hvLayoutButtons.add(wbuExport, "", 100)
			.add(wbuOkay, "", 100)
			.add(wbuCancel, "", 100);
		
//		getContentPane().setLayout(new GridBagLayout());
//		getContentPane().add(panelExport,
//               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, 
//                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		getContentPane().add(panelInfo,
//                new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
//                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		getContentPane().add(panelButton,
//                new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, 
//                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		getContentPane().setLayout(new MigLayout("", "[fill,grow]", "[fill,grow|10%|10%]"));
		getContentPane().add(panelExport, "wrap");
		getContentPane().add(panelInfo, "wrap");
		getContentPane().add(panelButton, "wrap");
		
	}
	
	protected void setTextLabelVerify(String text) {
		wlaVerify.setText(text);
	}
	
	protected void setTextButtonImprtExportButton(String text) {
		wbuExport.setText(text);
	}
	
	protected String getTextImportExportButton() {
		return LPMain.getTextRespectUISPr("artikel.vendidata.export.exportieren");
	}
	
	private void initTableResults() {
		tableResults = new JTable(getTableModel());
		panelTableResults = new JScrollPane(tableResults);
		panelTableResults.setPreferredSize(new Dimension(1000, 250));
	}
	
	protected void setPreferredColumnWidth(Integer[] preferredSizes) {
		for (int i = 0; i < preferredSizes.length; i++) {
			TableColumn col = tableResults.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredSizes[i]);
		}
	}
	
	private void initButton(WrapperButton wb, String text, boolean visible, boolean enable) {
		wb.setText(text);
		wb.setVisible(visible);
		wb.setEnabled(enable);
		wb.addActionListener(this);
	}
	
	private void initExportLabel(WrapperLabel wl) {
		initExportLabel(wl, null);
	}

	private void initExportLabel(WrapperLabel wl, String text) {
		wl.setText(text);
		wl.setHorizontalAlignment(SwingConstants.LEFT);
	}
	
	protected void actionUIVerify() {
		wlaVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
		wbuExport.setEnabled(false);
		invalidate();
	}
	
	protected void actionUIVerificationDone(boolean bVerificationSucceeded) {
		wlaVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.geprueft")) ;
		setFieldsForStats(false);

		wbuExport.setEnabled(bVerificationSucceeded);
	}
	
	protected void actionUIExport() {
//		chooseFile();
		wlaVerify = new WrapperLabel(getTextExportImportLaeuft());
		tableResults.revalidate();
		invalidate();
	}
	
	protected String getTextExportImportLaeuft() {
		return LPMain.getTextRespectUISPr("artikel.vendidata.export.exportlaeuft");
	}

	protected void actionUIExportDone(boolean bExportSucceeded) {
		wbuExport.setVisible(false);
		wbuCancel.setVisible(false);
		wbuOkay.setVisible(true);
		if (bExportSucceeded) {
			wlaVerify.setText(getTextExportImportDurchgefuehrt()) ;
			setFieldsForStats(true);
		}
		invalidate();
	}
	
	protected String getTextExportImportDurchgefuehrt() {
		return LPMain.getTextRespectUISPr("artikel.vendidata.export.exportiert");
	}
	
	protected String getFilenameSuggestion() {
		return cFilenameSuggestion == null ? "" : cFilenameSuggestion;
	}

	protected void setFilenameSuggestion(String cFilenameSuggestion) {
		this.cFilenameSuggestion = cFilenameSuggestion;
	}

	protected void chooseFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setLocale(this.getLocale());
		fileChooser.updateUI();
		fileChooser.setSelectedFile(new File(getFilenameSuggestion()));
		int retValue = fileChooser.showSaveDialog(this);

		if (retValue == JFileChooser.APPROVE_OPTION) {
			fileController.setFile(fileChooser.getSelectedFile());
		}
	}
	
	protected void showStats(boolean bShow) {
		wlaTotalExports.setVisible(false);
		wlaErrorCounts.setVisible(false);
		wlaGoodExports.setVisible(false);
	}
	
	private void setFieldsForStats(boolean exported) {
		if (getTotalCounts() == 1) {
			wlaTotalExports.setText(getTextEinDatensatzGefunden());
		} else {
			wlaTotalExports.setText(getTextMehrereDatensaetzeGefunden());
		}
		
		if(getErrorCounts() > 0) {
			String msg = LPMain.getMessageTextRespectUISPr(
					getErrorCounts() > 1 
						? "dialog.export.errors" 
						: "dialog.export.oneerror", new Object[]{getErrorCounts()});
			wlaErrorCounts.setText(msg) ;
			wlaErrorCounts.setForeground(Color.RED) ;
		} else {
			wlaErrorCounts.setText(LPMain.getTextRespectUISPr("dialog.export.noerrors")) ;
			wlaErrorCounts.setForeground(new Color(0, 204, 102)) ;
		}
		
		if (exported) {
			if (getTotalCounts() == 1) {
				wlaGoodExports.setText(getTextEinDatensatzExportiert());
			} else  {
				wlaGoodExports.setText(getTextMehrereDatensaetzeExportiert());
			}
		} else {
			wlaGoodExports.setText("");
		}
	}
	
	protected String getTextEinDatensatzGefunden() {
		return LPMain.getMessageTextRespectUISPr("dialog.export.singletotalcounts");
	}
	
	protected String getTextMehrereDatensaetzeGefunden() {
		return LPMain.getMessageTextRespectUISPr("dialog.export.multipletotalcounts", 
						new Object[]{getTotalCounts()});
	}
	
	protected String getTextEinDatensatzExportiert() {
		return LPMain.getTextRespectUISPr("dialog.export.singlesaved");
	}
	
	protected String getTextMehrereDatensaetzeExportiert() {
		return LPMain.getMessageTextRespectUISPr("dialog.export.multiplesaved", new Object[]{getTotalCounts(), getFilename()});
	}

	private String getFilename() {
		File file = fileController.getFile();
		return file == null ? "" : file.getName();
	}

	protected int getTotalCounts() {
		return totalCounts;
	}

	protected void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}

	protected int getErrorCounts() {
		return errorCounts;
	}

	protected void setErrorCounts(int errorCounts) {
		this.errorCounts = errorCounts;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (wbuExport == source) {
			actionExport();
		} else if (wbuCancel == source || wbuOkay == source) {
			setVisible(false);
		}
	}

}
