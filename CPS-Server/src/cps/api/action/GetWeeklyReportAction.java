package cps.api.action;

import java.time.LocalDate;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when the Global Manager wants to receive a weekly statistical report. */
public class GetWeeklyReportAction extends RequestReportAction {
  private static final long serialVersionUID = 1L;
  private int               lotID;

  public GetWeeklyReportAction(int userID, int reportType, LocalDate periodStart, LocalDate periodEnd, int lotID) {
    super(userID, reportType, periodStart, periodEnd);
    this.lotID = lotID;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object)
   */
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
