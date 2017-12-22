package cps.api.request;

public class DisableSlotRequest extends ClientRequest {
	private static final long serialVersionUID = 1L;
	private int lotID;
	private int i;
	private int j;
	private int k;

	/*public DisableSlotRequest(int userId, int lotId, int i, int j, int k) {
		super(userId);
		// TODO Auto-generated constructor stub
	}*/

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

}
