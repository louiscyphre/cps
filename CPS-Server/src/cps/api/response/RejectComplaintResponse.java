package cps.api.response;

public class RejectComplaintResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  private int    complaintID = 0;
  private int    customerID  = 0;
  private String reason      = null;

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

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
