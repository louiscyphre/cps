package cps.api.response;

/** Is sent in response to a SetFullLot action. */
public class SetFullLotResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see cps.api.response.ServerResponse#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
