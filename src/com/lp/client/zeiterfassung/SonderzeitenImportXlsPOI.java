
package com.lp.client.zeiterfassung;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.SonderzeitenImportDto;
import com.lp.util.Helper;

public class SonderzeitenImportXlsPOI {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private HashMap<Integer, ArrayList<SonderzeitenImportDto>> hmPersonen;
	private HashMap<?, ?> hmTaetigkeiten;

	private Date startDate;
	private Date endDate;

	public SonderzeitenImportXlsPOI() {
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void importFile(File xlsFile, Date startDate, Date endDate)
			throws IndexOutOfBoundsException, ExceptionLP, Throwable {
		this.startDate = startDate;
		this.endDate = endDate;

		if (startDate == null) {
			myLogger.warn("Ignoriere Datei '" + xlsFile.getName() + " da Startdatum nicht gesetzt");
			return;
		}

		if (endDate == null) {
			myLogger.warn("Ignoriere Datei '" + xlsFile.getName() + " da Endedatum nicht gesetzt");
			return;
		}

		this.endDate = new Date(endDate.getTime() + 24 * 3600000);

		FileInputStream fis = new FileInputStream(xlsFile);

		// Finds the workbook instance for XLSX file
		HSSFWorkbook myWorkBook = new HSSFWorkbook(fis);

		for (int i = 0; i < myWorkBook.getNumberOfSheets(); i++) {

			// Return first sheet from the XLSX workbook
			HSSFSheet mySheet = myWorkBook.getSheetAt(i);

			importSheet(mySheet, this.startDate, this.endDate);
		}
	}

	public HashMap<Integer, ArrayList<SonderzeitenImportDto>> getImportData() {
		return hmPersonen;
	}

	protected void importSheet(HSSFSheet sheet, Date startDate, Date endDate) throws ExceptionLP, Throwable {
		myLogger.info("Arbeitsblatt '" + sheet.getSheetName() + "' verarbeiten...");

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		if (sheet.getPhysicalNumberOfRows() < 7) {
			myLogger.warn("Arbeitsblatt '" + sheet.getSheetName() + "' wird ignoriert (Zeilen: '"
					+ sheet.getPhysicalNumberOfRows() + "' (mindestens 7)");
			return;
		}

		// Spaltenanzahl
		Row r = rowIterator.next();
		if (r.getLastCellNum() < 4) {
			myLogger.warn("Arbeitsblatt '" + sheet.getSheetName() + "' wird ignoriert Spalten '"
					+ r.getPhysicalNumberOfCells() + "' (mindestens 4).");
			return;
		}

		List<Integer> dateCols = findDateCols(sheet);

		for (int row = 6; row < sheet.getPhysicalNumberOfRows(); row++) {

			Row zeile = sheet.getRow(row);

			PersonalDto personalDto = null;
			java.util.Date xlsStartDate = null;
			int persCol = -1;

			myLogger.info("Arbeitsblatt '" + sheet.getSheetName() + "', Zeile '" + row + "' verarbeiten...");

			for (int col = 0; col < zeile.getPhysicalNumberOfCells(); col++) {
				if (personalDto == null) {

					org.apache.poi.ss.usermodel.Cell cell = zeile.getCell(col);

					String persnr = null;

					if (cell != null) {
						cell.setCellType(CellType.STRING);

						persnr = cell.getStringCellValue();
					}

					if (persnr != null && persnr.trim().length() > 0) {
						xlsStartDate = null;
						personalDto = DelegateFactory.getInstance().getPersonalDelegate()
								.personalFindByCPersonalnrMandantCNrOhneExc(persnr);
						if (personalDto == null) {
							myLogger.warn("Ignoriere unbekannte Personalnummer '" + persnr + "' in Zeile '" + row
									+ "', Spalte '" + col + "'.");
							break;
						}

						persCol = col;
						myLogger.info("Personalnummer '" + persnr + "' in Spalte '" + persCol + "' verarbeiten...");
					} else {
						myLogger.warn("Keine Personalnummer in Zeile '" + row + "', Spalte '" + col + "'.");
						// Die naechste Spalte ermitteln, in der wieder ein Datum stehen sollte
						col = findNextDateCol(dateCols, col + 2) - 2 - 1;
						myLogger.warn("Naechste Datumsspalte '" + col + "'.");
						if (col <= 0) {
							break;
						} else {
							continue;
						}
					}

				}

				if (xlsStartDate == null) {
					if (persCol < 0) {
						myLogger.warn("Kein Personalnummer erkannt, und trotzdem Datum laden?");
						break;
					}

					org.apache.poi.ss.usermodel.Cell c = sheet.getRow(1).getCell(persCol + 2);

					if (c.getCellType() ==CellType.FORMULA) {

						  System.out.println("Formula is " + c.getCellFormula());
					        switch(c.getCachedFormulaResultType()) {
					            case NUMERIC:
					                System.out.println("Last evaluated as: " + c.getNumericCellValue());
					                break;
					            case STRING:
					                System.out.println("Last evaluated as \"" + c.getRichStringCellValue() + "\"");
					                break;
					        }
						
					}
					
					if (c.getCellType() == CellType.NUMERIC
							&& HSSFDateUtil.isCellDateFormatted(c)) {
						xlsStartDate = c.getDateCellValue();
						col += 2;
						continue;
					}

					myLogger.warn("Ignoriere fehlendes Startdatum '" + c.getStringCellValue() + "' in Zeile '" + (1)
							+ "', Spalte '" + (col + 2) + "'.");
					break;

				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(xlsStartDate);

				int daysInMonth = col - persCol - 2 - 1;
				if (daysInMonth >= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					xlsStartDate = null;
					personalDto = null;
					continue;
				}

				cal.add(Calendar.DATE, daysInMonth);
				myLogger.debug("Tag '" + cal.get(Calendar.DAY_OF_MONTH) + "'.");
				long l = Helper.cutTimestamp(new Timestamp(cal.getTimeInMillis())).getTime();
				if (l >= endDate.getTime()) {
					break;
				}

				if (l < startDate.getTime()) {
					continue;
				}

				Integer taetigkeitId = isTaetigkeitCell(zeile.getCell(col));
				if (taetigkeitId == null)
					continue;

				ArrayList<SonderzeitenImportDto> importDtos = getHmPersonen().get(personalDto.getIId());
				if (importDtos == null) {
					importDtos = new ArrayList<SonderzeitenImportDto>();
					getHmPersonen().put(personalDto.getIId(), importDtos);
				}

				SonderzeitenImportDto importDto = new SonderzeitenImportDto(personalDto.getIId(), taetigkeitId,
						Helper.cutTimestamp(new Timestamp(cal.getTimeInMillis())));
				importDto.setRow(row);
				importDto.setCol(col);
				importDto.setSource(sheet.getSheetName());

				addImportDtos(importDtos, importDto);
			}
		}
	}

	/**
	 * Jene Spalten ermitteln, in denen ein Datum steht.</br>
	 * <p>
	 * Der Definition ist das Datum in der zweiten (menschlichen) Zeile. Das erste
	 * Datum steht in der dritten Spalte (C)
	 * </p>
	 * 
	 * @param sheet das Arbeitsblatt
	 * @return eine (leere) Liste aller Spaltennummern in denen ein Datum steht
	 */
	private List<Integer> findDateCols(HSSFSheet sheet) {
		List<Integer> dateCols = new ArrayList<Integer>();

		Row row = sheet.getRow(1);

		for (int col = 2; col < row.getPhysicalNumberOfCells(); col++) {

			org.apache.poi.ss.usermodel.Cell cell = row.getCell(col);

			if (cell != null && cell.getCellType() == CellType.NUMERIC
					&& HSSFDateUtil.isCellDateFormatted(cell)) {
				dateCols.add(col);
			}

		}

		return dateCols;
	}

	private int findNextDateCol(List<Integer> dateCols, int startCol) {
		for (Integer dateCol : dateCols) {
			if (dateCol > startCol) {
				return dateCol;
			}
		}

		return -1;
	}

	private void addImportDtos(ArrayList<SonderzeitenImportDto> importDtos, SonderzeitenImportDto addImportDto) {
		for (SonderzeitenImportDto importDto : importDtos) {
			if (importDto.gettDatum().equals(addImportDto.gettDatum())) {
				myLogger.warn("Bereits bekanntes Datum vorhanden '" + addImportDto.gettDatum() + "', personalId '"
						+ addImportDto.getPersonalIId() + "'.");
			}
		}

		importDtos.add(addImportDto);
	}

	private Integer isTaetigkeitCell(org.apache.poi.ss.usermodel.Cell c) throws ExceptionLP, Throwable {
		if (c.getCellType() == CellType.STRING
				|| c.getCellType() == CellType.NUMERIC) {
			c.setCellType(CellType.STRING);
			String contents = c.getStringCellValue();
			if (contents != null) {
				return (Integer) getHmTaetigkeiten().get(contents.trim());
			}
		}

		return null;
	}

	private HashMap<Integer, ArrayList<SonderzeitenImportDto>> getHmPersonen() {
		if (hmPersonen == null) {
			hmPersonen = new HashMap<Integer, ArrayList<SonderzeitenImportDto>>();
		}

		return hmPersonen;
	}

	private HashMap<?, ?> getHmTaetigkeiten() throws ExceptionLP, Throwable {
		if (hmTaetigkeiten == null) {
			hmTaetigkeiten = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.taetigkeitenMitImportkennzeichen();
		}

		return hmTaetigkeiten;
	}
}
