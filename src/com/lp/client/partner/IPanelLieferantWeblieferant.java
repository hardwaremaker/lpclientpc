package com.lp.client.partner;

import javax.swing.JComponent;

import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.partner.service.LieferantDto;

public interface IPanelLieferantWeblieferant {

	void setWebpartnerDto(IWebpartnerDto webpartnerDto, LieferantDto lieferantDto);

	void create() throws Throwable;
	
	void update() throws Throwable;
	
	void remove() throws Throwable;
	
	JComponent[][] getPanelComponents();
	
}
