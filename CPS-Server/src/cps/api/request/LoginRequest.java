package cps.api.request;

import cps.api.response.ServerResponse;

/** Is sent by the client application when a customer wants to login with their email and password that they received from previously buying a service. */
public class LoginRequest extends Request {
  private static final long serialVersionUID = 1L;

  private String email;
  private String password;

  public LoginRequest(String email, String password) {
    this.email = email;
    this.setPassword(password);
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
