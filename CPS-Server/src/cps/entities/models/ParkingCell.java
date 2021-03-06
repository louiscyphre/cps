package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;

import cps.common.Constants;
import cps.server.ServerException;

/** Represents a parking cell as a data object that can be sent through the network and persisted in the database.
 * A parking cell is the single unit of parking inside of a parking lot.
 * One parking cell contains one car. */
public class ParkingCell implements Serializable {
  private static final long serialVersionUID = 1L;

  public final int  lotID;
  public final int  width;
  public final int  height;
  public final int  depth;
  private String    carID;
  private Timestamp plannedExitTime;
  private boolean   reserved;
  private boolean   disabled;

  /** Instantiates a new parking cell.
   * @param lotID the lot ID
   * @param width the width coordinate
   * @param height the height coordinate
   * @param depth the depth coordinate
   * @param carID the car ID
   * @param plannedExitTime the planned exit time of the car
   * @param reserved the reserved flag
   * @param disabled the disabled flag */
  public ParkingCell(int lotID, int width, int height, int depth, String carID, Timestamp plannedExitTime, boolean reserved, boolean disabled) {
    this.lotID = lotID;
    this.width = width;
    this.height = height;
    this.depth = depth;
    this.carID = carID;
    this.plannedExitTime = plannedExitTime;
    this.reserved = reserved;
    this.disabled = disabled;
  }

  /** Instantiates a new parking cell from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException on error */
  public ParkingCell(ResultSet rs) throws SQLException {
    this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getTimestamp(6), rs.getBoolean(7), rs.getBoolean(8));
  }

  public String getCarID() {
    return carID;
  }

  public void setCarID(String carID) {
    this.carID = carID;
  }

  public Timestamp getPlannedExitTime() {
    return plannedExitTime;
  }

  public void setPlannedExitTime(Timestamp plannedExitTime) {
    this.plannedExitTime = plannedExitTime;
  }

  public boolean isReserved() {
    return reserved;
  }

  public void setReserved(boolean reserved) {
    this.reserved = reserved;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean isFree() {
    return carID == null && !reserved && !disabled;
  }

  public void clear() {
    setCarID(null);
    setPlannedExitTime(null);
    setReserved(false);
    setDisabled(false);
  }

  public boolean containsCar() {
    return carID != null;
  }

  public boolean contains(String otherCarID) {
    return carID != null && carID.equals(otherCarID);
  }

  /** Creates the.
   * @param conn the SQL connection
   * @param lotID the lot ID
   * @param width the width coordinate
   * @param height the height coordinate
   * @param depth the depth coordinate
   * @param carID the car ID
   * @param plannedExitTime the planned exit time of the car
   * @param reserved the reserved flag
   * @param disabled the disabled flag
   * @return the parking cell
   * @throws SQLException on error
   * @throws ServerException on error */
  public static ParkingCell create(Connection conn, int lotID, int width, int height, int depth, String carID, Timestamp plannedExitTime, boolean reserved,
      boolean disabled) throws SQLException, ServerException {
    // Create SQL statement
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_PARKING_CELL);

    // Fill in the fields of the SQL statement
    int field = 1;
    statement.setInt(field++, lotID);
    statement.setInt(field++, width);
    statement.setInt(field++, height);
    statement.setInt(field++, depth);
    statement.setString(field++, carID);
    statement.setTimestamp(field++, plannedExitTime);
    statement.setBoolean(field++, reserved);
    statement.setBoolean(field++, disabled);

    // Execute SQL query
    if (statement.executeUpdate() < 1) {
      throw new ServerException("Failed to create ParkingCell");
    }

    statement.close();

    return new ParkingCell(lotID, width, height, depth, carID, plannedExitTime, reserved, disabled);
  }

  public static void createArray(Connection conn, int lotID, int sizeI, int sizeJ, int sizeK) throws SQLException, ServerException {
    String query = "INSERT INTO parking_cell VALUES"
        + String.join(", ", Collections.nCopies(sizeI * sizeJ * sizeK, "(?, ?, ?, ?, default, default, default, default)"));
    
    // Create SQL statement
    PreparedStatement statement = conn.prepareStatement(query);

    // Fill in the fields of the SQL statement

    int field = 1;

    for (int i = 0; i < sizeI; i++) {
      for (int j = 0; j < sizeJ; j++) {
        for (int k = 0; k < sizeK; k++) {
          statement.setInt(field++, lotID);
          statement.setInt(field++, i);
          statement.setInt(field++, j);
          statement.setInt(field++, k);
        }
      }
    }

    // Execute SQL query
    if (statement.executeUpdate() < 1) {
      throw new ServerException("Failed to create ParkingCell array");
    }

    statement.close();
  }

  /** Find.
   * @param conn the SQL connection
   * @param lotID the lot ID
   * @param i the I coordinate
   * @param j the J coordinate
   * @param k the K coordinate
   * @return the parking cell
   * @throws SQLException on error */
  public static ParkingCell find(Connection conn, int lotID, int i, int j, int k) throws SQLException {
    ParkingCell item = null;
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_PARKING_CELL);

    statement.setInt(1, lotID);
    statement.setInt(2, i);
    statement.setInt(3, j);
    statement.setInt(4, k);

    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = new ParkingCell(result);
    }

    result.close();
    statement.close();
    return item;
  }

  /** Update the ParkingCell record in the database.
   * Find record by the instance's id attribute and update record fields to match the instance.
   * @param conn the SQL connection
   * @throws SQLException on error */
  public void update(Connection conn) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_UPDATE_PARKING_CELL);

    int field = 1;
    statement.setString(field++, carID);
    statement.setTimestamp(field++, plannedExitTime);
    statement.setBoolean(field++, reserved);
    statement.setBoolean(field++, disabled);
    statement.setInt(field++, lotID);
    statement.setInt(field++, width);
    statement.setInt(field++, height);
    statement.setInt(field++, depth);

    statement.executeUpdate();

    statement.close();
  }

  public interface ParkingCellVisitor {
    public void call(ParkingCell argument);
  }

  public interface ParkingCellVisitorWithException {
    public void call(ParkingCell argument) throws ServerException;
  }

  /** Find all parking cells that belong to the specified lot and perform a callback function on each.
   * @param conn the SQL connection
   * @param lotID the lot ID
   * @param visitor the visitor
   * @throws SQLException on error */
  public static void lotForEach(Connection conn, int lotID, ParkingCellVisitor visitor) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_PARKING_CELL_BY_LOT_ID);

    statement.setInt(1, lotID);

    ResultSet result = statement.executeQuery();

    while (result.next()) {
      visitor.call(new ParkingCell(result));
    }

    result.close();
    statement.close();
  }

  /** Count the number of parking cells that do not have cars in them and are not disabled or reserved.
   * @param conn the SQL connection
   * @param lotID the lot ID
   * @return the number of available cells
   * @throws SQLException on error */
  public static int countAvailable(Connection conn, int lotID) throws SQLException {
    int count = 0;

    PreparedStatement statement = conn.prepareStatement(Constants.SQL_COUNT_FREE_PARKING_CELLS);

    statement.setInt(1, lotID);

    ResultSet result = statement.executeQuery();

    if (result.next()) {
      count = result.getInt(1);
    }

    result.close();
    statement.close();

    return count;
  }
}
