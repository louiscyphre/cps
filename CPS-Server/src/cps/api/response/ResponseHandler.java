package cps.api.response;

public interface ResponseHandler {
  
  // Dispatch
  public ServerResponse dispatch(Response response);
  
  // Customer Responses
  public ServerResponse handle(CancelOnetimeParkingResponse response);
  public ServerResponse handle(ComplaintResponse response);
  public ServerResponse handle(FullSubscriptionResponse response);
  public ServerResponse handle(IncidentalParkingResponse response);
  public ServerResponse handle(ListOnetimeEntriesResponse response);
  public ServerResponse handle(ParkingEntryResponse response);
  public ServerResponse handle(ParkingExitResponse response);
  public ServerResponse handle(RegularSubscriptionResponse response);
  public ServerResponse handle(ReservedParkingResponse response);
  public ServerResponse handle(LoginResponse response);
  public ServerResponse handle(ServerResponse response);

  // CompanyPerson responses
  public ServerResponse handle(DisableParkingSlotsResponse response);
  public ServerResponse handle(InitLotResponse response);
  public ServerResponse handle(RefundResponse response);
  public ServerResponse handle(RequestReportResponse response);
  public ServerResponse handle(ReserveParkingSlotsResponse response);
  public ServerResponse handle(SetFullLotResponse response);
  public ServerResponse handle(UpdatePricesResponse response);
}

