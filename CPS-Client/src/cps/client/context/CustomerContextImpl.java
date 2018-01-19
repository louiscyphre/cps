package cps.client.context;

public class CustomerContextImpl implements CustomerContext {

  /**
   * 
   */
  private int     customerId;
  /**
   * 
   */
  private String  customerEmail;
  /**
   * 
   */
  private String  pendingEmail;
  /**
   * 
   */
  private boolean loggedIn;
  /**
   * 
   */
  private int     chosenLotIDforSubscription;

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#getCustomerId()
   */
  @Override
  public int getCustomerId() {
    if (isLoggedIn()) {
      return customerId;
    }
    return 0;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#setCustomerId(int)
   */
  @Override
  public void setCustomerId(int id) {
    this.customerId = id;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#getCustomerEmail()
   */
  @Override
  public String getCustomerEmail() {
    if (isLoggedIn()) {
      return customerEmail;
    }
    return null;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#getPendingEmail()
   */
  @Override
  public String getPendingEmail() {
    return pendingEmail;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#setPendingEmail(java.lang.String)
   */
  @Override
  public void setPendingEmail(String pendingEmail) {
    this.pendingEmail = pendingEmail;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#acceptPendingEmail()
   */
  @Override
  public void acceptPendingEmail() {
    this.customerEmail = pendingEmail;
    this.pendingEmail = null;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#isLoggedIn()
   */
  @Override
  public boolean isLoggedIn() {
    return this.loggedIn;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#setLoggedIn(boolean)
   */
  @Override
  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#logContextOut()
   */
  @Override
  public void logContextOut() {
    customerId = 0;
    customerEmail = null;
    pendingEmail = null;
    loggedIn = false;
  }

  /*
   * (non-Javadoc)
   * @see cps.client.context.CustomerContext#getChosenLotID()
   */
  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#getChosenLotID()
   */
  @Override
  public int getChosenLotID() {
    return chosenLotIDforSubscription;
  }

  /*
   * (non-Javadoc)
   * @see cps.client.context.CustomerContext#setChosenLotID(int)
   */
  /* (non-Javadoc)
   * @see cps.client.context.CustomerContext#setChosenLotID(int)
   */
  @Override
  public void setChosenLotID(int lotID) {
    chosenLotIDforSubscription = lotID;
  }
}
