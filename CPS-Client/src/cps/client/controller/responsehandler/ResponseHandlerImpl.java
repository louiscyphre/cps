package cps.client.controller.responsehandler;

import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ComplaintResponse;
import cps.api.response.CurrentPerformanceResponse;
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
import cps.api.response.PeriodicReportResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.RejectComplaintResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.Response;
import cps.api.response.ResponseHandler;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.SimpleResponse;
import cps.api.response.UpdatePricesResponse;
import cps.api.response.WeeklyReportResponse;
import cps.client.controller.ControllersClientAdapter;
import javafx.application.Platform;

/**
 * Client-side Implementation of Response Handler defined in API.
 */
public class ResponseHandlerImpl implements ResponseHandler {

  /**
   * Constructor, instantiating 2 contexts - Employee and Customer.
   */
  public ResponseHandlerImpl() {
    super();
    this.customerResponseHandler = new CustomerResponseHandlerImpl();
    this.serviceResponseHandler = new ServiceResponseHandlerImpl();
  }

  /** Service Response Handler */
  private ServiceResponseHandler  serviceResponseHandler;
  /** Customer Response Handler */
  private CustomerResponseHandler customerResponseHandler;

  /** Find the appropriate handler for the response and run it.
   * @param resp the response
   * @see cps.api.response.ResponseHandler#dispatch(cps.api.response.Response) */
  @Override
  public void dispatch(Response resp) {
    Platform.runLater(() -> {
      resp.handle(this);
    });
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.api.response.ResponseHandler#handle(cps.api.response.SimpleResponse)
   */
  @Override
  public void handle(SimpleResponse response) {
    ControllersClientAdapter.getCurrentCtrl().handle(response);
  }

  // // // // // // // // // // // // // // // // // // // // // // // //
  // ----- Service -----
  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * DisableParkingSlotsResponse)
   */
  @Override
  public void handle(DisableParkingSlotsResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.api.response.ResponseHandler#handle(cps.api.response.InitLotResponse)
   */
  @Override
  public void handle(InitLotResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.api.response.ResponseHandler#handle(cps.api.response.RefundResponse)
   */
  @Override
  public void handle(RefundResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * RequestLotStateResponse)
   */
  @Override
  public void handle(RequestLotStateResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ReserveParkingSlotsResponse)
   */
  @Override
  public void handle(ReserveParkingSlotsResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * SetFullLotResponse)
   */
  @Override
  public void handle(SetFullLotResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * UpdatePricesResponse)
   */
  @Override
  public void handle(UpdatePricesResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ServiceLoginResponse)
   */
  @Override
  public void handle(ServiceLoginResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * RejectComplaintResponse)
   */
  @Override
  public void handle(RejectComplaintResponse response) {
    this.serviceResponseHandler.handle(response);
  }
  
  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ListComplaintsResponse)
   */
  @Override
  public void handle(ListComplaintsResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.WeeklyReportResponse)
   */
  @Override
  public void handle(WeeklyReportResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.QuarterlyReportResponse)
   */
  @Override
  public void handle(QuarterlyReportResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  @Override
  public void handle(CurrentPerformanceResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  @Override
  public void handle(PeriodicReportResponse response) {
    this.serviceResponseHandler.handle(response);
  }

  // // // // // // // // // // // // // // // // // // // // // // // //
  // ----- Customer -----
  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * CancelOnetimeParkingResponse)
   */
  @Override
  public void handle(CancelOnetimeParkingResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.api.response.ResponseHandler#handle(cps.api.response.ComplaintResponse)
   */
  @Override
  public void handle(ComplaintResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * FullSubscriptionResponse)
   */
  @Override
  public void handle(FullSubscriptionResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * IncidentalParkingResponse)
   */
  @Override
  public void handle(IncidentalParkingResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ListOnetimeEntriesResponse)
   */
  @Override
  public void handle(ListOnetimeEntriesResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ListParkingLotsResponse)
   */
  @Override
  public void handle(ListParkingLotsResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.api.response.ResponseHandler#handle(cps.api.response.LoginResponse)
   */
  @Override
  public void handle(LoginResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ParkingEntryResponse)
   */
  @Override
  public void handle(ParkingEntryResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ParkingExitResponse)
   */
  @Override
  public void handle(ParkingExitResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * RegularSubscriptionResponse)
   */
  @Override
  public void handle(RegularSubscriptionResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ReservedParkingResponse)
   */
  @Override
  public void handle(ReservedParkingResponse response) {
    this.customerResponseHandler.handle(response);
  }

  /*
   * (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.
   * ListMyComplaintsResponse)
   */
  @Override
  public void handle(ListMyComplaintsResponse response) {
    this.customerResponseHandler.handle(response);
  }
}
