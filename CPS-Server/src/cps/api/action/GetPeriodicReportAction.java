package cps.api.action;

import java.time.LocalDate;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;
import cps.common.Constants;

/** Is sent by the client application when the Global Manager wants to see periodic activity statistics. */
public class GetPeriodicReportAction extends RequestReportAction {
  private static final long serialVersionUID = 1L;

  public GetPeriodicReportAction(int userID, LocalDate periodStart, LocalDate periodEnd) {
    super(userID, Constants.REPORT_TYPE_PERIODIC, periodStart, periodEnd);
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

}
