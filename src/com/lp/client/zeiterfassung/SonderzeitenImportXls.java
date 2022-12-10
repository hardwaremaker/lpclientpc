package com.lp.client.zeiterfassung;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.SonderzeitenImportDto;
import com.lp.util.Helper;

public class SonderzeitenImportXls {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	private HashMap<Integer, ArrayList<SonderzeitenImportDto>> hmPersonen ;
	private HashMap<?,?> hmTaetigkeiten ;

	private Date startDate ;
	private Date endDate ;
	
	public SonderzeitenImportXls() {		
	}
	
	public Date getStartDate() {
		return startDate ;
	}
	
	public Date getEndDate() {
		return endDate ;
	}
	
	public void importFile(File xlsFile, Date startDate, Date endDate) throws IndexOutOfBoundsException, ExceptionLP, Throwable {
		this.startDate = startDate;
		this.endDate = endDate;
		
		if(startDate == null) {
			myLogger.warn("Ignoriere Datei '" + xlsFile.getName() + " da Startdatum nicht gesetzt");
			return;
		}
		
		if(endDate == null) {
			myLogger.warn("Ignoriere Datei '" + xlsFile.getName() + " da Endedatum nicht gesetzt");
			return;
		}
		
		this.endDate = new Date(endDate.getTime() + 24* 3600000) ;
		
		Workbook workbook = Workbook.getWorkbook(xlsFile) ;
		for(int i = 0 ; i < workbook.getSheets().length; i++) {
			importSheet(workbook.getSheet(i), this.startDate, this.endDate);
		}
	}
	
	public HashMap<Integer, ArrayList<SonderzeitenImportDto>> getImportData() {
		return hmPersonen ;
	}
	
	protected void importSheet(Sheet sheet, Date startDate, Date endDate) throws ExceptionLP, Throwable {
		myLogger.info("Arbeitsblatt '" + sheet.getName() + "' verarbeiten...");
		
		if(sheet.getRows() < 7 || sheet.getColumns() < 4) {
			myLogger.warn("Arbeitsblatt '" + sheet.getName() + 
					"' wird ignoriert (Zeilen: '" + sheet.getRows() + 
					"' (mindestens 7), Spalten '" + sheet.getColumns() + "' (mindestens 4).");
			return ;
		}

		List<Integer> dateCols = findDateCols(sheet) ;
		
		for(int row = 6; row < sheet.getRows(); row++) {
			Cell[] cells = sheet.getRow(row) ;
			PersonalDto personalDto = null;
			java.util.Date xlsStartDate = null ;
			int persCol = -1 ;
			
			myLogger.info("Arbeitsblatt '" + sheet.getName() + "', Zeile '" + row + "' verarbeiten...");
			
			for(int col = 0; col < cells.length; col++) {
				if(personalDto == null) {
					String persnr = cells[col].getContents() ;
					if(persnr != null && persnr.trim().length() > 0) {
						xlsStartDate = null ;
						personalDto = DelegateFactory.getInstance().getPersonalDelegate()
								.personalFindByCPersonalnrMandantCNrOhneExc(persnr) ;
						if(personalDto == null) {
							myLogger.warn("Ignoriere unbekannte Personalnummer '" + persnr + "' in Zeile '" + row + "', Spalte '" + col + "'.");
							break ;
						}	
				
						persCol = col ;
						myLogger.info("Personalnummer '" + persnr + "' in Spalte '" + persCol + "' verarbeiten...");
					} else {
						myLogger.warn("Keine Personalnummer in Zeile '" + row + "', Spalte '" + col + "'.");
						// Die naechste Spalte ermitteln, in der wieder ein Datum stehen sollte
						col = findNextDateCol(dateCols, col + 2) - 2 - 1 ;
						myLogger.warn("Naechste Datumsspalte '" + col + "'.");
						if(col <= 0) {
							break ;
						} else { 
							continue ;
						}
					}
				}

				if(xlsStartDate == null) {
					if(persCol < 0) {
						myLogger.warn("Kein Personalnummer erkannt, und trotzdem Datum laden?") ;
						break ;
					}

					Cell c = sheet.getCell(persCol + 2, 1) ;
					if(c.getType() != CellType.DATE) {
						myLogger.warn("Ignoriere fehlendes Startdatum '" + 
								c.getContents() + "' in Zeile '" + (1) + "', Spalte '" + (col + 2) + "'.") ;
						break ;						
					}
					
					xlsStartDate = ((DateCell)c).getDate() ;
					col += 2 ;
					continue ;
				}
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(xlsStartDate);
				
				int daysInMonth = col - persCol - 2 - 1;
				if(daysInMonth >= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					xlsStartDate = null ;
					personalDto = null ;
					continue ;
				}
				
				cal.add(Calendar.DATE, daysInMonth) ;
				myLogger.debug("Tag '" + cal.get(Calendar.DAY_OF_MONTH) + "'.") ;
				long l = Helper.cutTimestamp(new Timestamp(cal.getTimeInMillis())).getTime();
				if(l >= endDate.getTime()) {
					break ;
				}
				
				if(l < startDate.getTime()) {
					continue ;
				}
				
				Integer taetigkeitId = isTaetigkeitCell(cells[col]) ;
				if(taetigkeitId == null) continue ;
				
				ArrayList<SonderzeitenImportDto> importDtos = getHmPersonen().get(personalDto.getIId()) ;
				if(importDtos == null) {
					importDtos = new ArrayList<SonderzeitenImportDto>() ;
					getHmPersonen().put(personalDto.getIId(), importDtos) ;
				}
				
				SonderzeitenImportDto importDto = new SonderzeitenImportDto(
						personalDto.getIId(), taetigkeitId,
						Helper.cutTimestamp(new Timestamp(cal.getTimeInMillis())));
				importDto.setRow(row);
				importDto.setCol(col);
				importDto.setSource(sheet.getName());
				
				addImportDtos(importDtos, importDto) ;
			}
		}
	}

	/**
	 * Jene Spalten ermitteln, in denen ein Datum steht.</br>
	 * <p>Der Definition ist das Datum in der zweiten (menschlichen) Zeile. 
	 * Das erste Datum steht in der dritten Spalte (C)</p>
	 * 
	 * @param sheet das Arbeitsblatt
	 * @return eine (leere) Liste aller Spaltennummern in denen ein Datum steht
	 */
	private List<Integer> findDateCols(Sheet sheet) {
		List<Integer> dateCols = new ArrayList<Integer>() ;

		Cell[] cells = sheet.getRow(1) ;		
		for(int col = 2; col < cells.length; col++) {
			if(cells[col].getType() == CellType.DATE || cells[col].getType() == CellType.DATE_FORMULA) {
				dateCols.add(col) ;
			}
		}
		
		return dateCols ;
	}

	private int findNextDateCol(List<Integer> dateCols, int startCol) {
		for (Integer dateCol : dateCols) {
			if(dateCol > startCol) {
				return dateCol ;
			}
		}
		
		return -1 ;				
	}
	
	private void addImportDtos(ArrayList<SonderzeitenImportDto> importDtos, SonderzeitenImportDto addImportDto) {
		for (SonderzeitenImportDto importDto : importDtos) {
			if(importDto.gettDatum().equals(addImportDto.gettDatum())) {
				myLogger.warn("Bereits bekanntes Datum vorhanden '" + 
						addImportDto.gettDatum() + "', personalId '" + addImportDto.getPersonalIId() + "'.") ;
			}
		}
		
		importDtos.add(addImportDto) ;
	}
	
	private Integer isTaetigkeitCell(Cell c) throws ExceptionLP, Throwable {
		if(c.getType() == CellType.LABEL || c.getType() == CellType.NUMBER) {
			String contents = c.getContents() ;
			if(contents != null) {
				return (Integer) getHmTaetigkeiten().get(contents.trim()) ;
			}
		}
		
		return null ;
	}
	
	private HashMap<Integer, ArrayList<SonderzeitenImportDto>> getHmPersonen() {
		if(hmPersonen == null) {
			hmPersonen = new HashMap<Integer, ArrayList<SonderzeitenImportDto>>();		
		} 
		
		return hmPersonen ;
	}
	
	private HashMap<?,?> getHmTaetigkeiten() throws ExceptionLP, Throwable {
		if(hmTaetigkeiten == null) {
			hmTaetigkeiten  = DelegateFactory
					.getInstance().getZeiterfassungDelegate()
					.taetigkeitenMitImportkennzeichen() ; 
		}
		
		return hmTaetigkeiten ;
	}
}
