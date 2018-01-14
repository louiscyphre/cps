/**
 * 
 */
package cps.client.controller.customer;

import cps.api.request.LoginRequest;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllerConstants.InputVerification;
import cps.client.controller.ControllersClientAdapter;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends CustomerActionControllerBase {

  @FXML // fx:id="emailTextField"
  private TextField emailTextField; // Value injected by FXMLLoader

  @FXML // fx:id="passwordTextField"
  private PasswordField passwordTextField;

  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    if (ControllersClientAdapter.getCustomerContext().isLoggedIn()) {
      return;
    }
    validateAndSend();
  }

  private void validateAndSend() {
    String email = null;
    try {
      email = emailTextField.getText();
    } catch (NumberFormatException e) {
      displayError(InputVerification.MISSING_USERID.getMsg());
      return;
    }
    String password = null;
    try {
      password = passwordTextField.getText();
    } catch (NumberFormatException e) {
      displayError(InputVerification.MISSING_PASSWORD.getMsg());
      return;
    }
    if (!InputFormats.isValidEmailAddress(email)) {
      displayError(InputVerification.MISSING_EMAIL.getMsg());
      return;
    }

    ControllersClientAdapter.getCustomerContext().setPendingEmail(email);

    LoginRequest request = new LoginRequest(email, password);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert emailTextField != null : "fx:id=\"emailTextField\" was not injected: check your FXML file 'LoginScene.fxml'.";
    assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'LoginScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.LOGIN);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // input fields clear
    emailTextField.clear();
    passwordTextField.clear();
  }

}
