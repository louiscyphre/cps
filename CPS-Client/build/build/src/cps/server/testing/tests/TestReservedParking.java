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
public class TestReservedParking extends ServerControllerTest {
  @Test
  public void testReservedParking() throws ServerException, InterruptedException {
    /*
     * Scenario: 1. Create Parking Lot 2. Send Reserved Parking request 3. Send
     * Parking Entry request - license: ReservedParking 4. Send Parking Exit
     * request
     */

    header("testReservedParking");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    initParkingLot();
    requestReservedParking(data, getContext());
    Thread.sleep(1500); // TODO Cauchy find a better way to adjust time
    
    requestParkingEntry(data, getContext());
    requestParkingExit(data, getContext());
  }
  
  @Test
  public void testDuplicateParking() throws ServerException, InterruptedException {
    /*
     * Scenario: 1. Create Parking Lot 2. Send Incidental Parking request 3.
     * Send Parking Entry request - license: IncidentalParking 4. Send Parking
     * Exit request
     */

    header("testDuplicateParking -- reserved");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    initParkingLot();
    requestReservedParking(data, getContext());
    Thread.sleep(1500);
    
    requestParkingEntry(data, getContext());

    ParkingEntryRequest request = new ParkingEntryRequest(data.customerID, data.subsID, data.lotID, data.carID);
    ServerResponse response = server.dispatch(request, getContext());
    assertNotNull(response);
    printObject(response);
    assertFalse(response.success());

    requestParkingExit(data, getContext());
  }
}
