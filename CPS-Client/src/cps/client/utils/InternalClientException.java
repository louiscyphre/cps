package cps.client.utils;

public class InternalClientException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public InternalClientException(String errorMessage) {
    super(errorMessage);
  }
}
