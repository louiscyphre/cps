package cps.entities.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import cps.server.ServerException;

/** Consolidates statistical data used for quarterly reports.
 * A quarterly report is represented as a collection of monthly reports. */
@SuppressWarnings("unused")
public class MonthlyReport {

  /** The year. */
  private int year;

  /** The month. */
  private int month;

  /** The lot id. */
  private int lotId;

  /** Amount of Reserved parkings. */
  private int ordReserved;

  /** Amount of Incidendal parkings. */
  private int ordIncidental;

  /** Amount of Regular subscriptions. */
  private int ordRegular;

  /** Amount of Full subscriptions. */
  private int ordFull;

  /** Amount of complaints. */
  private int coplaintsCount;

  /** The complaints closed count. */
  private int complaintsClosedCount;

  /** The complaints refunded count. */
  private int complaintsRefundedCount;

  /** Amount of disabled slots. */
  private int disabledSlots;

  /** The label for this data entry. */
  private String fieldText;

  /** Instantiates a new monthly report.
   * @param _year the year
   * @param _month the month
   * @param _lotid the lotid
   * @param _ordreserved the number of reserved parkings
   * @param _ordincidental the number of incidental parkings
   * @param _ordregular the number of regular subscriptions
   * @param _ordfull the number of full subscriptions
   * @param _coplaintscount the number of complaints
   * @param _disabledslots the number disabled slots */
  public MonthlyReport(int _year, int _month, int _lotid, int _ordreserved, int _ordincidental, int _ordregular,
      int _ordfull, int _coplaintscount, int complaintsclosedcount, int complaintsrefundedcount, int _disabledslots,
      String field) {
    this.year = _year;
    this.month = _month;
    this.lotId = _lotid;
    this.ordReserved = _ordreserved;
    this.ordIncidental = _ordincidental;
    this.ordRegular = _ordregular;
    this.ordFull = _ordfull;
    this.coplaintsCount = _coplaintscount;
    this.complaintsClosedCount = complaintsclosedcount;
    this.complaintsRefundedCount = complaintsrefundedcount;
    this.disabledSlots = _disabledslots;
    this.fieldText = field;
  }

  /** Instantiates a new monthly report from the columns of a ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException the SQL exception */
  public MonthlyReport(ResultSet rs) throws SQLException {
    this.year = rs.getInt("year");
    this.month = rs.getInt("month");
    this.lotId = rs.getInt("lot_id");
    this.ordReserved = rs.getInt("ordered_reserved");
    this.ordIncidental = rs.getInt("ordered_incidental");
    this.ordRegular = rs.getInt("ordered_regular");
    this.ordFull = rs.getInt("ordered_full");
    this.coplaintsCount = rs.getInt("complaints_count");
    this.disabledSlots = rs.getInt("disabled_slots");
  }

  /** Creates Monthly Report in the database.
   * @param conn the SQL connectionection
   * @param year the year
   * @param month the month
   * @param lotid the lot id
   * @return Newly created Monthly Report
   * @throws SQLException the SQL exception */
  public static MonthlyReport create(Connection conn, int year, int month, int lotid) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(
        "INSERT INTO monthly_report VALUES(?,?,?,default,default,default,default,default,default,default,default)");
    int i = 1;
    stmt.setInt(i++, year);
    stmt.setInt(i++, month);
    stmt.setInt(i++, lotid);
    stmt.executeUpdate();
    stmt.close();
    return new MonthlyReport(year, month, lotid, 0, 0, 0, 0, 0, 0, 0, 0, Month.of(month).toString());
  }

  /** Find a report in the database.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException the SQL exception */
  public static MonthlyReport findReport(Connection conn, int year, int month, int lotid) throws SQLException {
    MonthlyReport result = null;
    PreparedStatement stmt = conn
        .prepareStatement("SELECT * FROM monthly_report WHERE year=? AND month=? AND lot_id=?");
    int i = 1;
    stmt.setInt(i++, year);
    stmt.setInt(i++, month);
    stmt.setInt(i++, lotid);
    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
      result = new MonthlyReport(rs);
    }

    rs.close();
    stmt.close();

    return result;
  }

  /** Add the numeric fields of another report to this report.
   * @param rep the other report */
  public void add(MonthlyReport rep) {
    this.ordIncidental += rep.ordIncidental;
    this.ordReserved += rep.ordReserved;
    this.ordRegular += rep.ordRegular;
    this.ordFull += rep.ordFull;
    this.coplaintsCount += rep.coplaintsCount;
    this.complaintsClosedCount += rep.complaintsClosedCount;
    this.complaintsRefundedCount += rep.complaintsRefundedCount;
    this.disabledSlots += rep.disabledSlots;
  }
  
  /** Try to find a report, throw an exception if not found.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static MonthlyReport findReportNotNull(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport report = findReport(conn, year, month, lotid);

    if (report == null) {
      throw new ServerException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }

    return report;
  }

  /** Create or find a MonthlyReport.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException the SQL exception */
  public static MonthlyReport createOrFind(Connection conn, int year, int month, int lotid) throws SQLException {
    MonthlyReport report = findReport(conn, year, month, lotid);

    if (report == null) {
      report = create(conn, year, month, lotid);
    }

    return report;
  }

  /** Creates the or find not null.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static MonthlyReport createOrFindNotNull(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport report = createOrFind(conn, year, month, lotid);

    if (report == null) {
      throw new ServerException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }

    return report;
  }

  /** Update.
   * @param conn the SQL connection
   * @throws SQLException the SQL exception */
  public void update(Connection conn) throws SQLException {
    String helper = "UPDATE monthly_report SET ordered_reserved=?, ordered_incidental=?, ordered_regular=?, ";
    helper += "ordered_full=?, complaints_count=?, disabled_slots=?";
    helper += " WHERE year=? AND month=? AND lot_id=?";
    PreparedStatement stmt = conn.prepareStatement(helper);
    int i = 1;
    stmt.setInt(i++, this.ordReserved);
    stmt.setInt(i++, this.ordIncidental);
    stmt.setInt(i++, this.ordRegular);
    stmt.setInt(i++, this.ordFull);
    stmt.setInt(i++, this.coplaintsCount);
    stmt.setInt(i++, this.disabledSlots);
    stmt.setInt(i++, this.year);
    stmt.setInt(i++, this.month);
    stmt.setInt(i++, this.lotId);
    stmt.executeUpdate();
    stmt.close();
  }

  /** Increase reserved.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseReserved(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.ordReserved++;
    rep.update(conn);
  }

  /** Increase incidental.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseIncidental(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.ordIncidental++;
    rep.update(conn);
  }

  /** Increase count of regular subscriptions.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseRegular(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.ordRegular++;
    rep.update(conn);
  }

  /** Increase count of full subscriptions.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseFull(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.ordFull++;
    rep.update(conn);
  }

  /** Increase complaints.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseComplaints(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.coplaintsCount++;
    rep.update(conn);
  }

  /** Increase closed complaints count.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseClosed(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.complaintsClosedCount++;
    rep.update(conn);
  }

  /** Increase refunded complaints count.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseRefunded(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.complaintsRefundedCount++;
    rep.update(conn);
  }

  /** Count disabled cells.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void countDisabledCells(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    rep.disabledSlots = DisabledCellsStatistics.countDisabledCells(conn, lotid, LocalDateTime.of(year, month, 1, 0, 0),
        LocalDateTime.of(year, month + 1, 1, 0, 0).minusDays(1));
    rep.update(conn);
  }

  /** Retrieve the monthly report from the database.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static MonthlyReport getMonthlyReport(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    countDisabledCells(conn, year, month, lotid);
    MonthlyReport rep = findReportNotNull(conn, year, month, lotid);
    return rep;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getLotId() {
    return lotId;
  }

  public void setLotId(int lotId) {
    this.lotId = lotId;
  }

  public int getOrdReserved() {
    return ordReserved;
  }

  public void setOrdReserved(int ordReserved) {
    this.ordReserved = ordReserved;
  }

  public int getOrdIncidental() {
    return ordIncidental;
  }

  public void setOrdIncidental(int ordIncidental) {
    this.ordIncidental = ordIncidental;
  }

  public int getOrdRegular() {
    return ordRegular;
  }

  public void setOrdRegular(int ordRegular) {
    this.ordRegular = ordRegular;
  }

  public int getOrdFull() {
    return ordFull;
  }

  public void setOrdFull(int ordFull) {
    this.ordFull = ordFull;
  }

  public int getCoplaintsCount() {
    return coplaintsCount;
  }

  public void setCoplaintsCount(int coplaintsCount) {
    this.coplaintsCount = coplaintsCount;
  }

  public int getDisabledSlots() {
    return disabledSlots;
  }

  public void setDisabledSlots(int disabledSlots) {
    this.disabledSlots = disabledSlots;
  }

  public int getComplaintsClosedCount() {
    return complaintsClosedCount;
  }

  public void setComplaintsClosedCount(int complaintsClosedCount) {
    this.complaintsClosedCount = complaintsClosedCount;
  }

  public int getComplaintsRefundedCount() {
    return complaintsRefundedCount;
  }

  public void setComplaintsRefundedCount(int complaintsRefundedCount) {
    this.complaintsRefundedCount = complaintsRefundedCount;
  }

}
