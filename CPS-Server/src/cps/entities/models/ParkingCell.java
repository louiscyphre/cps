package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import cps.common.Constants;
import cps.server.ServerException;

/**
 * The Class ParkingCell.
 */
public class ParkingCell implements Serializable {
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  public final int  lotID;
  public final int  locationI;
  public final int  locationJ;
  public final int  locationK;
  private String    carID;
  private Timestamp plannedExitTime;
  private boolean   reserved;
  private boolean   disabled;

  /**
   * Instantiates a new parking cell.
   *
   * @param lotID the lot ID
   * @param locationI the location I
   * @param locationJ the location J
   * @param locationK the location K
   * @param carID the car ID
   * @param plannedExitTime the planned exit time
   * @param reserved the reserved
   * @param disabled the disabled
   */
  public ParkingCell(int lotID, int locationI, int locationJ, int locationK, String carID, Timestamp plannedExitTime,
      boolean reserved, boolean disabled) {
    this.lotID = lotID;
    this.locationI = locationI;
    this.locationJ = locationJ;
    this.locationK = locationK;
    this.carID = carID;
    this.plannedExitTime = plannedExitTime;
    this.reserved = reserved;
    this.disabled = disabled;
  }

  /**
   * Instantiates a new parking cell.
   *
   * @param rs the rs
   * @throws SQLException the SQL exception
   */
  public ParkingCell(ResultSet rs) throws SQLException {
    this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getTimestamp(6), rs.getBoolean(7),
        rs.getBoolean(8));
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

  /**
   * Creates the.
   *
   * @param conn the conn
   * @param lotID the lot ID
   * @param locationI the location I
   * @param locationJ the location J
   * @param locationK the location K
   * @param carID the car ID
   * @param plannedExitTime the planned exit time
   * @param reserved the reserved
   * @param disabled the disabled
   * @return the parking cell
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception
   */
  public static ParkingCell create(Connection conn, int lotID, int locationI, int locationJ, int locationK,
      String carID, Timestamp plannedExitTime, boolean reserved, boolean disabled)
      throws SQLException, ServerException {
    // Create SQL statement
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_PARKING_CELL);

    // Fill in the fields of the SQL statement
    int field = 1;
    statement.setInt(field++, lotID);
    statement.setInt(field++, locationI);
    statement.setInt(field++, locationJ);
    statement.setInt(field++, locationK);
    statement.setString(field++, carID);
    statement.setTimestamp(field++, plannedExitTime);
    statement.setBoolean(field++, reserved);
    statement.setBoolean(field++, disabled);

    // Execute SQL query
    if (statement.executeUpdate() < 1) {
      throw new ServerException("Failed to create ParkingCell");
    }

    statement.close();

    return new ParkingCell(lotID, locationI, locationJ, locationK, carID, plannedExitTime, reserved, disabled);
  }

  /**
   * Find.
   *
   * @param conn the conn
   * @param lotID the lot ID
   * @param i the i
   * @param j the j
   * @param k the k
   * @return the parking cell
   * @throws SQLException the SQL exception
   */
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

  /**
   * Update.
   *
   * @param conn the conn
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception
   */
  public void update(Connection conn) throws SQLException, ServerException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_UPDATE_PARKING_CELL);

    int field = 1;
    statement.setInt(field++, lotID);
    statement.setInt(field++, locationI);
    statement.setInt(field++, locationJ);
    statement.setInt(field++, locationK);
    statement.setString(field++, carID);
    statement.setTimestamp(field++, plannedExitTime);
    statement.setBoolean(field++, reserved);
    statement.setBoolean(field++, disabled);

    if (statement.executeUpdate() < 1) {
      throw new ServerException("Failed to update ParkingCell");
    }

    statement.close();
  }

  public interface ParkingCellVisitor {
    public void call(ParkingCell argument);
  }

  /**
   * Lot for each.
   *
   * @param conn the conn
   * @param lotID the lot ID
   * @param visitor the visitor
   * @throws SQLException the SQL exception
   */
  public static void lotForEach(Connection conn, int lotID, ParkingCellVisitor visitor) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_PARKING_CELL_BY_LOT_ID);
    ResultSet result = statement.executeQuery();

    statement.setInt(1, lotID);

    while (result.next()) {
      visitor.call(new ParkingCell(result));
    }

    result.close();
    statement.close();
  }
}
