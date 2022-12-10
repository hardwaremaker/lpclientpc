package com.lp.client.finanz.sepaimportassistent;

import java.math.BigDecimal;

public class ManuelleAuswahlUebersichtEntry {
	private String arNr;
	private String erNr;
	private String kontoNr;
	private String name;
	private String info;
	private BigDecimal betrag;
	private String waehrungCNr;
	
	public ManuelleAuswahlUebersichtEntry() {
	}
	public String getArNr() {
		return arNr != null ? arNr : "";
	}
	public void setArNr(String arNr) {
		this.arNr = arNr;
	}
	public String getErNr() {
		return erNr != null ? erNr : "";
	}
	public void setErNr(String erNr) {
		this.erNr = erNr;
	}
	public String getKontoNr() {
		return kontoNr != null ? kontoNr : "";
	}
	public void setKontoNr(String kontoNr) {
		this.kontoNr = kontoNr;
	}
	public String getName() {
		return name != null ? name : "";
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getBetrag() {
		return betrag;
	}
	public void setBetrag(BigDecimal betrag) {
		this.betrag = betrag;
	}
	public String getWaehrungCNr() {
		return waehrungCNr != null ? waehrungCNr : "";
	}
	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}
	public String getInfo() {
		return info != null ? info : "";
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (!getArNr().isEmpty()) {
			builder.append("AR " + getArNr());
		} else if (!getErNr().isEmpty()) {
			builder.append("ER " + getErNr());
		} else if (!getKontoNr().isEmpty()) {
			builder.append("Kto " + getKontoNr());
		}
		
		builder.append(": ");
		if (!getName().isEmpty()) {
			builder.append(getName());
			builder.append(", ");
		}
		if (!getInfo().isEmpty()) {
			builder.append(getInfo());
			builder.append(", ");
		}
		builder.append(getBetrag());
		builder.append(getBetrag() != null ? " " + getWaehrungCNr() : "");
		
		return builder.toString();
	}
}
