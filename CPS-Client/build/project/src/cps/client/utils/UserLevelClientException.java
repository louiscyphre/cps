package cps.client.utils;

/**
 * @author firl
 *
 */
public class UserLevelClientException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param errorMessage
   */
  public UserLevelClientException(String errorMessage) {
    super(errorMessage);
  }
}
