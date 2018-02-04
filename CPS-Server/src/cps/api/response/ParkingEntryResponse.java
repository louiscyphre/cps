package cps.api.response;

import java.util.Collection;

import cps.entities.models.ParkingLot;

/** Is sent in response to a ParkingEntry request. */
public class ParkingEntryResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private int               lotID            = 0;
  Collection<ParkingLot>    alternativeLots  = null;

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

  public Collection<ParkingLot> getAlternativeLots() {
    return alternativeLots;
  }

  public void setAlternativeLots(Collection<ParkingLot> alternativeLots) {
    this.alternativeLots = alternativeLots;
  }
}
