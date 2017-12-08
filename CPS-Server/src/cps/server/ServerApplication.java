package cps.server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;

import cps.common.*;
import cps.core.*;
import cps.model.*;

public class ServerApplication extends AbstractServer {
	/**
	 * Constructs an instance of the server application.
	 *
	 * @param port
	 *            The port number to connect on.
	 */

	DatabaseController databaseController;

	public ServerApplication(int port) throws Exception {
		super(port);
		databaseController = new DatabaseController(Constants.DB_HOST, Constants.DB_NAME, Constants.DB_USERNAME,
				Constants.DB_PASSWORD);
	}

	private void sendToClient(ConnectionToClient client, Object msg) {
		try {
			client.sendToClient(msg);
		} catch (Exception ex) {
			System.out.println("ERROR - Could not send to client!");
			ex.printStackTrace();
		}
	}

	private void insertIncidentalParking(IncidentalParking incidentalParking) {
		Connection conn = null;
		try {
			conn = databaseController.getConnection();
			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp plannedEndTime = Timestamp.valueOf(incidentalParking.getPlannedEndTime());
			OnetimeParking onetimeParking = OnetimeParking.create(conn, IncidentalParking.TYPE,
				incidentalParking.getCustomerID(), incidentalParking.getEmail(), incidentalParking.getCarID(),
				incidentalParking.getLotID(), startTime, plannedEndTime, startTime, null);
			System.out.println(onetimeParking);
		} catch (SQLException ex) {
			databaseController.handleSQLException(ex);
		} finally {
			databaseController.closeConnection(conn);
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
		if (msg != null) {
			System.out
					.println("Message received: " + msg + ", from: " + client + ", type: " + msg.getClass().toString());
			sendToClient(client, msg);
			if (msg instanceof IncidentalParking) {
				insertIncidentalParking((IncidentalParking) msg);
			}
		} else {
			System.out.println("Message received: " + msg + ", from: " + client);
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

		try {
			ServerApplication server = new ServerApplication(port);
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			ex.printStackTrace();
		}
	}
}
