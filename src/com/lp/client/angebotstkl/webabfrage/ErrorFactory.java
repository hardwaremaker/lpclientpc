package com.lp.client.angebotstkl.webabfrage;

import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.service.findchips.FindchipsApiRequest;

public class ErrorFactory {

	public WebabfrageError createError(FindchipsApiRequest request) {
		WebabfrageError error = new WebabfrageError(
				request.getId(), request.getPartnr(), AngebotstklFac.WebabfrageTyp.FINDCHIPS);
		
		return error;
	}
	
	public WebabfrageException createException(FindchipsApiRequest request, Exception e) {
		WebabfrageException exception = new WebabfrageException(
				request.getId(), request.getPartnr(), AngebotstklFac.WebabfrageTyp.FINDCHIPS, e);
		
		return exception;
	}
}
