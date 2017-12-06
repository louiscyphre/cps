package cps.client;

import java.io.IOException;

import ocsf.client.AbstractClient;

public class ClientController extends AbstractClient {
	ClientUI clientUI;

	public ClientController(String host, int port, ClientUI clientUI) throws IOException {
		super(host, port);
		this.clientUI = clientUI;
		openConnection();
	}

	public void display(String message) {
		System.out.println("> " + message);
	}

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg
	 *          The message from the server.
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message
	 *          The message from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			clientUI.displayError("Could not send message to server. Terminating client.");
			e.printStackTrace();
			quit(); // TODO: replace this with something smarter
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
