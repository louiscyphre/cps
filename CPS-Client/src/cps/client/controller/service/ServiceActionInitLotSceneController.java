package cps.client.controller.service;

import cps.api.action.InitLotAction;
import cps.api.action.ServiceLoginAction;
import cps.api.response.InitLotResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ControllerConstants.InputVerification;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.utils.FormatValidation;
import cps.entities.people.User;
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
    if (processing) {
      return;
    }
    validateAndSend();

  }

  private void validateAndSend() {    
    try {
      String streetAddress = streetAddressTF.getText();
      int lotSize = Integer.parseInt(lotSizeTF.getText());
      float incidentalTariff = Float.parseFloat(incidentalTariffTF.getText());
      float reservedTariff = Float.parseFloat(reservedTariffTF.getText());
      String robotIp = robotIpTF.getText();
  
      User user = ControllersClientAdapter.getEmployeeContext().getCompanyPerson();
      InitLotAction action = new InitLotAction(user.getId(), streetAddress, lotSize, incidentalTariff, reservedTariff, robotIp);
      ControllersClientAdapter.getClient().sendRequest(action);
    } catch (Exception e) {
      displayError("Something is wrong with the input");
      // TODO: detailed input validation
    }
  }

  @Override
  public ServerResponse handle(InitLotResponse response) {
    if (response.success()) {
      turnProcessingStateOff();
      displayInfo(response.getDescription());
    } else {
      displayError(response.getDescription());
      turnProcessingStateOff();
    }
    
    return null;
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
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
