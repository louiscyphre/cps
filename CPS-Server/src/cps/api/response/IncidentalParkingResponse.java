package cps.api.response;

/** Is sent in response to an IncidentalParking request. */
public class IncidentalParkingResponse extends OnetimeParkingResponse {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
