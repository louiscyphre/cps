package cps.entities.people;

import cps.common.Constants;

/** Has access to Parking Lot actions for the assigned lot (department ID). */
public class LocalWorker extends LocalEmployee {
  private static final long serialVersionUID = 1L;

  public LocalWorker(int id, String email, String username, String password, String firstName, String lastName,
      String jobTitle, int managerID, int departmentID) {
    super(id, email, username, password, firstName, lastName, jobTitle, managerID, departmentID);
    setAccessLevel(Constants.ACCESS_LEVEL_LOCAL_WORKER);
    setAccessDomain(Constants.ACCESS_DOMAIN_PARKING_LOT);
  }
}
