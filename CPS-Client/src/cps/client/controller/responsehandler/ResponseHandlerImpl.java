package cps.client.controller.responsehandler;

import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ComplaintResponse;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ListMyComplaintsResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.LoginResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.RejectComplaintResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.Response;
import cps.api.response.ResponseHandler;
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.SimpleResponse;
import cps.api.response.UpdatePricesResponse;
import cps.api.response.WeeklyReportResponse;
import javafx.application.Platform;
//import cps.client.controller.ControllersClientAdapter;
//import javafx.application.Platform;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;

/**
 * @author firl
 *
 */
public class ResponseHandlerImpl implements ResponseHandler {

  /**
   * 
   */
  public ResponseHandlerImpl() {
    super();
    this.customerResponseHandler = new CustomerResponseHandlerImpl();
    this.serviceResponseHandler = new ServiceResponseHandlerImpl();
  }

  /**
   * 
   */
  private ServiceResponseHandler  serviceResponseHandler;
  /**
   * 
   */
  private CustomerResponseHandler customerResponseHandler;

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#dispatch(cps.api.response.Response)
   */
  @Override
  public ServerResponse dispatch(Response resp) {
    Platform.runLater(() -> {
      resp.handle(this);
    });
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.SimpleResponse)
   */
  @Override
  public ServerResponse handle(SimpleResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  // // // // // // // // // // // // // // // // // // // // // // // //
  // ----- Service -----
  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.DisableParkingSlotsResponse)
   */
  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.InitLotResponse)
   */
  @Override
  public ServerResponse handle(InitLotResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RefundResponse)
   */
  @Override
  public ServerResponse handle(RefundResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RequestLotStateResponse)
   */
  @Override
  public ServerResponse handle(RequestLotStateResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ReserveParkingSlotsResponse)
   */
  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.SetFullLotResponse)
   */
  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.UpdatePricesResponse)
   */
  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ServiceLoginResponse)
   */
  @Override
  public ServerResponse handle(ServiceLoginResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RejectComplaintResponse)
   */
  @Override
  public ServerResponse handle(RejectComplaintResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  // // // // // // // // // // // // // // // // // // // // // // // //
  // ----- Customer -----
  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.CancelOnetimeParkingResponse)
   */
  @Override
  public ServerResponse handle(CancelOnetimeParkingResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ComplaintResponse)
   */
  @Override
  public ServerResponse handle(ComplaintResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.FullSubscriptionResponse)
   */
  @Override
  public ServerResponse handle(FullSubscriptionResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.IncidentalParkingResponse)
   */
  @Override
  public ServerResponse handle(IncidentalParkingResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListOnetimeEntriesResponse)
   */
  @Override
  public ServerResponse handle(ListOnetimeEntriesResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListParkingLotsResponse)
   */
  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.LoginResponse)
   */
  @Override
  public ServerResponse handle(LoginResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ParkingEntryResponse)
   */
  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ParkingExitResponse)
   */
  @Override
  public ServerResponse handle(ParkingExitResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RegularSubscriptionResponse)
   */
  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ReservedParkingResponse)
   */
  @Override
  public ServerResponse handle(ReservedParkingResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListComplaintsResponse)
   */
  @Override
  public ServerResponse handle(ListComplaintsResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListMyComplaintsResponse)
   */
  @Override
  public ServerResponse handle(ListMyComplaintsResponse response) {
    return null;
  }


  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.WeeklyReportResponse)
   */
  @Override
  public ServerResponse handle(WeeklyReportResponse response) {
    return null;
  }


  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.QuarterlyReportResponse)
   */
  @Override
  public ServerResponse handle(QuarterlyReportResponse response) {
    return null;
  }

}
