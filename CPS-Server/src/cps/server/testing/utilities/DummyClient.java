package cps.server.testing.utilities;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.LinkedList;

import cps.api.response.ServerResponse;
import ocsf.client.AbstractClient;

public class DummyClient extends AbstractClient {
	LinkedList<ServerResponse> responses;
	
	public DummyClient(String host, int port) {
		super(host, port);
		try {
			openConnection();
		} catch (Exception exception) {
			System.out.println("Error: Can't setup connection! Terminating client.");
			System.exit(1);
		}
	}

	@Override
	public void handleMessageFromServer(Object message) {
		assertThat(message, instanceOf(ServerResponse.class));
		responses.add((ServerResponse) message);
	}
	
	public ServerResponse getLastResponse() {
		return responses.getLast();
	}
}
