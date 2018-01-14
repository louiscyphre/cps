package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;

import com.google.gson.Gson;

import cps.common.Constants;
import cps.server.ServerException;

/**
 * The Class ParkingLot.
 */
public class ParkingLot implements Serializable {
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** Parking lot id. */
  private int id;

  /** Parking lot street address. */
  private String streetAddress;

  /** Amount of columns in parking lot. */
  private int width;

  /** The price of incidental parking. */
  private float price1;

  /** The price one time parking. */
  private float price2;

  /**
   * Represents list of alternative parking lots that will be provided if the
   * current lot is full.
   */
  private String alternativeLots;

  /** IP address of the robot. */
  private String robotIP;

  private boolean lotFull;

  /**
   * Instantiates a new parking lot.
   *
   * @param id
   *          Parking lot id
   * @param streetAddress
   *          Parking lot street address
   * @param size
   *          Amount of columns
   * @param content
   *          Serialized three dimensional array that reflects status of the
   *          parking spots
   * @param price1
   *          Price of incidental parking
   * @param price2
   *          Price of one time parking
   * @param alternativeLots
   *          List of alternative parking lots
   * @param robotIP
   *          IP address of the robot
   */
  public ParkingLot(int id, String streetAddress, int size, float price1, float price2, String alternativeLots,
      String robotIP, boolean lotfull) {
    this.id = id;
    this.streetAddress = streetAddress;
    this.width = size;
    this.price1 = price1;
    this.price2 = price2;
    this.alternativeLots = alternativeLots;
    this.robotIP = robotIP;
    this.lotFull = lotfull;
  }

  /**
   * Instantiates a new parking lot.
   *
   * @param rs
   *          the Result set
   * @throws SQLException
   *           the SQL exception
   */
  public ParkingLot(ResultSet rs) throws SQLException {
    this(rs.getInt("id"), rs.getString("street_address"), rs.getInt("size"), rs.getFloat("price1"),
        rs.getFloat("price2"), rs.getString("alternative_lots"), rs.getString("robot_ip"), rs.getBoolean("lot_full"));
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id
   *          the new id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the street address.
   *
   * @return the street address
   */
  public String getStreetAddress() {
    return streetAddress;
  }

  /**
   * Sets the street address.
   *
   * @param streetAddress
   *          the new street address
   */
  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  /**
   * Gets the size.
   *
   * @return the size
   */
  public int getWidth() {

    return width;
  }

  /**
   * Sets the size.
   *
   * @param width
   *          the new size
   */
  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return Constants.LOT_HEIGHT;
  }

  public int getDepth() {
    return Constants.LOT_DEPTH;
  }

  /**
   * @return a product of lot dimensions
   */
  public int getVolume() {
    return getWidth() * getHeight() * getDepth();
  }

  /**
   * @return a three dimensional array that reflects status of the parking
   *         spots.
   */
  public ParkingCell[][][] constructContentArray(Connection conn) throws SQLException, ServerException {
    ParkingCell[][][] result = new ParkingCell[width][3][3];
    ParkingCell.lotForEach(conn, id, cell -> result[cell.width][cell.height][cell.depth] = cell);
    return result;
  }

  /**
   * Gets the price of incidental parking.
   *
   * @return the price of incidental parking
   */
  public float getPrice1() {
    return price1;
  }

  /**
   * Sets the price of incidental parking.
   *
   * @param price1
   *          the new price of incidental parking
   */
  public void setPrice1(float price1) {
    this.price1 = price1;
  }

  /**
   * Gets the price of one time parking.
   *
   * @return the price of one time parking
   */
  public float getPrice2() {
    return price2;
  }

  public float getPriceForService(int serviceType) {
    switch (serviceType) {
      case Constants.PARKING_TYPE_INCIDENTAL:
        return price1;
      default:
        return price2;
    }
  }

  /**
   * Sets the price of one time parking.
   *
   * @param price2
   *          the new price of one time parking
   */
  public void setPrice2(float price2) {
    this.price2 = price2;
  }

  /**
   * Gets the alternative lots.
   *
   * @return the alternative lots
   */
  public String getAlternativeLots() {
    return alternativeLots;
  }

  /**
   * Sets the alternative lots.
   *
   * @param alternativeLots
   *          the new alternative lots
   */
  public void setAlternativeLots(String alternativeLots) {
    this.alternativeLots = alternativeLots;
  }

  /**
   * Gets the robot IP.
   *
   * @return the robot IP
   */
  public String getRobotIP() {
    return robotIP;
  }

  /**
   * Sets the robot IP.
   *
   * @param robotIP
   *          the new robot IP
   */
  public void setRobotIP(String robotIP) {
    this.robotIP = robotIP;
  }

  public boolean isLotFull() {
    return lotFull;
  }

  public void setLotFull(boolean lotFull) {
    this.lotFull = lotFull;
  }

  /**
   * Insert new parking lot into the database and create a new parking lot
   * object.
   *
   * @param conn
   *          Connection to database server
   * @param streetAddress
   *          Parking lot street address
   * @param size
   *          Amount of columns in parking lot
   * @param price1
   *          The price of incidental parking
   * @param price2
   *          The price one time parking
   * @param robotIP
   *          IP address of the robot
   * @return New parking lot object
   * @throws SQLException
   *           the SQL exception
   * @throws ServerException
   */
  public static ParkingLot create(Connection conn, String streetAddress, int size, float price1, float price2,
      String robotIP) throws SQLException, ServerException {
    // Create SQL statement and request table for table keys
    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_PARKING_LOT, Statement.RETURN_GENERATED_KEYS);

    // Fill in the fields of the SQL statement
    int field = 1;
    stmt.setString(field++, streetAddress);
    stmt.setInt(field++, size);

    // Parking lot content as a string
    stmt.setFloat(field++, price1);
    stmt.setFloat(field++, price2);
    stmt.setString(field++, robotIP);

    // Execute SQL query
    if (stmt.executeUpdate() < 1) {
      throw new ServerException("Failed to create ParkingLot");
    }

    // Extract the auto-generated keys created as a result of executing this
    // Statement object
    ResultSet keys = stmt.getGeneratedKeys();
    int newID = 0;

    // If keys were created take the first one
    // A ResultSet cursor is initially positioned before the first row; the
    // first call to the method next makes the first row the current row
    if (keys != null && keys.next()) {
      newID = keys.getInt(1);
      keys.close();
    }

    stmt.close();

    // Create parking cells
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < 3; j++) {
        for (int k = 0; k < 3; k++) {
          ParkingCell.create(conn, newID, i, j, k, null, null, false, false);
        }
      }
    }

    return new ParkingLot(newID, streetAddress, size, price1, price2, null, robotIP, false);
  }

  /**
   * Find by ID.
   *
   * @param conn
   *          the conn
   * @param id
   *          the id
   * @return the parking lot
   * @throws SQLException
   *           the SQL exception
   */
  public static ParkingLot findByID(Connection conn, int id) throws SQLException {
    ParkingLot result = null;

    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM parking_lot WHERE id = ?");
    stmt.setInt(1, id);
    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      result = new ParkingLot(rs);
    }

    rs.close();
    stmt.close();

    return result;
  }

  public static ParkingLot findByIDNotNull(Connection conn, int id) throws SQLException, ServerException {
    ParkingLot result = findByID(conn, id);

    if (result == null) {
      throw new ServerException("ParkingLot with id " + id + " does not exist");
    }

    return result;
  }

  /**
   * Counts the number of cells that will need to be available for
   * OnetimeService customers at a given point in time
   *
   * @param conn
   *          the conn
   * @param lotId
   *          the lot id
   * @param startTime
   *          the start time
   * @param hoursInAdvance
   *          The hours in advance - default should be 1
   * @return the int
   * @throws SQLException
   */
  public int countOrderedCells(Connection conn, int lotId, Timestamp startTime, int hoursInAdvance)
      throws SQLException {
    int result = 0;
    LocalDateTime latest = startTime.toLocalDateTime();
    latest.plusHours(hoursInAdvance);
    PreparedStatement stmt = conn.prepareStatement(
        "SELECT count(*) FROM onetime_service WHERE (planned_start_time <= ? AND ? <= planned_end_time) OR (? <= planned_start_time AND planned_start_time <= ?)");
    int i = 1;
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, Timestamp.valueOf(latest));
    ResultSet rs = stmt.executeQuery();
    rs.next();
    result = rs.getInt(1);
    return result;
  }

  public void update(Connection conn) throws SQLException, ServerException {
    PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_PARKING_LOT);

    int index = 1;
    st.setString(index++, this.streetAddress);
    st.setInt(index++, this.width);
    st.setFloat(index++, this.price1);
    st.setFloat(index++, this.price2);
    st.setString(index++, this.alternativeLots);
    st.setString(index++, this.robotIP);
    st.setBoolean(index++, this.lotFull);
    st.setInt(index++, this.id);

    st.executeUpdate();

    st.close();
  }

  public static Collection<ParkingLot> findAll(Connection conn) throws SQLException {
    LinkedList<ParkingLot> results = new LinkedList<ParkingLot>();

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(Constants.SQL_FIND_ALL_PARKING_LOTS);

    while (rs.next()) {
      results.add(new ParkingLot(rs));
    }

    rs.close();
    stmt.close();

    return results;
  }

  public int countFreeCells(Connection conn) throws SQLException, ServerException {
    int count = ParkingCell.countAvailable(conn, id);

    if (count > getVolume()) {
      // This shouldn't happen during normal operation
      throw new ServerException("There is more free cells than the lot size");
    }

    return count;
  }

  public void updateContent(Connection conn, ParkingCell[][][] content) throws SQLException {
    for (ParkingCell[][] slice1 : content) {
      for (ParkingCell[] slice2 : slice1) {
        for (ParkingCell cell : slice2) {
          cell.update(conn);
        }
      }
    }
  }

  public Collection<ParkingLot> retrieveAlternativeLots(Connection conn, Gson gson)
      throws SQLException, ServerException {
    int[] lotIDs;

    try {
      lotIDs = gson.fromJson(getAlternativeLots(), int[].class);
    } catch (Exception e) {
      throw new ServerException(e.getMessage());
    }

    LinkedList<ParkingLot> lots = new LinkedList<>();

    for (int lotID : lotIDs) {
      ParkingLot lot = findByID(conn, lotID);
      if (lot != null) {
        lots.add(lot);
      }
    }

    return lots;
  }
}
