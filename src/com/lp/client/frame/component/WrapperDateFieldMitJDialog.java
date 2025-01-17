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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.calendar.IDateEditor;
import com.lp.client.frame.component.calendar.JTextFieldDateEditor;
import com.lp.client.pc.LPMain;
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
public class WrapperDateFieldMitJDialog extends JPanel implements
		ActionListener, IControl, PropertyChangeListener, FocusListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected JTextFieldDateEditor dateEditor;

	protected JButton calendarButton;

	private Locale locale = null;

	protected boolean isInitialized;

	protected boolean dateSelected;

	private boolean bWithRubber = true;

	private ImageIcon imageIconLeeren = null;
	private boolean isMandatoryField = false;
	private boolean isMandatoryFieldDB = false;
	private boolean isActivatable = true;
	private JButton jbuSetNull = null;
	private Date oldDate = null;
	public final static int I_BREITE = 90;

	public void removeContent() {
		this.setDate(null);
	}

	private static final String ACTION_SPECIAL_LEEREN = "action_special_datefield_leeren";

	private ImageIcon getImageIconLeeren() {
		if (imageIconLeeren == null) {
			imageIconLeeren = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/leeren.png"));
		}
		return imageIconLeeren;
	}

	public WrapperDateFieldMitJDialog() {

		try {
			locale = LPMain.getTheClient().getLocUi();
		} catch (Throwable t) {
			locale = Locale.getDefault();
			/**
			 * TODO: Konstruktor soll throwen
			 */
		}
		dateEditor = new JTextFieldDateEditor();
		dateEditor.setLocale(locale);
		dateEditor.addPropertyChangeListener("date", this);
		dateEditor.addFocusListener(this);

		dateEditor.setLocale(locale);
		jbuSetNull = new JButton();
		jbuSetNull.setActionCommand(ACTION_SPECIAL_LEEREN);
		jbuSetNull.addActionListener(this);
		jbuSetNull.setIcon(getImageIconLeeren());

		this.dateEditor.setMinimumSize(new Dimension(Defaults.getInstance()
				.bySizeFactor(I_BREITE), Defaults.getInstance()
				.getControlHeight()));
		this.dateEditor.setPreferredSize(new Dimension(Defaults.getInstance()
				.bySizeFactor(I_BREITE), Defaults.getInstance()
				.getControlHeight()));
		jbuSetNull
				.setMinimumSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));
		jbuSetNull
				.setPreferredSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));

		setLayout(new GridBagLayout());

		setDateFormatString(null);
		setDate(null);

		// Display a calendar button with an icon
		URL iconURL = getClass().getResource(
				"/com/lp/client/res/JDateChooserIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);

		calendarButton = new JButton(icon) {

			/**
		 *
		 */
			private static final long serialVersionUID = 1L;

			public boolean isFocusable() {
				return false;
			}
		};
		calendarButton
				.setMinimumSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));

		calendarButton
				.setPreferredSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));
		calendarButton.setMargin(new Insets(0, 0, 0, 0));
		calendarButton.addActionListener(this);

		// Alt + 'C' selects the calendar.
		calendarButton.setMnemonic(KeyEvent.VK_C);

		add(this.dateEditor.getUiComponent(), new GridBagConstraints(0, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		add(calendarButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		add(jbuSetNull, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		add(new JLabel(), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		isInitialized = true;
	}

	/**
	 * Called when the jalendar button was pressed.
	 *
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(jbuSetNull)) {
			setDate(null);
		} else {

			DialogKalender dk = new DialogKalender(locale,
					dateEditor.getDate(), dateEditor.getMaxSelectableDate(),
					dateEditor.getMinSelectableDate());

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			int x = calendarButton.getWidth()
					- (int) dk.getPreferredSize().getWidth();
			int y = calendarButton.getY() + calendarButton.getHeight();

			int locationy = (int) calendarButton.getLocationOnScreen().getY()
					+ y;

			if (screenSize.getHeight() < (locationy + dk.getSize().getHeight())) {

				locationy = (int) screenSize.getHeight()
						- (int) dk.getSize().getHeight();

			}

			dk.setLocation((int) calendarButton.getLocationOnScreen().getX()
					+ x, locationy);
			dk.setVisible(true);

			if (dk.returnDate != null) {
				setDate(dk.returnDate);
				showDialogIfDiffMoreThan(getDate(), 5);
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
	 * Listens for a "date" property change or a "day" property change event
	 * from the JCalendar. Updates the date editor and closes the popup.
	 *
	 * @param evt
	 *            the event
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("date")) {
			if (evt.getSource() == dateEditor) {
				firePropertyChange("date", evt.getOldValue(), evt.getNewValue());
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

		String token = diff > 0 ? "calendar.info.vergangenheit"
				: "calendar.info.zukunft";

		JOptionPane.showMessageDialog(null,
				LPMain.getMessageTextRespectUISPr(token, dateEditor.getText()),
				LPMain.getTextRespectUISPr("calendar.info.titel"), JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Updates the UI of itself and the popup.
	 */
	public void updateUI() {
		super.updateUI();
		setEnabled(isEnabled());
	}

	/**
	 * Sets the locale.
	 *
	 * @param l
	 *            The new locale value
	 */
	public void setLocale(Locale l) {
		super.setLocale(l);
		dateEditor.setLocale(l);

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
	 * @param dfString
	 *            The dateFormatString to set.
	 */
	public void setDateFormatString(String dfString) {
		dateEditor.setDateFormatString(dfString);
		invalidate();
	}

	/**
	 * Returns the date. If the JDateChooser is started with a null date and no
	 * date was set by the user, null is returned.
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
	 * @param date
	 *            the new date.
	 */
	public void setDate(Date date) {
		dateEditor.setDate(date);
		if (getParent() != null) {
			getParent().invalidate();
		}
	}

	public void setMaximumValue(Date dMaximum) {
		dateEditor.setMaxSelectableDate(dMaximum);
	}

	public void setMinimumValue(Date dMinimum) {
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
		// if (calendar == null) {
		// dateEditor.setDate(null);
		// } else {
		// dateEditor.setDate(calendar.getTime());
		// }
		//
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
				dateEditor.getUiComponent().setBackground(
						HelperClient.getEditableColor());
			} else {
				dateEditor.getUiComponent().setBackground(
						HelperClient.getNotEditableColor());
			}

		}
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
		this.dateEditor.setSelectableDateRange(min, max);
	}

	public void setMaxSelectableDate(Date max) {
		this.dateEditor.setMaxSelectableDate(max);
	}

	public void setMinSelectableDate(Date min) {
		this.dateEditor.setMinSelectableDate(min);
	}

	/**
	 * Gets the maximum selectable date.
	 *
	 * @return the maximum selectable date
	 */
	public Date getMaxSelectableDate() {
		return dateEditor.getMaxSelectableDate();
	}

	/**
	 * Gets the minimum selectable date.
	 *
	 * @return the minimum selectable date
	 */
	public Date getMinSelectableDate() {
		return dateEditor.getMinSelectableDate();
	}

	/**
	 * Should only be invoked if the JDateChooser is not used anymore. Due to
	 * popup handling it had to register a change listener to the default menu
	 * selection manager which will be unregistered here. Use this method to
	 * cleanup possible memory leaks.
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
				this.dateEditor.getUiComponent().setBorder(
						Defaults.getInstance().getMandatoryFieldBorder());
			} else {
				this.dateEditor.getUiComponent().setBorder(
						new WrapperTextField().getBorder());
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
		// ist nicht relevant
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
