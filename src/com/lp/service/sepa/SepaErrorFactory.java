package com.lp.service.sepa;

import com.lp.server.rechnung.service.sepa.errors.ISepaBankverbindungException;
import com.lp.server.rechnung.service.sepa.errors.ISepaException;
import com.lp.server.rechnung.service.sepa.errors.ISepaMandantException;
import com.lp.server.rechnung.service.sepa.errors.ISepaReBankException;
import com.lp.server.rechnung.service.sepa.errors.ISepaReException;
import com.lp.server.rechnung.service.sepa.errors.ISepaReKundeException;

public class SepaErrorFactory {

	public ISepaErrorReAction getSepaErrorRechnungAction(ISepaReKundeException sepaException) {
		return new SepaErrorRechnungKunde(sepaException);
	}
	
	public ISepaErrorReAction getSepaErrorRechnungAction(ISepaReBankException sepaException) {
		return new SepaErrorRechnungBank(sepaException);
	}

	public ISepaErrorReAction getSepaErrorRechnungAction(ISepaMandantException sepaException) {
		return new SepaErrorMandant(sepaException);
	}

	public ISepaErrorReAction getSepaErrorRechnungAction(ISepaBankverbindungException sepaException) {
		return new SepaErrorBankverbindungMandant(sepaException);
	}

	public ISepaErrorReAction getSepaErrorRechnungAction(ISepaReException sepaException) {
		return new SepaErrorRechnung(sepaException);
	}
	
	public ISepaErrorReAction getSepaErrorUnbekannt(ISepaException sepaException) {
		return new SepaErrorUnbekannt(sepaException);
	}
	
	public ISepaErrorReAction getSepaErrorRechnungAction(ISepaException sepaException) {
		if (sepaException instanceof ISepaReKundeException) {
			return getSepaErrorRechnungAction((ISepaReKundeException)sepaException);
		}
		if (sepaException instanceof ISepaReBankException) {
			return getSepaErrorRechnungAction((ISepaReBankException)sepaException);
		}
		if (sepaException instanceof ISepaMandantException) {
			return getSepaErrorRechnungAction((ISepaMandantException)sepaException);
		}
		if (sepaException instanceof ISepaReException) {
			return getSepaErrorRechnungAction((ISepaReException)sepaException);
		}
		if (sepaException instanceof ISepaBankverbindungException) {
			return getSepaErrorRechnungAction((ISepaBankverbindungException)sepaException);
		}
		
		return getSepaErrorUnbekannt(sepaException);
	}
}
