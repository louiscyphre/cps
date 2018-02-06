package cps.client.controller.service;

import cps.api.action.InitLotAction;
import cps.api.response.InitLotResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.people.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Init Lot Scene controller class. */
public class ServiceActionInitLotSceneController extends ServiceActionControllerBaseSubmitAndFinish {
  /** Street Address TextField */
  @FXML // fx:id="streetAddressTF"
  private TextField streetAddressTF; // Value injected by FXMLLoader

  /** Lot Size TextField */
  @FXML // fx:id="lotSizeTF"
  private TextField lotSizeTF; // Value injected by FXMLLoader

  /** Incidental tariff TextField */
  @FXML // fx:id="incidentalTariffTF"
  private TextField incidentalTariffTF; // Value injected by FXMLLoader

  /** Reserved tariff TextField */
  @FXML // fx:id="reservedTariffTF"
  private TextField reservedTariffTF; // Value injected by FXMLLoader

  /** RobotIP TextField */
  @FXML // fx:id="robotIpTF"
  private TextField robotIpTF; // Value injected by FXMLLoader

  /* (non-Javadoc)
   * @see
   * cps.client.controller.service.ServiceActionControllerBase#validateAndSend() */
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

  /** Handles the response from server regarding lot initialization, displays error message from server if request failed. */
  @Override
  public void handle(InitLotResponse response) {
    if (response.success()) {
      setFinishInsteadOfSubmit(true);
    }
    super.handleGenericResponse(response);
  }

  /** Initializes the Controller and Registers it. */
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

  /* (non-Javadoc)
   * @see
   * cps.client.controller.service.ServiceActionControllerBaseSubmitAndFinish#
   * cleanCtrl() */
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
