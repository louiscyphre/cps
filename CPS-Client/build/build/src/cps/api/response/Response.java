package cps.api.response;

public abstract class Response {
  public abstract ServerResponse handle(ResponseHandler handler);
}
