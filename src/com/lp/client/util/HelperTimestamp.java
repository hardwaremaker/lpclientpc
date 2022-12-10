package com.lp.client.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

import com.lp.service.BelegDto;

public class HelperTimestamp {

	/**
	 * Die aktuelle Zeit als Timestamp
	 * 
	 * @return die aktuelle Zeit als Timestamp
	 */
	public static Timestamp current() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Der aktuelle Tag als Timestamp</br>
	 * <p>Der Tag beginnt um 00:00:00.000h
	 * @return der aktuelle Tag als Timestamp
	 */
	public static Timestamp cut() {
		return cut(current());
	}
	
	/**
	 * Timestamp zum Tagesbeginn ermitteln
	 * @param ts der Timestamp der auf den aktuellen Tag reduziert werden soll.
	 * Ist <code>ts</code> null wird eine explizite Nullpointer-Ex geliefert
	 * @return ts auf den aktuellen Tag (00:00:00.000h) bezogen
	 */
	public static Timestamp cut(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Objects.requireNonNull(ts, "Timestamp null!").getTime());
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		return new java.sql.Timestamp(cal.getTimeInMillis());		
	}
	
	/**
	 * Timestamp auf den Tagesbeginn setzen</br>
	 * <p>Ist der Timestamp null, wird "heute" verwendet</p>
	 * 
	 * @param ts wenn != null auf den Tagesbeginn setzen, ansonsten 
	 * Tagesbeginn von "heute" verwenden
	 * @return der Timestamp am Tagesbeginn
	 */
	public static Timestamp cutOrToday(Timestamp ts) {
		return ts == null ? cut() : cut(ts);
	}
	
	/**
	 * Einen Timestamp auf den Tagesbeginn aus einem Date setzen</br>
	 * <p>Ist das Date null, wird "heute" verwendet</p>
	 * @param date wenn null wird heute verwendet
	 * @return der Timestamp am Tagesbeginn
	 */
	public static Timestamp cutOrToday(Date date) {
		return date == null ? cut() : cut(new Timestamp(date.getTime()));
	}
	
	/**
	 * Das Belegdatum (eines Belegs)</br>
	 * <p>Sollte der Beleg == null sein, oder sein Belegdatum, wird
	 * das aktuelle Datum bereinigt auf den Tag verwendet</p>
	 * @param belegDto stellt das Belegdatum zur Verf&uuml;gung
	 * @return das Belegdatum (oder "heute") des Belegs
	 */
	public static Timestamp belegDatum(BelegDto belegDto) {
		if(belegDto == null || belegDto.getTBelegdatum() == null) { 
			return cut();
		} else {
			return belegDto.getTBelegdatum();
		}
	}
}
