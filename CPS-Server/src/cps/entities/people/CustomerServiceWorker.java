package cps.entities.people;

import cps.common.Constants;

public class CustomerServiceWorker extends CustomerServiceEmployee {
  private static final long serialVersionUID = 1L;

  public CustomerServiceWorker(int id, String email, String username, String password, String firstName,
      String lastName, String jobTitle, int managerID, int departmentID) {
    super(id, email, username, password, firstName, lastName, jobTitle, managerID, departmentID);
    setAccessLevel(Constants.ACCESS_LEVEL_CUSTOMER_SERVICE_WORKER);
    setAccessDomain(Constants.ACCESS_DOMAIN_CUSTOMER_SERVICE);
  }
}
