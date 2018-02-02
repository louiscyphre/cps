package cps.api.request;

import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer wants to see a list of all the complaints that they have filed, and their status. */
public class ListMyComplaintsRequest extends CustomerRequest {
  private static final long serialVersionUID = 1L;

  public ListMyComplaintsRequest(int customerID) {
    super(customerID);
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}