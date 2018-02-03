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
import cps.server.ServerException;
import cps.server.database.QueryBuilder;

/** Database entity for monthly parking subscriptions - regular or full both stored in the same table. */
public class SubscriptionService implements ParkingService {
  private static final long serialVersionUID = 1L;

  /** The id. */
  int             id;
  
  /** The subscription type. */
  int             subscriptionType; // 1 = regular, 2 = full
  
  /** The email. */
  String          email;
  
  /** The customer ID. */
  int             customerID;
  
  /** The car ID. */
  String          carID;
  
  /** The lot ID. */
  int             lotID;            // if null then full, else regular
  
  /** The start date. */
  LocalDate       startDate;
  
  /** The end date. */
  LocalDate       endDate;
  
  /** The daily exit time. */
  LocalTime       dailyExitTime;    // null for full subscription
  
  /** Whether the car is currently parked with this subscription. */
  boolean         parked;
  
  /** Whether this subscription is completed (can't be used anymore). */
  private boolean completed;
  
  /** Whether this subscription was canceled. */
  private boolean canceled;
  
  /** Whether the customer was warned that the subscription is about to expire. */
  private boolean warned;

  /** Instantiates a new subscription service object.
   * @param id the subscription id
   * @param type the subscription type - full or monthly
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param startDate the start date
   * @param endDate the end date
   * @param dailyExitTime the daily exit time
   * @param parked the parked flag
   * @param completed the completed flag
   * @param canceled the canceled flag
   * @param warned the warned flag */
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

  /** Instantiates a new subscription service object.
   * Shorter version of the constructor.
   * @param id the subscription id
   * @param type the subscription type - full or monthly
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param startDate the start date
   * @param endDate the end date
   * @param dailyExitTime the daily exit time */
  public SubscriptionService(int id, int type, int customerID, String email, String carID, int lotID, LocalDate startDate, LocalDate endDate,
      LocalTime dailyExitTime) {
    this(id, type, customerID, email, carID, lotID, startDate, endDate, dailyExitTime, false, false, false, false);
  }

  /** Instantiates a new subscription service from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException on error */
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
   * @param conn the SQL connection
   * @param type the subscription type - full or monthly
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param startDate the start date
   * @param endDate the end date
   * @param dailyExitTime the daily exit time
   * @param parked the parked flag
   * @param completed the completed flag
   * @param canceled the canceled flag
   * @param warned the warned flag
   * @return the new subscription service
   * @throws SQLException on error */
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
   * @param conn the SQL connection
   * @param type the type
   * @param customerID the customer ID
   * @param email the email
   * @param carID the car ID
   * @param lotID the lot ID
   * @param startDate the start date
   * @param endDate the end date
   * @param dailyExitTime the daily exit time
   * @return the new subscription service
   * @throws SQLException on error */
  public static SubscriptionService create(Connection conn, int type, int customerID, String email, String carID, int lotID, LocalDate startDate,
      LocalDate endDate, LocalTime dailyExitTime) throws SQLException {
    return create(conn, type, customerID, email, carID, lotID, startDate, endDate, dailyExitTime, false, false, false, false);
  }

  /** Find a SubscriptionService record that could qualify a customer for entry into the parking lot.
   * Checks if a SubscriptionService with the specified customer ID, car ID and subscription ID exists in the database.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param carID the car ID
   * @param subsID the subscription ID
   * @return the subscription service
   * @throws SQLException on error */
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

  /** Find subscriptions by customer ID.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @return a list of all subscriptions that the specified customer has purchased
   * @throws SQLException on error */
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

  /** Return the daily exit time for this subscription.
   * For regular subscription: specified at creation
   * For full subscription: always return midnight 
   * @see cps.entities.models.ParkingService#getExitTime() */
  @Override
  public LocalDateTime getExitTime() {
    if (this.subscriptionType == Constants.SUBSCRIPTION_TYPE_FULL) {
      return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    }
    return this.dailyExitTime.atDate(LocalDate.now());
  }

  /** Find a subscription service record by ID.
   * @param conn the SQL connection
   * @param id the subscription ID to search
   * @return the subscription service
   * @throws SQLException on error */
  public static SubscriptionService findByID(Connection conn, int id) throws SQLException {
    SubscriptionService item = null;

    PreparedStatement statement = conn.prepareStatement(Constants.SQL_GET_SUBSCRIPTION_SERVICE_BY_ID);
    statement.setInt(1, id);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = new SubscriptionService(result);
    }

    result.close();
    statement.close();

    return item;

  }

  /** Count the number of subscriptions of the specified type that the customer has purchased.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param subscriptionType the subscription type
   * @return the number of subscriptions purchased by the customer
   * @throws SQLException on error */
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

  /** Find a subscription service record by ID, throw an exception if not found.
   * @param conn the SQL connection
   * @param id the id
   * @return the subscription service
   * @throws SQLException on error
   * @throws ServerException the runtime exception */
  public static SubscriptionService findByIDNotNull(Connection conn, int id) throws SQLException, ServerException {
    SubscriptionService item = findByID(conn, id);

    if (item == null) {
      throw new ServerException("SubscriptionService with id " + id + " does not exist");
    }

    return item;
  }

  /** Check if overlap exists.
   * @param conn
   *        the SQL connection
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
   * @return true if there is a subscription record of the same type for the same car id in
   *         the same parking lot
   * @throws SQLException
   *         on error */
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

  /** Update the database record for this SubscriptionService.
   * @see cps.entities.models.ParkingService#update(java.sql.Connection)
   */
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
  
  /** Whether the subscription service record should be marked as complete after the customer exits from parking.
   * @return true if this was the last time that the customer could use the subscription
   * @see cps.entities.models.ParkingService#shouldCompleteAfterExit()
   */
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

  /** Count all active subscription service records.
   * @param conn the SQL connection
   * @return the number of subscription service records in the database
   * @throws SQLException on error */
  public static int countAll(Connection conn) throws SQLException {
    return new QueryBuilder<Integer>("SELECT customer_id FROM subscription_service WHERE not completed AND not canceled AND adddate(curdate(), interval 1 month) <= end_date GROUP BY customer_id")
        .countResults(conn);
  }

  /** Count all customers that own an active subscription for more than one car.
   * @param conn the SQL connection
   * @return the number of multiple car subscriptions in the database
   * @throws SQLException on error */
  public static int countWithMultipleCars(Connection conn) throws SQLException {
    return new QueryBuilder<Integer>(String.join(" ",
      "SELECT a.customer_id FROM subscription_service a INNER JOIN subscription_service b",
      "ON a.customer_id = b.customer_id AND a.car_id != b.car_id GROUP BY a.customer_id"))
      .countResults(conn);
  }

  /** Find all subscriptions that are going to expire after the specified period of time.
   * @param conn the SQL connection
   * @param delta the period of time
   * @param warned whether to search records where the customer was already warned, or records where the customer has not been warned yet
   * @return the collection
   * @throws SQLException on error */
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