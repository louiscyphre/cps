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

/**
 * Customer Login scene controller.
 */
public class LoginController extends CustomerActionControllerBase {

  /**
   * Email TextField
   */
  @FXML // fx:id="emailTextField"
  private TextField emailTextField; // Value injected by FXMLLoader

  /**
   * Password TextField
   */
  @FXML // fx:id="passwordTextField"
  private PasswordField passwordTextField;

  /**
   * Cancel Button
   */
  @FXML
  private Button cancelButton;

  /**
   * Submit button
   */
  @FXML
  private Button submitButton;

  /*
   * (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#
   * handleSubmitButton(javafx.event.ActionEvent)
   */
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

  /**
   * Validates that the fields and Sends API request to the server.
   */
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

  /**
   * Initializes the Controller and Registers it.
   */
  @FXML
  void initialize() {
    super.baseInitialize();
    assert emailTextField != null : "fx:id=\"emailTextField\" was not injected: check your FXML file 'LoginScene.fxml'.";
    assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'LoginScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.LOGIN);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
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

  /*
   * (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.
   * LoginResponse)
   */
  @Override
  public ServerResponse handle(LoginResponse response) {

    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (response.getStatus() == ServerResponse.STATUS_OK) {
      // Customer ID
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      // Making bold font
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));
      // Info message creation
      Text loginSuccessfulMessage = new Text("Login was successful");
      formattedMessage.add(loginSuccessfulMessage);
      formattedMessage.add(new Text("\n"));
      // Applying customer ID
      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      // Turning the LoggedIn state, application-wide
      ControllersClientAdapter.turnLoggedInStateOn();
      ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU, 10);
      // Turning off the processing state
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      // Turning off the processing state
      ctrl.turnProcessingStateOff();
      // Error message creation
      ctrl.displayError(response.getDescription());
    }

    return response;
  }
}
