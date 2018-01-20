package cps.common;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    private A a;
    private B b;

    public Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }

    public A getA() {
      return a;
    }

    public void setA(A a) {
      this.a = a;
    }

    public B getB() {
      return b;
    }

    public void setB(B b) {
      this.b = b;
    }
  }

  public interface Visitor<T> {
    void apply(T argument);
  }

  public interface VisitorWithReturnType<T, R> {
    R apply(T argument);
  }

  public interface VisitorWithException<T, E extends Throwable> {
    void apply(T argument) throws E;
  }

  public interface VisitorWithExceptionAndReturnType<T, E extends Throwable, R> {
    R apply(T argument) throws E;
  }

  public static String randomString(String characterSet, int length) {
    StringBuilder builder = new StringBuilder();
    int setLength = characterSet.length();

    while (length-- != 0) {
      int character = (int) (Math.random() * setLength);
      builder.append(characterSet.charAt(character));
    }

    return builder.toString();
  }

  public static boolean isWeekend(DayOfWeek day) {
    return day == DayOfWeek.SATURDAY;
  }

  public static boolean isEmpty(String string) {
    return string == null || string.trim().isEmpty();
  }

  public static boolean between(int x, int a, int b) {
    return a <= x && x <= b;
  }

  public static boolean between(float x, float a, float b) {
    return a <= x && x <= b;
  }

  public static <T> T valueOrDefault(T value, T defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static String emptyIfNull(String value) {
    return value == null ? "" : value;
  }

  public static void debugPrintln(String message, Object... args) {
    if (Constants.DEBUG_MODE) {
      System.out.println(String.format(message, args));
    }
  }

  public static void debugPrint(String message, Object... args) {
    if (Constants.DEBUG_MODE) {
      System.out.print(String.format(message, args));
    }
  }

  public static LocalDate findWeekStart(LocalDate start) {
    while (!start.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      start = start.minusDays(1);
    }

    return start;
  }

  public static int countWeeksInMonth(int year, int month) {
    int weekCount = 0;
    LocalDate start = LocalDate.of(year, month, 1);
    LocalDate end = start.plusMonths(1);
    while (start.isBefore(end)) {
      weekCount++;
      start = start.plusDays(7);
    }
    return weekCount;
  }

}
