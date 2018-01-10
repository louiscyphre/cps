package cps.client.controller.responsehandler;

import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ComplaintResponse;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.LoginResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.ServerResponse;

class CustomerResponseHandlerImpl implements CustomerResponseHandler {

  private int customerId;
  private String email;
  
  @Override
  public void setCustomerId(int id) {
    this.customerId = id;
  }

  @Override
  public int getCustomerId() {
    return this.customerId;
  }

  @Override
  public void setCustomerEmail(String email) {
    this.email = email;
  }

  @Override
  public String getCustomerEmail() {
    return this.email;
  }
  
  // // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ \\
  // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\ // \\
  
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
  public ServerResponse handle(LoginResponse response) {
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



}
