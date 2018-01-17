package cps.server.controllers;

import com.google.gson.Gson;

import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.database.DatabaseController;

/**
 * The Class RequestController.
 */
public abstract class RequestController {

  /** The database controller. */
  protected final DatabaseController database;

  /** The server application. */
  protected final ServerController serverController;
  protected final Gson gson;

  /**
   * Instantiates a new request controller.
   *
   * @param serverController
   *          the server application
   */
  public RequestController(ServerController serverController) {
    this.database = serverController.getDatabaseController();
    this.serverController = serverController;
    this.gson = new Gson();
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

  public void errorIf(boolean condition, String message) throws ServerException {
    if (condition) {
      throw new ServerException(message);
    }
  }

  public void errorIfNull(Object object, String message) throws ServerException {
    errorIf(object == null, message);
  }
}
