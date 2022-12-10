/**
 * 
 */
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.Helper;

/**
 * @author Laura
 *
 */
public class WrapperMapButton extends WrapperButton implements ActionListener {
	private static final long serialVersionUID = -5433422231803295029L;

	private PartnerDto partnerDto;
	//private IPartnerDto iPartnerDto;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());
	
	public WrapperMapButton(PartnerDto partnerDto){
		super(IconFactory.getLocation());
		setPartnerDto(partnerDto);
		addActionListener(this);
		setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		setToolTipText(LPMain.getTextRespectUISPr("lp.adresseanzeigen"));
		//iPartnerDto = null;
		
	}
	/*public WrapperMapButton(IPartnerDto partnerDto) {
		this(partnerDto.getPartnerDto());
		iPartnerDto = partnerDto;
	}*/
	public PartnerDto getPartnerDto() {
		return partnerDto;
	}
	
	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
		disableIfAddressIsEmpty();
	}

	private void disableIfAddressIsEmpty(){
		if(hasAddress()) {
			this.setEnabled(false);
		}
		else{
			this.setEnabled(true);
		}
	}
	private boolean hasAddress() {
		return getPartnerDto() == null || Helper.isStringEmpty(getPartnerDto().formatAdresse());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (hasAddress()) return;
		
		String searchParameters = getSearchParameters();
		String url = null;
		try {
			url = DelegateFactory.getInstance().getParameterDelegate().getKartenUrl();
		} catch (Throwable e2) {
			myLogger.error("actionPerformed()", e2);
			new DialogError(LPMain.getInstance().getDesktop(), e2,
					DialogError.TYPE_ERROR);
			return;
		}
		URI uri;
		if(url == null){
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getTextRespectUISPr("lp.adresseanzeigen.error"));
		}
		else{
			try {
				uri = new URI(url + searchParameters);
				java.awt.Desktop.getDesktop().browse(uri);
				} catch (URISyntaxException e1) {
					myLogger.error(LPMain.getTextRespectUISPr("lp.fehlerhafteurl"), e1);
				} catch (IOException e1) {
					myLogger.error("actionPerformed()", e1);
				}
		}
		
	}
	private String getSearchParameters() {
		return getPartnerDto().formatAdresse().replace(" ", "+");
	}
	
	public void setEditable(boolean bEditable) {
	    setEnabled(!bEditable);
	}
	
	public void setMinimumSize(Dimension d){
		super.setMinimumSizeImpl(d);
	}
	
	public void setPreferredSize(Dimension d){
		super.setPreferredSizeImpl(d);
	}
	
}
