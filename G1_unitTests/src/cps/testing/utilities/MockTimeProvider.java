package cps.testing.utilities;

import java.time.LocalDateTime;

import cps.server.TimeProvider;

/** Returns fake current time that can be modified at will. */
public class MockTimeProvider implements TimeProvider {
  private LocalDateTime value;
  
  /** Instantiates a new mock time provider. */
  public MockTimeProvider() {
    this.value = LocalDateTime.now().withNano(0); 
  }
  
  /** Instantiates a new mock time provider.
   * @param value the value */
  public MockTimeProvider(LocalDateTime value) {
    this.value = value; 
  }

  /** Get the current date-time.
   * @see cps.server.TimeProvider#now()
   */
  @Override
  public LocalDateTime now() {
    return value;
  }

  /** Set the current date-time.
   * @param value the value */
  public void set(LocalDateTime value) {
    this.value = value;
  }

  @Override
  public TimeProvider copy() {
    return new MockTimeProvider(value);
  }

}
