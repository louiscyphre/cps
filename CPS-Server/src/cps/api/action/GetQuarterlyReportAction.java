package cps.api.action;

import java.time.LocalDate;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class GetQuarterlyReportAction extends RequestReportAction {
  private static final long serialVersionUID = 1L;

  int lotID;

  public GetQuarterlyReportAction(int userID, int reportType, LocalDate periodStart, LocalDate periodEnd, int lotID) {
    super(userID, reportType, periodStart, periodEnd);
    this.lotID = lotID;
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

}
