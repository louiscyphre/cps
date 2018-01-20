package cps.client.context;

import cps.client.utils.InternalClientException;
import cps.entities.people.CompanyPerson;

/**
 * Public interface representing Employee context. 
 * Represents the state of customer - logged or not. 
 * Possesses the following data : id, email, chosen lot id.
 */
public interface EmployeeContext {

  /**
   * Retrieves the employee person, which is currently logged in.
   * @see CompanyPerson
   * @return company person bound to the context
   */
  public CompanyPerson getCompanyPerson();

  /**
   * Retrieves the employee person, which is currently logged in. If company person is not assigned - throws InternalClientException.
   * @return company person
   * @throws InternalClientException
   */
  public CompanyPerson requireCompanyPerson() throws InternalClientException;

  /**
   * Setter function binding the given company person to the context
   * @param companyPerson - company person to be bound to the context
   */
  public void setCompanyPerson(CompanyPerson companyPerson);

  /**
   * Logs the whole context out
   */
  public void logContextOut();
}
