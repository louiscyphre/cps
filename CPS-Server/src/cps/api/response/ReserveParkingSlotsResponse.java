package cps.api.response;

public class ReserveParkingSlotsResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
