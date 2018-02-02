package cps.client.controller.responsehandler;

import cps.api.response.CurrentPerformanceResponse;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.PeriodicReportResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RejectComplaintResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.UpdatePricesResponse;
import cps.api.response.WeeklyReportResponse;
import cps.client.controller.ControllersClientAdapter;

/**
 * Implementation class. Delegates to specific handler the handling.
 */
class ServiceResponseHandlerImpl implements ServiceResponseHandler {

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.DisableParkingSlotsResponse)
   */
  @Override
  public void handle(DisableParkingSlotsResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.InitLotResponse)
   */
  @Override
  public void handle(InitLotResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.RefundResponse)
   */
  @Override
  public void handle(RefundResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.RequestLotStateResponse)
   */
  @Override
  public void handle(RequestLotStateResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ReserveParkingSlotsResponse)
   */
  @Override
  public void handle(ReserveParkingSlotsResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.SetFullLotResponse)
   */
  @Override
  public void handle(SetFullLotResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ListParkingLotsResponse)
   */
  @Override
  public void handle(ListParkingLotsResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.UpdatePricesResponse)
   */
  @Override
  public void handle(UpdatePricesResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ServiceLoginResponse)
   */
  @Override
  public void handle(ServiceLoginResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ListComplaintsResponse)
   */
  @Override
  public void handle(ListComplaintsResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.RejectComplaintResponse)
   */
  public void handle(RejectComplaintResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  @Override
  public void handle(WeeklyReportResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  @Override
  public void handle(QuarterlyReportResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  @Override
  public void handle(CurrentPerformanceResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  @Override
  public void handle(PeriodicReportResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }
}
