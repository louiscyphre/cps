package cps.api.response;

import cps.entities.people.CompanyPerson;

/** Is sent in response to a ServiceLogin action. */
public class ServiceLoginResponse extends ServiceActionResponse {
  private static final long serialVersionUID = 1L;
  private CompanyPerson     user             = null;

  public CompanyPerson getUser() {
    return user;
  }

  public void setUser(CompanyPerson user) {
    this.user = user;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ServerResponse#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
