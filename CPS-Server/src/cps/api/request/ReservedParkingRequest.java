package cps.api.request;

import java.time.LocalDateTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;

/** Is sent by the client application when a customer wants to reserve parking for some point in the future. */
public class ReservedParkingRequest extends OnetimeParkingRequest {
  private static final long serialVersionUID = 1L;
  private LocalDateTime     plannedStartTime;

  public ReservedParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedStartTime,
      LocalDateTime plannedEndTime) {
    super(customerID, email, carID, lotID, plannedEndTime);
    this.plannedStartTime = plannedStartTime;
  }

  public LocalDateTime getPlannedStartTime() {
    return plannedStartTime;
  }

  public void setPlannedStartTime(LocalDateTime plannedStartTime) {
    this.plannedStartTime = plannedStartTime;
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  @Override
  public int getParkingType() {
    return Constants.PARKING_TYPE_RESERVED;
  }

}
