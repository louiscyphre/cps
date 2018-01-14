package cps.client.utils;

public class UserLevelClientException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public UserLevelClientException(String errorMessage) {
    super(errorMessage);
  }
}
