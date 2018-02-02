package cps.api.request;

import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer wants to cancel their parking reservation. */
public class CancelOnetimeParkingRequest extends CustomerRequest {
  public CancelOnetimeParkingRequest(int customerID, int onetimeServiceID) {
    super(customerID);
    this.onetimeServiceID = onetimeServiceID;
  }

  private static final long serialVersionUID = 1L;
  private int               onetimeServiceID;

  public int getOnetimeServiceID() {
    return onetimeServiceID;
  }

  public void setOnetimeServiceID(int onetimeServiceID) {
    this.onetimeServiceID = onetimeServiceID;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
