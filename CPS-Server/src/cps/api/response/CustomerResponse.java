package cps.api.response;

public abstract class CustomerResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;
	private int customerID;

	public CustomerResponse(boolean success, String description, int customerID) {
		super(success, description);
		this.customerID = customerID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
}
