package cps.entities.people;

import cps.common.Constants;

/** The manager of the entire network. Has access to all actions. */
public class GlobalManager extends CompanyPerson {
  private static final long serialVersionUID = 1L;

  public GlobalManager(int id, String email, String username, String password, String firstName, String lastName,
      String jobTitle) {
    super(id, email, username, password, firstName, lastName, jobTitle);
    setAccessLevel(Constants.ACCESS_LEVEL_GLOBAL_MANAGER);
    setAccessDomain(Constants.ACCESS_DOMAIN_EVERYTHING);
  }
}
