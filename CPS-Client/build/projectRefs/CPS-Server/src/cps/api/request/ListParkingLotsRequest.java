package cps.api.request;

import cps.api.response.ServerResponse;

public class ListParkingLotsRequest extends Request {
  private static final long serialVersionUID = 1L;

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
