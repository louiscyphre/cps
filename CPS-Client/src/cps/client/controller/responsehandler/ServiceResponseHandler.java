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

/**
 * Service Response Handler Class. Used to Handle generic and specific Responses.
 */
public interface ServiceResponseHandler {
  /**
   * Handles DisableParkingSlots Response.
   * @param response
   * @return
   */
  public ServerResponse handle(DisableParkingSlotsResponse response);

  /**
   * Handles InitLot Response.
   * @param response
   * @return
   */
  public ServerResponse handle(InitLotResponse response);

  /**
   * Handles Refund Response.
   * @param response
   * @return
   */
  public ServerResponse handle(RefundResponse response);

  /**
   * Handles RequestLotState Response.
   * @param response
   * @return
   */
  public ServerResponse handle(RequestLotStateResponse response);

  /**
   * Handles RequestReport Response.
   * @param response
   * @return
   */
  public ServerResponse handle(RequestReportResponse response);

  /**
   * Handles ReserveParkingSlots Response.
   * @param response
   * @return
   */
  public ServerResponse handle(ReserveParkingSlotsResponse response);

  /**
   * Handles SetFullLot Response.
   * @param response
   * @return
   */
  public ServerResponse handle(SetFullLotResponse response);

  /**
   * Handles ListParkingLots Response.
   * @param response
   * @return
   */
  public ServerResponse handle(ListParkingLotsResponse response);

  /**
   * Handles UpdatePrices Response.
   * @param response
   * @return
   */
  public ServerResponse handle(UpdatePricesResponse response);

  /**
   * Handles ServiceLogin Response.
   * @param response
   * @return
   */
  public ServerResponse handle(ServiceLoginResponse response);

  /**
   * Handles ListComplaints Response.
   * @param response
   * @return
   */
  public ServerResponse handle(ListComplaintsResponse response);

  /**
   * Handles RejectComplaint Response.
   * @param response
   * @return
   */
  public ServerResponse handle(RejectComplaintResponse response);

}
