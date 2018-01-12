package cps.server.session;

import cps.entities.people.User;
import cps.server.ServerException;

public class BasicSession implements UserSession {
	private UserSession newSession;

	@Override
	public User getUser() {
		return null;
	}

	@Override
	public User requireUser() throws ServerException {
		User user = getUser();

		if (user == null) {
			throw new ServerException("This action requires login");
		}

		return user;
	}

	@Override
	public void setNewSession(UserSession session) {
		newSession = session;

	}

	@Override
	public UserSession getNewSession() {
		return newSession;
	}

}
