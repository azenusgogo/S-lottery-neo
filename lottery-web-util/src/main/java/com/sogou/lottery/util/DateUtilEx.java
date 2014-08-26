package com.sogou.lottery.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtilEx {

	/**
	 * Calculates the number of days between two calendar days in a manner which
	 * is independent of the Calendar type used.
	 * 
	 * @param d1
	 *            The first date.
	 * @param d2
	 *            The second date.
	 * 
	 * @return The number of days between the two dates. Zero is returned if the
	 *         dates are the same, one if the dates are adjacent, etc. The order
	 *         of the dates does not matter, the value returned is always >= 0.
	 *         If Calendar types of d1 and d2 are different, the result may not
	 *         be accurate.
	 * @throws NullPointException
	 */
	public static int getDaysBetween(Calendar c1, Calendar c2) throws NullPointerException {
		int days = -1;
		if (c1.after(c2)) { // swap dates so that c1 is start and c2 is end
			Calendar swap = c1;
			c1 = c2;
			c2 = swap;
		}
		days = c2.get(Calendar.DAY_OF_YEAR)	- c1.get(Calendar.DAY_OF_YEAR);
		int y2 = c2.get(Calendar.YEAR);
		while (c1.get(Calendar.YEAR) != y2) {
			days += c1.getActualMaximum(Calendar.DAY_OF_YEAR);
			c1.add(Calendar.YEAR, 1);
		}
		return days;
	}
	
	/**
	 * Calculates the number of days between two date days in a manner which
	 * is independent of the Calendar type used.
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDaysBetween(Date d1, Date d2){
		try {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(d1);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(d2);
			
			return getDaysBetween(c1, c2);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	public static Timestamp getBeginOfDay(Date d) {
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = form.format(d.getTime()) + " 00:00:00";
		Date date = null;
		try {
			date = form.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(date.getTime());
	}
	
	public static Timestamp getEndOfDay(Date d) {
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = form.format(d.getTime()) + " 23:59:59";
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(date.getTime());
	}
}
