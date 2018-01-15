package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

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

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
