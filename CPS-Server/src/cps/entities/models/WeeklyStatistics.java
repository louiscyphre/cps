package cps.entities.models;

import static cps.common.Utilities.debugPrintln;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import cps.common.Constants;
import cps.common.Utilities;

/** Database entity for storing weekly statistics.
 * Gathered from analyzing daily statistics. */
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

  /** Instantiates a new weekly statistics object.
   * @param start the starting date
   * @param lotID the lot ID
   * @param realizedOrdersMean the realized orders mean
   * @param canceledOrdersMean the canceled orders mean
   * @param lateArrivalsMean the late arrivals mean
   * @param realizedOrdersMedian the realized orders median
   * @param canceledOrdersMedian the canceled orders median
   * @param lateArrivalsMedian the late arrivals median
   * @param realizedOrdersDist the realized orders distribution
   * @param canceledOrdersDist the canceled orders distribution
   * @param lateArrivalsDist the late arrivals distribution */
  public WeeklyStatistics(LocalDate start, int lotID, float realizedOrdersMean, float canceledOrdersMean, float lateArrivalsMean, float realizedOrdersMedian,
      float canceledOrdersMedian, float lateArrivalsMedian, String realizedOrdersDist, String canceledOrdersDist, String lateArrivalsDist) {
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

  /** Instantiates a new weekly statistics object from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException on error */
  public WeeklyStatistics(ResultSet rs) throws SQLException {
    this(rs.getDate(1).toLocalDate(), rs.getInt(2), rs.getFloat(3), rs.getFloat(4), rs.getFloat(5), rs.getFloat(6), rs.getFloat(7), rs.getFloat(8),
        rs.getString(9), rs.getString(10), rs.getString(11));
  }

  /** Create a new weekly statistics record in the database.
   * @param conn the SQL connection
   * @param start the starting date
   * @param lotID the lot ID
   * @param realizedOrdersMean the realized orders mean
   * @param canceledOrdersMean the canceled orders mean
   * @param lateArrivalsMean the late arrivals mean
   * @param realizedOrdersMedian the realized orders median
   * @param canceledOrdersMedian the canceled orders median
   * @param lateArrivalsMedian the late arrivals median
   * @param realizedOrdersDist the realized orders distribution
   * @param canceledOrdersDist the canceled orders distribution
   * @param lateArrivalsDist the late arrivals distribution
   * @return the new weekly statistics object
   * @throws SQLException on error */
  public static WeeklyStatistics create(Connection conn, LocalDate start, int lotID, float realizedOrdersMean, float canceledOrdersMean, float lateArrivalsMean,
      float realizedOrdersMedian, float canceledOrdersMedian, float lateArrivalsMedian, String realizedOrdersDist, String canceledOrdersDist,
      String lateArrivalsDist) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_WEEKLY_STATISTICS);

    int field = 1;

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

    return new WeeklyStatistics(start, lotID, realizedOrdersMean, canceledOrdersMean, lateArrivalsMean, realizedOrdersMedian, canceledOrdersMedian,
        lateArrivalsMedian, realizedOrdersDist, canceledOrdersDist, lateArrivalsDist);
  }

  /** Create or update weekly statistics for the specified date and parking lot.
   * @param conn the SQL connection
   * @param start the starting date
   * @param lotid the lot ID
   * @return the weekly statistics object
   * @throws SQLException on error */
  public static WeeklyStatistics createUpdateWeeklyReport(Connection conn, LocalDate start, int lotid) throws SQLException {
    // Find this week's Sunday
    start = Utilities.findWeekStart(start);
    WeeklyStatistics result = findOrCreate(conn, start, lotid);

    DailyStatistics[] days = DailyStatistics.getSevenDays(conn, start, lotid);

    /* Calculate median and mean */
    int[][] medianHelper = new int[2][7];

    float numRealizedOrders = 0f;
    float numCanceledOrders = 0f;
    float numLateArrivals = 0f;

    for (int i = 0; i < 7; i++) {
      medianHelper[0][i] = days[i].getRealizedOrders();
      medianHelper[1][i] = days[i].getCanceledOrders();
      medianHelper[2][i] = days[i].getLateArrivals();
      numRealizedOrders += (float) days[i].getRealizedOrders();
      numCanceledOrders += (float) days[i].getCanceledOrders();
      numLateArrivals += (float) days[i].getLateArrivals();
    }
    for (int i = 0; i < 2; i++) {
      Arrays.sort(medianHelper[i]);
    }
    result.realizedOrdersMean = numRealizedOrders / 7;
    result.canceledOrdersMean = numCanceledOrders / 7;
    result.lateArrivalsMean = numLateArrivals / 7;
    result.realizedOrdersMedian = medianHelper[0][3];
    result.canceledOrdersMedian = medianHelper[1][3];
    result.lateArrivalsMedian = medianHelper[2][3];

    /* Calculate distribution */

    result.canceledOrdersDist = "";
    result.lateArrivalsDist = "";
    result.realizedOrdersDist = "";
    for (int i = 0; i < 6; i++) {
      result.realizedOrdersDist += String.format("%f,", days[i].getRealizedOrders() / numRealizedOrders);
      result.canceledOrdersDist += String.format("%f,", days[i].getCanceledOrders() / numCanceledOrders);
      result.lateArrivalsDist += String.format("%f,", days[i].getLateArrivals() / numLateArrivals);
    }
    result.realizedOrdersDist += String.format("%f", days[6].getRealizedOrders() / numRealizedOrders);
    result.canceledOrdersDist += String.format("%f", days[6].getCanceledOrders() / numCanceledOrders);
    result.lateArrivalsDist += String.format("%f", days[6].getLateArrivals() / numLateArrivals);

    /* Distributions now recorded as comma separated values from Sunday to Saturday */
    result.update(conn);
    debugPrintln("WeeklyStatistics: generated for %s", start);
    return result;
  }

  /** Collect the weekly statistics that are needed for producing a periodic activity report.
   * @param conn the SQL connection
   * @param start the starting date
   * @return the weekly statistics object
   * @throws SQLException the SQL exception */
  public static WeeklyStatistics getWeeklyReportForPeriodic(Connection conn, LocalDate start) throws SQLException {
    // Find this week's Sunday
    start = Utilities.findWeekStart(start);
    WeeklyStatistics result = new WeeklyStatistics(start, 0, 0, 0, 0, 0, 0, 0, "", "", "");

    DailyStatistics[] days = DailyStatistics.getSevenDaysForPeriodic(conn, start);

    /* Calculate median and mean */
    int[][] medianHelper = new int[2][7];

    float numRealizedOrders = 0f;
    float numCanceledOrders = 0f;
    float numLateArrivals = 0f;

    for (int i = 0; i < 7; i++) {
      medianHelper[0][i] = days[i].getRealizedOrders();
      medianHelper[1][i] = days[i].getCanceledOrders();
      medianHelper[2][i] = days[i].getLateArrivals();
      numRealizedOrders += (float) days[i].getRealizedOrders();
      numCanceledOrders += (float) days[i].getCanceledOrders();
      numLateArrivals += (float) days[i].getLateArrivals();
    }
    for (int i = 0; i < 2; i++) {
      Arrays.sort(medianHelper[i]);
    }
    result.realizedOrdersMean = numRealizedOrders / 7;
    result.canceledOrdersMean = numCanceledOrders / 7;
    result.lateArrivalsMean = numLateArrivals / 7;
    result.realizedOrdersMedian = medianHelper[0][3];
    result.canceledOrdersMedian = medianHelper[1][3];
    result.lateArrivalsMedian = medianHelper[2][3];

    return result;
  }

  /** Find a weekly statistics record for the specified starting time and parking lot id, or create new if doesn't exist.
   * @param conn the SQL connection
   * @param start the starting date
   * @param lotID the lot ID
   * @return the weekly statistics object
   * @throws SQLException on error */
  public static WeeklyStatistics findOrCreate(Connection conn, LocalDate start, int lotID) throws SQLException {
    WeeklyStatistics item = null;
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_WEEKLY_STATISTICS);

    int field = 1;

    statement.setDate(field++, Date.valueOf(start));
    statement.setInt(field++, lotID);

    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = new WeeklyStatistics(result);
    } else {
      item = create(conn, start, lotID, 0, 0, 0, 0, 0, 0, "", "", "");
    }

    result.close();
    statement.close();

    return item;

  }

  /** Synchronize this weekly statistics object with its associated database record.
   * @param conn the SQL connection
   * @throws SQLException on error */
  public void update(Connection conn) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_UPDATE_WEEKLY_STATISTICS);

    int field = 1;

    statement.setFloat(field++, realizedOrdersMean);
    statement.setFloat(field++, canceledOrdersMean);
    statement.setFloat(field++, lateArrivalsMean);
    statement.setFloat(field++, realizedOrdersMedian);
    statement.setFloat(field++, canceledOrdersMedian);
    statement.setFloat(field++, lateArrivalsMedian);
    statement.setString(field++, realizedOrdersDist);
    statement.setString(field++, canceledOrdersDist);
    statement.setString(field++, lateArrivalsDist);
    statement.setDate(field++, Date.valueOf(start));
    statement.setInt(field++, lotID);

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
