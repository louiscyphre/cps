package cps.api.request;

public class ComplaintRequest extends CustomerRequest {
	private static final long serialVersionUID = 1L;

	private String content;

	public ComplaintRequest(int customerID, String content) {
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
