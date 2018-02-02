package cps.api.response;

import java.util.Collection;

import cps.entities.models.MonthlyReport;

public class QuarterlyReportResponse extends RequestReportResponse<Collection<MonthlyReport>> {
  private static final long serialVersionUID = 1L;

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}

