package cps.api.request;

import java.time.LocalDate;
import java.time.LocalTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;

public class RegularSubscriptionRequest extends SubscriptionRequest {
  private static final long serialVersionUID = 1L;
  private int               lotID;
  private LocalTime         dailyExitTime;

  public RegularSubscriptionRequest(int customerID, String email, String carID, LocalDate startDate, int lotID,
      LocalTime dailyExitTime) {
    super(customerID, email, carID, startDate);
    this.lotID = lotID;
    this.dailyExitTime = dailyExitTime;
  }

  @Override
  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public LocalTime getDailyExitTime() {
    return dailyExitTime;
  }

  public void setDailyExitTime(LocalTime endTime) {
    this.dailyExitTime = endTime;
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  @Override
  public int getSubscriptionType() {
    return Constants.SUBSCRIPTION_TYPE_REGULAR;
  }
}
