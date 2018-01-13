package cps.client.context;

public class EmployeeContextImpl implements EmployeeContext {

  private int    id;
  private String username;
  private int    accessLevel;

  @Override
  public int getEmployeeId() {
    return this.id;
  }

  @Override
  public void setEmployeeId(int id) {
    this.id = id;
  }

  @Override
  public String getEmployeeUsername() {
    return this.username;
  }

  @Override
  public void setEmployeeUsername(String username) {
    this.username = username;
  }

  @Override
  public int getEmployeeAccessLevel() {
    return this.accessLevel;
  }

  @Override
  public void setEmployeeAccessLevel(int accessLevel) {
    this.accessLevel = accessLevel;
  }

  @Override
  public void logContextOut() {
    id = 0;
    username = null;
    accessLevel = 0;
  }

}
