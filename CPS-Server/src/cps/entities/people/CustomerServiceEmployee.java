package cps.entities.people;

/** Base class for Customer Service employees. */
public abstract class CustomerServiceEmployee extends Employee {
  private static final long serialVersionUID = 1L;

  public CustomerServiceEmployee(int id, String email, String username, String password, String firstName,
      String lastName, String jobTitle, int managerID, int departmentID) {
    super(id, email, username, password, firstName, lastName, jobTitle, managerID, departmentID);
  }

  public void receiveComplaint() {
    throw new UnsupportedOperationException();
  }

  public void authorizeRefund() {
    throw new UnsupportedOperationException();
  }

}
