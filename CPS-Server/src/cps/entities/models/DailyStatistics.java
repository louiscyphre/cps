package cps.entities.models;

import java.io.Serializable;
import java.time.LocalDate;

public class DailyStatistics implements Serializable {
  // `day` date NOT NULL,
  // `realized_orders` int(11) DEFAULT NULL,
  // `canceled_orders` int(11) DEFAULT NULL,
  // `late_arrivals` int(11) DEFAULT NULL,
  // `complaints` int(11) DEFAULT NULL,
  // PRIMARY KEY (`day`)

  private static final long serialVersionUID = 1L;

  private LocalDate day;
  private int realizedOrders;
  private int canceledOrders;
  private int lateArrivals;
  private int complaints;

  /*public DailyStatistics(LocalDate day, int realizedOrders, int canceledOrders, int lateArrivals, int complaints) {
    super();
    this.day = day;
    this.realizedOrders = realizedOrders;
    this.canceledOrders = canceledOrders;
    this.lateArrivals = lateArrivals;
    this.complaints = complaints;
  }

  public DailyStatistics(ResultSet rs) throws SQLException {
    this(rs.getDate(1).toLocalDate(), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
  }*/

  public LocalDate getDay() {
    return day;
  }

  public void setDay(LocalDate day) {
    this.day = day;
  }

  public int getRealizedOrders() {
    return realizedOrders;
  }

  public void setRealizedOrders(int realizedOrders) {
    this.realizedOrders = realizedOrders;
  }

  public int getCanceledOrders() {
    return canceledOrders;
  }

  public void setCanceledOrders(int canceledOrders) {
    this.canceledOrders = canceledOrders;
  }

  public int getLateArrivals() {
    return lateArrivals;
  }

  public void setLateArrivals(int lateArrivals) {
    this.lateArrivals = lateArrivals;
  }

  public int getComplaints() {
    return complaints;
  }

  public void setComplaints(int complaints) {
    this.complaints = complaints;
  }
}