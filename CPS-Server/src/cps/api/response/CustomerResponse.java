package cps.api.response;

public abstract class CustomerResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;
	private int customerID;
	private String password;

	public CustomerResponse(boolean success, String description, int customerID, String password) {
		super(success, description);
		this.customerID = customerID;
		this.password = password;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
