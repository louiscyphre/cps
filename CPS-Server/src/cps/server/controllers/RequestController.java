package cps.server.controllers;

import cps.server.ServerApplication;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestController.
 */
public abstract class RequestController {
	
	/** The database controller. */
	protected final DatabaseController databaseController;
	
	/** The server application. */
	protected final ServerApplication serverApplication;
	
	/**
	 * Instantiates a new request controller.
	 *
	 * @param serverApplication the server application
	 */
	public RequestController(ServerApplication serverApplication) {
		this.databaseController = serverApplication.getDatabaseController();
		this.serverApplication = serverApplication;
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
	public ServerApplication getServerApplication() {
		return serverApplication;
	}

}
