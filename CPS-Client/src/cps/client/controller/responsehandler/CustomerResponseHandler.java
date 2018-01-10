package cps.client.controller.responsehandler;

import cps.api.response.*;

public interface CustomerResponseHandler {
  
  public void setCustomerId(int id);
  public int getCustomerId();
  
  public void setCustomerEmail(String email);
  public String getCustomerEmail();
  
  public ServerResponse handle(CancelOnetimeParkingResponse response);
  public ServerResponse handle(ComplaintResponse response);
  public ServerResponse handle(FullSubscriptionResponse response);
  public ServerResponse handle(IncidentalParkingResponse response);
  public ServerResponse handle(ListOnetimeEntriesResponse response);
  public ServerResponse handle(LoginResponse response);
  public ServerResponse handle(ParkingEntryResponse response);
  public ServerResponse handle(ParkingExitResponse response);
}
