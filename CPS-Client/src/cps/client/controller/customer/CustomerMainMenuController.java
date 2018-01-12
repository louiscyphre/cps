/**
 * 
 */
package cps.client.controller.customer;

import java.util.List;
import java.util.ResourceBundle;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created on: 2018-01-08 11:17:39 PM
 */
/**
 * Created on: 2018-01-08 11:41:25 PM
 */
public class CustomerMainMenuController implements ViewController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private VBox infoBox;

  @FXML
  private Label infoLabel;;

  @FXML
  void handleLoginButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.LOGIN);
  }

  @FXML
  void handleReserveParkingButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.RESERVE_PARKING);
  }

  @FXML
  void handleBuySubscriptionButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
  }

  @FXML
  void handleParkNowButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ALPHA_INCIDENTAL_PARKING);
  }

  @FXML
  void initialize() {
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file"
        + ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU;
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file"
        + ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU;
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  // TODO @Michael check this idea out
  @Override
  public void displayInfo(List<Text> formattedText) {
    // TODO change the layout element to text flow
  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    // TODO change the layout element to text flow
  }

  @Override
  public void turnProcessingStateOn() {
    throw new UnsupportedOperationException(); // TODO check if better throw
                                               // exception or leave stub
  }

  @Override
  public void turnProcessingStateOff() {
    throw new UnsupportedOperationException(); // TODO check if better throw
                                               // exception or leave stub
  }

  @Override
  public void displayInfo(String simpleInfoMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayError(String simpleErrorMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnLoggedInStateOn() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void turnLoggedInStateOff() {
    // TODO Auto-generated method stub
    
  }

}
