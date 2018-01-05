package cps.client.alpha;

import java.io.IOException;

import ocsf.client.AbstractClient;

public class ClientControllerAlpha extends AbstractClient {
	ClientUIAlpha clientUI;

	public ClientControllerAlpha(String host, int port, ClientUIAlpha clientUI) throws IOException {
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
  public void handleMessageFromClientUI(Object message) {
    try {
      // message is an instance of Request or Action
      sendToServer(message);
    } catch (IOException e) {
      // FIXME some business logic comes here
    }
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

}
