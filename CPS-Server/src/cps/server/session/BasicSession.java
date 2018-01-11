package cps.server.session;

import cps.entities.people.User;

public class BasicSession implements UserSession {
	private UserSession newSession;

	@Override
	public User getUser() {
		return null;
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
