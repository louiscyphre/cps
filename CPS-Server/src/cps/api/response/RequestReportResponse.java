package cps.api.response;

public class RequestReportResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;
	
  @Override
  public ServerResponse handle(ResponseHandler handler) {
    return handler.handle(this);
  }
}
