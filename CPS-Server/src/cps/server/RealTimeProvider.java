package cps.server;

import java.time.LocalDateTime;

/** Returns the real current time. */
public class RealTimeProvider implements TimeProvider {

  /** Return the current date-time.
   * @see cps.server.TimeProvider#now() */
  @Override
  public LocalDateTime now() {
    return LocalDateTime.now();
  }

  @Override
  public TimeProvider copy() {
    return new RealTimeProvider();
  }

}
