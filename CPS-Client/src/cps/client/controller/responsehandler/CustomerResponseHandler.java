package cps.client.controller.responsehandler;

import cps.api.response.*;

public interface CustomerResponseHandler {

  public ServerResponse handle(CancelOnetimeParkingResponse response);

  public ServerResponse handle(ComplaintResponse response);

  public ServerResponse handle(FullSubscriptionResponse response);

  public ServerResponse handle(IncidentalParkingResponse response);

  public ServerResponse handle(ListOnetimeEntriesResponse response);

  public ServerResponse handle(ParkingEntryResponse response);

  public ServerResponse handle(ParkingExitResponse response);
  
  public ServerResponse handle(ListParkingLotsResponse response);
  
  public ServerResponse handle(RegularSubscriptionResponse response);

  public ServerResponse handle(ReservedParkingResponse response);

  public ServerResponse handle(LoginResponse response);

  public ServerResponse handle(ServerResponse response);
}
