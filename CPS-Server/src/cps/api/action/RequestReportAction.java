package cps.api.action;

public abstract class RequestReportAction extends ServiceAction {
  private static final long serialVersionUID = 1L;
  private int            reportType;

  public RequestReportAction(int userID, int reportType) {
    super(userID);
    this.reportType = reportType;
  }

  public int getReportType() {
    return reportType;
  }

  public void setReportType(int reportType) {
    this.reportType = reportType;
  }
}
