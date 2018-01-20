package cps.api.response;

import java.time.Duration;
import java.time.LocalDate;

public abstract class RequestReportResponse<T> extends ServerResponseWithData<T> {
  private static final long serialVersionUID = 1L;
  private int               lotID            = 0;
  private LocalDate         periodStart      = null;
  private LocalDate         periodEnd        = null;
  private Duration          interval         = null;

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public LocalDate getPeriodStart() {
    return periodStart;
  }

  public void setPeriodStart(LocalDate periodStart) {
    this.periodStart = periodStart;
  }

  public LocalDate getPeriodEnd() {
    return periodEnd;
  }

  public void setPeriodEnd(LocalDate periodEnd) {
    this.periodEnd = periodEnd;
  }

  public Duration getInterval() {
    return interval;
  }

  public void setInterval(Duration interval) {
    this.interval = interval;
  }
  
  public void setPeriod(LocalDate periodStart, LocalDate periodEnd, Duration interval) {
    this.periodStart = periodStart;
    this.periodEnd = periodEnd;
    this.interval = interval;
  }
}
