package cps.api.response;

/** Is sent in response to a Complaint request. */
public class ComplaintResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;
  private int               complaintID      = 0;

  public int getComplaintID() {
    return complaintID;
  }

  public void setComplaintID(int complaintID) {
    this.complaintID = complaintID;
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
