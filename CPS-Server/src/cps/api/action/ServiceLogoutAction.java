package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class ServiceLogoutAction extends ServiceAction {
  private static final long serialVersionUID = 1L;

  public ServiceLogoutAction(int userID) {
    super(userID);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
