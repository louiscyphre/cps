package cps.api.request;

import java.time.LocalDateTime;

import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.server.RequestHandler;

public class FullSubscriptionRequest extends SubscriptionRequest {
	private static final long serialVersionUID = 1L;

	public FullSubscriptionRequest(int customerID, int carID, LocalDateTime startDate) {
		super(customerID, carID, startDate);
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}

	@Override
	public int getSubscriptionType() {
		return Constants.SUBSCRIPTION_TYPE_FULL;
	}
}
