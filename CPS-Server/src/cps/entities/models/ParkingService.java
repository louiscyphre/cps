package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public interface ParkingService extends Serializable {
  public int getId();

  public int getLicenseType();

  public LocalDateTime getExitTime();
  
  public boolean isParked();

  public void setParked(boolean parked);

  public void update(Connection conn) throws SQLException;
}
