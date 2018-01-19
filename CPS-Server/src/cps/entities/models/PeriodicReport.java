package cps.entities.models;

public class PeriodicReport extends GenericReport<StatisticalData> {
  private static final long serialVersionUID = 1L;

  public PeriodicReport() {
    super("realizedOrders", "canceledOrders", "disabledParkingHours");
  }

}
