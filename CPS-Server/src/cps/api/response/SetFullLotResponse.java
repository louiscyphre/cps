package cps.api.response;

public class SetFullLotResponse extends ServerResponse {
	private static final long serialVersionUID = 1L;

	@Override
	public ServerResponse handle(ResponseHandler handler) {
		return handler.handle(this);
	}

	public SetFullLotResponse(boolean success, String description) {
		super(success, description);
	}
}
