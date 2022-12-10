package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NotEnoughInStockException;

public class CsvErrorNotEnoughInStock implements ICsvErrorAction {

	private NotEnoughInStockException exception;
	
	public CsvErrorNotEnoughInStock(NotEnoughInStockException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.zuwenigimlager", 
				exception.getQuantityInStock(), exception.getArticlenumber(), exception.getQuantityRequired());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
