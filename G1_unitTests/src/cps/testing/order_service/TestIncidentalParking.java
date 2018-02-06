package cps.testing.order_service;

import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.Test;

import cps.api.request.IncidentalParkingRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.entities.models.ParkingLot;
import cps.server.ServerException;
import cps.testing.utilities.OnetimeServiceTestBase;

public class TestIncidentalParking extends OnetimeServiceTestBase {  
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
    int numCustomers = db.countEntities("customer"); // should be 0
    assertEquals(0, numCustomers);

    int numServices = db.countEntities("onetime_service"); // should be 0
    assertEquals(0, numServices);
    
    requestIncidentalParking(custData, getContext(), plannedEndTime);    
    checkRegistered(custData, numCustomers);

    // A new onetime_service record should have been created
    numServices = db.countEntities("onetime_service"); // should be 1
    assertEquals(1, numServices);
    
    // Exit from parking - exit in time
    float expectedPrice = calculateFlatPayment(startTime, plannedEndTime, lotData[0].getPrice1());
    exitParking(custData, plannedEndTime, expectedPrice);
    
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

    // A new onetime_service record should have been created
    numServices = db.countEntities("onetime_service"); // should be 2
    assertEquals(2, numServices);
    
    // Exit from parking - exit in time
    expectedPrice = calculateFlatPayment(startTime, plannedEndTime, lotData[0].getPrice1());
    exitParking(custData, plannedEndTime, expectedPrice);
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
    float expectedPrice = calculateFlatPayment(plannedStartTime, plannedEndTime, lotData[0].getPrice2());
    requestReservedParking(custData, getContext(), plannedStartTime, plannedEndTime, expectedPrice);

    // The customer should now be registered
    checkRegistered(custData, numCustomers);
    
    // Save current state
    numCustomers = db.countEntities("customer"); // should be 1
    assertEquals(1, numCustomers);
    
    int numServices = db.countEntities("onetime_service"); // should be 1
    assertEquals(1, numServices);
    
    // Now try to order an incidental parking that overlaps with the previous parking reservation
    setTime(getTime().plusMinutes(10));
    plannedEndTime = plannedStartTime.plusHours(1);
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
}
