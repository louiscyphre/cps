package cps.entities.people;

public abstract class Employee extends CompanyPerson {
  private static final long serialVersionUID = 1L;

  private int managerID;
  private int departmentID;

  public Employee(int id, String email, String username, String password, String firstName, String lastName,
      String jobTitle, int managerID, int departmentID) {
    super(id, email, username, password, firstName, lastName, jobTitle);
    this.managerID = managerID;
    this.departmentID = departmentID;
  }
  
  @Override
  public int getManagerID() {
    return managerID;
  }

  public void setManagerID(int managerID) {
    this.managerID = managerID;
  }
  
  @Override
  public int getDepartmentID() {
    return departmentID;
  }

  @Override
  public void setDepartmentID(int departmentID) {
    this.departmentID = departmentID;
  }

}
