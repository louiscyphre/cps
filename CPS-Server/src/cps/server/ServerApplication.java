/*
 * 
 */
package cps.server;

import com.google.gson.Gson;

import cps.api.request.Request;
import cps.api.response.ServerResponse;
import cps.api.response.SimpleResponse;
import cps.common.Constants;
import cps.server.background.Reminder;
import cps.server.database.DatabaseController;
import cps.server.session.SessionHolder;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/** The main class of the server application.
 * 
 * Contains the static main() method to serve as the entry point for running the application.
 * Inherits from OCSF AbstractServer and lays out a basic framework for communicating with a client connected over the network.
 * 
 * Actual processing of client requests is performed in a ServerController object,
 * which is decoupled from the networking aspect and simply generates responses for requests,
 * while performing the necessary database actions.
 * 
 * In addition, the ServerApplication object starts a thread which monitors the database for certain conditions (such as expiring subscriptions and late reservations)
 * and notifies the customer if needed.
 * */
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
    this.serverController = new ServerController(config, new RealTimeProvider());
  }

  private void initialize(ServerConfig config) throws Exception {
    // Create background thread to poll the database every minute
    reminder = new Reminder(config);
    reminder.start();
  }

  public ServerController getServerController() {
    return serverController;
  }

  public DatabaseController getDatabaseController() {
    return serverController.getDatabaseController();
  }

  String describeMessage(String header, ConnectionToClient client, Object msg, Gson gson) {
    return String.format("%s: %s %s\n  type: %s\n  content: %s", header, client.getName(), client,
        msg.getClass().getSimpleName(), gson.toJson(msg));
  }

  /**
   * Send message to client.
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
   * This method overrides the one in the superclass.
   * Called when the server starts listening for connections.
   */
  @Override
  protected void serverStarted() {
    System.out.println("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.
   * Called when the server stops listening for connections.
   */
  @Override
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }


  /**
   * This method overrides the one in the superclass.
   * Called when a new client connects to the server.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("Client connected: " + client);
  }


  /**
   * This method overrides the one in the superclass.
   * Called when an exception occurs in the client connection, and the client is disconnected as a result.
   * This gets called when the client application is closed or loses connection for some reason.
   * This allows us to keep track of which users are currently logged in, and not allow more than one session for the same user.
   */
  @Override
  protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println("Client disconnected: " + client.getName());
    
    SessionHolder context = (SessionHolder) client.getInfo("Context");
    
    if (context != null) {
      serverController.removeSession(context);
    }
  }

  /** Application entry point.
   * @param args command-line arguments */
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
      server.initialize(config);
      server.listen(); // Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
      ex.printStackTrace();
    }
  }
}
