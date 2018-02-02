package cps.api.response;

import cps.entities.models.WeeklyStatistics;

public class WeeklyReportResponse extends RequestReportResponse<WeeklyStatistics> {
  private static final long serialVersionUID = 1L;

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
