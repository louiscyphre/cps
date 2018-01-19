package cps.client.context;

import cps.client.utils.InternalClientException;
import cps.entities.people.CompanyPerson;

public interface EmployeeContext {

  /**
   * @return
   */
  public CompanyPerson getCompanyPerson();

  /**
   * @return
   * @throws InternalClientException
   */
  public CompanyPerson requireCompanyPerson() throws InternalClientException;

  /**
   * @param companyPerson
   */
  public void setCompanyPerson(CompanyPerson companyPerson);

  /**
   * 
   */
  public void logContextOut();
}
