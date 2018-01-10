package cps.api.response;

public class SubscriptionResponse extends CustomerPasswordResponse {
	private static final long serialVersionUID = 1L;
	private int serviceID;
	private float payment = 0f;

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

	public float getPayment() {
		return payment;
	}

	public void setPayment(float payment) {
		this.payment = payment;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	@Override
	public ServerResponse handle(ResponseHandler handler) {
		return handler.handle(this);
	}
}
