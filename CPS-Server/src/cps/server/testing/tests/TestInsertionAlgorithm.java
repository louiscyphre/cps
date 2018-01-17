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
import cps.server.controllers.OnetimeParkingController;
import cps.server.controllers.ParkingEntryController;
import cps.server.database.DatabaseController;
import cps.server.session.CustomerSession;
import cps.server.session.SessionHolder;
import cps.server.testing.utilities.CustomerData;

@SuppressWarnings("unused")
public class TestInsertionAlgorithm {

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
    // Create parking lot Create incidental parking request
    // Insert the car
    int carsInLot1 = 1;
    int carsInLot2 = 1;
    ParkingLot lot[] = new ParkingLot[3];
    lot[0] = initParkingLot();
    lot[1] = db.performQuery(conn -> ParkingLot.create(conn, "Rabin 14", 8, 5, 3, "13.f.t43"));
    lot[2] = db.performQuery(conn -> ParkingLot.create(conn, "Big 16", 12, 5, 3, "14.f.t43"));

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
    OnetimeService[] reservedParkings1 = new OnetimeService[carsInLot1];
    db.performAction(conn -> {
      for (int j = 0; j < carsInLot1; j++) {
        int customerEGO = (int) Math.random() * 500;
        String randomemail = Utilities.randomString("angjurufjfjsl", 7) + "@gmail.com";
        String randomcarID = Utilities.randomString("ILBTA", 2) + "-" + Utilities.randomString("1234567890", 6);
        reservedParkings1[j] = OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, customerEGO,
            randomemail, randomcarID, lot[0].getId(), a[0], a[(int) (Math.random() * 8) + 1]);
      }
    });
    OnetimeService[] reservedParkings2 = new OnetimeService[carsInLot2];
    db.performAction(conn -> {
      for (int j = 0; j < carsInLot2; j++) {
        int customerEGO = (int) Math.random() * 500;
        String randomemail = Utilities.randomString("angjurufjfjsl", 7) + "@gmail.com";
        String randomcarID = Utilities.randomString("ILBTA", 2) + "-" + Utilities.randomString("1234567890", 6);
        reservedParkings2[j] = OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, customerEGO,
            randomemail, randomcarID, lot[1].getId(), a[0], a[(int) (Math.random() * 8) + 1]);
      }
    });
    db.performAction(conn ->

    {
      for (int j = 0; j < carsInLot1; j++) {
        System.out.printf("%s ", reservedParkings1[j].getPlannedEndTime().toString());
        transcontroller.insertCar(conn, lot[0], reservedParkings1[j].getCarID(), reservedParkings1[j].getExitTime());
      }
    });

    db.performAction(conn ->

    {
      for (int j = 0; j < carsInLot2; j++) {
        System.out.printf("%s ", reservedParkings2[j].getPlannedEndTime().toString());
        transcontroller.insertCar(conn, lot[1], reservedParkings2[j].getCarID(), reservedParkings2[j].getExitTime());
      }
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
    a[0] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(10));
    // Main reservation end - will be used for more reservations
    a[1] = Timestamp.valueOf(LocalDateTime.now().minusMinutes(10));
    // Reservation starts before but continues into
    a[2] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(10));
    // Reservation starts after the beginning
    a[3] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(30));
    // Reservation that doesn't overlap start
    a[4] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(5));
    a[5] = Timestamp.valueOf(LocalDateTime.now().plusMinutes(4).plusDays(1));

    OnetimeService[] reservedParkings = new OnetimeService[5];
    db.performAction(conn -> {
      for (int j = 0; j < 5; j++) {
        int customerEGO = (int) Math.random() * 500;
        String randomemail = Utilities.randomString("angjurufjfjsl", 7) + "@gmail.com";
        String randomcarID = Utilities.randomString("ILBTA", 2) + "-" + Utilities.randomString("1234567890", 6);
        reservedParkings[j] = OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, customerEGO,
            randomemail, randomcarID, lot.getId(), a[0], a[(int) (Math.random() * 4) + 1]);
      }
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

  protected Customer initCustomer() throws ServerException {
    return db.performQuery(conn -> Customer.create(conn, "me@.com", "1234"));
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
