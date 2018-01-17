package cps.entities.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import cps.common.Constants;
import cps.server.ServerException;

// Database entity for one-time parking services - incidental parking or reserved parking both stored in the same table.

public class OnetimeService implements ParkingService {
  private static final long serialVersionUID = 1L;

  private int       id;
  private int       parkingType;      // 1 = incidental, 2 = reserved
  private int       customerID;
  private String    email;
  private String    carID;
  private int       lotID;
  private Timestamp plannedStartTime; // Current time for incidental
  private Timestamp plannedEndTime;
  private boolean   canceled;

  public OnetimeService(int id, int type, int customerID, String email, String carID, int lotID,
      Timestamp plannedStartTime, Timestamp plannedEndTime, boolean canceled) {
    this.id = id;
    this.parkingType = type;
    this.customerID = customerID;
    this.email = email;
    this.carID = carID;
    this.lotID = lotID;
    this.plannedStartTime = plannedStartTime;
    this.plannedEndTime = plannedEndTime;
    this.canceled = canceled;
  }

  public OnetimeService(ResultSet rs) throws SQLException {
    this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getTimestamp(7),
        rs.getTimestamp(8), rs.getBoolean(9));
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getParkingType() {
    return parkingType;
  }

  public void setParkingType(int type) {
    this.parkingType = type;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCarID() {
    return carID;
  }

  public void setCarID(String carID) {
    this.carID = carID;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public Timestamp getPlannedStartTime() {
    return plannedStartTime;
  }

  public void setPlannedStartTime(Timestamp plannedStartTime) {
    this.plannedStartTime = plannedStartTime;
  }

  public Timestamp getPlannedEndTime() {
    return plannedEndTime;
  }

  public void setPlannedEndTime(Timestamp plannedEndTime) {
    this.plannedEndTime = plannedEndTime;
  }

  public boolean isCanceled() {
    return canceled;
  }

  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }

  @Override
  public int getLicenseType() {
    return Constants.LICENSE_TYPE_ONETIME;
  }

  /**
   * Creates Onetime Service.
   *
   * @param conn
   *          the Connection
   * @param type
   *          Constants.ServiceType
   * @param customerID
   *          the customer ID
   * @param email
   *          the email
   * @param carID
   *          the car ID
   * @param lotID
   *          the lot ID
   * @param plannedStartTime
   *          the planned start time
   * @param plannedEndTime
   *          the planned end time
   * @param canceled
   *          If the service was canceled (default false)
   * @return the onetime service
   * @throws SQLException
   *           the SQL exception
   */
  public static OnetimeService create(Connection conn, int type, int customerID, String email, String carID, int lotID,
      Timestamp plannedStartTime, Timestamp plannedEndTime, boolean canceled) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_ONETIME_SERVICE,
        Statement.RETURN_GENERATED_KEYS);

    int field = 1;
    stmt.setInt(field++, type);
    stmt.setInt(field++, customerID);
    stmt.setString(field++, email);
    stmt.setString(field++, carID);
    stmt.setInt(field++, lotID);
    stmt.setTimestamp(field++, plannedStartTime);
    stmt.setTimestamp(field++, plannedEndTime);
    stmt.setBoolean(field++, canceled);
    stmt.executeUpdate();

    ResultSet keys = stmt.getGeneratedKeys();
    int newID = 0;

    if (keys != null && keys.next()) {
      newID = keys.getInt(1);
      keys.close();
    }

    stmt.close();

    return new OnetimeService(newID, type, customerID, email, carID, lotID, plannedStartTime, plannedEndTime, canceled);
  }

  public static Collection<OnetimeService> findByCustomerID(Connection conn, int userID) throws SQLException {
    LinkedList<OnetimeService> results = new LinkedList<OnetimeService>();

    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_GET_ONETIME_SERVICE_BY_CUSTOMER_ID);
    stmt.setInt(1, userID);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
      results.add(new OnetimeService(rs));
    }

    rs.close();
    stmt.close();

    return results;
  }

  public static OnetimeService findForEntry(Connection conn, int customerID, String carID, int lotID)
      throws SQLException {
    OnetimeService result = null;

    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_GET_ONETIME_SERVICE_BY_CUSTID_CARID_LOTID);

    stmt.setInt(1, customerID);
    stmt.setString(2, carID);
    stmt.setInt(3, lotID);

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      result = new OnetimeService(rs);
    }

    rs.close();
    stmt.close();

    return result;
  }

  // Return a list of all entries whose time period overlaps with the given
  // period
  public static ArrayList<OnetimeService> findForOverlap(Connection conn, String carID, Timestamp startTime,
      Timestamp plannedEndTime) throws SQLException {
    ArrayList<OnetimeService> result = new ArrayList<OnetimeService>();

    // When two time periods [a, b] [c, d] overlap, one of the following
    // conditions is true:
    // 1. c <= a and a <= d -- Start time of the given period is between the
    // start time and end time of another period
    // 2. a <= c and c <= b -- Start time of another period is between the start
    // time and end time of the given period
    PreparedStatement stmt = conn.prepareStatement(
        "SELECT * FROM onetime_service WHERE car_id=? AND ((planned_start_time < ? AND ? < planned_end_time) OR (? < planned_start_time AND planned_start_time < ?))");

    int i = 1;
    stmt.setString(i++, carID);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, plannedEndTime);

    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
      result.add(new OnetimeService(rs));
    }

    rs.close();
    stmt.close();

    return result;
  }

  // Return true if there is at least one entry whose time period overlaps with
  // the given period
  public static boolean overlapExists(Connection conn, String carID, Timestamp startTime, Timestamp endTime)
      throws SQLException {
    boolean result = false;

    // When two time periods [a, b] [c, d] overlap, one of the following
    // conditions is true:
    // 1. c <= a and a <= d -- Start time of the given period is between the
    // start time and end time of another period
    // 2. a <= c and c <= b -- Start time of another period is between the start
    // time and end time of the given period
    PreparedStatement stmt = conn.prepareStatement(
        "SELECT count(*) FROM onetime_service WHERE car_id=? AND ((planned_start_time <= ? AND ? <= planned_end_time) OR (? <= planned_start_time AND planned_start_time <= ?))");

    int i = 1;
    stmt.setString(i++, carID);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, startTime);
    stmt.setTimestamp(i++, endTime);

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      // If there is one or more results, we have an overlapping OnetimeService
      // entry
      result = rs.getInt(1) > 0;
    }

    rs.close();
    stmt.close();

    return result;
  }

  public static OnetimeService findByID(Connection conn, int sId) throws SQLException {
    OnetimeService result = null;

    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_GET_ONETIME_SERVICE_BY_ID);

    stmt.setInt(1, sId);

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      result = new OnetimeService(rs);
    }

    rs.close();
    stmt.close();

    return result;
  }

  /**
   * Finds onetime service in database by it's id and updates all fields in db
   * to match fields in instance.
   *
   * @param conn
   *          the conn
   * @throws SQLException
   *           the SQL exception
   */
  public void update(Connection conn) throws SQLException {
    java.sql.PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_ONETIME_BY_ID);
    int index = 1;
    st.setInt(index++, this.parkingType);
    st.setInt(index++, this.customerID);
    st.setString(index++, this.email);
    st.setString(index++, this.carID);
    st.setInt(index++, this.lotID);
    st.setTimestamp(index++, this.plannedStartTime);
    st.setTimestamp(index++, this.plannedEndTime);
    st.setBoolean(index++, this.canceled);
    st.setInt(index++, this.id);
    st.executeUpdate();
    st.close();
  }

  @Override
  public LocalDateTime getExitTime() {
    return this.plannedEndTime.toLocalDateTime();
  }

  public static OnetimeService findByIDNotNull(Connection conn, int id) throws SQLException, RuntimeException {
    OnetimeService item = findByID(conn, id);

    if (item == null) {
      throw new RuntimeException("OnetimeService with id " + id + " does not exist");
    }

    return item;
  }

  public ParkingLot getParkingLot(Connection conn) throws SQLException, ServerException {
    return ParkingLot.findByIDNotNull(conn, lotID);
  }

  public Customer getCustomer(Connection conn) throws SQLException, ServerException {
    return Customer.findByIDNotNull(conn, customerID);
  }

  public Duration getPlannedDuration() {
    return Duration.between(plannedStartTime.toLocalDateTime(), plannedEndTime.toLocalDateTime());
  }

  public float calculatePayment(float pricePerHour) {
    return pricePerHour * getPlannedDuration().getSeconds() / 3600f;
  }

  public static Collection<WarningMessage> findLateUnwarnedCustomers(Connection conn) throws SQLException {
    LinkedList<WarningMessage> mess = new LinkedList<WarningMessage>();
    String helper = "";
    helper += "SELECT os.customer_id,os.email,os.car_id,os.lot_id,os.planned_start_time,os.planned_end_time ";
    helper += "FROM onetime_service os";
    helper += "WHERE os.planned_start_time <= ? AND"; // variable 1 - current
                                                      // time
    helper += " NOT EXISTS (SELECT *";
    helper += "FROM car_transportation ct";
    helper += "WHERE ct.customer_id=os.customer_id AND";
    helper += "ct.auth_id=os.id AND";
    helper += "ct.auth_type=1 AND";
    helper += "os.lot_id=ct.lot_id) AND os.warned=false AND os.canceled=false";
    PreparedStatement stmt = conn.prepareStatement(helper);
    stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
      mess.add(new WarningMessage(rs));
    }

    return mess;
  }
}
