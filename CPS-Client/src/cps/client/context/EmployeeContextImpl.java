package cps.client.context;

import cps.client.utils.InternalClientException;
import cps.entities.people.CompanyPerson;

/**
 * Straightforward implementation of the EmployeeContext
 */
public class EmployeeContextImpl implements EmployeeContext {

  /**
   * CompanyPerson entity bound to the context.
   */
  private CompanyPerson companyPerson;

  /** ID of the chosen lot */
  private int chosenLotIDforQuery;

  /* (non-Javadoc)
   * @see cps.client.context.EmployeeContext#logContextOut()
   */
  @Override
  public void logContextOut() {
    companyPerson = null;
  }

  /* (non-Javadoc)
   * @see cps.client.context.EmployeeContext#getCompanyPerson()
   */
  @Override
  public CompanyPerson getCompanyPerson() {
    return this.companyPerson;
  }

  /* (non-Javadoc)
   * @see cps.client.context.EmployeeContext#requireCompanyPerson()
   */
  @Override
  public CompanyPerson requireCompanyPerson() throws InternalClientException {
    if (companyPerson == null) {
      throw new InternalClientException("This action requires a logged in user");
    }

    return companyPerson;
  }

  /* (non-Javadoc)
   * @see cps.client.context.EmployeeContext#setCompanyPerson(cps.entities.people.CompanyPerson)
   */
  @Override
  public void setCompanyPerson(CompanyPerson companyPerson) {
    this.companyPerson = companyPerson;
  }
  
  /*
   * (non-Javadoc)
   * @see cps.client.context.CustomerContext#getChosenLotID()
   */
  @Override
  public int getChosenLotID() {
    return chosenLotIDforQuery;
  }


  /*
   * (non-Javadoc)
   * @see cps.client.context.CustomerContext#setChosenLotID(int)
   */
  @Override
  public void setChosenLotID(int lotID) {
    chosenLotIDforQuery = lotID;
  }
}
