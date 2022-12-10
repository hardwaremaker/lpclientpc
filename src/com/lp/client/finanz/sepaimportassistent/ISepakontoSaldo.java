package com.lp.client.finanz.sepaimportassistent;

import java.math.BigDecimal;

import com.lp.server.finanz.service.KontoDto;

public interface ISepakontoSaldo {

	BigDecimal getSaldo();
	
	KontoDto getKonto();
	
	void refresh();
}
