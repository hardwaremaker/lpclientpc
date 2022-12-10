package com.lp.service.findchips;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Anfragen bei findchips durchf&uuml;hren</br>
 * <p>Die Findchips Api stellt eine REST API zur Verf&uuml;gung, die benutzt werden kann, um
 * Informationen zu elektronischen Bauteilen (Verf&uuml;gbarkeit, Preise, ...) zu erhalten.</p>
 * 
 * <p>Es k&ouml;nnen mehrere Bauteile gleichzeitig angefragt werden, dies wird &uuml;ber den
 * FindchipsApiController erm&ouml;glicht.</p>
 * 
 * @author Gerold
 */
public interface IFindchipsApi {
	/**
	 * Eine Findchips Anfrage durchf&uuml;hren und auf das Ergebnis warten</br>
	 * <p>Es wird explizit ein FindchipsApiRequest aus der partnr erzeugt</p>
	 * @param partnr ist der zu suchende Baustein bzw. Teile des Namens davon
	 * @return die (nicht null) Antwort
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	FindchipsApiResponse findByPartnr(String partnr) throws InterruptedException, ExecutionException ;

	/**
	 * Eine Findchips Anfrage durchf&uuml;hren und auf das Ergebnis warten</br>
	 * <p>Der Aufrufer wird dabei blockiert</p>
	 * @param request ist der zu suchende Baustein bzw. Teile des Namens davon
	 * @return die (nicht null) Antwort
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	FindchipsApiResponse findByPartnr(FindchipsApiRequest request) throws InterruptedException, ExecutionException ;
	
	/**
	 * Eine Findchips Anfrage durchf&uuml;hren und auf das Ergebnis warten</br>
	 * <p>Sobald die Information vom Service vorliegt, wird der callback aufgerufen. Der Aufrufer wird blockiert.
	 * Es empfielt sich, diesen Aufruf aus einem SwingWorker aus auszurufen</p>
	 * @param request ist der zu suchende Baustein bzw. Teile des Namens davon
	 * @param callback ist die Methode die aufgerufen werden soll, sobald die Antwort vorliegt
	 * @return die (nicht null) Antwort
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	FindchipsApiResponse findByPartnr(FindchipsApiRequest request, 
			IFindchipsResponseCallback callback) throws InterruptedException, ExecutionException ;
	
	/**
	 * Eine Findchips Anfrage f&uuml;r mehrere Bauteile durchf&uuml;hren und auf das Ergebnis warten</br>
	 * <p>Sobald die Information eines angefragten Bauteils vom Service vorliegt, wird der callback aufgerufen. 
	 * Der Aufrufer wird blockiert. Es empfielt sich, diesen Aufruf aus einem SwingWorker aus auszurufen</p>
	 * @param requests eine Liste von Anfragen
	 * @param callback die Methode die aufgerufen werden soll, sobald eine Information vom Service f&uuml;r eine
	 *  der Anfragen vorliegt
	 * @throws InterruptedException
	 */
	void findByPartnr(List<FindchipsApiRequest> requests,
			IFindchipsResponseCallback callback) throws InterruptedException ;
	
	/**
	 * Eine Findchips Anfrage f&uuml;r mehrere Bauteile durchf&uuml;hren.</br>
	 * <p>Der Aufrufer wird nicht blockiert. Sobald die Information eines angefragten Bauteils vom Service vorliegt,
	 *  wird der callback aufgerufen. Es empfielt sich, diesen Aufruf aus einem SwingWorker/thread aus auszurufen</p>
	 * @throws InterruptedException
	 * @param requests eine Liste von Anfragen
	 * @param callback die Methode die aufgerufen werden soll, sobald eine Information vom Service f&uuml;r eine
	 *  der Anfragen vorliegt
	 * @return Information &uuml;ber die Ausf&uuml;hrung. Kann dann verwendet werden, um die anstehenden
	 * Anfragen durch {@link #cancel(FindRequestExecutor)} abzubrechen.
	 * @throws InterruptedException
	 */
	FindRequestExecutor findByPartnrAsynch(List<FindchipsApiRequest> requests,
			IFindchipsResponseCallback callback) throws InterruptedException ;	
	
	/**
	 * Eine laufende Anfrage abbrechen
	 * @param executor ist jene Information, die durch {@link #findByPartnrAsynch(List, IFindchipsResponseCallback)}
	 * geliefert wurde
	 */
	void cancel(FindRequestExecutor executor) ;
	
	/**
	 * Auf die Beendigung der Anfrage(n) warten
	 * @param executor ist jene Information, die durch {@link #findByPartnrAsynch(List, IFindchipsResponseCallback)}
	 * geliefert wurde
	 */
	void awaitTermination(FindRequestExecutor executor) ;	
}
