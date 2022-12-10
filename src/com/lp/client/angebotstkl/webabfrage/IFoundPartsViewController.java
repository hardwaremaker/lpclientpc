package com.lp.client.angebotstkl.webabfrage;


public interface IFoundPartsViewController {

	public FoundPartsTableModel getFoundPartsTableModel();
	
	public void actionResultSelected(IWebabfrageResult result);

	public void actionFoundPartSelected(NormalizedWebabfragePart part);
	
	public void actionSetFoundPartToResult();

	public void actionFilterNurWeblieferanten();

	public void actionFilterAlleLieferanten();
	
	public void actionFilterBezeichnung(String text);

	public Integer getSelectedResultRow();

	public void actionMengenstaffelSelected();
}
