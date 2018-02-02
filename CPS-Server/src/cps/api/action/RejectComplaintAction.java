package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer service employee wants to reject a customer's complaint. */
public class RejectComplaintAction extends ServiceAction {
  private static final long serialVersionUID = 1L;
  private int               complaintID;
  private String            reason;

  public RejectComplaintAction(int userID, int complaintID, String reason) {
    super(userID);
    this.complaintID = complaintID;
    this.reason = reason;
  }

  public int getComplaintID() {
    return complaintID;
  }

  public void setComplaintID(int complaintID) {
    this.complaintID = complaintID;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
