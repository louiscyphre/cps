package cps.server.controllers;

import cps.api.action.ServiceLoginAction;
import cps.api.request.LoginRequest;
import cps.api.response.LoginResponse;
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.entities.models.Customer;
import cps.entities.people.CompanyPerson;
import cps.server.ServerController;
import cps.server.session.CustomerSession;
import cps.server.session.ServiceSession;

public class UserController extends RequestController {

  public UserController(ServerController serverController) {
    super(serverController);
  }

  public ServerResponse handle(LoginRequest request, CustomerSession session) {
    return database.performQuery(new LoginResponse(), (conn, response) -> {
      // Check credentials
      Customer customer = Customer.findByEmailAndPassword(conn, request.getEmail(), request.getPassword());
      errorIfNull(customer, "Login failed, invalid email or password.");
      
      // Success
      session.setCustomer(customer);
      response.setCustomerID(customer.getId());
      response.setSuccess("Login successful");
      return response;
    });
  }

  public ServerResponse handle(ServiceLoginAction action, ServiceSession session) {
    return database.performQuery(new ServiceLoginResponse(), (conn, response) -> {
      CompanyPerson person = session.login(action.getUsername(), action.getPassword());
      errorIfNull(person, "Login failed, invalid username or password.");
      response.setUser(person);
      response.setSuccess("Login successful");
      return response;
    });
  }
}
