package cps.client.network;

public interface INetworkClient {
  public void sendRequest(Object rqst);
  public void receiveResponse(Object resp);
}
