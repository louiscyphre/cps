package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServiceActionReserveSlotController extends ServiceActionControllerBase {
  @FXML // fx:id="columnITF"
  private TextField columnITF; // Value injected by FXMLLoader

  @FXML // fx:id="floorJTF"
  private TextField floorJTF; // Value injected by FXMLLoader

  @FXML // fx:id="rowKTF"
  private TextField rowKTF; // Value injected by FXMLLoader

  @FXML
  void handleOkButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.initialize();
    assert columnITF != null : "fx:id=\"columnITF\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    assert floorJTF != null : "fx:id=\"floorJTF\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    assert rowKTF != null : "fx:id=\"rowKTF\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_RESERVE_SLOT);

  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    columnITF.clear();
    floorJTF.clear();
    rowKTF.clear();
  }
}
