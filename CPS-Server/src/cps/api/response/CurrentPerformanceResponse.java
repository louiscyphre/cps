package cps.api.response;

import java.time.LocalDate;

/** Is sent in response to a GetCurrentPerformance action. */
public class CurrentPerformanceResponse extends ServerResponse {
  private static final long serialVersionUID = 1L;
  private LocalDate         date;
  private int               numberOfSubscriptions;
  private int               numberOfSubscriptionsWithMultipleCars;

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public int getNumberOfSubscriptions() {
    return numberOfSubscriptions;
  }

  public void setNumberOfSubscriptions(int numberOfSubscriptions) {
    this.numberOfSubscriptions = numberOfSubscriptions;
  }

  public int getNumberOfSubscriptionsWithMultipleCars() {
    return numberOfSubscriptionsWithMultipleCars;
  }

  public void setNumberOfSubscriptionsWithMultipleCars(int numberOfSubscriptionsWithMultipleCars) {
    this.numberOfSubscriptionsWithMultipleCars = numberOfSubscriptionsWithMultipleCars;
  }

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
