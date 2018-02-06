package cps.client.controller.service;

import cps.api.action.ServiceLoginAction;
import cps.api.response.ServiceLoginResponse;
import cps.client.controller.ControllerConstants.InputVerification;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.utils.FormatValidation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Controller class for Service Login. */
public class ServiceLoginSceneController extends ServiceActionControllerBase {
  @FXML // fx:id="usernameTF"
  private TextField usernameTF; // Value injected by FXMLLoader

  @FXML // fx:id="passwordTF"
  private TextField passwordTF; // Value injected by FXMLLoader

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert usernameTF != null : "fx:id=\"usernameTF\" was not injected: check your FXML file 'ServiceLoginScene.fxml'.";
    assert passwordTF != null : "fx:id=\"passwordTF\" was not injected: check your FXML file 'ServiceLoginScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_LOGIN);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.service.ServiceActionControllerBase#validateAndSend() */
  @Override
  void validateAndSend() {
    String username = null;
    try {
      username = getUsername();
    } catch (NumberFormatException e) {
      displayError(InputVerification.MISSING_USERID.getMsg());
      return;
    }
    String password = null;
    try {
      password = getPassword();
    } catch (NumberFormatException e) {
      displayError(InputVerification.MISSING_PASSWORD.getMsg());
      return;
    }
    if (!FormatValidation.InputFormats.USERNAME.validate(username)) {
      displayError(FormatValidation.InputFormats.USERNAME.errorMsg());
      return;
    }

    ServiceLoginAction loginRequest = new ServiceLoginAction(username, password);
    ControllersClientAdapter.getClient().sendRequest(loginRequest);
  }

  /** @return */
  private String getPassword() {
    return passwordTF.getText();
  }

  /** @return */
  private String getUsername() {
    return usernameTF.getText();
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl() */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    usernameTF.clear();
    passwordTF.clear();
  }

  /** Handles the response from the server , displays error is request failed.*/
  @Override
  public void handle(ServiceLoginResponse response) {
    ControllersClientAdapter.getEmployeeContext().setCompanyPerson(response.getUser());
    turnProcessingStateOff();

    if (response.success()) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
    } else {
      displayError(response.getDescription());
    }
  }
}
