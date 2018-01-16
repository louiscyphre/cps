package cps.server.testing.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cps.entities.models.ParkingLot;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.CarTransportationController;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.LotController;
import cps.server.session.SessionHolder;

public class TestReserveOrDisable {
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

  protected ParkingLot initParkingLot() throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
    return lot;
  }
  
}
