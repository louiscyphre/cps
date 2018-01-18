package cps.api.response;

import java.util.Collection;

import cps.entities.models.ParkingLot;

public abstract class ParkingServiceResponse extends CustomerPasswordResponse {
  private static final long serialVersionUID = 1L;
  private int               serviceID        = 0;
  private float             payment          = 0f;
  Collection<ParkingLot>    alternativeLots  = null;

  public int getServiceID() {
    return serviceID;
  }

  public void setServiceID(int serviceID) {
    this.serviceID = serviceID;
  }

  public float getPayment() {
    return payment;
  }

  public void setPayment(float payment) {
    this.payment = payment;
  }

  public Collection<ParkingLot> getAlternativeLots() {
    return alternativeLots;
  }

  public void setAlternativeLots(Collection<ParkingLot> alternativeLots) {
    this.alternativeLots = alternativeLots;
  }
}
