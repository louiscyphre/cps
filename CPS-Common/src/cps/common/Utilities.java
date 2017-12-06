package cps.common;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Utilities {
	public static int stringToInteger(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (Throwable t) {
			return defaultValue;
		}
	}
	
	public static String dateToString(Date date) {
		return new SimpleDateFormat(Constants.DATETIME_FORMAT).format(date);
	}
}
