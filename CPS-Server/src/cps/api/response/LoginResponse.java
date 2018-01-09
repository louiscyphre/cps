package cps.api.response;

public class LoginResponse extends CustomerResponse {
	private static final long serialVersionUID = 1L;

	public LoginResponse(boolean success, String description, int customerID) {
		super(success, description, customerID);
	}
}
