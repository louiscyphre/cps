package cps.client.context;

import cps.client.utils.InternalClientException;
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
  public CompanyPerson requireCompanyPerson() throws InternalClientException {
    if (companyPerson == null) {
      throw new InternalClientException("This action requires a logged in user");
    }
    
    return companyPerson;
  }

  @Override
  public void setCompanyPerson(CompanyPerson companyPerson) {
    this.companyPerson = companyPerson;
  }

}
