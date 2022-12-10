package com.lp.client.angebotstkl.webabfrage;

import com.lp.client.frame.assistent.view.IDataUpdateListener;

public interface INeueLieferantenModelController {

	public void actionZuordnen(Integer webpartnerIId);
	
	public void registerNeueLieferantenDataUpdateListener(IDataUpdateListener listener);

}
