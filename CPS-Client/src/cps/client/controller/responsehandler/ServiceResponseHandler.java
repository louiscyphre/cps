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
 * @author firl
 *
 */
public interface ServiceResponseHandler {
  // CompanyPerson responses
  /**
   * @param response
   * @return
   */
  public ServerResponse handle(DisableParkingSlotsResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(InitLotResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(RefundResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(RequestLotStateResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(RequestReportResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ReserveParkingSlotsResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(SetFullLotResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ListParkingLotsResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(UpdatePricesResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ServiceLoginResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(ListComplaintsResponse response);

  /**
   * @param response
   * @return
   */
  public ServerResponse handle(RejectComplaintResponse response);

}
