package cps.server.testing.utilities;

public class CustomerData {
  public int    customerID;
  public String email;
  public String password;
  public String carID;
  public int    lotID;
  public int    subsID;
  public int    onetimeServiceID;

  public CustomerData(int customerID, String email, String password, String carID, int lotID, int subsID) {
    this.customerID = customerID;
    this.email = email;
    this.password = password;
    this.carID = carID;
    this.lotID = lotID;
    this.subsID = subsID;
    this.onetimeServiceID = 0;
  }
  
  public CustomerData() {
    this(0, null, null, null, 0, 0);
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCarID() {
    return carID;
  }

  public void setCarID(String carID) {
    this.carID = carID;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public int getSubsID() {
    return subsID;
  }

  public void setSubsID(int subsID) {
    this.subsID = subsID;
  }

  public int getOnetimeServiceID() {
    return onetimeServiceID;
  }

  public void setOnetimeServiceID(int onetimeServiceID) {
    this.onetimeServiceID = onetimeServiceID;
  }
}
