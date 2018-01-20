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
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;

/**
 * @author firl
 *
 */
class CustomerResponseHandlerImpl implements CustomerResponseHandler {

  // handlers
  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.CancelOnetimeParkingResponse)
   */
  @Override
  public ServerResponse handle(CancelOnetimeParkingResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.ComplaintResponse)
   */
  @Override
  public ServerResponse handle(ComplaintResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.IncidentalParkingResponse)
   */
  @Override
  public ServerResponse handle(IncidentalParkingResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.ListOnetimeEntriesResponse)
   */
  @Override
  public ServerResponse handle(ListOnetimeEntriesResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.LoginResponse)
   */
  @Override
  public ServerResponse handle(LoginResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.ParkingEntryResponse)
   */
  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.ParkingExitResponse)
   */
  @Override
  public ServerResponse handle(ParkingExitResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.ListParkingLotsResponse)
   */
  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    if (ControllersClientAdapter.getCurrentCtrl() instanceof ParkingLotsController) {
      ParkingLotsController ctrl = (ParkingLotsController) ControllersClientAdapter.getCurrentCtrl();
      ctrl.setParkingLots(response.getData());
      ctrl.turnProcessingStateOff();
    }
    return response;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.ReservedParkingResponse)
   */
  @Override
  public ServerResponse handle(ReservedParkingResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.RegularSubscriptionResponse)
   */
  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.api.response.FullSubscriptionResponse)
   */
  @Override
  public ServerResponse handle(FullSubscriptionResponse response) {
    return ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

}
