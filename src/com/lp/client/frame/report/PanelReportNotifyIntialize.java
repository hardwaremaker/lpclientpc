package com.lp.client.frame.report;

/**
 * Interface fuer Report Klassen die wissen muessen wann alles initialisiert
 * ist.
 * 
 * @author Alexander Daum
 *
 */
public interface PanelReportNotifyIntialize {
	/**
	 * Wird aufgerufen wenn die initialisierung der Komponente fertig ist
	 */
	void setInitialized();
}
