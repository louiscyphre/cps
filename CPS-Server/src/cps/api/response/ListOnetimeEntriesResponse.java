package cps.api.response;

import java.util.Collection;
import cps.entities.models.OnetimeService;

/** Is sent in response to a ListOnetimeEntries request. */
public class ListOnetimeEntriesResponse extends ServerResponse {
  private static final long          serialVersionUID = 1L;
  private Collection<OnetimeService> data             = null;
  private int                        customerID       = 0;

  public Collection<OnetimeService> getData() {
    return data;
  }

  public void setData(Collection<OnetimeService> data) {
    this.data = data;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
