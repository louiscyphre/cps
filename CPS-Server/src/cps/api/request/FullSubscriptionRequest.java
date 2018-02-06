package cps.api.request;

import java.time.LocalDate;

import cps.api.response.ServerResponse;
import cps.common.Constants;

/** Is sent by the client application when a customer wants to purchase a full monthly subscription. */
public class FullSubscriptionRequest extends SubscriptionRequest {
  private static final long serialVersionUID = 1L;

  public FullSubscriptionRequest(int customerID, String email, String[] carIDs, LocalDate startDate, int lotID) {
    super(customerID, email, carIDs, startDate, lotID);
  }

  public FullSubscriptionRequest(int customerID, String email, String carID, LocalDate startDate, int lotID) {
    super(customerID, email, carID, startDate, lotID);
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  @Override
  public int getSubscriptionType() {
    return Constants.SUBSCRIPTION_TYPE_FULL;
  }
}
