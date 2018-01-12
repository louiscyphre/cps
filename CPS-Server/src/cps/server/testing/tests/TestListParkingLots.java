package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
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
public class TestListParkingLots extends ServerControllerTest {
  @Test
  public void testListParkingLots() {
    header("testListParkingLots");

    // Create lots
    for (int i = 1; i <= 3; i++) {
      InitLotAction request = new InitLotAction(1000, "Lot " + i + " Address", 3, 5, 4, "113.0.1.1" + i);
      ServerResponse response = server.dispatch(request, getContext());
      assertTrue(response.success());
    }

    assertEquals(3, db.countEntities("parking_lot"));

    // Make request
    ListParkingLotsRequest request = new ListParkingLotsRequest();

    // Test response
    ServerResponse response = server.dispatch(request, getContext());
    assertNotNull(response);
    printObject(response);
    assertTrue(response.success());
    assertThat(response, instanceOf(ListParkingLotsResponse.class));

    ListParkingLotsResponse specificResponse = (ListParkingLotsResponse) response;
    Collection<ParkingLot> parkingLots = specificResponse.getData();
    assertNotNull(parkingLots);
    assertEquals(3, parkingLots.size());
  }
}
