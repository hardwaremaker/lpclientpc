package com.lp.client.system.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.SystemDelegate;
import com.lp.client.pc.LPMain;
import com.lp.server.system.mail.service.MailPropertyDto;
import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertiesController {
	public static final String PwdMask = "********";
	private Map<MailSecurityEnum, Integer> mapSecurityPortSmtp = new HashMap<MailSecurityEnum, Integer>() {
		private static final long serialVersionUID = 2013751661575326172L;
		{
			put(MailSecurityEnum.None, 25);
			put(MailSecurityEnum.Starttls, 587);
			put(MailSecurityEnum.Ssl, 465);
		}
	};
	private Map<MailSecurityEnum, Integer> mapSecurityPortImap = new HashMap<MailSecurityEnum, Integer>() {
		private static final long serialVersionUID = 3609162776573995919L;
		{
			put(MailSecurityEnum.None, 143);
			put(MailSecurityEnum.Ssl, 993);
		}
	};
	
	private List<IMailPropertyCtrl> propertyCtrls;
	
	public MailPropertiesController() {
		initPropertyCtrls();
	}
	
	private void initPropertyCtrls() {
		propertyCtrls = new ArrayList<IMailPropertyCtrl>();
		propertyCtrls.add(new MailPropertySmtpServer());
		propertyCtrls.add(new MailPropertySmtpBenutzer());
		propertyCtrls.add(new MailPropertySmtpKennwort());
		propertyCtrls.add(new MailPropertySmtpSicherheit());
		propertyCtrls.add(new MailPropertySmtpPort());
		propertyCtrls.add(new MailPropertySmtpMitAuth());
		
		propertyCtrls.add(new MailPropertyImapServer());
		propertyCtrls.add(new MailPropertyImapAdmin());
		propertyCtrls.add(new MailPropertyImapKennwort());
		propertyCtrls.add(new MailPropertyImapSicherheit());
		propertyCtrls.add(new MailPropertyImapPort());
		propertyCtrls.add(new MailPropertyImapVerzeichnis());
	}
	
	public List<IMailPropertyCtrl> getPropertyCtrls() {
		return propertyCtrls;
	}

	private SystemDelegate systemDelegate() throws Throwable {
		return DelegateFactory.getInstance().getSystemDelegate();
	}
	
	public void loadProperties() throws Throwable {
		Map<MailPropertyEnum, MailPropertyDto> hmProperties = systemDelegate().getMapAllKnownMailProperties();
		for (IMailPropertyCtrl ctrl : getPropertyCtrls()) {
			ctrl.loadProperty(hmProperties);
		}
	}

	public MailPropertiesUI getProperties() {
		MailPropertiesUI propsUi = new MailPropertiesUI();
		for (IMailPropertyCtrl ctrl : getPropertyCtrls()) {
			ctrl.setDataUI(propsUi);
		}
		return propsUi;
	}
	
	public void save(MailPropertiesUI properties) throws Throwable {
		Map<MailPropertyEnum, String> hmProperties = new HashMap<MailPropertyEnum, String>();
		for (IMailPropertyCtrl propertyCtrl : getPropertyCtrls()) {
			propertyCtrl.getDataUI(properties);
			if (propertyCtrl.hasChanged()) {
				propertyCtrl.saveProperty(hmProperties);
			}
		}
		systemDelegate().updateMailProperties(transformAsDtoList(hmProperties));
	}

	private List<MailPropertyDto> transformAsDtoList(Map<MailPropertyEnum, String> hmProperties) throws Throwable {
		List<MailPropertyDto> dtos = new ArrayList<MailPropertyDto>();
		for (Entry<MailPropertyEnum, String> entry : hmProperties.entrySet()) {
			MailPropertyDto transformed = new MailPropertyDto(entry.getKey().getValue(), entry.getValue());
			transformed.setMandantCNr(LPMain.getTheClient().getMandant());
			dtos.add(transformed);
		}
		return dtos;
	}
	
	public Integer getDefaultSmtpPortBySecurity(MailSecurityEnum securityEnum) {
		return mapSecurityPortSmtp.get(securityEnum);
	}

	public Integer getDefaultImapPortBySecurity(MailSecurityEnum securityEnum) {
		return mapSecurityPortImap.get(securityEnum);
	}
}
