package cps.server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import cps.common.*;

public class ServerApplication extends AbstractServer {
	/**
	 * Constructs an instance of the server application.
	 *
	 * @param port
	 *          The port number to connect on.
	 */
	public ServerApplication(int port) {
		super(port);
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *          The message received from the client.
	 * @param client
	 *          The connection from which the message originated.
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg != null) {
			System.out.println("Message received: " + msg + ", from: " + client + ", type: " + msg.getClass().toString());
			this.sendToAllClients(msg);
		} else {
			System.out.println("Message received: " + msg + ", from: " + client);
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client connected: " + client);
	}

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client disconnected: " + client);
	}

	public static void main(String[] args) {
		int port = Constants.DEFAULT_PORT;

		try {
			port = Integer.parseInt(args[0]);
		} catch (Throwable t) {
			port = Constants.DEFAULT_PORT;
		}

		ServerApplication server = new ServerApplication(port);

		try {
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
