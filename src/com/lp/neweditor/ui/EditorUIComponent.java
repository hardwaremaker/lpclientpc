package com.lp.neweditor.ui;

import javax.swing.JComponent;

/**
 * Interface f&uuml;r neue Editor Komponenten. Viele neue Editor Komponenten
 * sind selbst keine Swing Components, um mehr Programmfreiheit und weniger
 * Membervariablen im Debugger zu erhalten, sondern geben mit der getUIComponent
 * den SwingComponent zur&uuml;ck. Wenn ein Editor Komponente schon ein Swing
 * Component ist, kann <code> this </code> zur&uuml;ck gegeben werden.
 * 
 * @author Alexander Daum
 *
 */
public interface EditorUIComponent {
	/**
	 * Gibt den Swing UI Component zur&uuml;ck, der mit diesem Editor UI Element
	 * angezeigt werden soll. <br>
	 * Ein Objekt muss hier immer den selben Component zur&uuml;ck geben (F&uuml;r
	 * zwei Objekte, die von dieser Methode zur&uuml;ck gegeben wurden, muss
	 * {@link Object#equals(Object)} <code>true</code> sein), andernfalls kann es zu
	 * Problemen mit der UI kommen, da alte Komponenten nicht mehr entfernt werden
	 * k&ouml;nnen!
	 * 
	 * @return
	 */
	JComponent getUIComponent();
}
