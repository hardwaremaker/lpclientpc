package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.List;


public interface IWebabfrageResultModelController {

	public void registerDataUpdateListener(IWebabfrageResultTableUpdateListener listener);
	
	public List<IWebabfrageResult> getWebabfragePositionen();

	public BigDecimal getSelectedEinkaufsangebotMenge();
}
