package com.lp.client.finanz.sepaimportassistent;

public class StatementOverviewTableEntry {
	
	private String name;
	private String iban;
	private String zahldatum;
	private String betrag;
	private MultilineText referenztext;

	public StatementOverviewTableEntry() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getZahldatum() {
		return zahldatum;
	}

	public void setZahldatum(String zahldatum) {
		this.zahldatum = zahldatum;
	}

	public String getBetrag() {
		return betrag;
	}

	public void setBetrag(String betrag) {
		this.betrag = betrag;
	}

	public MultilineText getReferenztext() {
		return referenztext;
	}

	public void setReferenztext(MultilineText referenztext) {
		this.referenztext = referenztext;
	}

}
