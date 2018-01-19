package cps.client.context;

/**
 * @author firl
 *
 */
public interface CustomerContext {
  /**
   * @return
   */
  public int getCustomerId();

  /**
   * @param id
   */
  public void setCustomerId(int id);

  /**
   * @return
   */
  public String getCustomerEmail();

  /**
   * @return
   */
  public String getPendingEmail();

  /**
   * @param pendingEmail
   */
  public void setPendingEmail(String pendingEmail);

  /**
   * 
   */
  public void acceptPendingEmail();

  /**
   * @return
   */
  public boolean isLoggedIn();

  /**
   * @param loggedIn
   */
  public void setLoggedIn(boolean loggedIn);

  /**
   * 
   */
  public void logContextOut();

  /**
   * @return
   */
  public int getChosenLotID();

  /**
   * @param lotID
   */
  public void setChosenLotID(int lotID);
}
