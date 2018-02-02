package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when the application needs to have access to the internal state of a parking lot. */
public class RequestLotStateAction extends LotAction {
  private static final long serialVersionUID = 1L;

  public RequestLotStateAction(int userID, int lotID) {
    super(userID, lotID);
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
