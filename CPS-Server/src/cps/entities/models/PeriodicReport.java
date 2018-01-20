package cps.entities.models;

import java.sql.Connection;

public class PeriodicReport extends GenericReport<StatisticalData> {
  private static final long serialVersionUID = 1L;

  private double completed;
  private double canceled;
  private double hours;
  private String filed;
  
  public PeriodicReport() {
    super("field","realizedOrders", "canceledOrders", "disabledParkingHours");
  }

  public static void getPeriodicReport(Connection conn) {
    
  }
}
