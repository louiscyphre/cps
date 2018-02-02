package cps.api.response;

/** Is sent in response to a ParkingCellSetReserved action. */
public class ReserveParkingSlotsResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
