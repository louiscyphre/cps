package cps.api.action;

import cps.api.request.Request;
import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when an employee or the global manager want to login via the service interface. */
public class ServiceLoginAction extends Request {
  private static final long serialVersionUID = 1L;
  
  private String username;
  private String password;

  public ServiceLoginAction(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
