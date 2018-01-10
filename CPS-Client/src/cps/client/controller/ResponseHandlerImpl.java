package cps.client.controller;

import cps.api.response.*;

// TODO implement the response handling at this class
public class ResponseHandlerImpl implements ResponseHandler {

  @Override
  public ServerResponse dispatch(Response resp) {
    ServerResponse response = resp.handle(this);

    if (response == null) {
      return ServerResponse.error("Not implemented");
    }

    return response;
  }

  @Override
  public ServerResponse handle(CancelOnetimeParkingResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ComplaintResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(FullSubscriptionResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(IncidentalParkingResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ListOnetimeEntriesResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ParkingExitResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ReservedParkingResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(LoginResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ServerResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(InitLotResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(RefundResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(RequestLotStateResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(RequestReportResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

}
