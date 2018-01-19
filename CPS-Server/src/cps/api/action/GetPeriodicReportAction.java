package cps.api.action;

import java.time.LocalDate;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class GetPeriodicReportAction extends ServiceAction {
  private static final long serialVersionUID = 1L;
  
  private LocalDate periodStart;
  private LocalDate periodEnd;
  
  public GetPeriodicReportAction(int userID) {
    super(userID);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
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
