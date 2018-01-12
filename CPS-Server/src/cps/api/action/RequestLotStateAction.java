package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;
import cps.server.session.UserSession;

public class RequestLotStateAction extends LotAction {
	private static final long serialVersionUID = 1L;
	
	public RequestLotStateAction(int userID, int lotID) {
		super(userID, lotID);
	}

	@Override
	public ServerResponse handle(RequestHandler handler, UserSession session) {
		return handler.handle(this, session);
	}
}
