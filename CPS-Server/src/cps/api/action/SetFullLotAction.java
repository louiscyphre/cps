package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class SetFullLotAction extends LotAction {
  private static final long serialVersionUID = 1L;
  int[]                     alternativeLots;
  boolean                   lotFull;

  public SetFullLotAction(int userID, int lotID, boolean lotfull, int[] alternativeLots) {
    super(userID, lotID);
    this.alternativeLots = alternativeLots;
    this.lotFull = lotfull;
  }

  // TODO discuss the lotFull flag
  public boolean getLotFull() {
    return lotFull;
  }

  public void setLotFull(boolean lotFull) {
    this.lotFull = lotFull;
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }

  public int[] getAlternativeLots() {
    return alternativeLots;
  }

  public void setAlternativeLots(int[] alternativeLots) {
    this.alternativeLots = alternativeLots;
  }
}
