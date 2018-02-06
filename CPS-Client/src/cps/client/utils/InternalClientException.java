package cps.client.utils;

import cps.client.controller.ViewController;

/**
 * Exception class used by client application, caused by Client's internal error.
 */
public class InternalClientException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * @param errorMessage error message to be shown on displayError
   * @see ViewController#displayError(String)
   * @see ViewController#displayError(java.util.List)
   */
  public InternalClientException(String errorMessage) {
    super(errorMessage);
  }
}
