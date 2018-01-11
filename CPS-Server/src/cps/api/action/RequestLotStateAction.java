package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class RequestLotStateAction extends LotAction {
	private static final long serialVersionUID = 1L;
	
	public RequestLotStateAction(int userID, int lotID) {
		super(userID, lotID);
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}
}
