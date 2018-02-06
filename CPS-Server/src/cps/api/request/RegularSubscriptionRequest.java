package cps.api.request;

import java.time.LocalDate;
import java.time.LocalTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;

/** Is sent by the client application when a customer wants to purchase a regular monthly subscription. */
public class RegularSubscriptionRequest extends SubscriptionRequest {
  private static final long serialVersionUID = 1L;
  private int               lotID;
  private LocalTime         dailyExitTime;

  /**
   * Instantiates a new regular subscription request.
   *
   * @param customerID the customer ID
   * @param email the email
   * @param carIDs the list of car IDs
   * @param startDate the start date
   * @param lotID the lot ID
   * @param dailyExitTime the daily exit time
   */
  public RegularSubscriptionRequest(int customerID, String email, String[] carIDs, LocalDate startDate, int lotID,
      LocalTime dailyExitTime) {
    super(customerID, email, carIDs, startDate);
    this.lotID = lotID;
    this.dailyExitTime = dailyExitTime;
  }


  /**
   * Instantiates a new regular subscription request.
   *
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param startDate the start date
   * @param lotID the lot ID
   * @param dailyExitTime the daily exit time
   */
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

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  @Override
  public int getSubscriptionType() {
    return Constants.SUBSCRIPTION_TYPE_REGULAR;
  }
}
