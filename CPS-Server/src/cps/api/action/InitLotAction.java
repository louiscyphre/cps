package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when a local employee wants to initialize a parking lot. */
public class InitLotAction extends ServiceAction {
  private static final long serialVersionUID = 1L;

  private String streetAddress;
  private int    size;
  private float  price1;
  private float  price2;
  private String robotIP;
  
  public InitLotAction(int userID, String streetAddress, int size, float price1, float price2, String robotIP) {
    super(userID);
    this.streetAddress = streetAddress;
    this.size = size;
    this.price1 = price1;
    this.price2 = price2;
    this.robotIP = robotIP;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
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

  public String getRobotIP() {
    return robotIP;
  }

  public void setRobotIP(String robotIP) {
    this.robotIP = robotIP;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
