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

  public static String randomString(String characterSet, int length) {
    StringBuilder builder = new StringBuilder();
    int setLength = characterSet.length();

    while (length-- != 0) {
      int character = (int) (Math.random() * setLength);
      builder.append(characterSet.charAt(character));
    }

    return builder.toString();
  }
}
