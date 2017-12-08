package cps.core;

public class Complaint extends CustomerAction {
	private static final long serialVersionUID = 1L;

	private String content;

	public Complaint(int customerID, String content) {
		super(customerID);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
