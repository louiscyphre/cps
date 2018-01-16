package cps.api.response;

import cps.api.request.Request;
import cps.api.request.RequestHandler;

public class ListComplaintsAction extends Request {
  private static final long serialVersionUID = 1L;

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
