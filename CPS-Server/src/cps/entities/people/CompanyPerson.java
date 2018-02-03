package cps.entities.people;

import java.io.Serializable;

import cps.common.Constants;

/** Base class for all system users (employees). */
public abstract class CompanyPerson implements User, Serializable {
  private static final long serialVersionUID = 1L;

  private int    id;
  private String email;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String jobTitle;
  private int    accessLevel  = 0;
  private int    accessDomain = 0;

  public CompanyPerson(int id, String email, String username, String password, String firstName, String lastName,
      String jobTitle) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.jobTitle = jobTitle;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public int getUserType() {
    return Constants.USER_TYPE_COMPANY_PERSON;
  }

  @Override
  public int getAccessLevel() {
    return accessLevel;
  }

  protected void setAccessLevel(int accessLevel) {
    this.accessLevel = accessLevel;
  }

  public int getAccessDomain() {
    return accessDomain;
  }

  protected void setAccessDomain(int accessDomain) {
    this.accessDomain = accessDomain;
  }

  @Override
  public boolean canAccessDomain(int domain) {
    return (accessDomain & domain) != 0;
  }
  
  public int getManagerID() {
    return 0;
  }
  
  public int getDepartmentID() {
    return 0;
  }
  
  public int getLotID() {
    return 0;
  }

  public void setDepartmentID(int id) {
    
  }
}
