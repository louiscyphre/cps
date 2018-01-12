package cps.api.response;

import java.util.Collection;

import cps.entities.models.ParkingLot;

public class ListParkingLotsResponse extends ServerResponse {
  private static final long      serialVersionUID = 1L;
  private Collection<ParkingLot> data;

  public ListParkingLotsResponse(boolean success, String description, Collection<ParkingLot> data) {
    super(success, description);
    this.data = data;
  }

  public ListParkingLotsResponse(Collection<ParkingLot> data) {
    super(true, "ListParkingLotsRequest completed successfully");
    this.data = data;
  }

  public Collection<ParkingLot> getData() {
    return data;
  }

  public void setData(Collection<ParkingLot> data) {
    this.data = data;
  }
}
