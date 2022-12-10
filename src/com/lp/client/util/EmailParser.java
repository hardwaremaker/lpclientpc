package com.lp.client.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.datatypes.Chunk;
import org.apache.poi.hsmf.datatypes.Chunks;
import org.apache.poi.hsmf.datatypes.MAPIProperty;
import org.apache.poi.hsmf.datatypes.StringChunk;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.rtf.RTFParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableList;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

public abstract class EmailParser {
	protected Logger logger;
	private List<String> inlineImageURLs = new ArrayList<String>();
	public static final String IMAGE_URL_PREFIX = "http://localhost/";

	protected EmailParser() {
		logger = Logger.getLogger(this.getClass());
	}

	public abstract String[] getTo();

	public abstract String getFrom();

	public abstract String getSubject();

	public abstract String getContent();

	public abstract String getDate();

	public abstract TreeMap<String, ? extends Image> getInlineImages();

	public abstract TextContentType getContentType();

	public static Optional<EmailParser> create(InputStream mailData, String mimetype)
			throws IOException, MessagingException {
		return create(mailData, mimetype, false);
	}

	public static Optional<EmailParser> create(InputStream mailData, String mimetype, boolean preferPlainText)
			throws IOException, MessagingException {
		if (MediaFac.DATENFORMAT_MIMETYPE_APP_MSOUTLOOK.equals(mimetype)
				|| MediaFac.DATENFORMAT_MIMETYPE_TIKA_MSOFFICE.equals(mimetype)) {
			return Optional.of(new MsgData(mailData, preferPlainText));
		} else if (MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822.equals(mimetype)) {
			return Optional.of(new EmlData(mailData, preferPlainText));
		}
		return Optional.empty();
	}

	/**
	 * List of URLs that are used for images, that are not sent with the mail
	 * 
	 * @return
	 */
	public List<String> getInlineImageURLs() {
		return inlineImageURLs;
	}

	protected String modifyHTML(String html) {
		try {
			HTMLEditorKit kit = new HTMLEditorKit();
			HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
			doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
			kit.read(new StringReader(html), doc, 0);

			ElementAccumulator imgAccumulator = new ElementAccumulator(this::isImageTag);
			List<ElementAccumulator> accumulators = ImmutableList.of(imgAccumulator);

			checkElements(doc, accumulators);
			for (Element img : imgAccumulator.getElements()) {
				AttributeSet attrs = img.getAttributes();
				Enumeration<?> attrNames = attrs.getAttributeNames();
				String src = attrs.getAttribute(HTML.Attribute.SRC).toString();
				if (src.startsWith("http:") || src.startsWith("https:")) {
					inlineImageURLs.add(src);
				}
				String tagString = modifySrcTags(attrs, attrNames);
				doc.setOuterHTML(img, tagString);
			}

			StringWriter sw = new StringWriter();
			kit.write(sw, doc, 0, doc.getLength());
			return sw.toString();
		} catch (IOException e) {
			logger.warn("Error while getting Image, may not be shown correctly", e);
		} catch (BadLocationException e) {
			logger.warn("Error while getting Image, may not be shown correctly", e);
		}
		return html;
	}

	/**
	 * Parse html image tag und
	 */
	private boolean isImageTag(Element elem) {
		if (!elem.isLeaf())
			return false;
		AttributeSet attrs = elem.getAttributes();
		if (attrs.getAttribute(StyleConstants.NameAttribute).equals(Tag.IMG)) {
			Object srcAttr = attrs.getAttribute(HTML.Attribute.SRC);
			return srcAttr != null;
		}
		return false;
	}

	private Dimension getRealSize(int width, int height, Image img) {
		int imgWidth = img.getWidth(null);
		int imgHeight = img.getHeight(null);
		double aspectRatio = ((double) imgWidth) / ((double) imgHeight);
		if (width == -1) {
			if (height == -1) {
				// both invalid
				return new Dimension(imgWidth, imgHeight);
			}
			// have to respect aspect ratio
			int aspectWidth = (int) (height * aspectRatio);
			return new Dimension(aspectWidth, height);
		} else if (height == -1) {
			int aspectHeight = (int) (width / aspectRatio);
			return new Dimension(width, aspectHeight);
		}
		return new Dimension(width, height);
	}

	private String modifySrcTags(AttributeSet attrs, Enumeration<?> attrNames) {
		StringBuilder tagString = new StringBuilder();
		tagString.append("<img ");

		int width = -1;
		int height = -1;
		String src = attrs.getAttribute(HTML.Attribute.SRC).toString();

		while (attrNames.hasMoreElements()) {
			Object name = attrNames.nextElement();
			if (name instanceof HTML.Attribute) {
				tagString.append(name.toString());
				tagString.append("=\"");
				if (name.equals(HTML.Attribute.SRC)) {
					tagString.append(IMAGE_URL_PREFIX);
					tagString.append(src);
				} else {
					tagString.append(attrs.getAttribute(name));
				}

				if (name.equals(HTML.Attribute.WIDTH))
					width = Integer.valueOf(attrs.getAttribute(name).toString());
				if (name.equals(HTML.Attribute.HEIGHT))
					height = Integer.valueOf(attrs.getAttribute(name).toString());

				tagString.append("\" ");
			}
		}

		if (width == -1 || height == -1) {
			String srcNoCID = src.substring("cid:".length());
			Image img = getInlineImages().get(srcNoCID);
			if (img != null) {
				Dimension realSize = getRealSize(width, height, img);
				if (width == -1) {
					tagString.append(" width=\"");
					tagString.append(realSize.width);
					tagString.append("\"");
				}
				if (height == -1) {
					tagString.append(" height=\"");
					tagString.append(realSize.height);
					tagString.append("\"");
				}
			}
		}

		tagString.append('>');
		return tagString.toString();
	}

	private void checkElements(HTMLDocument doc, Collection<ElementAccumulator> accumulators) {
		ElementIterator elemIter = new ElementIterator(doc);
		Element elem = null;
		while ((elem = elemIter.next()) != null) {
			for (ElementAccumulator acc : accumulators) {
				acc.testAndAccept(elem);
			}
		}
	}

	protected class ElementAccumulator implements Predicate<Element>, Consumer<Element> {
		private final Predicate<Element> predicate;
		private final List<Element> elements;

		public ElementAccumulator(Predicate<Element> predicate) {
			Objects.requireNonNull(predicate);
			this.predicate = predicate;
			elements = new ArrayList<>();
		}

		@Override
		public void accept(Element t) {
			elements.add(t);
		}

		@Override
		public boolean test(Element t) {
			return predicate.test(t);
		}

		public void testAndAccept(Element t) {
			if (test(t)) {
				accept(t);
			}
		}

		public List<Element> getElements() {
			return elements;
		}

	}

	public static enum TextContentType {
		PLAIN, HTML, UNKNOWN;
	}

	protected static class MailContent {
		public final String content;
		public final TextContentType contentType;

		public MailContent(String content, TextContentType contentType) {
			super();
			this.content = content;
			this.contentType = contentType;
		}
	}

	private static class EmlData extends EmailParser {

		// NOTE: Anhang -> Content-Disposition oder Filename in Content-Type

		private final MimeMessage message;
		private EmailParser.MailContent content;
		private TreeMap<String, BufferedImage> inlineImages = new TreeMap<String, BufferedImage>();

		public EmlData(InputStream stream, boolean preferPlainText) throws MessagingException {
			Session sess = Session.getDefaultInstance(System.getProperties());
			this.message = new MimeMessage(sess, stream);
			content = parse(message, preferPlainText);
			if (content.contentType == TextContentType.HTML) {
				String html = modifyHTML(content.content);
				content = new EmailParser.MailContent(html, content.contentType);
			}
		}

		private EmailParser.MailContent parse(MimePart message, boolean preferPlainText) throws MessagingException {
			String sContentType = message.getContentType();
			try {
				if (sContentType.startsWith("text/plain")) {
					return new EmailParser.MailContent(message.getContent().toString(), TextContentType.PLAIN);
				} else if (sContentType.startsWith("text/html")) {
					return new EmailParser.MailContent(message.getContent().toString(), TextContentType.HTML);
				} else if (sContentType.startsWith("multipart/alternative")) {
					return parseMultipartAlternative((MimeMultipart) message.getContent(), preferPlainText);
				} else if (sContentType.startsWith("multipart")) {
					HvOptional<EmailParser.MailContent> optCont = tryAndParseMultipartMixedOrRelated(
							(MimeMultipart) (message).getContent(), preferPlainText);
					if (optCont.isPresent())
						return optCont.get();
					else
						return new EmailParser.MailContent("Unable to parse", TextContentType.UNKNOWN);
				}
			} catch (IOException e) {
				return new EmailParser.MailContent(e.getLocalizedMessage(), TextContentType.PLAIN);
			}
			return new EmailParser.MailContent("", TextContentType.UNKNOWN);
		}

		private EmailParser.MailContent parseMultipartAlternative(MimeMultipart multipart, boolean preferPlainText)
				throws MessagingException, IOException {
			int cnt = multipart.getCount();
			boolean complexContent = false;
			EmailParser.MailContent cont = new EmailParser.MailContent("", TextContentType.UNKNOWN);
			for (int i = 0; i < cnt; i++) {
				BodyPart part = multipart.getBodyPart(i);
				String sContentType = part.getContentType();
				if (sContentType.startsWith("text/plain")) {
					if (preferPlainText) {
						cont = new EmailParser.MailContent(part.getContent().toString(), TextContentType.PLAIN);
						break;
					}
					if (cont.contentType != TextContentType.UNKNOWN) {
						continue;
					}
					cont = new EmailParser.MailContent(part.getContent().toString(), TextContentType.PLAIN);
				} else if (sContentType.startsWith("text/html")) {
					if (complexContent)
						continue;
					cont = new EmailParser.MailContent(part.getContent().toString(), TextContentType.HTML);
				} else if (sContentType.startsWith("multipart/alternative")) {
					EmailParser.MailContent altCont = parseMultipartAlternative((MimeMultipart) part.getContent(),
							preferPlainText);
					if (cont.contentType.compareTo(altCont.contentType) <= 0 && !complexContent) {
						cont = altCont;
					}
				} else if (sContentType.startsWith("multipart")) {
					HvOptional<EmailParser.MailContent> parsed = tryAndParseMultipartMixedOrRelated(
							(MimeMultipart) part.getContent(), preferPlainText);
					if (parsed.isPresent()) {
						cont = parsed.get();
						complexContent = true;
					}
				}
			}
			return cont;
		}

		private HvOptional<EmailParser.MailContent> tryAndParseMultipartMixedOrRelated(MimeMultipart multipart,
				boolean preferPlainText) throws MessagingException, IOException {
			StringBuilder sbContent = new StringBuilder();
			String tail = "";
			TextContentType contentType = TextContentType.UNKNOWN;

			// Assume first part is main part
			MimeBodyPart mainPart = (MimeBodyPart) multipart.getBodyPart(0);
			EmailParser.MailContent mainContent = parse(mainPart, preferPlainText);
			String content = mainContent.content;
			contentType = mainContent.contentType;

			if (contentType == TextContentType.HTML) {
				// Only put everything up to the </body> tag in the stringBuilder
				int idxBodyEnd = content.indexOf("</body>");
				sbContent.append(content.substring(0, idxBodyEnd));
				tail = content.substring(idxBodyEnd);
			} else {
				sbContent.append(content);
			}

			int partCount = multipart.getCount();
			for (int i = 1; i < partCount; i++) {
				BodyPart partn = multipart.getBodyPart(i);
				String disposition = partn.getDisposition();
				if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
					continue;
				}
				String partType = partn.getContentType();
				if (partType.startsWith("multipart")) {
					HvOptional<EmailParser.MailContent> addCont = HvOptional.empty();
					if (partType.startsWith("multipart/alternative")) {
						addCont = HvOptional
								.of(parseMultipartAlternative((MimeMultipart) partn.getContent(), preferPlainText));
					} else {
						addCont = tryAndParseMultipartMixedOrRelated((MimeMultipart) partn.getContent(),
								preferPlainText);
					}
					if (addCont.isPresent()) {
						EmailParser.MailContent cont = addCont.get();
						if (cont.contentType == TextContentType.PLAIN) {
							sbContent.append(cont.content);
						} else if (cont.contentType == TextContentType.HTML) {
							int bodyStart = cont.content.indexOf("<body>");
							int bodyEnd = cont.content.indexOf("</body>");
							sbContent.append(cont.content.substring(bodyStart, bodyEnd - "</body>".length()));
						}
					}
				} else if (partType.startsWith("text/plain")) {
					sbContent.append(partn.getContent().toString());
				} else if (partType.startsWith("text/html")) {
					String html = partn.getContent().toString();
					int bodyStart = html.indexOf("<body>");
					int bodyEnd = html.indexOf("</body>");
					sbContent.append(html.substring(bodyStart, bodyEnd - "</body>".length()));
				}
				// Images could be inlined
				else if (partType.startsWith("image")) {
					// Image should have content ID
					String[] ids = partn.getHeader("content-id");
					if (ids == null)
						continue;
					String id = ids[0];
					// remove < and >
					id = id.substring(1, id.length() - 1);
					// This will be a BASE64DecoderStream if data is BASE64 encoded
					InputStream is = partn.getInputStream();
					BufferedImage bimg = ImageIO.read(is);
					inlineImages.put(id, bimg);
				}
			}

			sbContent.append(tail);

			String htmlContent = sbContent.toString();

			return HvOptional.of(new EmailParser.MailContent(htmlContent, contentType));
		}

		@Override
		public String[] getTo() {
			try {
				Address[] addrs = message.getAllRecipients();
				if (addrs == null)
					return new String[0];
				String[] toStrings = new String[addrs.length];
				for (int i = 0; i < addrs.length; i++) {
					toStrings[i] = addrs[i].toString();
				}
				return toStrings;
			} catch (MessagingException e) {
			}
			return new String[] {};
		}

		@Override
		public String getFrom() {
			try {
				Address[] addrs = message.getFrom();
				return getFirstInternetAddress(addrs);
			} catch (MessagingException e) {
			}
			return "";
		}

		private String getFirstInternetAddress(Address[] addrs) {
			if (addrs != null && addrs.length > 0) {
				for (Address addr : addrs) {
					if (addr instanceof InternetAddress) {
						InternetAddress iaddr = (InternetAddress) addr;
						return iaddr.toUnicodeString();
					}
				}
			}
			return "";
		}

		@Override
		public String getSubject() {
			try {
				return message.getSubject();
			} catch (MessagingException e) {
				return "";
			}
		}

		@Override
		public String getContent() {
			return content.content;
		}

		@Override
		public TextContentType getContentType() {
			return content.contentType;
		}

		@Override
		public String getDate() {
			try {
				Date d = message.getReceivedDate();
				if (d == null) {
					d = message.getSentDate();
				}
				if (d != null) {
					return Helper.formatTimestamp(new Timestamp(d.getTime()), LPMain.getTheClient().getLocUi());
				}
			} catch (MessagingException e) {
				logger.error("Error getting Date from Message", e);
			} catch (Throwable e) {
				logger.error("Error in getDate", e);
			}
			return "";
		}

		@Override
		public TreeMap<String, ? extends Image> getInlineImages() {
			return inlineImages;
		}
	}

	private static class MsgData extends EmailParser {

		private MAPIMessage msg;
		private EmailParser.MailContent content;
		private TreeMap<String, Image> inlineImages = new TreeMap<String, Image>();

		private static final Pattern imgSrcPattern = Pattern.compile("\\{.*?(<img.*?src=\"(.+?)\".*?>).*?\\}");

		public MsgData(InputStream data, boolean preferPlainText) throws IOException {
			msg = new MAPIMessage(data);
			parse(preferPlainText);
		}

		private void parse(boolean preferPlainText) {
			try {
				if (preferPlainText) {
					// Fuer plain text werden keine inline Bilder gebraucht
					content = new EmailParser.MailContent(msg.getTextBody(), TextContentType.PLAIN);
					return;
				}
				parseAttachmentImages();
				msg.setReturnNullOnMissingChunk(true);
				String htmlText = msg.getHtmlBody();
				if (htmlText != null) {
					parseHTML(htmlText);
					return;
				}
				String rtfText = msg.getRtfBody();
				if (rtfText != null) {
					parseHTML(rtfToHtml(rtfText));
					return;
				}
				content = new EmailParser.MailContent(msg.getTextBody(), TextContentType.PLAIN);

			} catch (ChunkNotFoundException e) {
				// Nicht moeglich
			} catch (IOException e) {
				// ByteArrayInputStream can not throw IOException
			} finally {
				msg.setReturnNullOnMissingChunk(false);
				if (content == null)
					content = new EmailParser.MailContent("", TextContentType.UNKNOWN);
			}
		}

		private void parseHTML(String htmlText) {
			// This should be valid HTML here
			htmlText = modifyHTML(htmlText);
			content = new EmailParser.MailContent(htmlText, TextContentType.HTML);
		}

		private String rtfReplaceImageTagWithMarker(String rtf, Map<String, String> idToTagMap) {
			StringBuilder sbContent = new StringBuilder(rtf);

			int offs = 0;
			Matcher imgSrcMatcher = imgSrcPattern.matcher(rtf);
			while (imgSrcMatcher.find()) {
				String replace = "inlineimg:" + imgSrcMatcher.group(2);
				idToTagMap.put(replace, imgSrcMatcher.group(1));
				sbContent.replace(imgSrcMatcher.start() - offs, imgSrcMatcher.end() - offs, replace);
				// Buffer will be length of replaced String - length of inserted String shorter
				offs += imgSrcMatcher.end() - imgSrcMatcher.start() - replace.length();
			}

			return sbContent.toString();
		}

		private String rtfToHtml(String rtf) throws IOException {
			Map<String, String> idTagMap = new HashMap<String, String>();
			rtf = rtfReplaceImageTagWithMarker(rtf, idTagMap);

			String html = "";
			try {
				ContentHandler handler = new ToXMLContentHandler();
				RTFParser parser = new RTFParser();
				Metadata metadata = new Metadata();
				ParseContext context = new ParseContext();
				parser.parse(new ByteArrayInputStream(rtf.getBytes()), handler, metadata, context);
				html = handler.toString();
			} catch (SAXException | TikaException e) {
				logger.warn("Error parsing RTF Text, mail might not be displayed correctly", e);
			}
			StringBuilder sb = new StringBuilder(html);
			for (Map.Entry<String, String> entr : idTagMap.entrySet()) {
				int idx = 0;
				while ((idx = sb.indexOf(entr.getKey(), idx)) != -1) {
					sb.replace(idx, idx + entr.getKey().length(), entr.getValue());
					idx += entr.getValue().length(); // look for next match after end of current
				}
			}

			return sb.toString();
		}

		private void parseAttachmentImages() {
			AttachmentChunks[] allAttachments = msg.getAttachmentFiles();

			for (AttachmentChunks attachment : allAttachments) {
				if (attachment.getAttachMimeTag() != null
						&& attachment.getAttachMimeTag().toString().startsWith("image")) {
					// Look for the content-id chunk. Should start with the file name and then have
					// @some-hex-numbers
					String cid = null;
					Chunk[] allChunks = attachment.getAll();
					for (Chunk chunk : allChunks) {
						if (chunk.getChunkId() == MAPIProperty.ATTACH_CONTENT_ID.id) {
							cid = chunk.toString();
							break;
						}
					}
					if (cid == null) {
						// This is not an inlined Image!
						continue;
					}
					BufferedImage img;
					try {
						img = ImageIO.read(new ByteArrayInputStream(attachment.getAttachData().getValue()));
						inlineImages.put(cid, img);
					} catch (IOException e) {
					}
				}
			}
		}

		@Override
		public String[] getTo() {
			String[] names = new String[0];
			try {
				names = msg.getRecipientNamesList();
			} catch (ChunkNotFoundException e1) {
				// Ignore if no names are found
			}
			try {
				String[] addrs = msg.getRecipientEmailAddressList();
				if (addrs == null)
					return new String[0];
				if (names != null && names.length == addrs.length) {
					for (int i = 0; i < addrs.length; i++) {
						// Sometimes name field is the same as address, do not have to show it then
						if (!names[i].equals(addrs[i]))
							addrs[i] = names[i] + "<" + addrs[i] + ">";
					}
				}
				return addrs;
			} catch (ChunkNotFoundException e) {
				return new String[0];
			}
		}

		@Override
		public String getFrom() {
			String fromName = "";
			try {
				fromName = msg.getDisplayFrom();
			} catch (ChunkNotFoundException e) {
				// Ignore
			}
			Chunks mainChunks = msg.getMainChunks();
			StringChunk emailFromChunk = mainChunks.getEmailFromChunk();
			/*
			 * Outlook messages, die vom gleichen ExchangeServer kommen, haben eine Outlook
			 * X500 Adresse, diese nicht anzeigen
			 */
			if (emailFromChunk != null && emailFromChunk.toString().matches(".+@.+\\..+")) {
				if (!fromName.equals("")) {
					return fromName + "<" + emailFromChunk.getValue() + ">";
				} else
					return emailFromChunk.getValue();
			} else
				return fromName;
		}

		@Override
		public String getSubject() {
			try {
				return msg.getSubject();
			} catch (ChunkNotFoundException e) {
				return "";
			}
		}

		@Override
		public String getContent() {
			return content.content;
		}

		@Override
		public String getDate() {
			try {
				Calendar sentDate = msg.getMessageDate();
				if (sentDate == null) {
					return "";
				}
				return Helper.formatTimestamp(new Timestamp(sentDate.getTimeInMillis()),
						LPMain.getTheClient().getLocUi());
			} catch (ChunkNotFoundException e) {
				return "";
			} catch (Throwable e) {
				logger.error("Error in getDate", e);
				return "";
			}
		}

		@Override
		public TextContentType getContentType() {
			return content.contentType;
		}

		@Override
		public TreeMap<String, Image> getInlineImages() {
			return inlineImages;
		}
	}

}