package cps.api.request;

import java.time.LocalDate;

/** Base class for monthly subscription purchase requests - full or regular. */
public abstract class SubscriptionRequest extends CustomerRequest {
  private static final long serialVersionUID = 1L;
  private String            email;
  private String            carID;
  private LocalDate         startDate;

  public SubscriptionRequest(int customerID, String email, String carID, LocalDate startDate) {
    super(customerID);
    this.email = email;
    this.carID = carID;
    this.startDate = startDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCarID() {
    return carID;
  }

  public void setCarID(String carID) {
    this.carID = carID;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public abstract int getSubscriptionType();

  public abstract int getLotID();
}
