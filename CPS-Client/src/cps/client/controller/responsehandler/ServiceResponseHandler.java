package cps.client.controller.responsehandler;

import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RejectComplaintResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.RequestReportResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.UpdatePricesResponse;

public interface ServiceResponseHandler {
  // CompanyPerson responses
  public ServerResponse handle(DisableParkingSlotsResponse response);

  public ServerResponse handle(InitLotResponse response);

  public ServerResponse handle(RefundResponse response);

  public ServerResponse handle(RequestLotStateResponse response);

  public ServerResponse handle(RequestReportResponse response);

  public ServerResponse handle(ReserveParkingSlotsResponse response);

  public ServerResponse handle(SetFullLotResponse response);
  
  public ServerResponse handle(ListParkingLotsResponse response);

  public ServerResponse handle(UpdatePricesResponse response);

  public ServerResponse handle(ServiceLoginResponse response);

  public ServerResponse handle(ListComplaintsResponse response);
  
  public ServerResponse handle(RejectComplaintResponse response);
  
}
