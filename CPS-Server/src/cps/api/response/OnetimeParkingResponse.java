package cps.api.response;

public abstract class OnetimeParkingResponse extends CustomerPasswordResponse {
  private static final long serialVersionUID = 1L;

  private int   serviceID = 0;
  private float payment   = 0f;

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

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }
}
