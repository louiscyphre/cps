package cps.client.controller.responsehandler;

import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.RequestReportResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.UpdatePricesResponse;
import cps.client.controller.ControllersClientAdapter;

class ServiceResponseHandlerImpl implements ServiceResponseHandler {

  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(InitLotResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  @Override
  public ServerResponse handle(RefundResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  @Override
  public ServerResponse handle(RequestLotStateResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(RequestReportResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ServiceLoginResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  @Override
  public ServerResponse handle(ListComplaintsResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

}
