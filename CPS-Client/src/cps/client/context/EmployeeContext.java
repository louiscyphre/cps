package cps.client.context;

import cps.entities.people.CompanyPerson;

public interface EmployeeContext {

  public CompanyPerson getCompanyPerson();

  public void setCompanyPerson(CompanyPerson companyPerson);

  public void logContextOut();
}
