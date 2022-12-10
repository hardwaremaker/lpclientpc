package com.lp.client.system.mail;

import com.lp.util.Helper;

public class MailPropertiesUI {

	private String smtpServer;
	private String smtpBenutzer;
	private String smtpPasswort;
	private Integer smtpPort;
	private Boolean smtpMitAuth;
	private MailSecurityEnum smtpSicherheitType;
	
	private String imapServer;
	private String imapAdmin;
	private String imapPasswort;
	private Integer imapPort;
	private Boolean imapMitAuth; //??
	private String imapSentFolder;
	private MailSecurityEnum imapSicherheitType;
	
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = emptyAsNull(smtpServer);
	}
	public String getSmtpBenutzer() {
		return smtpBenutzer;
	}
	public void setSmtpBenutzer(String smtpBenutzer) {
		this.smtpBenutzer = emptyAsNull(smtpBenutzer);
	}
	public String getSmtpPasswort() {
		return smtpPasswort;
	}
	public void setSmtpPasswort(String smtpPasswort) {
		this.smtpPasswort = emptyAsNull(smtpPasswort);
	}
	public Integer getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}
	public Boolean getSmtpMitAuth() {
		return smtpMitAuth;
	}
	public void setSmtpMitAuth(Boolean smtpMitAuth) {
		this.smtpMitAuth = smtpMitAuth;
	}
	public MailSecurityEnum getSmtpSicherheitType() {
		return smtpSicherheitType;
	}
	public void setSmtpSicherheitType(MailSecurityEnum smtpSicherheitType) {
		this.smtpSicherheitType = smtpSicherheitType;
	}
	public String getImapServer() {
		return imapServer;
	}
	public void setImapServer(String imapServer) {
		this.imapServer = emptyAsNull(imapServer);
	}
	public String getImapAdmin() {
		return imapAdmin;
	}
	public void setImapAdmin(String imapAdmin) {
		this.imapAdmin = emptyAsNull(imapAdmin);
	}
	public String getImapPasswort() {
		return imapPasswort;
	}
	public void setImapPasswort(String imapPasswort) {
		this.imapPasswort = emptyAsNull(imapPasswort);
	}
	public Integer getImapPort() {
		return imapPort;
	}
	public void setImapPort(Integer imapPort) {
		this.imapPort = imapPort;
	}
	public Boolean getImapMitAuth() {
		return imapMitAuth;
	}
	public void setImapMitAuth(Boolean imapMitAuth) {
		this.imapMitAuth = imapMitAuth;
	}
	public String getImapSentFolder() {
		return imapSentFolder;
	}
	public void setImapSentFolder(String imapSentFolder) {
		this.imapSentFolder = emptyAsNull(imapSentFolder);
	}
	public MailSecurityEnum getImapSicherheitType() {
		return imapSicherheitType;
	}
	public void setImapSicherheitType(MailSecurityEnum imapSicherheitType) {
		this.imapSicherheitType = imapSicherheitType;
	}
	
	private String emptyAsNull(String value) {
		return Helper.isStringEmpty(value) ? null : value;
	}
}
