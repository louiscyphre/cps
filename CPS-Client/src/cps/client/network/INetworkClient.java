package cps.client.network;

/**
 * Network client interface, communication done with Java Objects.
 */
public interface INetworkClient {
  /**
   * @param rqst object containing request
   */
  public void sendRequest(Object rqst);

  /**
   * @param resp object containing response
   */
  public void receiveResponse(Object resp);
}
