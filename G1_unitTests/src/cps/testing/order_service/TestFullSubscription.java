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
public class TestFullSubscription extends ServerControllerTest {
  @Test
  public void testFullSubscription() throws ServerException {
    /*
     * Scenario: 1. Create Parking Lot 2. Send Full Subscription request 3. Send
     * Parking Entry request - license: FullSubscription 4. Send Parking Exit
     * request
     */

    header("testFullSubscription");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    initParkingLot();
    requestFullSubscription(data, getContext());
    requestParkingEntry(data, getContext());
    requestParkingExit(data, getContext());
  }
}