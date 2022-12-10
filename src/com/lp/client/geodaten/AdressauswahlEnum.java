package com.lp.client.geodaten;

import com.lp.client.pc.LPMain;

public enum AdressauswahlEnum {
	MANUELL("Manuell"),
	KUNDE(LPMain.getTextRespectUISPr("lp.kunde")),
	LIEFERANT(LPMain.getTextRespectUISPr("lp.lieferant")),
	PARTNER(LPMain.getTextRespectUISPr("part.partner"));
	
	private String value;
	
	private AdressauswahlEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
