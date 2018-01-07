package cps.api.response;

public class FullSubscriptionResponse extends SubscriptionResponse {
	private static final long serialVersionUID = 1L;
	
	public FullSubscriptionResponse(boolean success, String description, int customerID, String password,
			int serviceID) {
		super(success, "FullSubscriptionRequest", customerID, password, serviceID);
	}
}
