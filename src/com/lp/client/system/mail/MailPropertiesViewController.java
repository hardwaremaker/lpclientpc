package com.lp.client.system.mail;

import java.util.HashMap;
import java.util.Map;

import com.lp.client.pc.LPMain;

public class MailPropertiesViewController {

	private PanelMailPropertiesUser panelProperties;
	private MailPropertiesController controller;
	
	public MailPropertiesViewController(MailPropertiesController controller) {
		this.controller = controller;
	}
	
	private MailPropertiesController getController() {
		return controller;
	}
	
	public void setPanelMailProperties(PanelMailPropertiesUser panelProperties) {
		this.panelProperties = panelProperties;
	}
	
	public PanelMailPropertiesUser getPanelMailProperties() {
		return panelProperties;
	}
	
	private String textCombobox(String coToken) {
		return LPMain.getTextRespectUISPr("lp.mail.combobox.sicherheit." + coToken);
	}
	
	public Map<MailSecurityEnum, String> loadMapSmtpSicherheit() {
		Map<MailSecurityEnum, String> hmSmtpPorts = new HashMap<MailSecurityEnum, String>();
		hmSmtpPorts.put(MailSecurityEnum.None, textCombobox("keine"));
		hmSmtpPorts.put(MailSecurityEnum.Starttls, textCombobox("starttls"));
		hmSmtpPorts.put(MailSecurityEnum.Ssl, textCombobox("ssl"));
		return hmSmtpPorts;
	}
	
	public Map<MailSecurityEnum, String> loadMapImapSicherheit() {
		Map<MailSecurityEnum, String> hmSmtpPorts = new HashMap<MailSecurityEnum, String>();
		hmSmtpPorts.put(MailSecurityEnum.None, textCombobox("keine"));
		hmSmtpPorts.put(MailSecurityEnum.Starttls, textCombobox("starttls"));
		hmSmtpPorts.put(MailSecurityEnum.Ssl, textCombobox("ssl"));
		return hmSmtpPorts;
	}

	public void userPanelSelected() throws Throwable {
		getController().loadProperties();
	}
	
	public Integer getSmtpPortBySecurity(MailSecurityEnum securityEnum) {
		return getController().getDefaultSmtpPortBySecurity(securityEnum);
	}
	
	public Integer getImapPortBySecurity(MailSecurityEnum securityEnum) {
		return getController().getDefaultImapPortBySecurity(securityEnum);
	}
}
