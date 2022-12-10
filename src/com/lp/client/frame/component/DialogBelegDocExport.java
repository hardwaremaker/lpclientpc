package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.EventObject;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.util.HvOptional;

import net.miginfocom.swing.MigLayout;

public abstract class DialogBelegDocExport extends JDialog implements ActionListener, ItemChangedListener {
	private static final long serialVersionUID = -1089737576473316478L;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private InternalFrame internalFrame;
	private WrapperButton directoryButton;
	private WrapperTextField directoryField;
	private WrapperLabel dateLabel;
	private WrapperLabel startdateLabel;
	private WrapperLabel enddateLabel;
	private WrapperDateField startdateField;
	private WrapperDateField enddateField;
	private WrapperDateRangeController daterangePanel;
	private JPanel datesPanel;
	private WrapperButton partnerButton;
	private WrapperTextField partnerField;
	private PanelQueryFLR partnerPanelFLR;
	private WrapperButton exportButton;
	
	private HvLayout layout;
	private JPanel exportPanel;
	
	public DialogBelegDocExport(Frame owner, InternalFrame internalFrame, String title) {
		super(owner, title, true);
		this.internalFrame = internalFrame;
		initLayout();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(600, 200));
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
	}
	
	protected InternalFrame getInternalFrame() {
		return internalFrame;
	}
	
	protected String fromToken(String token) {
		return LPMain.getTextRespectUISPr(token);
	}
	
	private void initLayout() {
		directoryButton = new WrapperButton(fromToken("button.zielverzeichnis"));
		directoryButton.addActionListener(this);
		directoryField = new WrapperTextField(255);
		directoryField.setMandatoryField(true);
		dateLabel = new WrapperLabel();
		startdateLabel = new WrapperLabel(fromToken("lp.von"));
		startdateField = new WrapperDateField();
		startdateField.setMandatoryField(true);
		enddateLabel = new WrapperLabel(fromToken("lp.bis"));
		enddateField = new WrapperDateField();
		enddateField.setMandatoryField(true);
		daterangePanel = new WrapperDateRangeController(startdateField, enddateField);
		daterangePanel.doClickUp();
		datesPanel = new JPanel();
		partnerButton = new WrapperButton();
		partnerButton.addActionListener(this);
		partnerField = new WrapperTextField();
		exportButton = new WrapperButton(fromToken("button.belegeexportieren"));
		exportButton.addActionListener(this);
		
		exportPanel = new JPanel();
		layout = HvLayoutFactory.create(exportPanel, "wrap 2", "[20%,fill|80%,fill,grow]", "");

		getContentPane().setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));
		getContentPane().add(exportPanel, "wrap");

		getInternalFrame().addItemChangedListener(this);
	}
	
	public DialogBelegDocExport addDirectory() {
		return addDirectory(HvOptional.empty());
	}
	
	public DialogBelegDocExport addDirectory(HvOptional<String> path) {
		directoryField.setText(path.isPresent() ? path.get() : null);
		layout.add(directoryButton)
			.add(directoryField);
		return this;
	}
	
	public DialogBelegDocExport addReceiptStartEndDate() {
		dateLabel.setText(LPMain.getTextRespectUISPr("label.belegdatum"));
		HvLayout datesLayout = HvLayoutFactory.create(datesPanel, "", "[30px,fill|100px,fill|50px,fill|100px,fill|100px,fill,grow]", "[fill]");
		datesLayout
			.add(startdateLabel)
			.add(startdateField)
			.add(enddateLabel)
			.add(enddateField)
			.add(daterangePanel, "align left");
		
		layout.add(dateLabel)
			.add(datesPanel);
		
		return this;
	}
	
	public DialogBelegDocExport addSupplier() {
		partnerButton.setText(fromToken("button.lieferant"));
		addPartner();
		return this;
	}
	
	public DialogBelegDocExport addCustomer() {
		partnerButton.setText(fromToken("button.kunde"));
		addPartner();
		return this;
	}
	
	private void addPartner() {
		layout.add(partnerButton)
			.add(partnerField);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == partnerButton) {
				onPartnerButton();
			} else if (e.getSource() == exportButton) {
				if (validProperties()) {
					onExportButton();
				}
			} else if (e.getSource() == directoryButton) {
				onDirectoryButton();
			}
		} catch (Throwable t) {
			getInternalFrame().handleException(t, false);
		}
	}

	private void onDirectoryButton() {
		HvOptional<DirectoryFile> f = HelperClient.chooseDirectoryNew(this, getFileChooserConfigToken());
		if(f.isPresent()) {
			directoryField.setText(f.get().getDirectory().getAbsolutePath());				
		}
	}

	private boolean validProperties() {
		if (getDirectory() == null || getStartdate() == null || getEnddate() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
			return false;
		}
		return true;
	}

	protected abstract void onExportButton();
	
	protected void showDialogExportSucceeded() {
		DialogFactory.showModalInfoDialog(LPMain.getTextRespectUISPr("lp.info"),
				LPMain.getMessageTextRespectUISPr("er.export.dokumente.exporterfolgreich", getDirectory()));
	}

	protected void showDialogError(Throwable t) {
		myLogger.error("", t);
		new DialogError(LPMain.getInstance().getDesktop(), t,
				DialogError.TYPE_ERROR);
	}
	
	@Override
	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == partnerPanelFLR) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
//				doActionPersonalSelected(key);
			}
		}
	}

	private void onPartnerButton() throws Throwable {
		partnerPanelFLR = createPartnerPanelFLR();
		new DialogQuery(partnerPanelFLR);
	}

	protected abstract PanelQueryFLR createPartnerPanelFLR() throws Throwable;
	protected abstract FileChooserConfigToken getFileChooserConfigToken();
	
	@Override
	public void setVisible(boolean b) {
		layout.add(exportButton, "span, align center, wmin 100, wmax 150, gaptop 20");
		super.setVisible(b);
	}
	
	protected String getDirectory() {
		return directoryField.getText();
	}
	
	protected Date getStartdate() {
		return startdateField.getDate();
	}

	protected Date getEnddate() {
		return enddateField.getDate();
	}
	
}
