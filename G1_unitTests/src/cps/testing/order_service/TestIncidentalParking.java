package cps.testing.order_service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.CarTransportationController;
import cps.server.database.DatabaseController;
import cps.server.session.CompanyPersonService;
import cps.server.session.CustomerSession;
import cps.server.session.ServiceSession;
import cps.server.session.SessionHolder;
import cps.server.session.UserSession;
import cps.server.testing.utilities.CustomerActor;
import cps.server.testing.utilities.EmployeeActor;
import cps.testing.utilities.CustomerData;
import cps.testing.utilities.ParkingLotData;
import cps.testing.utilities.ServerControllerTestBase;
import cps.api.request.*;
import cps.api.action.*;
import cps.api.response.*;
import cps.common.Constants;
import cps.common.Utilities;
import cps.common.Utilities.Pair;
import cps.entities.models.CarTransportation;
import cps.entities.models.Complaint;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.entities.people.User;
import cps.server.ServerConfig;

import junit.framework.TestCase;
import org.junit.Test;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
public class TestIncidentalParking extends ServerControllerTestBase {
  CustomerData custData;
  ParkingLotData[] lotData = new ParkingLotData[2];
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    custData = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
    
    // Setup Parking Lot data
    lotData[0] = new ParkingLotData(0, "Sesame, 1", 4, 5f, 4f, "1.0.0.1");
    lotData[1] = new ParkingLotData(0, "Zanzibar, 2", 4, 5f, 4f, "1.0.0.2");
  }
  
  @Test
  public void testIncidentalParking() throws ServerException {
    // Test 1 - Order of incidental parking by new customer (without ID and password)
    /* Scenario:
     * 1. Create Parking Lot
     * 2. Send Incidental Parking request
     * 3. Send Parking Exit request */

    header("testIncidentalParking - new customer");

    // Create Parking Lot
    initParkingLot(lotData[0]);
    
    // Planned time for parking
    LocalDateTime startTime = getTime();
    LocalDateTime plannedEndTime = startTime.plusHours(8);
    
    // Send Incidental Parking request    
    // After performing this request, the system should register our customer as a new customer
    int numCustomers = db.countEntities("customer");
    requestIncidentalParking(custData, getContext(), plannedEndTime);    
    checkRegistered(custData, numCustomers);
    
    // Exit from parking - exit in time
    exitParking(custData, startTime, plannedEndTime);
    
    // Now we will try to order parking again, now as a registered customer
    header("testIncidentalParking - registered customer");
    
    // Since the server currently considered us logged in, we clear out the session context, in order to simulate a clean login
    getContext().setCustomerSession(null);
    
    // Attempt to login again with our email and password
    requestLogin(custData, getContext());
    
    // Repeat the procedure
    startTime = getTime();
    plannedEndTime = startTime.plusHours(8);
    
    // Save current state parameters
    numCustomers = db.countEntities("customer");
    int previousID = custData.getCustomerID();
    
    // Send Incidental Parking request
    requestIncidentalParking(custData, getContext(), plannedEndTime);
    
    // Check that the customer ID stayed the same --  a new customer was NOT registered
    assertEquals(previousID, custData.getCustomerID());
    
    // Check this in the database too -- number of customers did NOT increase
    assertEquals(numCustomers, db.countEntities("customer"));
    
    // Exit from parking - exit in time
    exitParking(custData, startTime, plannedEndTime);
  }
  
  @Test
  public void testReservationOverlap() throws ServerException {
    // Order of incidental parking by existing customer. The incidental parking overlaps a reserved parking.
    header("testIncidentalParking - overlap with reserved parking");

    // Create Parking Lot
    initParkingLot(lotData[0]);
    
    // Planned time for parking
    LocalDateTime plannedStartTime = getTime().plusHours(1);
    LocalDateTime plannedEndTime = plannedStartTime.plusHours(8);
    
    // Save current state
    int numCustomers = db.countEntities("customer"); // should be 0
    assertEquals(0, numCustomers);
    
    // Send Reserved Parking request
    requestReservedParking(custData, getContext(), plannedStartTime, plannedEndTime);

    // The customer should now be registered
    checkRegistered(custData, numCustomers);
    
    // Save current state
    numCustomers = db.countEntities("customer"); // should be 1
    assertEquals(1, numCustomers);
    
    int numServices = db.countEntities("onetime_service"); // should be 1
    assertEquals(1, numServices);
    
    // Now try to order an incidental parking that overlaps with the previous parking reservation
    plannedEndTime = getTime().plusHours(2);
    IncidentalParkingRequest request = new IncidentalParkingRequest(custData.getCustomerID(), custData.getEmail(), custData.getCarID(), custData.getLotID(), plannedEndTime);
    printObject(request);
    IncidentalParkingResponse response = sendRequest(request, getContext(), IncidentalParkingResponse.class);
    printObject(response);
    
    // The response should be a failure
    assertFalse(response.success());
    
    // Test that no new parking service was added in the database
    assertEquals(numServices, db.countEntities("onetime_service"));
  }
  
  @Test
  public void testFullLot() throws ServerException {
    // Order of incidental parking with no available parking slots and set alternative parking lots
    header("testIncidentalParking - full parking lot");

    // Create two Parking Lots
    ParkingLot lot1 = initParkingLot(lotData[0]);
    ParkingLot lot2 = initParkingLot(lotData[1]);
    
    // Fill lot1 with cars
    fillLot(lot1);
    
    // Set redirect from lot1 to lot2
    setRedirects(lot1, new int[] { lot2.getId() });
    
    // Attempt to park in this lot
    
    // Planned time for parking
    LocalDateTime startTime = getTime();
    LocalDateTime plannedEndTime = startTime.plusHours(8);
    
    // Send Incidental Parking request    
    IncidentalParkingRequest request = new IncidentalParkingRequest(custData.getCustomerID(), custData.getEmail(), custData.getCarID(), custData.getLotID(), plannedEndTime);
    printObject(request);    
    IncidentalParkingResponse response = sendRequest(request, getContext(), IncidentalParkingResponse.class);
    printObject(response);
    
    // The response must be a failure
    assertFalse(response.success()); 
    
    // We should see a list of alternative parking lots in the response, that includes lot2
    Collection<ParkingLot> alternativeLots = response.getAlternativeLots();
    assertNotNull(alternativeLots);
    assertTrue(alternativeLots.size() > 0);
    ParkingLot firstElement = alternativeLots.iterator().next();
    assertNotNull(firstElement);
    assertEquals(lot2.getId(), firstElement.getId());
  }
  
  private void setRedirects(ParkingLot lot, int[] alternativeLots) throws ServerException {
    ServiceSession session = getContext().acquireServiceSession();
    User user = session.login("sarit", "1234");
    assertNotNull(user);
    
    SetFullLotAction action = new SetFullLotAction(user.getId(), lot.getId(), false, alternativeLots);
    printObject(action);
    SetFullLotResponse response = sendRequest(action, getContext(), SetFullLotResponse.class);
    assertTrue(response.success());
    printObject(response);
    db.performAction(conn -> lot.update(conn));    
  }
  
  private void fillLot(ParkingLot lot) throws ServerException {    
    db.performAction(conn -> {
      CarTransportationController tc = server.getTransportationController();
      for (int i = 0, n = lot.getVolume(); i < n; i++) {
        String carID = Utilities.randomString("ILBTA", 2) + "-" + Utilities.randomString("1234567890", 6);
        LocalDateTime plannedExitTime = getTime().plusDays(10);
        tc.insertCar(conn, lot, carID, plannedExitTime);
      }
    });
  }

  private void checkRegistered(CustomerData data, int numCustomers) throws ServerException {    
    // Check that a new customer ID was created
    assertNotEquals(0, custData.getCustomerID());
    
    // Check that a new password was created
    assertNotNull(custData.getPassword());
    
    // The password should have a length of 4
    assertEquals(4, custData.getPassword().length());
    
    // Test database result
    assertEquals(numCustomers + 1, db.countEntities("customer"));
  }

  private void exitParking(CustomerData custData2, LocalDateTime startTime, LocalDateTime endTime) throws ServerException {    
    // Send Parking Exit request
    setTime(endTime);
    ParkingExitResponse response = requestParkingExit(custData, getContext());
    
    // Check that the payment was correct
    float expectedPrice = calculateFlatPayment(startTime, endTime, lotData[0].getPrice1());
    assertEquals(expectedPrice, response.getPayment());
  }

  private float calculateFlatPayment(LocalDateTime start, LocalDateTime end, float price) {
    Duration duration = Duration.between(start, end);    
    return price * duration.getSeconds() / 3600f;
  }
  
  private void requestLogin(CustomerData data, SessionHolder context) {
    LoginRequest request = new LoginRequest(data.getEmail(), data.getPassword());
    printObject(request);
    LoginResponse response = sendRequest(request, getContext(), LoginResponse.class);
    assertTrue(response.success());
    printObject(response);
  }

  void requestIncidentalParking(CustomerData data, SessionHolder context, LocalDateTime plannedEndTime) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

    // Make the request
    IncidentalParkingRequest request = new IncidentalParkingRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), data.getLotID(), plannedEndTime);
    printObject(request);

    // Run general tests
    requestOnetimeParking(request, context, data, holder);

    // Run type-specific tests
    assertThat(holder.getB(), instanceOf(IncidentalParkingResponse.class));
  }
  
  private void requestReservedParking(CustomerData data, SessionHolder context, LocalDateTime plannedStartTime, LocalDateTime plannedEndTime) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

    // Make the request
    ReservedParkingRequest request = new ReservedParkingRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), data.getLotID(), plannedStartTime, plannedEndTime);
    printObject(request);

    // Run general tests
    requestOnetimeParking(request, context, data, holder);

    // Run type-specific tests
    assertThat(holder.getB(), instanceOf(ReservedParkingResponse.class));
  }
}
