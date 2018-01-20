package cps.api.response;

public class ParkingExitResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private float             payment = 0f;

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
