package cps.server.testing.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import cps.api.action.InitLotAction;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.common.Utilities;
import cps.common.Utilities.Pair;
import cps.entities.models.CarTransportation;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.OnetimeParkingController;
import cps.server.controllers.ParkingEntryController;
import cps.server.session.CustomerSession;
import cps.server.session.SessionHolder;
import cps.server.testing.utilities.CustomerData;

@SuppressWarnings("unused")
public class TegraTests {

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
  public void testInsertCars() throws ServerException {
    int parkingRequestsNo = 5;
    // Create parking lot Create incidental parking request
    // Insert the car
    ParkingLot lot = initParkingLot();
    CustomerData data = new CustomerData((int) Math.random() * 1000,
        Utilities.randomString("abcdefghijklmnopqrstuvwxyz", 8), Utilities.randomString("1234567890", 4),
        Utilities.randomString("1234567890ABCDTRUOTSKL", 7), 1, 0);
    /*
     * OnetimeService[] incidentalParkingRequests = new
     * OnetimeService[parkingRequestsNo]; for (int i = 0; i < parkingRequestsNo;
     * i++) { incidentalParkingRequests[i] = newIncidentalParking(lot.getId());
     * }
     */
    /*
     * CarTransportation[] req = new CarTransportation[parkingRequestsNo]; for
     * (int i = 0; i < parkingRequestsNo; i++) { req[i] =
     * newParkingEntry(incidentalParkingRequests[i]);
     */

    Timestamp[] a = new Timestamp[10];
    a[0] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(5));
    a[1] = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
    a[2] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(10));
    a[3] = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
    OnetimeService[] reservedParkings = new OnetimeService[5];
    db.performAction(conn -> {
      reservedParkings[0] = OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, 3,
          "no@email.com", "123-sdf", lot.getId(), a[0], a[1], false);
    });

  }

  @Test
  public void testOverlap() throws ServerException {
    // Create parking lot Create incidental parking request
    // Insert the car
    ParkingLot lot = initParkingLot();
    CustomerData data = new CustomerData((int) Math.random() * 1000,
        Utilities.randomString("abcdefghijklmnopqrstuvwxyz", 8), Utilities.randomString("1234567890", 4),
        Utilities.randomString("1234567890ABCDTRUOTSKL", 7), 1, 0);

    Timestamp[] a = new Timestamp[10];
    // Main reservation start
    a[0] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(20));
    // Main reservation end - will be used for more reservations
    a[1] = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
    // Reservation starts before but continues into
    a[2] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(10));
    // Reservation starts after the beginning
    a[3] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(30));
    // Reservation that doesn't overlap start
    a[4] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(5));
    a[5] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(4).plusDays(1));

    OnetimeService[] reservedParkings = new OnetimeService[5];
    db.performAction(conn -> {
      reservedParkings[0] = OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, 3,
          "no@email.com", "123-sdf", lot.getId(), a[0], a[1], false);
      assertTrue(0 < OnetimeService.findForOverlap(conn, "123-sdf", a[2], a[1]).size());
      assertTrue(0 < OnetimeService.findForOverlap(conn, "123-sdf", a[3], a[1]).size());
      assertTrue(0 == OnetimeService.findForOverlap(conn, "123-sdf", a[4], a[2]).size());
      assertTrue(0 < OnetimeService.findForOverlap(conn, "123-sdf", a[4], a[5]).size());
    });

  }

  private CarTransportation newParkingEntry(OnetimeService _cdata) {
    ParkingEntryRequest request = new ParkingEntryRequest(_cdata.getCustomerID(), 0, _cdata.getLotID(),
        _cdata.getCarID());
    ParkingEntryResponse respo = (ParkingEntryResponse) server.dispatch(request, context);
    assertNotNull(respo);
    assertTrue(ServerResponse.STATUS_OK == respo.getStatus());
    return new CarTransportation(respo.getCustomerID(), _cdata.getCarID(), Constants.LICENSE_TYPE_ONETIME,
        _cdata.getId(), _cdata.getLotID(), Timestamp.valueOf(LocalDateTime.now()), null);
  }

  private OnetimeService newRandomIncidentalParking(int lotId) {
    LocalDateTime endTime = LocalDateTime.now().plusHours((long) (Math.random() * 12) + 2)
        .plusMinutes((long) Math.random() * 55);
    IncidentalParkingRequest request = new IncidentalParkingRequest((int) Math.random() * 500,
        Utilities.randomString("angjurufjfjsl@", 10), Utilities.randomString("ILBTA1234567890-", 7), lotId, endTime);
    IncidentalParkingResponse response = (IncidentalParkingResponse) server.dispatch(request, context);
    assertNotNull(response);
    assertTrue(response.getStatus() == ServerResponse.STATUS_OK);
    return new OnetimeService(response.getServiceID(), 1, response.getCustomerID(), request.getEmail(),
        request.getCarID(), lotId, Timestamp.valueOf(LocalDateTime.now()),
        Timestamp.valueOf(request.getPlannedEndTime()), false);
  }

  protected ParkingLot initParkingLot() throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
    return lot;
  }

  /*
   * @Test public void testRetrieveCar() { fail("Not yet implemented"); }
   * @Test public void testHandleInitLotAction() { fail("Not yet implemented");
   * }
   * @Test public void testHandleUpdatePricesAction() {
   * fail("Not yet implemented"); }
   * @Test public void testHandleSetFullLotAction() {
   * fail("Not yet implemented"); }
   */
}
