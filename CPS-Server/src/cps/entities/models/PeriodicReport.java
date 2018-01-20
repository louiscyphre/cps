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

  public static Collection<PeriodicReport> getPeriodicReport(Connection conn, int year, int month) throws SQLException {

    /* The length of the report is bound to number of weeks in the month so we will count weeks in month */
    int weeks = Utilities.countWeeksInMonth(year, month);
    DailyStatistics[] days = null;
    LocalDate start = LocalDate.of(year, month, 1);
    int resize = weeks + 3;

    /* Additionally to weeks number we have to supply three more rows of data. So we increase the size of the report */
    PeriodicReport[] res = new PeriodicReport[resize];

    /* Adjust the start date to be the begining of the week */
    start = Utilities.findWeekStart(start);

    /* Zero Total, Average, and Median data */
    for (int i = weeks; i < weeks + 3; i++) {
      res[i].canceled = 0;
      res[i].completed = 0;
      res[i].hours = 0;
    }
    /* Set labels */
    res[weeks].field = "Total";
    res[weeks + 1].field = "Average";
    res[weeks + 2].field = "Median";

    /* Create median helper */
    double[][] medianHelper = new double[3][weeks];

    /* for every week of the month */
    for (int i = 0; i < weeks; i++) {

      /* get data for one week */
      days = DailyStatistics.getSevenDaysForPeriodic(conn, start);

      /* zero corresponding data */
      res[i].completed = 0;
      res[i].canceled = 0;

      /* for every day in week */
      for (int j = 0; j < 7; j++) {

        /* Populate the week rows and prepare data to calculate Total, Average, and Median */
        res[i].completed += days[j].getRealizedOrders();
        res[i].canceled += days[j].getCanceledOrders();
      }

      /* Count total hours of disabled cells */
      res[i].hours = DisabledCellsStatistics.countDisableHours(conn, start, start.plusDays(7));

      /* Set label to week number */
      res[i].field = String.format("Week %d", i);

      /* Add this week's data to tolal */
      res[weeks].completed += res[i].completed;
      res[weeks].canceled += res[i].canceled;
      res[weeks].hours += res[i].hours;

      /* insert data to median helper */
      medianHelper[0][i] = res[i].completed;
      medianHelper[1][i] = res[i].canceled;
      medianHelper[2][i] = res[i].hours;
    }
    /* Calculate averages */
    res[weeks + 1].completed = res[weeks].completed / weeks;
    res[weeks + 1].canceled = res[weeks].canceled / weeks;
    res[weeks + 1].hours = res[weeks].hours / weeks;

    /* Calculate median */

    Arrays.sort(medianHelper[0]);
    Arrays.sort(medianHelper[1]);
    Arrays.sort(medianHelper[2]);

    /* Take the middle as the Median */
    /* For calendarical reasons it is always ok to take the third week as the median */

    res[weeks + 2].completed = medianHelper[0][2];
    res[weeks + 2].canceled = medianHelper[1][2];
    res[weeks + 2].hours = medianHelper[2][2];
Collection<PeriodicReport> result = ;
    for (int i=0;i<weeks+3;i++) {
  
}
    
    return result;

  }
}
