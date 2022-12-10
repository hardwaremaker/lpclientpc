package com.lp.client.finanz;

public interface IWaehrungListener {

	public void updateWaehrung(String waehrungCNr);
	
	public void enableWaehrung(Boolean enable);
}
