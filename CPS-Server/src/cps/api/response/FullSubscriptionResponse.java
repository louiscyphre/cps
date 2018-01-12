package cps.api.response;

public class FullSubscriptionResponse extends SubscriptionResponse {
	private static final long serialVersionUID = 1L;

	public FullSubscriptionResponse(boolean success, String description, int customerID, String password,
			int serviceID) {
		super(success, description, customerID, password, serviceID);
	}

	public FullSubscriptionResponse(boolean success, String description) {
		super(success, description, 0, "", 0);
	}

	@Override
	public ServerResponse handle(ResponseHandler handler) {
		return handler.handle(this);
	}
}
