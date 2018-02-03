package cps.api.response;

/** Is sent in response to a ParkingCellSetDisabled action. */
public class DisableParkingSlotsResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
