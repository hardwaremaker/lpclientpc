/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
 *  DateUtil.java  - Date handling utilities
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

import java.util.Calendar;
import java.util.Date;

/**
 *  A utility class for some date operations.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision: 95 $
 * @version $LastChangedDate: 2006-05-05 18:43:15 +0200 (Fr, 05 Mai 2006) $
 */
public class DateUtil {
	protected Date minSelectableDate;

	protected Date maxSelectableDate;

	protected Date defaultMinSelectableDate;

	protected Date defaultMaxSelectableDate;

	public DateUtil() {
		Calendar tmpCalendar = Calendar.getInstance();
		tmpCalendar.set(1, 0, 1, 1, 1);
		defaultMinSelectableDate = tmpCalendar.getTime();
		minSelectableDate = defaultMinSelectableDate;
		tmpCalendar.set(9999, 0, 1, 1, 1);
		defaultMaxSelectableDate = tmpCalendar.getTime();
		maxSelectableDate = defaultMaxSelectableDate;
	}

	/**
	 * Sets a valid date range for selectable dates. If max is before min, the
	 * default range with no limitation is set.
	 * 
	 * @param min
	 *            the minimum selectable date or null (then the minimum date is
	 *            set to 01\01\0001)
	 * @param max
	 *            the maximum selectable date or null (then the maximum date is
	 *            set to 01\01\9999)
	 */
	public void setSelectableDateRange(Date min, Date max) {
		if (min == null) {
			minSelectableDate = defaultMinSelectableDate;
		} else {
			minSelectableDate = min;
		}
		if (max == null) {
			maxSelectableDate = defaultMaxSelectableDate;
		} else {
			maxSelectableDate = max;
		}
		if (maxSelectableDate.before(minSelectableDate)) {
			minSelectableDate = defaultMinSelectableDate;
			maxSelectableDate = defaultMaxSelectableDate;
		}
	}

	/**
	 * Sets the maximum selectable date. If null, the date 01\01\9999 will be set instead.
	 * 
	 * @param max the maximum selectable date
	 * 
	 * @return the maximum selectable date
	 */
	public Date setMaxSelectableDate(Date max) {
		if (max == null) {
			maxSelectableDate = defaultMaxSelectableDate;
		} else {
			maxSelectableDate = max;
		}
		return maxSelectableDate;
	}

	/**
	 * Sets the minimum selectable date. If null, the date 01\01\0001 will be set instead.
	 * 
	 * @param min the minimum selectable date
	 * 
	 * @return the minimum selectable date
	 */
	public Date setMinSelectableDate(Date min) {
		if (min == null) {
			minSelectableDate = defaultMinSelectableDate;
		} else {
			minSelectableDate = min;
		}
		return minSelectableDate;
	}

	/**
	 * Gets the maximum selectable date.
	 * 
	 * @return the maximum selectable date
	 */
	public Date getMaxSelectableDate() {
		return maxSelectableDate;
	}

	/**
	 * Gets the minimum selectable date.
	 * 
	 * @return the minimum selectable date
	 */
	public Date getMinSelectableDate() {
		return minSelectableDate;
	}

	/**
	 * Checks a given date if it is in the formally specified date range.
	 * 
	 * @param date
	 *            the date to check
	 * @return true, if the date is within minSelectableDate and
	 *         maxSelectableDate
	 */
	public boolean checkDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Calendar minCal = Calendar.getInstance();
		minCal.setTime(minSelectableDate);
		minCal.set(Calendar.HOUR_OF_DAY, 0);
		minCal.set(Calendar.MINUTE, 0);
		minCal.set(Calendar.SECOND, 0);
		minCal.set(Calendar.MILLISECOND, 0);

		Calendar maxCal = Calendar.getInstance();
		maxCal.setTime(maxSelectableDate);
		maxCal.set(Calendar.HOUR_OF_DAY, 0);
		maxCal.set(Calendar.MINUTE, 0);
		maxCal.set(Calendar.SECOND, 0);
		maxCal.set(Calendar.MILLISECOND, 0);
		
		return !(calendar.before(minCal) || calendar.after(maxCal));
	}

}
