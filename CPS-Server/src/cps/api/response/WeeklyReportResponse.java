package cps.api.response;

import cps.entities.models.WeeklyStatistics;

/** Is sent in response to a GetWeeklyReport action. */
public class WeeklyReportResponse extends RequestReportResponse<WeeklyStatistics> {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see cps.api.response.ServerResponse#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
