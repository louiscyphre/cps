package cps.api.response;

import cps.entities.models.PeriodicReport;

public class PeriodicReportResponse extends RequestReportResponse<PeriodicReport>  {
  private static final long serialVersionUID = 1L;

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
