package cps.entities.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;

import cps.common.Utilities;;

/** Aggregates data that is used for producing and displaying periodic activity reports to the Global Manager. */
public class PeriodicReport extends GenericReport<StatisticalData> {
  private static final long serialVersionUID = 1L;
  
  /** The row names. */
  String[] rowNames;

  /** Instantiates a new periodic report.
   * @param rowNames the row names */
  public PeriodicReport(String[] rowNames) {
    super(rowNames.length, "realizedOrders", "canceledOrders", "disabledParkingHours");
    this.rowNames = rowNames;
  }
  
  /** Set the values for a data column.
   * @param name the name
   * @param values the values */
  public void setColumn(String name, double[] values) {
    setData(name, new StatisticalData(values));
  }

  public String[] getRowNames() {
    return rowNames;
  }

  public void setRowNames(String[] rowNames) {
    this.rowNames = rowNames;
  }
  
  /** Retrieve a row of data.
   * @param i the i
   * @return the row */
  public Collection<Double> getRow(int i) {
    LinkedList<Double> row = new LinkedList<>();
    
    for (String key : keySet()) {
      StatisticalData data = getData(key);
      if (data == null || data.getValues() == null || data.getValues().length < i + 1) {
        row.add(0.0);
      } else {
        row.add(data.getValues()[i]);
      }
    }
    
    // Return a data row with an entry for each column
    return row;
  }
  
  /** Generate report for the given month by querying the database, collecting the necessary data and calculating the statistics.
   * @param conn the SQL connection
   * @param year the year
   * @param month the month
   * @return the periodic report
   * @throws SQLException on error */
  public static PeriodicReport generate(Connection conn, int year, int month) throws SQLException {    
    /* The length of the report is bound to number of weeks in the month so we will count weeks in month */
    int weeks = Utilities.countWeeksInMonth(year, month);
    
    DailyStatistics[] days = null;
    LocalDate start = LocalDate.of(year, month, 1);
    double[] completed = new double[weeks];
    double[] canceled = new double[weeks];
    double[] hours = new double[weeks];
    String[] rows = new String[weeks];

    /* Adjust the start date to be the beginning of the week */
    start = Utilities.findWeekStart(start);

    /* for every week of the month */
    for (int i = 0; i < weeks; i++) {

      /* get data for one week */
      days = DailyStatistics.getSevenDaysForPeriodic(conn, start);

      /* for every day in week */
      for (int j = 0; j < 7; j++) {
        /* Populate the week rows */
        completed[i] += days[j].getRealizedOrders();
        canceled[i] += days[j].getCanceledOrders();
      }

      /* Count total hours of disabled cells */
      hours[i] = DisabledCellsStatistics.countDisableHours(conn, start, start.plusDays(7));

      /* Set label to week number */
      rows[i] = String.format("Week %d", i + 1);
    }

    PeriodicReport report = new PeriodicReport(rows);
    report.setColumn("realizedOrders", completed);
    report.setColumn("canceledOrders", canceled);
    report.setColumn("disabledParkingHours", hours);

    return report;
    
  }
}
