package cps.client;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Scanner;

import com.google.gson.Gson;

import cps.common.*;
import cps.api.action.*;
import cps.api.request.*;
import cps.api.response.*;
import cps.entities.models.*;
import cps.entities.people.*;

@SuppressWarnings("unused")
public class ConsoleClient implements ClientUI {
  ClientController client;
  Gson             gson = new Gson();

  /**
   * Constructs an instance of the ConsoleClient UI.
   *
   * @param host
   *          The host to connect to.
   * @param port
   *          The port to connect on.
   */
  public ConsoleClient(String host, int port) {
    try {
      client = new ClientController(host, port, this);
    } catch (Exception exception) {
      System.out.println("Error: Can't setup connection! Terminating client.");
      System.exit(1);
    }
  }

  /**
   * This method overrides the method in the ClientUI interface. It displays a
   * message onto the screen.
   *
   * @param message
   *          The string to be displayed.
   */
  public void display(Object message) {
    if (message instanceof ListOnetimeEntriesResponse) {
      ListOnetimeEntriesResponse response = (ListOnetimeEntriesResponse) message;
      System.out.println("OnetimeService entries for customer id: " + response.getCustomerID() + ", count: "
          + response.getData().size());
      for (OnetimeService entry : response.getData()) {
        System.out.println(gson.toJson(entry));
      }
    }

    else if (message instanceof ServerResponse) {
      ServerResponse response = (ServerResponse) message;
      System.out.println("Response from server: " + response);
    }

    else {
      System.out.println("Unrecognized server response type: " + message.getClass());
    }
  }

  @Override
  public void displayError(Object message) {
    System.out.println("Error: " + message);
  }

  private static LocalDateTime readTime(Scanner scanner) {
    String timeStr = scanner.nextLine().trim();
    return LocalDateTime.parse(timeStr);
  }

  private void menuChoiceIncidentalParking() {
    IncidentalParkingRequest request = null;
    Scanner scanner = new Scanner(System.in);
    boolean done = false;

    while (!done) {
      try {
        System.out.print("User ID> ");
        int userID = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Email> ");
        String email = scanner.nextLine().trim();

        System.out.print("Car ID> ");
        String carID = scanner.nextLine().trim();

        System.out.print("Lot ID> ");
        int lotID = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Planned end time> ");
        LocalDateTime date = readTime(scanner);

        request = new IncidentalParkingRequest(userID, email, carID, lotID, date);
        done = true;
      } catch (Exception ex) {
        System.out.println(ex);
        System.out.println("Invalid data format. Try again? [y/n] ");
        if (scanner.nextLine().trim().equals("n")) {
          done = true;
        }
      }
    }

    if (request != null) {
      System.out.println("Sending IncidentalParking request: " + gson.toJson(request));
      client.handleMessageFromClientUI(request);
    }
  }

  @SuppressWarnings("resource")
  private void menuChoiceViewRequests() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("User ID> ");
    int userID = 0;

    try {
      userID = Integer.parseInt(scanner.nextLine().trim());
    } catch (Exception ex) {
      System.out.println(ex);
      System.out.println("Invalid data format.");
      return;
    }

    ListOnetimeEntriesRequest request = new ListOnetimeEntriesRequest(userID);
    System.out.println("Sending status query: " + gson.toJson(request));
    client.handleMessageFromClientUI(request);
  }

  @SuppressWarnings("resource")
  private void menuChoiceParkingEntry() {
    ParkingEntryRequest request = null;
    Scanner scanner = new Scanner(System.in);
    boolean done = false;

    while (!done) {
      try {
        System.out.print("User ID> ");
        int userID = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Car ID> ");
        String carID = scanner.nextLine().trim();

        System.out.print("Subscription ID> ");
        int subsID = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Lot ID> ");
        int lotID = Integer.parseInt(scanner.nextLine().trim());

        request = new ParkingEntryRequest(userID, subsID, lotID, carID);
        done = true;
      } catch (Exception ex) {
        System.out.println(ex);
        System.out.println("Invalid data format. Try again? [y/n] ");
        if (scanner.nextLine().trim().equals("n")) {
          done = true;
        }
      }
    }

    if (request != null) {
      System.out.println("Sending ParkingEntry request: " + gson.toJson(request));
      client.handleMessageFromClientUI(request);
    }

  }

  @SuppressWarnings("resource")
  private void menuChoiceInitParkingLot() {
    InitLotAction request = null;
    Scanner scanner = new Scanner(System.in);
    boolean done = false;

    while (!done) {
      try {
        System.out.print("Street Address> ");
        String streetAddress = scanner.nextLine().trim();

        System.out.print("Cars per row> ");
        int width = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Price for IncidentalParkingRequest> ");
        float price1 = Float.parseFloat(scanner.nextLine().trim());

        System.out.print("Price for ReservedParkingRequest> ");
        float price2 = Float.parseFloat(scanner.nextLine().trim());

        System.out.print("Robot IP> ");
        String robotIP = scanner.nextLine().trim();

        request = new InitLotAction(1, streetAddress, width, price1, price2, robotIP);
        done = true;
      } catch (Exception ex) {
        System.out.println(ex);
        System.out.println("Invalid data format. Try again? [y/n] ");
        if (scanner.nextLine().trim().equals("n")) {
          done = true;
        }
      }
    }

    if (request != null) {
      System.out.println("Sending InitLotAction request: " + gson.toJson(request));
      client.handleMessageFromClientUI(request);
    }
  }

  /**
   * This method waits for input from the console. Once it is received, it sends
   * it to the client's message handler.
   */
  public void interactWithUser() {
    try {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String input;

      while (true) {
        System.out.println("Main menu:");
        System.out.println("[1] Quit");
        System.out.println("[2] Request Incidental Parking");
        System.out.println("[3] View my Parking Requests");
        System.out.println("[4] Request Parking Entry");
        System.out.println("[5] Init Parking Lot");
        input = fromConsole.readLine();
        int choice = Utilities.stringToInteger(input, -1);

        switch (choice) {
          case 1:
            client.closeConnection();
            return;
          case 2:
            menuChoiceIncidentalParking();
            break;
          case 3:
            menuChoiceViewRequests();
            break;
          case 4:
            menuChoiceParkingEntry();
            break;
          case 5:
            menuChoiceInitParkingLot();
            break;
          default:
            System.out.println("Invalid choice. Please choose a number from 1 to 3.");
        }
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String host = Constants.DEFAULT_HOST;
    int port = Constants.DEFAULT_PORT;

    try {
      host = args[0];
    } catch (Throwable t) {
      host = Constants.DEFAULT_HOST;
    }

    try {
      port = Integer.parseInt(args[1]);
    } catch (Throwable t) {
      port = Constants.DEFAULT_PORT;
    }

    ConsoleClient app = new ConsoleClient(host, port);
    app.interactWithUser();
  }
}
