/*
 * 
 */
package cps.server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import com.google.gson.Gson;

import cps.common.*;
import cps.api.response.*;
import cps.server.controllers.*;

/**
 * The Class ServerApplication.
 */
public class ServerApplication extends AbstractServer {
	private Gson gson = new Gson();
	private ServerController serverController;

	/**
	 * Constructs an instance of the server application.
	 *
	 * @param port The port number to connect on.
	 * @param config the config
	 * @throws Exception the exception
	 */
	public ServerApplication(int port, ServerConfig config) throws Exception {
		super(port);
		this.serverController = new ServerController(config);

	}

	public ServerController getServerController() {
		return serverController;
	}
	
	public DatabaseController getDatabaseController() {
		return serverController.getDatabaseController();
	}

	/**
	 * Send object to client.
	 *
	 * @param client The client object
	 * @param msg The Object to be serialized and sent
	 */
	private void sendToClient(ConnectionToClient client, Object msg) {
		try {
			System.out.println("Sending to client: " + gson.toJson(msg));
			client.sendToClient(msg);
		} catch (Exception ex) {
			System.out.println("ERROR - Could not send to client!!!");
			ex.printStackTrace();
		}
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param message
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	@Override
	protected void handleMessageFromClient(Object message, ConnectionToClient client) {
		if (message == null) {
			return;
		}
		
		System.out.println("Message from: " + client + ", type: " + message.getClass().getSimpleName() + ", content: "
				+ gson.toJson(message));
		
		ServerResponse response = serverController.handle(message);
		
		if (response != null) {
			sendToClient(client, response);
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	@Override
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@Override
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

		boolean remote = true;

		for (String elem : args) {
			if (elem.equals("--local")) {
				remote = false;
			}
		}

		try {
			ServerConfig config = remote ? ServerConfig.remote() : ServerConfig.local();
			ServerApplication server = new ServerApplication(port, config);
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			ex.printStackTrace();
		}
	}
}
