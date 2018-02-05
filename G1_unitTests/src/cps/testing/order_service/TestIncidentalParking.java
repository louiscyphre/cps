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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
public class TestIncidentalParking extends ServerControllerTestBase {
  @Test
  public void testIncidentalParking() throws ServerException {
    // Test 1 - Order of incidental parking by new customer (without ID and password)
    /* Scenario:
     * 1. Create Parking Lot
     * 2. Send Incidental Parking request
     * 3. Send Parking Exit request */

    header("testIncidentalParking - new customer");
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    // Create Parking Lot
    initParkingLot();
    
    // Planned time for parking
    LocalDateTime plannedEndTime = getTime().plusHours(8).withNano(0);
    
    // Send Incidental Parking request    
    // After performing this request, the system should register our customer as a new customer
    requestIncidentalParking(data, getContext(), plannedEndTime);
    
    // Check that a new customer ID was created
    assertNotEquals(0, data.customerID);
    
    // Check that a new password was created
    assertNotNull(data.password);
    
    // The password should have a length of 4
    assertEquals(4, data.password.length());
    
    // Send Parking Exit request
    // We will exit on time
    setTime(plannedEndTime);
    ParkingExitResponse response = requestParkingExit(data, getContext());
  } 
  
  void requestIncidentalParking(CustomerData data, SessionHolder context, LocalDateTime plannedEndTime) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

    // Make the request
    IncidentalParkingRequest request = new IncidentalParkingRequest(data.customerID, data.email, data.carID, data.lotID, plannedEndTime);

    // Run general tests
    requestOnetimeParking(request, context, data, holder);

    // Run type-specific tests
    assertThat(holder.getB(), instanceOf(IncidentalParkingResponse.class));
  }
  
  @Test
  public void testDuplicateParking() throws ServerException {
    /*
     * Scenario: 1. Create Parking Lot 2. Send Incidental Parking request 3.
     * Send Parking Entry request - license: IncidentalParking 4. Send Parking
     * Exit request
     */

    header("testDuplicateParking -- incidental");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    initParkingLot();
    requestIncidentalParking(data, getContext());


    ParkingEntryRequest request = new ParkingEntryRequest(data.customerID, data.subsID, data.lotID, data.carID);
    ServerResponse response = server.dispatch(request, getContext());
    assertNotNull(response);
    printObject(response);
    assertFalse(response.success());

    requestParkingExit(data, getContext());
  }
}
