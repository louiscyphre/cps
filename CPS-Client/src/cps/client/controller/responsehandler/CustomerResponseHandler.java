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

/**
 * @author firl
 *
 */
public interface CustomerResponseHandler {

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(CancelOnetimeParkingResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ComplaintResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(FullSubscriptionResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(IncidentalParkingResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ListOnetimeEntriesResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ParkingEntryResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ParkingExitResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ListParkingLotsResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(RegularSubscriptionResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ReservedParkingResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(LoginResponse response);
}
