package cps.api.action;

import cps.api.request.Request;
import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer service employee wants to see a list of customer complaints. */
public class ListComplaintsAction extends Request {
  private static final long serialVersionUID = 1L;

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
