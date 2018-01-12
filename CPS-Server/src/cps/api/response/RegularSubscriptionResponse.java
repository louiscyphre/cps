package cps.api.response;

public class RegularSubscriptionResponse extends SubscriptionResponse {
	private static final long serialVersionUID = 1L;

	public RegularSubscriptionResponse(boolean success, String description, int customerID, String password,
			int serviceID) {
		super(success, description, customerID, password, serviceID);
	}

	public RegularSubscriptionResponse(boolean success, String description) {
		super(success, description, 0, "", 0);
	}

	@Override
	public ServerResponse handle(ResponseHandler handler) {
		return handler.handle(this);
	}
}
