package cps.server.controllers;

import cps.server.ServerController;
import cps.server.ServerException;

/**
 * The Class RequestController.
 */
public abstract class RequestController {
	
	/** The database controller. */
	protected final DatabaseController database;
	
	/** The server application. */
	protected final ServerController serverController;
	
	/**
	 * Instantiates a new request controller.
	 *
	 * @param serverController the server application
	 */
	public RequestController(ServerController serverController) {
		this.database = serverController.getDatabaseController();
		this.serverController = serverController;
	}
	
	/**
	 * Gets the database controller.
	 *
	 * @return the database controller
	 */
	public DatabaseController getDatabaseController() {
		return database;
	}

	/**
	 * Gets the server application.
	 *
	 * @return the server application
	 */
	public ServerController getServerController() {
		return serverController;
	}
	
	public void errorIfNull(Object object, String message) throws ServerException {
		if (object == null) {
			throw new ServerException(message);
		}
	}
}
