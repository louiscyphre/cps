package cps.entities.models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mysql.jdbc.Connection;

import cps.common.Constants;

// TODO: Auto-generated Javadoc
/** The Class WeeklyStatistics. */
public class WeeklyStatistics implements Serializable {
  private static final long serialVersionUID = 1L;
  // `start` date NOT NULL,
  // `lot_id` int(10) NOT NULL,
  // `realized_orders_mean` float NOT NULL DEFAULT '0',
  // `realized_orders_median` float NOT NULL DEFAULT '0',
  // `canceled_orders_mean` float NOT NULL DEFAULT '0',
  // `canceled_orders_median` float NOT NULL DEFAULT '0',
  // `late_arrivals_mean` float NOT NULL DEFAULT '0',
  // `late_arrivals_median` float NOT NULL DEFAULT '0',
  // `realized_orders_dist` varchar(300) NOT NULL,
  // `canceled_orders_dist` varchar(300) NOT NULL,
  // `late_arrivals_dist` varchar(300) NOT NULL,
  private LocalDate start;
  private int       lotID;
  private float     realizedOrdersMean;
  private float     canceledOrdersMean;
  private float     lateArrivalsMean;
  private float     realizedOrdersMedian;
  private float     canceledOrdersMedian;
  private float     lateArrivalsMedian;
  private String    realizedOrdersDist;
  private String    canceledOrdersDist;
  private String    lateArrivalsDist;

  /** Instantiates a new weekly statistics.
   * @param start the start
   * @param lotID the lot ID
   * @param realizedOrdersMean the realized orders mean
   * @param canceledOrdersMean the canceled orders mean
   * @param lateArrivalsMean the late arrivals mean
   * @param realizedOrdersMedian the realized orders median
   * @param canceledOrdersMedian the canceled orders median
   * @param lateArrivalsMedian the late arrivals median
   * @param realizedOrdersDist the realized orders dist
   * @param canceledOrdersDist the canceled orders dist
   * @param lateArrivalsDist the late arrivals dist */
  public WeeklyStatistics(LocalDate start, int lotID, float realizedOrdersMean, float canceledOrdersMean,
      float lateArrivalsMean, float realizedOrdersMedian, float canceledOrdersMedian, float lateArrivalsMedian,
      String realizedOrdersDist, String canceledOrdersDist, String lateArrivalsDist) {
    super();
    this.start = start;
    this.setLotID(lotID);
    this.realizedOrdersMean = realizedOrdersMean;
    this.canceledOrdersMean = canceledOrdersMean;
    this.lateArrivalsMean = lateArrivalsMean;
    this.realizedOrdersMedian = realizedOrdersMedian;
    this.canceledOrdersMedian = canceledOrdersMedian;
    this.lateArrivalsMedian = lateArrivalsMedian;
    this.realizedOrdersDist = realizedOrdersDist;
    this.canceledOrdersDist = canceledOrdersDist;
    this.lateArrivalsDist = lateArrivalsDist;
  }

  /** Instantiates a new weekly statistics.
   * @param rs the rs
   * @throws SQLException the SQL exception */
  public WeeklyStatistics(ResultSet rs) throws SQLException {
    this(rs.getDate(1).toLocalDate(), rs.getInt(2), rs.getFloat(3), rs.getFloat(4), rs.getFloat(5), rs.getFloat(6),
        rs.getFloat(7), rs.getFloat(8), rs.getString(9), rs.getString(10), rs.getString(11));
  }

  /** Creates the.
   * @param conn the conn
   * @param start the start
   * @param lotID the lot ID
   * @param realizedOrdersMean the realized orders mean
   * @param canceledOrdersMean the canceled orders mean
   * @param lateArrivalsMean the late arrivals mean
   * @param realizedOrdersMedian the realized orders median
   * @param canceledOrdersMedian the canceled orders median
   * @param lateArrivalsMedian the late arrivals median
   * @param realizedOrdersDist the realized orders dist
   * @param canceledOrdersDist the canceled orders dist
   * @param lateArrivalsDist the late arrivals dist
   * @return the weekly statistics
   * @throws SQLException the SQL exception */
  public static WeeklyStatistics create(Connection conn, LocalDate start, int lotID, float realizedOrdersMean,
      float canceledOrdersMean, float lateArrivalsMean, float realizedOrdersMedian, float canceledOrdersMedian,
      float lateArrivalsMedian, String realizedOrdersDist, String canceledOrdersDist, String lateArrivalsDist)
      throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_WEEKLY_STATISTICS);

    int field = 0;

    statement.setDate(field++, Date.valueOf(start));
    statement.setInt(field++, lotID);
    statement.setFloat(field++, realizedOrdersMean);
    statement.setFloat(field++, canceledOrdersMean);
    statement.setFloat(field++, lateArrivalsMean);
    statement.setFloat(field++, realizedOrdersMedian);
    statement.setFloat(field++, canceledOrdersMedian);
    statement.setFloat(field++, lateArrivalsMedian);
    statement.setString(field++, realizedOrdersDist);
    statement.setString(field++, canceledOrdersDist);
    statement.setString(field++, lateArrivalsDist);

    statement.executeUpdate();
    statement.close();

    return new WeeklyStatistics(start, lotID, realizedOrdersMean, canceledOrdersMean, lateArrivalsMean,
        realizedOrdersMedian, canceledOrdersMedian, lateArrivalsMedian, realizedOrdersDist, canceledOrdersDist,
        lateArrivalsDist);
  }

  /** Creates the for.
   * @param conn the conn
   * @param day the day
   * @param lotid the lotid */
  public static void createFor(Connection conn, LocalDateTime day, int lotid) {
    // XXX I'm here

  }

  /** Find.
   * @param conn the conn
   * @param start the start
   * @param lotID the lot ID
   * @return the weekly statistics
   * @throws SQLException the SQL exception */
  public static WeeklyStatistics find(Connection conn, LocalDate start, int lotID) throws SQLException {
    WeeklyStatistics item = null;
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_WEEKLY_STATISTICS);

    int field = 0;

    statement.setDate(field++, Date.valueOf(start));
    statement.setInt(field++, lotID);

    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = new WeeklyStatistics(result);
    }

    result.close();
    statement.close();

    return item;

  }

  /** Update.
   * @param conn the conn
   * @throws SQLException the SQL exception */
  public void update(Connection conn) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_UPDATE_WEEKLY_STATISTICS);

    int field = 0;

    statement.setDate(field++, Date.valueOf(start));
    statement.setInt(field++, lotID);
    statement.setFloat(field++, realizedOrdersMean);
    statement.setFloat(field++, canceledOrdersMean);
    statement.setFloat(field++, lateArrivalsMean);
    statement.setFloat(field++, realizedOrdersMedian);
    statement.setFloat(field++, canceledOrdersMedian);
    statement.setFloat(field++, lateArrivalsMedian);
    statement.setString(field++, realizedOrdersDist);
    statement.setString(field++, canceledOrdersDist);
    statement.setString(field++, lateArrivalsDist);

    statement.executeUpdate();

    statement.close();
  }

  public LocalDate getStart() {
    return start;
  }

  public void setStart(LocalDate start) {
    this.start = start;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public float getRealizedOrdersMean() {
    return realizedOrdersMean;
  }

  public void setRealizedOrdersMean(float realizedOrdersMean) {
    this.realizedOrdersMean = realizedOrdersMean;
  }

  public float getCanceledOrdersMean() {
    return canceledOrdersMean;
  }

  public void setCanceledOrdersMean(float canceledOrdersMean) {
    this.canceledOrdersMean = canceledOrdersMean;
  }

  public float getLateArrivalsMean() {
    return lateArrivalsMean;
  }

  public void setLateArrivalsMean(float lateArrivalsMean) {
    this.lateArrivalsMean = lateArrivalsMean;
  }

  public float getRealizedOrdersMedian() {
    return realizedOrdersMedian;
  }

  public void setRealizedOrdersMedian(float realizedOrdersMedian) {
    this.realizedOrdersMedian = realizedOrdersMedian;
  }

  public float getCanceledOrdersMedian() {
    return canceledOrdersMedian;
  }

  public void setCanceledOrdersMedian(float canceledOrdersMedian) {
    this.canceledOrdersMedian = canceledOrdersMedian;
  }

  public float getLateArrivalsMedian() {
    return lateArrivalsMedian;
  }

  public void setLateArrivalsMedian(float lateArrivalsMedian) {
    this.lateArrivalsMedian = lateArrivalsMedian;
  }

  public String getRealizedOrdersDist() {
    return realizedOrdersDist;
  }

  public void setRealizedOrdersDist(String realizedOrdersDist) {
    this.realizedOrdersDist = realizedOrdersDist;
  }

  public String getCanceledOrdersDist() {
    return canceledOrdersDist;
  }

  public void setCanceledOrdersDist(String canceledOrdersDist) {
    this.canceledOrdersDist = canceledOrdersDist;
  }

  public String getLateArrivalsDist() {
    return lateArrivalsDist;
  }

  public void setLateArrivalsDist(String lateArrivalsDist) {
    this.lateArrivalsDist = lateArrivalsDist;
  }
}
