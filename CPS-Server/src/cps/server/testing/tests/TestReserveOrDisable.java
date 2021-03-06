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
    LocalDateTime exitTime = getTime();
    
    CarTransportationController transportationController = server.getTransportationController();
    
    db.performAction(conn -> {
      transportationController.insertCar(conn, lot, carId, exitTime.plusMinutes(50));
    });
    
    int i = 3, j = 2, k = 2;
    
    ServiceSession session = getContext().acquireServiceSession();
    session.login("sarit", "1234");
    
    ParkingCellSetDisabledAction disableAction = new ParkingCellSetDisabledAction(1, 1, i, j, k);
    printObject(disableAction);    
    ServerResponse res = server.handle(disableAction, getContext());
    printObject(res);
    assertFalse(res.success());
    
    disableAction = new ParkingCellSetDisabledAction(1, 1, i, j, 1);
    printObject(disableAction);
    res = server.handle(disableAction, getContext());
    printObject(res);
    assertTrue(res.success());
    
    disableAction = new ParkingCellSetDisabledAction(1, 1, i, j, 1, false);
    printObject(disableAction);
    res = server.handle(disableAction, getContext());
    printObject(res);
    assertTrue(res.success());
    
    ParkingCellSetReservedAction reserveAction = new ParkingCellSetReservedAction(1, 1, i, j, k);
    printObject(reserveAction);
    res = server.handle(reserveAction, getContext());
    printObject(res);
    assertFalse(res.success());
    
    reserveAction = new ParkingCellSetReservedAction(1, 1, i, j, 0);
    printObject(reserveAction);
    res = server.handle(reserveAction, getContext());
    printObject(res);
    assertTrue(res.success());
  }

  protected ParkingLot initParkingLot() throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
    return lot;
  }

}
