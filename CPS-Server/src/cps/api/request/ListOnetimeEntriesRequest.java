package cps.api.request;

import cps.api.response.ServerResponse;

public class ListOnetimeEntriesRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;

	public ListOnetimeEntriesRequest(int customerID) {
		super(customerID);
	}

	@Override
	public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
		return handler.handle(this, session);
	}
}
