package cps.api.response;

public class SubscriptionResponse extends CustomerPasswordResponse {
  private static final long serialVersionUID = 1L;
  private int               serviceID        = 0;
  private float             payment          = 0f;

  public int getServiceID() {
    return serviceID;
  }

  public float getPayment() {
    return payment;
  }

  public void setPayment(float payment) {
    this.payment = payment;
  }

  public void setServiceID(int serviceID) {
    this.serviceID = serviceID;
  }

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }
}
