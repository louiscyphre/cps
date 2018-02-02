package cps.api.request;

/** Base class for client requests that represent customer actions. */
public abstract class CustomerRequest extends Request {
  private static final long serialVersionUID = 1L;

  private int customerID;

  public CustomerRequest(int customerID) {
    this.customerID = customerID;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

}
