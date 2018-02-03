package cps.server.testing.tests;

import static cps.common.Utilities.isWeekend;

import org.junit.Test;

import cps.server.ServerException;
import cps.server.testing.utilities.CustomerData;
import cps.server.testing.utilities.ServerControllerTest;

public class TestRegularSubscription extends ServerControllerTest {
  @Test
  public void testRegularSubscription() throws ServerException {
    /*
     * Scenario: 1. Create Parking Lot 2. Send Full Subscription request 3. Send
     * Parking Entry request - license: FullSubscription 4. Send Parking Exit
     * request
     */

    header("testRegularSubscription");
    CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

    initParkingLot();
    requestRegularSubscription(data, getContext());
    if (requestParkingEntry(data, getContext(), isWeekend(getClock().now().getDayOfWeek()))) {
      requestParkingExit(data, getContext());
    }
  }
}