/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.finanz.sepaimportassistent;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.commons.lang.StringUtils;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAdapter;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungAdapter;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.Iso20022BankverbindungDto;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaSpesen;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.finanz.service.SepakontoauszugDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.RechnungAdapter;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungzahlungAdapter;
import com.lp.server.util.BankverbindungId;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;
import com.lp.util.Helper;

public class SepaImportModel {
	
	public static final String DATE_FORMAT = "dd.MM.YYYY";
	
	private SepaKontoauszugModel kontoauszugModel;
	private BankverbindungDto bvDto;
	private Map<Integer, PartnerDto> bvPartnerMap;
	private ISepakonto sepakonto;
	private boolean hasFibu = true;
	
	public SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	private List<ISepaImportResult> results;

	public SepaImportModel(SepakontoauszugDto statement, BankverbindungDto bvDto) throws ExceptionLP {
		bvPartnerMap = new HashMap<Integer, PartnerDto>();
		setBankverbindungDto(bvDto);
		setSepakontauszugDto(statement);
	}

	public ISepakonto getSepakonto() {
		return sepakonto;
	}
	
	public void setSepakonto(ISepakonto sepakonto) {
		this.sepakonto = sepakonto;
	}
	
	public void setSepakontauszugDto(SepakontoauszugDto statement) throws ExceptionLP {
		Iso20022BankverbindungDto iso20022Bankverbindung = DelegateFactory.getInstance().getFinanzDelegate().iso20022BankverbindungFindByBankverbindungIId(
				new BankverbindungId(getSelectedBankverbindung().getIId()));
		Iso20022StandardEnum iso20022Standard = iso20022Bankverbindung != null
				? iso20022Bankverbindung.getStandardEnum() : Iso20022StandardEnum.SEPA;
		kontoauszugModel = new SepaKontoauszugModel(statement, iso20022Standard);
	}
	
	public SepakontoauszugDto getSepakontoauszugDto() {
		return kontoauszugModel.getKontoauszugDto();
	}
	
	public SepaKontoauszug getCurrentStatement() {
		return kontoauszugModel == null ? null : kontoauszugModel.getStatement();
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public PartnerDto getPartnerDtoByBankIId(Integer bankIId) {
		return bvPartnerMap.get(bankIId);
	}

	public void setBankverbindungDto(BankverbindungDto bvDto) throws ExceptionLP {
		this.bvDto = bvDto;
		
		PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate().partnerFindByPrimaryKey(bvDto.getBankIId());
		bvPartnerMap.put(bvDto.getBankIId(), partnerDto);
		
	}
	
	public BankverbindungDto getSelectedBankverbindung() {
		return bvDto;
	}

	public TableModel getOverviewTableModel() {
		StatementOverviewTableModel tableModel = new StatementOverviewTableModel();
		tableModel.setEntries(new StatementOverviewTableEntryNormalizer(kontoauszugModel).getOverviewTableEntries());
		return tableModel;
	}
	
	public SearchResultTableModel getSearchResultsTableModel(SepaImportPage4Ctrl controller) {
		return new SearchResultTableModel(controller);
	}
	
	public List<ISepaImportResult> getResults() {
		return results;
	}

	public void setResults(List<ISepaImportResult> results) {
		this.results = results;
	}
	
	public Integer getAuszugsnummer() {
		return kontoauszugModel.getAuszugsnummer();
	}
	
	public Integer getAuszugsnummerByPayment(SepaZahlung payment) {
		return Iso20022StandardEnum.SWISS.equals(kontoauszugModel.getIso20022Standard())
				? getSwissAuszugsnummer(payment)
				: getAuszugsnummer();
	}

	private Integer getSwissAuszugsnummer(SepaZahlung payment) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String swissAuszug = dateFormat.format(getZahldatum(payment));
		return Integer.parseInt(swissAuszug);
	}

	public Date getZahldatum(SepaZahlung payment) {
		SepaBuchung buchung = kontoauszugModel.getBuchungByPayment(payment);
		return buchung.getValutadatum() != null ? buchung.getValutadatum() : buchung.getBuchungsdatum();
	}
	
	public Date getZahldatum(Integer paymentIndex) {
		return getZahldatum(kontoauszugModel.getPayments().get(paymentIndex));
	}

	public BigDecimal getZahlungsbetrag(Integer paymentIndex) {
		return kontoauszugModel.getPayments().get(paymentIndex).getBetrag().getWert();
	}
	/**
	 * @param result
	 * @return
	 * @throws Throwable 
	 * @throws ExceptionLP 
	 */
	public void prepareBelegZahlungen(ISepaImportResult result) throws ExceptionLP, Throwable {
		Integer index = results.indexOf(result);
		
		for (BelegZahlungAdapter belegZahlung : result.getManualPayments()) {
			belegZahlung.setBankkontoIId(getSelectedBankverbindung().getIId());
			belegZahlung.setBKursuebersteuert(false);
			belegZahlung.setDZahldatum(getZahldatum(index));
			belegZahlung.setIAuszug(getAuszugsnummerByPayment(kontoauszugModel.getPayments().get(index)));
			belegZahlung.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_BANK);
			if (!SepaImportPage4Ctrl.MANUELLE_AUSWAHL.equals(result.getSelectedBeleg())) {
				belegZahlung.setBErledigt(result.completeSelectedBeleg());
			}
			
//			belegZahlung.setRechnungIId(result.getSelectedBeleg().getIId());
//			BigDecimal zahlungsbetrag = getZahlungsbetrag(index);
//			belegZahlung.setNBetragfw(zahlungsbetrag);
		}
	}
	
	public void belegChangedUpdateBelegZahlung(ISepaImportResult result) {
		if (result == null || result.getSelectedBeleg() == null) return;
		
		Integer index = results.indexOf(result);
		BelegAdapter beleg = result.getSelectedBeleg();
		BelegZahlungAdapter belegZahlung = getEmptyBelegZahlung(beleg);
		
		belegZahlung.setRechnungIId(beleg.getIId());
		belegZahlung.setNBetragfw(getZahlungsbetrag(index));
		
		List<BelegZahlungAdapter> belegZahlungen = new ArrayList<BelegZahlungAdapter>();
		belegZahlungen.add(belegZahlung);
		result.setManualPayments(belegZahlungen);
	}
	
	private BelegZahlungAdapter getEmptyBelegZahlung(BelegAdapter selectedBeleg) {
		
		if (selectedBeleg instanceof EingangsrechnungAdapter) {
			return new EingangsrechnungzahlungAdapter();
		} else if (selectedBeleg instanceof RechnungAdapter) {
			return new RechnungzahlungAdapter();
		}
		
		return null;
	}
	
	public Boolean hasStatementSalden() {
		if (getCurrentStatement() != null) return getCurrentStatement().hatSalden();
		
		return false;
	}

	public BigDecimal getEndSaldoBetrag() {
		if (!hasStatementSalden()) return null;
		
		SepaKontoauszug ktoauszug = getCurrentStatement();
		if (ktoauszug.getEndSaldo() == null || ktoauszug.getEndSaldo().getBetrag() == null) return null;
		
		return ktoauszug.getEndSaldo().getBetrag().getPlusMinusWert();
	}
	
	public BigDecimal getAnfangsSaldoBetrag() {
		if (!hasStatementSalden()) return null;

		SepaKontoauszug ktoauszug = getCurrentStatement();
		if (ktoauszug.getStartSaldo() == null || ktoauszug.getStartSaldo().getBetrag() == null) return null;
		
		return ktoauszug.getStartSaldo().getBetrag().getPlusMinusWert();
	}

	protected String createZahlungsInfo(ISepaImportResult result) {
		SepaZahlung payment = result.getPayment();
		StringBuilder builder = new StringBuilder();
		
		if (payment.isSammler()) {
			builder.append(LPMain.getTextRespectUISPr("fb.sepa.import.sammlerbuchung")).append("\n");
			if (1 == payment.getEnthalteneBuchungen()) {
				builder.append(LPMain.getTextRespectUISPr("fb.sepa.import.eineenthaltenebuchung"));
			} else {
				builder.append(LPMain.getMessageTextRespectUISPr("fb.sepa.import.mehrereenthaltenbuchungen", 
						payment.getEnthalteneBuchungen()));
				
			}
		} else {
			if (payment.getBeteiligter().getName() != null) {
				builder.append(payment.getBeteiligter().getName());
				builder.append(", ");
			}
			if (payment.getBeteiligter().getIban() != null) {
				builder.append(payment.getBeteiligter().getIban());
			}
			builder.append("\n");
			if (payment.getZahlungsreferenz() != null) {
				builder.append(payment.getZahlungsreferenz());
				if (payment.getVerwendungszweck() != null
						&& !payment.getVerwendungszweck().isEmpty()) builder.append(", ");
			}
			builder.append(payment.getVerwendungszweck());
		}
		
		return builder.toString();
	}
	
	protected MultilineText createZahlungsInfoForTable(ISepaImportResult result) {
		SepaZahlung payment = result.getPayment();
		MultilineText multilineText = new MultilineText();
		
		String kontoInfo = getKontoInformation(payment);
		String referenz = getReferenzZweck(payment);
		String spesen = getSpesen(payment);
		String zusatztext = getZusatzinformation(payment)
				+ (Helper.isStringEmpty(spesen) ? "" : spesen);
		
		if (!Helper.isStringEmpty(zusatztext) && !Helper.isStringEmpty(referenz)) {
			splitInParts(multilineText, kontoInfo + "; " + referenz + "; " + zusatztext, 140);
		} else {
			multilineText.addLine(kontoInfo);
			if (!Helper.isStringEmpty(referenz)) 
				multilineText.addLine(referenz);
			
			if (!Helper.isStringEmpty(zusatztext)) 
				multilineText.addLine(zusatztext);
		}
		
		return multilineText;
	}
	
	private String getSpesen(SepaZahlung payment) {
		if (payment.getSpesen().isEmpty())
			return null;
		
		List<String> spesenParts = new ArrayList<String>();
		for (SepaSpesen spesen : payment.getSpesen()) {
			String part = "";
			if (spesen.getInfo() != null) {
				part += " " + spesen.getInfo() + "=";
			}
			part += Helper.formatZahl(spesen.getBetrag().getPlusMinusWert(), 2, LPMain.getInstance().getUISprLocale());
			spesenParts.add(part);
		}
		
		return " Spesen:" + StringUtils.join(spesenParts.iterator(), ", ");
	}
	
	private String getZusatzinformation(SepaZahlung payment) {
		return !Helper.isStringEmpty(payment.getZusatzinformation())
				? payment.getZusatzinformation().replaceAll("\\s+", " ")
				: "";
	}
	
	private String getReferenzZweck(SepaZahlung payment) {
		if (payment.isSammler()) return null;
		
		if (payment.getZahlungsreferenz() != null) {
			String referenz = payment.getZahlungsreferenz();
			if (!Helper.isStringEmpty(payment.getVerwendungszweck())) {
				referenz += "; " + payment.getVerwendungszweck();
			}
			return referenz;
		} 
		
		return payment.getVerwendungszweck();
	}

	private String getKontoInformation(SepaZahlung payment) {
		if (payment.isSammler()) {
			String text = LPMain.getTextRespectUISPr("fb.sepa.import.sammlerbuchung") + ", ";
			if (payment.getEnthalteneBuchungen() != null && payment.getEnthalteneBuchungen() == 1) {
				text += LPMain.getTextRespectUISPr("fb.sepa.import.eineenthaltenebuchung");
			} else {
				text += LPMain.getMessageTextRespectUISPr("fb.sepa.import.mehrereenthaltenbuchungen", 
						payment.getEnthalteneBuchungen());
			}
			return text;
		}
		
		String text = "";
		if (payment.getBeteiligter().getName() != null) {
			text += payment.getBeteiligter().getName() + ", ";
		}
		if (payment.getBeteiligter().getIban() != null) {
			text += payment.getBeteiligter().getIban();
		}
		return text;
	}

	private void splitInParts(MultilineText referenztext, String value, Integer size) {
		for (int i = 0; i < value.length(); i += size) {
			referenztext.addLine(value.substring(i, Math.min(i + size, value.length())));
		}
	}
	
	public String getInfoBuchungszeile(ISepaImportResult result) {
		SepaZahlung zahlung = result.getPayment();
		StringBuilder sb = new StringBuilder();
		
		Date buchungsdatum = getZahldatum(getResults().indexOf(result));
		sb.append(dateFormat.format(buchungsdatum));
		sb.append(" | ");
		String beteiligter = zahlung.getBeteiligter().getName();
		sb.append(beteiligter != null ? beteiligter : "<unbekannt>");
		sb.append(" | Betrag: ");
		sb.append(Helper.formatZahl(zahlung.getBetrag().getPlusMinusWert(),
				2, LPMain.getInstance().getUISprLocale()));
		sb.append(" | Zweck/Referenz: ");
		if (zahlung.isSammler()) {
			sb.append(LPMain.getTextRespectUISPr("fb.sepa.import.sammlerbuchung") 
						+ "("+ String.valueOf(zahlung.getEnthalteneBuchungen()) + ")");
		} else {
			sb.append(zahlung.getZahlungsreferenz() != null ? zahlung.getZahlungsreferenz() : zahlung.getVerwendungszweck());
		}
		return sb.toString();
	}

	/**
	 * TableModel fuer die Uebersicht der Suchergebnisse
	 * 
	 * @author andi
	 *
	 */
	protected class SearchResultTableModel implements TableModel {
		
		protected static final int IDX_ZAHLUNG = 0;
		protected static final int IDX_BUCHUNGSDATUM = 1;
		protected static final int IDX_BETRAG = 2;
		protected static final int IDX_TREFFER = 3;
		protected static final int IDX_RESTBETRAG = 4;
		protected static final int IDX_ERLEDIGT = 5;
		protected static final int IDX_UEBERNAHME = 6;
		protected static final int IDX_IGNORIEREN = 7;
		
		private String[] columnNames;
		private String[] columnNamesShort;
		private SepaImportPage4Ctrl controller;

		public SearchResultTableModel(SepaImportPage4Ctrl controller) {
			columnNames = new String[8];
			columnNames[IDX_ZAHLUNG] = LPMain.getTextRespectUISPr("fb.sepa.import.head.zahlung");
			columnNames[IDX_BUCHUNGSDATUM] = LPMain.getTextRespectUISPr("fb.sepa.import.head.buchungsdatum");
			columnNames[IDX_BETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.head.betrag");
			columnNames[IDX_TREFFER] = LPMain.getTextRespectUISPr("fb.sepa.import.head.treffer");
			columnNames[IDX_RESTBETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.head.restbetrag");
			columnNames[IDX_ERLEDIGT] = LPMain.getTextRespectUISPr("fb.sepa.import.head.erledigt");
			columnNames[IDX_UEBERNAHME] = LPMain.getTextRespectUISPr("fb.sepa.import.head.uebernehmen");
			columnNames[IDX_IGNORIEREN] = LPMain.getTextRespectUISPr("fb.sepa.import.head.ignorieren");
			
			columnNamesShort = new String[8];
			columnNamesShort[IDX_ZAHLUNG] = LPMain.getTextRespectUISPr("fb.sepa.import.head.zahlung.short");
			columnNamesShort[IDX_BUCHUNGSDATUM] = LPMain.getTextRespectUISPr("fb.sepa.import.head.buchungsdatum.short");
			columnNamesShort[IDX_BETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.head.betrag.short");
			columnNamesShort[IDX_TREFFER] = LPMain.getTextRespectUISPr("fb.sepa.import.head.treffer.short");
			columnNamesShort[IDX_RESTBETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.head.restbetrag.short");
			columnNamesShort[IDX_ERLEDIGT] = LPMain.getTextRespectUISPr("fb.sepa.import.head.erledigt.short");
			columnNamesShort[IDX_UEBERNAHME] = LPMain.getTextRespectUISPr("fb.sepa.import.head.uebernehmen.short");
			columnNamesShort[IDX_IGNORIEREN] = LPMain.getTextRespectUISPr("fb.sepa.import.head.ignorieren.short");
			this.controller = controller;
		}
		
		@Override
		public void addTableModelListener(TableModelListener l) {
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
				case IDX_ZAHLUNG:
					return MultilineText.class;
				case IDX_TREFFER:
					return ISepaImportResult.class;
				case IDX_ERLEDIGT:
				case IDX_UEBERNAHME:
				case IDX_IGNORIEREN:
					return Boolean.class;
			}
			
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return hasFibu() ? columnNames.length - 1 : columnNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNamesShort[columnIndex];
		}
		
		public String getColumnNameToolTip(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			return results.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ISepaImportResult result = results.get(rowIndex);

			if (result == null) return null;
			
			switch (columnIndex) {
				case IDX_ZAHLUNG:
					return createZahlungsInfoForTable(result);
				case IDX_BUCHUNGSDATUM:
					return getDateFormat().format(getZahldatum(rowIndex));
				case IDX_BETRAG:
					return result.getPayment().getBetrag().getPlusMinusWert();
				case IDX_TREFFER:
					return result;
				case IDX_RESTBETRAG:
					return result.getRemainingAmount();
				case IDX_ERLEDIGT:
					return result.completeSelectedBeleg();
				case IDX_UEBERNAHME:
					return result.applyPayment();
				case IDX_IGNORIEREN:
					return result.ignorePayment();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (IDX_TREFFER == columnIndex) {
				return true;
			}
			
			Integer selectedIndex = getResultAtRow(rowIndex).getSelectedIndex();
			if (IDX_ERLEDIGT == columnIndex 
					&& selectedIndex != null && selectedIndex >= 0 
					&& getResultAtRow(rowIndex).getSelectedBeleg() != SepaImportPage4Ctrl.MANUELLE_AUSWAHL) {
				return true;
			}
				
			if (IDX_UEBERNAHME == columnIndex 
					&& selectedIndex != null && selectedIndex >= 0) {
				return true;
			}
			
			if (IDX_IGNORIEREN == columnIndex) {
				return true;
			}
			
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (!(value instanceof Boolean))
				return;
			
			Boolean bValue = (Boolean)value;
			ISepaImportResult result = results.get(rowIndex);
			if (IDX_ERLEDIGT == columnIndex) {
				result.setCompletedForSelectedBeleg(bValue);
			} else if (IDX_UEBERNAHME == columnIndex) {
				result.setApplyPayment(bValue);
				if (!hasFibu() && bValue) {
					result.setIgnorePayment(!bValue);
				}
				controller.tableModelValueChanged();
			} else if (IDX_IGNORIEREN == columnIndex) {
				result.setIgnorePayment(bValue);
				if (!hasFibu() && bValue) {
					result.setApplyPayment(!bValue);
				}
				controller.tableModelValueChanged();
			} else if (value instanceof ISepaImportResult) {
//				ISepaImportResult result = (ISepaImportResult) value;
//				if (result.getSelectedBeleg() == SepaImportPage4Ctrl.MANUELLE_AUSWAHL) {
//					controller.actionManuelleAuswahlDialog();
////					controller.addToManuelleAuswahlSelectedResults(result);
////					result.setSelectedIndex(-1);
//				} else {
//					results.get(rowIndex).setSelectedIndex(result.getSelectedIndex());
//					belegChangedUpdateBelegZahlung(results.get(rowIndex));
//					controller.tableModelValueChanged();
//					//controller.setBelegZahlungen(rowIndex, result.completeSelectedBeleg());
//				}
			}
		}

		public ISepaImportResult getResultAtRow(int row) {
			return results.get(row);
		}

	}

	public boolean hasFibu() {
		return hasFibu;
	}
	
	public void setHasFibu(boolean hasFibu) {
		this.hasFibu = hasFibu;
	}
	
	public Integer getNiedrigsteAuszugsnummer() {
		if (Iso20022StandardEnum.SEPA.equals(kontoauszugModel.getIso20022Standard()))
			return kontoauszugModel.getAuszugsnummer();
		
		Date lowestDate = null;
		for (SepaBuchung buchung : getSepakontoauszugDto().getKontoauszug().getBuchungen()) {
			Date compareDate = buchung.getValutadatum() != null ? buchung.getValutadatum() : buchung.getBuchungsdatum();
			if (lowestDate == null || compareDate.before(lowestDate)) {
				lowestDate = compareDate;
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String swissAuszug = dateFormat.format(lowestDate);
		return Integer.parseInt(swissAuszug);
	}

	public Iso20022StandardEnum getIso20022Standard() {
		return kontoauszugModel.getIso20022Standard();
	}
}
