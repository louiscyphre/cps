package cps.client.context;

public class CustomerContextImpl implements CustomerContext {

  private int     customerId;
  private String  customerEmail;
  private String  pendingEmail;
  private boolean loggedIn;

  @Override
  public int getCustomerId() {
    if (isLoggedIn()) {
      return customerId;
    }
    return 0;
  }

  @Override
  public void setCustomerId(int id) {
    this.customerId = id;
  }

  @Override
  public String getCustomerEmail() {
    if (isLoggedIn()) {
      return customerEmail;
    }
    return null;
  }

  @Override
  public String getPendingEmail() {
    return pendingEmail;
  }

  @Override
  public void setPendingEmail(String pendingEmail) {
    this.pendingEmail = pendingEmail;
  }

  @Override
  public void acceptPendingEmail() {
    this.customerEmail = pendingEmail;
    this.pendingEmail = null;
  }

  @Override
  public boolean isLoggedIn() {
    return this.loggedIn;
  }

  @Override
  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  @Override
  public void logContextOut() {
    customerId = 0;
    customerEmail = null;
    pendingEmail = null;
    loggedIn = false;
  }
}
