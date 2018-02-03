package cps.api.response;

import java.util.Collection;

import cps.entities.models.Complaint;

/** Is sent in response to a ListComplaints action. */
public class ListComplaintsResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;
  Collection<Complaint> data = null;

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

  public Collection<Complaint> getData() {
    return data;
  }

  public void setData(Collection<Complaint> data) {
    this.data = data;
  }

}
