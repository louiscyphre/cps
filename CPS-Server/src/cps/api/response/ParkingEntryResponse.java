package cps.api.response;

public class ParkingEntryResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private int               lotID            = 0;

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }
}
