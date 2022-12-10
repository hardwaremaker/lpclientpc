package com.lp.neweditor.data;

import com.lp.neweditor.event.ModelChangedListener;

/**
 * Interface fuer Klassen, die DataChanged Events bereitstellen
 * 
 * @author Alexander Daum
 *
 * @param <ModelType>
 */
public interface IModelChangedProvider<ModelType> {
	/**
	 * F&uuml;gt einen Listener f&uuml;r ModelChange hinzu
	 * 
	 * @param listener
	 */
	void addModelChangeListener(ModelChangedListener<ModelType> listener);

	/**
	 * Entfernt einen Listener f&uuml;r ModelChange
	 * 
	 * @param listener
	 */
	void removeModelChangeListener(ModelChangedListener<ModelType> listener);

	/**
	 * Gibt das aktuelle Model zur&uuml;ck
	 * 
	 * @return
	 */
	ModelType getModel();

	/**
	 * Setzt das aktuelle Model
	 * <hr>
	 * Achtung bei Typen, die als Generic verwendet werden. Als Nebeneffekt des
	 * Generics, kann die setModel Methode nicht aufgerufen werden, wenn der Typ
	 * <code> &lt;? extends ...&gt; </code> oder <code> &lt;?&gt; </code> (was das
	 * gleiche bedeutet, wie <code> &lt;? extends Object&gt; </code>) ist. Das
	 * Problem liegt hier dabei, dass der ModelType dieses Objekts irgendeine
	 * Subklasse des gegebenen Capture Types sein kann, aber nicht bekannt ist,
	 * welcher. Zuweisen eines Objektes einer falschen Klasse w&uuml;rde zu <i> Heap
	 * Pollution </i> f&uuml;hren. Potentiell wird dadurch erst sp&auml;ter im
	 * Programmablauf eine Exception geworfen, und der Fehler ist nicht auf diesen
	 * Methodenaufruf zur&uuml;ck f&uuml;hrbar <br>
	 * Ist bekannt welches Model wirklich ben&ouml;tigt wird, kann der Controller
	 * zuerst auf einen bestimmten Typ gecastet werden.
	 * 
	 * @param model
	 */
	void setModel(ModelType model);

}