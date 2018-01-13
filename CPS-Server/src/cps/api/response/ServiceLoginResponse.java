package cps.api.response;

import cps.entities.people.CompanyPerson;

public class ServiceLoginResponse extends ServiceActionResponse {
  private static final long serialVersionUID = 1L;
  private CompanyPerson user = null;
  
  public CompanyPerson getUser() {
    return user;
  }
  public void setUser(CompanyPerson user) {
    this.user = user;
  }
  
}
