package cps.server.session;

import cps.entities.people.*;

public class ServiceSession extends BasicSession {
	CompanyPerson user;

	@Override
	public User getUser() {
		return user;
	}
	
	public CompanyPerson login(String username, String password) {
		user = CompanyPersonService.findWithLoginData(username, password);		
		return user;
	}
}
