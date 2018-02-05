package cps.api.response;

import java.util.Collection;

import cps.entities.models.ParkingLot;

public abstract class CustomerResponseWithLotStatus extends CustomerPasswordResponse {
  private static final long serialVersionUID = 1L;
  private int               lotID            = 0;
  boolean                   lotFull        = false;
  Collection<ParkingLot>    alternativeLots  = null;

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public boolean isLotFull() {
    return lotFull;
  }

  public void setLotFull(boolean lotIsFull) {
    this.lotFull = lotIsFull;
  }

  public Collection<ParkingLot> getAlternativeLots() {
    return alternativeLots;
  }

  public void setAlternativeLots(Collection<ParkingLot> alternativeLots) {
    this.alternativeLots = alternativeLots;
  }
}
