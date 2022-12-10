package com.lp.client.frame;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalDto;

public class PersonalFormatter {

	/**
	 * Liefert einen String mit formattierten aktuellen Zeitpunkt (dd.MM.yyyy HH:mm) 
	 * und dem Kurzzeichen des uuml;bergebenen Personals.
	 * 
	 * @param personalDto
	 * @return
	 */
	public String formatNowStaffShortSign(PersonalDto personalDto) {
		try {
			Locale uiLocale = LPMain.getInstance().getUISprLocale();
			Calendar c = Calendar.getInstance(uiLocale);
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", uiLocale);
			
			return new StringBuilder()
					.append(dateTimeFormat.format(c.getTime()))
					.append(" ")
					.append(personalDto.getCKurzzeichen()).toString();
		} catch (Throwable t) {
		}
		return "";
	}
	
	/**
	 * Liefert einen String mit formattiertem aktuellen Zeitpunkt (dd.MM.yyyy HH:mm) 
	 * und dem Kurzzeichen des aktuellen Benutzers.
	 * 
	 * @return
	 */
	public String formatNowUserShortSign() {
		try {
			Integer personalId = LPMain.getTheClient().getIDPersonal();
			PersonalDto personalDto = DelegateFactory.getInstance()
					.getPersonalDelegate().personalFindByPrimaryKey(personalId);
			return formatNowStaffShortSign(personalDto);
		} catch (Throwable e) {
		}
		return "";
	}
}
