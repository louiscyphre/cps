package cps.api.response;

/** Is sent in response to a ParkingExit request. */
public class ParkingExitResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private float             payment = 0f;

  public float getPayment() {
    return payment;
  }

  public void setPayment(float payment) {
    this.payment = payment;
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
