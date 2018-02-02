package cps.api.response;

import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingLot;

/** Is sent in response to a RequestLotState action. */
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

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
