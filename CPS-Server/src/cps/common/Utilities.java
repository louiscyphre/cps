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

	public static class Holder<T> {
		private T value;

		public Holder(T value) {
			setValue(value);
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
	}
	
	public static class Pair<A, B> {

	    public final A a;
	    public final B b;

	    public Pair(A a, B b) {
	        this.a = a;
	        this.b = b;
	    }
	}
}
