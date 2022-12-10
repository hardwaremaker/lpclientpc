package com.lp.client.finanz.sepaimportassistent;

import com.lp.server.finanz.service.ISepaImportResult;

public interface ISepaCellEditorController {

	public void dataUpdated();
	
	public void actionNewSelection(ISepaImportResult result, Integer previousSelection);
}
