package com.afn.realstat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class AfnDateUtil {

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date of(int year, int month, int dayOfMonth) {
		LocalDate ld = LocalDate.of(year, month, dayOfMonth);
		return AfnDateUtil.asDate(ld);
	}

	public static long yearsBetween(Date d1, Date d2) {
		long years = 0;
		LocalDate ld1 = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate ld2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		years = ChronoUnit.YEARS.between(ld1, ld2);
		return years;
	}

	public static Date dateToday() {
		LocalDate ld = LocalDate.now();
		LocalDateTime ldt = ld.atStartOfDay();
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());	    
	}

	public static Date dateYesterday() {
		LocalDate ld = LocalDate.now();
		ld.minusDays(1);
		LocalDateTime ldt = ld.atStartOfDay();
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());	
	}

	public static Date never() {
		// set to maximal MySql value
		Date never = AfnDateUtil.of(9999,1,1);
		return never;
	}
}
