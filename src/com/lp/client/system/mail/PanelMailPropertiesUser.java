package com.lp.client.system.mail;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperPasswordFieldPlain;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.InternalFrameSystem;

public class PanelMailPropertiesUser extends PanelBasis {
	private static final long serialVersionUID = 5229962517941241685L;
	
	private WrapperLabel wlaSmtp;
	private WrapperLabel wlaSmtpServer;
	private WrapperTextField wtfSmtpServer;
	private WrapperLabel wlaSmtpBenutzer;
	private WrapperTextField wtfSmtpBenutzer;
	private WrapperLabel wlaSmtpKennwort;
	private WrapperPasswordFieldPlain wpfSmtpKennwort;
	private WrapperLabel wlaSmtpPort;
	private WrapperLabel wlaSmtpSicherheit;
	private WrapperComboBox wcoSmtpSicherheit;
	private WrapperNumberField wnfSmtpPortManual;
	private WrapperCheckBox wcbSmtpMitAuth;

	private WrapperCheckBox wcbImap;
	private WrapperLabel wlaImap;
	private WrapperLabel wlaImapServer;
	private WrapperTextField wtfImapServer;
	private WrapperLabel wlaImapAdmin;
	private WrapperTextField wtfImapAdmin;
	private WrapperLabel wlaImapKennwort;
	private WrapperPasswordFieldPlain wpfImapKennwort;
	private WrapperLabel wlaImapPort;
	private WrapperLabel wlaImapSicherheit;
	private WrapperComboBox wcoImapSicherheit;
	private WrapperNumberField wnfImapPortManual;
	private WrapperLabel wlaImapVerzeichnis;
	private WrapperTextField wtfImapVerzeichnis;
	
	private JPanel jPanelWorkingOn;
	private JPanel panelSmtp;
	private JPanel panelImap;
	
	private MailPropertiesController controller;
	private MailPropertiesViewController viewController;
	
	private final String ACTION_SMTP_SICHERHEIT_COMBO = "SMTP_SICHERHEIT_COMBO";
	private final String ACTION_IMAP_SICHERHEIT_COMBO = "IMAP_SICHERHEIT_COMBO";
	private final String ACTION_SMTP = "SMTP";
	private final String ACTION_IMAP = "IMAP";
	
	public PanelMailPropertiesUser(InternalFrameSystem internalFrame, MailPropertiesViewController viewController, 
			MailPropertiesController controller) throws Throwable {
		super(internalFrame, LPMain.getTextRespectUISPr("lp.system.parameter.tab.mailproperties"));
		this.controller = controller;
		this.viewController = viewController;
		setKeyWhenDetailPanel(" ");
		jbInit();
		initComponents();
	}
	
	@Override
	public InternalFrameSystem getInternalFrame() {
		return (InternalFrameSystem) super.getInternalFrame();
	}

	private MailPropertiesController getController() {
		return controller;
	}
	
	private MailPropertiesViewController viewController() {
		return viewController;
	}
	
	private String smtpLabel(String labelToken) {
		return textFromToken("lp.mail.smtp.label." + labelToken);
	}
	
	private String imapLabel(String imapToken) {
		return textFromToken("lp.mail.imap.label." + imapToken);
	}
	
	private void jbInit() throws Throwable {
		createComponents();
		createLayout();

		this.setLayout(new GridBagLayout());
		// Actionpanel von Oberklasse holen und anhaengen.
		getInternalFrame().addItemChangedListener(this);
		this.add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
	}
	
	private void createLayout() throws Exception {
		String colConstraints = "[fill,20%|fill,150::150|fill,150::170|fill,100::100|fill,30%]";
		panelSmtp = new JPanel();
		HvLayout layout = HvLayoutFactory.create(panelSmtp, 
				"insets 5, hidemode 1", colConstraints, "[|]15[||]");
		layout
			.add(wlaSmtpServer).add(wtfSmtpServer).spanAndWrap(3)
			.add(wlaSmtpSicherheit).add(wcoSmtpSicherheit).add(wlaSmtpPort).add(wnfSmtpPortManual).wrap()
			.add(wlaSmtpBenutzer).add(wtfSmtpBenutzer).spanAndWrap(3)
			.add(wlaSmtpKennwort).add(wpfSmtpKennwort).span(3).add(wpfSmtpKennwort.getShowPlainTextComponent()).wrap()
			.add(wcbSmtpMitAuth, "skip 1, span 2");

		panelImap = new JPanel();
		HvLayout layoutImap = HvLayoutFactory.create(panelImap,
				"insets 5", colConstraints, "[|]15[|||]");
		layoutImap
			.add(wlaImapServer).add(wtfImapServer).spanAndWrap(3)
			.add(wlaImapSicherheit).add(wcoImapSicherheit).add(wlaImapPort).add(wnfImapPortManual).wrap()
			.add(wlaImapAdmin).add(wtfImapAdmin).spanAndWrap(3)
			.add(wlaImapKennwort).add(wpfImapKennwort).span(3).add(wpfImapKennwort.getShowPlainTextComponent()).wrap()
			.add(wlaImapVerzeichnis).add(wtfImapVerzeichnis).spanAndWrap(3);
//			.add(wcbImapMitAuth, "skip 1, span 2");
		
		jPanelWorkingOn = new JPanel();
		HvLayout layoutAll = HvLayoutFactory.create(jPanelWorkingOn,
				"ins 0", "[fill,grow]", "20[|]40[|]");
		layoutAll
			.add(wlaSmtp, "gapleft 20, wrap")
			.add(panelSmtp).wrap()
			.add(wlaImap, "gapleft 20, wrap")
			.add(panelImap);
	}

	private void createComponents() throws Throwable {
		wlaSmtp = new WrapperLabel(smtpLabel("einstellungen"));
		wlaSmtp.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSmtpServer = new WrapperLabel(smtpLabel("server"));
		wtfSmtpServer = new WrapperTextField(3000);
		
		wlaSmtpBenutzer = new WrapperLabel(smtpLabel("benutzer"));
		wtfSmtpBenutzer = new WrapperTextField(3000);
		
		wlaSmtpKennwort = new WrapperLabel(smtpLabel("kennwort"));
		wpfSmtpKennwort = new WrapperPasswordFieldPlain();
		wpfSmtpKennwort.setActivatable(true);
		wpfSmtpKennwort.setShowPlainTextAllowed(false);
		wpfSmtpKennwort.addFocusListener(new PwdFocusListener(wpfSmtpKennwort));

		wcbSmtpMitAuth = new WrapperCheckBox(smtpLabel("mitauth"));
		
		wlaSmtpPort = new WrapperLabel(smtpLabel("port"));
		wlaSmtpSicherheit = new WrapperLabel(smtpLabel("verschluesselung"));
		wcoSmtpSicherheit = createSmtpSicherheitCombobox();
		
		wnfSmtpPortManual = new WrapperNumberField();
		wnfSmtpPortManual.setFractionDigits(0);
		
		wcbImap = new WrapperCheckBox(imapLabel("einstellungen"));
		wcbImap.setActionCommand(ACTION_IMAP);
		wcbImap.addActionListener(this);
		wlaImap = new WrapperLabel(imapLabel("einstellungen"));
		wlaImap.setHorizontalAlignment(SwingConstants.LEFT);
		
		wlaImapServer = new WrapperLabel(imapLabel("server"));
		wtfImapServer = new WrapperTextField(3000);
		
		wlaImapAdmin = new WrapperLabel(imapLabel("benutzer"));
		wtfImapAdmin = new WrapperTextField(3000);
		
		wlaImapKennwort = new WrapperLabel(imapLabel("kennwort"));
		wpfImapKennwort = new WrapperPasswordFieldPlain();
		wpfImapKennwort.setActivatable(true);
		wpfImapKennwort.setShowPlainTextAllowed(false);
		wpfImapKennwort.addFocusListener(new PwdFocusListener(wpfImapKennwort));
		
		wlaImapPort = new WrapperLabel(imapLabel("port"));
		wlaImapSicherheit = new WrapperLabel(imapLabel("verschluesselung"));
		wcoImapSicherheit = createImapSicherheitCombobox();
		wnfImapPortManual = new WrapperNumberField();
		wnfImapPortManual.setFractionDigits(0);
		
		wlaImapVerzeichnis = new WrapperLabel(imapLabel("sentfolder"));
		wtfImapVerzeichnis = new WrapperTextField();
	}
	
	private WrapperComboBox createImapSicherheitCombobox() {
		WrapperComboBox co = new WrapperComboBox();
		co.setMandatoryField(true);
		co.setMap(viewController().loadMapImapSicherheit(), true);
		co.addActionListener(this);
		co.setActionCommand(ACTION_IMAP_SICHERHEIT_COMBO);
		
		return co;
	}

	private WrapperComboBox createSmtpSicherheitCombobox() {
		WrapperComboBox co = new WrapperComboBox();
		co.setMandatoryField(true);
		co.setMap(viewController().loadMapSmtpSicherheit(), true);
		co.addActionListener(this);
		co.setActionCommand(ACTION_SMTP_SICHERHEIT_COMBO);
		
		return co;
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_MAILPROPERTIES;
	}

	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(bNeedNoYouAreSelectedI);
		
		getInternalFrame().getTabbedPaneParameter().disableParametermandantgueltigab();
		viewController().userPanelSelected();
		dto2Components();
	}
	
	@Override
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		super.eventActionSave(e, bNeedNoSaveI);
		
		MailPropertiesUI properties = components2Dto();
		getController().save(properties);
		eventYouAreSelected(false);
		
		wpfImapKennwort.editModeDone();
		wpfSmtpKennwort.editModeDone();
	}

	@Override
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		wpfImapKennwort.editModeDone();
		wpfSmtpKennwort.editModeDone();
	}

	@Override
	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		wpfImapKennwort.editMode();
		wpfSmtpKennwort.editMode();
	}

	private MailPropertiesUI components2Dto() throws ExceptionLP {
		MailPropertiesUI properties = new MailPropertiesUI();
		properties.setSmtpServer(wtfSmtpServer.getText());
		properties.setSmtpBenutzer(wtfSmtpBenutzer.getText());
		properties.setSmtpPasswort(new String(wpfSmtpKennwort.getPassword()));
		properties.setSmtpMitAuth(wcbSmtpMitAuth.isSelected());
		properties.setSmtpPort(wnfSmtpPortManual.getInteger());
		properties.setSmtpSicherheitType((MailSecurityEnum) wcoSmtpSicherheit.getKeyOfSelectedItem());
		
		properties.setImapServer(wtfImapServer.getText());
		properties.setImapAdmin(wtfImapAdmin.getText());
		properties.setImapPasswort(new String(wpfImapKennwort.getPassword()));
		properties.setImapSentFolder(wtfImapVerzeichnis.getText());
		properties.setImapPort(wnfImapPortManual.getInteger());
		properties.setImapSicherheitType((MailSecurityEnum) wcoImapSicherheit.getKeyOfSelectedItem());
		
		return properties;
	}
	
	private void dto2Components() {
		MailPropertiesUI properties = getController().getProperties();
		wtfSmtpServer.setText(properties.getSmtpServer());
		wtfSmtpBenutzer.setText(properties.getSmtpBenutzer());
		wcbSmtpMitAuth.setSelected(properties.getSmtpMitAuth());
		wpfSmtpKennwort.setText(properties.getSmtpPasswort());
		wcoSmtpSicherheit.setKeyOfSelectedItem(properties.getSmtpSicherheitType());
		wnfSmtpPortManual.setInteger(properties.getSmtpPort());
		
		wtfImapServer.setText(properties.getImapServer());
		wtfImapAdmin.setText(properties.getImapAdmin());
		wpfImapKennwort.setText(properties.getImapPasswort());
		wtfImapVerzeichnis.setText(properties.getImapSentFolder());
		wcoImapSicherheit.setKeyOfSelectedItem(properties.getImapSicherheitType());
		wnfImapPortManual.setInteger(properties.getImapPort());
	}
	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		String command = e.getActionCommand();
		if (ACTION_SMTP_SICHERHEIT_COMBO.equals(command)) {
			Object selectedItem = wcoSmtpSicherheit.getKeyOfSelectedItem();
			MailSecurityEnum selectedPortType = selectedItem != null ? (MailSecurityEnum) selectedItem : null;
			onActionSmtpSicherheit(selectedPortType);
		
		} else if (ACTION_IMAP_SICHERHEIT_COMBO.equals(command)) {
			Object selectedItem = wcoImapSicherheit.getKeyOfSelectedItem();
			MailSecurityEnum selectedPortType = selectedItem != null ? (MailSecurityEnum) selectedItem : null;
			onActionImapSicherheit(selectedPortType);
		
//		} else if (ACTION_IMAP.equals(command)) {
//			panelImap.setVisible(wcbImap.isSelected());
		}
	}

	private void onActionSmtpSicherheit(MailSecurityEnum selectedPortType) {
		wnfSmtpPortManual.setInteger(viewController().getSmtpPortBySecurity(selectedPortType));
	}

	private void onActionImapSicherheit(MailSecurityEnum selectedPortType) {
		wnfImapPortManual.setInteger(viewController().getImapPortBySecurity(selectedPortType));
	}
	
	private class PwdFocusListener implements FocusListener {
		private WrapperPasswordFieldPlain pwdField;
		
		public PwdFocusListener(WrapperPasswordFieldPlain pwdField) {
			this.pwdField = pwdField;
		}
		
		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			if (!pwdField.validateLeadingTrailingSpaces()) {
				DialogFactory.showMeldung(
						textFromToken("lp.mail.kennwort.pruefung.leerzeichen"), 
						textFromToken("lp.hint"), 
						JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
			}
		}
		
	}
}
