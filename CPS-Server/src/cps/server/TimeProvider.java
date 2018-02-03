package cps.server;

import java.time.LocalDateTime;

/** Abstracts access to current time (useful for tests). */
public interface TimeProvider {
  
  /** Return the current date-time.
   * @return the local date time */
  public LocalDateTime now();
}
