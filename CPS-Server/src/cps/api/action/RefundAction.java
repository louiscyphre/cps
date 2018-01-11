package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class RefundAction extends ServiceAction {
	private static final long serialVersionUID = 1L;
	private double amount;
	private int complaintID;

	public RefundAction(int userID, double amount, int complaintID) {
		super(userID);
		this.amount = amount;
		this.complaintID = complaintID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getComplaintID() {
		return complaintID;
	}

	public void setComplaintID(int complaintID) {
		this.complaintID = complaintID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public ServerResponse handle(RequestHandler handler) {
		return handler.handle(this);
	}
}
