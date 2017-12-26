package cps.api.action;

public class RequestLotStateAction extends LotAction {
	private static final long serialVersionUID = 1L;
	
	public RequestLotStateAction(int userID, int lotID) {
		super(userID, lotID);
	}
}
