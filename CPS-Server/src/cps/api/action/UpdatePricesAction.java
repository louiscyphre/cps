package cps.api.action;

import cps.api.response.ServerResponse;
import cps.server.RequestHandler;

public class UpdatePricesAction extends LotAction {
	private static final long serialVersionUID = 1L;
	private float price1;
	private float price2;

	public UpdatePricesAction(int userID, int lotID, float price1, float price2) {
		super(userID, lotID);
		this.price1 = price1;
		this.price2 = price2;
	}

	public float getPrice1() {
		return price1;
	}

	public void setPrice1(float price1) {
		this.price1 = price1;
	}

	public float getPrice2() {
		return price2;
	}

	public void setPrice2(float price2) {
		this.price2 = price2;
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}
}
