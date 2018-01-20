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

/** Processes login requests. */
public class UserController extends RequestController {

  /** Instantiates a new user controller.
   * @param serverController the server controller */
  public UserController(ServerController serverController) {
    super(serverController);
  }

  /** Called when a customer wants to login with their email and password.
   * On success, saves the current user in the session object, which can be retrieved later in other requests.
   * @param request the request
   * @param session the session
   * @return the server response */
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

  /** Called when an employee (or global manager) wants to login with their username and password.
   * On success, saves the current user in the session object, which can be retrieved later in other requests.
   * The user's access permissions and other data are accessible through the CompanyPerson object, which is contained in the session.
   * This way, other request handlers can check if the currently logged in user has the correct permissions to perform the action.
   * (I.e. a Local Employee shouldn't be able to do actions that only the Global Manager has permission to access.)
   * @param action the action
   * @param session the session
   * @return the server response */
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
