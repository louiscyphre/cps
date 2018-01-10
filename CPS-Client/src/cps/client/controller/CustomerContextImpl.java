package cps.client.controller;

public class CustomerContextImpl implements CustomerContext {

  private int customerId;
  private String customerEmail;
  
  @Override
  public int getCustomerId() {
    return customerId;
  }
  @Override
  public void setCustomerId(int id) {
    this.customerId = id;
  }
  @Override
  public String getCustomerEmail() {
    return customerEmail;
  }
  @Override
  public void setCustomerEmail(String email) {
    this.customerEmail = email;
  }


}
