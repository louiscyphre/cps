package cps.api.request;

import cps.api.response.ServerResponse;

public class ListMyComplaintsRequest extends CustomerRequest {
  private static final long serialVersionUID = 1L;

  public ListMyComplaintsRequest(int customerID) {
    super(customerID);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}