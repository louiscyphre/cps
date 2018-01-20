package cps.api.response;

public abstract class CustomerResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;
  private int               customerID       = 0;

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }
}
