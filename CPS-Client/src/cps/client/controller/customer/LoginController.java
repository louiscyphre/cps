/**
 * 
 */
package cps.client.controller.customer;

import java.util.List;

import cps.api.request.LoginRequest;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllerConstants.InputVerification;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LoginController implements ViewController {

  private boolean             processing         = false;

  @FXML // fx:id="emailTextField"
  private TextField emailTextField; // Value injected by FXMLLoader

  @FXML // fx:id="passwordTextField"
  private PasswordField passwordTextField;

  @FXML
  private VBox infoBox;

  @FXML
  private TextFlow infoLabel;

  @FXML
  private ProgressIndicator infoProgress;

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
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
  }

  @FXML
  void initialize() {
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'LoginScene.fxml'.";
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'LoginScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'LoginScene.fxml'.";
    assert emailTextField != null : "fx:id=\"emailTextField\" was not injected: check your FXML file 'LoginScene.fxml'.";
    assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'LoginScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.LOGIN);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  @Override
  public void displayInfo(List<Text> formattedText) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formattedText) {
      infoLabel.getChildren().add(ft);
    }
  }

  @Override
  public void displayInfo(String simpleInfoMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleInfoMsg));
  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formettedErrorMsg) {
      infoLabel.getChildren().add(ft);
    }
  }

  @Override
  public void displayError(String simpleErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleErrorMsg));
  }

  @Override
  public void turnProcessingStateOn() {
    infoProgress.visibleProperty().set(true);
    Text text = new Text("Processing...");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(text);
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("processingLabel");
    processing = true;
  }

  @Override
  public void turnProcessingStateOff() {
    infoProgress.visibleProperty().set(false);
    processing = false;
  }

  @Override
  public void turnLoggedInStateOn() {
    // view does not change
  }

  @Override
  public void turnLoggedInStateOff() {
    // view does not change
  }

  @Override
  public void cleanCtrl() {
    // info box clear
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoProgress.visibleProperty().set(false);
    infoLabel.getChildren().clear();
    // input fields clear
    emailTextField.clear();
    passwordTextField.clear();
  }

}
