package cps.testing.utilities;

public class ParkingLotData {
  private int lotID;
  private String streetAddress;
  private int width;
  private float price1;
  private float price2;
  private String robotIP;
  
  public ParkingLotData(int lotID, String streetAddress, int width, float price1, float price2, String robotIP) {
    this.lotID = lotID;
    this.streetAddress = streetAddress;
    this.width = width;
    this.price1 = price1;
    this.price2 = price2;
    this.robotIP = robotIP;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
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
}
