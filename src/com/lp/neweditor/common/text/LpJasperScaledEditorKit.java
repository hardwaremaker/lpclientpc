/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.neweditor.common.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabSet;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import com.lp.editor.text.LpJasperGenerator;
import com.lp.editor.text.LpJasperParser;
import com.lp.editor.text.LpJasperReportEditorKit;
import com.lp.editor.text.LpStyledEditorKit;

/**
 * Abgewandeltes {@link LpJasperReportEditorKit} f&uuml;r neuen Editor. Zoom
 * funkion wurde &uuml;berarbeitet, damit es auch mit den Textbl&ouml;cken
 * richtig funktioniert.
 * 
 * @author Sascha Zelzer
 * @author Kajetan Fuchsberger
 * @author Alexander Daum
 * @version 1.0
 */

public class LpJasperScaledEditorKit extends LpStyledEditorKit {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private int tabsize;
	private StyledViewFactory styledViewFactory = new StyledViewFactory();

	public LpJasperScaledEditorKit() {
		super();
		tabsize = 72; // Default Size Java
	}

	public String getContentType() {
		return "text/jasper";
	}

	public void read(InputStream in, Document doc, int pos) throws IOException, BadLocationException {
		if (doc instanceof StyledDocument) {
			LpJasperParser parser = new LpJasperParser();
			parser.parseFromStream(in, (StyledDocument) doc, pos);
		} else {
			// treat as text/plain
			super.read(in, doc, pos);
		}
	}

	public void read(Reader reader, Document doc, int pos) throws IOException, BadLocationException {
		if (doc instanceof StyledDocument) {
			LpJasperParser parser = new LpJasperParser();
			parser.parseFromReader(reader, (StyledDocument) doc, pos);
		} else {
			// treat as text/plain
			super.read(reader, doc, pos);
		}

	}

	public void write(OutputStream out, Document doc, int pos, int len) throws IOException, BadLocationException {
		if (doc instanceof StyledDocument) {
			LpJasperGenerator generator = new LpJasperGenerator();

			if ((pos < 0) || ((pos + len) > doc.getLength())) {
				throw new BadLocationException("LPJasperReportEditorKit.write", pos);
			}

			generator.writeFormat((StyledDocument) doc, out, pos, len);
		} else {
			super.write(out, doc, pos, len);
		}
	}

	public void write(Writer writer, Document doc, int pos, int len) throws IOException, BadLocationException {
		if (doc instanceof StyledDocument) {
			LpJasperGenerator generator = new LpJasperGenerator();

			if ((pos < 0) || ((pos + len) > doc.getLength())) {
				throw new BadLocationException("LPJasperReportEditorKit.write", pos);
			}

			generator.writeFormat((StyledDocument) doc, writer, pos, len);
		} else {
			super.write(writer, doc, pos, len);
		}

	}

	public int getTabsize() {
		return tabsize;
	}

	public void setTabsize(int newTabsize) {
		tabsize = newTabsize;
	}

	/**
	 * Ueberschreibt createDefaultDocument um Default-Properties fuer das neue
	 * Dokument zu setzen.
	 * 
	 * @return Document
	 */
	public Document createDefaultDocument() {
		Document doc = super.createDefaultDocument();
		return doc;
	}

//	public ViewFactory getViewFactory() {
//		return styledViewFactory;
//	}

	protected class StyledViewFactory implements ViewFactory {

		public View create(Element elem) {
			String kind = elem.getName();
			if (kind != null) {
				if (kind.equals(AbstractDocument.ContentElementName)) {
					return new ScaledLableView(elem);
				} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
					return new LpParagraphView(elem);
				} else if (kind.equals(AbstractDocument.SectionElementName)) {
					return new BoxView(elem, View.Y_AXIS);
				} else if (kind.equals(StyleConstants.ComponentElementName)) {
					return new ComponentView(elem);
				} else if (kind.equals(StyleConstants.IconElementName)) {
					return new IconView(elem);
				}
			}

			// default to text display
			return new LabelView(elem);
		}

	}

	protected class LpParagraphView extends ParagraphView {
		public LpParagraphView(Element element) {
			super(element);
		}

		@Override
		public float nextTabStop(float x, int tabOffset) {
			TabSet tabs = getTabSet();
			if (tabs == null) {
				return (float) (getTabBase() + (((int) x / getTabsize() + 1) * getTabsize()));
			}

			return super.nextTabStop(x, tabOffset);
		}
	}

	protected static class ScaledLableView extends LabelView {
		static GlyphPainter defaultPainter;

		public ScaledLableView(Element elem) {
			super(elem);
		}

		protected void checkPainter() {
			if (getGlyphPainter() == null) {
				if (defaultPainter == null) {
					defaultPainter = new ScaledGlyphPainter();
				}
				setGlyphPainter(defaultPainter.getPainter(this, getStartOffset(), getEndOffset()));
			}
		}
	}
}
