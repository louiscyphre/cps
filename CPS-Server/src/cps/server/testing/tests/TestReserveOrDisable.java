package cps.server.testing.tests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.sun.corba.se.impl.protocol.InfoOnlyServantCacheLocalCRDImpl;

import cps.api.action.DisableParkingSlotsAction;
import cps.api.action.ReserveParkingSlotsAction;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.ServerResponse;
import cps.entities.models.ParkingLot;
import cps.entities.models.ParkingCell.ParkingCellVisitor;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.CarTransportationController;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.LotController;
import cps.server.session.ServiceSession;
import cps.server.session.SessionHolder;
import cps.server.testing.utilities.ServerControllerTest;

public class TestReserveOrDisable extends ServerControllerTest {
  ServerController   server;
  DatabaseController db;
  SessionHolder      context = new SessionHolder();

  @Before
  public void setUp() throws Exception {
    this.server = new ServerController(ServerConfig.testing());
    this.db = server.getDatabaseController();
    db.truncateTables();
  }

//  @Test
//  public void testReserveOrDisable() throws ServerException {
//    ParkingLot lot=initParkingLot();
//    LotController lotcontroller=server.getLotController();
//    CarTransportationController transportationController = server.getTransportationController();
//    db.performQuery(query);
//    
//    transportationController.insertCar(
//    //lotcontroller.reserveOrDisable(session, serverResponse, lotID, i, j, k, visitor, successMessage)
//    fail("Not yet implemented");
//  }
  @Test
  public void testReserveOrDisable() throws ServerException {
    ParkingLot lot = initParkingLot();
    String carId = "IL-12-12";
    LocalDateTime exitTime = LocalDateTime.now();
    LotController lotcontroller = server.getLotController();
    
    CarTransportationController transportationController = server.getTransportationController();
    
    db.performAction(conn -> {
      transportationController.insertCar(conn, lot, carId, exitTime.plusMinutes(50));
    });
    
    DisableParkingSlotsAction disableAction = new DisableParkingSlotsAction(1, 1, 0, 2, 2);
    ServerResponse res = server.handle(disableAction, context);
    
    assertFalse(res.success());
    
    
    disableAction = new DisableParkingSlotsAction(1, 1, 0, 2, 1);
    res = server.handle(disableAction, context);
    assertTrue(res.success());
    ReserveParkingSlotsAction reserveAction = new ReserveParkingSlotsAction(1, 1, 0, 2, 2);
    res = server.handle(reserveAction, context);
    assertFalse(res.success());
    reserveAction = new ReserveParkingSlotsAction(1, 1, 0, 2, 0);
    res = server.handle(reserveAction, context);
    assertTrue(res.success());
  }

  protected ParkingLot initParkingLot() throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
    return lot;
  }

}
