package cps.api.request;

import cps.api.response.ServerResponse;
import cps.server.RequestHandler;

public class LoginRequest extends Request {
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;

	public LoginRequest(String email, String password) {
		this.email = email;
		this.setPassword(password);
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
