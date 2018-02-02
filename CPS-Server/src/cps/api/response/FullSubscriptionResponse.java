package cps.api.response;

public class FullSubscriptionResponse extends SubscriptionResponse {
  private static final long serialVersionUID = 1L;

  @Override
  public void handle(ResponseHandler handler) {
    handler.handle(this);
  }
}
