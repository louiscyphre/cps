package cps.server.controllers;

import java.time.LocalDateTime;

import com.google.gson.Gson;

import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.database.DatabaseController;

/** Base class for controllers that process user requests. */
public abstract class RequestController {

  /** The database controller. */
  protected final DatabaseController database;

  /** The server application. */
  protected final ServerController serverController;
  protected final Gson gson;

  /** Instantiates a new request controller.
   * @param serverController the server controller */
  public RequestController(ServerController serverController) {
    this.database = serverController.getDatabaseController();
    this.serverController = serverController;
    this.gson = new Gson();
  }

  public DatabaseController getDatabaseController() {
    return database;
  }

  public ServerController getServerController() {
    return serverController;
  }

  void error(String message) throws ServerException {
      throw new ServerException(message);
  }

  void errorIf(boolean condition, String message) throws ServerException {
    if (condition) {
      throw new ServerException(message);
    }
  }

  void errorIfNull(Object object, String message) throws ServerException {
    errorIf(object == null, message);
  }
  
  LocalDateTime now() {
    return serverController.getClock().now();
  }
}
