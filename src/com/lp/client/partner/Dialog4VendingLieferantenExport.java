package com.lp.client.partner;

import java.awt.Frame;

import javax.swing.table.TableModel;

import com.lp.client.artikel.IVendidataExportController;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.util.EJBExceptionData;
import com.lp.util.EJBExceptionLP;
import com.lp.util.FremdsystemnrNichtNummerischException;
import com.lp.util.KreditorKontoException;

public class Dialog4VendingLieferantenExport extends Dialog4VendingPartnerExport implements TableModel {
	private static final long serialVersionUID = 6508670295892998681L;

	public Dialog4VendingLieferantenExport(Frame owner, String title,
			boolean modal, IVendidataExportController<VendidataPartnerExportResult> controller) {
		super(owner, title, modal, controller);
		setFilenameSuggestion("4vending_lief.xml");
	}

	protected void initTableModel() {
		setColumnNames(new String[] {
				LPMain.getTextRespectUISPr("dialog.export.fehlernummer"),
				LPMain.getTextRespectUISPr("lp.kreditorennummer"),
				LPMain.getTextRespectUISPr("lp.lieferant"),
				LPMain.getTextRespectUISPr("dialog.export.fehlerbezeichnung")
			});
		setColumnClasses(new Class[] {Integer.class, Integer.class, String.class, String.class});
	}

	@Override
	protected TableModel getTableModel() {
		initTableModel();
		return this;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= getExportErrors().size()) return null;
		EJBExceptionLP ejbEx = getExportErrors().get(rowIndex);
		ExceptionLP ex = new ExceptionLP(ejbEx.getCode(), ejbEx.getMessage(), 
				ejbEx.getAlInfoForTheClient(), ejbEx.getCause(), ejbEx.getExceptionData());
		LieferantDto lieferantDto = getLieferantDto(ex.getExceptionData());
		
		if (0 == columnIndex) return ex.getICode();
		if (1 == columnIndex) return lieferantDto.getIKreditorenkontoAsIntegerNotiId();
		if (2 == columnIndex) return lieferantDto.getPartnerDto().getCKbez();
		if (3 == columnIndex) return LPMain.getInstance().getMsg(ex);
		
		return null;
	}

	private LieferantDto getLieferantDto(EJBExceptionData exceptionData) {
		if (exceptionData instanceof KreditorKontoException) {
			return ((KreditorKontoException)exceptionData).getLieferantDto();
		}

		if (exceptionData instanceof FremdsystemnrNichtNummerischException) {
			return ((FremdsystemnrNichtNummerischException)exceptionData).getLieferantDto();
		}
		return null;
	}

}
