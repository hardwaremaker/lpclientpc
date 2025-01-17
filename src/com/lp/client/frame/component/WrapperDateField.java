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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.calendar.IDateEditor;
import com.lp.client.frame.component.calendar.JCalendar;
import com.lp.client.frame.component.calendar.JTextFieldDateEditor;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.util.Helper;

/**
 * A date chooser containig a date editor and a button, that makes a JCalendar
 * visible for choosing a date. If no date editor is specified, a
 * JTextFieldDateEditor is used as default.
 *
 * @author Kai Toedter
 * @version $LastChangedRevision: 101 $
 * @version $LastChangedDate: 2006-06-04 14:42:29 +0200 (So, 04 Jun 2006) $
 */
public class WrapperDateField extends JPanel
		implements ActionListener, IControl, PropertyChangeListener, FocusListener {
	private static final long serialVersionUID = -8301216956445623703L;

	private static final String ACTION_SPECIAL_LEEREN = "action_special_datefield_leeren";
	private static final int I_BREITE = 90;

// 	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private Locale locale;

	private ChangeListener changeListener;

	private JTextFieldDateEditor dateEditor;

	private BorderButton calendarButton;

	private JCalendar jcalendar;

	private JPopupMenu popup;

	protected boolean isInitialized;

	protected boolean dateSelected;

	private boolean bWithRubber = true;

	private boolean isMandatoryField = false;
	private boolean isMandatoryFieldDB = false;

	private boolean isActivatable = true;
	private JButton jbuSetNull = null;
	private Date oldDate = null;

	public WrapperDateField() {
		this(null, null);
	}

	public WrapperDateField(Date minDate) {
		this(minDate, null);
	}

	public WrapperDateField(Date minDate, Date maxDate) {

		try {
			locale = LPMain.getTheClient().getLocUi();
		} catch (Throwable t) {
			locale = Locale.getDefault();
		}

		dateEditor = new JTextFieldDateEditor();
		dateEditor.setLocale(locale);
		jcalendar = new JCalendar(null, locale);
		jbuSetNull = ButtonFactory.createJButton(IconFactory.getClear(), ACTION_SPECIAL_LEEREN);

		this.dateEditor.setMinimumSize(new Dimension(Defaults.getInstance().bySizeFactor(I_BREITE),
				Defaults.getInstance().getControlHeight()));
		this.dateEditor.setPreferredSize(new Dimension(Defaults.getInstance().bySizeFactor(I_BREITE),
				Defaults.getInstance().getControlHeight()));
		jbuSetNull.setMinimumSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));
		jbuSetNull.setPreferredSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

		setLayout(new GridBagLayout());

		jcalendar.setTodayButtonVisible(true);
		jcalendar.setYesterdayButtonVisible(true);

		setDateFormatString(null);
		setDate(null);

		jcalendar.getDayChooser().setAlwaysFireDayProperty(true);

		// Display a calendar button with an icon
		calendarButton = new BorderButton(IconFactory.getDateChooser()) {

			private static final long serialVersionUID = 1L;

			public boolean isFocusable() {
				return false;
			}
		};
		HelperClient.setButtonBackgroundBorderColor(calendarButton, Defaults.getInstance().getButtonBackgroundColor(),
				Defaults.getInstance().getButtonBorderColor());
		calendarButton.setDefaultBorder(calendarButton.getBorder());

		calendarButton.setMinimumSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

		calendarButton.setPreferredSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));
		calendarButton.setMargin(new Insets(0, 0, 0, 0));

		// Alt + 'C' selects the calendar.
//        calendarButton.setMnemonic(KeyEvent.VK_C);

		add(this.dateEditor.getUiComponent(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		add(calendarButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(jbuSetNull, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		add(new JLabel(), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if (minDate != null || maxDate != null) {
			setSelectableDateRange(minDate, maxDate);
		}

		popup = new JPopupMenu() {

			private static final long serialVersionUID = 1L;

			public void setVisible(boolean b) {
				Boolean isCanceled = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");
				if (b || dateSelected || ((isCanceled != null) && isCanceled.booleanValue())) {
					super.setVisible(b);
					SwingUtilities.invokeLater(getDialogIfDiffMoreThanAsRunnable(getDate(), 5));
				}
			}
		};

		popup.setLightWeightPopupEnabled(true);

		popup.add(jcalendar);

		setListener();

		isInitialized = true;
	}

	private void setListener() {
		dateEditor.addPropertyChangeListener("date", this);
		dateEditor.addFocusListener(this);
		calendarButton.addFocusListener(this);

		calendarButton.addActionListener(this);

		jbuSetNull.setActionCommand(ACTION_SPECIAL_LEEREN);
		jbuSetNull.addActionListener(this);

		jcalendar.addPropertyChangeListener(this);

		// Corrects a problem that occurred when the JMonthChooser's combobox is
		// displayed, and a click outside the popup does not close it.

		// The following idea was originally provided by forum user
		// podiatanapraia:
		changeListener = new ChangeListener() {
			boolean hasListened = false;

			public void stateChanged(ChangeEvent e) {
				if (hasListened) {
					hasListened = false;
					return;
				}
				if (popup != null && popup.isVisible() && jcalendar.getMonthChooser().getComboBox().hasFocus()) {
					MenuElement[] me = MenuSelectionManager.defaultManager().getSelectedPath();
					MenuElement[] newMe = new MenuElement[me.length + 1];
					newMe[0] = popup;
					for (int i = 0; i < me.length; i++) {
						newMe[i + 1] = me[i];
					}
					hasListened = true;
					MenuSelectionManager.defaultManager().setSelectedPath(newMe);

				}
			}
		};
		MenuSelectionManager.defaultManager().addChangeListener(changeListener);
		// end of code provided by forum user podiatanapraia

	}

	public void removeContent() {
		this.setDate(null);
	}

	/**
	 * Called when the jalendar button was pressed.
	 *
	 * @param e the action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(jbuSetNull)) {
			setDate(null);

			if (getParent() instanceof WrapperTimestampField) {
				WrapperTimestampField wtf = (WrapperTimestampField) getParent();
				wtf.getWtfZeit().removeContent();
			}

		}

		else {
			if (!popup.isVisible()) {
				int x = calendarButton.getWidth() - (int) popup.getPreferredSize().getWidth();
				int y = calendarButton.getY() + calendarButton.getHeight();

				Date date = dateEditor.getDate();

				jcalendar.setDate(date != null ? date : new Date());
				popup.show(calendarButton, x, y);
				dateSelected = false;

				jcalendar.updateUI();
				jcalendar.getDayChooser().updateUI();
			}
		}

	}

	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		if (!isActivatable)
			setEditable(false);
	}

	public boolean isActivatable() {
		return isActivatable;
	}

	/**
	 * Listens for a "date" property change or a "day" property change event from
	 * the JCalendar. Updates the date editor and closes the popup.
	 *
	 * @param evt the event
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals("calendar")) {
			setDate(jcalendar.getDate());
		} else if (evt.getPropertyName().equals("daySelected")) {
			if (popup.isVisible()) {
				dateSelected = true;
				popup.setVisible(false);
			}
		} else if (evt.getPropertyName().equals("date")) {
			if (evt.getSource() == dateEditor) {
				firePropertyChange("date", evt.getOldValue(), evt.getNewValue());
			} else {
				setDate((Date) evt.getNewValue());
			}
		}
	}

	private void showDialogIfDiffMoreThan(Date date, int allowedDifferenceYears) {
		if (date == null || date.equals(oldDate))
			return;
		Calendar now = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime());

		oldDate = new Date(date.getTime());

		int diff = now.get(Calendar.YEAR) - c.get(Calendar.YEAR);
		if (Math.abs(diff) < allowedDifferenceYears)
			return;

		String token = diff > 0 ? "calendar.info.vergangenheit" : "calendar.info.zukunft";

		JOptionPane.showMessageDialog(LPMain.getInstance().getDesktop(),
				LPMain.getMessageTextRespectUISPr(token, dateEditor.getText()),
				LPMain.getTextRespectUISPr("calendar.info.titel"), JOptionPane.INFORMATION_MESSAGE);

//		JOptionPane.showMessageDialog(LPMain.getInstance().getDesktop(), dateEditor.getText() + "\n" + LPMain.getTextRespectUISPr(token),
//				LPMain.getTextRespectUISPr("calendar.info.titel"), JOptionPane.INFORMATION_MESSAGE);
	}

	private Runnable getDialogIfDiffMoreThanAsRunnable(final Date date, final int allowedDifferenceYears) {
		return new Runnable() {
			@Override
			public void run() {
				showDialogIfDiffMoreThan(date, allowedDifferenceYears);
			}
		};
	}

	/**
	 * Updates the UI of itself and the popup.
	 */
	public void updateUI() {
		super.updateUI();
		setEnabled(isEnabled());

		if (jcalendar != null) {
			SwingUtilities.updateComponentTreeUI(popup);
		}
	}

	/**
	 * Sets the locale.
	 *
	 * @param l The new locale value
	 */
	public void setLocale(Locale l) {
		super.setLocale(l);
		dateEditor.setLocale(l);
		jcalendar.setLocale(l);
	}

	/**
	 * Gets the date format string.
	 *
	 * @return Returns the dateFormatString.
	 */
	public String getDateFormatString() {
		return dateEditor.getDateFormatString();
	}

	/**
	 * Sets the date format string. E.g "MMMMM d, yyyy" will result in "July 21,
	 * 2004" if this is the selected date and locale is English.
	 *
	 * @param dfString The dateFormatString to set.
	 */
	public void setDateFormatString(String dfString) {
		dateEditor.setDateFormatString(dfString);
		invalidate();
	}

	/**
	 * Returns the date. If the JDateChooser is started with a null date and no date
	 * was set by the user, null is returned.
	 *
	 * @return the current date
	 */
	public java.sql.Date getDate() {
		Date d = dateEditor.getDate();
		return d != null ? new java.sql.Date(d.getTime()) : null;
	}

	/**
	 * Sets the date. Fires the property change "date" if date != null.
	 *
	 * @param date the new date.
	 */
	public void setDate(Date date) {
		dateEditor.setDate(date);
		if (getParent() != null) {
			getParent().invalidate();
		}
	}

	/**
	 * Returns the calendar. If the JDateChooser is started with a null date (or
	 * null calendar) and no date was set by the user, null is returned.
	 *
	 * @return the current calendar
	 */
	public Calendar getCalendar() {
		Date date = getDate();
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		dateEditor.setDate(calendar != null ? calendar.getTime() : null);
	}

	public void setDatumHeute() {
		setDate(Helper.cutDate(new Date()));
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (dateEditor != null) {
			dateEditor.setEnabled(enabled);
			calendarButton.setEnabled(enabled);

			if (!isMandatoryField) {
				if (bWithRubber) {
					jbuSetNull.setVisible(enabled);
				}
			}

			if (enabled) {
				dateEditor.getUiComponent().setBackground(HelperClient.getEditableColor());
			} else {
				dateEditor.getUiComponent().setBackground(HelperClient.getNotEditableColor());
			}

		}
	}

	public void cleanup() {
		jcalendar = null;
		popup = null;
	}

	public boolean isEnabled() {
		return super.isEnabled();
	}

	public JButton getCalendarButton() {
		return calendarButton;
	}

	public IDateEditor getDateEditor() {
		return dateEditor;
	}

	public void setSelectableDateRange(Date min, Date max) {
		jcalendar.setSelectableDateRange(min, max);
		this.dateEditor.setSelectableDateRange(jcalendar.getMinSelectableDate(), jcalendar.getMaxSelectableDate());
	}

	public void setMaxSelectableDate(Date max) {
		jcalendar.setMaxSelectableDate(max);
		this.dateEditor.setMaxSelectableDate(max);
	}

	public void setMinSelectableDate(Date min) {
		jcalendar.setMinSelectableDate(min);
		this.dateEditor.setMinSelectableDate(min);
	}

	public void setMaximumValue(Date dMaximum) {
		jcalendar.setMaxSelectableDate(dMaximum);
		dateEditor.setMaxSelectableDate(dMaximum);
	}

	public void setMinimumValue(Date dMinimum) {
		jcalendar.setMinSelectableDate(dMinimum);
		dateEditor.setMinSelectableDate(dMinimum);
	}

	public void setTimestamp(Timestamp t) {
		setDate(t != null ? new Date(t.getTime()) : null);
	}

	public Timestamp getTimestamp() {
		Date d = getDate();
		return d != null ? new Timestamp(d.getTime()) : null;
	}

	/**
	 * Gets the maximum selectable date.
	 *
	 * @return the maximum selectable date
	 */
	public Date getMaxSelectableDate() {
		return jcalendar.getMaxSelectableDate();
	}

	/**
	 * Gets the minimum selectable date.
	 *
	 * @return the minimum selectable date
	 */
	public Date getMinSelectableDate() {
		return jcalendar.getMinSelectableDate();
	}

	/**
	 * Should only be invoked if the JDateChooser is not used anymore. Due to popup
	 * handling it had to register a change listener to the default menu selection
	 * manager which will be unregistered here. Use this method to cleanup possible
	 * memory leaks.
	 */

	public JComponent getDisplay() {
		return dateEditor.getUiComponent();
	}

	public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
		this.isMandatoryFieldDB = isMandatoryFieldDB;
		if (isMandatoryFieldDB == true) {
			setMandatoryField(true);
		}
	}

	public boolean isMandatoryField() {
		return isMandatoryField || isMandatoryFieldDB;
	}

	public void setShowRubber(boolean bShowRubber) {
		jbuSetNull.setVisible(bShowRubber);
	}

	public void setMandatoryField(boolean isMandatoryField) {
		jbuSetNull.setVisible(!isMandatoryField);
		if (isMandatoryFieldDB == false || isMandatoryField == true) {
			this.isMandatoryField = isMandatoryField;
			if (this.isMandatoryField == true) {
				this.dateEditor.getUiComponent().setBorder(Defaults.getInstance().getMandatoryFieldBorder());
			} else {
				this.dateEditor.getUiComponent().setBorder(new WrapperTextField().getBorder());
			}
		}
	}

	public void setEditable(boolean bEditable) {
		this.dateEditor.getUiComponent().setEnabled(bEditable);
	}

	public boolean isMandatoryFieldDB() {
		return isMandatoryFieldDB;
	}

	public boolean isBwithRubber() {
		return bWithRubber;
	}

	public void setBwithRubber(boolean bwithRubber) {
		this.bWithRubber = bwithRubber;
	}

	@Override
	public void focusGained(FocusEvent arg0) {

	}

	@Override
	public void focusLost(FocusEvent arg0) {
		showDialogIfDiffMoreThan(getDate(), 5);
	}

	@Override
	public boolean hasContent() throws Throwable {
		return getDate() != null;// && dateEditor.checkDate();
	}

}
