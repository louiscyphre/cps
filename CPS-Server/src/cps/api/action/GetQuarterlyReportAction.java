package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;
import cps.common.Constants;

public class GetQuarterlyReportAction extends RequestReportAction {
  private static final long serialVersionUID = 1L;

  public GetQuarterlyReportAction(int userID) {
    super(userID, Constants.REPORT_TYPE_QUARTERLY);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
  
}
