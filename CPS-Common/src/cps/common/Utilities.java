package cps.common;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public abstract class Utilities {
	public static int stringToInteger(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (Throwable t) {
			return defaultValue;
		}
	}
	
	public static String dateToString(LocalDateTime localDateTime) {
		return new SimpleDateFormat(Constants.DATETIME_FORMAT).format(localDateTime);
	}
}
