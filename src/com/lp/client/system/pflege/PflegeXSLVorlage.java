package com.lp.client.system.pflege;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperHtmlField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.util.HvOptional;

public class PflegeXSLVorlage implements IPflegefunktion, ActionListener {

	private JPanel panel;
	private WrapperHtmlField htmlEditor;

	private JList<String> xslFieldList;
	private DefaultListModel<String> xslFieldListModel;

	private WrapperButton btnOpen;
	private WrapperButton btnSave;

	private Path xslFile = null;
	private boolean fileOpen = false;

	private final InternalFrame iFrame;

	/**
	 * Pattern um einen XSL Select Tag zu finden Der Tag sieht aus wie:
	 * <xsl:value-of select="/mail/TAG"/>
	 * 
	 * zwischen value-of und select, sowie zwischen dem " am Ende des Pfades und dem
	 * /> k&ouml;nnten mehr Zeichen stehen. Diese werden durch non-greedy Matches von
	 * beliebigen Zeichen implementiert (.*?) Non-Greedy ist wichtig, da sonst bei
	 * mehreren XSL Tags in einer Zeile alle mit nur einem Regex gematcht werden!
	 */
	private static final Pattern XSLSelectPattern = Pattern.compile("<xsl:value-of.*?select=\"/mail/([\\w/-]+)\".*?/>");
	private static final Pattern HTMLEscapePattern = Pattern.compile("&[^#]\\w*;");

	/**
	 * XSL select Tags werden mit ### umgeben um im Text dargestellt zu werden
	 * Regex-Pattern um diese zu finden
	 */
	private static final Pattern XSLEscapedSelectPattern = Pattern.compile("###([\\w/-]+)###");
	// Ersetzungspattern, Match-Gruppe 1 wird zwischen ### gefuegt
	private static final String XSLEscapeReplacement = "###$1###";

	private static final String MARKER_HTML_START = "<HeliumV_HTML>";
	private static final String MARKER_HTML_END = "</HeliumV_HTML>";
	private static final String MARKER_TAGLIST_START = "<HeliumV_Taglist>";
	private static final String MARKER_TAGLIST_END = "</HeliumV_Taglist>";

	private Logger logger;

	public PflegeXSLVorlage(InternalFrame iFrame) {
		this.iFrame = iFrame;
		logger = Logger.getLogger(getClass());
	}

	@Override
	public String getKategorie() {
		return KATEGORIE_ALLGEMEIN;
	}

	@Override
	public String getName() {
		return "XSL Vorlage aendern";
	}

	@Override
	public String getBeschreibung() {
		return "Mit dieser Funktion k\u00F6nnen XSL Vorlagen f\u00FCr"
				+ " den Emailversand erstellt und ver\u00E4ndert werden." + "<br><br>"
				+ "Die m\u00f6glichen Tags sind auf der rechten Seite aufgelistet. Um einen Tag einzuf\u00FCgen muss dessen name mit ### umgeben im Textfeld eingegeben werden.<br>"
				+ "Mittels Doppelklick auf einen Tag wird dieser Text in die Zwischenablage kopiert";
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public boolean isStartable() {
		return false;
	}

	@Override
	public void run() {
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public void init() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		btnOpen = new WrapperButton("\u00d6ffnen");
		btnSave = new WrapperButton("Speichern");

		btnOpen.setPreferredSize(HelperClient.getSizeFactoredDimension(70));
		btnSave.setPreferredSize(HelperClient.getSizeFactoredDimension(70));

		btnOpen.addActionListener(this);
		btnSave.addActionListener(this);

		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
		panelButtons.add(btnOpen);
		panelButtons.add(btnSave);

		xslFieldListModel = new DefaultListModel<String>();
		xslFieldList = new JList<String>(xslFieldListModel);
		JScrollPane scrollPaneField = new JScrollPane(xslFieldList);

		xslFieldList.addMouseListener(new XSLFieldListMouseHandler());

		try {
			htmlEditor = new WrapperHtmlField(iFrame, "", true);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}

		JSplitPane centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, htmlEditor, scrollPaneField);
		centerPane.setResizeWeight(1);

		panel.add(centerPane, BorderLayout.CENTER);
		panel.add(panelButtons, BorderLayout.NORTH);
	}

	private void resetState() {
		htmlEditor.setText("");
		xslFieldListModel.clear();
		fileOpen = false;
	}

	@Override
	public void eventYouAreSelected() {
		resetState();
	}

	@Override
	public boolean supportsProgressBar() {
		return false;
	}

	@Override
	public void addPflegeEventListener(PflegeEventListener listener) {
	}

	@Override
	public void removeAllPflegeEventListeners() {
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Konvertiert Xsl Tags in HTML
	 * 
	 * @param original Die Eingangsdaten
	 * @return
	 */
	private String convertXslTagInHTML(CharSequence original) {
		Matcher applyMatcher = XSLSelectPattern.matcher(original);
		return applyMatcher.replaceAll(XSLEscapeReplacement);
	}

	private String replaceHTMLEscapes(String s) {
		Matcher match = HTMLEscapePattern.matcher(s);
		StringBuffer returnBuffer = new StringBuffer();
		while (match.find()) {
			String unescaped = StringEscapeUtils.unescapeHtml(match.group());
			String xmlEscaped = StringEscapeUtils.escapeXml(unescaped);
			if (xmlEscaped == null) {
				// nicht gefunden, gleich lassen
				xmlEscaped = match.group();
			}
			match.appendReplacement(returnBuffer, xmlEscaped);
		}
		match.appendTail(returnBuffer);
		return returnBuffer.toString();
	}

	/**
	 * Konvertiert den HTML Text zu Text, der in das XSL File geschrieben werden
	 * kann. Ersetzt &lt; und &gt; durch <code> &amp;lt; </code> und
	 * <code> &amp;gt; </code>
	 * 
	 * @param original
	 * @return
	 */
	private String convertHtmlToXslText(CharSequence original) {
		StringBuilder sbOrig = null;
		if (original instanceof StringBuilder) {
			sbOrig = (StringBuilder) original;
		} else {
			sbOrig = new StringBuilder(original);
		}
		Matcher mat = XSLEscapedSelectPattern.matcher(sbOrig);
		String text = mat.replaceAll("<xsl:value-of select=\"/mail/$1\"/>");
		return replaceHTMLEscapes(text);
	}

	/**
	 * Versucht das XSL vom Reader zu lesen. XSL sollte im folgenden Format sein:
	 * <br>
	 * <hrule> XSL Tags <br>
	 * {@link PflegeXSLVorlage#MARKER_HTML_START} <br>
	 * HTML mit xsl select tags <br>
	 * {@link PflegeXSLVorlage#MARKER_HTML_END} <br>
	 * XSL Tags und Kommentar start <br>
	 * {@link PflegeXSLVorlage#MARKER_TAGLIST_START} in Kommentar <br>
	 * Liste der XSL tags, die im original xml sind (immer noch Kommentar) <br>
	 * {@link PflegeXSLVorlage#MARKER_TAGLIST_END} <br>
	 * Restlicher Kommentar
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private HvOptional<String> parseFileContent(BufferedReader reader) throws IOException {
		StringBuilder sbHtml = new StringBuilder();
		String line;
		ReaderState state = ReaderState.START;
		while ((line = reader.readLine()) != null) {
			switch (state) {
			case START:
				if (line.contains(MARKER_HTML_START)) {
					state = ReaderState.HTML;
				}
				break;
			case HTML:
				if (line.contains(MARKER_HTML_END)) {
					state = ReaderState.MIDDLE;
				} else {
					sbHtml.append(line);
					sbHtml.append(System.lineSeparator());
				}
				break;
			case MIDDLE:
				if (line.contains(MARKER_TAGLIST_START)) {
					state = ReaderState.TAGLIST;
				}
				break;
			case TAGLIST:
				if (line.contains(MARKER_TAGLIST_END)) {
					return HvOptional.of(sbHtml.toString());
				}
				line = line.trim();
				if (!line.isEmpty()) {
					xslFieldListModel.addElement(line);
				}
				break;
			}
		}

		return HvOptional.empty();
	}

	private void loadFileContent() {
		try (BufferedReader reader = Files.newBufferedReader(xslFile, StandardCharsets.UTF_8)) {
			HvOptional<String> content = parseFileContent(reader);
			if (!content.isPresent()) {
				resetState();
				DialogFactory.showModalInfoDialog("Ung\u00FCltige Datei",
						LPMain.getMessageTextRespectUISPr("lp.pflege.xslvorlage.invalidedatei", xslFile.toString()));
				return;
			}
			String editorText = convertXslTagInHTML(content.get());
			htmlEditor.setText(editorText);
		} catch (IOException e) {
			resetState();
			DialogFactory.showModalInfoDialog("Fehler", LPMain.getTextRespectUISPr("error.fehlerbeimlesen"));
		}
	}

	private void saveFileContent() {
		try {
			// Hier getPlainText verwendet, weil das auch den HTML Text zurueck gibt, aber
			// ohne es in <html><body></body></html> zu verpacken
			String htmlPart = convertHtmlToXslText(htmlEditor.getPlainText());
			List<String> lines = getXSLLinesWithReplacedHTML(xslFile, htmlPart);
			// Bei Fehler ist lines leer -> nur schreiben wenn Zeilen da sind!
			if (lines.size() > 0) {
				Files.write(xslFile, lines, StandardCharsets.UTF_8);
			}
		} catch (TextBlockOverflowException e1) {
			// Ignore
		} catch (IOException e) {
			DialogFactory.showModalInfoDialog("Fehler", LPMain.getTextRespectUISPr("error.fehlerbeimlesen"));
		}
	}

	private List<String> getXSLLinesWithReplacedHTML(Path file, String htmlPart) {
		List<String> lines = new ArrayList<String>();
		boolean htmlBlock = false;
		try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!htmlBlock) {
					lines.add(line);
					if (line.contains(MARKER_HTML_START)) {
						htmlBlock = true;
					}
				} else {
					if (line.contains(MARKER_HTML_END)) {
						lines.addAll(Arrays.asList(htmlPart.split("\\R")));
						lines.add(line);
						htmlBlock = false;
					}
				}
			}
		} catch (IOException e) {
			DialogFactory.showModalInfoDialog("Fehler", LPMain.getTextRespectUISPr("error.fehlerbeimlesen"));
			resetState();
			return new ArrayList<String>(0);
		}
		if (htmlBlock) {
			return new ArrayList<String>(0);
		}
		return lines;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOpen) {
			HvOptional<WrapperFile> wf = HelperClient.showOpenFileDialog(null, panel, ".xsl");
			if (!wf.isPresent())
				return;
			File f = wf.get().getFile();
			xslFile = f.toPath();
			fileOpen = true;
			loadFileContent();
			htmlEditor.startEditing();
		} else if (e.getSource() == btnSave) {
			if (fileOpen) {
				saveFileContent();
			}
		}
	}

	private static enum ReaderState {
		START, HTML, MIDDLE, TAGLIST
	}

	private class XSLFieldListMouseHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				int index = xslFieldList.locationToIndex(e.getPoint());
				String element = xslFieldListModel.get(index);
				element = element.split("\\s")[0]; // nur 1. Wort, falls Leerzeichen drin sind
				StringSelection selection = new StringSelection("###" + element + "###");
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, null);
			}
		}
	}

}
