package com.lp.client.partner;

import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.LieferantDelegate;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.partner.service.LieferantDto;

public abstract class PanelLieferantWebabfrageDetailBase<T extends IWebpartnerDto> extends JPanel implements IPanelLieferantWeblieferant {
	private static final long serialVersionUID = -1912181765323271165L;
	
	private T webpartnerDto;

	public PanelLieferantWebabfrageDetailBase() {
	}

	protected LieferantDelegate lieferantDelegate() throws Throwable {
		return DelegateFactory.getInstance().getLieferantDelegate();
	}
	
	public T getWebpartnerDto() {
		return webpartnerDto;
	}
	
	public void setWebpartnerDto(IWebpartnerDto webpartnerDto, LieferantDto lieferantDto) {
		if (webpartnerDto == null) {
			webpartnerDto = createWebPartnerDto();
			webpartnerDto.setLieferantIId(lieferantDto.getIId());
			webpartnerDto.setLieferantDto(lieferantDto);
		}
		this.webpartnerDto = (T) webpartnerDto;
		dto2components();
	}
	
	public void update() throws Throwable {
		components2Dto();
		if (getWebpartnerDto().getIId() == null) {
			create();
		} else {
			lieferantDelegate().updateWeblieferant(getWebpartnerDto());
		}
	}
	
	public void create() throws ExceptionLP, Throwable {
		components2Dto();
		lieferantDelegate().createWeblieferant(getWebpartnerDto());
	}
	
	public void remove() throws ExceptionLP, Throwable {
		if (getWebpartnerDto().getIId() == null) return;
		
		lieferantDelegate().removeWeblieferant(getWebpartnerDto().getIId());
	}
	
	protected abstract void components2Dto();
	protected abstract void dto2components();
	protected abstract T createWebPartnerDto();
}
