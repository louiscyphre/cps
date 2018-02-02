package cps.api.request;

import cps.api.response.ServerResponse;

/** Is sent by the client application when the application needs to display a list of parking lots to the user.
 * Is used to populate drop-down menus for parking lot selection. */
public class ListParkingLotsRequest extends Request {
  private static final long serialVersionUID = 1L;

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
