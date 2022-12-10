package com.lp.client.finanz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayout.Align;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.util.Facade;

public class DialogSepakontoauszugImport extends JDialog implements ActionListener, IDataUpdateListener {

	private static final long serialVersionUID = -6438524612842729623L;

	private ISepakontoauszugImportController controller;
	
	private JPanel workingPanel;
	private WrapperLabel wlBankverbindung;
	private WrapperTextField wtfBankverbindung;
	private WrapperLabel wlSepaVerzeichnis;
	private WrapperTextField wtfSepaVerzeichnis;
//	private WrapperLabel wlCamtFormat;
//	private WrapperComboBox wcbCamtFormat;
//	private WrapperLabel wlAuszugsnummer;
//	private JTextField wtfAuszugsnummer;
//	private WrapperLabel wlLetzteBuchung;
//	private WrapperTextField wtfLetzteBuchung;
	private WrapperTextArea wtaErrors;
	private JScrollPane jScrollPaneMessages;
	
	private WrapperButton wbuImport;
	private WrapperButton wbuCancel;
	
	public DialogSepakontoauszugImport(Frame owner, String title, boolean modal, ISepakontoauszugImportController controller) {
		super(owner, title, modal);
		this.controller = controller;
		this.controller.registerDataUpdateListener(this);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
	}
	
	private void jbInit() {
		setPreferredSize(new Dimension(1000, 400));
		
		wlBankverbindung = new WrapperLabel(LPMain.getTextRespectUISPr("finanz.bankverbindung"));
		wtfBankverbindung = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfBankverbindung.setEditable(false);
		wtfBankverbindung.setText(getController().getBankverbindungName());
		
		wlSepaVerzeichnis = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.sepaverzeichnis"));
		wtfSepaVerzeichnis = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfSepaVerzeichnis.setEditable(false);
		wtfSepaVerzeichnis.setText(getController().getSepaVerzeichnis());

//		wlCamtFormat = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.camtformat"));
//		wcbCamtFormat = new WrapperComboBox();
//		wlAuszugsnummer = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.auszugsnummer"));
//
//		wcbCamtFormat.addAll(getController().getCamtFormate());
//		wcbCamtFormat.setSelectedIndex(getController().getCamtFormatSelection());
//		wcbCamtFormat.addActionListener(this);
		
//		wtfAuszugsnummer = new JTextField();
//		wtfAuszugsnummer.setEditable(true);
//		wtfAuszugsnummer.setHorizontalAlignment(SwingConstants.RIGHT);
//		wtfAuszugsnummer.setBorder(Defaults.getInstance().getMandatoryFieldBorder());
//		wtfAuszugsnummer.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				String value = wtfAuszugsnummer.getText();
//				if (value != null && value.matches("\\d+")) {
//					getController().setAuszugsnummer(Integer.parseInt(value));
//				} else {
//					wtfAuszugsnummer.setText(null);
//				}
//			}
//		});;
//		
//		wlLetzteBuchung = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.letztebuchung"));
//		wtfLetzteBuchung = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
//		wtfLetzteBuchung.setEditable(false);
//		wtfLetzteBuchung.setText(getController().getTextLetzteBuchung());
		
		wtaErrors = new WrapperTextArea();
		wtaErrors.setEditable(false);
//		wtaErrors.setBorder(BorderFactory.createEtchedBorder());
		wtaErrors.setBackground(Color.WHITE);
		jScrollPaneMessages = new JScrollPane(wtaErrors);

		wbuImport = new WrapperButton(LPMain.getTextRespectUISPr("finanz.sepakontoauszug.import.dialog.button.importieren"));
		wbuImport.addActionListener(this);
		wbuImport.setVisible(true);
		wbuImport.setEnabled(false);
		
		wbuCancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbuCancel.addActionListener(this);
		wbuCancel.setVisible(true);
		JPanel buttonPanel = new JPanel();
		HvLayout hvLayoutButtons = HvLayoutFactory.create(buttonPanel, "wrap 2");
		hvLayoutButtons.addAligned(Align.LEFT, wbuImport, 100);
		hvLayoutButtons.addAligned(Align.RIGHT, wbuCancel, 100);

		workingPanel = new JPanel();
		this.add(workingPanel);
		HvLayout hvLayout = HvLayoutFactory.create(workingPanel, 
				"wrap 5", 
				"[15%,fill|10%,fill|60%,fill|15%,fill]", 
				"[fill|fill|60%,fill|10%,fill]");
		hvLayout.add(wlBankverbindung, "cell 1 0");
		hvLayout.add(wtfBankverbindung).wrap();
		hvLayout.add(wlSepaVerzeichnis, "cell 1 1");
		hvLayout.add(wtfSepaVerzeichnis).wrap();
//		hvLayout.add(wlCamtFormat, "cell 1 2");
//		hvLayout.add(wcbCamtFormat).wrap();
//		hvLayout.add(wlAuszugsnummer, "cell 1 3");
//		hvLayout.add(wtfAuszugsnummer).wrap();
//		hvLayout.add(wlLetzteBuchung, "cell 1 4");
//		hvLayout.add(wtfLetzteBuchung).wrap();
		hvLayout.add(jScrollPaneMessages).spanAndWrap(5);
		hvLayout.add(buttonPanel, "cell 3 3, al right").span(2);
		
		dataUpdated();
	}
	
	private ISepakontoauszugImportController getController() {
		return controller;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == wbuImport) {
			actionImport();
		}
		if (source == wbuCancel) {
			setVisible(false);
		}
//		if (source == wcbCamtFormat) {
//			getController().setSelectedCamtFormat(wcbCamtFormat.getSelectedIndex());
//		}
		
	}

	private void actionImport() {
		getController().actionImport();
		wbuImport.setEnabled(false);
		wbuCancel.setText(LPMain.getTextRespectUISPr("button.ok"));
		wtaErrors.setText(getController().getImportMessages());
	}

	@Override
	public void dataUpdated() {
		if (getController().shouldCloseDialog()) {
			setVisible(false);
			return;
		}
		wbuImport.setEnabled(getController().isImportPossible());
		
//		boolean bShowAuszugsnummer = getController().shouldShowAuszugsnummer();
//		wtfAuszugsnummer.setVisible(bShowAuszugsnummer);
//		wlAuszugsnummer.setVisible(bShowAuszugsnummer);
//		wtfLetzteBuchung.setVisible(bShowAuszugsnummer);
//		wlLetzteBuchung.setVisible(bShowAuszugsnummer);
	}
}
