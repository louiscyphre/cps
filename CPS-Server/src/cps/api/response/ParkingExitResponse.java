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
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
