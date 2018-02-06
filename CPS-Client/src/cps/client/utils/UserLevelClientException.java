package cps.client.utils;

import cps.client.controller.ViewController;

/** Exception class used by client application, caused by faulty user interaction. */

public class UserLevelClientException extends Exception {
  private static final long serialVersionUID = 1L;

  /** @param errorMessage error message to be shown on displayError
   * @see ViewController#displayError(String)
   * @see ViewController#displayError(java.util.List) */
  public UserLevelClientException(String errorMessage) {
    super(errorMessage);
  }
}
