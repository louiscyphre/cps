/**
 * 
 */
package cps.client.controller.customer;

import java.util.List;

import cps.api.request.LoginRequest;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created on: 2018-01-06 6:43:00 PM
 */
public class LoginController implements ViewController {

  @FXML // fx:id="emailTextField"
  private TextField emailTextField; // Value injected by FXMLLoader

  @FXML // fx:id="passwordTextField"
  private TextField passwordTextField;

  @FXML
  private VBox infoBox;

  @FXML
  private Label infoLabel;

  @FXML
  private ProgressIndicator infoProgress;

  @FXML
  void handleSubmitButton(ActionEvent event) {

    String email = null;
    try {
      email = emailTextField.getText();
    } catch (NumberFormatException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_USERID.getMsg());
      return;
    }
    String password = null;
    try {
      password = passwordTextField.getText();
    } catch (NumberFormatException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_PASSWORD.getMsg());
      return;
    }
    if (!InputFormats.isValidEmailAddress(email)) {
      // displayError(InputValidation.BAD_EMAIL.getMsg());
      return;
    }

    LoginRequest request = new LoginRequest(email, password);
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
  }
  /*
   * (non-Javadoc)
   * @see cps.client.controller.Notification#displayInfo(java.lang.String)
   */
  // @Override
  // public void displayInfo(String infoMsg) {
  // TODO Auto-generated method stub

  // }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.Notification#displayError(java.lang.String)
   */
  // @Override
  // public void displayError(String errorMsg) {
  // TODO Auto-generated method stub

  // }

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
    // TODO Auto-generated method stub

  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnProcessingStateOn() {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnProcessingStateOff() {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayInfo(String simpleInfoMsg) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void displayError(String simpleErrorMsg) {
    // TODO Auto-generated method stub
    
  }

}
