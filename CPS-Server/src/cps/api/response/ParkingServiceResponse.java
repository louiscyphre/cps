package cps.api.response;

/** Base class for responses to requests for buying a parking service. */
public abstract class ParkingServiceResponse extends CustomerResponseWithLotStatus {
  private static final long serialVersionUID = 1L;
  private int               serviceID        = 0;
  private float             payment          = 0f;

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
}
