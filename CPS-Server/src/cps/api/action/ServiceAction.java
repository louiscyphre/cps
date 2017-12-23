package cps.api.action;

import java.io.Serializable;

public abstract class ServiceAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private int userID;

	public ServiceAction(int userID) {
		super();
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
}
