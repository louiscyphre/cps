package cps.testing.order_service;

import static cps.common.Utilities.isWeekend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import cps.api.request.RegularSubscriptionRequest;
import cps.api.response.RegularSubscriptionResponse;
import cps.common.Utilities;
import cps.server.ServerException;
import cps.testing.utilities.CustomerData;
import cps.testing.utilities.ParkingLotData;
import cps.testing.utilities.ServerControllerTestBase;

public class TestRegularSubscription extends ServerControllerTestBase {
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
  public void testRegularSubscription() throws ServerException {
    /* Scenario:
     * 1. Create Parking Lot
     * 2. Send Regular Subscription request
     * 3. Send Parking Entry request - license: RegularSubscription
     * 4. Send Parking Exit request */

    header("testRegularSubscription - single car");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    initParkingLot(lotData[0]);
    LocalDate startDate = getTime().toLocalDate();
    LocalTime dailyExitTime = LocalTime.of(17, 30);
    requestRegularSubscription(data, getContext(), startDate, dailyExitTime);
    // TODO check payment
    
    // The request should succeed on regular days, and fail on a weekend, because regular subscriptions are not active on weekends
    if (requestParkingEntry(data, getContext(), isWeekend(LocalDateTime.now().getDayOfWeek()))) {
      setTime(startDate.atTime(dailyExitTime));
      exitParking(data, getContext());
    }
  }
  
  @Test
  public void testMultipleCars() throws ServerException {
    header("testRegularSubscription - multiple cars");
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
      RegularSubscriptionRequest request = new RegularSubscriptionRequest(data.getCustomerID(), data.getEmail(), carIDs[i], startDate, data.getLotID(), dailyExitTime);
      printObject(request);
      RegularSubscriptionResponse response = sendRequest(request, getContext(), RegularSubscriptionResponse.class);
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
    for (int i = 0; i < numSubscriptions; i++) {
      data.setSubsID(subsIDs[i]);
      data.setCarID(carIDs[i]);
      // The request should succeed on regular days, and fail on a weekend, because regular subscriptions are not active on weekends
      if (requestParkingEntry(data, getContext(), isWeekend(LocalDateTime.now().getDayOfWeek()))) {
        exitParking(data, getContext());
      }      
    }
  }
}