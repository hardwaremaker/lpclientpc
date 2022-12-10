package com.lp.client.angebotstkl.webabfrage;

import java.util.List;

public interface IWebabfrageCallback {

	public void updateNumberOfRequests(Integer numberOfRequests);
	
	public void processParts(List<NormalizedWebabfragePart> parts);
	
	public void processNoResultFound(Integer searchId);
	
	public void done(List<WebabfrageError> errors, List<WebabfrageException> exceptions);
}
