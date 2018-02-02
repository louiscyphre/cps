package cps.api.response;

public class InitLotResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;
  private int               lotID            = 0;

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
