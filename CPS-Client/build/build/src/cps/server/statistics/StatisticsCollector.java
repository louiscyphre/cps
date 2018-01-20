package cps.server.statistics;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cps.common.Constants;
import cps.entities.models.DailyStatistics;
import cps.entities.models.DisabledCellsStatistics;
import cps.entities.models.MonthlyReport;
import cps.server.ServerException;

/** Consolidates the collection of statistics throughout the system.
 * At certain points in the source code, there are statistical entry points, which are calls to the static methods of this class. */
public class StatisticsCollector {
  /** Increase canceled order count by one for today in specific parking lot.
   * @param conn the SQL connection
   * @param lotID the lot ID
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseCanceledOrder(Connection conn, LocalDate date, int lotID) throws SQLException, ServerException {
    DailyStatistics.increaseCanceledOrder(conn, date, lotID);
  }
  
  /** Increase the number of complaints filed this month.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @param lotID the lot ID
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseComplaints(Connection conn, int year, int month, int lotID) throws SQLException, ServerException {
    MonthlyReport.increaseComplaints(conn, year, month, lotID);
  }

  /** Increase the number of subscriptions purchased this month.
   * @param conn the SQL connection
   * @param subsType the subscription type (regular or full)
   * @param lotID the lot ID
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseSubscription(Connection conn, int subsType, int lotID) throws SQLException, ServerException {
    if (subsType == Constants.SUBSCRIPTION_TYPE_REGULAR) {
      MonthlyReport.increaseRegular(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), lotID);
    } else {
      MonthlyReport.increaseFull(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 0);
    }
  }
  
  /** Increase the number of one-time parking services ordered this month or today.
   * Used as an entry point for both daily and monthly statistics.
   * @param conn the SQL connection
   * @param serviceID the service ID
   * @param licenseType the license type
   * @param parkingType the parking type
   * @param lotID the lot ID
   * @param warned used to check late arrivals
   * @throws SQLException on error
   * @throws ServerException on error */
  public static void increaseOnetime(Connection conn, int serviceID, int licenseType, int parkingType, int lotID, boolean warned)
      throws SQLException, ServerException {
    if (licenseType == Constants.LICENSE_TYPE_ONETIME) {
      // Update daily statistics - realized orders
      DailyStatistics.increaseRealizedOrder(conn, lotID);
      if (parkingType == Constants.PARKING_TYPE_INCIDENTAL) {
        // Monthly statistics
        MonthlyReport.increaseIncidental(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), lotID);
      } else {
        // Daily statistics - late arrival
        if (warned) {
          DailyStatistics.increaseLateArrival(conn, serviceID);
        }
        // Monthly statistics
        MonthlyReport.increaseReserved(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), lotID);
      }
    }
  }

  /** Collect statistics about parking cell (the location inside of a parking lot) disable/enable actions.
   * Record location, date disabled, date enabled.
   * @param conn the SQL connection
   * @param disabled was the cell disabled or enabled
   * @param lotID the lot ID
   * @param i the i coordinate
   * @param j the j coordinate
   * @param k the k coordinate
   * @throws SQLException on error */
  public static void registerCellDisableAction(Connection conn, boolean disabled, int lotID, int i, int j, int k) throws SQLException {
    if (disabled) {
      DisabledCellsStatistics.create(conn, lotID, i, j, k);
    } else {
      DisabledCellsStatistics.markfixed(conn, lotID, i, j, k);
    }
  }
}
