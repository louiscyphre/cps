package cps.client.controller.responsehandler;

import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ComplaintResponse;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.LoginResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.ServerResponse;

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
}
