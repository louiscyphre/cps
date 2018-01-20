package cps.client.network;

/**
 * @author firl
 *
 */
public interface INetworkClient {
  /**
   * @param rqst
   */
  public void sendRequest(Object rqst);

  /**
   * @param resp
   */
  public void receiveResponse(Object resp);
}
