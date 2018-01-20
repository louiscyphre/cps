package cps.entities.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import cps.common.Utilities;;

public class PeriodicReport extends GenericReport<StatisticalData> {
  private static final long serialVersionUID = 1L;

  private double completed;
  private double canceled;
  private double hours;
  private String filed;

  public PeriodicReport() {
    super("field", "realizedOrders", "canceledOrders", "disabledParkingHours");
  }

  public static PeriodicReport[] getPeriodicReport(Connection conn, int year, int month) throws SQLException {

    /* The length of the report is bound to number of weeks in the month so we will count weeks in month */
    int weeks = Utilities.countWeeksInMonth(year, month);
    /* Additionally to weeks number we have to supply three more rows of data. So we increase the size of the report */
    PeriodicReport[] result = new PeriodicReport[weeks + 3];

    return null;

  }
}
