package cps.api.response;

public interface ResponseHandler {

  // Dispatch
  public void dispatch(Response response);

  // Customer Responses
  public void handle(CancelOnetimeParkingResponse response);

  public void handle(ComplaintResponse response);

  public void handle(FullSubscriptionResponse response);

  public void handle(IncidentalParkingResponse response);
  
  public void handle(ListMyComplaintsResponse response);

  public void handle(ListOnetimeEntriesResponse response);  

  public void handle(ListParkingLotsResponse response);

  public void handle(ParkingEntryResponse response);

  public void handle(ParkingExitResponse response);

  public void handle(RegularSubscriptionResponse response);

  public void handle(ReservedParkingResponse response);

  public void handle(LoginResponse response);

  // Common responses - can be sent to Customers and Employees
  public void handle(SimpleResponse simpleResponse);

  // CompanyPerson responses
  public void handle(DisableParkingSlotsResponse response);

  public void handle(InitLotResponse response);
  
  public void handle(ListComplaintsResponse response);

  public void handle(RefundResponse response);
  
  public void handle(RejectComplaintResponse response);

  public void handle(ReserveParkingSlotsResponse response);

  public void handle(SetFullLotResponse response);

  public void handle(UpdatePricesResponse response);

  public void handle(RequestLotStateResponse response);
  
  public void handle(ServiceLoginResponse response);

  public void handle(WeeklyReportResponse response);

  public void handle(QuarterlyReportResponse response);

  public void handle(CurrentPerformanceResponse response);

  public void handle(PeriodicReportResponse response);
}
