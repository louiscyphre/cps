package cps.api.response;

import java.util.Collection;

import cps.entities.models.MonthlyReport;

public class QuarterlyReportResponse extends ServerResponseWithData<Collection<MonthlyReport>> {
  private static final long serialVersionUID = 1L;

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }

}

