package com.lp.client.pc.erroraction;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;

public class ReportvarianteNichtGefundenError implements IErrorAction {

	public ReportvarianteNichtGefundenError() {
	}

	@Override
	public String getMsg(ExceptionLP ex) {
		List<?> allInfos = ex.getAlInfoForTheClient();
		return LPMain.getMessageTextRespectUISPr("lp.drucken.error.reportvarianteunbekannt", allInfos.toArray());
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
