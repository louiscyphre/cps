package cps.api.action;

public class SetFullLotAction extends LotAction {
	private static final long serialVersionUID = 1L;
	int alternativeLotID;

	public SetFullLotAction(int userID, int lotID, int alternativeLotID) {
		super(userID, lotID);
		this.alternativeLotID = alternativeLotID;
	}

	public int getAlternativeLotID() {
		return alternativeLotID;
	}

	public void setAlternativeLotID(int alternativeLotID) {
		this.alternativeLotID = alternativeLotID;
	}
}