package cps.api.response;

import java.util.Collection;

import cps.entities.models.ParkingLot;

public class ListParkingLotsResponse extends ServerResponse {
  private static final long      serialVersionUID = 1L;
  private Collection<ParkingLot> data             = null;

  public Collection<ParkingLot> getData() {
    return data;
  }

  public void setData(Collection<ParkingLot> data) {
    this.data = data;
  }

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
