package cps.client.controller.responsehandler;

import cps.api.response.*;

public interface ServiceResponseHandler {
  // CompanyPerson responses
  public ServerResponse handle(DisableParkingSlotsResponse response);

  public ServerResponse handle(InitLotResponse response);

  public ServerResponse handle(RefundResponse response);

  public ServerResponse handle(RequestLotStateResponse response);

  public ServerResponse handle(RequestReportResponse response);

  public ServerResponse handle(ReserveParkingSlotsResponse response);

  public ServerResponse handle(SetFullLotResponse response);

  public ServerResponse handle(UpdatePricesResponse response);

  public ServerResponse handle(ServiceLoginResponse response);
}
