package cps.server.controllers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import cps.api.action.ServiceLoginAction;
import cps.api.action.ServiceLogoutAction;
import cps.api.request.LoginRequest;
import cps.api.response.LoginResponse;
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SimpleResponse;
import cps.entities.models.Customer;
import cps.entities.people.CompanyPerson;
import cps.entities.people.User;
import cps.server.ServerController;
import cps.server.session.CustomerSession;
import cps.server.session.ServiceSession;
import cps.server.session.SessionHolder;

/** Processes login requests. */
public class UserController extends RequestController {
  private Set<Integer> loggedInCustomers = Collections.synchronizedSet(new HashSet<Integer>());
  private Set<Integer> loggedInEmployees = Collections.synchronizedSet(new HashSet<Integer>());

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
      
      // Check if not already logged in
      errorIf(loggedInCustomers.contains(customer.getId()), "You are already logged in");
      
      // Mark user as logged in
      loggedInCustomers.add(customer.getId());
      
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
      // Check credentials
      CompanyPerson person = session.login(action.getUsername(), action.getPassword());
      errorIfNull(person, "Login failed, invalid username or password.");
      
      // Check if not already logged in
      errorIf(loggedInEmployees.contains(person.getId()), "You are already logged in");
      
      // Mark user as logged in
      loggedInEmployees.add(person.getId());   
      
      // Success
      response.setUser(person);
      response.setSuccess("Login successful");
      return response;
    });
  }

  public void removeSession(SessionHolder context) {
    if (context.getCustomerSession() != null) {
      User user = context.getCustomerSession().getUser();
      if (user != null) {
        loggedInCustomers.remove(user.getId());
      }
    }
    
    if (context.getServiceSession() != null) {
      User user = context.getServiceSession().getUser();
      if (user != null) {
        loggedInEmployees.remove(user.getId()); 
      }
    }
  }

  public ServerResponse handle(ServiceLogoutAction action, ServiceSession session) {
    // Check if we have a valid session
    if (session == null) {
      return SimpleResponse.error("Invalid parameters");
    }
    
    // Check if the user is logged in
    User user = session.getUser();
    
    if (user == null || !loggedInEmployees.contains(user.getId())) {
      return SimpleResponse.error("Not logged in");
    }
    
    // Remove logged in user
    loggedInEmployees.remove(user.getId()); 
    
    // Success    
    return SimpleResponse.ok("Logged out");
  }
}
