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
import cps.entities.models.ParkingLot;
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

    // Create lot
    ParkingLot lot = initParkingLot(lotData[0]);
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", lot.getId(), 0);
    
    LocalDate startDate = getTime().toLocalDate();
    LocalTime dailyExitTime = LocalTime.of(17, 30);

    float expectedPayment = lotData[0].getPrice2() * 60f;
    requestRegularSubscription(data, getContext(), startDate, dailyExitTime, expectedPayment);
    
    // The request should succeed on regular days, and fail on a weekend, because regular subscriptions are not active on weekends
    if (requestParkingEntry(data, getContext(), isWeekend(LocalDateTime.now().getDayOfWeek()))) {
      setTime(startDate.atTime(dailyExitTime));
      exitParking(data, getContext());
    }
  }
  
  @Test
  public void testMultipleCars() throws ServerException {
    header("testRegularSubscription - multiple cars");

    // Create lot
    ParkingLot lot = initParkingLot(lotData[0]);
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", lot.getId(), 0);
    
    LocalDate startDate = getTime().toLocalDate();
    LocalTime dailyExitTime = LocalTime.of(17, 30);
    
    setTime(startDate.atTime(9, 0));
    
    int numCars = 10;
    String carIDs[] = new String[numCars];
    
    for (int i = 0; i < numCars; i++) {
      carIDs[i] = Utilities.randomString("ILBTA", 2) + "-" + Utilities.randomString("1234567890", 6);
    }
    

    float expectedPayment = lotData[0].getPrice2() * 54f * numCars;
    RegularSubscriptionRequest request = new RegularSubscriptionRequest(data.getCustomerID(), data.getEmail(), carIDs, startDate, data.getLotID(), dailyExitTime);
    printObject(request);
    RegularSubscriptionResponse response = sendRequest(request, getContext(), RegularSubscriptionResponse.class);
    assertNotNull(response);
    printObject(response);
    assertTrue(response.success());
    assertEquals(expectedPayment, response.getPayment());
    
    // After the first request, we will be registered as a new user
    // Remember out ID and password
    if (data.getCustomerID() == 0) {
      data.setCustomerID(response.getCustomerID());
      data.setPassword(response.getPassword());
    }
    
    int subsIDs[] = response.getSubscriptionIDs();
    assertNotNull(subsIDs);
    assertEquals(numCars, subsIDs.length);
    
    // Check that we have one customer and 10 subscriptions
    assertEquals(1, db.countEntities("customer"));
    assertEquals(numCars, db.countEntities("subscription_service"));
    
    // Park with all of them
    setTime(getTime().plusMinutes(10));
    
    // Park with all of them
    for (int i = 0; i < numCars; i++) {
      data.setSubsID(subsIDs[i]);
      data.setCarID(carIDs[i]);
      // The request should succeed on regular days, and fail on a weekend, because regular subscriptions are not active on weekends
      if (requestParkingEntry(data, getContext(), isWeekend(LocalDateTime.now().getDayOfWeek()))) {
        exitParking(data, getContext());
      }      
    }
  }
}