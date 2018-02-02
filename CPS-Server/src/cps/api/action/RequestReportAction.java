package cps.api.action;

import java.time.LocalDate;

/** Base Class for actions that request statistical reports. */
public abstract class RequestReportAction extends ServiceAction {
  private static final long serialVersionUID = 1L;
  private int               reportType;
  private LocalDate         periodStart;
  private LocalDate         periodEnd;

  public RequestReportAction(int userID, int reportType, LocalDate periodStart, LocalDate periodEnd) {
    super(userID);
    this.reportType = reportType;
    this.periodStart = periodStart;
    this.periodEnd = periodEnd;
  }
  
  public int getReportType() {
    return reportType;
  }

  public void setReportType(int reportType) {
    this.reportType = reportType;
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
}
