package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when a user wants to log out via the service interface (the interface for employees and the global manager). */
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
