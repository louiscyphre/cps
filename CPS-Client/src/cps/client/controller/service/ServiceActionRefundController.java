package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

// TODO verify with server side how the refund should behave
public class ServiceActionRefundController extends ServiceActionControllerBase {
  @FXML // fx:id="complaintIdTF"
  private TextField complaintIdTF; // Value injected by FXMLLoader

  @FXML // fx:id="refundAmountTF"
  private TextField refundAmountTF; // Value injected by FXMLLoader

  @FXML
  void handleOkButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.initialize();
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
}
