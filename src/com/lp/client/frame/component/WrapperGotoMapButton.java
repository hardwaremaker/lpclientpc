/**
 * 
 */
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.partner.IPartnerDto;

/**
 * @author Laura
 *
 */
public class WrapperGotoMapButton extends WrapperGotoButton {
	private static final long serialVersionUID = -4434323464209633482L;

	private IPartnerDto partnerDto;
	private WrapperMapButton mapButton;

	protected WrapperGotoMapButton(int iWhereToGo, IPartnerDto partnerDto) {
		super(iWhereToGo);
		this.partnerDto = partnerDto; 
		init();
	}
	
	protected WrapperGotoMapButton(String text, int iWhereToGo, IPartnerDto partnerDto){
		super(text, iWhereToGo);
		this.partnerDto = partnerDto; 
		init();
	}
	
	private void init(){
		mapButton = new WrapperMapButton(partnerDto.getPartnerDto());
		HelperClient.setMinimumAndPreferredSize(mapButton, new Dimension (20,Defaults.getInstance().getControlHeight()));
		this.add(mapButton, new GridBagConstraints(1, 0, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	@Override
	public void setOKey(Object oKey) {
		super.setOKey(oKey);
		
		if(oKey == null){
			mapButton.setEnabled(false);
			mapButton.setPartnerDto(null);
		}
		else{
			mapButton.setPartnerDto(partnerDto.getPartnerDto());
		}
	}
	
	@Override
	protected int getColumnForGotoButton() {
		return 2;
	}
	
	@Override
	public void setEnabled(boolean enable){
		super.setEnabled(enable);
		if (getOKey() == null) {
			mapButton.setEnabled(false);
		}
	}
	
}
