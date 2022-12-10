package com.lp.client.frame.filechooser;

import java.util.List;


public interface ChooserOpenDialogEnd<T> extends ChooserDialogEnd {

	/**
	 * Die Auswahl einer Datei durchf&uuml;hren.</br>
	 * <p>Die zugrundeliegende Klasse WrapperFileChooser pr&uuml;ft
	 * immer, ob die gew&auml;hlte Datei zum aktuellen Zeit existiert</p>
	 * <p>Zus&auml;tzlich wird auch &uuml;berpr&uuml;ft,
	 * da&szlig; die Datei eine der zuvor gew&auml;hlten 
	 * Dateiendungen hat.</p>
	 * @return
	 */
	T openSingle();
	
	/**
	 * Die Auswahl einer Datei durchf&uuml;hren.</br>
	 * <p>Die zugrundeliegende Klasse WrapperFileChooser pr&uuml;ft
	 * immer, ob die gew&auml;hlte Datei zum aktuellen Zeit existiert</p>
	 * <p>Eine weitere Pr&uuml;fung der Datei erfolgt nicht.</p>
	 * @return
	 */
	T openSingleUnchecked();
	
	List<T> openMultiple();
}
