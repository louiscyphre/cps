package cps.api.response;

/** Used to implement dispatching and handling of responses by type in the client.
 * When the client receives a response from the server,
 * it calls the handle() method of the response object
 * and supplies an instance of a concrete implementation of this interface.
 * The concrete implementation contains code for handling each type of response
 * (such as updating UI widgets with the data from the response). */
public interface ResponseHandler {

  // Dispatch
  public void dispatch(ServerResponse response);

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
