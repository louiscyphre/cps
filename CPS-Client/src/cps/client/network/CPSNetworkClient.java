package cps.client.network;

import java.io.IOException;

import ocsf.client.AbstractClient;

/**
 * Network client used by CPS application
 */
public class CPSNetworkClient extends AbstractClient {
  /**
   * 
   */
  INetworkClient clientUI;

  /**
   * @param host
   * @param port
   * @param clientUI
   * @throws IOException
   */
  public CPSNetworkClient(String host, int port, INetworkClient clientUI) throws IOException {
    super(host, port);
    this.clientUI = clientUI;
    openConnection();
  }

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg
   *          The message from the server.
   */
  /*
   * (non-Javadoc)
   * @see ocsf.client.AbstractClient#handleMessageFromServer(java.lang.Object)
   */
  @Override
  protected void handleMessageFromServer(Object msg) {
    clientUI.receiveResponse(msg);
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message
   *          The message from the UI.
   */
  public void handleMessageFromClientUI(Object message) throws IOException {
    // message is an instance of Request or Action
    sendToServer(message);
  }

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      System.exit(0);
    }
  }

  @Override
  protected void connectionException(Exception exception) {
    System.err.format("Connection exception: %s\n", exception.getMessage());
    // TODO show message "lost connection to server"
  }

}
