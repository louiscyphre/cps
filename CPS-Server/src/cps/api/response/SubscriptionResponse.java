package cps.api.response;

/** Base classes for responses to requests for purchasing a subscription. */
public abstract class SubscriptionResponse extends ParkingServiceResponse {
  private static final long serialVersionUID = 1L;
  private int[]             subscriptionIDs;

  public int[] getSubscriptionIDs() {
    return subscriptionIDs;
  }

  public void setSubscriptionIDs(int[] subscriptionIDs) {
    this.subscriptionIDs = subscriptionIDs;
  }
}
