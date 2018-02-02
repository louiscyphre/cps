package cps.api.request;

import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer wants to file a complaint. */
public class ComplaintRequest extends CustomerRequest {
  private static final long serialVersionUID = 1L;

  private int lotID = 0;
  private String content;

  public ComplaintRequest(int customerID, String content) {
    super(customerID);
    this.content = content;
  }

  public ComplaintRequest(int customerID, int lotID, String content) {
    super(customerID);
    this.lotID = lotID;
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }
}
