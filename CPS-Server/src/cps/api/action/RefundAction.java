package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer service employee wants to refund a customer that filed a complaint. */
public class RefundAction extends ServiceAction {
  private static final long serialVersionUID = 1L;
  private int               complaintID;
  private float             amount;
  private String            reason;

  public RefundAction(int userID, float amount, int complaintID) {
    super(userID);
    this.amount = amount;
    this.complaintID = complaintID;
    this.reason = null;
  }

  public RefundAction(int userID, int complaintID, float amount, String reason) {
    super(userID);
    this.complaintID = complaintID;
    this.amount = amount;
    this.reason = reason;
  }

  public RefundAction(int userID, double amount, int complaintID) {
    this(userID, (float) amount, complaintID);
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public void setAmount(double amount) {
    this.amount = (float) amount;
  }

  public int getComplaintID() {
    return complaintID;
  }

  public void setComplaintID(int complaintID) {
    this.complaintID = complaintID;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
