package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.Map;

public interface IWebabfrageViewController {
	
	public WebabfrageResultTableModel getWebabfrageResultTableModel();
	
	public Map<Integer, BigDecimal> getEinkaufsangebotMengenstaffel();
	
	public void actionStartWebabfrageAlle();
	
	public void actionStartWebabfrageSelektierte(int[] is);
	
	public void actionCancelWebabfrage();
	
	public void actionRefreshResultTable();
	
	public void registerWebabfrageControlListener(IWebabfrageControlListener listener);

	public void actionMengenstaffelSelected(Integer selectedMengenstaffel);
}
