package cps.common;

public abstract class Utilities {
	public static int stringToInteger(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (Throwable t) {
			return defaultValue;
		}
	}
}
