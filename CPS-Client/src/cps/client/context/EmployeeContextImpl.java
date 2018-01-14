package cps.client.context;

import cps.entities.people.CompanyPerson;

public class EmployeeContextImpl implements EmployeeContext {

  private CompanyPerson companyPerson;
  
  @Override
  public void logContextOut() {
    companyPerson = null;
  }

  @Override
  public CompanyPerson getCompanyPerson() {
    return this.companyPerson;
  }

  @Override
  public void setCompanyPerson(CompanyPerson companyPerson) {
    this.companyPerson = companyPerson;
  }

}
