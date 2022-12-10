package com.lp.client.frame.filechooser.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
 * <p><T> soll hier nur den Unterschied zwischen FileExtension
 * Filtern und "StringExtension" Filtern erm&ouml;glichen.</p>
 * @author ghp
 */
public abstract class HvTaggedFileBaseAcceptor<T> {
	private List<T> extensions = new ArrayList<T>();
	
	public HvTaggedFileBaseAcceptor(T element) {
		extensions.add(element);
	}
	
	public HvTaggedFileBaseAcceptor<T> add(T element) {
		extensions.add(element);
		return this;
	}
	
	public List<T> getExtensions() {
		return extensions;
	}
	
	public List<String> getStringExtensions() {
		List<String> entries = new ArrayList<String>();
		for (T extension : getExtensions()) {
			entries.add(asString(extension));
		}
		return entries;
	}
	
	public boolean accept(File f) {
		for (T extension : extensions) {
			if (acceptName(f.getName(), extension)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Die m&ouml;glichen Filextensions in einer menschlich
	 * lesbaren Form liefern</br>
	 * <p>(*.jpg, *.jpeg)</p>
	 */
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		for (T extension : extensions) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("*.").append(asString(extension));
		}
		
		return "(" + sb.toString() + ")";
	}
	
	protected boolean acceptName(String filename, T extension) {
		return filename.toLowerCase().endsWith(asString(extension));
	}
	
	/**
	 * Die Extension als String zur&uuml;ckgeben</br>
	 * 
	 * @param element
	 * @return die angegebene Extension als String
	 */
	protected abstract String asString(T element); 
	
	/**
	 * Pr&uuml;ft ob die Datei die angegebene Extension hat</br>
	 * <p>Gross- bzw. Kleinschreibung spielt keine Rolle</p>
	 * 
	 * @param f ist die zu pr&uuml;fende Datei
	 * @param extension die zu pr&uuml;fende Dateierweiterung
	 * @return true wenn die Datei mit der zu pr&uuml;fenden 
	 * Dateierweiterung endet
	 */
	public boolean hasExtension(File f, T extension) {
		return acceptName(f.getName(), extension);
	}
	
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
		if (extensions.isEmpty()) return f;

		T extension = extensions.get(0);
		if (hasExtension(f, extension)) return f;

		String ext = asString(extension);
		if (!ext.startsWith(".")) {
			ext = "." + ext;
		}
		
		return new File(f.getAbsolutePath() + ext);
	}
}
