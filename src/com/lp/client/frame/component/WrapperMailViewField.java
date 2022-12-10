package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.resource.spi.IllegalStateException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.apache.log4j.Logger;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.client.util.EmailParser;
import com.lp.client.util.EmailParser.TextContentType;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

public class WrapperMailViewField extends PanelBasis implements IControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String ACTION_SPECIAL_DATEI = "action_special_datei";
	private static final String ACTION_SPECIAL_ANZEIGEN = "action_special_anzeigen";
	private static final String ACTION_SPECIAL_SPEICHERN = "action_special_speichern";

	private WrapperLabel wlaFrom;
	private WrapperTextField wtfFrom;
	private WrapperLabel wlaTo;
	private JComboBox<String> jcbTo;
	private WrapperLabel wlaSubject;
	private WrapperTextField wtfSubject;
	private WrapperLabel wlaDatum;
	private WrapperTextField wtfDatum;

	private WrapperButton wbuDatei;
	private WrapperTextField wtfDatei;

	private WrapperButton wbuAnzeigen;
	private WrapperButton wbuSpeichern;

	private JTextPane htmlField;
	private JScrollPane htmlScrollPane;

	private JPanel panelInternetImage;
	private WrapperButton wbuInternetImageOK;
	private WrapperButton wbuInternetImageNO;
	private WrapperLabel wlaInternetImageTxt;

	private EmailParser mailData;
	private byte[] originalData;

	private boolean isActivatable = true;
	private boolean isMandatoryField = false;
	private boolean bWithoutButtons;

	private Logger logger;

	public WrapperMailViewField(InternalFrame iFrame, String addTitle, boolean withoutButtons) throws Throwable {
		super(iFrame, addTitle);
		this.bWithoutButtons = withoutButtons;
		logger = Logger.getLogger(WrapperMailViewField.class);
		jbInit();

	}

	public void setMail(byte[] data, String mimeType) throws Throwable {
		// check if data is same, then don't reload;
		if (Arrays.equals(originalData, data))
			return;
		// Datei aus Zwischenablage
		originalData = data;
		if (originalData == null) {
			removeContent();
			return;
		}

		try {
			Optional<EmailParser> parser = EmailParser.create(new ByteArrayInputStream(originalData), mimeType);
			if (!parser.isPresent()) {
				removeContent();
			} else {
				mailData = parser.get();
				jcbTo.removeAllItems();
				for (String s : mailData.getTo())
					jcbTo.addItem(s);
				wtfFrom.setText(mailData.getFrom());
				wtfSubject.setText(mailData.getSubject());
				wtfDatum.setText(mailData.getDate());
				TextContentType type = mailData.getContentType();
				switch (type) {
				case PLAIN:
					htmlField.setContentType("text/plain");
					htmlField.setText(mailData.getContent());
					break;
				case HTML:
					htmlField.setContentType("text/html");
					htmlField.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
					htmlField.setText(mailData.getContent());
					addImages(mailData.getInlineImages());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							htmlScrollPane.getVerticalScrollBar().setValue(0);
						}
					});
					break;
				default:
					htmlField.setContentType("text/plain");
					htmlField.setText("");
				}
				panelInternetImage.setVisible(!mailData.getInlineImageURLs().isEmpty());
				if (getDateiname() == "")
					setDateiname("Zwischenablage" + getExtensionByType(mimeType));
			}
		} catch (IOException e) {
			logger.error("Error in setMail", e);
			throw e;
		} catch (MessagingException e) {
			logger.error("Error in setMail", e);
			throw e;
		}
	}

	private void addImages(TreeMap<String, ? extends Image> inlineImages) {
		Document doc = htmlField.getDocument();
		@SuppressWarnings("unchecked")
		Dictionary<URL, Image> cache = (Dictionary<URL, Image>) doc.getProperty("imageCache");
		if (cache == null) {
			cache = new Hashtable<URL, Image>();
			doc.putProperty("imageCache", cache);
		}
		for (Map.Entry<String, ? extends Image> entry : inlineImages.entrySet()) {
			try {
				cache.put(new URL("http://localhost/cid:" + entry.getKey()), entry.getValue());
			} catch (MalformedURLException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}

	private String elementToHTML(Element elem) {
		AttributeSet attrs = elem.getAttributes();
		Enumeration<?> names = attrs.getAttributeNames();
		StringBuilder sb = new StringBuilder();
		sb.append('<');
		sb.append(elem.getName());
		while (names.hasMoreElements()) {
			Object name = names.nextElement();
			if (name instanceof HTML.Tag || name instanceof StyleConstants || name == HTML.Attribute.ENDTAG) {
				continue;
			}
			sb.append(" ");
			sb.append(name);
			sb.append("=\"");
			sb.append(attrs.getAttribute(name));
			sb.append("\"");
		}
		sb.append('>');
		return sb.toString();
	}

	private void loadInternetImages() {
		List<String> urls = mailData.getInlineImageURLs();
		HTMLDocument doc = (HTMLDocument) htmlField.getDocument();
		@SuppressWarnings("unchecked")
		Dictionary<URL, Image> cache = (Dictionary<URL, Image>) doc.getProperty("imageCache");
		if (cache == null) {
			cache = new Hashtable<URL, Image>();
			doc.putProperty("imageCache", cache);
		}
		for (String sURL : urls) {
			URL url;
			try {
				url = new URL(sURL);
				String sFudgedURL = EmailParser.IMAGE_URL_PREFIX + sURL;
				cache.put(new URL(sFudgedURL), Toolkit.getDefaultToolkit().createImage(url));
				Element elem = doc.getElement(doc.getDefaultRootElement(), HTML.Attribute.SRC, sFudgedURL);
				if (elem != null) {
					try {
						doc.setOuterHTML(elem, elementToHTML(elem));
					} catch (BadLocationException e) {
						logger.error("BadLocation when Loading Image: " + elem + ", may not display correctly", e);
					} catch (IOException e) {
						logger.error("IOException when Loading Image: " + elem + ", may not display correctly", e);
					}
				}
			} catch (MalformedURLException e1) {
				logger.warn("Warning, malformed URL in Mail Image", e1);
			}
		}
	}

	private String getExtensionByType(String type) throws IllegalStateException {
		if (MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822.equals(type))
			return ".eml";
		else if (MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK.equals(type))
			return ".msg";
		throw new IllegalStateException("WrapperMailField does not support type " + type);
	}

	public byte[] getMailData() {
		return originalData;
	}

	private void jbInit() throws Throwable {
		wlaFrom = new WrapperLabel(LPMain.getTextRespectUISPr("label.absender"));
		wtfFrom = new WrapperTextField(256);
		wtfFrom.setEditable(false);

		wlaTo = new WrapperLabel(LPMain.getTextRespectUISPr("label.empfaenger"));
		jcbTo = new JComboBox<String>();
		jcbTo.setMinimumSize(new Dimension(10, jcbTo.getMinimumSize().height));
		jcbTo.setPreferredSize(jcbTo.getMinimumSize());
		jcbTo.setEditable(true);
		Component editor = jcbTo.getEditor().getEditorComponent();
		if (editor instanceof JTextComponent) {
			((JTextComponent) editor).setEditable(false);
		} else {
			jcbTo.setEditable(false);
		}

		wlaSubject = new WrapperLabel(LPMain.getTextRespectUISPr("label.betreff"));
		wtfSubject = new WrapperTextField(128);
		wtfSubject.setEditable(false);

		wlaDatum = new WrapperLabel(LPMain.getTextRespectUISPr("media.inbox.preview.datum"));
		wtfDatum = new WrapperTextField(32);
		wtfDatum.setEditable(false);

		wbuDatei = new WrapperButton(LPMain.getTextRespectUISPr("lp.datei"));
		wbuAnzeigen = new WrapperButton(LPMain.getTextRespectUISPr("lp.anzeigen"));
		wbuSpeichern = new WrapperButton(LPMain.getTextRespectUISPr("lp.speichern"));
		wtfDatei = new WrapperTextField();
		wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
		wtfDatei.setEditable(false);

		htmlField = new JTextPane();
		htmlField.setEditable(false);
		htmlScrollPane = new JScrollPane(htmlField);

		panelInternetImage = new JPanel();
		wbuInternetImageNO = new WrapperButton(LPMain.getTextRespectUISPr("lp.nein"));
		wbuInternetImageOK = new WrapperButton(LPMain.getTextRespectUISPr("lp.ja"));
		wlaInternetImageTxt = new WrapperLabel();
		wlaInternetImageTxt.setText(LPMain.getTextRespectUISPr("lp.warnung.inhalteausinternet"));
		wlaInternetImageTxt.setHorizontalAlignment(JLabel.CENTER);

		panelInternetImage.setLayout(new GridBagLayout());
		panelInternetImage.setBackground(Color.orange);
		panelInternetImage.add(wlaInternetImageTxt, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInternetImage.add(wbuInternetImageOK, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInternetImage.add(wbuInternetImageNO, new GridBagConstraints(2, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		wbuInternetImageNO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panelInternetImage.setVisible(false);
			}
		});
		wbuInternetImageOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panelInternetImage.setVisible(false);
				loadInternetImages();
			}
		});

		this.setLayout(new GridBagLayout());

		add(wlaFrom, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		add(wtfFrom, new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		add(wlaTo, new GridBagConstraints(4, 0, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		add(jcbTo, new GridBagConstraints(5, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		add(wlaSubject, new GridBagConstraints(1, 1, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		add(wtfSubject, new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		add(wlaDatum, new GridBagConstraints(4, 1, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		add(wtfDatum, new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		add(panelInternetImage, new GridBagConstraints(1, 3, GridBagConstraints.REMAINDER, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		add(htmlScrollPane, new GridBagConstraints(1, 4, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (!bWithoutButtons) {
			// Add Buttons

			wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
			wbuDatei.addActionListener(this);
			wbuAnzeigen.setActionCommand(ACTION_SPECIAL_ANZEIGEN);
			wbuAnzeigen.addActionListener(this);
			wbuSpeichern.setActionCommand(ACTION_SPECIAL_SPEICHERN);
			wbuSpeichern.addActionListener(this);

			HelperClient.setMinimumAndPreferredSize(wbuDatei, HelperClient.getSizeFactoredDimension(80));

			add(wbuDatei, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			add(wtfDatei, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			add(wbuAnzeigen, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			add(wbuSpeichern, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		}
	}

	private File createTempFile() throws IOException {
		String name = getDateiname();
		if (name == null)
			return null;
		File tmpFile = File.createTempFile(Helper.getName(name), Helper.getMime(name));

		FileOutputStream fos = new FileOutputStream(tmpFile);
		if (originalData != null)
			fos.write(originalData);
		fos.close();

		return tmpFile;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		wbuAnzeigen.setEnabled(enabled);
		wbuDatei.setEnabled(enabled);
		wbuSpeichern.setEnabled(enabled);
	}

	@Override
	public void removeContent() throws Throwable {
		for (Component comp : this.getComponents()) {
			if (comp instanceof IControl)
				((IControl) comp).removeContent();
		}
		panelInternetImage.setVisible(false);
		wtfDatei.setText("");
		htmlField.setContentType("text/plain");
		htmlField.setText("");
		originalData = null;
		mailData = null;
	}

	@Override
	public boolean hasContent() throws Throwable {
		return mailData != null;
	}

	@Override
	public boolean isMandatoryField() {
		return isMandatoryField;
	}

	@Override
	public void setMandatoryField(boolean isMandatoryField) {
		this.isMandatoryField = isMandatoryField;
	}

	@Override
	public boolean isActivatable() {
		return isActivatable;
	}

	@Override
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		if (!isActivatable) {
			setEnabled(false);
		}
	}

	public String getDateiname() {
		String n = wtfDatei.getText();
		return n == null ? "" : n;
	}

	public void setDateiname(String datei) {
		wtfDatei.setText(datei);
	}

	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand() == null)
			return;
		else if (e.getActionCommand().equals(ACTION_SPECIAL_ANZEIGEN)) {
			File tmpFile = createTempFile();
			if (tmpFile != null)
				HelperClient.desktopTryToOpenElseSave(tmpFile, getInternalFrame());
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SPEICHERN)) {
			File tmpFile = createTempFile();
			if (tmpFile != null) {
				String name = getDateiname();
				HelperClient.showSaveFileDialog(tmpFile, name != null ? new File(name) : null, getInternalFrame(),
						name != null ? Helper.getMime(name) : null);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DATEI)) {
			String currFileName = getDateiname();
			HvOptional<WrapperFile> wf = HelperClient.showOpenFileDialog(
					currFileName != null ? new File(currFileName) : null,
							getInternalFrame(), ".eml", ".msg");
			if (!wf.isPresent()) return;
			
			WrapperFile wFile = wf.get();
			String mime = "";
			String ext = Helper.getMime(wFile.getFile().getName());
			if (ext.toLowerCase().equals(".eml"))
				mime = MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822;
			else if (ext.toLowerCase().equals(".msg"))
				mime = MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK;
			setDateiname(wFile.getFile().getName());
			try {
				setMail(wFile.getBytes(), mime);
			} catch(Throwable t) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getMessageTextRespectUISPr(
								"lp.error.emailnichtverarbeitbar", wFile.getFile().getAbsolutePath()));			
			}
/*			
			List<File> selected = HelperClient.showOpenFileDialog(currFileName != null ? new File(currFileName) : null,
					getInternalFrame(), false, ".eml", ".msg");
			if (selected == null)
				return;
			Iterator<File> iter = selected.iterator();
			if (iter.hasNext()) {
				File f = iter.next();
				String mime = "";
				String ext = Helper.getMime(f.getName());
				if (ext.toLowerCase().equals(".eml"))
					mime = MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822;
				else if (ext.toLowerCase().equals(".msg"))
					mime = MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK;
				setDateiname(f.getName());
				setMail(Helper.getBytesFromFile(f), mime);
			}
*/
		}
	}

	public void cleanup() {
		try {
			removeContent();
		} catch (Throwable e) {
			logger.error("Error in cleanup", e);
		}
	}


}
