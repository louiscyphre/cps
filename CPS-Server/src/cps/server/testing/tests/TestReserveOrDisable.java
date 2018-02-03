package cps.server.testing.tests;

import java.time.LocalDateTime;

import org.junit.Test;

import cps.api.action.ParkingCellSetDisabledAction;
import cps.api.action.ParkingCellSetReservedAction;
import cps.api.response.ServerResponse;
import cps.entities.models.ParkingLot;
import cps.server.ServerException;
import cps.server.controllers.CarTransportationController;
import cps.server.session.ServiceSession;
import cps.server.testing.utilities.ServerControllerTest;

public class TestReserveOrDisable extends ServerControllerTest {

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
    ServiceSession session = getContext().acquireServiceSession();
    session.login("sarit", "1234");
    
    ServerResponse res = server.handle(disableAction, getContext());
    
    assertFalse(res.success());
    
    
    disableAction = new ParkingCellSetDisabledAction(1, 1, 0, 2, 1);
    res = server.handle(disableAction, getContext());
    assertTrue(res.success());
    ParkingCellSetReservedAction reserveAction = new ParkingCellSetReservedAction(1, 1, 0, 2, 2);
    res = server.handle(reserveAction, getContext());
    assertFalse(res.success());
    reserveAction = new ParkingCellSetReservedAction(1, 1, 0, 2, 0);
    res = server.handle(reserveAction, getContext());
    assertTrue(res.success());
  }

  protected ParkingLot initParkingLot() throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
    return lot;
  }

}
