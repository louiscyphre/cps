package cps.api.action;

public class RefundAction extends ServiceAction {
	private static final long serialVersionUID = 1L;
	private double amount;
	private int complaintID;

	public RefundAction(double amount, int complaintID) {
		super();
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
}
