package com.lp.client.frame.report;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvCreatingCachingProvider;
import com.lp.editor.util.FontNotFoundException;
import com.lp.util.EJBExceptionLP;
import com.lp.util.FontNichtGefundenJRDetailsException;
import com.lp.util.report.ReportPatch;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.fonts.FontUtil;

public class ReportClientPatch extends ReportPatch {
	
	private FontCache fontCache;

	public ReportClientPatch(JasperPrint jasperPrint) {
		super(jasperPrint);
	}

	@Override
	protected void applyTo(JRTemplatePrintText textField) {
		super.applyTo(textField);
	}
	
	public void checkContent(JasperPrint jasperPrint, JasperReportsContext jrContext) throws ExceptionLP {
		if(jasperPrint == null) throw new IllegalArgumentException("jasperPrint") ;
		createFontCache(jrContext);
		
		for (JRPrintPage page : jasperPrint.getPages()) {
			for(JRPrintElement element : page.getElements()) {
				if(element instanceof JRTemplatePrintText) {
					JRTemplatePrintText textElement = (JRTemplatePrintText) element;
					checkFont(page, textElement) ;
					checkStyledFont(page, textElement);
				}
			}
		}
	}

	protected void checkStyledFont(JRPrintPage page, JRTemplatePrintText textElement) throws ExceptionLP {
		String text = textElement.getFullText();
		String fontNameStart = "fontName=\"";
		int startIndex = 0;
		int indexStyle;
		
		while ((indexStyle = text.indexOf("<style", startIndex)) > -1) {
			int indexStyleEnd = text.indexOf("\"", indexStyle);
			if (indexStyleEnd < 0)
				break;
			
			int indexFontName = text.indexOf(fontNameStart, indexStyle);
			if (indexFontName > -1 && indexFontName < indexStyleEnd) {
				indexFontName = indexFontName + fontNameStart.length();
				int indexFontNameEnd = text.indexOf("\"", indexFontName);
				if (indexFontNameEnd < 0)
					break;
				
				String styledFontName = text.substring(indexFontName, indexFontNameEnd);
				if (getFontCache().getValueOfKey(styledFontName) == null) {
					throw createFontNotFoundExc(textElement, styledFontName);
				}
				startIndex = indexFontName;
			} else {
				startIndex = indexStyleEnd;
			}
		}
	}

	protected void checkFont(JRPrintPage page, JRTemplatePrintText textField) throws ExceptionLP {
		String fontname = textField.getFontName();
		if (getFontCache().getValueOfKey(fontname) == null) {
			throw createFontNotFoundExc(textField, fontname);
		}
	}

	private ExceptionLP createFontNotFoundExc(JRTemplatePrintText textField, String fontname) {
		ExceptionLP fontNotFoundExc = new ExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FONT_NICHT_GEFUNDEN_CLIENT, 
				new FontNotFoundException("Font '" + fontname + "' am Client nicht gefunden", new String[] {fontname}));
		fontNotFoundExc.setExceptionData(new FontNichtGefundenJRDetailsException(textField, fontname));
		return fontNotFoundExc;
	}
	
	private FontCache getFontCache() {
		if (fontCache == null) {
			createFontCache(DefaultJasperReportsContext.getInstance());
		}
		return fontCache;
	}
	
	private void createFontCache(JasperReportsContext jrContext) {
		List<String> knownFonts = new ArrayList<String>();
		// alle systembekannten Fonts
		knownFonts.addAll(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		// alle der Jasper-Library bekannten Fonts (inkl. Font-Extensions)
		knownFonts.addAll(FontUtil.getInstance(jrContext).getFontFamilyNames());
		fontCache = new FontCache(knownFonts);
	}

	private class FontCache extends HvCreatingCachingProvider<String, String> {
		private List<String> availableFonts;
		public FontCache(List<String> availableFonts) {
			this.availableFonts = availableFonts;
		}

		@Override
		protected String provideValue(String fontName, String transformedKey) throws ExceptionLP {
			String systemFont = findFont(fontName);
			return systemFont;
		}

		private String findFont(String fontName) {
			for (String f : availableFonts) {
				if (f.equals(fontName)) {
					return f;
				}
			}
			return null;
		}
		
	}
}
