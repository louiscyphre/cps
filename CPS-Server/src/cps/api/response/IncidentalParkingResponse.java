package cps.api.response;

public class IncidentalParkingResponse extends OnetimeParkingResponse {
  private static final long serialVersionUID = 1L;

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }
}
