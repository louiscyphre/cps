package cps.entities.models;

import static cps.common.Utilities.debugPrintln;

import java.io.Serializable;
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
public class MonthlyReport implements Serializable {
  private static final long serialVersionUID = 1L;

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
  private int complaintsCount;

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
   * @param _complaintscount the number of complaints
   * @param complaintsclosedcount the number of closed complaints
   * @param complaintsrefundedcount the number of refunded complaints
   * @param _disabledslots the number disabled slots
   * @param field the field */
  public MonthlyReport(int _year, int _month, int _lotid, int _ordreserved, int _ordincidental, int _ordregular,
      int _ordfull, int _complaintscount, int complaintsclosedcount, int complaintsrefundedcount, int _disabledslots,
      String field) {
    this.year = _year;
    this.month = _month;
    this.lotId = _lotid;
    this.ordReserved = _ordreserved;
    this.ordIncidental = _ordincidental;
    this.ordRegular = _ordregular;
    this.ordFull = _ordfull;
    this.complaintsCount = _complaintscount;
    this.complaintsClosedCount = complaintsclosedcount;
    this.complaintsRefundedCount = complaintsrefundedcount;
    this.disabledSlots = _disabledslots;
    this.fieldText = field;
  }

  /** Instantiates a new monthly report from the columns of a ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException on error */
  public MonthlyReport(ResultSet rs) throws SQLException {
    this.year = rs.getInt("year");
    this.month = rs.getInt("month");
    this.lotId = rs.getInt("lot_id");
    this.ordReserved = rs.getInt("ordered_reserved");
    this.ordIncidental = rs.getInt("ordered_incidental");
    this.ordRegular = rs.getInt("ordered_regular");
    this.ordFull = rs.getInt("ordered_full");
    this.complaintsCount = rs.getInt("complaints_count");
    this.complaintsClosedCount = rs.getInt("complaints_closed_count");
    this.complaintsRefundedCount = rs.getInt("complaints_refunded_count");
    this.disabledSlots = rs.getInt("disabled_slots");
  }

  /** Creates Monthly Report in the database.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lot id
   * @return Newly created Monthly Report
   * @throws SQLException on error */
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
   * @throws SQLException on error */
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
    this.complaintsCount += rep.complaintsCount;
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
   * @throws SQLException on error
   * @throws ServerException on error */
  public static MonthlyReport findReportNotNull(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport report = findReport(conn, year, month, lotid);

    if (report == null) {
      throw new ServerException(
          String.format("Failed to find information for monthly report: year=%d, month=%d, lot=%d", year, month, lotid));
    }

    return report;
  }

  /** Attempt to find a MonthlyReport with the given parameters, create new if doesn't exist.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException on error */
  public static MonthlyReport createOrFind(Connection conn, int year, int month, int lotid) throws SQLException {
    MonthlyReport report = findReport(conn, year, month, lotid);

    if (report == null) {
      report = create(conn, year, month, lotid);
    }

    return report;
  }

  /** Attempts to create or find a report, throws an exception if failed.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException on error
   * @throws ServerException on error */
  public static MonthlyReport createOrFindNotNull(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport report = createOrFind(conn, year, month, lotid);

    if (report == null) {
      throw new ServerException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }

    return report;
  }

  /** Update the database record for this MonthlyReport.
   * @param conn the SQL connection
   * @throws SQLException on error */
  public void update(Connection conn) throws SQLException {
    String helper = "UPDATE monthly_report SET ordered_reserved=?, ordered_incidental=?, ordered_regular=?, ";
    helper += "ordered_full=?, complaints_count=?, complaints_closed_count=?, complaints_refunded_count=?, disabled_slots=?";
    helper += " WHERE year=? AND month=? AND lot_id=?";
    PreparedStatement stmt = conn.prepareStatement(helper);
    int i = 1;
    stmt.setInt(i++, this.ordReserved);
    stmt.setInt(i++, this.ordIncidental);
    stmt.setInt(i++, this.ordRegular);
    stmt.setInt(i++, this.ordFull);
    stmt.setInt(i++, this.complaintsCount);
    stmt.setInt(i++, this.complaintsClosedCount);
    stmt.setInt(i++, this.complaintsRefundedCount);
    stmt.setInt(i++, this.disabledSlots);
    stmt.setInt(i++, this.year);
    stmt.setInt(i++, this.month);
    stmt.setInt(i++, this.lotId);
    stmt.executeUpdate();
    stmt.close();
  }

  /** Increase number of parking reservations.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseReserved(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    debugPrintln("MonthlyStatistics: incrementing ordReserved: %d -> %d", rep.ordReserved, rep.ordReserved + 1);
    rep.ordReserved++;
    rep.update(conn);
  }

  /** Increase number of incidental parkings.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseIncidental(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    debugPrintln("MonthlyStatistics: incrementing ordIncidental: %d -> %d", rep.ordIncidental, rep.ordIncidental + 1);
    rep.ordIncidental++;
    rep.update(conn);
  }

  /** Increase number of regular subscriptions.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseRegular(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    debugPrintln("MonthlyStatistics: incrementing ordRegular: %d -> %d", rep.ordRegular, rep.ordRegular + 1);
    rep.ordRegular++;
    rep.update(conn);
  }

  /** Increase number of full subscriptions.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseFull(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    debugPrintln("MonthlyStatistics: incrementing ordFull: %d -> %d", rep.ordFull, rep.ordFull + 1);
    rep.ordFull++;
    rep.update(conn);
  }

  /** Increase number of complaints.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseComplaints(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    debugPrintln("MonthlyStatistics: incrementing complaintsCount: %d -> %d", rep.complaintsCount, rep.complaintsCount + 1);
    rep.complaintsCount++;
    rep.update(conn);
  }

  /** Increase closed complaints count.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseClosed(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    debugPrintln("MonthlyStatistics: incrementing complaintsClosedCount: %d -> %d", rep.complaintsClosedCount, rep.complaintsClosedCount + 1);
    rep.complaintsClosedCount++;
    rep.update(conn);
  }

  /** Increase refunded complaints count.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseRefunded(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    debugPrintln("MonthlyStatistics: incrementing complaintsRefundedCount: %d -> %d", rep.complaintsRefundedCount, rep.complaintsRefundedCount + 1);
    rep.complaintsRefundedCount++;
    rep.update(conn);
  }

  /** Count disabled cells.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lot id
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void countDisabledCells(Connection conn, int year, int month, int lotid)
      throws SQLException, ServerException {
    MonthlyReport rep = createOrFindNotNull(conn, year, month, lotid);
    LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime end = start.plusMonths(1).minusDays(1);
    rep.disabledSlots = DisabledCellsStatistics.countDisabledCells(conn, lotid, start, end);
    debugPrintln("MonthlyStatistics: counting disabledSlots: %d", rep.disabledSlots);
    rep.update(conn);
  }

  /** Retrieve the monthly report from the database.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @return the monthly report
   * @throws SQLException on error
   * @throws ServerException on error */
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

  public int getComplaintsCount() {
    return complaintsCount;
  }

  public void setComplaintsCount(int complaintsCount) {
    this.complaintsCount = complaintsCount;
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
