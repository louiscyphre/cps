package cps.api.request;

import cps.api.response.ServerResponse;
import cps.server.session.UserSession;

public class ListOnetimeEntriesRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;
	
	public ListOnetimeEntriesRequest(int customerID) {
		super(customerID);
	}

	@Override
	public ServerResponse handle(RequestHandler handler, UserSession session) {
		return handler.handle(this, session);
	}
}
