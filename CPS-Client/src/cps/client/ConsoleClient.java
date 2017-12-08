package cps.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import cps.common.Constants;
import cps.common.Utilities;
import cps.core.IncidentalParking;

public class ConsoleClient implements ClientUI {
	ClientController client;

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
	public void display(String message) {
		System.out.println("> " + message);
	}

	@Override
	public void displayError(String message) {
		System.out.println("> " + message);		
	}

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */
	public void interactWithUser() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			String message;

			while (true) {				
				System.out.println("[1] Send Parking Request");
				System.out.println("[2] Quit");				
				message = fromConsole.readLine();
				int choice = Utilities.stringToInteger(message, -1);
				
				switch (choice) {
				case 1:
					LocalDateTime date = LocalDateTime.now().plusHours(3);
					IncidentalParking request = new IncidentalParking(1, 1, "user@email", 1, 1, date);
					System.out.println(request);
					client.handleMessageFromClientUI(request);
					break;
				case 2:
					client.closeConnection();
					return;
				default:
					System.out.println("Invalid choice. Please choose 1 or 2.");
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
