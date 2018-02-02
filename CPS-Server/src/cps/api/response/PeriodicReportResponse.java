package cps.api.response;

import cps.entities.models.PeriodicReport;

/** Is sent in response to a GetPeriodicReport action. */
public class PeriodicReportResponse extends RequestReportResponse<PeriodicReport>  {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see cps.api.response.Response#handle(cps.api.response.ResponseHandler)
   */
  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }

}
