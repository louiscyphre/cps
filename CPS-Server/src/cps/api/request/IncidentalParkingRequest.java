package cps.api.request;

import java.time.LocalDateTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;

/** Is sent by the client application when a customer wants to park right now, without making a reservation or buying a subscription. */
public class IncidentalParkingRequest extends OnetimeParkingRequest {
  private static final long serialVersionUID = 1L;

  public IncidentalParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedEndTime) {
    super(customerID, email, carID, lotID, plannedEndTime);
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  @Override
  public int getParkingType() {
    return Constants.PARKING_TYPE_INCIDENTAL;
  }
}
