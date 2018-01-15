package cps.api.response;

import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingLot;

public class RequestLotStateResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;
  private ParkingLot        lot              = null;
  private ParkingCell[][][] content          = null;

  public ParkingLot getLot() {
    return lot;
  }

  public void setLot(ParkingLot lot) {
    this.lot = lot;
  }

  public ParkingCell[][][] getContent() {
    return content;
  }

  public void setContent(ParkingCell[][][] content) {
    this.content = content;
  }

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }
}
