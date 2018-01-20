package cps.api.action;

import java.time.LocalDate;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;
import cps.common.Constants;

public class GetPeriodicReportAction extends RequestReportAction {
  private static final long serialVersionUID = 1L;

  public GetPeriodicReportAction(int userID, LocalDate periodStart, LocalDate periodEnd) {
    super(userID, Constants.REPORT_TYPE_PERIODIC, periodStart, periodEnd);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
