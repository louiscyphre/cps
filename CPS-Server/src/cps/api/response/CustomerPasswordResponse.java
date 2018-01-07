package cps.api.response;

public abstract class CustomerPasswordResponse extends CustomerResponse {
	private static final long serialVersionUID = 1L;
	String password;

	public CustomerPasswordResponse(boolean success, String description, int customerID, String password) {
		super(success, description, customerID);
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
