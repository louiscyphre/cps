package cps.server.session;

import cps.entities.people.User;
import cps.server.ServerException;

public interface UserSession {
	public User getUser();
	public User requireUser() throws ServerException;
	public void setNewSession(UserSession session);
	public UserSession getNewSession();
}
