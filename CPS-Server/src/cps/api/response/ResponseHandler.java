package cps.api.response;

public interface ResponseHandler {

  // Dispatch
  public ServerResponse dispatch(Response response);

  // Customer Responses
  public ServerResponse handle(CancelOnetimeParkingResponse response);

  public ServerResponse handle(ComplaintResponse response);

  public ServerResponse handle(FullSubscriptionResponse response);

  public ServerResponse handle(IncidentalParkingResponse response);
  
  public ServerResponse handle(ListMyComplaintsResponse response);

  public ServerResponse handle(ListOnetimeEntriesResponse response);  

  public ServerResponse handle(ListParkingLotsResponse response);

  public ServerResponse handle(ParkingEntryResponse response);

  public ServerResponse handle(ParkingExitResponse response);

  public ServerResponse handle(RegularSubscriptionResponse response);

  public ServerResponse handle(ReservedParkingResponse response);

  public ServerResponse handle(LoginResponse response);

  // Common responses - can be sent to Customers and Employees
  public ServerResponse handle(SimpleResponse simpleResponse);

  // CompanyPerson responses
  public ServerResponse handle(DisableParkingSlotsResponse response);

  public ServerResponse handle(InitLotResponse response);
  
  public ServerResponse handle(ListComplaintsResponse response);

  public ServerResponse handle(RefundResponse response);
  
  public ServerResponse handle(RejectComplaintResponse response);

  public ServerResponse handle(ReserveParkingSlotsResponse response);

  public ServerResponse handle(SetFullLotResponse response);

  public ServerResponse handle(UpdatePricesResponse response);

  public ServerResponse handle(RequestLotStateResponse response);
  
  public ServerResponse handle(ServiceLoginResponse response);

  public ServerResponse handle(WeeklyReportResponse response);

  public ServerResponse handle(QuarterlyReportResponse response);

  public ServerResponse handle(CurrentPerformanceResponse response);

  public ServerResponse handle(PeriodicReportResponse response);
}
