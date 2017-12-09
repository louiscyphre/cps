package cps.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Utilities {
	static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
	
	public static int stringToInteger(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (Throwable t) {
			return defaultValue;
		}
	}
	
	public static String dateToString(LocalDateTime localDateTime) {
		return localDateTime.format(dateTimeFormatter);
	}
}
