package cps.api.response;

public class RegularSubscriptionResponse extends SubscriptionResponse {
	private static final long serialVersionUID = 1L;

	public RegularSubscriptionResponse(boolean success, String description, int customerID, String password,
			int serviceID) {
		super(success, "RegularSubscriptionRequest", customerID, password, serviceID);
	}
}
