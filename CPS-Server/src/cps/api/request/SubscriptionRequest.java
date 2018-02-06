package cps.api.request;

import java.time.LocalDate;

/** Base class for monthly subscription purchase requests - full or regular. */
public abstract class SubscriptionRequest extends CustomerRequest {
  private static final long serialVersionUID = 1L;
  private String            email;
  private String[]          carIDs;
  private LocalDate         startDate;

  public SubscriptionRequest(int customerID, String email, String[] carIDs, LocalDate startDate) {
    super(customerID);
    this.email = email;
    this.carIDs = carIDs;
    this.startDate = startDate;
  }

  public SubscriptionRequest(int customerID, String email, String carID, LocalDate startDate) {
    super(customerID);
    this.email = email;
    this.carIDs = new String[] { carID };
    this.startDate = startDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String[] getCarIDs() {
    return carIDs;
  }

  public void setCarIDs(String[] carIDs) {
    this.carIDs = carIDs;
  }

  public String getCarID(int index) {
    if (carIDs == null) {
      return null;
    }
    
    return carIDs[index];
  }

  public String getCarID() {
    if (carIDs == null) {
      return null;
    }
    
    return carIDs[0];
  }

  public void setCarID(String carID) {
    if (carIDs == null) {
      carIDs = new String[1];
    }
    
    carIDs[0] = carID;
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
