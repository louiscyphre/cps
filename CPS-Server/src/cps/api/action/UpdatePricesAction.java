package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when a local manager wants to update the prices in a parking lot. */
public class UpdatePricesAction extends LotAction {
  private static final long serialVersionUID = 1L;
  private float             price1;
  private float             price2;

  public UpdatePricesAction(int userID, int lotID, float price1, float price2) {
    super(userID, lotID);
    this.price1 = price1;
    this.price2 = price2;
  }

  public float getPrice1() {
    return price1;
  }

  public void setPrice1(float price1) {
    this.price1 = price1;
  }

  public float getPrice2() {
    return price2;
  }

  public void setPrice2(float price2) {
    this.price2 = price2;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
