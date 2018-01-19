package cps.api.action;

import java.time.LocalDate;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class GetQuarterlyReportAction extends RequestReportAction {
  private static final long serialVersionUID = 1L;
  
  public GetQuarterlyReportAction(int userID, int reportType, LocalDate periodStart, LocalDate periodEnd) {
    super(userID, reportType, periodStart, periodEnd);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
  
}
