package cps.client.context;

public interface EmployeeContext {
  public int getEmployeeId();
  public void setEmployeeId(int id);
  public String getEmployeeUsername();
  public void setEmployeeUsername(String username);
  public int getEmployeeAccessLevel();
  public void setEmployeeAccessLevel(int accessLevel);
}
