package cps.api.action;

public class SetLotRedirectAction extends LotAction {
	private static final long serialVersionUID = 1L;
	int alternativeLotID;

	public int getAlternativeLotID() {
		return alternativeLotID;
	}

	public void setAlternativeLotID(int alternativeLotID) {
		this.alternativeLotID = alternativeLotID;
	}
}
