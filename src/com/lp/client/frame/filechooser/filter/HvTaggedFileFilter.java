package com.lp.client.frame.filechooser.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Ein Dateifilter der einen Dateitypen abbildet.</br>
 * <p>Ein Dateityp kann mehrere Extensions haben wie zum
 * Beispiel bei "JPG" Dateien, bei denen man implizit davon
 * ausgeht, dass sowohl die Extension ".jpg" als auch ".jpeg"
 * akzeptiert wird. Oder "TIF" wo sowohl ".tiff" als auch ".tif"
 * korrekt ist.</p>
 * 
 * <p>Ein "CsvFilter" besteht also im Idealfall aus der realen
 * Klasse CsvFilter und den dort definierten "Acceptors" der
 * aus ".csv" besteht.</p>
 * 
 * <p>Ein "Excelfilter" k&ouml;nnte aus Acceptors von ".xls",
 * ".xlsx" und auch ".csv" bestehen.</p>
 * 
 * <p>Gerade bei mehreren Acceptors ist es
 * nach der vom Benutzer getroffenen Auswahl einer Datei wichtig
 * zu wissen, welcher Acceptor (Extension) entschieden hat, 
 * dass er daf&uuml;r zust&auml;ndig ist. Siehe oben, es war
 * zwar ein "Excelfile", aber es ist eine .csv gewesen.</p>
 * 
 * @author ghp
 */
public abstract class HvTaggedFileFilter extends FileFilter {

	private final HvTaggedFileBaseAcceptor<?> acceptor;
	
	public HvTaggedFileFilter(HvTaggedFileBaseAcceptor<?> acceptor) {
		this.acceptor = acceptor;
	}
	
	
	@Override
	public boolean accept(File f) {
		return f.isDirectory() || acceptor().accept(f);
	}

	/**
	 * Die Beschreibung dieses Filters</br>
	 * <p>TaggedFileFilter liefern als Beschreibung 
	 * zuerst den Tag, dann ein Leerzeichen und dann
	 * in in runden Klammern umschlossen die Extensions</p>
	 * <p>Excel (.xls, .xlsx)</p>
	 * 
	 *  @return "TAG (*.extension, *.extension)"
	 */
	@Override
	public String getDescription() {
		return getTag() + " " + acceptor().getDescription();
	}

	public HvTaggedFileBaseAcceptor<?> acceptor() {
		return this.acceptor;
	}
	
	/**
	 * Liefert den "Tag" dieses Filters</br>
	 * <p>Beispiel: "JPG" oder auch "Excel"
	 * oder "Bilder"</p>
	 * @return
	 */
	public abstract String getTag();
	
	/**
	 * H&auml;ngt die erste der Erweiterungen an die Datei,
	 * sofern die Datei diese nicht bereits hat.</br>
	 * <p>Die JFileChooser ist beim showSaveDialog etwas
	 * merkw&uuml;rdig, er akzeptiert Dateinamen ohne die
	 * Extension die im gew&auml;hlten Filter eigentlich
	 * gefordert wird.</p>
	 * <p>Hat dieser Acceptor keine Erweiterungen, wird die
	 * Datei unver&auml;ndert zur&uuml;ckgegeben</p>
	 * 
	 * @param f die Ausgangsdatei - bzw. der Name, die 
	 *  Datei muss ja noch nicht zwangsl&auml;fig existieren.
	 *  
	 * @return eine Datei mit der ersten Erweiterung
	 */
	public File appendExtension(File f) {
		return acceptor().appendExtension(f);
	}
}
