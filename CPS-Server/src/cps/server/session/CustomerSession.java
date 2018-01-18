package cps.server.session;

import static cps.common.Utilities.randomString;

import java.sql.Connection;
import java.sql.SQLException;

import com.google.gson.Gson;

import cps.api.response.ParkingServiceResponse;
import cps.entities.models.Customer;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import cps.server.ServerException;

public class CustomerSession extends BasicSession {
  protected Customer customer;

  public CustomerSession() {
    this.customer = null;
  }

  public CustomerSession(Customer customer) {
    this.customer = customer;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  @Override
  public User getUser() {
    return getCustomer();
  }

  public Customer requireCustomer() throws ServerException {
    if (customer == null) {
      throw new ServerException("This action requires a logged in customer");
    }

    return customer;
  }

  public Customer findCustomer(Connection conn, int customerID) throws SQLException, ServerException {
    customer = Customer.findByID(conn, customerID);
    
    if (customer == null) {
      throw new ServerException("Failed to find customer with id " + customerID);
    }

    return customer;
  }

  public Customer registerCustomer(Connection conn, String email) throws SQLException, ServerException {
    String password = randomString("0123456789", 4);
    
    // System.out.println(String.format("Sending password '%s' to email %s",
    // password, email));
    
    // Don't allow duplicate email addresses
    if (Customer.findByEmail(conn, email) != null) {
      throw new ServerException("Someone is already using that email address");
    }
    
    customer = Customer.create(conn, email, password);
    
    if (customer == null) {
      throw new ServerException("Failed to register customer with email " + email);
    }

    return customer;
  }

  public Customer requireRegisteredCustomer(Connection conn, int customerID, String email)
      throws SQLException, ServerException {
    // If customerID != 0 -> we try to find an existing customer
    // If customerID == 0 -> we create a new customer
    
    if (customerID != 0) {
      return findCustomer(conn, customerID);
    }

    return registerCustomer(conn, email);
  }
  
  public void requireLotNotFull(Connection conn, Gson gson, ParkingLot lot, ParkingServiceResponse response) throws SQLException, ServerException {
    if (lot.isLotFull() || lot.countFreeCells(conn) < 1) {
      response.setAlternativeLots(lot.retrieveAlternativeLots(conn, gson));
      throw new ServerException("The specified lot is full; please try one of the alternative lots");
    }
  }
}
