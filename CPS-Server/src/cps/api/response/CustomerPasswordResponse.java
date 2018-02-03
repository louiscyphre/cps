package cps.api.response;

import cps.entities.models.Customer;

/** Base class for responses that may contain a newly generated customer password. */
public abstract class CustomerPasswordResponse extends CustomerResponse {
  private static final long serialVersionUID = 1L;
  String                    password         = null;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setCustomerData(Customer customer) {
    setCustomerID(customer.getId());
    setPassword(customer.getPassword());
  }
}
