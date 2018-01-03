package cps.server.controllers;

import cps.server.ServerApplication;
import cps.server.ServerController;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestController.
 */
public abstract class RequestController {
	
	/** The database controller. */
	protected final DatabaseController databaseController;
	
	/** The server application. */
	protected final ServerController serverController;
	
	/**
	 * Instantiates a new request controller.
	 *
	 * @param serverController the server application
	 */
	public RequestController(ServerController serverController) {
		this.databaseController = serverController.getDatabaseController();
		this.serverController = serverController;
	}
	
	/**
	 * Gets the database controller.
	 *
	 * @return the database controller
	 */
	public DatabaseController getDatabaseController() {
		return databaseController;
	}

	/**
	 * Gets the server application.
	 *
	 * @return the server application
	 */
	public ServerController getServerController() {
		return serverController;
	}

}
