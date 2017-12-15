package cps.api.response;

import java.io.Serializable;

public class ServerResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int STATUS_OK = 1;
	public static final int STATUS_ERROR = -1;
	
	private int status;
	private String description;
	
	public ServerResponse() {
		this.status = STATUS_OK;
		this.description = "";
	}
	
	public ServerResponse(int status, String description) {
		this.status = status;
		this.description = description;
	}
	
	public static ServerResponse ok(String description) {
		return new ServerResponse(STATUS_OK, description);
	}
	
	public static ServerResponse error(String description) {
		return new ServerResponse(STATUS_ERROR, description);
	}
	
	public static ServerResponse decide(String description, boolean condition) {
		if (condition) {
			return ServerResponse.ok(description + " successful");
		} else {
			return ServerResponse.error(description + " failed");
		}		
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@Override
	public String toString() {
		String statusRepr;
		
		if (status == STATUS_OK) {
			statusRepr = "OK";
		} else if (status == STATUS_ERROR) {
			statusRepr = "ERROR";
		} else {
			statusRepr = Integer.toString(status);
		}
		
		return "{status: " + statusRepr + ", description: " + description + "})";
	}
}
