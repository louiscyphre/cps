package cps.api.response;

public class ReservedParkingResponse extends OnetimeParkingResponse {
  private static final long serialVersionUID = 1L;

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
