package cps.api.response;

public class SubscriptionResponse extends CustomerPasswordResponse {
	private static final long serialVersionUID = 1L;
	private int serviceID;
	
	public SubscriptionResponse(boolean success, String description, int customerID, String password, int serviceID) {
		super(success, description, customerID, password);
		this.serviceID = serviceID;
	}
	
	public SubscriptionResponse(boolean success, String description) {
		this(success, description, 0, null, 0);
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}
}
