package cps.api.action;

public abstract class LotAction extends ServiceAction {
	private static final long serialVersionUID = 1L;
	private int lotID;

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
