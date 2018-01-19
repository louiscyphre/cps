package cps.api.response;

import java.util.Collection;

import cps.entities.models.PeriodicReport;

public class PeriodicReportResponse extends ServerResponseWithData<Collection<PeriodicReport>>  {
  private static final long serialVersionUID = 1L;

  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }

}
