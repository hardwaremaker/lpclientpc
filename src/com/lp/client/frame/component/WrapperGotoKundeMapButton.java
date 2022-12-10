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
public class WrapperGotoKundeMapButton extends WrapperGotoMapButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8400802901539648912L;

	public WrapperGotoKundeMapButton(IPartnerDto partnerDto) {
		super(GotoHelper.GOTO_KUNDE_AUSWAHL, partnerDto);
	}
	
	public WrapperGotoKundeMapButton(String text, IPartnerDto partnerDto){
		super(text, GotoHelper.GOTO_KUNDE_AUSWAHL, partnerDto );
	}

}
