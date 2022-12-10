package com.lp.client.partner;

import javax.swing.JComponent;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.LieferantDelegate;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.partner.service.LieferantDto;

public class PanelLieferantWebabfrageLeer implements IPanelLieferantWeblieferant {

	private LieferantDto lieferantDto;
	
	public PanelLieferantWebabfrageLeer() {
	}

	protected LieferantDelegate lieferantDelegate() throws Throwable {
		return DelegateFactory.getInstance().getLieferantDelegate();
	}

	@Override
	public void setWebpartnerDto(IWebpartnerDto webpartnerDto, LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

	@Override
	public void create() throws Throwable {
		removeWeblieferant();
	}

	@Override
	public void update() throws Throwable {
		removeWeblieferant();
	}

	@Override
	public void remove() throws Throwable {
		removeWeblieferant();
	}

	private void removeWeblieferant() throws Throwable {
		IWebpartnerDto savedWebpartner = lieferantDelegate().weblieferantFindByLieferantIIdOhneExc(lieferantDto.getIId());
		lieferantDelegate().removeWeblieferant(savedWebpartner.getIId());
	}

	@Override
	public JComponent[][] getPanelComponents() {
		return new JComponent[0][0];
	}

}
