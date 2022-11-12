package com.sik.ivb.utils;
/**
 * @author sik
 *
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sik.ivb.api.CalendarEvent;
import com.sik.ivb.api.RepeatType;

public class M4DateUtils {
	
	private static final Logger LOG = LogManager.getLogger(M4DateUtils.class);
	private static final DateTimeFormatter DFZ = DateTimeFormatter.ofPattern("YYYYMMDD'T'HHmmSS'Z'");
	private static final DateTimeFormatter DFT = DateTimeFormatter.ofPattern("YYYYMMDD'T'HHmmSS");
	private static final DateTimeFormatter DFS = DateTimeFormatter.ofPattern("YYYYMMDD");
	private static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
	private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final int DST_START_DAY = 85;
	private static final int DST_END_DAY = 300;
	private static final int VALID_DATE_16 = 16;
	private static final int VALID_DATE_15 = 15;
	private static final int VALID_DATE_8 = 8;
	private static final int DAY_END_HH = 23;
	private static final int DAY_END_MM = 59;
	private static final int DAY_END_SS = 59;
	private static final int DAY_END_MS = 999;
	
	private static final String EMPTY = "";
	private static final String T = "T";
	private static final String Z = "Z";
	private static final String TME_SFX = "123456";
	//private static final String YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
	//private static final String YYYYMMDD = "yyyyMMdd";
	private static final String VALUE = "VALUE=DATE:";
	private static final String TZID = "TZID=Europe/London:";
	public static final int DAY_LIMIT = 7;
	public static final int OBSOLETE_DAY_LIMIT = 2;
	public static final LocalDateTime RECENT_UPDATE_DATE = LocalDateTime.now().minusDays(DAY_LIMIT);
		
	/**
	 * Parse the input string to isolate the date string. 
	 * @param inDate
	 * @return
	 */
	public String stdDate(final String inDate) {
		String outDate = EMPTY;
		if (inDate.contains(VALUE)) {
			outDate = inDate.replace(VALUE, EMPTY);
		} else if (inDate.contains(TZID)) {
			outDate = inDate.replace(TZID, EMPTY);
		} else {
			outDate = inDate;
		}
		return outDate;
	}

	/**
	 * Roll the date by the supplied number of units.
	 * @param date
	 * @param rType
	 * @param increment
	 * @return
	 */
	public LocalDateTime dateRoll(final LocalDateTime date, final RepeatType rType,
			final int increment) {
		switch (rType) {
		case MONTHLY:
			return date.plusMonths(increment);
		case WEEKLY:
			return date.plusDays(increment * 7);
		case DAILY:
			return date.plusDays(increment);
		}

		return date.plusDays(increment);
	}

	/**
	 * Subtract 1 millisecond from the date.
	 * @param inDate
	 * @return
	 */
	public LocalDateTime subtractOneMillisecond(final LocalDateTime inDate) {
		return inDate.minusNanos(1000L);
	}

	/**
	 * Format the date.
	 * @param inDate
	 * @return
	 */
	public LocalDateTime formatDate(final String inDate) {
		LocalDateTime outDate = LocalDateTime.now();
		
		if (inDate.length() == VALID_DATE_16) {
			outDate = LocalDateTime.parse(inDate.replace(T, EMPTY).replace(Z, EMPTY), YYYYMMDDHHMMSS);
		} else if (inDate.length() == VALID_DATE_15) {
			outDate = LocalDateTime.parse(inDate.replace(T, EMPTY), YYYYMMDDHHMMSS);
		} else if (inDate.length() == VALID_DATE_8) {
			LOG.warn("Short date encountered: " + inDate + " - appending nominal time suffix: " + TME_SFX);
			outDate = LocalDateTime.parse(inDate + TME_SFX, YYYYMMDDHHMMSS);
		} else {
			throw new IllegalStateException("Unexpected date format:"
					+ inDate + " - len=" + inDate.length());
		}
		
		return outDate;
	}
	
	/**
	 * Adjust time for daylight saving time.
	 * @param date
	 * @return
	 */
	public LocalDateTime adjustForDaylightSaving(LocalDateTime date) {
		if (date.getDayOfYear() >= DST_START_DAY && date.getDayOfYear() <= DST_END_DAY) {
			return date.plusHours(1);
		} else {
			return date;
		}
	}

	/**
	 * Format the date.
	 * @param inDate
	 * @return
	 */
	public LocalDateTime formatLocalDate(final String inDate) {
		LocalDateTime outDate = LocalDateTime.now();
		
		if (inDate.length() == VALID_DATE_16) {
			outDate = LocalDateTime.parse(inDate, DFZ);
		} else if (inDate.length() == VALID_DATE_15) {
			outDate = LocalDateTime.parse(inDate, DFT);
		} else if (inDate.length() == VALID_DATE_8) {
			outDate = LocalDateTime.parse(inDate, DFS);
		} else {
			throw new IllegalStateException("Unexpected date format:"
					+ inDate + " - len=" + inDate.length());
		}
		return outDate;
	}
	
	/**
	 * Is the CalendarEvent recently updated?
	 * @param e
	 * @return
	 */
	public boolean isRecentlyUpdated(final CalendarEvent e) {
		return e.getLastUpdated().isAfter(RECENT_UPDATE_DATE); 
	}
	
	/**
	 * Is date t1 equal or after date t2?
	 * @param t1
	 * @param t2
	 * @return
	 */
	public boolean isOnAfter(final LocalDateTime t1, final LocalDateTime t2) {
		return t1.isEqual(t2) || t1.isAfter(t2);
	}

	/**
	 * Is date t1 equal or before date t2?
	 * @param t1
	 * @param t2
	 * @return
	 */
	public boolean isOnBefore(final LocalDateTime t1, final LocalDateTime t2) {
		return t1.isEqual(t2) || t1.isBefore(t2);
	}
	
	/**
	 * Is date t1 equal or before date t2?
	 * @param t1
	 * @param t2
	 * @return
	 */
	public boolean isBefore(final LocalDateTime t1, final LocalDateTime t2) {
		return t1.isBefore(t2);
	}
	
	/**
	 * Is the date recent?
	 * @param t1
	 * @param OBSOLETE_DAY_LIMIT
	 * @return
	 */
	public boolean isRecent(final LocalDateTime t1) {
		LocalDateTime recent = LocalDateTime.now().minusDays(OBSOLETE_DAY_LIMIT);
		return t1.isAfter(recent);
	}
	
	/**
	 * Set the date to the 'end' of the supplied date.
	 * @param dateTime
	 * @return
	 */
	public LocalDateTime setEndOf(LocalDateTime dateTime) {
		return LocalDateTime
				.of(dateTime.getYear(), 
					dateTime.getMonth(), 
					dateTime.getDayOfMonth(), 
					DAY_END_HH, 
					DAY_END_MM, 
					DAY_END_SS, 
					DAY_END_MS);
	}
}
