/**
 * 
 */
package cps.client.controller.customer;

import java.util.LinkedList;
import java.util.List;

import cps.api.request.LoginRequest;
import cps.api.response.LoginResponse;
import cps.api.response.ServerResponse;
import cps.client.context.CustomerContext;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllerConstants.InputVerification;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginController extends CustomerActionControllerBase {
  @FXML
  protected Button cancelButton;
  
  @FXML
  protected Button submitButton;

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
    submitButton.setDisable(false);
    cancelButton.setDisable(false);
  }
  
  @Override
  public ServerResponse handle(LoginResponse response) {

    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (response.getStatus() == ServerResponse.STATUS_OK) {
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));

      Text loginSuccessfulMessage = new Text("Login was successful");
      formattedMessage.add(loginSuccessfulMessage);
      formattedMessage.add(new Text("\n"));

      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
      ctrl.displayInfo(formattedMessage);
      ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU, 10);
      ctrl.turnProcessingStateOff();
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      ControllersClientAdapter.getCurrentCtrl().turnProcessingStateOff();
      ctrl.displayError(response.getDescription());
    }

    return response;
  }
}
