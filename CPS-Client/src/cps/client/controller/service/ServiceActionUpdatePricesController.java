package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServiceActionUpdatePricesController extends ServiceActionControllerBase {

  @FXML // fx:id="newreservedTF"
  private TextField newreservedTF; // Value injected by FXMLLoader

  @FXML // fx:id="newincidentalTF"
  private TextField newincidentalTF; // Value injected by FXMLLoader

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert newreservedTF != null : "fx:id=\"newreservedTF\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";
    assert newincidentalTF != null : "fx:id=\"newincidentalTF\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_UPDATE_PRICES);

  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    newreservedTF.clear();
    newincidentalTF.clear();
  }

  @Override
  void validateAndSend() {
    // TODO Auto-generated method stub
    
  }
}
