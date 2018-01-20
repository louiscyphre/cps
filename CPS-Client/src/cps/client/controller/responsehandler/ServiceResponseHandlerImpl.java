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
import cps.client.controller.ControllersClientAdapter;

/**
 * @author firl
 *
 */
class ServiceResponseHandlerImpl implements ServiceResponseHandler {

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.DisableParkingSlotsResponse)
   */
  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.InitLotResponse)
   */
  @Override
  public ServerResponse handle(InitLotResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.RefundResponse)
   */
  @Override
  public ServerResponse handle(RefundResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.RequestLotStateResponse)
   */
  @Override
  public ServerResponse handle(RequestLotStateResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.RequestReportResponse)
   */
  @Override
  public ServerResponse handle(RequestReportResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ReserveParkingSlotsResponse)
   */
  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.SetFullLotResponse)
   */
  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ListParkingLotsResponse)
   */
  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.UpdatePricesResponse)
   */
  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ServiceLoginResponse)
   */
  @Override
  public ServerResponse handle(ServiceLoginResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.ListComplaintsResponse)
   */
  @Override
  public ServerResponse handle(ListComplaintsResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.ServiceResponseHandler#handle(cps.api.response.RejectComplaintResponse)
   */
  public ServerResponse handle(RejectComplaintResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }
}
