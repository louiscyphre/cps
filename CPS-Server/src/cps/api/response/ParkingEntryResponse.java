package cps.api.response;

import cps.api.request.ParkingEntryRequest;

public class ParkingEntryResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  private int               lotID;

  public ParkingEntryResponse(boolean success, String description, int customerID, int lotID) {
    super(success, description, customerID);
    this.lotID = lotID;
  }

  public ParkingEntryResponse(boolean success, String description, ParkingEntryRequest request) {
    super(success, description, request.getCustomerID());
    this.lotID = request.getLotID();
  }

  public ParkingEntryResponse(boolean success, String description) {
    this(success, description, 0, 0);
  }

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
