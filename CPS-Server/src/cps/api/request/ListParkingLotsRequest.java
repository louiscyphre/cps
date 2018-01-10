package cps.api.request;

import cps.api.response.ServerResponse;
import cps.server.RequestHandler;

public class ListParkingLotsRequest extends Request {
	private static final long serialVersionUID = 1L;

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}
}
