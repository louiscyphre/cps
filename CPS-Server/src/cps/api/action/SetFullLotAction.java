package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

/** Is sent by the client application when a local employee wants to mark a parking lot as full
 * and/or to set a list of alternative parking lots where customers will be redirected in case the current parking lot is full. */
public class SetFullLotAction extends LotAction {
  private static final long serialVersionUID = 1L;

  int[]   alternativeLots;
  boolean lotFull;

  public SetFullLotAction(int userID, int lotID, boolean lotfull, int[] alternativeLots) {
    super(userID, lotID);
    this.alternativeLots = alternativeLots;
    this.lotFull = lotfull;
  }

  public boolean getLotFull() {
    return lotFull;
  }

  public void setLotFull(boolean lotFull) {
    this.lotFull = lotFull;
  }

  /** Call the handler for this request.
   * @see cps.api.request.Request#handle(cps.api.request.RequestHandler, java.lang.Object) */
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
