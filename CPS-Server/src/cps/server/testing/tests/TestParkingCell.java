package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Test;

import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingLot;
import cps.server.ServerException;
import cps.server.testing.utilities.ServerControllerTest;

@SuppressWarnings("unused")
public class TestParkingCell extends ServerControllerTest {

  @Test
  public void testParkingCell() throws ServerException {
    header("testParkingCell");
    
    int lotID = 1;
    int i = 0, j = 0, k = 1;
    
    // Create cell
    ParkingCell cell = db.performQuery(conn -> ParkingCell.create(conn, lotID, i, j, k, "IL11-222-33", Timestamp.valueOf(getTime()), false, false));
    assertNotNull(cell);
    printObject(cell);
    
    // Update cell
    cell.setCarID(null);
    cell.setPlannedExitTime(null);
    cell.setReserved(true);
    cell.setDisabled(true);
    db.performAction(conn -> cell.update(conn));
    
    // Find cell
    ParkingCell result = db.performQuery(conn -> ParkingCell.find(conn, lotID, i, j, k));
    assertNotNull(result);
    printObject(result);
    assertEquals(cell.lotID, result.lotID);
    assertEquals(cell.width, result.width);
    assertEquals(cell.height, result.height);
    assertEquals(cell.depth, result.depth);
    assertEquals(cell.getCarID(), result.getCarID());
    assertEquals(cell.isReserved(), result.isReserved());
    assertEquals(cell.isDisabled(), result.isDisabled());
  }
  
  @Test
  public void testAssociatedActions() throws ServerException {
    header("testAssociatedActions");
    
    // Create lot
    ParkingLot lot = initParkingLot();
    assertNotNull(lot);
    
    // Test database result
    assertEquals(lot.getVolume(), db.countEntities("parking_cell"));
    
    ParkingCell[][][] content = db.performQuery(conn -> lot.constructContentArray(conn));
    assertNotNull(content);    

    for (ParkingCell[][] i: content) {
      for (ParkingCell[] j: i) {
        for (ParkingCell cell: j) {
          printObject(cell);          
        }
      }
    }
    
    int numFreeCells = db.performQuery(conn -> lot.countFreeCells(conn));
    assertEquals(lot.getVolume(), numFreeCells);
    
    String newCarID = "IL11-222-33";
    
    int targetI = 1, targetJ = 1, targetK = 1;
    content[targetI][targetJ][targetK].setCarID(newCarID);
    
    db.performAction(conn -> lot.updateContent(conn, content));
    
    ParkingCell[][][] newContent = db.performQuery(conn -> lot.constructContentArray(conn));
    
    boolean found = false;
    int locI = -1, locJ = -1, locK = -1;
    
    for (int i = 0; i < newContent.length && !found; i++) {
      for (int j = 0; j < newContent[i].length && !found; j++) {
        for (int k = 0; k < newContent[i][j].length && !found; k++) {
          if (newContent[i][j][k].contains(newCarID)) {
            locI = i;
            locJ = j;
            locK = k;
            found = true;
            printObject(newContent[i][j][k]);
          }
        }
      }
    }
    
    assertTrue(found);
    assertEquals(targetI, locI);
    assertEquals(targetJ, locJ);
    assertEquals(targetK, locK);
  }
}
