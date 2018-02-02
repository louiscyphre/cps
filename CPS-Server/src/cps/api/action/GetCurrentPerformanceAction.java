package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when the Global Manager wants to see current performance statistics. */
public class GetCurrentPerformanceAction extends ServiceAction {
  private static final long serialVersionUID = 1L;

  public GetCurrentPerformanceAction(int userID) {
    super(userID);
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
