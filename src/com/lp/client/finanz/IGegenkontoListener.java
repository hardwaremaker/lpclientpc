package com.lp.client.finanz;

import com.lp.server.finanz.service.KontoDto;

public interface IGegenkontoListener {

	public void updateGegenkonto(KontoDto gegenkontoDto);
	
	public void updateAuszugnummerGegenkonto(Integer auszugnummer);
	
	public void enableAuszugnummerGegenkonto(Boolean enable);
	
	public void updateKontotypGegenkonto(String kontotyp);
}
