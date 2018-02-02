package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import cps.common.Constants;
import cps.server.ServerException;

/** Records daily statistical data to be used as a data source for generating weekly statistics. */
public class DailyStatistics implements Serializable {
  private static final long serialVersionUID = 1L;

  /** The day. */
  private LocalDate day;

  /** The lot id. */
  private int lotId;

  /** The number of realized orders. */
  private int realizedOrders;

  /** The number of canceled orders. */
  private int canceledOrders;

  /** The number of late arrivals. */
  private int lateArrivals;

  /** The number of complaints. */
  private int complaints;

  /** Instantiates a new daily statistics object.
   * @param day the day
   * @param lotId the lot id
   * @param realizedOrders the realized orders
   * @param canceledOrders the canceled orders
   * @param lateArrivals the late arrivals
   * @param complaints the complaints */
  public DailyStatistics(LocalDate day, int lotId, int realizedOrders, int canceledOrders, int lateArrivals, int complaints) {
    super();
    this.day = day;
    this.lotId = lotId;
    this.realizedOrders = realizedOrders;
    this.canceledOrders = canceledOrders;
    this.lateArrivals = lateArrivals;
    this.complaints = complaints;
  }

  /** Instantiates a new daily statistics object from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException on error */
  public DailyStatistics(ResultSet rs) throws SQLException {
    this(rs.getDate("day").toLocalDate(), rs.getInt("lot_id"), rs.getInt("realized_orders"), rs.getInt("canceled_orders"), rs.getInt("late_arrivals"),
        rs.getInt("complaints"));
  }

  public LocalDate getDay() {
    return day;
  }

  public void setDay(LocalDate day) {
    this.day = day;
  }

  public int getLotId() {
    return lotId;
  }

  public void setLotId(int lotId) {
    this.lotId = lotId;
  }

  public int getRealizedOrders() {
    return realizedOrders;
  }

  public void setRealizedOrders(int realizedOrders) {
    this.realizedOrders = realizedOrders;
  }

  public int getCanceledOrders() {
    return canceledOrders;
  }

  public void setCanceledOrders(int canceledOrders) {
    this.canceledOrders = canceledOrders;
  }

  public int getLateArrivals() {
    return lateArrivals;
  }

  public void setLateArrivals(int lateArrivals) {
    this.lateArrivals = lateArrivals;
  }

  public int getComplaints() {
    return complaints;
  }

  public void setComplaints(int complaints) {
    this.complaints = complaints;
  }

  /** Creates an empty entry in the table for specific date and lotId. All other
   * parameters are zero by default
   * @param conn the SQL connection
   * @param today the today
   * @param lotId the lot id
   * @return the daily statistics
   * @throws SQLException on error */
  public static DailyStatistics create(Connection conn, LocalDate today, int lotId) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_NEW_DAY);

    int field = 1;
    stmt.setDate(field++, Date.valueOf(today));
    stmt.setInt(field++, lotId);
    stmt.executeUpdate();

    stmt.close();
    return new DailyStatistics(today, lotId, 0, 0, 0, 0);

  }

  /** If a daily statistics entry for the given date exists in the database, the function returns the
   * corresponding entry. Else it creates a new entry initialized with zeros and returns it.
   * @param conn the SQL connection
   * @param _date the date
   * @param lotId the lot id
   * @return the daily statistics
   * @throws SQLException on error */
  public static DailyStatistics createIfNotExists(Connection conn, LocalDate _date, int lotId) throws SQLException {
    DailyStatistics item = null;

    PreparedStatement st = conn.prepareStatement(Constants.SQL_CHECK_DATE);
    st.setDate(1, Date.valueOf(_date));
    st.setInt(2, lotId);
    ResultSet rs = st.executeQuery();

    if (rs.wasNull()) {
      item = create(conn, _date, lotId);
      // if doesn't exists - create empty line with zeroes
    }

    rs.close();
    st.close();
    return item;
  }

  /** Increase realized order count by one for today in specific parking lot.
   * @param conn the SQL connection
   * @param lotId the lot id
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseRealizedOrder(Connection conn, int lotId) throws SQLException, ServerException {
    increaseRealizedOrder(conn, LocalDate.now(), lotId);
  }

  /** Increase realized order count by one for the specified parking lot and date.
   * @param conn the SQL connection
   * @param _date the date
   * @param lotId the lot id
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseRealizedOrder(Connection conn, LocalDate _date, int lotId) throws SQLException, ServerException {
    // check if line exists in database
    DailyStatistics entry = createIfNotExists(conn, _date, lotId);

    if (entry != null) {
      int index = 1;
      PreparedStatement stmt = conn.prepareStatement(Constants.SQL_INCREASE_REALIZED_ORDER);

      // Increase the number of realized orders
      stmt.setInt(index++, entry.getRealizedOrders() + 1);
      stmt.setDate(index++, Date.valueOf(_date));
      stmt.setInt(index++, lotId);

      stmt.executeUpdate();

      stmt.close();
    }
  }

  /** Return seven successive daily statistics entries for a specified lot.
   * Used for calculated weekly statistics.
   * @param conn the SQL connection
   * @param firstDay the first day
   * @param lotId the lot id
   * @return the entries
   * @throws SQLException on error */
  public static DailyStatistics[] getSevenDays(Connection conn, LocalDate firstDay, int lotId) throws SQLException {
    DailyStatistics[] item = new DailyStatistics[7];
    PreparedStatement st = null;
    ResultSet rs = null;
    LocalDate iDate = firstDay;

    for (int i = 0; i < 7; i++) {
      st = conn.prepareStatement(Constants.SQL_CHECK_DATE);
      st.setDate(1, Date.valueOf(iDate));
      st.setInt(2, lotId);
      rs = st.executeQuery();

      if (!rs.wasNull()) {
        item[i] = new DailyStatistics(rs);

      } else {
        // if doesn't exists - create empty line with zeroes
        item[i] = new DailyStatistics(iDate, lotId, 0, 0, 0, 0);
      }
      rs.close();
      st.close();
    }

    return item;
  }

  /** Return seven successive daily statistics entries for all lots.
   * Used for producing periodic reports.
   * @param conn the SQL connection
   * @param firstDay the first day
   * @return the entries
   * @throws SQLException on error */
  public static DailyStatistics[] getSevenDaysForPeriodic(Connection conn, LocalDate firstDay) throws SQLException {
    DailyStatistics[] item = new DailyStatistics[7];
    PreparedStatement st = null;
    ResultSet rs = null;
    LocalDate iDate = firstDay;

    for (int i = 0; i < 7; i++) {
      st = conn.prepareStatement(Constants.SQL_GET_DATE);
      st.setDate(1, Date.valueOf(iDate));
      rs = st.executeQuery();

      if (rs.next()) {
        item[i] = new DailyStatistics(iDate, 0, rs.getInt("realized_orders"), rs.getInt("canceled_orders"), rs.getInt("late_arrivals"),
            rs.getInt("inactive_slots"));

      } else {
        // if doesn't exists - create empty line with zeroes
        item[i] = new DailyStatistics(iDate, 0, 0, 0, 0, 0);
      }
      rs.close();
      st.close();
      iDate = iDate.plusDays(1);
    }

    return item;
  }

  /** Increase canceled order count by one for the specified parking lot and date.
   * @param conn the SQL connection
   * @param _date the date
   * @param lotId the lot id
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseCanceledOrder(Connection conn, LocalDate _date, int lotId) throws SQLException, ServerException {
    // check if line exists in database
    DailyStatistics entry = createIfNotExists(conn, _date, lotId);

    if (entry != null) {
      int index = 1;
      PreparedStatement stmt = conn.prepareStatement(Constants.SQL_INCREASE_CANCELED_ORDER);

      // Increase the number of canceled orders
      stmt.setInt(index++, entry.getCanceledOrders() + 1);
      stmt.setDate(index++, Date.valueOf(_date));
      stmt.setInt(index++, lotId);

      stmt.executeUpdate();

      stmt.close();
    }
  }

  /** Increase Late Arrival count by one for today in the specified parking lot.
   * @param conn the SQL connection
   * @param lotId the lot id
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseLateArrival(Connection conn, int lotId) throws SQLException, ServerException {
    increaseLateArrival(conn, LocalDate.now(), lotId);
  }

  /** Increase Late Arrival count by one for the specified parking lot and date.
   * @param conn the SQL connection
   * @param _date the date
   * @param lotId the lot id
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseLateArrival(Connection conn, LocalDate _date, int lotId) throws SQLException, ServerException {
    // check if line exists in database
    DailyStatistics entry = createIfNotExists(conn, _date, lotId);

    if (entry != null) {
      int index = 1;
      PreparedStatement stmt = conn.prepareStatement(Constants.SQL_INCREASE_LATE_ARRIVAL);

      // Increase the number of late arrivals
      stmt.setInt(index++, entry.getLateArrivals() + 1);
      stmt.setDate(index++, Date.valueOf(_date));
      stmt.setInt(index++, lotId);

      stmt.executeUpdate();

      stmt.close();
    }
  }

}