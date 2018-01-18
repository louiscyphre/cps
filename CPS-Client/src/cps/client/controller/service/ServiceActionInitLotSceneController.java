package cps.client.controller.service;

import java.util.LinkedList;
import java.util.List;

import cps.api.action.InitLotAction;
import cps.api.response.InitLotResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.people.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

//TODO stub - need to implement the initialization request and all
public class ServiceActionInitLotSceneController extends ServiceActionControllerBaseSubmitAndFinish {
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

  @Override
  void validateAndSend() { 
    try {
      String streetAddress = requireFieldTrim(streetAddressTF, "Street Address");
      int lotSize = requireInteger(lotSizeTF, "Lot Width");
      float incidentalTariff = requireFloat(incidentalTariffTF, "Incidental Tariff");
      float reservedTariff = requireFloat(reservedTariffTF, "Reserved Tariff");
      String robotIp = requireFieldTrim(robotIpTF, "Robot IP");
  
      User user = ControllersClientAdapter.getEmployeeContext().requireCompanyPerson();
      sendRequest(new InitLotAction(user.getId(), streetAddress, lotSize, incidentalTariff, reservedTariff, robotIp));
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  @Override
  public ServerResponse handle(InitLotResponse response) {
    ServerResponse value = super.handleGenericResponse(response);
    
    if(response.success()) {
      List<Text> formattedMessage = new LinkedList<Text>();
      formattedMessage.add(new Text(response.getDescription() + "\n"));
      formattedMessage.add(new Text(String.format("New Lot ID: %s", response.getLotID())));
      turnProcessingStateOff();
      displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    }
    
    return value;
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
