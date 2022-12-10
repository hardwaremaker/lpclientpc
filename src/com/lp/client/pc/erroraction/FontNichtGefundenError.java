package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.FontNichtGefundenException;
import com.lp.util.FontNichtGefundenJRDetailsException;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.fill.JRTemplatePrintText;

public abstract class FontNichtGefundenError implements IErrorAction {

	public FontNichtGefundenError() {
	}

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof FontNichtGefundenException) {
			FontNichtGefundenException exc = (FontNichtGefundenException)exception.getExceptionData();
			StringBuilder msgBuilder = new StringBuilder();
			appendMainErrorMessage(msgBuilder, exc);
			appendReportDetails(msgBuilder, exc);

			return msgBuilder.toString();
		}
		return null;
	}

	protected abstract void appendMainErrorMessage(StringBuilder msgBuilder, FontNichtGefundenException exc);
	
	protected void appendReportDetails(StringBuilder msgBuilder, FontNichtGefundenException exc) {
		if (exc instanceof FontNichtGefundenJRDetailsException) {
			FontNichtGefundenJRDetailsException detailsExc = (FontNichtGefundenJRDetailsException)exc;
			JRTemplatePrintText jrTextField = detailsExc.getTextField();
			msgBuilder.append("\n\n").append(LPMain.getMessageTextRespectUISPr(
					"lp.report.error.fontnichtgefunden.details.text", 
					cutText(jrTextField.getFullText(), 200)));
		}
	}

	protected String cutText(String text, int length) {
		return Helper.cutString(text, length) + (text.length() > length ? "..." : "");
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
