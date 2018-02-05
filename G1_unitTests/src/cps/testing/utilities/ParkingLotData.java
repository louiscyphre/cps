package cps.testing.utilities;

public class ParkingLotData {
  public int lotID;
  public String streetAddress;
  public int width;
  public float price1;
  public float price2;
  public String robotIP;
  
  public ParkingLotData(int lotID, String streetAddress, int width, float price1, float price2, String robotIP) {
    this.lotID = lotID;
    this.streetAddress = streetAddress;
    this.width = width;
    this.price1 = price1;
    this.price2 = price2;
    this.robotIP = robotIP;
  }
}
