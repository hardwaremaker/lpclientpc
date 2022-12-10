package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.List;

public interface IFoundPartsTableModelController {

	public void registerDataUpdateListener(IFoundPartsTableModelListener listener);
	
	public List<NormalizedWebabfragePart> getParts();
	
	public IWebabfrageResult getSelectedPosition();

	public void actionUrlButtonPressed(String data);

	public BigDecimal getSelectedMenge();
}
