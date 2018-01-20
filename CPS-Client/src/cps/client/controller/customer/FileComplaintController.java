package cps.client.controller.customer;

import cps.api.request.ComplaintRequest;
import cps.api.response.ComplaintResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * File Complaint controller.
 */
public class FileComplaintController extends CustomerActionControllerBaseSubmitAndFinish {
  /**
   * Complaint Text Area.
   */
  @FXML // fx:id="complaintContent"
  private TextArea complaintContent; // Value injected by FXMLLoader

  /**
   * Initializes the Controller and Registers it.
   */
  @FXML
  // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    super.baseInitialize();
    assert complaintContent != null : "fx:id=\"complaintContent\" was not injected: check your FXML file 'EnterParkingScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.FILE_COMPLAINT);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#sendMainRequest()
   */
  @Override
  void sendMainRequest() {
    try {
      int customerId = notNull(ControllersClientAdapter.getCustomerContext(), "CustomerContext").getCustomerId();
      String content = requireField(complaintContent, "Complaint Content");
      sendRequest(new ComplaintRequest(customerId, content));
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.ComplaintResponse)
   */
  @Override
  public ServerResponse handle(ComplaintResponse response) {
    super.handleGenericResponse(response);
    if (response.success()) {
      setFinishInsteadOfSubmit(true);
    }
    return null;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    complaintContent.clear();
  }

}
