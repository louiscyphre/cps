package cps.core;

import java.io.Serializable;

public class ServerResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int STATUS_OK = 1;
	public static final int STATUS_ERROR = -1;
	
	public int status;
	public String description;
	public Object data;
	
	public ServerResponse(int status, String description, Object data) {
		this.status = status;
		this.description = description;
		this.data = data;
	}
	
	public static ServerResponse ok(String description, Object data) {
		return new ServerResponse(STATUS_OK, description, data);
	}
	
	public static ServerResponse error(String description, Object data) {
		return new ServerResponse(STATUS_ERROR, description, data);
	}
	
	public static ServerResponse decide(String description, Object data) {
		if (data == null) {
			return ServerResponse.error(description + " failed", data);
		} else {
			return ServerResponse.ok(description + " successful", data);
		}		
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
		
		return "ServerResponse({status: " + statusRepr + ", description: " + description + "})"; 
	}
}
