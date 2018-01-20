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
import cps.api.response.ServerResponse;

/**
 * Customer Response Handler Class. Used to Handle generic and specific Responses.
 */
public interface CustomerResponseHandler {

  /**
   * Handles Onetime Parking cancellation.
   * @param response
   * @return
   */
  public ServerResponse handle(CancelOnetimeParkingResponse response);

  /**
   * Handles Complaint response.
   * @param response
   * @return
   */
  public ServerResponse handle(ComplaintResponse response);

  /**
   * Handles FullSubscription response.
   * @param response
   * @return
   */
  public ServerResponse handle(FullSubscriptionResponse response);

  /**
   * Handles FullSubscription response.
   * @param response
   * @return
   */
  public ServerResponse handle(IncidentalParkingResponse response);

  /**
   * Handles ListOnetimeEntries response.
   * @param response
   * @return
   */
  public ServerResponse handle(ListOnetimeEntriesResponse response);

  /**
   * Handles ParkingEntry response.
   * @param response
   * @return
   */
  public ServerResponse handle(ParkingEntryResponse response);

  /**
   * Handles ParkingExit response.
   * @param response
   * @return
   */
  public ServerResponse handle(ParkingExitResponse response);

  /**
   * Handles ListParkingLots response.
   * @param response
   * @return
   */
  public ServerResponse handle(ListParkingLotsResponse response);

  /**
   * Handles RegularSubscription response.
   * @param response
   * @return
   */
  public ServerResponse handle(RegularSubscriptionResponse response);

  /**
   * Handles ReservedParking response.
   * @param response
   * @return
   */
  public ServerResponse handle(ReservedParkingResponse response);

  /**
   * Handles Login response.
   * @param response
   * @return
   */
  public ServerResponse handle(LoginResponse response);
  
  /**
   * Handles ListMyComplaints response.
   * @param response
   * @return
   */
  public ServerResponse handle(ListMyComplaintsResponse response);
}
