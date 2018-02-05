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
import cps.server.session.UserSession;
import cps.testing.utilities.CustomerData;
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
public class TestReservedParking extends ServerControllerTestBase {
  CustomerData custData;
  ParkingLotData lotData;
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    custData = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
    
    // Setup Parking Lot data
    lotData = new ParkingLotData(0, "Sesame, 1", 4, 5f, 4f, "1.0.0.1");
  }
  @Test
  public void testReservedParking() throws ServerException, InterruptedException {
    /* Scenario:
     * 1. Create Parking Lot
     * 2. Send Reserved Parking request
     * 3. Send Parking Entry request - license: ReservedParking
     * 4. Send Parking Exit request */

    header("testReservedParking");

    initParkingLot(lotData);
    requestReservedParking(custData, getContext());
    setTime(getTime().plusSeconds(3));
    
    requestParkingEntry(custData, getContext());
    requestParkingExit(custData, getContext());
    
    // Check that we can't enter again with the same reservation
    header("testReservedParking - duplicate entry");
  }
  
  @Test
  public void testDuplicateParking() throws ServerException, InterruptedException {
    /* Scenario:
     * 1. Create Parking Lot
     * 2. Send Incidental Parking request
     * 3. Send Parking Entry request - license: IncidentalParking
     * 4. Send Parking Exit request */

    header("testDuplicateParking -- reserved");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    initParkingLot(lotData);
    requestReservedParking(data, getContext());
    setTime(getTime().plusSeconds(3));
    
    requestParkingEntry(data, getContext());

    ParkingEntryRequest request = new ParkingEntryRequest(data.getCustomerID(), data.getSubsID(), data.getLotID(), data.getCarID());
    ServerResponse response = server.dispatch(request, getContext());
    assertNotNull(response);
    printObject(response);
    assertFalse(response.success());

    requestParkingExit(data, getContext());
  }
}
