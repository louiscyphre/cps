package cps.server.testing.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import cps.server.controllers.CarTransportationControllerA;
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
import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingLot;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.CarTransportationControllerA;
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
    int carstoinsert = 7;
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
    // Begining of a car park
    a[0] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(5));
    // exit in two hours
    a[1] = Timestamp.valueOf(LocalDateTime.now().plusHours(2));
    // exit in seven hours
    a[2] = Timestamp.valueOf(LocalDateTime.now().plusHours(7));
    // exit in 13 hours
    a[3] = Timestamp.valueOf(LocalDateTime.now().plusHours(13));
    // Exit in 18 hours
    a[4] = Timestamp.valueOf(LocalDateTime.now().plusHours(18));
    // Exit in 24 hours
    a[5] = Timestamp.valueOf(LocalDateTime.now().plusHours(24));
    // Exit in 26 hours
    a[6] = Timestamp.valueOf(LocalDateTime.now().plusHours(26));
    // Exit in 30 hours
    a[7] = Timestamp.valueOf(LocalDateTime.now().plusHours(30));
    // Exit in 40 hours
    a[8] = Timestamp.valueOf(LocalDateTime.now().plusHours(40));
    // Exit in 50 hours
    a[9] = Timestamp.valueOf(LocalDateTime.now().plusHours(50));
    CarTransportationControllerA transcontroller = new CarTransportationControllerA(server);
    OnetimeService[][] reservedParkings = new OnetimeService[3][carstoinsert];
    db.performAction(conn -> {
      for (int j = 1, k = 0; j < 4; j++, k++) {
        for (int i = 0; i < carstoinsert; i++) {
          int customerEGO = (int) Math.random() * 500;
          String randomemail = Utilities.randomString("angjurufjfjsl", 7) + "@gmail.com";
          String randomcarID = Utilities.randomString("ILBTA", 2) + "-" + Utilities.randomString("1234567890", 6);
          reservedParkings[k][i] = OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED,
              customerEGO, randomemail, randomcarID, lot.getId(), a[0], a[j], false);
        }
      }

    });

    db.performAction(conn -> {
      for (int j = 2; j >= 0; j--) {
        for (int i = 0; i < carstoinsert; i++) {
          transcontroller.insertCar(conn, lot, reservedParkings[j][i].getCarID(), reservedParkings[j][i].getExitTime());
        }
      }
      /*
      ParkingLot nlot = ParkingLot.findByID(conn, lot.getId());
      ParkingCell[][][] cells = nlot.constructContentArray(conn);
      System.out.println("---- Printing Parking Lot ----");
      System.out.println(cells.toString());*/
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
