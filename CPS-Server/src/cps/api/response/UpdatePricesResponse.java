package cps.api.response;

import cps.entities.models.ParkingLot;

/** Is sent in response to an UpdatePrices action. */
public class UpdatePricesResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  private int    lotID  = 0;
  private String streetAddress;
  private float  price1 = 0f;
  private float  price2 = 0f;

  /* (non-Javadoc)
   * @see cps.api.response.ServerResponse#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
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

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public void setLotData(ParkingLot lot) {
    lotID = lot.getId();
    streetAddress = lot.getStreetAddress();
    price1 = lot.getPrice1();
    price2 = lot.getPrice2();
  }
}
