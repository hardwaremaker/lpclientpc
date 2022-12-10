package com.lp.client.finanz;

import java.math.BigDecimal;

import com.lp.server.system.service.WechselkursDto;

public interface IBetragListener {
	
	public void updateBuchungsbetrag(BigDecimal betrag);
	
	public void updateKurs(WechselkursDto kursDto);
}
