package cps.api.response;

/** Is sent in response to a Refund action. */
public class RefundResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  private int   complaintID;
  private int   customerID;
  private float amount;

  public RefundResponse(boolean success, String description, int complaintID, int customerID, float amount) {
    super(success, description);
    this.complaintID = complaintID;
    this.customerID = customerID;
    this.amount = amount;
  }

  public RefundResponse() {
    this(false, "", 0, 0, 0f);
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

  public int getComplaintID() {
    return complaintID;
  }

  public void setComplaintID(int complaintID) {
    this.complaintID = complaintID;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }
}
