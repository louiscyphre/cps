package cps.api.action;

public class InitParkingLot extends LotAction {
	private static final long serialVersionUID = 1L;
	private int size;

	public InitParkingLot(int userID, int lotID, int size) {
		super(userID, lotID);
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
