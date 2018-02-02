package cps.api.response;

import java.io.Serializable;

/** Base class for all server responses. */
public abstract class ServerResponse implements Serializable {

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
    this.status = STATUS_ERROR;
    this.description = "Request not completed";
  }

  /**
   * Instantiates a new server response.
   *
   * @param status
   *          the status
   * @param description
   *          the description
   */
  public ServerResponse(int status, String description) {
    this.status = status;
    this.description = description;
  }

  /**
   * Instantiates a new server response.
   *
   * @param success
   *          was the request completed successfully
   * @param description
   *          short description, will get extended with success message
   */
  public ServerResponse(boolean success, String description) {
    this.status = success ? STATUS_OK : STATUS_ERROR;
    this.description = description;
  }

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
   * @param status
   *          the new status
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
   * @param description
   *          the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets status = STATUS_ERROR and updates the description
   * 
   * @param description the description
   */
  public void setError(String description) {
    this.status = STATUS_ERROR;
    this.description = description;
  }

  /**
   * Sets status = STATUS_OK and updates the description
   * 
   * @param description the description
   */
  public void setSuccess(String description) {
    this.status = STATUS_OK;
    this.description = description;
  }

  public boolean success() {
    return this.status == STATUS_OK;
  }

  public boolean isError() {
    return this.status == STATUS_ERROR;
  }
  
  /** Call the handler for this response.
   * Response handlers are implemented in the client, in order to be able to process server responses and perform some kind of action when a response is received.
   * (Such as update a UI widget with the data in the response.)
   * This is an abstract method and it has to be implemented in each concrete subclass to call the right type of handler for that subclass.
   * @param handler the handler */
  public abstract void handle(ResponseHandler handler);
}
