package cps.api.response;

import java.io.Serializable;

/**
 * The Class ServerResponse.
 */
public class ServerResponse implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant STATUS_OK. */
	public static final int STATUS_OK = 1;
	
	/** The Constant STATUS_ERROR. */
	public static final int STATUS_ERROR = -1;
	
	/** The status. */
	private int status;
	
	/** The description. */
	private String description;
	
	/**
	 * Instantiates a new server response.
	 */
	public ServerResponse() {
		this.status = STATUS_OK;
		this.description = "";
	}
	
	/**
	 * Instantiates a new server response.
	 *
	 * @param status the status
	 * @param description the description
	 */
	public ServerResponse(int status, String description) {
		this.status = status;
		this.description = description;
	}
	
	/**
	 * Instantiates a new server response.
	 *
	 * @param success was the request completed successfully
	 * @param description short description, will get extended with success message
	 */	
	public ServerResponse(boolean success, String description) {
		this.status = success ? STATUS_OK : STATUS_ERROR;
		this.description = description + (success ? " completed successfully" : " failed");
	}
	
	/**
	 * Returns Server Response OK.
	 *
	 * @param description the description
	 * @return the server response
	 */
	public static ServerResponse ok(String description) {
		return new ServerResponse(STATUS_OK, description);
	}
	
	/**
	 * Returns Server Response Error.
	 *
	 * @param description the description
	 * @return the server response
	 */
	public static ServerResponse error(String description) {
		return new ServerResponse(STATUS_ERROR, description);
	}
	
//	/**
//	 * Returns Server Response OK if condition == true
//	 * else returns Server Response Error
//	 *
//	 * @param description the description
//	 * @param condition the condition
//	 * @return the server response
//	 */
//	public static ServerResponse decide(String description, boolean condition) {
//		if (condition) {
//			return ServerResponse.ok(description + " completed successfully");
//		} else {
//			return ServerResponse.error(description + " failed");
//		}		
//	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets status = STATUS_ERROR and updates the description
	 * @param description
	 */
	public void setError(String description) {
		this.status = STATUS_ERROR;
		this.description = description;		
	}
	
	/**
	 * Sets status = STATUS_OK and updates the description
	 * @param description
	 */
	public void setSuccess(String description) {
		this.status = STATUS_OK;
		this.description = description;		
	}

	
	/** 
	 * Parse status property to String 
	 */
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
	
	public boolean success() {
		return this.status == STATUS_OK;
	}
	
	public boolean isError() {
		return this.status == STATUS_ERROR;
	}
}
