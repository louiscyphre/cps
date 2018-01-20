package cps.entities.models;

import java.sql.Connection;

public class PeriodicReport extends GenericReport<StatisticalData> {
  private static final long serialVersionUID = 1L;

  public PeriodicReport() {
    super("realizedOrders", "canceledOrders", "disabledParkingHours");
  }

  public static void getPeriodicReport(Connection conn) {
    
  }
}
