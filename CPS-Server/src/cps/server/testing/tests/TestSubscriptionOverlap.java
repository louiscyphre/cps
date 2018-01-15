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
    db.performAction(conn -> {
      SubscriptionService.create(conn, Constants.SUBSCRIPTION_TYPE_FULL, 1, "NO@email.com", "TL-12-12", 2, starttime,
          endtime, dailyexittime);

      FullSubscriptionRequest over = new FullSubscriptionRequest(1, "NO@email.com", "TL-12-12",
          LocalDate.now().plusDays(1));
      CustomerSession sess = new CustomerSession(new Customer(1, "NO@email.com", "123", 0f, 0f));
      FullSubscriptionResponse serverResponse = new FullSubscriptionResponse();
      ServerResponse response = subc.handle(over, sess, serverResponse, starttime, endtime, dailyexittime);
      assertFalse(response.success());

      SubscriptionService.create(conn, Constants.SUBSCRIPTION_TYPE_REGULAR, 2, "NO@email.com", "TL-13-12", 1, starttime,
          endtime, dailyexittime);
      RegularSubscriptionRequest over2 = new RegularSubscriptionRequest(2, "NO@email.com", "TL-13-12",
          starttime.plusDays(1), 1, dailyexittime);
      sess = new CustomerSession(new Customer(2, "NO@email.com", "123", 0f, 0f));
      RegularSubscriptionResponse serverResponse2 = new RegularSubscriptionResponse();
      response = subc.handle(over2, sess, serverResponse2, starttime.plusDays(2), endtime.plusDays(2), dailyexittime);
      assertFalse(response.success());

      over2 = new RegularSubscriptionRequest(2, "NO@email.com", "TL-13-12", starttime.plusDays(2), 1, dailyexittime);
      response = subc.handle(over2, sess, serverResponse2, starttime.plusDays(2), endtime.plusDays(2), dailyexittime);
       assertTrue(response.success());
    });

  }

}
