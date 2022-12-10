package com.lp.client.angebotstkl.webabfrage;

import com.lp.client.frame.assistent.view.IDataUpdateListener;

public interface IBekannteLieferantenModelController {

	public void actionAddEkWeblieferant(Integer webpartnerIId);
	
	public void actionRemoveEkweblieferant(Integer weblieferantIId);
	
	public void registerBekannteLieferantenDataUpdateListener(IDataUpdateListener listener);
}
