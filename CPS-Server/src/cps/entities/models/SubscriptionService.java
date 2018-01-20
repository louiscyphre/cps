package cps.entities.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedList;

import cps.common.Constants;
import cps.server.database.QueryBuilder;

// Database entity for monthly parking subscriptions - regular or full both stored in the same table.

public class SubscriptionService implements ParkingService {
  private static final long serialVersionUID = 1L;

  int             id;
  int             subscriptionType; // 1 = regular, 2 = full
  String          email;
  int             customerID;
  String          carID;
  int             lotID;            // if null then full, else regular
  LocalDate       startDate;
  LocalDate       endDate;
  LocalTime       dailyExitTime;    // null for full subscription
  boolean         parked;
  private boolean completed;
  private boolean canceled;
  private boolean warned;

  public SubscriptionService(int id, int type, int customerID, String email, String carID, int lotID, LocalDate startDate, LocalDate endDate,
      LocalTime dailyExitTime, boolean parked, boolean completed, boolean canceled, boolean warned) {
    this.id = id;
    this.subscriptionType = type;
    this.customerID = customerID;
    this.email = email;
    this.carID = carID;
    this.lotID = lotID;
    this.startDate = startDate;
    this.endDate = endDate;
    this.dailyExitTime = dailyExitTime;
    this.parked = parked;
    this.completed = completed;
    this.canceled = canceled;
    this.warned = warned;
  }

  public SubscriptionService(int id, int type, int customerID, String email, String carID, int lotID, LocalDate startDate, LocalDate endDate,
      LocalTime dailyExitTime) {
    this(id, type, customerID, email, carID, lotID, startDate, endDate, dailyExitTime, false, false, false, false);
  }

  public SubscriptionService(ResultSet rs) throws SQLException {
    this(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getDate(7).toLocalDate(), rs.getDate(8).toLocalDate(),
        rs.getTime(9).toLocalTime(), rs.getBoolean(10), rs.getBoolean(11), rs.getBoolean(12), rs.getBoolean(13));
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getSubscriptionType() {
    return subscriptionType;
  }

  public void setSubscriptionType(int type) {
    this.subscriptionType = type;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
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

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalTime getDailyExitTime() {
    return dailyExitTime;
  }

  public void setDailyExitTime(LocalTime endTime) {
    this.dailyExitTime = endTime;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public int getLicenseType() {
    return Constants.LICENSE_TYPE_SUBSCRIPTION;
  }
  
  @Override
  public boolean isParked() {
    return parked;
  }

  @Override
  public void setParked(boolean parked) {
    this.parked = parked;
  }

  /** Creates a new SubscriptionService entry in the database.
   * @param conn the conn
   * @param type the type
   * @param customerID int - Customer ID
   * @param email the email
   * @param carID String - The car ID
   * @param lotID int - The lot ID
   * @param startDate LocalDate - The start date
   * @param endDate LocalDate - The end date
   * @param dailyExitTime LocalTime - The daily exit time
   * @param parked the parked
   * @param completed the completed
   * @param canceled the canceled
   * @param warned the warned
   * @return the subscription service
   * @throws SQLException the SQL exception */
  public static SubscriptionService create(Connection conn, int type, int customerID, String email, String carID, int lotID, LocalDate startDate,
      LocalDate endDate, LocalTime dailyExitTime, boolean parked, boolean completed, boolean canceled, boolean warned) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_SUBSCRIPTION_SERVICE, Statement.RETURN_GENERATED_KEYS);

    int field = 1;
    statement.setInt(field++, type);
    statement.setInt(field++, customerID);
    statement.setString(field++, email);
    statement.setString(field++, carID);
    statement.setInt(field++, lotID);
    statement.setDate(field++, Date.valueOf(startDate));
    statement.setDate(field++, Date.valueOf(endDate));
    statement.setTime(field++, Time.valueOf(dailyExitTime));
    statement.setBoolean(field++, parked);
    statement.setBoolean(field++, completed);
    statement.setBoolean(field++, canceled);
    statement.setBoolean(field++, warned);
    statement.executeUpdate();

    ResultSet keys = statement.getGeneratedKeys();
    int newID = 0;

    if (keys != null && keys.next()) {
      newID = keys.getInt(1);
      keys.close();
    }

    statement.close();

    return new SubscriptionService(newID, type, customerID, email, carID, lotID, startDate, endDate, dailyExitTime, parked, completed, canceled, warned);
  }
  
  /** A shorter version of create.
   * @return the new subscription service
   * @throws SQLException the SQL exception */
  public static SubscriptionService create(Connection conn, int type, int customerID, String email, String carID, int lotID, LocalDate startDate,
      LocalDate endDate, LocalTime dailyExitTime) throws SQLException {
    return create(conn, type, customerID, email, carID, lotID, startDate, endDate, dailyExitTime, false, false, false, false);
  }

  public static SubscriptionService findForEntry(Connection conn, int customerID, String carID, int subsID) throws SQLException {
    SubscriptionService item = null;

    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_SUBSCRIPTION_FOR_ENTRY);

    int index = 1;

    statement.setInt(index++, subsID);
    statement.setInt(index++, customerID);
    statement.setString(index++, carID);

    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = new SubscriptionService(result);
    }

    result.close();
    statement.close();

    return item;
  }

  public static Collection<SubscriptionService> findByCustomerID(Connection conn, int customerID) throws SQLException {
    LinkedList<SubscriptionService> items = new LinkedList<SubscriptionService>();

    PreparedStatement statement = conn.prepareStatement(Constants.SQL_GET_SUBSCRIPTION_SERVICE_BY_CUSTOMER_ID);
    statement.setInt(1, customerID);
    ResultSet result = statement.executeQuery();

    while (result.next()) {
      items.add(new SubscriptionService(result));
    }

    result.close();
    statement.close();

    return items;
  }

  @Override
  public LocalDateTime getExitTime() {
    if (this.subscriptionType == Constants.SUBSCRIPTION_TYPE_FULL) {
      return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    }
    return this.dailyExitTime.atDate(LocalDate.now());
  }

  public static SubscriptionService findByID(Connection conn, int authID) throws SQLException {
    SubscriptionService item = null;

    PreparedStatement statement = conn.prepareStatement(Constants.SQL_GET_SUBSCRIPTION_SERVICE_BY_ID);
    statement.setInt(1, authID);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = new SubscriptionService(result);
    }

    result.close();
    statement.close();

    return item;

  }

  public static int countForCustomer(Connection conn, int customerID, int subscriptionType) throws SQLException {
    PreparedStatement statement = conn.prepareStatement("SELECT count(id) FROM subscription_service WHERE customer_id = ? AND subs_type = ?");

    statement.setInt(1, customerID);
    statement.setInt(2, subscriptionType);

    ResultSet result = statement.executeQuery();

    int count = 0;

    if (result.next()) {
      count = result.getInt(1);
    }

    result.close();
    statement.close();

    return count;
  }

  public static SubscriptionService findByIDNotNull(Connection conn, int id) throws SQLException, RuntimeException {
    SubscriptionService item = findByID(conn, id);

    if (item == null) {
      throw new RuntimeException("SubscriptionService with id " + id + " does not exist");
    }

    return item;
  }

  /** CHeck if Overlap exists.
   * @param conn
   *        the conn
   * @param carID
   *        the car ID
   * @param subsType
   *        the subs type
   * @param lotId
   *        the lot id
   * @param startDate
   *        the start date
   * @param endDate
   *        the end date
   * @return True if exists subscription of the same type for the same car id in
   *         the same parking lot
   * @throws SQLException
   *         the SQL exception */
  public static boolean overlapExists(Connection conn, String carID, int subsType, int lotId, LocalDate startDate, LocalDate endDate) throws SQLException {
    PreparedStatement stmt = null;
    boolean result = false;
    int i = 1;
    if (subsType == Constants.SUBSCRIPTION_TYPE_FULL) {
      stmt = conn.prepareStatement(
          "SELECT count(*) FROM subscription_service WHERE car_id = ? AND subs_type = ? AND ((start_date <= ? AND ? <= end_date) OR (? <= start_date AND start_date <= ?))");
      stmt.setString(i++, carID);
      stmt.setInt(i++, subsType);

    } else {
      stmt = conn.prepareStatement(
          "SELECT count(*) FROM subscription_service WHERE car_id = ? AND ((subs_type != ?) OR (subs_type = ? AND lot_id = ?)) AND ((start_date <= ? AND ? <= end_date) OR (? <= start_date AND start_date <= ?))");
      stmt.setString(i++, carID);
      stmt.setInt(i++, subsType);
      stmt.setInt(i++, subsType);
      stmt.setInt(i++, lotId);
    }

    stmt.setDate(i++, Date.valueOf(startDate));
    stmt.setDate(i++, Date.valueOf(startDate));
    stmt.setDate(i++, Date.valueOf(startDate));
    stmt.setDate(i++, Date.valueOf(endDate));

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      result = rs.getInt(1) > 0;
    }

    return result;
  }

  @Override
  public void update(Connection conn) throws SQLException {
    java.sql.PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_SUBSCRIPTION_BY_ID);
    int index = 1;
    st.setInt(index++, this.subscriptionType);
    st.setInt(index++, this.customerID);
    st.setString(index++, this.email);
    st.setString(index++, this.carID);
    st.setInt(index++, this.lotID);
    st.setDate(index++, Date.valueOf(this.startDate));
    st.setDate(index++, Date.valueOf(this.endDate));
    st.setTime(index++, Time.valueOf(this.dailyExitTime));
    st.setBoolean(index++, this.parked);
    st.setBoolean(index++, this.completed);
    st.setBoolean(index++, this.canceled);
    st.setBoolean(index++, this.warned);
    st.setInt(index++, this.id);
    st.executeUpdate();
    st.close();
    
  }
  
  @Override
  public boolean shouldCompleteAfterExit() {
    return LocalDateTime.of(endDate, dailyExitTime).isBefore(LocalDateTime.now()) && !parked;    
  }

  @Override
  public boolean isCompleted() {
    return completed;
  }

  @Override
  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  @Override
  public boolean isCanceled() {
    return canceled;
  }

  @Override
  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }

  @Override
  public boolean isWarned() {
    return warned;
  }

  @Override
  public void setWarned(boolean warned) {
    this.warned = warned;
  }

  @Override
  public int getParkingType() {
    return Constants.PARKING_TYPE_RESERVED;
  }

  public static int countAll(Connection conn) throws SQLException {
    return new QueryBuilder<Integer>("SELECT count(*) FROM subscription_service")
        .fetchResult(conn, result -> result.getInt(1));
  }

  public static int countWithMultipleCars(Connection conn) throws SQLException {
    return new QueryBuilder<Integer>(String.join(" ",
      "SELECT count(a.id) FROM subscription_service a INNER JOIN subscription_service b",
      "ON a.customer_id = b.customer_id AND a.car_id != b.car_id GROUP BY a.customer_id"))
      .fetchResult(conn, result -> result.getInt(1));
  }

  public static Collection<SubscriptionService> findExpiringAfter(Connection conn, Duration delta, boolean warned) throws SQLException {
    return new QueryBuilder<SubscriptionService>(String.join(" ",
      "SELECT *", "FROM subscription_service os", "WHERE end_date <= ? ",
      "AND not completed AND warned=? AND not canceled"
    )).withFields(statement -> {
      statement.setDate(1, Date.valueOf(LocalDateTime.now().plus(delta).toLocalDate()));
      statement.setBoolean(2, warned);
    }).collectResults(conn, result -> new SubscriptionService(result));
  }
}