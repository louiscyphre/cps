package cps.api.response;

import cps.entities.models.WeeklyStatistics;

public class WeeklyReportResponse extends ServerResponseWithData<WeeklyStatistics> {
  private static final long serialVersionUID = 1L;

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }

}
