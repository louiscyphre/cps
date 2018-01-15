package cps.api.response;

import java.util.Collection;

import cps.entities.models.Complaint;

public class ListComplaintsResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;
  Collection<Complaint> data = null;

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }

  public Collection<Complaint> getData() {
    return data;
  }

  public void setData(Collection<Complaint> data) {
    this.data = data;
  }

}
