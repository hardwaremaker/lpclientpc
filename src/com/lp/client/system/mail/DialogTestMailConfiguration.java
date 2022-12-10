package com.lp.client.system.mail;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.EventObject;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.IControl;
import com.lp.client.frame.component.IHvValueHolderListener;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.mail.service.LPMailDto;
import com.lp.server.system.mail.service.MailTestMessage;
import com.lp.server.system.mail.service.MailTestMessageResult;
import com.lp.server.system.service.VersandFac;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

public class DialogTestMailConfiguration extends JDialog implements ActionListener, ItemChangedListener, IHvValueHolderListener {
	private static final long serialVersionUID = 5467517780260794603L;

	private WrapperLabel wlaFrom;
	private WrapperTextField wtfFrom;
	private WrapperLabel wlaTo;
	private WrapperTextField wtfTo;
	private WrapperLabel wlaSubject;
	private WrapperTextField wtfSubject;
	private WrapperLabel wlaText;
	private WrapperTextField wtfText;
	private WrapperCheckBox wcbMitParameter;
	private JTextArea wtaLog;
	private WrapperButton wbuSelectPersonal;
	private WrapperTextField wtfPersonal;
	private WrapperLabel wlaLog;
	
	private WrapperButton wbuSend;
	private JPanel panelVersand;
	
	private InternalFrame internalFrame;
	private PanelQueryFLR panelQueryFLRPersonal;
	private PersonalDto personalDto;
	
	private TestMailConfigCtrl controller;
	
	public DialogTestMailConfiguration(Frame owner, InternalFrame internalFrame, String title, boolean modal,
			TestMailConfigCtrl controller) {
		super(owner, title, modal);
		
		this.internalFrame = internalFrame;
		this.controller = controller;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		setPreferredSize(new Dimension(1000, 400));
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
		
		setupComponents();
	}

	private void jbInit() {
		wlaFrom = new WrapperLabel(LPMain.getTextRespectUISPr("label.absender"));
		wtfFrom = new WrapperTextField(VersandFac.MAX_ABSENDER);
		wtfFrom.setMandatoryField(true);
		
		wlaTo = new WrapperLabel(LPMain.getTextRespectUISPr("label.empfaenger"));
		wtfTo = new WrapperTextField(VersandFac.MAX_EMPFAENGER);
		wtfTo.setMandatoryField(true);
		
		wlaSubject = new WrapperLabel(LPMain.getTextRespectUISPr("label.betreff"));
		wtfSubject = new WrapperTextField(VersandFac.MAX_BETREFF);
		wtfSubject.setMandatoryField(true);

		wlaText = new WrapperLabel(LPMain.getTextRespectUISPr("label.text"));
		wtfText = new WrapperTextField(VersandFac.MAX_BETREFF);
		wtfText.setMandatoryField(true);
		
		wbuSend = new WrapperButton(LPMain.getTextRespectUISPr("lp.senden"));
		wbuSend.addActionListener(this);
		
		wcbMitParameter = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.system.parameter.mail.testmailsenden.mitparameter"));
		wbuSelectPersonal = new WrapperButton(LPMain.getTextRespectUISPr("button.personal"));
		wbuSelectPersonal.addActionListener(this);
		wtfPersonal = new WrapperTextField();
		wtfPersonal.setMandatoryField(true);
		wtfPersonal.setActivatable(false);
		
		wlaLog = new WrapperLabel(LPMain.getTextRespectUISPr("lp.system.parameter.mail.testmailsenden.log"));
		wlaLog.setVerticalAlignment(SwingConstants.TOP);
		
		wtaLog = new JTextArea();
		wtaLog.setEditable(false);
		wtaLog.setFont(Font.decode(Font.MONOSPACED));
		JScrollPane scrollPane = new JScrollPane(wtaLog, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		panelVersand = new JPanel();
		HvLayout layoutVersand = HvLayoutFactory.create(panelVersand, "", "[20%,fill|50%,fill]20[50%,fill|10%,fill,grow]", "[||||]20[fill,grow]");
		layoutVersand
			.add(wbuSelectPersonal).add(wtfPersonal).wrap()
			.add(wlaFrom).add(wtfFrom).wrap()
			.add(wlaTo).add(wtfTo).wrap()
			.add(wlaSubject).add(wtfSubject).add(wcbMitParameter).wrap()
			.add(wlaText).add(wtfText).add(wbuSend).wrap()
			.add(wlaLog).add(scrollPane, "span, push");
		
		getContentPane().setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));
		getContentPane().add(panelVersand, "wrap");
		
		getInternalFrame().addItemChangedListener(this);
	}

	private void setupComponents() {
		if (!controller.hasLastMailTestMessage()) {
			return;
		}
		
		MailTestMessage message = controller.getLastMailTestMessage();
		if (message.getPersonalIIdFrom() != null) {
			doActionPersonalSelected(message.getPersonalIIdFrom());
		}
		if (message.getFrom() != null) {
			wtfFrom.setText(message.getFrom());
		}
		wtfTo.setText(message.getTo());
		wtfSubject.setText(message.getSubject());
		wtfText.setText(message.getMessage());
		wcbMitParameter.setSelected(message.isAddMailPropertiesToMessage());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (wbuSend == e.getSource()) {
			doActionSenden();
		} else if (wbuSelectPersonal == e.getSource()) {
			doActionSelectPersonal();
		}
	}
	
	@Override
	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPersonal) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				doActionPersonalSelected(key);
			}
		}
	}

	private void doActionPersonalSelected(Integer selectedKey) {
		try {
			personalDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(selectedKey);
			wtfPersonal.setText(personalDto.formatAnrede());
			wtfFrom.setText(personalDto.getCEmail());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doActionSelectPersonal() {
		try {
			panelQueryFLRPersonal = PersonalFilterFactory.getInstance().createPanelFLRPersonal(
					getInternalFrame(), true, false, personalDto != null ? personalDto.getIId() : null);
			new DialogQuery(panelQueryFLRPersonal);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private InternalFrame getInternalFrame() {
		return internalFrame;
	}

	private void doActionSenden() {
		if (!allMandatoryFieldsSet())  {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
			return;
		}
		
		clearInfo();
		MailTestMessage testMessage = buildTestMessage();
		controller.setLastMailTestMessage(testMessage);
		addInfo(">> Sende Testmail: " + testMessage.asString());
		addInfo(">> " + Helper.formatTimestamp(new Timestamp(System.currentTimeMillis()), Defaults.getInstance().getLocUI()));
		SendMessageWorker worker = new SendMessageWorker(testMessage);
		worker.execute();
	}
		private boolean allMandatoryFieldsSet() {
		try {
			Component[] components = panelVersand.getComponents();
			for (Component comp : components) {
				if (comp instanceof IControl) {
					IControl c = (IControl) comp;
					if (c.isMandatoryField() && !c.hasContent())
						return false;
				}			
			}
			return true;
		} catch (Throwable t) {
			// TODO
			return false;
		}
	}

	private MailTestMessage buildTestMessage() {
		return new MailTestMessage()
				.from(wtfFrom.getText())
				.personalFrom(personalDto)
				.to(wtfTo.getText())
				.subject(wtfSubject.getText())
				.message(wtfText.getText())
				.addMailPropertiesToMessage(wcbMitParameter.isSelected());
	}
	
	private void showResult(MailTestMessageResult result) {
		String yes = LPMain.getTextRespectUISPr("lp.ja");
		String no = LPMain.getTextRespectUISPr("lp.nein");
		LPMailDto mailDto = result.getMailTestMessage().getMailDto();

		addNewLine();
		addInfo("> SMTP-Server: " + mailDto.getSmtpServer());
		addInfo("> SMTP-Benutzer: " + mailDto.getSmtpBenutzer());
		addInfo("Mail erzeugt: " + (result.isMailCreated() ? yes : no));
		addInfo("Mail gesendet: " + (result.isMailSent() ? yes : no));
//		addInfo("Hat IMAP: " + (result.hasImap() ? yes : no));
		
		if (result.hasImap()) {
			addNewLine();
			addInfo("> IMAP-Server: " + mailDto.getImapServer());
			addInfo("> IMAP-Benutzer: " + mailDto.getImapAdmin());
			addInfo("> IMAP-Verzeichnis: " + mailDto.getSentFolder());

			addInfo("Verbindung zum IMAP-Server hergestellt: " + (result.isImapConnected() ? yes : no));
			addInfo("Mail im IMAP-Verzeichnis abgelegt: " + (result.isMailStored() ? yes : no));
			if (result.isImapConnected() && !result.isMailStored()) {
				addInfo("Gefundene IMAP Verzeichnisse: " + StringUtils.join(result.getImapFolders().toArray(), ", "));
			}
		} else {
			addNewLine();
			addInfo("Kein IMAP-Server definiert.");
		}
		
		addNewLine();
		addInfo("Verwendete Mail-Parameter:");
		addInfo(getPropertiesAsString(result));

		if (result.hasException()) {
			showExc(result.getEx());
		}
		
		wtaLog.setCaretPosition(0);
	}
		private String getPropertiesAsString(MailTestMessageResult result) {
		StringWriter writer = new StringWriter();
		try {
			result.getMailProperties().store(new PrintWriter(writer), "");
			return writer.toString();
		} catch (IOException e) {
			// TODO
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				// TODO
			}
		}
		return null;
	}
	
	private void showExc(Throwable t) {
		addNewLine();
		addInfo(ExceptionUtils.getStackTrace(t));
	}
	
	private void addNewLine() {
		wtaLog.append("\n");
	}
	
	private void addInfo(String info) {
		wtaLog.append(info);
		addNewLine();
	}

	private void clearInfo() {
		wtaLog.setText(null);
	}
	
	public class SendMessageWorker extends SwingWorker<MailTestMessageResult, Void> {
		private MailTestMessage testMessage;
		private Throwable resultExc = null;
		
		public SendMessageWorker(MailTestMessage testMessage) {
			this.testMessage = testMessage;
		}

		@Override
		protected MailTestMessageResult doInBackground() throws Exception {
			try {
				MailTestMessageResult result = DelegateFactory.getInstance().getVersandDelegate().testMailConfiguration(testMessage);
				return result;
			} catch (Throwable e) {
				addNewLine();
				addInfo("Es ist ein unerwarteter Fehler aufgetreten: ");
				resultExc = e;
				return null;
			}
		}
		@Override
		protected void done() {
			try {
				MailTestMessageResult result = get();
				if (resultExc != null) {
					showExc(resultExc);
				} else {
					showResult(result);
				}
			} catch (InterruptedException e) {
				showExc(e);
			} catch (ExecutionException e) {
				showExc(e);
			}
		}
	}

	@Override
	public void valueChanged(Component reference, Object oldValue, Object newValue) {
		
	}
}
