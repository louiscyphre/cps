package cps.api.request;

import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer wants to retrieve their car from the parking lot. */
public class ParkingExitRequest extends CustomerRequest {
  private static final long serialVersionUID = 1L;

  private int    lotID;
  private String carID;

  public ParkingExitRequest(int customerID, int lotID, String carID) {
    super(customerID);
    this.lotID = lotID;
    this.carID = carID;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public String getCarID() {
    return carID;
  }

  public void setCarID(String carID) {
    this.carID = carID;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
