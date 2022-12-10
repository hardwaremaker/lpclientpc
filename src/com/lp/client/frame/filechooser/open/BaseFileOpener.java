package com.lp.client.frame.filechooser.open;

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.FileExtensionAcceptor;
import com.lp.client.pc.LPMain;
import com.lp.server.util.HvOptional;

/**
 * Basisklasse, die dem Anwender die M&ouml;glichkeit gibt,
 * eine Datei zu w&auml;hlen. Nachdem die Auswahl getroffen
 * wurde, wird &uuml;berpr&uuml;ft, ob die Datei auch tats&auml;chlich
 * existiert und akzeptiert werden kann. 
 * 
 * <p>Das <code>token</token> erm&ouml;glicht die Funktionalit&auml;t,
 * dass das bei der Auswahl der Datei verwendete Verzeichnis f&uuml;r
 * sp&auml;tere Wiederverwendung gespeichert wird.</p>
 * 
 * <p>Mit <code>openSingle</code> kann eine Datei ausgew&auml;hlt 
 * werden.</p>
 * 
 * @author ghp
 *
 * @param <T> ist die WrapperFile-Klasse wie zum Beispiel
 *   CsvFile, XlsFile, ...
 */
public abstract class BaseFileOpener<T extends WrapperFile> {
	private final Component parent;
	private final FileChooserConfigToken token;
	private final FileExtensionAcceptor acceptor;
	
	protected BaseFileOpener(FileExtensionAcceptor acceptor,
			Component parent, FileChooserConfigToken token) {
		this.parent = parent;
		this.token = token;
		this.acceptor = acceptor;
	}
	
	/**
	 * Liefert die vom Anwender gew&auml;hlte Datei. 
	 * Es kann nur eine Datei ausgew&auml;hlt werden.</br>
	 * <p>Hat der Anwender keine Datei gew&auml;hlt (Auswahl
	 * unterbrochen) wird sofort beendet.</p>
	 * <p>Wurde eine Datei gew&auml;hlt, wird gepr&uuml;ft,
	 * ob diese Datei existiert und die DateiExtension 
	 * jener entspricht, die erwartet wird. Falls eine 
	 * dieser Bedingungen nicht zutrifft, wird der Anwender 
	 * darauf hingewiesen und er hat die M&ouml;glichkeit, die 
	 * Auswahl nochmals durchzuf&uuml;hren.</p>
	 * 
	 * @return HvOptional.empty wenn der Anwender keine 
	 * Datei gew&auml;hlt oder die Wahl unterbrochen hat. 
	 * Ansonsten HvOptional<Datei>
	 */
	public HvOptional<T> selectSingle() { 
		for(;;) {
			T someFile = openSingle(parent, token);

			if (someFile == null || !someFile.hasFile()) {
				return HvOptional.empty();
			}
/*			
			boolean ok = acceptsFile(someFile);
			if (!ok && ! shouldTryAgainDoesntAccept(acceptor(), someFile)) {
				return HvOptional.empty();
			}	
*/			
			return HvOptional.of(someFile);
		}		
	}
	
	public HvOptional<T> selectSingleUnchecked() {
		for(;;) {
			T someFile = openSingleUnchecked(parent, token);

			if (someFile == null || !someFile.hasFile()) {
				return HvOptional.empty();
			}

			return HvOptional.of(someFile);
		}				
	}
	
	/*
	 FileChooserBuilder.createOpenDialog(token, parent)
					.addXlsFilter().openSingle()
	 */
	protected abstract T openSingle(Component parent, FileChooserConfigToken token);

	protected abstract T openSingleUnchecked(Component parent, FileChooserConfigToken token);
	
	/**
	 * Prueft, ob die Datei den Kriterien entspricht.</br>
	 * <p>Angenommen wir haben den Filter "*.xls" + "alle".
	 * Dann wuerde der Filter beim Auswaehlen der Datei 
	 * dies zulassen. Der Anwender waehlt jetzt eine ".csv"
	 * Datei aus (obwohl er weiss, dass er xls auswaehlen 
	 * sollte), dann soll spaetestens jetzt diese Datei 
	 * nicht zugelassen werden.</p>
	 * 
	 * <p>Ich frage mich zwar, warum man dann im Filter nicht
	 * hart "*.xls" selektiert, aber was weiss ich schon.</p>
	 * 
	 * @param wrapperFile
	 * @return
	 */
	protected boolean acceptsFile(T wrapperFile) {
		return acceptor().accept(wrapperFile.getFile());		
	}

	/**
	 * Prueft ob es sich um eine lesbare Datei handelt
	 * @param wrapperFile die zu pruefende Datei
	 * @return true wenn es sich um eine Datei handelt,
	 * 	und diese tatsaechlich existiert. Ein Verzeichnis
	 *  ist keine Datei.
	 */
	protected boolean existsFile(T wrapperFile) {
		File f = wrapperFile.getFile();
		return f.exists() && f.isFile(); 
	}
	
	
	/**
	 * Der Anwender erh&auml;lt einen Dialog, der in dar&uuml;ber
	 * informiert, dass die von ihm gew&auml;hlte Datei nicht
	 * existiert, bzw. keine Datei ist. Es kann nun gew&auml;hlt
	 * werden, ob es einen weiteren Versuch geben soll.
	 * 
	 * @param someWrapperFile
	 * @return true wenn die Auswahl der Datei nochmals erfolgen soll
	 */
	protected boolean shouldTryAgainDoesntExist(T someWrapperFile) {
		return DialogFactory
				.showModalJaNeinDialog(parent, 
						LPMain.getMessageTextRespectUISPr("lp.datei.open.nichtvorhanden",
						someWrapperFile.getFile().getAbsolutePath()),
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.NO_OPTION);
	}

	/**
	 * Der Anwender erh&auml;lt einen Dialog, der in dar&uuml;ber
	 * informiert, dass die von ihm gew&auml;hlte Datei nicht die
	 * geforderte Dateinamenserweiterung enth&auml;lt. Es kann nun
	 * gew&auml;hlt werden, ob es einen weiteren Versuch geben soll.
	 * 
	 * @param someWrapperFile
	 * @return true wenn die Auswahl der Datei nochmals erfolgen soll
	 */
	protected boolean shouldTryAgainDoesntAccept(
			FileExtensionAcceptor acceptor, T someWrapperFile) {
		return DialogFactory
				.showModalJaNeinDialog(parent, 
						LPMain.getMessageTextRespectUISPr("lp.datei.open.falscheendung", 
						acceptor.extensionsAsString(), someWrapperFile.getFile().getAbsolutePath()), 
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.NO_OPTION);	
	}
	
	protected FileExtensionAcceptor acceptor() {
		return this.acceptor;
	}
	
	public String getAllowedExtensions() {
		return acceptor().extensionsAsString();
	}
}
