package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class GetCurrentPerformanceAction extends ServiceAction {
  private static final long serialVersionUID = 1L;

  public GetCurrentPerformanceAction(int userID) {
    super(userID);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
