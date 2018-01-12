package cps.client.context;

public class CustomerContextImpl implements CustomerContext {

  private int    customerId;
  private String customerEmail;
  private String pendingEmail;

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

}
