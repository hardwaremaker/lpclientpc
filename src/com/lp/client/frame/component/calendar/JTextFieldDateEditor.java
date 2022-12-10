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
/*
 *  Copyright (C) 2006 Kai Toedter
 *  kai@toedter.com
 *  www.toedter.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.lp.client.frame.component.calendar;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.CornerInfoButton;
import com.lp.client.frame.component.FocusHighlighter;
import com.lp.client.frame.component.IDirektHilfe;
import com.lp.client.frame.component.InfoButtonRelocator;
import com.lp.client.util.logger.LpLogger;
import com.lp.util.Helper;

/**
 * JTextFieldDateEditor is the default editor used by JDateChooser. It is a
 * formatted text field, that colores valid dates green/black and invalid dates
 * red. The date format patten and mask can be set manually. If not set, the
 * MEDIUM pattern of a SimpleDateFormat with regards to the actual locale is
 * used.
 *
 * @author Kai Toedter
 * @version $LastChangedRevision: 97 $
 * @version $LastChangedDate: 2006-05-24 17:30:41 +0200 (Mi, 24 Mai 2006) $
 */
public class JTextFieldDateEditor extends JFormattedTextField implements
		IDateEditor, CaretListener, FocusListener, ActionListener, IDirektHilfe {

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	private static final long serialVersionUID = 1L;

	private static final String PATTERN_DE = "dd.MM.yyyy";

	private static final String PATTERN_EN = "MM/dd/yyyy";

	protected Date date;

	protected Date oldDate;

	protected Date initDate;

	protected boolean dateInitialized;

	protected SimpleDateFormat dateFormatter;

	protected MaskFormatter maskFormatter;

	protected String datePattern;

	protected String maskPattern;

	protected char placeholder;

	protected DateUtil dateUtil;

	private boolean isMaskVisible;

	private boolean ignoreDatePatternChange;

	private int hours;

	private int minutes;

	private int seconds;

	private int millis;

	private Calendar calendar;

	private CornerInfoButton cib = null;
	private Dimension datePatternDimension;

	public JTextFieldDateEditor() {
		this(false, null, null, ' ');
	}

	public JTextFieldDateEditor(String datePattern, String maskPattern,
			char placeholder) {
		this(true, datePattern, maskPattern, placeholder);
	}

	public JTextFieldDateEditor(boolean showMask, String datePattern,
			String maskPattern, char placeholder) {
		setDocument(new PlainDocument() {
			private static final long serialVersionUID = 3152057063797995081L;

			@Override
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {
				if (str == null || getLength() >= 10) {
					return;
				}

				if (isNumber(str)) {
					if (offs == getLength() && offs > 0) {
						// cursor am ende, mindestens eine zahl schon
						// eingegeben, noch nicht 10 zeichen lang
						if (isNumber(getText(offs - 1, 1))) {
							// ist letztes zeichen eine zahl?
							if (getText(0, getLength()).split(
									getSeperatorAsRegex()).length < 3) {
								// bei der jahreszahl keinen punkt machen
								if (offs > 1
										&& !getText(offs - 2, 1).equals(
												getSeperator()))
									return;
								// wenn zwei stellen vorher kein trennzeichen
								// ist nichts tun.
								str = str + getSeperator();
							}
						}
					}
				} else if (str.equals(".") || str.equals(",")
						|| str.equals("/")) {
					if (offs > 0) {
						if (getText(offs - 1, 1).equals(getSeperator()))
							return;
						if (getText(0, getLength())
								.split(getSeperatorAsRegex()).length < 3)
							// bei der jahreszahl keinen punkt machen
							str = getSeperator();
						else
							return;
					} else
						return;
				}

				super.insertString(offs, str, a);
			}
		});
		dateUtil = new DateUtil();
		dateFormatter = new SimpleDateFormat(getPattern(getLocale()));
		dateFormatter.setLenient(false);

		setDateFormatString(datePattern);
		if (datePattern != null) {
			ignoreDatePatternChange = true;
		}

		this.placeholder = placeholder;

		if (maskPattern == null) {
			this.maskPattern = createMaskFromDatePattern(this.datePattern);
		} else {
			this.maskPattern = maskPattern;
		}

		setToolTipText(this.datePattern);
		setMaskVisible(showMask);

		addCaretListener(this);
		addFocusListener(this);
		addActionListener(this);

		calendar = Calendar.getInstance();

		new FocusHighlighter(this);
		cib = new CornerInfoButton(this);
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	private boolean isNumber(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private String getSeperator() {
		if(Locale.ENGLISH.getLanguage().equals(getLocale().getLanguage()))
			return "/";
		return ".";
	}

	private String getSeperatorAsRegex() {
		if(Locale.ENGLISH.getLanguage().equals(getLocale().getLanguage()))
			return "/";
		return "\\.";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.lp.client.frame.component.calendar.IDateEditor#getDate()
	 */
	public Date getDate() {
		checkText(false) ;
		try {
			calendar.setTime(dateFormatter.parse(getText()));
			calendar.set(Calendar.HOUR_OF_DAY, hours);
			calendar.set(Calendar.MINUTE, minutes);
			calendar.set(Calendar.SECOND, seconds);
			calendar.set(Calendar.MILLISECOND, millis);
			date = calendar.getTime();
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.lp.client.frame.component.calendar.IDateEditor#setDate(java.util.Date)
	 */
	public void setDate(Date date) {
		setDate(date, true);
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the date
	 * @param firePropertyChange
	 *            true, if the date property should be fired.
	 */
	protected void setDate(Date date, boolean firePropertyChange) {
		if(!dateInitialized) {
			initDate = date;
			dateInitialized = true;
		}
		Date oldDate = this.date;
		this.date = date;

		if (date == null) {
			setText("");
		} else {
			calendar.setTime(date);
			hours = calendar.get(Calendar.HOUR_OF_DAY);
			minutes = calendar.get(Calendar.MINUTE);
			seconds = calendar.get(Calendar.SECOND);
			millis = calendar.get(Calendar.MILLISECOND);

			String formattedDate = dateFormatter.format(date);
			try {
				setText(formattedDate);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		if (date != null && dateUtil.checkDate(date)) {
			setForeground(Defaults.getInstance().getDefaultTextColor());
		}

		if (firePropertyChange) {
			if(oldDate == null && date == null) {				
			} else {
				firePropertyChange("date", oldDate, date);
//				boolean changed = oldDate == null ? date.equals(oldDate) : oldDate.equals(date) ;
//				if(changed) {
//					firePropertyChange("date", oldDate, date);
//				}
			}
		}
		updateColor(false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.lp.client.frame.component.calendar.IDateEditor#setDateFormatString(java.lang.String)
	 */
	public void setDateFormatString(String dateFormatString) {
		if (ignoreDatePatternChange) {
			return;
		}

		try {
			dateFormatter.applyPattern(dateFormatString);
		} catch (RuntimeException e) {
			dateFormatter = new SimpleDateFormat(getPattern(getLocale()));
			dateFormatter.setLenient(false);
			ignoreDatePatternChange = false;
		}
		this.datePattern = dateFormatter.toPattern();
		setDatePatternDimension(datePattern);
		setToolTipText(this.datePattern);
		setDate(date, false);
	}

	private String getPattern(Locale locale) {
		if(locale == null) return PATTERN_DE;

		if(Locale.ENGLISH.getLanguage().equals(locale.getLanguage())) return PATTERN_EN;
		return PATTERN_DE;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see com.lp.client.frame.component.calendar.IDateEditor#getDateFormatString()
	 */
	public String getDateFormatString() {
		return datePattern;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.lp.client.frame.component.calendar.IDateEditor#getUiComponent()
	 */
	public JComponent getUiComponent() {
		return this;
	}

	/**
	 * After any user input, the value of the textfield is proofed. Depending on
	 * being a valid date, the value is colored green or red.
	 *
	 * @param event
	 *            the caret event
	 */
	public void caretUpdate(CaretEvent event) {
		if(!hasFocus()) return;
		updateColor(true);
	}

	/**
	 *
	 * @return true wenn das Datum zwischen Minimum und Maximum Wert ist.
	 */
	public boolean checkDate() {
		return dateUtil.checkDate(date);
	}


	private void updateColor(boolean useOkColor) {
		validateDateUI(useOkColor) ;
	}

	class DateCheck {
		private boolean valid ;
		private Date initialDate ;
		private Date usedDate ;

		public DateCheck() {
		}

		public boolean validateDate() {
			try {
				String s = getText() ;
				if(!Helper.isStringEmpty(s)) {
					initialDate = dateFormatter.parse(s);
					usedDate = normalize(initialDate) ;
					valid = dateUtil.checkDate(usedDate) ;
					return valid ;					
				} 
			} catch(Exception e) {
				myLogger.debug("Invalid date Exc");
			}

			return false ;
		}

		/**
		 * Gueltiges Datum, wurde als T.M.14 erfasst, gemeint ist T.M.2014
		 *		                       oder T.M.99 erfasst, gemeint ist T.M.1999
		 *
		 * @param d das zu normalisierende Datum
		 * @return das normalisierte Datum
		 */
		public Date normalize(Date d) {
			Calendar c = Calendar.getInstance() ;
			c.setTime(d);
			int year = c.get(Calendar.YEAR) ;
			if(year < 100) {
				if(year >= 40) {
					year += 1900 ;
				} else {
					year += 2000 ;
				}
				c.set(Calendar.YEAR, year);
				d = c.getTime() ;
			}

			return d ;
		}

		public void setMinMaxDate() {
			Date minDate = getMinSelectableDate() ;
			if(minDate != null && usedDate != null && usedDate.before(minDate)) {
				setDate(minDate, false);
			}
			Date maxDate = getMaxSelectableDate() ;
			if(maxDate != null && usedDate != null && usedDate.after(maxDate)) {
				setDate(maxDate, false) ;
			}
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public Date getInitialDate() {
			return initialDate;
		}

		public void setInitialDate(Date initialDate) {
			this.initialDate = initialDate;
		}

		public Date getUsedDate() {
			return usedDate;
		}

		public void setUsedDate(Date usedDate) {
			this.usedDate = usedDate;
		}
	}

	private DateCheck isValidDate() {
		DateCheck rc = new DateCheck() ;
		rc.validateDate() ;
		return rc ;
	}

	private void validateDateUI(boolean useOkColor) {
		validateDateUI(isValidDate(), useOkColor) ;
	}

	private void validateDateUI(DateCheck dateCheck, boolean useOkColor) {
		if(dateCheck.isValid()) {
			setForeground(useOkColor ? Defaults.getInstance().getValidTextColor() : Defaults.getInstance().getDefaultTextColor());
		} else {
			setForeground(Defaults.getInstance().getInvalidTextColor());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent focusEvent) {
		DateCheck dateCheck = checkText(true);
		validateDateUI(dateCheck, true) ;
		dateCheck.setMinMaxDate();
	}

	private DateCheck checkText(boolean firePropertyChanged) {
		DateCheck rc = isValidDate() ;
		setDate(rc.getUsedDate(), firePropertyChanged);
		return rc ;
	}

//	private DateCheck checkText(boolean firePropertyChanged) {
//		try {
//			Date date = dateFormatter.parse(getText());

//			GregorianCalendar gcGrenzeUnten = new GregorianCalendar(1900, 0, 1);
//			GregorianCalendar gcGrenzeOben = new GregorianCalendar(2999, 11, 31);
//			GregorianCalendar gcDatum = new GregorianCalendar();
//
//			gcDatum.setTime(date);
//
//			if (String.valueOf(gcDatum.get(Calendar.YEAR)).length() == 2) {
//
//				int iJahrZweistellig = gcDatum.get(Calendar.YEAR) % 100;
//				int iJahrhundert;
//				if (iJahrZweistellig >= 40) {
//					iJahrhundert = 1900;
//				} else {
//					iJahrhundert = 2000;
//				}
//				gcDatum.set(Calendar.YEAR, iJahrZweistellig + iJahrhundert);
//
//			}
//
//			date = new java.sql.Date(gcDatum.getTime().getTime());
//
//			if (date.before(gcGrenzeUnten.getTime()) || date.after(gcGrenzeOben.getTime())) {
//				setDate(null, true);
//			} else {
//				myLogger.debug("Date to set '" + date.toLocaleString());
//	         	setDate(date, firePropertyChanged);
//		} catch (Exception e) {
//			// ignore
//		}
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		select(0, getText().length());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		if (locale == getLocale() || ignoreDatePatternChange) {
			return;
		}

		super.setLocale(locale);
		dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(
				DateFormat.MEDIUM, locale);
		setToolTipText(dateFormatter.toPattern());

		setDate(date, false);
		doLayout();
	}

	/**
	 * Creates a mask from a date pattern. This is a very simple (and
	 * incomplete) implementation thet works only with numbers. A date pattern
	 * of "MM/dd/yy" will result in the mask "##/##/##". Probably you want to
	 * override this method if it does not fit your needs.
	 *
	 * @param datePattern
	 *            the date pattern
	 * @return the mask
	 */
	public String createMaskFromDatePattern(String datePattern) {
		String symbols = "GyMdkHmsSEDFwWahKzZ";
		String mask = "";
		for (int i = 0; i < datePattern.length(); i++) {
			char ch = datePattern.charAt(i);
			boolean symbolFound = false;
			for (int n = 0; n < symbols.length(); n++) {
				if (symbols.charAt(n) == ch) {
					mask += "#";
					symbolFound = true;
					break;
				}
			}
			if (!symbolFound) {
				mask += ch;
			}
		}
		return mask;
	}

	/**
	 * Returns true, if the mask is visible.
	 *
	 * @return true, if the mask is visible
	 */
	public boolean isMaskVisible() {
		return isMaskVisible;
	}

	/**
	 * Sets the mask visible.
	 *
	 * @param isMaskVisible
	 *            true, if the mask should be visible
	 */
	public void setMaskVisible(boolean isMaskVisible) {
		this.isMaskVisible = isMaskVisible;
		if (isMaskVisible) {
			if (maskFormatter == null) {
				try {
					maskFormatter = new MaskFormatter(
							createMaskFromDatePattern(datePattern));
					maskFormatter.setPlaceholderCharacter(this.placeholder);
					maskFormatter.install(this);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns the preferred size. If a date pattern is set, it is the size the
	 * date pattern would take.
	 */
	public Dimension getPreferredSize() {
		return datePatternDimension != null ? datePatternDimension : super.getPreferredSize();
//		if (datePattern != null) {
//			return new JTextField(datePattern).getPreferredSize();
//		}
//		return super.getPreferredSize();
	}

	private void setDatePatternDimension(String pattern) {
		if (pattern == null) {
			datePatternDimension = null;
			return;
		}
		// PJ20484
		// wg. zu kleiner Datumsfelder in manchen Umgebungen
		int extraWidthSpace = 8;
		Dimension prefSize = new JTextField(datePattern).getPreferredSize();
		prefSize.setSize(new Dimension(
				new Double(prefSize.getWidth()).intValue() + extraWidthSpace, 
				new Double(prefSize.getHeight()).intValue()));
		datePatternDimension = prefSize;
	}
	
	/**
	 * Validates the typed date and sets it (only if it is valid).
	 */
	public void actionPerformed(ActionEvent e) {
		checkText(true);
	}

	/**
	 * Enables and disabled the compoment. It also fixes the background bug
	 * 4991597 and sets the background explicitely to a
	 * TextField.inactiveBackground.
	 */
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		if (!b) {
			super.setBackground(UIManager
					.getColor("TextField.inactiveBackground"));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.lp.client.frame.component.calendar.IDateEditor#getMaxSelectableDate()
	 */
	public Date getMaxSelectableDate() {
		return dateUtil.getMaxSelectableDate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.lp.client.frame.component.calendar.IDateEditor#getMinSelectableDate()
	 */
	public Date getMinSelectableDate() {
		return dateUtil.getMinSelectableDate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.lp.client.frame.component.calendar.IDateEditor#setMaxSelectableDate(java.util.Date)
	 */
	public void setMaxSelectableDate(Date max) {
		dateUtil.setMaxSelectableDate(max);
		checkText(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.lp.client.frame.component.calendar.IDateEditor#setMinSelectableDate(java.util.Date)
	 */
	public void setMinSelectableDate(Date min) {
		dateUtil.setMinSelectableDate(min);
		checkText(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.lp.client.frame.component.calendar.IDateEditor#setSelectableDateRange(java.util.Date,
	 * java.util.Date)
	 */
	public void setSelectableDateRange(Date min, Date max) {
		dateUtil.setSelectableDateRange(min, max);
		checkText(true);
	}

	/**
	 * Creates a JFrame with a JCalendar inside and can be used for testing.
	 *
	 * @param s
	 *            The command line arguments
	 */
	public static void main(String[] s) {
		JFrame frame = new JFrame("JTextFieldDateEditor");
		JTextFieldDateEditor jTextFieldDateEditor = new JTextFieldDateEditor();
		jTextFieldDateEditor.setDate(new Date());
		frame.getContentPane().add(jTextFieldDateEditor);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void setToken(String token) {
		cib.setToolTipToken(token);
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}

	@Override
	public void removeCib() {
		cib = null;
	}
	@Override
	public String getToken() {
		return cib.getToolTipToken();
	}
}
