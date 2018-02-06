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
import cps.server.ServerConfig;

import junit.framework.TestCase;
import org.junit.Test;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;

import static cps.common.Utilities.isWeekend;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
public class TestFullSubscription extends ServerControllerTestBase {
  protected CustomerData custData;
  protected ParkingLotData[] lotData = new ParkingLotData[2];
  
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
  public void testFullSubscription() throws ServerException {
    /* Scenario:
     * 1. Create Parking Lot
     * 2. Send Full Subscription request
     * 3. Send Parking Entry request - license: FullSubscription
     * 4. Send Parking Exit request */

    header("testFullSubscription");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
    
    LocalDate startDate = getTime().toLocalDate();

    ParkingLot lot = initParkingLot(lotData[0]);
    float expectedPayment = Constants.PRICE_PER_HOUR_RESERVED * 72f;
    requestFullSubscription(data, getContext(), startDate, expectedPayment);
    enterParking(data, getContext());
    setTime(getTime().plusHours(1));
    requestParkingExit(data, getContext());
  }
  @Test
  public void testMultipleCars() throws ServerException {
    header("testFullSubscription - multiple cars");
    initParkingLot(lotData[0]);
    
    CustomerData data = new CustomerData(0, "company@email", "", "", 1, 0);
    
    LocalDate startDate = getTime().toLocalDate();
    LocalTime dailyExitTime = LocalTime.of(17, 30);
    
    setTime(startDate.atTime(9, 0));
    
    int numSubscriptions = 10;
    String carIDs[] = new String[numSubscriptions];
    int subsIDs[] = new int[numSubscriptions];
    
    for (int i = 0; i < numSubscriptions; i++) {
      carIDs[i] = Utilities.randomString("ILBTA", 2) + "-" + Utilities.randomString("1234567890", 6);
      FullSubscriptionRequest request = new FullSubscriptionRequest(data.getCustomerID(), data.getEmail(), carIDs[i], startDate);
      printObject(request);
      FullSubscriptionResponse response = sendRequest(request, getContext(), FullSubscriptionResponse.class);
      assertNotNull(response);
      printObject(response);
      assertTrue(response.success());
      
      // After the first request, we will be registered as a new user
      // Remember out ID and password
      if (data.getCustomerID() == 0) {
        data.setCustomerID(response.getCustomerID());
        data.setPassword(response.getPassword());
      }
      
      subsIDs[i] = response.getServiceID();
      setTime(getTime().plusMinutes(1));
    }
    
    // Check that we have one customer and 10 subscriptions
    assertEquals(1, db.countEntities("customer"));
    assertEquals(numSubscriptions, db.countEntities("subscription_service"));
    
    // Park with all of them
    setTime(getTime().plusHours(1));
    
    for (int i = 0; i < numSubscriptions; i++) {
      data.setSubsID(subsIDs[i]);
      data.setCarID(carIDs[i]);
      // The request should succeed on regular days, and fail on a weekend, because regular subscriptions are not active on weekends
      enterParking(data, getContext());
      requestParkingExit(data, getContext()); 
      setTime(getTime().plusMinutes(1));     
    }
    
  }
}