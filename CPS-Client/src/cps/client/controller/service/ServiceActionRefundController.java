package cps.client.controller.service;

import cps.api.action.RefundAction;
import cps.api.response.RefundResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.people.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

// TODO verify with server side how the refund should behave
public class ServiceActionRefundController extends ServiceActionControllerBase {
  @FXML // fx:id="complaintIdTF"
  private TextField complaintIdTF; // Value injected by FXMLLoader

  @FXML // fx:id="refundAmountTF"
  private TextField refundAmountTF; // Value injected by FXMLLoader

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert complaintIdTF != null : "fx:id=\"complaintIdTF\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert refundAmountTF != null : "fx:id=\"refundAmountTF\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert refundAmountTF != null : "fx:id=\"refundAmountTF\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_REFUND);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    complaintIdTF.clear();
    refundAmountTF.clear();
  }

  @Override
  void validateAndSend() { 
    try {
      int complaintId = requireInteger(complaintIdTF, "Complaint ID");
      float refundAmount = requireFloat(refundAmountTF, "Refund Amount");
  
      User user = ControllersClientAdapter.getEmployeeContext().requireCompanyPerson();
      RefundAction action = new RefundAction(user.getId(), refundAmount, complaintId) ;
      ControllersClientAdapter.getClient().sendRequest(action);
    } catch (Exception e) {
      displayError(e.getMessage());
    }    
  }

  @Override
  public ServerResponse handle(RefundResponse response) {
    if (response.success()) {
      turnProcessingStateOff();
      displayInfo(response.getDescription());
    } else {
      displayError(response.getDescription());
      turnProcessingStateOff();
    }
    
    return null;
  }
}
