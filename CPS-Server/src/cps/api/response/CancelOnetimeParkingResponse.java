package cps.api.response;

/** Is sent in response to a CancelOnetimeParking request. */
public class CancelOnetimeParkingResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private int               onetimeServiceID = 0;
  private float             refundAmount     = 0f;

  public int getOnetimeServiceID() {
    return onetimeServiceID;
  }

  public void setOnetimeServiceID(int onetimeServiceID) {
    this.onetimeServiceID = onetimeServiceID;
  }

  public float getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(float refundAmount) {
    this.refundAmount = refundAmount;
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}