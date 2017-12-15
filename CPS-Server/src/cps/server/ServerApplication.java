package cps.server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;

import cps.common.*;
import cps.entities.models.*;
import cps.api.request.*;
import cps.api.response.*;

public class ServerApplication extends AbstractServer {
	DatabaseController databaseController;
	
	/**
	 * Constructs an instance of the server application.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public ServerApplication(int port) throws Exception {
		super(port);
		databaseController = new DatabaseController(Constants.DB_HOST, Constants.DB_NAME, Constants.DB_USERNAME,
				Constants.DB_PASSWORD);
	}

	private OnetimeService insertIncidentalParking(IncidentalParkingRequest incidentalParking) {
		Connection conn = null;
		OnetimeService onetimeParking = null;

		try {
			conn = databaseController.getConnection();
			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp plannedEndTime = Timestamp.valueOf(incidentalParking.getPlannedEndTime());
			onetimeParking = OnetimeService.create(conn, IncidentalParkingRequest.TYPE, incidentalParking.getCustomerID(),
					incidentalParking.getEmail(), incidentalParking.getCarID(), incidentalParking.getLotID(), startTime,
					plannedEndTime, false);
			// System.out.println(onetimeParking);
		} catch (SQLException ex) {
			databaseController.handleSQLException(ex);
		} finally {
			databaseController.closeConnection(conn);
		}

		return onetimeParking;
	}
	
	private Collection<OnetimeService> getOnetimeParkingEntriesForCustomer(int customerID) {
		Connection conn = null;
		Collection<OnetimeService> results = null;
		
		try {
			conn = databaseController.getConnection();
			results = OnetimeService.findByCustomerID(conn, customerID);
		} catch (SQLException ex) {
			databaseController.handleSQLException(ex);
		} finally {
			databaseController.closeConnection(conn);
		}
		
		return results;
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
			OnetimeService result = insertIncidentalParking((IncidentalParkingRequest) msg);
			sendToClient(client, ServerResponse.decide("Entry creation", result != null));
		}
		
		else if (msg instanceof ListOnetimeEntriesRequest) {
			ListOnetimeEntriesRequest request = (ListOnetimeEntriesRequest) msg;
			Collection<OnetimeService> result = getOnetimeParkingEntriesForCustomer(request.getCustomerID());
			System.out.println(result);
			if (result == null) {
				sendToClient(client, ServerResponse.error("Entry retrieval failed"));
			} else {
				sendToClient(client, new ListOnetimeEntriesResponse("Entry retrieval successful", result));
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

		try {
			ServerApplication server = new ServerApplication(port);
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			ex.printStackTrace();
		}
	}
}
