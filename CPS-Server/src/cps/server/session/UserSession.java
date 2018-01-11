package cps.server.session;

import cps.entities.people.User;

public interface UserSession {
	public User getUser();
	public void setNewSession(UserSession session);
	public UserSession getNewSession();
}
