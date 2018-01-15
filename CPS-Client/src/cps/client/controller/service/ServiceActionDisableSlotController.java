package cps.client.controller.service;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

// TODO stub - need to implement the dynamic grid creation and the view initialization
public class ServiceActionDisableSlotController extends ServiceActionControllerBase {
  @FXML // fx:id="columnITF"
  private TextField columnITF; // Value injected by FXMLLoader

  @FXML // fx:id="floorJTF"
  private TextField floorJTF; // Value injected by FXMLLoader

  @FXML // fx:id="rowKTF"
  private TextField rowKTF; // Value injected by FXMLLoader

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert columnITF != null : "fx:id=\"columnITF\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
    assert floorJTF != null : "fx:id=\"floorJTF\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
    assert rowKTF != null : "fx:id=\"rowKTF\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_DISABLE_SLOT);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    columnITF.clear();
    floorJTF.clear();
    rowKTF.clear();
  }

  @Override
  void validateAndSend() {
    // TODO Auto-generated method stub
    
  }
}
