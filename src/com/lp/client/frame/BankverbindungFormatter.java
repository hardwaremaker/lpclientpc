package com.lp.client.frame;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.Helper;

public class BankverbindungFormatter {

	public String formatNummerFuerTextfeldEinerAuswahl(BankverbindungDto bankverbindungDto) {
		if (bankverbindungDto == null) return null;
		
		if (!Helper.isStringEmpty(bankverbindungDto.getCBez())) 
			return bankverbindungDto.getCBez();
		
		if (!Helper.isStringEmpty(bankverbindungDto.getCKontonummer())) 
			return bankverbindungDto.getCKontonummer();

		if (!Helper.isStringEmpty(bankverbindungDto.getCIban()))
			return bankverbindungDto.getCIban();
		
		try {
			PartnerDto partner = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(bankverbindungDto.getBankIId());
			return partner.formatFixName1Name2();
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e, DialogError.TYPE_ERROR);
		}
		return null;
	}
}
