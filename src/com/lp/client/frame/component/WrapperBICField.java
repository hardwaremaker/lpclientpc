package com.lp.client.frame.component;

import com.lp.server.partner.service.BankFac;
import com.lp.util.BICValidator;

public class WrapperBICField extends WrapperTextField {
	private static final long serialVersionUID = -2387206065149524322L;

	private BICValidator bicValidator;
	
	public WrapperBICField() {
		super(BankFac.MAX_BIC);
	}
	
	public WrapperBICField(String bic) {
		super(bic, BankFac.MAX_BIC);
	}
	
	private BICValidator getBICValidator() {
		if (bicValidator == null) {
			bicValidator = new BICValidator();
		}
		return bicValidator;
	}
	
	public boolean pruefeBic() {
		String input = getText();
		
		if (!isMandatoryField() && (input == null || input.isEmpty())) {
			return true;
		}
		
		return getBICValidator().isValidBIC(input);
	}
}
