package com.lp.client.partner;

import java.awt.Frame;

import javax.swing.table.TableModel;

import com.lp.client.artikel.IVendidataExportController;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.util.DebitorKontoException;
import com.lp.util.EJBExceptionData;
import com.lp.util.EJBExceptionLP;
import com.lp.util.FremdsystemnrNichtNummerischException;

public class Dialog4VendingKundenExport extends Dialog4VendingPartnerExport {

	private static final long serialVersionUID = -7550087660178570703L;

	public Dialog4VendingKundenExport(Frame owner, String title, boolean modal,
			IVendidataExportController<VendidataPartnerExportResult> controller) {
		super(owner, title, modal, controller);
		setFilenameSuggestion("4vending_kunden.xml");
	}

	protected void initTableModel() {
		setColumnNames(new String[] {
				LPMain.getTextRespectUISPr("dialog.export.fehlernummer"),
				LPMain.getTextRespectUISPr("lp.debitorennummer"),
				LPMain.getTextRespectUISPr("lp.kunde"),
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
		EJBExceptionLP ex = getExportErrors().get(rowIndex);
		KundeDto kundeDto = getKundeDto(ex.getExceptionData());
		
		if (0 == columnIndex) return ex.getCode();
		if (1 == columnIndex) return kundeDto != null ? kundeDto.getIDebitorenkontoAsIntegerNotiId() : null;
		if (2 == columnIndex) return kundeDto != null ? kundeDto.getPartnerDto().getCKbez() : null;
		if (3 == columnIndex) return LPMain.getInstance().getMsg(new ExceptionLP(ex.getCode(),
				ex.getMessage(), ex.getAlInfoForTheClient(), ex.getCause(), ex.getExceptionData()));
		
		return null;
	}

	private KundeDto getKundeDto(EJBExceptionData exceptionData) {
		if (exceptionData instanceof DebitorKontoException)
			return ((DebitorKontoException)exceptionData).getKundeDto();
		
		if (exceptionData instanceof FremdsystemnrNichtNummerischException) 
			return ((FremdsystemnrNichtNummerischException)exceptionData).getKundeDto();
		
		return null;
	}
}
