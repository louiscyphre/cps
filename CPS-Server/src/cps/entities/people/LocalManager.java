package cps.entities.people;

import cps.common.Constants;

/** Has access to Parking Lot actions for the assigned lot (department ID) - higher access level than LocalWorker. */
public class LocalManager extends LocalEmployee {
  private static final long serialVersionUID = 1L;

  public LocalManager(int id, String email, String username, String password, String firstName, String lastName,
      String jobTitle, int managerID, int departmentID) {
    super(id, email, username, password, firstName, lastName, jobTitle, managerID, departmentID);
    setAccessLevel(Constants.ACCESS_LEVEL_LOCAL_MANAGER);
    setAccessDomain(Constants.ACCESS_DOMAIN_PARKING_LOT);
  }
}
