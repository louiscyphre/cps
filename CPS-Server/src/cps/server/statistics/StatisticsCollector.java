package cps.server.statistics;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cps.common.Constants;
import cps.entities.models.DailyStatistics;
import cps.entities.models.MonthlyReport;
import cps.server.ServerException;

public class StatisticsCollector {
  /** Increase complaints.
   * @param conn the conn
   * @param year the year
   * @param month the month
   * @param lotid the lotid
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseComplaints(Connection conn, int year, int month, int lotID) throws SQLException, ServerException {
    MonthlyReport.increaseComplaints(conn, year, month, lotID);
  }

  /** Increase canceled order count by one for today in specific parking lot.
   * @param conn the conn
   * @param lotId the lot id
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public static void increaseCanceledOrder(Connection conn, LocalDate date, int lotID) throws SQLException, ServerException {
    DailyStatistics.increaseCanceledOrder(conn, date, lotID);
  }

  public static void increaseSubscription(Connection conn, int subsType, int lotID) throws SQLException, ServerException {
    if (subsType == Constants.SUBSCRIPTION_TYPE_REGULAR) {
      MonthlyReport.increaseRegular(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), lotID);
    } else {
      // FIXME Tegra what to do with full subscription lot id?
      // Feedback - 0 is probably fine
      MonthlyReport.increaseFull(conn, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 0);
    }
  }

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
}
