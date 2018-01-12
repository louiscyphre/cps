package cps.server.controllers;

import cps.api.request.LoginRequest;
import cps.api.response.LoginResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.Customer;
import cps.server.ServerController;
import cps.server.session.CustomerSession;

public class CustomerController extends RequestController {

  public CustomerController(ServerController serverController) {
    super(serverController);
  }

  public ServerResponse handle(LoginRequest request, CustomerSession session) {
    return database.performQuery(new LoginResponse(), (conn, response) -> {
      Customer customer = Customer.findByEmailAndPassword(conn, request.getEmail(), request.getPassword());
      errorIfNull(customer, "Login failed, invalid email or password.");
      response.setCustomerID(customer.getId());
      response.setSuccess("Login successful");
      return response;
    });
  }
}
