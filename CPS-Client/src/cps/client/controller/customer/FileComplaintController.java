package cps.client.controller.customer;

import cps.api.request.ComplaintRequest;
import cps.api.response.ComplaintResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class FileComplaintController extends CustomerActionControllerBaseSubmitAndFinish {
  @FXML // fx:id="complaintContent"
  private TextArea complaintContent; // Value injected by FXMLLoader

  @FXML
  // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    super.baseInitialize();
    assert complaintContent != null : "fx:id=\"complaintContent\" was not injected: check your FXML file 'EnterParkingScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.FILE_COMPLAINT);
  }

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
  
  @Override
  public ServerResponse handle(ComplaintResponse response) {
    super.handleGenericResponse(response);
    if(response.success()) {
      setFinishInsteadOfSubmit(true);
    }
    return null;
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    complaintContent.clear();
  }

}
