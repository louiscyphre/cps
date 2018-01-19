package cps.entities.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

// TODO: Auto-generated Javadoc
/** The Class MonthlyReport. */
@SuppressWarnings("unused")
public class MonthlyReport {

  /** The year. */
  private int year;

  /** The month. */
  private int month;

  /** The lot id. */
  private int lotId;

  /** The ord reserved. */
  private int ordReserved;

  /** The ord incidental. */
  private int ordIncidental;

  /** The ord regular. */
  private int ordRegular;

  /** The ord full. */
  private int ordFull;

  /** The coplaints count. */
  private int coplaintsCount;

  /** The disabled slots. */
  private int disabledSlots;

  /** Instantiates a new monthly report.
   * @param _year
   *        the year
   * @param _month
   *        the month
   * @param _lotid
   *        the lotid
   * @param _ordreserved
   *        the ordreserved
   * @param _ordincidental
   *        the ordincidental
   * @param _ordregular
   *        the ordregular
   * @param _ordfull
   *        the ordfull
   * @param _coplaintscount
   *        the coplaintscount
   * @param _disabledslots
   *        the disabledslots */
  public MonthlyReport(int _year, int _month, int _lotid, int _ordreserved, int _ordincidental, int _ordregular,
      int _ordfull, int _coplaintscount, int _disabledslots) {
    this.year = _year;
    this.month = _month;
    this.lotId = _lotid;
    this.ordReserved = _ordreserved;
    this.ordIncidental = _ordincidental;
    this.ordRegular = _ordregular;
    this.ordFull = _ordfull;
    this.coplaintsCount = _coplaintscount;
    this.disabledSlots = _disabledslots;
  }

  /** Instantiates a new monthly report.
   * @param rs
   *        the rs
   * @throws SQLException
   *         the SQL exception */
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

  /** Creates the.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void create(Connection conn, int year, int month, int lotid) throws SQLException {
    PreparedStatement stmt = conn
        .prepareStatement("INSERT INTO monthly_report VALUES (?,?,?,default,default,default,default,default,default)");
    int i = 1;
    stmt.setInt(i++, year);
    stmt.setInt(i++, month);
    stmt.setInt(i++, lotid);
    stmt.executeUpdate();
    stmt.close();
  }

  /** Creates the if not exists.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void createIfNotExists(Connection conn, int year, int month, int lotid) throws SQLException {
    PreparedStatement stmt = conn
        .prepareStatement("SELECT count(*) FROM monthly_report WHERE year=? AND month=? AND lot_id=?");
    int i = 1;
    stmt.setInt(i++, year);
    stmt.setInt(i++, month);
    stmt.setInt(i++, lotid);
    ResultSet rs = stmt.executeQuery();
    if (!rs.next()) {
      create(conn, year, month, lotid);
    }
    stmt.close();
  }

  /** Find report.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @return the monthly report
   * @throws SQLException
   *         the SQL exception */
  public static MonthlyReport findReport(Connection conn, int year, int month, int lotid) throws SQLException {
    PreparedStatement stmt = conn
        .prepareStatement("SELECT count(*) FROM monthly_report WHERE year=? AND month=? AND lot_id=?");
    int i = 1;
    stmt.setInt(i++, year);
    stmt.setInt(i++, month);
    stmt.setInt(i++, lotid);
    ResultSet rs = stmt.executeQuery();
    MonthlyReport result=new MonthlyReport(rs);
    
    rs.close();
    stmt.close();
    
    return result;
    
  }

  /** Update.
   * @param conn
   *        the conn
   * @throws SQLException
   *         the SQL exception */
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
    if (stmt.executeUpdate() != 1) {
      throw new SQLException(
          String.format("Failed to update the required field in MonthReport. Year=%d, month=%d, lot=%d", this.year,
              this.month, this.lotId));
    }
    stmt.close();
  }

  /** Increase reserved.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void increaseReserved(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = findReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordReserved++;
    rep.update(conn);
  }

  /** Increase incidental.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void increaseIncidental(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = findReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordIncidental++;
    rep.update(conn);
  }

  /** Increase regular.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void increaseRegular(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = findReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordRegular++;
    rep.update(conn);
  }

  /** Increase full.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void increaseFull(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = findReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordFull++;
    rep.update(conn);
  }

  /** Increase complaints.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void increaseComplaints(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = findReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordRegular++;
    rep.update(conn);
  }

  /** Count disabled cells.
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lotid
   * @throws SQLException
   *         the SQL exception */
  public static void countDisabledCells(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = findReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.disabledSlots = DisabledCellsStatistics.countDisabledCells(conn, lotid, LocalDateTime.of(year, month, 1, 0, 0),
        LocalDateTime.of(year, month + 1, 1, 0, 0).minusDays(1));
    rep.update(conn);
  }

  /** Gets the monthly report. It includes all the required data for one month.
   * @author Tegra
   * @param conn
   *        the conn
   * @param year
   *        the year
   * @param month
   *        the month
   * @param lotid
   *        the lot id
   * @return the monthly report MonthlyReport type
   * @throws SQLException
   *         the SQL exception */
  public static MonthlyReport getMonthlyReport(Connection conn, int year, int month, int lotid) throws SQLException {
    countDisabledCells(conn, year, month, lotid);
    MonthlyReport rep = findReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
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

}
