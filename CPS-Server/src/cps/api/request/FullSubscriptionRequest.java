package cps.api.request;

import java.time.LocalDate;

import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.server.session.UserSession;

public class FullSubscriptionRequest extends SubscriptionRequest {
	private static final long serialVersionUID = 1L;

	public FullSubscriptionRequest(int customerID, String email, String carID, LocalDate startDate) {
		super(customerID, email, carID, startDate);
	}

	@Override
	public ServerResponse handle(RequestHandler handler, UserSession session) {
		return handler.handle(this, session);
	}

	@Override
	public int getSubscriptionType() {
		return Constants.SUBSCRIPTION_TYPE_FULL;
	}
	
	@Override
	public int getLotID() {
		return 0;
	}
}
