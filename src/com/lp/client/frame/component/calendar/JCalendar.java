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
 *  JCalendar.java  - A bean for choosing a date
 *  Copyright (C) 2004 Kai Toedter
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;

import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.calendar.components.UTF8ResourceBundle;
// import com.lp.client.pc.LPMain;
import com.lp.client.pc.LPMain;

/**
 * JCalendar is a bean for entering a date by choosing the year, month and day.
 *
 * @author Kai Toedter
 * @version $LastChangedRevision: 159 $
 * @version $LastChangedDate: 2011-06-22 21:07:24 +0200 (Mi, 22 Jun 2011) $
 */
public class JCalendar extends JPanel implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = -4440192491417925656L;

	private Calendar calendar;
	private boolean initialized = false;
	private final JPanel monthYearPanel;
	private final JPanel specialButtonPanel;
	private boolean isTodayButtonVisible;
	private boolean isYesterdayButtonVisible;

	private boolean isNullDateButtonVisible;
	private final String defaultTodayButtonText = LPMain.getTextRespectUISPr("lp.heute");
	private final String defaultYesterdayButtonText = LPMain.getTextRespectUISPr("lp.gestern");
	private final String defaultNullDateButtonText = "No Date";
	private String todayButtonText;
	private String yesterdayButtonText;
	private String nullDateButtonText;

	private static final String AC_WEEKUP = "weekup";
	private static final String AC_WEEKDOWN = "weekdown";

	/** the day chooser */
	protected JDayChooser dayChooser;

	/** indicates if weeks of year shall be visible */
	protected boolean weekOfYearVisible = true;

	/** the locale */
	protected Locale locale;

	/** the month chooser */
	protected JMonthChooser monthChooser;

	/** the year chooser */
	protected JYearChooser yearChooser;

	private final JButton todayButton;

	private final JButton yesterdayButton;

	private final JButton nullDateButton;

	/**
	 * Default JCalendar constructor.
	 */
	public JCalendar() {
		this(null, null, true, true);
	}

	/**
	 * JCalendar constructor which allows the initial date to be set.
	 *
	 * @param date the date
	 */
	public JCalendar(Date date) {
		this(date, null, true, true);
	}

	/**
	 * JCalendar constructor which allows the initial calendar to be set.
	 *
	 * @param calendar the calendar
	 */
	public JCalendar(Calendar calendar) {
		this(null, null, true, true);
		setCalendar(calendar);
	}

	/**
	 * JCalendar constructor allowing the initial locale to be set.
	 *
	 * @param locale the new locale
	 */
	public JCalendar(Locale locale) {
		this(null, locale, true, true);
	}

	/**
	 * JCalendar constructor specifying both the initial date and locale.
	 *
	 * @param date   the date
	 * @param locale the new locale
	 */
	public JCalendar(Date date, Locale locale) {
		this(date, locale, true, true);
	}

	/**
	 * JCalendar constructor specifying both the initial date and the month spinner
	 * type.
	 *
	 * @param date         the date
	 * @param monthSpinner false, if no month spinner should be used
	 */
	public JCalendar(Date date, boolean monthSpinner) {
		this(date, null, monthSpinner, true);
	}

	/**
	 * JCalendar constructor specifying both the locale and the month spinner.
	 *
	 * @param locale       the locale
	 * @param monthSpinner false, if no month spinner should be used
	 */
	public JCalendar(Locale locale, boolean monthSpinner) {
		this(null, locale, monthSpinner, true);
	}

	/**
	 * JCalendar constructor specifying the month spinner type.
	 *
	 * @param monthSpinner false, if no month spinner should be used
	 */
	public JCalendar(boolean monthSpinner) {
		this(null, null, monthSpinner, true);
	}

	/**
	 * JCalendar constructor with month spinner parameter.
	 *
	 * @param date              the date
	 * @param locale            the locale
	 * @param monthSpinner      false, if no month spinner should be used
	 * @param weekOfYearVisible true, if weeks of year shall be visible
	 */
	public JCalendar(Date date, Locale locale, boolean monthSpinner, boolean weekOfYearVisible) {

		setName("JCalendar");

		// needed for setFont() etc.
		dayChooser = null;
		monthChooser = null;
		yearChooser = null;
		this.weekOfYearVisible = weekOfYearVisible;

		if (locale == null) {
			this.locale = Locale.getDefault(Locale.Category.FORMAT);
		} else {
			this.locale = locale;
		}

		LookAndFeel actualLookAndFeel = javax.swing.UIManager.getLookAndFeel();

		try {
			javax.swing.UIManager.setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
		} catch (Throwable exc) {

			LPMain.getInstance().exitClientNowErrorDlg(exc);
		}

		calendar = Calendar.getInstance(this.locale);

		setLayout(new BorderLayout());

		monthYearPanel = new JPanel();
		monthYearPanel.setLayout(new BorderLayout());

		monthChooser = new JMonthChooser(monthSpinner);
		yearChooser = new JYearChooser();
		monthChooser.setYearChooser(yearChooser);
		monthChooser.setLocale(this.locale);
		monthYearPanel.add(monthChooser, BorderLayout.WEST);
		monthYearPanel.add(yearChooser, BorderLayout.CENTER);
		monthYearPanel.setBorder(BorderFactory.createEmptyBorder());

		dayChooser = new JDayChooser(weekOfYearVisible);
		dayChooser.addPropertyChangeListener(this);
		dayChooser.setLocale(this.locale);

		dayChooser.getButtonWeekDown().addActionListener(this);
		dayChooser.getButtonWeekUp().addActionListener(this);

		monthChooser.addPropertyChangeListener(this);
		yearChooser.addPropertyChangeListener(this);
		add(monthYearPanel, BorderLayout.NORTH);
		add(dayChooser, BorderLayout.CENTER);

		specialButtonPanel = new JPanel();
		todayButton = ButtonFactory.createJButton();
		todayButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDate(new Date());
				firePropertyChange("daySelected", false, true);
			}
		});
		yesterdayButton = ButtonFactory.createJButton();
		yesterdayButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Calendar yesterday = Calendar.getInstance(JCalendar.this.locale);
				yesterday.add(Calendar.DAY_OF_YEAR, -1);
				setCalendar(yesterday);
				firePropertyChange("daySelected", false, true);
			}
		});
		nullDateButton = ButtonFactory.createJButton();
		nullDateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dayChooser.firePropertyChange("day", 0, -1);
			}
		});
		specialButtonPanel.setVisible(false);
		add(specialButtonPanel, BorderLayout.SOUTH);

		// Set the initialized flag before setting the calendar. This will
		// cause the other components to be updated properly.
		if (date != null) {
			calendar.setTime(date);
		}

		initialized = true;

		setCalendar(calendar);

		// PJ 14586
		try {
			javax.swing.UIManager.setLookAndFeel(actualLookAndFeel);
		} catch (Throwable exc) {

			LPMain.getInstance().exitClientNowErrorDlg(exc);
		}

	}

	/**
	 * Creates a JFrame with a JCalendar inside and can be used for testing.
	 *
	 * @param s The command line arguments
	 */
	public static void main(String[] s) {
		JFrame frame = new JFrame("JCalendar");

		JCalendar jcalendar = new JCalendar();
		frame.getContentPane().add(jcalendar);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Returns the calendar property.
	 *
	 * @return the value of the calendar property.
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * Gets the dayChooser attribute of the JCalendar object
	 *
	 * @return the dayChooser value
	 */
	public JDayChooser getDayChooser() {
		return dayChooser;
	}

	/**
	 * Returns the locale.
	 *
	 * @return the value of the locale property.
	 *
	 * @see #setLocale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Gets the monthChooser attribute of the JCalendar object
	 *
	 * @return the monthChooser value
	 */
	public JMonthChooser getMonthChooser() {
		return monthChooser;
	}

	/**
	 * Gets the yearChooser attribute of the JCalendar object
	 *
	 * @return the yearChooser value
	 */
	public JYearChooser getYearChooser() {
		return yearChooser;
	}

	/**
	 * Indicates if the weeks of year are visible..
	 *
	 * @return boolean true, if weeks of year are visible
	 */
	public boolean isWeekOfYearVisible() {
		return dayChooser.isWeekOfYearVisible();
	}

	/**
	 * JCalendar is a PropertyChangeListener, for its day, month and year chooser.
	 *
	 * @param evt the property change event
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (calendar != null) {
			Calendar c = (Calendar) calendar.clone();

			if (evt.getPropertyName().equals("day")) {
				c.set(Calendar.DAY_OF_MONTH, ((Integer) evt.getNewValue()).intValue());
				setCalendar(c, false);
			} else if (evt.getPropertyName().equals("month")) {
				int diff = (int) evt.getNewValue() - c.get(Calendar.MONTH);
				c.add(Calendar.MONTH, diff);
				setCalendar(c, false);
			} else if (evt.getPropertyName().equals("year")) {
				int diff = (int) evt.getNewValue() - c.get(Calendar.YEAR);
				c.add(Calendar.YEAR, diff);
				setCalendar(c, false);
			} else if (evt.getPropertyName().equals("date")) {
				c.setTime((Date) evt.getNewValue());
				setCalendar(c, true);
			} else if (evt.getPropertyName().equals("daySelected")) {
				firePropertyChange("daySelected", false, true);
			}
		}
	}

	/**
	 * Sets the background color.
	 *
	 * @param bg the new background
	 */
	public void setBackground(Color bg) {
		super.setBackground(bg);

		if (dayChooser != null) {
			dayChooser.setBackground(bg);
		}
	}

	/**
	 * Sets the calendar property. This is a bound property.
	 *
	 * @param c the new calendar
	 * @throws NullPointerException - if c is null;
	 * @see #getCalendar
	 */
	public void setCalendar(Calendar c) {
		setCalendar(c, true);
	}

	/**
	 * Sets the calendar attribute of the JCalendar object
	 *
	 * @param c      the new calendar value
	 * @param update If any bound properties should be updated
	 * @throws NullPointerException - if c is null;
	 */
	private void setCalendar(Calendar c, boolean update) {
		if (c == null) {
			setDate(null);
		}
		Calendar oldCalendar = calendar;
		calendar = c;

		if (update) {
			// Thanks to Jeff Ulmer for correcting a bug in the sequence :)
			yearChooser.setYear(c.get(Calendar.YEAR));
			monthChooser.setMonth(c.get(Calendar.MONTH));
			dayChooser.setDay(c.get(Calendar.DATE));
		}
		dayChooser.setMonth(c.get(Calendar.MONTH));
		dayChooser.setYear(c.get(Calendar.YEAR));

		firePropertyChange("calendar", oldCalendar, calendar);
	}

	/**
	 * Enable or disable the JCalendar.
	 *
	 * @param enabled the new enabled value
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (dayChooser != null) {
			dayChooser.setEnabled(enabled);
			monthChooser.setEnabled(enabled);
			yearChooser.setEnabled(enabled);
		}
	}

	/**
	 * Returns true, if enabled.
	 *
	 * @return true, if enabled.
	 */
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Sets the font property.
	 *
	 * @param font the new font
	 */
	public void setFont(Font font) {
		super.setFont(font);

		if (dayChooser != null) {
			dayChooser.setFont(font);
			monthChooser.setFont(font);
			yearChooser.setFont(font);
		}
	}

	/**
	 * Sets the foreground color.
	 *
	 * @param fg the new foreground
	 */
	public void setForeground(Color fg) {
		super.setForeground(fg);

		if (dayChooser != null) {
			dayChooser.setForeground(fg);
			monthChooser.setForeground(fg);
			yearChooser.setForeground(fg);
		}
	}

	/**
	 * Sets the locale property. This is a bound property.
	 *
	 * @param l the new locale value
	 *
	 * @see #getLocale
	 */
	public void setLocale(Locale l) {
		if (!initialized) {
			super.setLocale(l);
		} else {
			Locale oldLocale = locale;
			locale = l;
			dayChooser.setLocale(locale);
			monthChooser.setLocale(locale);
			relayoutSpecialButtonPanel();
			firePropertyChange("locale", oldLocale, locale);
		}
	}

	/**
	 * Sets the week of year visible.
	 *
	 * @param weekOfYearVisible true, if weeks of year shall be visible
	 */
	public void setWeekOfYearVisible(boolean weekOfYearVisible) {
		dayChooser.setWeekOfYearVisible(weekOfYearVisible);
		setLocale(locale); // hack for doing complete new layout :)
	}

	/**
	 * Gets the visibility of the decoration background.
	 *
	 * @return true, if the decoration background is visible.
	 */
	public boolean isDecorationBackgroundVisible() {
		return dayChooser.isDecorationBackgroundVisible();
	}

	/**
	 * Sets the decoration background visible.
	 *
	 * @param decorationBackgroundVisible true, if the decoration background should
	 *                                    be visible.
	 */
	public void setDecorationBackgroundVisible(boolean decorationBackgroundVisible) {
		dayChooser.setDecorationBackgroundVisible(decorationBackgroundVisible);
		setLocale(locale); // hack for doing complete new layout :)
	}

	/**
	 * Gets the visibility of the decoration border.
	 *
	 * @return true, if the decoration border is visible.
	 */
	public boolean isDecorationBordersVisible() {
		return dayChooser.isDecorationBordersVisible();
	}

	/**
	 * Sets the decoration borders visible.
	 *
	 * @param decorationBordersVisible true, if the decoration borders should be
	 *                                 visible.
	 */
	public void setDecorationBordersVisible(boolean decorationBordersVisible) {
		dayChooser.setDecorationBordersVisible(decorationBordersVisible);
		setLocale(locale); // hack for doing complete new layout :)
	}

	/**
	 * Returns the color of the decoration (day names and weeks).
	 *
	 * @return the color of the decoration (day names and weeks).
	 */
	public Color getDecorationBackgroundColor() {
		return dayChooser.getDecorationBackgroundColor();
	}

	/**
	 * Sets the background of days and weeks of year buttons.
	 *
	 * @param decorationBackgroundColor the background color
	 */
	public void setDecorationBackgroundColor(Color decorationBackgroundColor) {
		dayChooser.setDecorationBackgroundColor(decorationBackgroundColor);
	}

	/**
	 * Returns the Sunday foreground.
	 *
	 * @return Color the Sunday foreground.
	 */
	public Color getSundayForeground() {
		return dayChooser.getSundayForeground();
	}

	/**
	 * Returns the weekday foreground.
	 *
	 * @return Color the weekday foreground.
	 */
	public Color getWeekdayForeground() {
		return dayChooser.getWeekdayForeground();
	}

	/**
	 * Sets the Sunday foreground.
	 *
	 * @param sundayForeground the sundayForeground to set
	 */
	public void setSundayForeground(Color sundayForeground) {
		dayChooser.setSundayForeground(sundayForeground);
	}

	/**
	 * Sets the weekday foreground.
	 *
	 * @param weekdayForeground the weekdayForeground to set
	 */
	public void setWeekdayForeground(Color weekdayForeground) {
		dayChooser.setWeekdayForeground(weekdayForeground);
	}

	/**
	 * Returns a Date object.
	 *
	 * @return a date object constructed from the calendar property.
	 */
	public Date getDate() {
		return calendar.getTime();
	}

	/**
	 * Sets the date. Fires the property change "date".
	 *
	 * @param date the new date.
	 * @throws NullPointerException - if the date is null
	 */
	public void setDate(Date date) {
		Date oldDate = calendar.getTime();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		yearChooser.setYear(year);
		monthChooser.setMonth(month);
		dayChooser.setCalendar(calendar);
		dayChooser.setDay(day);

		firePropertyChange("date", oldDate, date);
	}

	/**
	 * Sets a valid date range for selectable dates. If max is before min, the
	 * default range with no limitation is set.
	 *
	 * @param min the minimum selectable date or null (then the minimum date is set
	 *            to 01\01\0001)
	 * @param max the maximum selectable date or null (then the maximum date is set
	 *            to 01\01\9999)
	 */
	public void setSelectableDateRange(Date min, Date max) {
		dayChooser.setSelectableDateRange(min, max);
	};

	/**
	 * Gets the minimum selectable date.
	 *
	 * @return the minimum selectable date
	 */
	public Date getMaxSelectableDate() {
		return dayChooser.getMaxSelectableDate();
	}

	/**
	 * Gets the maximum selectable date.
	 *
	 * @return the maximum selectable date
	 */
	public Date getMinSelectableDate() {
		return dayChooser.getMinSelectableDate();
	}

	/**
	 * Sets the maximum selectable date.
	 *
	 * @param max maximum selectable date
	 */
	public void setMaxSelectableDate(Date max) {
		dayChooser.setMaxSelectableDate(max);
	}

	/**
	 * Sets the minimum selectable date.
	 *
	 * @param min minimum selectable date
	 */
	public void setMinSelectableDate(Date min) {
		dayChooser.setMinSelectableDate(min);
	}

	/**
	 * Gets the maximum number of characters of a day name or 0. If 0 is returned,
	 * dateFormatSymbols.getShortWeekdays() will be used.
	 *
	 * @return the maximum number of characters of a day name or 0.
	 */
	public int getMaxDayCharacters() {
		return dayChooser.getMaxDayCharacters();
	}

	/**
	 * Sets the maximum number of characters per day in the day bar. Valid values
	 * are 0-4. If set to 0, dateFormatSymbols.getShortWeekdays() will be used,
	 * otherwise theses strings will be reduced to the maximum number of characters.
	 *
	 * @param maxDayCharacters the maximum number of characters of a day name.
	 */
	public void setMaxDayCharacters(int maxDayCharacters) {
		dayChooser.setMaxDayCharacters(maxDayCharacters);
	}

	/**
	 * Sets the Today button visible.
	 *
	 * @param isTodayButtonVisible true, is the today button shall be visible.
	 */
	public void setTodayButtonVisible(boolean isTodayButtonVisible) {
		this.isTodayButtonVisible = isTodayButtonVisible;
		relayoutSpecialButtonPanel();
	}

	/**
	 * Sets the Yesterday button visible.
	 *
	 * @param isTodayButtonVisible true, is the today button shall be visible.
	 */
	public void setYesterdayButtonVisible(boolean isYesterdayButtonVisible) {
		this.isYesterdayButtonVisible = isYesterdayButtonVisible;
		relayoutSpecialButtonPanel();
	}

	/**
	 * @return true, if Today button is visible.
	 */
	public boolean isTodayButtonVisible() {
		return isTodayButtonVisible;
	}

	/**
	 * @return true, if Today button is visible.
	 */
	public boolean isYesterdayButtonVisible() {
		return isYesterdayButtonVisible;
	}

	/**
	 * Sets the Null Date button visible.
	 *
	 * @param isNullDateButtonVisible true, is the Null Date button shall be
	 *                                visible.
	 */
	public void setNullDateButtonVisible(boolean isNullDateButtonVisible) {
		this.isNullDateButtonVisible = isNullDateButtonVisible;
		relayoutSpecialButtonPanel();
	}

	/**
	 * @return true, if Null Date button is visible.
	 */
	public boolean isNullDateButtonVisible() {
		return isNullDateButtonVisible;
	}

	private void relayoutSpecialButtonPanel() {
		ResourceBundle resourceBundle = null;

		try {
			resourceBundle = UTF8ResourceBundle.getBundle("com.lp.client.res.jcalendar", locale);
		} catch (Exception e) {
			// ignore, fall back to set texts or defaults
			System.out.println("Exception caught with message: " + e.getMessage() + ", fallback to default");
		}

		specialButtonPanel.removeAll();
		int buttonCount = 0;
		if (isYesterdayButtonVisible) {
			String text = yesterdayButtonText;
			if (text == null) {
				text = defaultYesterdayButtonText;
			}
			yesterdayButton.setText(text);
			specialButtonPanel.add(yesterdayButton);
			buttonCount++;
		}
		if (isTodayButtonVisible) {
			String text = todayButtonText;
			if (text == null && resourceBundle != null) {
				try {
					text = resourceBundle.getString("todayButton.text");
				} catch (Exception e) {
					// ignore, fall back to set texts or defaults
				}
			}
			if (text == null) {
				text = defaultTodayButtonText;
			}
			todayButton.setText(text);
			specialButtonPanel.add(todayButton);
			buttonCount++;
		}
		if (isNullDateButtonVisible) {
			String text = nullDateButtonText;
			if (text == null && resourceBundle != null) {
				try {
					text = resourceBundle.getString("nullDateButton.text");
				} catch (Exception e) {
					// ignore, fall back to set texts or defaults
				}
			}
			if (text == null) {
				text = defaultNullDateButtonText;
			}
			nullDateButton.setText(text);
			specialButtonPanel.add(nullDateButton);
			buttonCount++;
		}

		specialButtonPanel.setLayout(new GridLayout(1, buttonCount));
//		if (isTodayButtonVisible) {
//			specialButtonPanel.add(todayButton);
//		}
//		if (isNullDateButtonVisible) {
//			specialButtonPanel.add(nullDateButton);
//		}

		specialButtonPanel.setVisible(isNullDateButtonVisible || isYesterdayButtonVisible || isTodayButtonVisible);

//		todayButton.invalidate();
//		todayButton.repaint();
//		nullDateButton.invalidate();
//		nullDateButton.repaint();
		specialButtonPanel.invalidate();
		specialButtonPanel.doLayout();
		specialButtonPanel.repaint();
		invalidate();
		repaint();
	}

	/**
	 * @return the text of the Today button
	 */
	public String getTodayButtonText() {
		return todayButtonText;
	}

	/**
	 * @return the text of the Yesterday button
	 */
	public String getYesterdayButtonText() {
		return yesterdayButtonText;
	}

	/**
	 * Sets the Today button text.
	 *
	 * @param todayButtonText the new text
	 */
	public void setTodayButtonText(String todayButtonText) {
		if (todayButtonText != null & todayButtonText.trim().length() == 0) {
			this.todayButtonText = null;
		} else {
			this.todayButtonText = todayButtonText;
		}
		relayoutSpecialButtonPanel();
	}

	/**
	 * Sets the Yesterday button text.
	 *
	 * @param todayButtonText the new text
	 */
	public void setYesterdayButtonText(String yesterdayButtonText) {
		if (yesterdayButtonText != null & yesterdayButtonText.trim().length() == 0) {
			this.yesterdayButtonText = null;
		} else {
			this.yesterdayButtonText = yesterdayButtonText;
		}
		relayoutSpecialButtonPanel();
	}

	/**
	 * @return the text of the Null Date button
	 */
	public String getNullDateButtonText() {
		return nullDateButtonText;
	}

	/**
	 * Sets the Null Date button text.
	 *
	 * @param nullDateButtonText the new text
	 */
	public void setNullDateButtonText(String nullDateButtonText) {
		if (nullDateButtonText != null && nullDateButtonText.trim().length() == 0) {
			this.nullDateButtonText = null;
		} else {
			this.nullDateButtonText = nullDateButtonText;
		}
		relayoutSpecialButtonPanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (AC_WEEKUP.equals(e.getActionCommand()) || AC_WEEKDOWN.equals(e.getActionCommand())) {

			Calendar c = (Calendar) calendar.clone();
			c.add(Calendar.DAY_OF_MONTH, AC_WEEKUP.equals(e.getActionCommand()) ? -7 : 7);

			setCalendar(c, true);

		}
	}

}
