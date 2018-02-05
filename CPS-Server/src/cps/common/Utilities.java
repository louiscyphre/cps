package cps.common;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;

/** Various utility methods and classes that are used throughout the application to facilitate writing common tasks. */
public abstract class Utilities {
  
  /** The date time formatter. */
  static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);

  /** Convert string to integer, or return a default value if conversion failed.
   * @param s the string
   * @param defaultValue the default value
   * @return the integer value */
  public static int stringToInteger(String s, int defaultValue) {
    try {
      return Integer.parseInt(s);
    } catch (Throwable t) {
      return defaultValue;
    }
  }

  /** Convert date to string with application specific formatting settings.
   * @param localDateTime the local date time
   * @return string representation */
  public static String dateToString(LocalDateTime localDateTime) {
    return localDateTime.format(dateTimeFormatter);
  }

  /** Used to box a value, so that it can be updated by a lambda function.
   * (Lambda functions cannot directly modify variables of the enclosing scope.)
   * @param <T> the generic type */
  public static class Holder<T> {
    private T value;

    /** Instantiates a new holder.
     * @param value the value */
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

  /** Used when a method needs to return two values.
   * @param <A> generic type A
   * @param <B> generic type B */
  public static class Pair<A, B> {

    private A a;
    private B b;

    /** Instantiates a new pair.
     * @param a the a
     * @param b the b */
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

  /** Signature of a callback function with one argument and no return values.
   * @param <T> the argument type */
  public interface Visitor<T> {
    
    /** Apply function.
     * @param argument the argument */
    void apply(T argument);
  }

  /** Signature of a callback function with one argument and a returned value.
   * @param <T> the argument type
   * @param <R> the returned type */
  public interface VisitorWithReturnType<T, R> {
    
    /** Apply function.
     * @param argument the argument
     * @return the r */
    R apply(T argument);
  }

  /** Signature of a callback function with one argument and no return values, that can throw an exception.
   * @param <T> the argument type
   * @param <E> the exception type */
  public interface VisitorWithException<T, E extends Throwable> {
    
    /** Apply function.
     * @param argument the argument
     * @throws E the e */
    void apply(T argument) throws E;
  }

  /** Signature of a callback function with one argument and a returned value, that can throw an exception.
   * @param <T> the argument type
   * @param <E> the exception type
   * @param <R> the returned type */
  public interface VisitorWithExceptionAndReturnType<T, E extends Throwable, R> {
    
    /** Apply function.
     * @param argument the argument
     * @return the r
     * @throws E the e */
    R apply(T argument) throws E;
  }

  /** Generate random string of the specified length from the specified set of characters.
   * @param characterSet the character set
   * @param length the length
   * @return the randomly generated string */
  public static String randomString(String characterSet, int length) {
    StringBuilder builder = new StringBuilder();
    int setLength = characterSet.length();

    while (length-- != 0) {
      int character = (int) (Math.random() * setLength);
      builder.append(characterSet.charAt(character));
    }

    return builder.toString();
  }

  /** Check if the day is a weekend.
   * @param day the day
   * @return true, if is weekend */
  public static boolean isWeekend(DayOfWeek day) {
    return day == DayOfWeek.SATURDAY;
  }

  /** Check if string is null or empty.
   * Strings that contain only whitespace are considered empty.
   * @param string the string
   * @return true, if is empty */
  public static boolean isEmpty(String string) {
    return string == null || string.trim().isEmpty();
  }

  /** Check if x lies in the range [a, b].
   * @param x the value to check
   * @param a the lower bound
   * @param b the higher bound
   * @return true if x is between a and b */
  public static boolean between(int x, int a, int b) {
    return a <= x && x <= b;
  }


  /** Check if x lies in the range [a, b].
   * @param x the value to check
   * @param a the lower bound
   * @param b the higher bound
   * @return true if x is between a and b */
  public static boolean between(float x, float a, float b) {
    return a <= x && x <= b;
  }

  /** Return the value if not null, or a default value otherwise.
   * @param <T> the generic type
   * @param value the value
   * @param defaultValue the default value
   * @return the value or default value */
  public static <T> T valueOrDefault(T value, T defaultValue) {
    return value == null ? defaultValue : value;
  }

  /** Return an empty string if the string reference is null, otherwise return the string itself.
   * @param value the string
   * @return the string or an empty string */
  public static String emptyIfNull(String value) {
    return value == null ? "" : value;
  }

  /** Print a formatted debug message.
   * @param message the message
   * @param args the arguments */
  public static void debugPrintln(String message, Object... args) {
    if (Constants.DEBUG_MODE) {
      System.out.println(String.format(message, args));
    }
  }

  /** Print a formatted debug message.
   * @param message the message
   * @param args the arguments */
  public static void debugPrint(String message, Object... args) {
    if (Constants.DEBUG_MODE) {
      System.out.print(String.format(message, args));
    }
  }

  /** Find the start of the week for the given date.
   * @param date the date
   * @return the date when the week started */
  public static LocalDate findWeekStart(LocalDate date) {
    while (!date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      date = date.minusDays(1);
    }

    return date;
  }

  /** Count the number of weeks in the given month.
   * @param year the year
   * @param month the month
   * @return the number of weeks */
  public static int countWeeksInMonth(int year, int month) {
    // TODO should this be aligned on whole weeks?
    return (int) Math.ceil(YearMonth.of(year, month).lengthOfMonth() / 7.f);
  }
  
  /** Get the last element of a collection.
   * @param <T> the generic type
   * @param collection the collection
   * @return the last element */
  public static <T> T getLastElement(Collection<T> collection) {
    T item = null;
    for (Iterator<T> it = collection.iterator(); it.hasNext(); item = it.next());
    return item;
  }

}
