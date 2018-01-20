package cps.entities.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    /* Zero Total, Average, and Median data */
    for (int i = weeks; i < weeks + 3; i++) {
      result[i].canceled = 0;
      result[i].completed = 0;
      result[i].hours = 0;
    }
    /* Set labels */
    result[weeks].field = "Total";
    result[weeks + 1].field = "Average";
    result[weeks + 2].field = "Median";

    /* Create median helper */
    double[][] mh = new double[3][weeks];

    /* for every week of the month */
    for (int i = 0; i < weeks; i++) {

      /* get data for one week */
      days = DailyStatistics.getSevenDaysForPeriodic(conn, start);

      /* zero corresponding data */
      result[i].completed = 0;
      result[i].canceled = 0;

      /* for every day in week */
      for (int j = 0; j < 7; j++) {

        /* Populate the week rows and prepare data to calculate Total, Average, and Median */
        result[i].completed += days[j].getRealizedOrders();
        result[i].canceled += days[j].getCanceledOrders();
      }

      /* Count total hours of disabled cells */
      result[i].hours = DisabledCellsStatistics.countDisableHours(conn, start, start.plusDays(7));

      /* Set label to week number */
      result[i].field = String.format("Week %d", i);

      /* Add this week's data to tolal */
      result[weeks].completed += result[i].completed;
      result[weeks].canceled += result[i].canceled;
      result[weeks].hours += result[i].hours;

      /* insert data to median helper */
      mh[0][i] = result[i].completed;
      mh[1][i] = result[i].canceled;
      mh[2][i] = result[i].hours;
    }
    /* Calculate averages */
    result[weeks + 1].completed = result[weeks].completed / weeks;
    result[weeks + 1].canceled = result[weeks].canceled / weeks;
    result[weeks + 1].hours = result[weeks].hours / weeks;

    /* Calculate median */

    Arrays.sort(mh[0]);
    Arrays.sort(mh[1]);
    Arrays.sort(mh[2]);

    /* Take the middle as the Median */
    /* For calendarical reasons it is always ok to take the third week as the median */

    result[weeks + 2].completed = mh[0][2];
    result[weeks + 2].canceled = mh[1][2];
    result[weeks + 2].hours = mh[2][2];

    return result;

  }
}
