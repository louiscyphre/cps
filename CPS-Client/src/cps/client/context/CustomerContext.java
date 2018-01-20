package cps.client.context;

/**
 *  Public interface representing Customer context.
 *  Represents the state of customer - logged or not.
 *  Possesses the following data : id, email, chosen lot id.
 */
public interface CustomerContext {
  /**
   * Gets the customer ID.
   * @return customer ID
   */
  public int getCustomerId();

  /**
   * Sets the customer ID according to the input.
   * @param id of the customer
   */
  public void setCustomerId(int id);

  /**
   * Gets the customer's email.
   * @return string representation of customer's email
   */
  public String getCustomerEmail();

  /**
   * Gets the pending email.
   * @return string representation of pending email 
   */
  public String getPendingEmail();

  /**
   * Sets the pending email.
   */
  public void setPendingEmail(String pendingEmail);

  /**
   * Pending email becomes the customer email. 
   */
  public void acceptPendingEmail();

  /**
   * Returns whether there is a logged in user at the moment.
   * @return <tt>true</tt> if there is logged in user
   */
  public boolean isLoggedIn();

  /**
   * Sets the logged in user parameter.
   * @param loggedIn - boolean indicating whether there is a logged in user.
   */
  public void setLoggedIn(boolean loggedIn);

  /**
   * Logs the whole context out.
   */
  public void logContextOut();

  /**
   * Returns the ID for currently chosen lot.
   * @return
   */
  public int getChosenLotID();

  /**
   * Sets the given lot ID to be currently chosen.
   * @param lotID
   */
  public void setChosenLotID(int lotID);
}
