package cps.core;

public class Complaint extends CustomerAction {

	private String content;

	public Complaint(int id, int customerID, String content) {
		super(id, customerID);
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
