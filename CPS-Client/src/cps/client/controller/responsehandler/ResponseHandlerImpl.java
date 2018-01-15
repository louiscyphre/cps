package cps.client.controller.responsehandler;

import cps.api.response.*;
import javafx.application.Platform;
//import cps.client.controller.ControllersClientAdapter;
//import javafx.application.Platform;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;

// TODO implement the response handling at this class
public class ResponseHandlerImpl implements ResponseHandler {

  public ResponseHandlerImpl() {
    super();
    this.customerResponseHandler = new CustomerResponseHandlerImpl();
    this.serviceResponseHandler = new ServiceResponseHandlerImpl();
  }

  private ServiceResponseHandler  serviceResponseHandler;
  private CustomerResponseHandler customerResponseHandler;

  @Override
  public ServerResponse dispatch(Response resp) {
    Platform.runLater(() -> {
      resp.handle(this);
    });

    // TODO time to get rid of stupid alerts
    // if (resp instanceof ServerResponse) {
    // ServerResponse srvrResp = (ServerResponse) resp;
    // if (srvrResp.getStatus() == ServerResponse.STATUS_OK) {
    // Platform.runLater(() -> {
    // Alert alert = new Alert(AlertType.INFORMATION);
    // alert.setTitle("Success");
    // alert.setHeaderText("The operation was successful");
    // alert.setContentText(srvrResp.getDescription());
    // alert.showAndWait();
    // });
    // }
    // }

    return null;
  }

  @Override
  public ServerResponse handle(ServerResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  // // // // // // // // // // // // // // // // // // // // // // // //
  // ----- Service -----
  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(InitLotResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(RefundResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(RequestLotStateResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(RequestReportResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ServiceLoginResponse response) {
    return this.serviceResponseHandler.handle(response);
  }

  // // // // // // // // // // // // // // // // // // // // // // // //
  // ----- Customer -----
  @Override
  public ServerResponse handle(CancelOnetimeParkingResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ComplaintResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(FullSubscriptionResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(IncidentalParkingResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ListOnetimeEntriesResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(LoginResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ParkingExitResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    return this.customerResponseHandler.handle(response);
  }

  @Override
  public ServerResponse handle(ReservedParkingResponse response) {
    return this.customerResponseHandler.handle(response);
  }

}
