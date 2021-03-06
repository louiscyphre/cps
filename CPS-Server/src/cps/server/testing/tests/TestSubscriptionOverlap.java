package cps.server.testing.tests;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import cps.common.Constants;
import cps.entities.models.Customer;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerException;
import cps.server.testing.utilities.ServerControllerTest;

public class TestSubscriptionOverlap extends ServerControllerTest {

  @Test
  public void testSubscriptionOverlap()
      throws ServerException {
    LocalDate starttime = getTime().toLocalDate();
    LocalDate endtime = starttime.plusDays(24);
    LocalTime dailyexittime = LocalTime.of(18, 0, 0);
    Customer mycust = initCustomer();
    ParkingLot lot = initParkingLot();
    ParkingLot lot2 = initParkingLot();
    String[] carid = new String[5];
    carid[0] = "TL-12-12";
    carid[1] = "TL-13-12";
    carid[2] = "TL-14-12";
    carid[3] = "TL-15-12";
    carid[4] = "TL-16-12";

    db.performAction(conn -> {
      SubscriptionService.create(conn, Constants.SUBSCRIPTION_TYPE_FULL, mycust.getId(), mycust.getEmail(), carid[0],
          lot.getId(), starttime, endtime, dailyexittime);
      // Test 1

      assertTrue(SubscriptionService.overlapExists(conn, carid[0], Constants.SUBSCRIPTION_TYPE_FULL, 0,
          starttime.plusDays(1), endtime));

      // Test 2

      assertFalse(SubscriptionService.overlapExists(conn, carid[1], Constants.SUBSCRIPTION_TYPE_FULL, 0,
          starttime.plusDays(1), endtime));

      // Test 3

      assertTrue(SubscriptionService.overlapExists(conn, carid[0], Constants.SUBSCRIPTION_TYPE_REGULAR, lot.getId(),
          starttime.plusDays(1), endtime));

      /* ------------- */

      SubscriptionService.create(conn, Constants.SUBSCRIPTION_TYPE_REGULAR, mycust.getId(), mycust.getEmail(), carid[2],
          lot.getId(), starttime, endtime, dailyexittime);

      // Test 3

      assertTrue(SubscriptionService.overlapExists(conn, carid[2], Constants.SUBSCRIPTION_TYPE_REGULAR, lot.getId(),
          starttime.plusDays(1), endtime));

      // Test 4
      assertFalse(SubscriptionService.overlapExists(conn, carid[3], Constants.SUBSCRIPTION_TYPE_REGULAR, lot.getId(),
          starttime.plusDays(1), endtime));

      // Test 5

      assertFalse(SubscriptionService.overlapExists(conn, carid[2], Constants.SUBSCRIPTION_TYPE_REGULAR, lot2.getId(),
          starttime.plusDays(1), endtime));

      assertFalse(SubscriptionService.overlapExists(conn, carid[2], Constants.SUBSCRIPTION_TYPE_FULL, 0,
          starttime.plusDays(1), endtime));

    });

  }

  protected ParkingLot initParkingLot() throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
    return lot;
  }

  protected Customer initCustomer() throws ServerException {
    return db.performQuery(conn -> Customer.create(conn, "me@.com", "1234"));
  }
}
