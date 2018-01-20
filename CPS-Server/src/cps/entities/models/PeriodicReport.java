package cps.entities.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import cps.common.Utilities;;

public class PeriodicReport extends GenericReport<StatisticalData> {
  private static final long serialVersionUID = 1L;

  private double completed;
  private double canceled;
  private double hours;
  private String field;

  public PeriodicReport() {
    super("field", "realizedOrders", "canceledOrders", "disabledParkingHours");
  }

  public PeriodicReport(String fi, double rea, double can, double dis) {
    super("field", "realizedOrders", "canceledOrders", "disabledParkingHours");
    this.field = fi;
    this.completed = rea;
    this.canceled = can;
    this.hours = dis;
  }

  public static PeriodicReport[] getPeriodicReport(Connection conn, int year, int month) throws SQLException {

    /* The length of the report is bound to number of weeks in the month so we will count weeks in month */
    int weeks = Utilities.countWeeksInMonth(year, month);
    DailyStatistics[] days = null;
    LocalDate start = LocalDate.of(year, month, 1);
    int resize = weeks + 3;

    /* Additionally to weeks number we have to supply three more rows of data. So we increase the size of the report */
    PeriodicReport[] result = new PeriodicReport[resize];

    /* Adjust the start date to be the begining of the week */
    start = Utilities.findWeekStart(start);

    int[][] mh = new int[2][weeks];

    /* Populate the week rows and prepare data to calculate Total, Average, and Median */

    for (int i = 0; i < weeks; i++) {
      days = DailyStatistics.getSevenDaysForPeriodic(conn, start);
      result[i].completed = 0;
      result[i].canceled = 0;
      for (int j = 0; j < 7; j++) {
        result[i].completed += days[j].getRealizedOrders();
        result[i].canceled += days[j].getCanceledOrders();
      }
    }

    return null;

  }
}
