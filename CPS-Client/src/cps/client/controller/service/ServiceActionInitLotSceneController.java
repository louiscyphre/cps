package cps.client.controller.service;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

//TODO stub - need to implement the initialization request and all
public class ServiceActionInitLotSceneController extends ServiceActionControllerBase {
  @FXML // fx:id="streetAddressTF"
  private TextField streetAddressTF; // Value injected by FXMLLoader

  @FXML // fx:id="lotSizeTF"
  private TextField lotSizeTF; // Value injected by FXMLLoader

  @FXML // fx:id="incidentalTariffTF"
  private TextField incidentalTariffTF; // Value injected by FXMLLoader

  @FXML // fx:id="reservedTariffTF"
  private TextField reservedTariffTF; // Value injected by FXMLLoader

  @FXML // fx:id="robotIpTF"
  private TextField robotIpTF; // Value injected by FXMLLoader

  @FXML
  void handleOkButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.initialize();
    assert streetAddressTF != null : "fx:id=\"streetAddressTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert lotSizeTF != null : "fx:id=\"lotSizeTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert incidentalTariffTF != null : "fx:id=\"incidentalTariffTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert reservedTariffTF != null : "fx:id=\"reservedTariffTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert robotIpTF != null : "fx:id=\"robotIpTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_INIT_LOT);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    streetAddressTF.clear();
    lotSizeTF.clear();
    incidentalTariffTF.clear();
    reservedTariffTF.clear();
    robotIpTF.clear();
  }
}
