package cps.api.response;

public class InitLotResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;
	private int lotID;

	public InitLotResponse(boolean success, String description, int lotID) {
		super(success, description);
		this.lotID = lotID;
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}

	@Override
	public ServerResponse handle(ResponseHandler handler) {
		return handler.handle(this);
	}
}
