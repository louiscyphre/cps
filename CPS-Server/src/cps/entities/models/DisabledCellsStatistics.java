package cps.entities.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class DisabledCellsStatistics {

  private int           lotId;
  private LocalDateTime dateDisabled;
  private int           width;
  private int           height;
  private int           depth;
  private LocalDateTime dateEnabled;

  public DisabledCellsStatistics(int _lotid, LocalDateTime _dateDisabled, int _width, int _height, int _depth,
      LocalDateTime _dateEnabled) {
    this.lotId = _lotid;
    this.dateDisabled = _dateDisabled;
    this.width = _width;
    this.height = _height;
    this.depth = _depth;
    this.dateEnabled = _dateEnabled;
  }

  public DisabledCellsStatistics(ResultSet rs) throws SQLException {
    this.lotId = rs.getInt("lotid");
    this.dateDisabled = rs.getTimestamp("date_disabled").toLocalDateTime();
    this.width = rs.getInt("width");
    this.height = rs.getInt("height");
    this.depth = rs.getInt("depth");
    this.dateEnabled = rs.getTimestamp("date_enabled").toLocalDateTime();
  }

  public static void create(Connection conn, int _lotid, int _width, int _height, int _depth) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement("INSERT INTO disabled_slots_table VALUES(?,?,?,?,?,default)");
    int i = 1;
    stmt.setInt(i++, _lotid);
    stmt.setTimestamp(i++, Timestamp.valueOf(LocalDateTime.now()));
    stmt.setInt(i++, _width);
    stmt.setInt(i++, _height);
    stmt.setInt(i++, _depth);
    stmt.executeUpdate();
  }

  public static void markfixed(Connection conn, int _lotid, int _width, int _height, int _depth) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(
        "UPDATE disabled_slots_table SET date_enabled='?' WHERE lotid=? AND width=? AND height=? AND depth=? AND date_enabled is null");
    int i = 1;
    stmt.setTimestamp(i++, Timestamp.valueOf(LocalDateTime.now()));
    stmt.setInt(i++, _lotid);
    stmt.setInt(i++, _width);
    stmt.setInt(i++, _height);
    stmt.setInt(i++, _depth);
    stmt.executeUpdate();
  }

  public static int countDisabledCells(Connection conn, int lotid, LocalDateTime from, LocalDateTime to)
      throws SQLException {
    String helper = "";
    // Count distinct selection
    helper += "SELECT count(DISTINCT lotid,width,height,depth) FROM disabled_slots_table ";
    // Was disabled during
    helper += "WHERE lotid=? AND ((? <= date_disabled AND date_disabled <= ?) ";
    // Was enabled during
    helper += "OR (? <= date_enabled AND date_enabled <= ?) ";
    // Was disabled before and wasn't enabled at all or was enabled after
    helper += "OR (date_disabled <= ? AND (date_enabled is null OR date_enabled >= ?)))";

    PreparedStatement stmt = conn.prepareStatement(helper);
    int i = 1;
    stmt.setInt(i++, lotid);
    stmt.setTimestamp(i++, Timestamp.valueOf(from));
    stmt.setTimestamp(i++, Timestamp.valueOf(to));
    stmt.setTimestamp(i++, Timestamp.valueOf(from));
    stmt.setTimestamp(i++, Timestamp.valueOf(to));
    stmt.setTimestamp(i++, Timestamp.valueOf(from));
    stmt.setTimestamp(i++, Timestamp.valueOf(to));
    return stmt.executeUpdate();
  }

}
