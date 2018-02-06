/**
 * 
 */
package cps.client.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Nullable timestamp
 */
public class NullableTimestamp extends Timestamp {

  private static final long serialVersionUID = 1L;

  public NullableTimestamp() {
    super(0L);
  }

  /**
   * @param value
   */
  public NullableTimestamp(long value) {
    super(value);
  }

  /* (non-Javadoc)
   * @see java.sql.Timestamp#toString()
   */
  @Override
  public String toString() {
    return this.getTime() > 0L ? super.toString() : "";
  }

  /**
   * @param localDateTime
   * @return
   */
  public static NullableTimestamp valueOf(LocalDateTime localDateTime) {
    return new NullableTimestamp(Timestamp.valueOf(localDateTime).getTime());
  }
}
