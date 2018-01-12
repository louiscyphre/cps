package cps.api.request;

import java.io.Serializable;
import cps.api.response.ServerResponse;
import cps.server.session.UserSession;

public abstract class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	public abstract ServerResponse handle(RequestHandler handler, UserSession session);
}
