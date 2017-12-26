package cps.server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import com.google.gson.Gson;

import cps.common.*;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.EntryExitController;
import cps.server.controllers.OnetimeParkingController;
import cps.server.controllers.RobotController;
import cps.api.request.*;
import cps.api.response.*;

public class ServerApplication extends AbstractServer {
  Gson gson = new Gson();
	private ServerConfig config;
	private DatabaseController databaseController;
	private RobotController robotController;
	private OnetimeParkingController onetimeParkingController;
  private EntryExitController entryExitController;
	
	/**
	 * Constructs an instance of the server application.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public ServerApplication(int port, ServerConfig config) throws Exception {
		super(port);
		this.config = config;		
		databaseController = new DatabaseController(config.get("db.host"), config.get("db.name"), config.get("db.username"),
				config.get("db.password"));
		robotController = new RobotController();
		onetimeParkingController = new OnetimeParkingController(this);
		entryExitController = new EntryExitController(this);
		
	}

	public ServerConfig getConfig() {
		return config;
	}

	public void setConfig(ServerConfig config) {
		this.config = config;
	}

	public DatabaseController getDatabaseController() {
		return databaseController;
	}

	public void setDatabaseController(DatabaseController databaseController) {
		this.databaseController = databaseController;
	}

	public RobotController getRobotController() {
		return robotController;
	}

	public void setRobotController(RobotController robotController) {
		this.robotController = robotController;
	}

	public OnetimeParkingController getOnetimeParkingController() {
		return onetimeParkingController;
	}

	public void setOnetimeParkingController(OnetimeParkingController onetimeParkingController) {
		this.onetimeParkingController = onetimeParkingController;
	}

	private void sendToClient(ConnectionToClient client, Object msg) {
		try {
			System.out.print("Sending to client: " + gson.toJson(msg));
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
		if (message == null) return;
		
		System.out.println("Message from: " + client + ", type: " + message.getClass().getSimpleName() + ", content: " + gson.toJson(message));
		ServerResponse response = null;
		
		// TODO: replace this with a better dispatch method
		if (message instanceof IncidentalParkingRequest) {
			response = onetimeParkingController.handle((IncidentalParkingRequest) message);
		} else if (message instanceof ListOnetimeEntriesRequest) {
			response = onetimeParkingController.handle((ListOnetimeEntriesRequest) message);
		} else if (message instanceof ParkingEntryRequest) {
		  response = entryExitController.handle((ParkingEntryRequest) message);
		} else {
			response = ServerResponse.error("Unknown request");
		}
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
			ServerConfig config = remote ? ServerConfig.getRemote() : ServerConfig.getLocal();
			ServerApplication server = new ServerApplication(port, config);
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			ex.printStackTrace();
		}
	}
}
