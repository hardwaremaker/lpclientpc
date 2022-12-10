package com.lp.client.finanz;

import java.io.Serializable;
import java.sql.Date;

import com.lp.server.finanz.service.BuchungsjournalExportDatumsart;

public class BuchungsjournalExportUIProperties implements Serializable {
	private static final long serialVersionUID = 8633237763877814180L;

	private String format;
	private BuchungsjournalExportDatumsart datumsart;
	private Date letztesBisDatum;
	private boolean mitAutoEB;
	private boolean mitAutoB;
	private boolean mitManEB;
	private boolean mitStornierte;
	private String dateipfad;
	
	public BuchungsjournalExportDatumsart getDatumsart() {
		return datumsart;
	}
	public void setDatumsart(BuchungsjournalExportDatumsart datumsart) {
		this.datumsart = datumsart;
	}
	public Date getLetztesBisDatum() {
		return letztesBisDatum;
	}
	public void setLetztesBisDatum(Date letztesBisDatum) {
		this.letztesBisDatum = letztesBisDatum;
	}
	public boolean isMitAutoEB() {
		return mitAutoEB;
	}
	public void setMitAutoEB(boolean mitAutoEB) {
		this.mitAutoEB = mitAutoEB;
	}
	public boolean isMitAutoB() {
		return mitAutoB;
	}
	public void setMitAutoB(boolean mitAutoB) {
		this.mitAutoB = mitAutoB;
	}
	public boolean isMitManEB() {
		return mitManEB;
	}
	public void setMitManEB(boolean mitManEB) {
		this.mitManEB = mitManEB;
	}
	public boolean isMitStornierte() {
		return mitStornierte;
	}
	public void setMitStornierte(boolean mitStornierte) {
		this.mitStornierte = mitStornierte;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDateipfad() {
		return dateipfad;
	}
	public void setDateipfad(String dateipfad) {
		this.dateipfad = dateipfad;
	}
}
