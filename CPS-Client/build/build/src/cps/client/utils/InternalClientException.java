package cps.client.utils;

/**
 * @author firl
 *
 */
public class InternalClientException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param errorMessage
   */
  public InternalClientException(String errorMessage) {
    super(errorMessage);
  }
}
