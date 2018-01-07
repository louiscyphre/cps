package cps.api.response;

public class SubscriptionResponse extends CustomerResponse {
	private static final long serialVersionUID = 1L;
	private int serviceID;
	
	public SubscriptionResponse(boolean success, String description, int customerID, String password, int serviceID) {
		super(success, description, customerID, password);
		this.serviceID = serviceID;
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}
}
