package cps.api.response;

import java.time.LocalDate;

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

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }

}
