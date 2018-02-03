package cps.api.response;

import java.util.Collection;


import cps.entities.models.Complaint;
/** Is sent in response to a ListMyComplaints request. */
public class ListMyComplaintsResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private Collection<Complaint> data = null;

  public Collection<Complaint> getData() {
    return data;
  }

  public void setData(Collection<Complaint> data) {
    this.data = data;
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
