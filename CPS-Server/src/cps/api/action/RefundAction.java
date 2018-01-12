package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class RefundAction extends ServiceAction {
	private static final long serialVersionUID = 1L;
	private float amount;
	private int complaintID;

	public RefundAction(int userID, float amount, int complaintID) {
		super(userID);
		this.amount = amount;
		this.complaintID = complaintID;
	}

	public RefundAction(int userID, double amount, int complaintID) {
		this(userID, (float) amount, complaintID);
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public void setAmount(double amount) {
		this.amount = (float) amount;
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
	public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
		return handler.handle(this, session);
	}
}
