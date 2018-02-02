package cps.api.response;

/** Base class for all server responses. */
public abstract class Response {
  
  /** Call the handler for this response.
   * Response handlers are implemented in the client, in order to be able to process server responses and perform some kind of action when a response is received.
   * (Such as update a UI widget with the data in the response.)
   * This is an abstract method and it has to be implemented in each concrete subclass to call the right type of handler for that subclass.
   * @param handler the handler
   * @return the server response */
  public abstract void handle(ResponseHandler handler);
}
