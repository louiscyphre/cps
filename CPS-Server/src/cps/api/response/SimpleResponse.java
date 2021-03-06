package cps.api.response;

/** A basic response that can signify error or success and contains a textual description.
 * Does not specify which request the response is for. */
public class SimpleResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;

  public SimpleResponse(int status, String description) {
    super(status, description);
  }

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

  /**
   * Returns Server Response OK.
   *
   * @param description
   *          the description
   * @return the server response
   */
  public static SimpleResponse ok(String description) {
    return new SimpleResponse(STATUS_OK, description);
  }

  /**
   * Returns Server Response Error.
   *
   * @param description
   *          the description
   * @return the server response
   */
  public static SimpleResponse error(String description) {
    return new SimpleResponse(STATUS_ERROR, description);
  }

  // /**
  // * Returns Server Response OK if condition == true
  // * else returns Server Response Error
  // *
  // * @param description the description
  // * @param condition the condition
  // * @return the server response
  // */
  // public static SimpleResponse decide(String description, boolean condition)
  // {
  // if (condition) {
  // return SimpleResponse.ok(description + " completed successfully");
  // } else {
  // return SimpleResponse.error(description + " failed");
  // }
  // }

}
