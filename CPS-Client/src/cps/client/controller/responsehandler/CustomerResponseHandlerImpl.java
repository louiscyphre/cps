package cps.client.controller.responsehandler;

import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ComplaintResponse;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ListMyComplaintsResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.LoginResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.ReservedParkingResponse;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;

/**
 * Implementation class. Delegates to specific handler the handling.
 */
class CustomerResponseHandlerImpl implements CustomerResponseHandler {

  // handlers
  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.CancelOnetimeParkingResponse)
   */
  @Override
  public void handle(CancelOnetimeParkingResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.ComplaintResponse)
   */
  @Override
  public void handle(ComplaintResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.IncidentalParkingResponse)
   */
  @Override
  public void handle(IncidentalParkingResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.ListOnetimeEntriesResponse)
   */
  @Override
  public void handle(ListOnetimeEntriesResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.LoginResponse)
   */
  @Override
  public void handle(LoginResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.ParkingEntryResponse)
   */
  @Override
  public void handle(ParkingEntryResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.ParkingExitResponse)
   */
  @Override
  public void handle(ParkingExitResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /**
   * Using the generic ListParking response.
   */
  @Override
  public void handle(ListParkingLotsResponse response) {
    if (ControllersClientAdapter.getCurrentCtrl() instanceof ParkingLotsController) {
      ParkingLotsController ctrl = (ParkingLotsController) ControllersClientAdapter.getCurrentCtrl();
      ctrl.setParkingLots(response.getData());
      ctrl.turnProcessingStateOff();
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.ReservedParkingResponse)
   */
  @Override
  public void handle(ReservedParkingResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.RegularSubscriptionResponse)
   */
  @Override
  public void handle(RegularSubscriptionResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.FullSubscriptionResponse)
   */
  @Override
  public void handle(FullSubscriptionResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.responsehandler.CustomerResponseHandler#handle(cps.
   * api.response.FullSubscriptionResponse)
   */
  @Override
  public void handle(ListMyComplaintsResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

}
