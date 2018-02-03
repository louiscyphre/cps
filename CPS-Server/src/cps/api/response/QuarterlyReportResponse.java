package cps.api.response;

import java.util.Collection;

import cps.entities.models.MonthlyReport;

/** Is sent in response to a GetQuarterlyReport action. */
public class QuarterlyReportResponse extends RequestReportResponse<Collection<MonthlyReport>> {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}

