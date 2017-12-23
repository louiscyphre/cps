package cps.server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.sql.Timestamp;
import java.util.Collection;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;

import cps.common.*;
import cps.entities.models.*;
import cps.api.request.*;
import cps.api.response.*;

public class ServerApplication extends AbstractServer {
	DatabaseController databaseController;
	Config config;
	
	/**
	 * Constructs an instance of the server application.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public ServerApplication(int port, boolean remote) throws Exception {
		super(port);
		config = remote ? Config.getRemote() : Config.getLocal();
		databaseController = new DatabaseController(config.get("db.host"), config.get("db.name"), config.get("db.username"),
				config.get("db.password"));
	}

	private void sendToClient(ConnectionToClient client, Object msg) {
		try {
			Gson gson = new Gson();
			System.out.print("Sending to client: " + gson.toJson(msg));
			client.sendToClient(msg);
		} catch (Exception ex) {
			System.out.println("ERROR - Could not send to client!");
			ex.printStackTrace();
		}
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg == null) return;
		
		Gson gson = new Gson();
		
		System.out.println("Message from: " + client + ", type: " + msg.getClass().getSimpleName() + ", content: " + gson.toJson(msg));
		
		if (msg instanceof IncidentalParkingRequest) {
			OnetimeService result = databaseController.insertIncidentalParking((IncidentalParkingRequest) msg);
			sendToClient(client, ServerResponse.decide("Entry creation", result != null));
		}
		
		else if (msg instanceof ListOnetimeEntriesRequest) {
			ListOnetimeEntriesRequest request = (ListOnetimeEntriesRequest) msg;
			Collection<OnetimeService> result = databaseController.getOnetimeParkingEntriesForCustomer(request.getCustomerID());
			System.out.println(result);
			if (result == null) {
				sendToClient(client, ServerResponse.error("Entry retrieval failed"));
			} else {
				sendToClient(client, new ListOnetimeEntriesResponse("Entry retrieval successful", result, request.getCustomerID()));
			}
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
			ServerApplication server = new ServerApplication(port, remote);
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			ex.printStackTrace();
		}
	}
}
