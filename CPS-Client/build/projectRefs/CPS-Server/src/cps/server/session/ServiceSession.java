package cps.server.session;

import cps.entities.people.*;
import cps.server.ServerException;

public class ServiceSession extends BasicSession {
  CompanyPerson user;

  @Override
  public User getUser() {
    return user;
  }

  public CompanyPerson login(String username, String password) {
    user = CompanyPersonService.findWithLoginData(username, password);
    return user;
  }
  
  public CompanyPerson requireCompanyPerson() throws ServerException {
    if (user == null) {
      throw new ServerException("This action requires a logged in Employee");
    }
    
    return user;
  }
}
