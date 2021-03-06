package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.database.DatabaseController;
import cps.server.session.CustomerSession;
import cps.server.session.UserSession;
import cps.server.testing.utilities.CustomerData;
import cps.server.testing.utilities.ServerControllerTest;
import cps.api.request.*;
import cps.api.action.*;
import cps.api.response.*;
import cps.common.Constants;
import cps.common.Utilities.Pair;
import cps.entities.models.CarTransportation;
import cps.entities.models.Complaint;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerConfig;

import junit.framework.TestCase;
import org.junit.Test;

import com.google.gson.Gson;

import org.junit.Assert;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
public class TestLogin extends ServerControllerTest {
  @Test
  public void testLogin() throws ServerException {
    /*
     * Scenario: 1. Create user 2. Attempt to login as user 3. Attempt to login
     * with a wrong password 4. Attempt to login with a wrong email
     */

    header("testLogin");

    // Create user
    CustomerData data = new CustomerData(0, "user@email", "1234", "IL11-222-33", 1, 0);
    Customer customer = db.performQuery(conn -> Customer.create(conn, data.email, data.password));
    assertNotNull(customer);
    printObject(customer);

    // Create login request
    LoginRequest request = new LoginRequest(data.email, data.password);

    // Test the response
    ServerResponse response = server.dispatch(request, getContext());
    printObject(response);
    assertThat(response, instanceOf(LoginResponse.class));
    assertTrue(response.success());

    LoginResponse loginResponse = (LoginResponse) response;
    assertEquals(customer.getId(), loginResponse.getCustomerID());
  }
  
  public void testServiceLogin() throws ServerException {
    header("testServiceLogin");
    
    // Create service login request
    ServiceLoginAction action = new ServiceLoginAction("malki", "1234");
    printObject(action);

    // Test the response
    ServerResponse response = server.dispatch(action, getContext());
    printObject(response);
    assertThat(response, instanceOf(ServiceLoginResponse.class));
    assertTrue(response.success());   
  }
}