package cps.entities.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface ParkingService extends Serializable {
  public int getId();

  public int getLicenseType();

  public LocalDateTime getExitTime();
}
