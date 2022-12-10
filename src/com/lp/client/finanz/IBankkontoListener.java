package com.lp.client.finanz;

import com.lp.server.finanz.service.KontoDto;

public interface IBankkontoListener {

	public void updateBankkonto(KontoDto bankkontoDto);
	
	public void updateAuszugnummer(Integer auszugnummer);
	
	public void enableAuszugnummer(Boolean enable);
}
