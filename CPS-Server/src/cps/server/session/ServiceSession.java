package cps.server.session;

import java.sql.Connection;

import cps.entities.people.*;

public class ServiceSession extends BasicSession {
	CompanyPerson user;

	@Override
	public User getUser() {
		return user;
	}
	
	public CompanyPerson login(Connection conn, String username, String password) {
		user = CompanyPersonService.findWithLoginData(username, password);		
		return user;
	}
}
