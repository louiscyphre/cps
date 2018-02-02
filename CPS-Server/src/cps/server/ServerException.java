package cps.server;

/** A general exception that can be thrown in the event of all kinds of internal logic errors while processing client requests.
 * Is automatically handled in some wrapper methods, to facilitate a more streamlined design for request handlers.
 * */
public class ServerException extends Exception {
  private static final long serialVersionUID = 1L;

  /** Instantiates a new server exception.
   * @param message the message */
  public ServerException(String message) {
    super(message);
  }
}