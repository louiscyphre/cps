package cps.testing.order_service;

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
import cps.server.session.SessionHolder;
import cps.server.session.UserSession;
import cps.testing.utilities.CustomerData;
import cps.testing.utilities.OnetimeServiceTestBase;
import cps.testing.utilities.ParkingLotData;
import cps.testing.utilities.ServerControllerTestBase;
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
import org.junit.Before;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
public class TestReservedParking extends OnetimeServiceTestBase {
  
  @Test
  public void testReservedParking() throws ServerException, InterruptedException {
    /* Scenario:
     * 1. Create Parking Lot
     * 2. Send Reserved Parking request
     * 3. Send Parking Entry request - license: ReservedParking
     * 4. Send Parking Exit request */

    header("testReservedParking - new customer");

    // Create Parking Lot
    ParkingLot lot = initParkingLot(lotData[0]);
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    custData = new CustomerData(0, "user@email", "", "IL11-222-33", lot.getId(), 0);
    
    // Define starting and ending times
    LocalDateTime plannedStartTime = getTime().plusHours(3);
    LocalDateTime plannedEndTime = plannedStartTime.plusHours(8);
    
    // Save current state
    int numCustomers = db.countEntities("customer"); // should be 0
    assertEquals(0, numCustomers);
    
    int numServices = db.countEntities("onetime_service"); // should be 0
    assertEquals(0, numServices);
    
    // Try reserving with the start/end times swapped around - should fail
    swappedTimesRequest(custData, plannedStartTime, plannedEndTime);
    
    // Send Reserved Parking request
    float expectedPrice = calculateFlatPayment(plannedStartTime, plannedEndTime, lotData[0].getPrice2());
    requestReservedParking(custData, getContext(), plannedStartTime, plannedEndTime, expectedPrice);

    // The customer should now be registered
    checkRegistered(custData, numCustomers);
    
    // Save current state
    numCustomers = db.countEntities("customer"); // should be 1
    assertEquals(1, numCustomers);
    
    numServices = db.countEntities("onetime_service"); // should be 1
    assertEquals(1, numServices);
    
    // Try entering before the planned starting time
    ParkingEntryResponse response = requestParkingEntry(custData, getContext());
    
    // The attempt should fail
    assertFalse(response.success());
    
    // Enter with the correct starting time
    setTime(plannedStartTime);
    response = requestParkingEntry(custData, getContext());
    
    // This time it should succeed
    assertTrue(response.success());
    
    // Check that we can't enter again with the same reservation
    header("testReservedParking - duplicate entry");
    setTime(plannedStartTime.plusMinutes(10));
    response = requestParkingEntry(custData, getContext());
    
    // The attempt should fail
    assertFalse(response.success());    
    
    // Exit parking at the correct time
    exitParking(custData, plannedEndTime, 0f);
    
    // Now we will try to reserve again, now as a registered customer
    header("testReservedParking - registered customer");
    
    // Since the server currently considered us logged in, we clear out the session context, in order to simulate a clean login
    getContext().setCustomerSession(null);
    
    // Attempt to login again with our email and password
    requestLogin(custData, getContext());
    
    // Repeat the procedure 
    plannedStartTime = getTime().plusHours(3);
    plannedEndTime = plannedStartTime.plusHours(8);
    
    // Save current state parameters
    numCustomers = db.countEntities("customer");
    int previousID = custData.getCustomerID();
    
    // Send Reserved Parking request
    expectedPrice = calculateFlatPayment(plannedStartTime, plannedEndTime, lotData[0].getPrice2());
    requestReservedParking(custData, getContext(), plannedStartTime, plannedEndTime, expectedPrice);
    
    // Check that the customer ID stayed the same --  a new customer was NOT registered
    assertEquals(previousID, custData.getCustomerID());
    
    // Check this in the database too -- number of customers did NOT increase
    assertEquals(numCustomers, db.countEntities("customer"));

    // A new onetime_service record should have been created
    numServices = db.countEntities("onetime_service"); // should be 2
    assertEquals(2, numServices);
    
    // Enter with the correct starting time
    setTime(plannedStartTime);
    response = requestParkingEntry(custData, getContext());
    assertTrue(response.success());
    
    // Exit from parking - exit in time
    exitParking(custData, plannedEndTime, 0f);    
  }
  
  private void swappedTimesRequest(CustomerData data, LocalDateTime plannedStartTime, LocalDateTime plannedEndTime) {
    ReservedParkingRequest request = new ReservedParkingRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), data.getLotID(), plannedEndTime, plannedStartTime);
    printObject(request);
    ReservedParkingResponse response = sendRequest(request, getContext(), ReservedParkingResponse.class);
    assertNotNull(response);
    printObject(response);
    assertFalse(response.success());
  }

  @Test
  public void testReservationOverlap() throws ServerException {
    // Order of incidental parking by existing customer. The incidental parking overlaps a reserved parking.
    header("testReservedParking - overlap with another reserved parking");

    // Create Parking Lot
    ParkingLot lot = initParkingLot(lotData[0]);
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    custData = new CustomerData(0, "user@email", "", "IL11-222-33", lot.getId(), 0);
    
    // Planned time for parking
    LocalDateTime plannedStartTime = getTime().plusHours(2);
    LocalDateTime plannedEndTime = plannedStartTime.plusHours(8);
    
    // Save current state
    int numCustomers = db.countEntities("customer"); // should be 0
    assertEquals(0, numCustomers);
    
    // Send Reserved Parking request
    float expectedPrice = calculateFlatPayment(plannedStartTime, plannedEndTime, lotData[0].getPrice2());
    requestReservedParking(custData, getContext(), plannedStartTime, plannedEndTime, expectedPrice);

    // The customer should now be registered
    checkRegistered(custData, numCustomers);
    
    // Save current state
    numCustomers = db.countEntities("customer"); // should be 1
    assertEquals(1, numCustomers);
    
    int numServices = db.countEntities("onetime_service"); // should be 1
    assertEquals(1, numServices);
    
    // Now try to make a parking reservation that overlaps with the previous parking reservation
    setTime(getTime().plusMinutes(10));
    plannedStartTime = getTime().plusHours(1);
    plannedEndTime = getTime().plusHours(4);
    ReservedParkingRequest request = new ReservedParkingRequest(custData.getCustomerID(), custData.getEmail(), custData.getCarID(), custData.getLotID(), plannedStartTime, plannedEndTime);
    printObject(request);
    ReservedParkingResponse response = sendRequest(request, getContext(), ReservedParkingResponse.class);
    printObject(response);
    
    // The response should be a failure
    assertFalse(response.success());
    
    // Test that no new parking service was added in the database
    assertEquals(numServices, db.countEntities("onetime_service"));
  }
}
