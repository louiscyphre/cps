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

/** Database entity for one-time parking services - incidental parking or reserved parking both stored in the same table. */
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
  private boolean   parked;
  private boolean   completed;
  private boolean   canceled;
  private boolean   warned;

  /** Instantiates a new onetime service.
   * @param id the id
   * @param type the type
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param plannedStartTime the planned start time
   * @param plannedEndTime the planned end time
   * @param parked the parked
   * @param completed the completed
   * @param canceled the canceled
   * @param warned the warned */
  public OnetimeService(int id, int type, int customerID, String email, String carID, int lotID, Timestamp plannedStartTime, Timestamp plannedEndTime,
      boolean parked, boolean completed, boolean canceled, boolean warned) {
    this.id = id;
    this.parkingType = type;
    this.customerID = customerID;
    this.email = email;
    this.carID = carID;
    this.lotID = lotID;
    this.plannedStartTime = plannedStartTime;
    this.plannedEndTime = plannedEndTime;
    this.parked = parked;
    this.completed = completed;
    this.canceled = canceled;
    this.warned = warned;
  }

  /** Instantiates a new onetime service.
   * @param id the id
   * @param type the type
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param plannedStartTime the planned start time
   * @param plannedEndTime the planned end time */
  public OnetimeService(int id, int type, int customerID, String email, String carID, int lotID, Timestamp plannedStartTime, Timestamp plannedEndTime) {
    this(id, type, customerID, email, carID, lotID, plannedStartTime, plannedEndTime, false, false, false, false);
  }

  /** Instantiates a new onetime service from an SQL ResultSet.
   * @param rs the ResultSet
   * @throws SQLException on error */
  public OnetimeService(ResultSet rs) throws SQLException {
    this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getTimestamp(7), rs.getTimestamp(8), rs.getBoolean(9),
        rs.getBoolean(10), rs.getBoolean(11), rs.getBoolean(12));
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
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

  public boolean isParked() {
    return parked;
  }

  public void setParked(boolean parked) {
    this.parked = parked;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isCanceled() {
    return canceled;
  }

  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }

  public boolean isWarned() {
    return warned;
  }

  public void setWarned(boolean warned) {
    this.warned = warned;
  }

  @Override
  public int getLicenseType() {
    return Constants.LICENSE_TYPE_ONETIME;
  }

  /** Create a new Onetime Service record in the database.
   * @param conn the SQL connection
   * @param type Constants.ServiceType
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param plannedStartTime the planned start time
   * @param plannedEndTime the planned end time
   * @param parked the parked
   * @param completed the completed
   * @param canceled If the service was canceled (default false)
   * @param warned the warned
   * @return the onetime service
   * @throws SQLException on error */
  public static OnetimeService create(Connection conn, int type, int customerID, String email, String carID, int lotID, Timestamp plannedStartTime,
      Timestamp plannedEndTime, boolean parked, boolean completed, boolean canceled, boolean warned) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_ONETIME_SERVICE, Statement.RETURN_GENERATED_KEYS);

    int field = 1;
    stmt.setInt(field++, type);
    stmt.setInt(field++, customerID);
    stmt.setString(field++, email);
    stmt.setString(field++, carID);
    stmt.setInt(field++, lotID);
    stmt.setTimestamp(field++, plannedStartTime);
    stmt.setTimestamp(field++, plannedEndTime);
    stmt.setBoolean(field++, parked);
    stmt.setBoolean(field++, completed);
    stmt.setBoolean(field++, canceled);
    stmt.setBoolean(field++, warned);
    stmt.executeUpdate();

    ResultSet keys = stmt.getGeneratedKeys();
    int newID = 0;

    if (keys != null && keys.next()) {
      newID = keys.getInt(1);
      keys.close();
    }

    stmt.close();

    return new OnetimeService(newID, type, customerID, email, carID, lotID, plannedStartTime, plannedEndTime, parked, completed, canceled, warned);
  }

  /** A shorter version of create.
   * @param conn the SQL connection
   * @param type the type
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param plannedStartTime the planned start time
   * @param plannedEndTime the planned end time
   * @return the new onetime service
   * @throws SQLException on error */
  public static OnetimeService create(Connection conn, int type, int customerID, String email, String carID, int lotID, Timestamp plannedStartTime,
      Timestamp plannedEndTime) throws SQLException {
    return create(conn, type, customerID, email, carID, lotID, plannedStartTime, plannedEndTime, false, false, false, false);
  }

  /** Find a OnetimeService record by customer ID.
   * @param conn the SQL connection
   * @param userID the user ID
   * @return the collection
   * @throws SQLException on error */
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

  /** Find a OnetimeService record that could qualify a customer for entry into the parking lot.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param carID the car ID
   * @param lotID the lot ID
   * @return the onetime service
   * @throws SQLException on error */
  public static OnetimeService findForEntry(Connection conn, int customerID, String carID, int lotID) throws SQLException {
    OnetimeService result = null;

    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_FIND_ONETIME_SERVICE_FOR_ENTRY);

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

  // 
  /** Return a list of all entries whose time period overlaps with the given period.
   * @param conn the SQL connection
   * @param carID the car ID
   * @param startTime the start time
   * @param plannedEndTime the planned end time
   * @return a list of overlapping entries
   * @throws SQLException on error */
  public static ArrayList<OnetimeService> findForOverlap(Connection conn, String carID, Timestamp startTime, Timestamp plannedEndTime) throws SQLException {
    ArrayList<OnetimeService> result = new ArrayList<OnetimeService>();

    // When two time periods [a, b] [c, d] overlap, one of the following
    // conditions is true:
    // 1. c <= a and a <= d -- Start time of the given period is between the
    // start time and end time of another period
    // 2. a <= c and c <= b -- Start time of another period is between the start
    // time and end time of the given period
    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_FIND_OVERLAPPING_ONETIME_SERVICE);

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

  /** Return true if there is at least one record whose time period overlaps with the given period.
   * @param conn the SQL connection
   * @param carID the car ID
   * @param startTime the start time
   * @param endTime the end time
   * @return true if an overlapping record was found
   * @throws SQLException on error */
  public static boolean overlapExists(Connection conn, String carID, Timestamp startTime, Timestamp endTime) throws SQLException {
    boolean result = false;

    // When two time periods [a, b] [c, d] overlap, one of the following
    // conditions is true:
    // 1. c <= a and a <= d -- Start time of the given period is between the
    // start time and end time of another period
    // 2. a <= c and c <= b -- Start time of another period is between the start
    // time and end time of the given period
    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_COUNT_OVERLAPPING_ONETIME_SERVICE);

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

  /** Find a OnetimeService record by ID.
   * @param conn the SQL connection
   * @param serviceID the id to find
   * @return the onetime service
   * @throws SQLException on error */
  public static OnetimeService findByID(Connection conn, int serviceID) throws SQLException {
    OnetimeService result = null;

    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_GET_ONETIME_SERVICE_BY_ID);

    stmt.setInt(1, serviceID);

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      result = new OnetimeService(rs);
    }

    rs.close();
    stmt.close();

    return result;
  }

  /** Update the database record for this OnetimeService.
   * Finds onetime service in the database by the instance's id attribute and updates all fields in the database record
   * to match the fields in the instance.
   * @param conn the SQL connection
   * @throws SQLException on error */
  @Override
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
    st.setBoolean(index++, this.parked);
    st.setBoolean(index++, this.completed);
    st.setBoolean(index++, this.canceled);
    st.setBoolean(index++, this.warned);
    st.setInt(index++, this.id);
    st.executeUpdate();
    st.close();
  }

  @Override
  public LocalDateTime getExitTime() {
    return this.plannedEndTime.toLocalDateTime();
  }

  /** Find a OnetimeService record by ID, throw ServerException if not found.
   * @param conn the SQL connection
   * @param id the id
   * @return the onetime service
   * @throws SQLException on error
   * @throws ServerException if not found */
  public static OnetimeService findByIDNotNull(Connection conn, int id) throws SQLException, ServerException {
    OnetimeService item = findByID(conn, id);

    if (item == null) {
      throw new ServerException("OnetimeService with id " + id + " does not exist");
    }

    return item;
  }

  /** Find the associated parking lot record in the database.
   * @param conn the SQL connection
   * @return the parking lot
   * @throws SQLException on error
   * @throws ServerException on error */
  public ParkingLot getParkingLot(Connection conn) throws SQLException, ServerException {
    return ParkingLot.findByIDNotNull(conn, lotID);
  }

  /** Find the associated customer record in the database.
   * @param conn the SQL connection
   * @return the customer
   * @throws SQLException on error
   * @throws ServerException on error */
  public Customer getCustomer(Connection conn) throws SQLException, ServerException {
    return Customer.findByIDNotNull(conn, customerID);
  }

  public Duration getPlannedDuration() {
    return Duration.between(plannedStartTime.toLocalDateTime(), plannedEndTime.toLocalDateTime());
  }

  /** Calculate payment for this service.
   * @param pricePerHour the price per hour
   * @return the float */
  public float calculatePayment(float pricePerHour) {
    return pricePerHour * getPlannedDuration().getSeconds() / 3600f;
  }

  /** Find OnetimeService records where the customer is late to their reservation.
   * Is used to warn customers that their scheduled reservation has started, and wait for a reply for 30 minutes.
   * Is used again to retrieve a list of all the OnetimeService records that should be canceled because their customers did not respond to the warning within 30 minutes.
   * @param conn the SQL connection
   * @param delta the delta
   * @param warned whether to search records where the customer was already warned, or records where the customer has not been warned yet
   * @return the collection
   * @throws SQLException on error */
  public static Collection<OnetimeService> findLateCustomers(Connection conn, Duration delta, boolean warned) throws SQLException {
    LinkedList<OnetimeService> items = new LinkedList<OnetimeService>();
    
    PreparedStatement stmt = conn.prepareStatement(
        String.join(" ",
            "SELECT os.*", "FROM onetime_service os", "WHERE os.planned_start_time <= ? ",
            "AND not parked AND not completed AND os.warned=? AND not os.canceled"));
    
    stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minus(delta)));
    stmt.setBoolean(2, warned);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
      items.add(new OnetimeService(rs));
    }

    rs.close();
    stmt.close();

    return items;
  }

  /* (non-Javadoc)
   * @see cps.entities.models.ParkingService#shouldCompleteAfterExit()
   */
  @Override
  public boolean shouldCompleteAfterExit() {
    return true;
  }
}
