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
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.UpdatePricesResponse;
import cps.api.response.WeeklyReportResponse;

// TODO: Auto-generated Javadoc
/**
 * Service Response Handler Class. Used to Handle generic and specific Responses.
 */
public interface ServiceResponseHandler {
  
  /** Handles DisableParkingSlots Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(DisableParkingSlotsResponse response);

  /** Handles InitLot Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(InitLotResponse response);

  /** Handles Refund Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(RefundResponse response);

  /** Handles RequestLotState Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(RequestLotStateResponse response);

  /** Handles ReserveParkingSlots Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(ReserveParkingSlotsResponse response);

  /** Handles SetFullLot Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(SetFullLotResponse response);

  /** Handles ListParkingLots Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(ListParkingLotsResponse response);

  /** Handles UpdatePrices Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(UpdatePricesResponse response);

  /** Handles ServiceLogin Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(ServiceLoginResponse response);

  /** Handles ListComplaints Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(ListComplaintsResponse response);

  /** Handles RejectComplaint Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(RejectComplaintResponse response);

  /** Handles WeeklyReport Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(WeeklyReportResponse response);

  /** Handles QuarterlyReport Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(QuarterlyReportResponse response);

  /** Handles CurrentPerformance Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(CurrentPerformanceResponse response);

  /** Handle PeriodicReport Response.
   * @param response the response
   * @return the server response */
  public ServerResponse handle(PeriodicReportResponse response);

}
