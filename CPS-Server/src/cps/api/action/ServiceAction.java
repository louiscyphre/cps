package cps.api.action;

import cps.api.request.Request;

/** Base class for all service actions - actions that can be performed by employees of the system and the global manager. */
public abstract class ServiceAction extends Request {
  private static final long serialVersionUID = 1L;
  private int               userID;

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
