package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** A common set of methods to OnetimeService and SubscriptionService entities, that can be called without knowing what is the underlying type of the parking service.
 * This interface is needed because OnetimeService and SubscriptionService are backed by different database tables, but share a lot of common functionality. */
public interface ParkingService extends Serializable {
  public int getId();

  public int getLicenseType();

  public LocalDateTime getExitTime(LocalDate now);
  
  public boolean isParked();

  public void setParked(boolean parked);
  
  public boolean isCompleted();

  public void setCompleted(boolean completed);
  
  public boolean isCanceled();
  
  public void setCanceled(boolean canceled);
  
  public boolean isWarned();
  
  public void setWarned(boolean warned);

  /** Synchronize the underlying entity object with the database.
   * @param conn the SQL connection
   * @throws SQLException on error */
  public void update(Connection conn) throws SQLException;

  public int getParkingType();

  /** Should the service be marked as complete after the customer exits parking.
   * @param now the current date-time
   * @return true if should be marked as complete */
  public boolean shouldCompleteAfterExit(LocalDateTime now);
}
