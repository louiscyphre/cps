package cps.api.action;

/** Base class for various service actions that deal with parking lots. */
public abstract class LotAction extends ServiceAction {
  private static final long serialVersionUID = 1L;
  private int               lotID;

  public LotAction(int userID, int lotID) {
    super(userID);
    this.lotID = lotID;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }
}
