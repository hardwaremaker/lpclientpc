package com.lp.client.finanz;

import com.lp.client.frame.assistent.view.IDataUpdateListener;

public interface ISepakontoauszugImportController {

	String getBankverbindungName();

	String[] getCamtFormate();

	void setAuszugsnummer(Integer integer);

	boolean isImportPossible();

	void setSelectedCamtFormat(int selectedIndex);

	public void registerDataUpdateListener(IDataUpdateListener listener);

	boolean shouldShowAuszugsnummer();
	
	public void actionImport();

	String getImportMessages();

	boolean shouldCloseDialog();

	String getSepaVerzeichnis();

	int getCamtFormatSelection();

	String getTextLetzteBuchung();
}
