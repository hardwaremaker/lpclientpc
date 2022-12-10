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
public class WrapperGotoLieferantMapButton extends WrapperGotoMapButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7382937991811628099L;

	public WrapperGotoLieferantMapButton(IPartnerDto partnerDto) {
		super(GotoHelper.GOTO_LIEFERANT_AUSWAHL, partnerDto);
	}
	
	public WrapperGotoLieferantMapButton(String text, IPartnerDto partnerDto){
		super(text, GotoHelper.GOTO_LIEFERANT_AUSWAHL, partnerDto );
	}
}
