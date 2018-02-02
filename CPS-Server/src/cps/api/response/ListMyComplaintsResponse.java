package cps.api.response;

import java.util.Collection;

import cps.entities.models.Complaint;

public class ListMyComplaintsResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private Collection<Complaint> data = null;

  public Collection<Complaint> getData() {
    return data;
  }

  public void setData(Collection<Complaint> data) {
    this.data = data;
  }

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
