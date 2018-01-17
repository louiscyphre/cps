package cps.server.testing.tests;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import cps.api.action.ParkingCellSetDisabledAction;
import cps.api.action.ParkingCellSetReservedAction;
import cps.api.response.ServerResponse;
import cps.entities.models.ParkingLot;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.CarTransportationController;
import cps.server.database.DatabaseController;
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

  @Test
  public void testReserveOrDisable() throws ServerException {
    ParkingLot lot = initParkingLot();
    String carId = "IL-12-12";
    LocalDateTime exitTime = LocalDateTime.now();
    
    CarTransportationController transportationController = server.getTransportationController();
    
    db.performAction(conn -> {
      transportationController.insertCar(conn, lot, carId, exitTime.plusMinutes(50));
    });
    
    ParkingCellSetDisabledAction disableAction = new ParkingCellSetDisabledAction(1, 1, 0, 2, 2);
    ServiceSession session = context.acquireServiceSession();
    session.login("sarit", "1234");
    
    ServerResponse res = server.handle(disableAction, context);
    
    assertFalse(res.success());
    
    
    disableAction = new ParkingCellSetDisabledAction(1, 1, 0, 2, 1);
    res = server.handle(disableAction, context);
    assertTrue(res.success());
    ParkingCellSetReservedAction reserveAction = new ParkingCellSetReservedAction(1, 1, 0, 2, 2);
    res = server.handle(reserveAction, context);
    assertFalse(res.success());
    reserveAction = new ParkingCellSetReservedAction(1, 1, 0, 2, 0);
    res = server.handle(reserveAction, context);
    assertTrue(res.success());
  }

  protected ParkingLot initParkingLot() throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
    return lot;
  }

}
