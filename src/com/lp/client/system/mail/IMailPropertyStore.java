package com.lp.client.system.mail;

import java.util.Map;

import com.lp.server.system.mail.service.MailPropertyDto;
import com.lp.server.system.mail.service.MailPropertyEnum;

public interface IMailPropertyStore {

	/**
	 * Zum Initialisieren der jeweiligen Property. Uebergabe aller geladenen MailProperties.
	 * Jede Property bedient sich daraus mit den f&uuml;r sie relevanten Daten.
	 * 
	 * @param store
	 */
	void loadProperty(Map<MailPropertyEnum, MailPropertyDto> store);
	
	/**
	 * F&uuml;gt der Map jene MailProperties hinzu, die von der Implementierung
	 * beeinflusst werden.
	 * 
	 * @param store
	 */
	void saveProperty(Map<MailPropertyEnum, String> store);
	
	/**
	 * Hat sich der Wert der Property ver&auml;ndert?
	 * 
	 * @return <code>true</code> wenn sich der Wert zum geladenen Wert ge&auml;ndert hat.
	 */
	boolean hasChanged();
}
