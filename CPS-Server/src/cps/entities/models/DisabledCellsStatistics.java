package cps.entities.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import cps.server.database.QueryBuilder;

/** Records statistical data about enable/disable actions performed on Parking Cells (car storage inside of a parking lot).
 * Allows calculating the number of disabled cells in a given period, and the amount of time during which cells were inactive. */
@SuppressWarnings("unused")
public class DisabledCellsStatistics {

  /** The lot id. */
  private int lotId;

  /** The date when the cell was disabled. */
  private LocalDateTime dateDisabled;

  /** The cell's width coordinate. */
  private int width;

  /** The cell's height coordinate. */
  private int height;

  /** The cell's depth. */
  private int depth;

  /** The date when the cell was enabled. */
  private LocalDateTime dateEnabled;

  /** Instantiates a new disabled cells statistics object.
   * @param _lotid the lotid
   * @param _dateDisabled the date disabled
   * @param _width the width
   * @param _height the height
   * @param _depth the depth
   * @param _dateEnabled the date enabled */
  public DisabledCellsStatistics(int _lotid, LocalDateTime _dateDisabled, int _width, int _height, int _depth, LocalDateTime _dateEnabled) {
    this.lotId = _lotid;
    this.dateDisabled = _dateDisabled;
    this.width = _width;
    this.height = _height;
    this.depth = _depth;
    this.dateEnabled = _dateEnabled;
  }

  /** Instantiates a new disabled cells statistics object from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException the SQL exception */
  public DisabledCellsStatistics(ResultSet rs) throws SQLException {
    this.lotId = rs.getInt("lotid");
    this.dateDisabled = rs.getTimestamp("date_disabled").toLocalDateTime();
    this.width = rs.getInt("width");
    this.height = rs.getInt("height");
    this.depth = rs.getInt("depth");
    this.dateEnabled = rs.getTimestamp("date_enabled").toLocalDateTime();
  }

  /** Create a new disabled cells statistics object.
   * @param conn the SQL connection
   * @param _lotid the lotid
   * @param _width the width
   * @param _height the height
   * @param _depth the depth
   * @throws SQLException the SQL exception */
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

  /** Find the most disabled cells statistics entry which recorded that a cell was disabled, but has not been enabled yet, and mark it as enabled.
   * @param conn the SQL connection
   * @param _lotid the lotid
   * @param _width the width
   * @param _height the height
   * @param _depth the depth
   * @throws SQLException the SQL exception */
  public static void markFixed(Connection conn, int _lotid, int _width, int _height, int _depth) throws SQLException {
    PreparedStatement stmt = conn
        .prepareStatement("UPDATE disabled_slots_table SET date_enabled=? WHERE lotid=? AND width=? AND height=? AND depth=? AND date_enabled is null");
    int i = 1;
    stmt.setTimestamp(i++, Timestamp.valueOf(LocalDateTime.now()));
    stmt.setInt(i++, _lotid);
    stmt.setInt(i++, _width);
    stmt.setInt(i++, _height);
    stmt.setInt(i++, _depth);
    stmt.executeUpdate();
  }

  /** Count the number of disabled cells during a given period.
   * @param conn the SQL connection
   * @param lotid the lot id
   * @param from starting point of the period
   * @param to ending point of the period
   * @return the number of disabled cells
   * @throws SQLException the SQL exception */
  public static int countDisabledCells(Connection conn, int lotid, LocalDateTime from, LocalDateTime to) throws SQLException {
    return new QueryBuilder<Integer>(String.join(" ",
        // Count distinct selection
        "SELECT count(DISTINCT lotid,width,height,depth) FROM disabled_slots_table",
        // Was disabled during
        "WHERE lotid=? AND ((? <= date_disabled AND date_disabled <= ?)",
        // Was enabled during
        "OR (? <= date_enabled AND date_enabled <= ?)",
        // Was disabled before and wasn't enabled at all or was enabled after
        "OR (date_disabled <= ? AND (date_enabled is null OR date_enabled >= ?)))"))
    .withFields(statement -> {
      int i = 1;
      statement.setInt(i++, lotid);
      statement.setTimestamp(i++, Timestamp.valueOf(from));
      statement.setTimestamp(i++, Timestamp.valueOf(to));
      statement.setTimestamp(i++, Timestamp.valueOf(from));
      statement.setTimestamp(i++, Timestamp.valueOf(to));
      statement.setTimestamp(i++, Timestamp.valueOf(from));
      statement.setTimestamp(i++, Timestamp.valueOf(to));
    }).fetchResult(conn, result -> result.getInt(1));
  }

  public static double countDisableHours(Connection conn, LocalDate from, LocalDate to) throws SQLException {
    long helper = 0;
    LocalDateTime start, end;
    String qhelper = "SELECT date_disabled,date_enabled FROM disabled_slots_table ";
    qhelper += "WHERE (? <= date_disabled AND date_disabled <= ?) ";
    qhelper += "OR (? <= date_enabled AND date_enabled <= ?)";
    qhelper += "OR (date_disabled <= ? AND (date_enabled is null OR date_enabled >= ?))";

    Timestamp _from = Timestamp.valueOf(from.atStartOfDay());
    Timestamp _to = Timestamp.valueOf(to.atTime(LocalTime.MAX));

    PreparedStatement stmt = conn.prepareStatement(qhelper);

    int i = 1;

    stmt.setTimestamp(i++, _from);
    stmt.setTimestamp(i++, _to);
    stmt.setTimestamp(i++, _from);
    stmt.setTimestamp(i++, _to);
    stmt.setTimestamp(i++, _from);
    stmt.setTimestamp(i++, _to);

    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
      start = rs.getTimestamp("date_disabled").toLocalDateTime();
      if (start.isBefore(from.atStartOfDay())) {
        start = from.atStartOfDay();
      }
      if (rs.getTimestamp("date_enabled") == null) {
        end = _to.toLocalDateTime();
      } else {
        end = rs.getTimestamp("date_enabled").toLocalDateTime();
        if (end.isAfter(_to.toLocalDateTime())) {
          end = _to.toLocalDateTime();
        }
      }
      helper += start.until(end, ChronoUnit.MINUTES);
    }
    rs.close();
    stmt.close();
    return (double) (helper / 60);
  }

}
