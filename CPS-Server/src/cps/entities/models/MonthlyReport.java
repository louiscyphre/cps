package cps.entities.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class MonthlyReport {

  /** The year. */
  private int year;
  /** The month */
  private int month;
  private int lotId;
  private int ordReserved;
  private int ordIncidental;
  private int ordRegular;
  private int ordFull;
  private int coplaintsCount;
  private int disabledSlots;

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

  public static void create(Connection conn, int year, int month, int lotid) throws SQLException {
    PreparedStatement stmt = conn
        .prepareStatement("INSERT INTO monthly_report VALUES (?,?,?,default,default,default,default,default,default)");
    int i = 1;
    stmt.setInt(i++, year);
    stmt.setInt(i++, month);
    stmt.setInt(i++, lotid);
    stmt.executeUpdate();
  }

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
  }

  public static MonthlyReport getReport(Connection conn, int year, int month, int lotid) throws SQLException {
    PreparedStatement stmt = conn
        .prepareStatement("SELECT count(*) FROM monthly_report WHERE year=? AND month=? AND lot_id=?");
    int i = 1;
    stmt.setInt(i++, year);
    stmt.setInt(i++, month);
    stmt.setInt(i++, lotid);
    ResultSet rs = stmt.executeQuery();
    return new MonthlyReport(rs);
  }

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
  }

  public static void increaseReserved(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = getReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordReserved++;
    rep.update(conn);
  }

  public static void increaseIncidental(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = getReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordIncidental++;
    rep.update(conn);
  }
  
  public static void increaseRegular(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = getReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordRegular++;
    rep.update(conn);
  }
  
  public static void increaseFull(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = getReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordFull++;
    rep.update(conn);
  }
  
  public static void increaseComplaints(Connection conn, int year, int month, int lotid) throws SQLException {
    createIfNotExists(conn, year, month, lotid);
    MonthlyReport rep = getReport(conn, year, month, lotid);
    if (rep == null) {
      throw new SQLException(
          String.format("Failed to get report from Monthly report. year=%d,month=%d,lot=%d", year, month, lotid));
    }
    rep.ordRegular++;
    rep.update(conn);
  }
  
}
