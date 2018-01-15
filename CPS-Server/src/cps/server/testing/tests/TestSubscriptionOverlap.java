package cps.server.testing.tests;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import com.sun.swing.internal.plaf.basic.resources.basic_de;

import cps.api.request.FullSubscriptionRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.request.SubscriptionRequest;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.ResponseHandler;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.entities.models.Customer;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.SubscriptionController;
import cps.server.session.CustomerSession;
import cps.server.session.SessionHolder;

public class TestSubscriptionOverlap {

  ServerController   server;
  DatabaseController db;
  SessionHolder      context = new SessionHolder();

  @Before
  public void setUp() throws Exception {
    this.server = new ServerController(ServerConfig.testing());
    this.db = server.getDatabaseController();
    db.truncateTables();
  }

  @Test
  public void testHandleSubscriptionRequestCustomerSessionSubscriptionResponseLocalDateLocalDateLocalTime()
      throws ServerException {
    SubscriptionController subc = server.getSubscriptionController();
    LocalDate starttime = LocalDate.now();
    LocalDate endtime = LocalDate.now().plusDays(24);
    LocalTime dailyexittime = LocalTime.of(18, 0, 0);
    Customer mycust = initCustomer();
    ParkingLot lot = initParkingLot();
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

      assertTrue(SubscriptionService.OverlapExists(conn, carid[0], Constants.SUBSCRIPTION_TYPE_FULL, 0,
          starttime.plusDays(1), endtime));

      // Test 2

      assertFalse(SubscriptionService.OverlapExists(conn, carid[1], Constants.SUBSCRIPTION_TYPE_FULL, 0,
          starttime.plusDays(1), endtime));

      /* ------------- */

      SubscriptionService.create(conn, Constants.SUBSCRIPTION_TYPE_REGULAR, mycust.getId(), mycust.getEmail(), carid[2],
          lot.getId(), starttime, endtime, dailyexittime);

      // Test 3

      assertTrue(SubscriptionService.OverlapExists(conn, carid[2], Constants.SUBSCRIPTION_TYPE_REGULAR, lot.getId(),
          starttime.plusDays(1), endtime));

      // Test 4

      // assertTrue(response.success());
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
