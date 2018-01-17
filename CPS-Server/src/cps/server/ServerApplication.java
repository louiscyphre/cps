/*
 * 
 */
package cps.server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import com.google.gson.Gson;

import cps.common.*;
import cps.api.request.Request;
import cps.api.response.*;
import cps.server.background.Reminder;
import cps.server.controllers.*;
import cps.server.session.*;

/**
 * The Class ServerApplication.
 */
public class ServerApplication extends AbstractServer {
  private Gson             gson     = new Gson();
  private ServerController serverController;
  private Reminder         reminder = null;

  /**
   * Constructs an instance of the server application.
   *
   * @param port
   *          The port number to connect on.
   * @param config
   *          the config
   * @throws Exception
   *           the exception
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

  public String describeMessage(String header, ConnectionToClient client, Object msg, Gson gson) {
    return String.format("%s: %s %s\n  type: %s\n  content: %s", header, client.getName(), client,
        msg.getClass().getSimpleName(), gson.toJson(msg));
  }

  /**
   * Send object to client.
   *
   * @param client
   *          The client object
   * @param msg
   *          The Object to be serialized and sent
   */
  private void sendToClient(ConnectionToClient client, Object msg) {
    try {
      System.out.println(describeMessage("Sending to", client, msg, gson));
      System.out.println();
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
   *          The message received from the client.
   * @param client
   *          The connection from which the message originated.
   */
  @Override
  protected void handleMessageFromClient(Object message, ConnectionToClient client) {
    // Message object should be of type Request and not null
    if (message == null || !(message instanceof Request)) {
      sendToClient(client, SimpleResponse.error("Unknown request type"));
      return;
    }

    Request request = (Request) message; // type cast was checked with
                                         // instanceof

    // Print the request to console
    System.out.println(describeMessage("Received from", client, request, gson));

    // Get the context object associated with this client connection, if exists
    // A context object stores data about the current client session
    SessionHolder context = (SessionHolder) client.getInfo("Context");

    if (context == null) { // Session doesn't exist -> create new
      context = new SessionHolder();
      client.setInfo("Context", context);
    }

    // Dispatch request and get a response object
    ServerResponse response = serverController.dispatch(request, context);

    // Send response to client
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
   * This method overrides the one in the superclass. Called when the server
   * stops listening for connections.
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
      server.initialize();
      server.listen(); // Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
      ex.printStackTrace();
    }
  }

  private void initialize() {
// Create background thread to poll the db every minute
    reminder = new Reminder(getServerController().getDatabaseController(),
        getServerController().getOnetimeParkingController());
    reminder.start();

  }

}
