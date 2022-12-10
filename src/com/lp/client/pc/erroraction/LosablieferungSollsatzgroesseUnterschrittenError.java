package com.lp.client.pc.erroraction;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosDto;

public class LosablieferungSollsatzgroesseUnterschrittenError implements IErrorAction {

	public LosablieferungSollsatzgroesseUnterschrittenError() {
	}

	@Override
	public String getMsg(ExceptionLP ex) {
		List<?> allInfos = ex.getAlInfoForTheClient();
		LosDto losDto = (LosDto) allInfos.get(1);
		//BigDecimal mengeAblieferung = (BigDecimal)allInfos.get(2);
		
		return LPMain.getMessageTextRespectUISPr("fert.ablieferung.sollsatzgroesseunterschritten.error", losDto.getCNr());
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
