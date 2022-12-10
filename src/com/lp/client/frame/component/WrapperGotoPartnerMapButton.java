/**
 * 
 */
package com.lp.client.frame.component;

import com.lp.client.partner.IPartnerDto;
import com.lp.util.GotoHelper;

/**
 * @author Laura
 *
 */
public class WrapperGotoPartnerMapButton extends WrapperGotoMapButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6867846054603041319L;

	/**
	 * @param partnerDto
	 */
	public WrapperGotoPartnerMapButton(IPartnerDto partnerDto) {
		super(GotoHelper.GOTO_PARTNER_AUSWAHL, partnerDto);
	}

	/**
	 * @param text
	 * @param partnerDto
	 */
	public WrapperGotoPartnerMapButton(String text, IPartnerDto partnerDto) {
		super(text, GotoHelper.GOTO_PARTNER_AUSWAHL, partnerDto);
	}

}
