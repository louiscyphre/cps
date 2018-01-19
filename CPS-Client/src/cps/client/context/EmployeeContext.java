package cps.client.context;

import cps.client.utils.InternalClientException;
import cps.entities.people.CompanyPerson;

public interface EmployeeContext {

  public CompanyPerson getCompanyPerson();

  public CompanyPerson requireCompanyPerson() throws InternalClientException;

  public void setCompanyPerson(CompanyPerson companyPerson);

  public void logContextOut();
}
